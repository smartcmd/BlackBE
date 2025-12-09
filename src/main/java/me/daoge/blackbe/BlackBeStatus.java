package me.daoge.blackbe;

/**
 * Represents a player's BlackBE status information
 *
 * @param name  Player name
 * @param xuid  Player XUID
 * @param info  Ban reason/info
 * @param level Ban level
 * @param qq    Player QQ number
 */
public record BlackBeStatus(String name, String xuid, String info, int level, long qq) {
    /**
     * Format the status as a colored message
     */
    public String toFormattedMessage() {
        return String.format(
                "§aName: §f%s\n§aXuid: §f%s\n§aInfo: §f%s\n§aLevel: §f%d\n§aQQ: §f%d",
                name, xuid, info, level, qq
        );
    }
}
