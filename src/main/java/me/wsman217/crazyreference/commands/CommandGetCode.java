package me.wsman217.crazyreference.commands;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.refercode.GetCode;
import me.wsman217.crazyreference.tools.CommandTools;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandGetCode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (CommandTools.CommandCheck(sender, "crazyreference.commands.getcode")) return true;

        Player p = (Player) sender;

        GetCode.Code code = GetCode.getCode(p);

        if (code == null) {
            p.sendMessage(ChatColor.RED + "Something has gone very very wrong.");
            throw new NullPointerException("The code for player " + p.getUniqueId() + " was null.");
        }
        TextComponent message = new TextComponent("Your code is " + code.value + " click this message to get a version that is easily copiable.");
        message.setColor(net.md_5.bungee.api.ChatColor.LIGHT_PURPLE);
        message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code.value));
        p.spigot().sendMessage(message);

        CrazyReference.balanceHandler.contains(p);
        return true;
    }
}
