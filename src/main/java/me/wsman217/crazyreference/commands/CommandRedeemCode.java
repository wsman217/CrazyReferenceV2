package me.wsman217.crazyreference.commands;

import me.wsman217.crazyreference.refercode.GetCode;
import me.wsman217.crazyreference.tools.CommandTools;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRedeemCode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (CommandTools.CommandCheck(sender, "crazyreference.commands.redeemcode"))
            return true;
        Player p = (Player) sender;
        if (args.length < 1) {
            //TODO add message.yml
            p.sendMessage(ChatColor.RED + "More arguments are required for this command.");
            return true;
        }
        Player inviter = GetCode.getPlayer(args[0]);
        if (inviter == null) {
            //TODO add message.yml
            p.sendMessage(ChatColor.RED + "This code is not valid!");
            return true;
        }
        if (inviter.getUniqueId().compareTo(p.getUniqueId()) == 0) {
            //TODO add message.yml
            p.sendMessage(ChatColor.RED + "You can not use your own code!");
            return true;
        }
        //TODO add code for the prizes for now we will just send them a message
        p.sendMessage(ChatColor.AQUA + "SUCCESS!");
        return true;
    }
}
