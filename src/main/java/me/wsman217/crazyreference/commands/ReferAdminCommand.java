package me.wsman217.crazyreference.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.data.DataHandler;
import me.wsman217.crazyreference.tools.FileManager;
import me.wsman217.crazyreference.tools.PluginInfo;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ReferAdminCommand implements CommandExecutor {

	CrazyReference plugin;
	FileConfiguration messages;
	DataHandler dh;

	public ReferAdminCommand(CrazyReference plugin) {
		this.plugin = plugin;
		messages = plugin.getFileManager().getFile(FileManager.Files.MESSAGE);
		dh = plugin.getDataHandler();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!sender.hasPermission("CrazyReference.admin"))
			return sendNoPerms(sender);
		
		if (!plugin.getConfig().getBoolean("Settings.LeaderboardEnabled")) {
			sender.sendMessage(ChatColor.RED
					+ "Sorry this command has been disabled please contact the server administrators for help.");
			return true;
		}

		if (args.length < 1) {
			return sendInfoMsg(sender);
		}

		if (args[0].equalsIgnoreCase("help"))
			return sendHelpMsg(sender);
		if (args[0].equalsIgnoreCase("del"))
			return delCommand(sender, args);
		if (args[0].equalsIgnoreCase("mod"))
			return modCommand(sender, args);
		return false;
	}

	private boolean sendNoPerms(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("General.NoPerms")));
		return true;
	}

	private boolean sendHelpMsg(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "-------CrazyReference Admin Commands-------");
		sender.sendMessage(ChatColor.DARK_AQUA + "/referadmin help " + ChatColor.GRAY + "- Shows this help menu.");
		sender.sendMessage(ChatColor.DARK_AQUA + "/referadmin del <name> " + ChatColor.GRAY
				+ "- Delete a player from the CrazyReference leader board.");
		sender.sendMessage(ChatColor.DARK_AQUA + "/referadmin mod <name> <new total> " + ChatColor.GRAY
				+ "- Change or add a players total referrals on the CrazyReference leader board.");
		sender.sendMessage(ChatColor.DARK_AQUA + "ReferAdmin alias " + ChatColor.GRAY + "rfa");
		return true;
	}

	private boolean sendInfoMsg(CommandSender sender) {
		sender.sendMessage(ChatColor.DARK_AQUA + "-----------CrazyReference Info-----------");
		sender.sendMessage(ChatColor.DARK_AQUA + "Plugin Version: " + ChatColor.GRAY + PluginInfo.version);
		sender.sendMessage(ChatColor.DARK_AQUA + "bStats enabled: " + ChatColor.GRAY + PluginInfo.bStats);
		sender.sendMessage(ChatColor.DARK_AQUA + "Plugin Developer: " + ChatColor.GRAY + "wsman217");

		if (sender instanceof Player) {
			Player p = (Player) sender;
			TextComponent discord = new TextComponent("Support Discord");
			discord.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/veTQgXK"));
			discord.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("Join Discord to recieve help!").create()));
			discord.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
			p.spigot().sendMessage(discord);

			TextComponent spigot = new TextComponent("Source Code");
			spigot.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL,
					"https://github.com/wsman217/CrazyReference/tree/master"));
			spigot.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new ComponentBuilder("View the source code for the plugin").create()));
			spigot.setColor(net.md_5.bungee.api.ChatColor.DARK_AQUA);
			p.spigot().sendMessage(spigot);
		} else {
			sender.sendMessage(
					ChatColor.DARK_AQUA + "Support Discord: " + ChatColor.GRAY + "https://discord.gg/veTQgXK");
			sender.sendMessage(ChatColor.DARK_AQUA + "Source Code: " + ChatColor.GRAY
					+ " https://github.com/wsman217/CrazyReference/tree/master");
		}
		return true;
	}

	private boolean delCommand(CommandSender sender, String[] args) {
		if (args.length >= 2) {
			dh.deleteFromTable(args[1]);
			sender.sendMessage(ChatColor.DARK_AQUA + args[1] + " has been removed from the leader board");
		} else {
			sender.sendMessage(
					ChatColor.RED + "Please specify a player you would like to delte from the leader board.");
		}
		return true;
	}

	private boolean modCommand(CommandSender sender, String[] args) {
		if (args.length >= 3) {
			if (isNumeric(args[2])) {
				dh.changeTotal(args[1], Integer.parseInt(args[2]));
				sender.sendMessage(ChatColor.DARK_AQUA + args[1] + "'s total referrals has been set to " + args[2]);
			} else {
				sender.sendMessage(ChatColor.RED + "Please include all arguments to use this command. Ex: /rfa mod wsman217 10");
			}
		} else {
			sender.sendMessage(
					ChatColor.RED + "Please include all arguments to use this command. Ex: /rfa mod wsman217 10");
		}
		return true;
	}

	public boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
}
