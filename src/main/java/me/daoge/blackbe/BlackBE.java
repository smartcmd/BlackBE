package me.daoge.blackbe;

import lombok.Getter;
import org.allaymc.api.eventbus.EventHandler;
import org.allaymc.api.eventbus.event.server.PlayerJoinEvent;
import org.allaymc.api.eventbus.event.server.PlayerLoginEvent;
import org.allaymc.api.plugin.Plugin;
import org.allaymc.api.registry.Registries;
import org.allaymc.api.server.Server;

/**
 * BlackBE Plugin for Allay Server
 * Checks players against BlackBE database and prevents banned players from joining
 */
public class BlackBE extends Plugin {
    @Getter
    private BlackBeAPI api;

    @Override
    public void onLoad() {
        this.pluginLogger.info("BlackBE is loaded!");
    }

    @Override
    public void onEnable() {
        this.pluginLogger.info("BlackBE is enabled!");

        // Initialize API client
        this.api = new BlackBeAPI(this.pluginLogger);

        // Register event listener
        Server.getInstance().getEventBus().registerListener(this);

        // Register command
        Registries.COMMANDS.register(new BlackBeCommand(this));

        this.pluginLogger.info("BlackBE command registered!");
    }

    @Override
    public void onDisable() {
        this.pluginLogger.info("BlackBE is disabled!");

        // Shutdown API client
        if (this.api != null) {
            this.api.shutdown();
        }
    }

    /**
     * Handle player login event - check if player is banned in BlackBE database
     */
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        var player = event.getPlayer();
        String playerName = player.getOriginName();

        // Query BlackBE database asynchronously
        api.queryStatusByName(playerName).whenComplete((status, throwable) -> {
            if (throwable != null) {
                pluginLogger.error("Failed to check player {} against BlackBE: {}",
                    playerName, throwable.getMessage());
                return;
            }

            if (status != null) {
                // Player is banned - kick them
                event.setCancelled(true);
                event.setDisconnectReason("§cYou are recorded in BlackBE!");

                pluginLogger.warn("§cPlayer {} failed BlackBE check!", playerName);
                pluginLogger.info("Ban info: {}", status.toString());
            } else {
                // Player is not banned
                pluginLogger.info("§aPlayer {} passed BlackBE check!", playerName);
            }
        });
    }
}
