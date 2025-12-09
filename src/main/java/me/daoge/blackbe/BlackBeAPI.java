package me.daoge.blackbe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * BlackBE API client for querying player ban status
 */
public class BlackBeAPI {
    private static final String BLACKBE_API = "https://api.blackbe.work/openapi/v3/check?";
    private static final int STATUS_UNBAN = 2001;
    private static final int STATUS_BAN = 2000;

    private final OkHttpClient client;
    private final Logger logger;

    public BlackBeAPI(Logger logger) {
        this.client = new OkHttpClient();
        this.logger = logger;
    }

    /**
     * Query player status by name
     *
     * @param name Player name
     * @return CompletableFuture that resolves to BlackBeStatus or null if not banned
     */
    public CompletableFuture<BlackBeStatus> queryStatusByName(String name) {
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String url = BLACKBE_API + "name=" + encodedName;
        return queryStatus(url);
    }

    /**
     * Query player status by QQ number
     *
     * @param qq QQ number
     * @return CompletableFuture that resolves to BlackBeStatus or null if not banned
     */
    public CompletableFuture<BlackBeStatus> queryStatusByQQ(String qq) {
        String url = BLACKBE_API + "qq=" + qq;
        return queryStatus(url);
    }

    /**
     * Execute the actual HTTP request and parse response
     *
     * @param url Full API URL
     * @return CompletableFuture with the query result
     */
    private CompletableFuture<BlackBeStatus> queryStatus(String url) {
        CompletableFuture<BlackBeStatus> future = new CompletableFuture<>();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                logger.error("BlackBE API request failed: {}", e.getMessage());
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (response) {
                    if (!response.isSuccessful()) {
                        logger.error("BlackBE API error: HTTP {}", response.code());
                        future.complete(null);
                        return;
                    }

                    String responseBody = response.body().string();
                    JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();

                    int status = json.get("status").getAsInt();

                    // If status is UNBAN, player is not in blacklist
                    if (status == STATUS_UNBAN) {
                        future.complete(null);
                        return;
                    }

                    // Parse player data
                    JsonObject data = json.getAsJsonObject("data")
                            .getAsJsonArray("info")
                            .get(0)
                            .getAsJsonObject();

                    BlackBeStatus blackBeStatus = new BlackBeStatus(
                            data.get("name").getAsString(),
                            data.get("xuid").getAsString(),
                            data.get("info").getAsString(),
                            data.get("level").getAsInt(),
                            data.get("qq").getAsLong()
                    );

                    future.complete(blackBeStatus);

                } catch (Exception e) {
                    logger.error("Failed to parse BlackBE API response", e);
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }

    /**
     * Shutdown the HTTP client
     */
    public void shutdown() {
        client.dispatcher().executorService().close();
        client.connectionPool().evictAll();
    }
}
