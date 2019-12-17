package me.wsman217.crazyreference.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.tools.FileManager;
import net.md_5.bungee.api.ChatColor;

@SuppressWarnings("deprecation")
public class ListenerPlayerJoin implements Listener {

	CrazyReference plugin;

	HashMap<UUID, UUID> inConfirm = new HashMap<UUID, UUID>();

	public ListenerPlayerJoin(CrazyReference plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {

		if (e.getPlayer().hasPlayedBefore() == false) {
			Player p = e.getPlayer();

			UUID pUUID = p.getUniqueId();

			List<String> variables = new ArrayList<String>();

			for (Player players : plugin.getServer().getOnlinePlayers()) {

				if (plugin.cMan.hasDataFile(players.getUniqueId())) {
					List<String> pData = plugin.cMan.readPlayerData(players.getUniqueId(), pUUID);
					if (pData != null) {
						variables.addAll(pData);
					}
				}
			}

			List<String> earliest = getBiggest(variables);

			if (earliest != null) {
				variables.removeAll(earliest);

				inConfirm.put(UUID.fromString(earliest.get(1)), UUID.fromString(earliest.get(2)));

				Bukkit.getScheduler().runTaskLater(plugin, () -> {

					for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
							.getStringList("NewPlayerJoin.PlayerJoinedAskIfRefered")) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replaceAll("%player%",
								plugin.getServer().getPlayer(UUID.fromString(earliest.get(2))).getName())));
					}
				}, 20);
			}
		}
	}

	@EventHandler
	public void onPlayerChatEvent(PlayerChatEvent e) {
		if (inConfirm.containsKey(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);

			String message = e.getMessage();

			if (message.startsWith("y") || message.startsWith("Y")) {

				for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getStringList("NewPlayerJoin.WasReferred")) {
					ChatColor.translateAlternateColorCodes('&', s);
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
				}

				OfflinePlayer p = plugin.getServer().getOfflinePlayer(inConfirm.get(e.getPlayer().getUniqueId()));
				plugin.getDataHandler().updateOnReferredJoin((Player) p);

				plugin.giveRewards.giveRewards(e.getPlayer(),
						plugin.getServer().getPlayer(inConfirm.get(e.getPlayer().getUniqueId())));
				inConfirm.remove(e.getPlayer().getUniqueId());
				return;
			} else if (message.startsWith("n") || message.startsWith("N")) {

				for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
						.getStringList("NewPlayerJoin.WasNotReferred")) {
					e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
				}
				inConfirm.remove(e.getPlayer().getUniqueId());
				return;
			}
			for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
					.getStringList("NewPlayerJoin.UnableToSendMessage")) {
				e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', s));
			}
		}
	}

	public List<String> getBiggest(List<String> variables) {
		Long earliestTime;
		Player oneRefered;
		Player oneThatRefered;

		List<String> biggest = new ArrayList<String>();

		if (variables.size() != 0) {
			if (variables.size() > 3 || (variables.size() % 3) == 0) {
				earliestTime = Long.parseLong(variables.get(1));
				oneRefered = Bukkit.getPlayer(UUID.fromString(variables.get(0)));
				oneThatRefered = Bukkit.getPlayer(UUID.fromString(variables.get(2)));

				for (int i = 1; i <= variables.size(); i += 3) {
					if (earliestTime > Long.parseLong(variables.get(i))) {
						earliestTime = Long.parseLong(variables.get(i));
						oneRefered = Bukkit.getPlayer(UUID.fromString(variables.get(i - 1)));
						oneThatRefered = Bukkit.getPlayer(UUID.fromString(variables.get(i + 1)));
					}
				}

				if (earliestTime != null && oneRefered != null && oneThatRefered != null) {
					biggest.add(0, earliestTime.toString());
					biggest.add(1, oneRefered.getUniqueId().toString());
					biggest.add(2, oneThatRefered.getUniqueId().toString());
				}
			}
		}
		return biggest.size() == 0 ? null : biggest;
	}
}
