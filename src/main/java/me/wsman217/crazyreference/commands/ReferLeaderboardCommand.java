package me.wsman217.crazyreference.commands;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.data.DataHandler;
import me.wsman217.crazyreference.data.LeaderboardStorage;
import me.wsman217.crazyreference.tools.FileManager;

public class ReferLeaderboardCommand implements CommandExecutor {

	CrazyReference plugin = CrazyReference.getInstance();

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
			if (args[0].equalsIgnoreCase("leaderboard") || args[0].equalsIgnoreCase("lb"))
				return onLeaderboardCommand(p, args);
			if (args[0].equalsIgnoreCase("rank"))
				return onRankCommand(p, args);
			if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?"))
				return onHelpCommand(p);
			// If it is none of those send the help message
			else {
				return onLeaderboardCommand(p, args);
			}
		} else
			// If the arguments are less than 1 send the help message
			return onLeaderboardCommand(p, args);
	}

	private boolean onHelpCommand(Player p) {

		// Check for the help permission
		if (p.hasPermission("CrazyReference.Leaderboard.help")) {
			p.sendMessage(ChatColor.DARK_AQUA + "-----CrazyReference Leaderboard Commands-----");
			// If they do not have the permission for a command they will not be able to see
			// the command in the help message.
			if (p.hasPermission("CrazyReference.Leaderboard"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/referleaderboard <page> - " + ChatColor.GRAY
						+ "Show a leaderboard of top referrals. Alias: [rflb]");
			if (p.hasPermission("CrazyReference.Leaderboard.Rank"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer rank - " + ChatColor.GRAY
						+ "Shows where you are on the leaderboard");
			if (p.hasPermission("CrazyReference.Leaderboard.RankOthers"))
				p.sendMessage(ChatColor.LIGHT_PURPLE + "/refer rank <player>- " + ChatColor.GRAY
						+ "Shows someone else's rank on the leaderboard.");
			p.sendMessage(ChatColor.LIGHT_PURPLE + "/rflb help - " + ChatColor.GRAY + "Shows this help menu");
			p.sendMessage(ChatColor.DARK_AQUA + "-----------------------------------");
			return true;
		}
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	private boolean onLeaderboardCommand(Player p, String[] args) {

		if (!p.hasPermission("CrazyReference.leaderboard")) {
			return noPerms(p);
		}

		DataHandler dh = plugin.getDataHandler();
		ArrayList<LeaderboardStorage> leader = dh.getLeaderboard();
		int page = 1;
		int size = (leader.size() / 10) + 1;

		NumericCheck:
		if (args.length >= 1) {
			if (!isNumeric(args[0])) {
				break NumericCheck;
			}
			page = Integer.parseInt(args[0]);
		}

		if (page > size) {
			page = size;
		}

		p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
				.getString("Commands.ReferLeaderboard.Leaderboard.TopMessage")));
		int count = (page * 10) - 10;
		Iterator<LeaderboardStorage> iterate = leader.iterator();
		for (int i = 0; i < count; i++) {
			if (iterate.hasNext())
				iterate.next();
		}

		for (int i = 0; i < 10; i++) {
			if (iterate.hasNext()) {
				LeaderboardStorage lbs = iterate.next();
				p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getString("Commands.ReferLeaderboard.Leaderboard.LeaderMessage")
						.replaceAll("%place%", "" + (count + i + 1)).replaceAll("%player_name%", lbs.getPlayerName())
						.replaceAll("%referrals%", "" + lbs.getTotalReferrals())));
			}
		}
		p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
				.getString("Commands.ReferLeaderboard.Leaderboard.PageMessage").replaceAll("%page%", "" + page)
				.replaceAll("%size%", "" + size)));
		return true;
	}

	private boolean onRankCommand(Player p, String[] args) {

		if (!p.hasPermission("CrazyReference.Leaderboard.Rank"))
			return noPerms(p);

		DataHandler dh = plugin.getDataHandler();
		ArrayList<LeaderboardStorage> leader = dh.getLeaderboard();

		// If command is just /rflb rank
		if (args.length == 1) {

			Iterator<LeaderboardStorage> iterate = leader.iterator();
			for (int i = 1; i <= leader.size(); i++) {
				if (iterate.hasNext()) {
					LeaderboardStorage lbs = iterate.next();

					if (lbs.getPlayerName().equalsIgnoreCase(p.getDisplayName())) {
						p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
								.getString("Commands.ReferLeaderboard.Rank.OnLb.PersonalTopMessage")));
						p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
								.getString("Commands.ReferLeaderboard.Rank.OnLb.RankMessage")
								.replaceAll("%player_name%", lbs.getPlayerName())
								.replaceAll("%referrals%", "" + lbs.getTotalReferrals())
								.replaceAll("%place%", "" + i)));
						return true;
					}
				}
			}
			p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
					.getString("Commands.ReferLeaderboard.Rank.YouAreNotOnLb")));
			return true;
		}

		if (!p.hasPermission("CrazyReference.Leaderboard.RankOthers"))
			return noPerms(p);

		String player = args[1];
		Iterator<LeaderboardStorage> iterate = leader.iterator();
		for (int i = 1; i <= leader.size(); i++) {
			if (iterate.hasNext()) {
				LeaderboardStorage lbs = iterate.next();
				if (lbs.getPlayerName().equalsIgnoreCase(player)) {
					p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
							.getString("Commands.ReferLeaderboard.Rank.OnLb.OtherTopMessage").replaceAll("%player_name%", lbs.getPlayerName())));
					p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
							.getString("Commands.ReferLeaderboard.Rank.OnLb.RankMessage")
							.replaceAll("%player_name%", lbs.getPlayerName())
							.replaceAll("%referrals%", "" + lbs.getTotalReferrals()).replaceAll("%place%", "" + i)));
					return true;
				}
			}
		}
		p.sendMessage(translateColors(plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
				.getString("Commands.ReferLeaderboard.Rank.OtherNotOnLb")));
		return true;
	}

	private boolean noPerms(Player p) {
		p.sendMessage(translateColors(
				plugin.getFileManager().getFile(FileManager.Files.MESSAGE).getString("General.NoPerms")));
		return true;
	}

	private String translateColors(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
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
