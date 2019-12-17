package me.wsman217.crazyreference.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.tools.FileManager;

public class ReferCommand implements CommandExecutor {

	// This is the main class for the command /refer

	CrazyReference plugin;

	// The max amount of referrals that a player can have at once
	int amountOfReference;

	public ReferCommand(CrazyReference plugin) {
		this.plugin = plugin;
		amountOfReference = plugin.getConfig().getInt("Settings.MaxAmountOfOpenReferences");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		// Check if the command executor is the console
		if (!(sender instanceof Player)) {
			sender.sendMessage(translateColors(
					plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.PlayersOnly")));
			return true;
		}

		// Else the sender is a player
		Player p = (Player) sender;

		// Check if the arguments of /refer are greater than or equal to 1
		if (args.length >= 1) {
			// Check for the "new" argument
			if (args[0].equalsIgnoreCase("new"))
				return onNewCommand(p, args);
			// Check for the "list" argument
			if (args[0].equalsIgnoreCase("list"))
				return onListCommand(p, args);
			// Check for the "del" argument
			if (args[0].equalsIgnoreCase("del"))
				return onDelCommand(p, args);
			// Check for the "reload" argument
			if (args[0].equalsIgnoreCase("reload"))
				return onReloadCommand(p, args);
			// If it is none of those send the help message
			else
				return onHelpCommand(p);
		} else
			// If the arguments are less than 1 send the help message
			return onHelpCommand(p);
	}

	private boolean onReloadCommand(Player p, String[] args) {

		// Check for the reload permission
		if (p.hasPermission("CrazyReference.reload")) {
			// Reload the plugin's main config
			plugin.reloadConfig();
			plugin.getFileManager().reloadAllFiles();
			p.sendMessage(translateColors(
					plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.ConfigsReloaded")));
			return true;
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	private boolean onHelpCommand(Player p) {

		// Check for the help permission
		if (p.hasPermission("CrazyReference.help")) {
			p.sendMessage(ChatColor.DARK_AQUA + "-----------CrazyReference Commands-----------");
			// If they do not have the permission for a command they will not be able to see
			// the command in the help message.
			if (p.hasPermission("CrazyReference.new"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer new <player name> - " + ChatColor.GRAY
						+ "adds that player to your referal list.");
			if (p.hasPermission("CrazyReference.del"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer del <player name> - " + ChatColor.GRAY
						+ "removes that player from your referal list.");
			if (p.hasPermission("CrazyReference.list"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer list - " + ChatColor.GRAY
						+ "lists all of the players you have refered.");
			if (p.hasPermission("CrazyReference.reload"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer reload - " + ChatColor.GRAY
						+ "reloads the CrazyReference config file.");
			if (p.hasPermission("CrazyReference.leaderboard"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer leaderboard <page> - " + ChatColor.GRAY
						+ "shows a leaderboard of everyones total referrals.");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer help - " + ChatColor.GRAY + "Shows this help menu");
			p.sendMessage(ChatColor.DARK_AQUA + "---------------------------------------------");
			return true;
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean onDelCommand(Player p, String[] args) {
		// Check for the del permission
		if (p.hasPermission("CrazyReference.del")) {
			// If the length of args is less than 2 send a little bit of context on how to
			// use the command to help the player out
			if (args.length < 2) {
				p.sendMessage(ChatColor.LIGHT_PURPLE + "To use this command use " + ChatColor.GRAY
						+ "/refer del <name in referal list>");
				p.sendMessage(ChatColor.LIGHT_PURPLE + "To see your referal list use the command " + ChatColor.GRAY
						+ "/refer list");
				return true;
			}
			// This will be the player to be removed from the player's referal list
			String nameToRemove = args[1];
			OfflinePlayer player = Bukkit.getOfflinePlayer(nameToRemove);

			// Check if the player has a data file made by CrazyReference
			if (!plugin.cMan.hasDataFile(p.getUniqueId())) {
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("General.YouHaveNotReferredAnyoneYet")));
				return true;
			}

			// Try to remove nameToRemove from the player's data file
			if (plugin.cMan.removePlayerFromFile(p.getUniqueId(), player.getUniqueId()))
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.Del.PlayerRemoved")));
			else
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.Del.NotInReferalList")));
			return true;
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	private boolean onListCommand(Player p, String[] args) {
		// Check for the list permission
		if (p.hasPermission("CrazyReference.list")) {

			// This is a list of all players in the senders data file
			List<OfflinePlayer> playerList = plugin.cMan.getPlayerList(p.getUniqueId());

			// Check if playerList is empty basically
			if (playerList == null) {
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("General.YouHaveNotReferredAnyoneYet")));
				return true;
			}

			int count = 1;

			p.sendMessage(translateColors(
					plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("Commands.List.TopLine")));

			// Go through each of the players in playerList and send a message to the player
			for (OfflinePlayer player : playerList) {
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.List.ListPlayers").replaceAll("%count%", "" + count)
						.replaceAll("%player%", player.getName())));
				count++;
			}
			return true;
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean onNewCommand(Player p, String[] args) {
		// Check for the new permission
		if (p.hasPermission("CrazyReference.new")) {
			// Check the length of args is less than 2
			if (args.length < 2) {
				p.sendMessage(ChatColor.LIGHT_PURPLE + "To use this command please inclue a player name");
				p.sendMessage(ChatColor.LIGHT_PURPLE + "Ex: " + ChatColor.GRAY + "/refer new wsman217");
				return true;
			}

			// This is the name to be added to the player's referal list
			OfflinePlayer nameToAdd = Bukkit.getOfflinePlayer(args[1]);

			// Check if the name to be added has played before
			if (nameToAdd.hasPlayedBefore()) {
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.New.PlayerHasPlayedBefore")));
				return true;
			}

			// Check if the player has a data file
			if (!plugin.cMan.hasDataFile(p.getUniqueId())) {
				// Create a new data file
				plugin.cMan.createNewPlayer(p);
			}

			// Check if player is going to go over the referal limit specified in the config
			if (plugin.cMan.isOverReferalLimit(p.getUniqueId())) {
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.New.UnableToAddPlayer")));
				return true;
			}

			// Create a list of the data in the players file
			List<String> readPData = plugin.cMan.readPlayerData(p.getUniqueId(), nameToAdd.getUniqueId());

			// Check if the data is null
			// If it is not null check if the player has already tried to refer this player
			if (readPData != null) {
				if (!readPData.contains(nameToAdd.getUniqueId().toString())) {
					plugin.cMan.addToPlayerFile(p.getUniqueId(), nameToAdd.getUniqueId());
					p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
							.getString("Commands.New.PlayerAdded").replaceAll("%player%", args[1])));
					return true;
				}
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.New.AlreadyInList")));
				return true;
			} else {
				plugin.cMan.addToPlayerFile(p.getUniqueId(), nameToAdd.getUniqueId());
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.New.PlayerAdded").replaceAll("%player%", args[1])));
				return true;
			}
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	private String translateColors(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
