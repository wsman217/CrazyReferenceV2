package me.wsman217.crazyreference.tools;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandTools {
    public static boolean CommandCheck(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by a player!");
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission(permission)) {
            //todo add a messages.yml so you can change this.
            p.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }
        return false;
    }
}
