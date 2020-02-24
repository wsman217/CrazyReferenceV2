package me.wsman217.crazyreference.commands;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.refercode.GetCode;
import me.wsman217.crazyreference.tools.CommandTools;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

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
        int playTime = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        double minutesPlayed = (((double) playTime) / 20d) / 60d;
        //TODO make the time configurable
        if (minutesPlayed > 15) {
            //TODO add message.yml
            p.sendMessage(ChatColor.RED + "You can not use this command if you have played more than 15 minutes!");
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

        ArrayList<String> ipsOfInviter = CrazyReference.ipHandler.findById(inviter.getUniqueId());
        ArrayList<String> ipsOfUser = CrazyReference.ipHandler.findById(p.getUniqueId());

        boolean isGood = true;
        invLoop: for (String ipInv: ipsOfInviter)
            for (String ipUse : ipsOfUser)
                if (ipInv.equals(ipUse)) {
                    isGood = false;
                    break invLoop;
                }

        if (!isGood) {
            //TODO add message.yml
            p.sendMessage(ChatColor.RED + "The user that referred you has already played on this ip address.");
            return true;
        }

        //TODO add code for the prizes for now we will just send them a message
        p.sendMessage(ChatColor.AQUA + "SUCCESS!");
        return true;
    }
}
