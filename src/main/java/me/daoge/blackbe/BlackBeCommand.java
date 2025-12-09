package me.daoge.blackbe;

import org.allaymc.api.command.Command;
import org.allaymc.api.command.CommandSender;
import org.allaymc.api.command.tree.CommandTree;

import java.util.function.BiConsumer;

/**
 * Main BlackBE command with subcommands for querying player ban status
 * Usage:
 *   /blackbe name <player_name> - Query by player name
 *   /blackbe qq <qq_number> - Query by QQ number
 */
public class BlackBeCommand extends Command {
    private final BlackBE plugin;

    public BlackBeCommand(BlackBE plugin) {
        super("blackbe", "Query player ban status from BlackBE database", "blackbe.command");
        this.plugin = plugin;
    }

    @Override
    public void prepareCommandTree(CommandTree tree) {
        // Branch 1: /blackbe name <name>
        tree.getRoot()
                .key("name")
                .str("player")
                .permission("blackbe.command.name")
                .exec(context -> {
                    // Check permission
                    var sender = context.getSender();
                    String playerName = context.getResult(1);

                    sender.sendMessage("§7Querying BlackBE database...");

                    // Query asynchronously
                    plugin.getApi().queryStatusByName(playerName).whenComplete(handleBlackBeStatus(sender));
                    return context.success();
                });

        // Branch 2: /blackbe qq <qq>
        tree.getRoot()
                .key("qq")
                .str("qq")
                .permission("blackbe.command.qq")
                .exec(context -> {
                    // Check permission
                    var sender = context.getSender();
                    String qq = context.getResult(1);

                    sender.sendMessage("§7Querying BlackBE database...");

                    // Query asynchronously
                    plugin.getApi().queryStatusByQQ(qq).whenComplete(handleBlackBeStatus(sender));
                    return context.success();
                });
    }

    private static BiConsumer<BlackBeStatus, Throwable> handleBlackBeStatus(CommandSender sender) {
        return (status, throwable) -> {
            if (throwable != null) {
                sender.sendMessage("§cError querying BlackBE API: " + throwable.getMessage());
                return;
            }

            if (status == null) {
                sender.sendMessage("§aPlayer isn't found in BlackBE database");
            } else {
                sender.sendMessage("§ePlayer is found in BlackBE database!");
                sender.sendMessage(status.toFormattedMessage());
            }
        };
    }
}
