package me.wsman217.crazyreference.commands;

import me.wsman217.crazyreference.tools.CommandTools;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandCrazyReference implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (CommandTools.CommandCheck(sender, ""))
            return true;

        return true;
    }
}
