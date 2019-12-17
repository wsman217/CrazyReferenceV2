package me.wsman217.crazyreference.listeners;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.tools.PluginInfo;
import net.md_5.bungee.api.ChatColor;

public class OnMeJoin implements Listener {

	CrazyReference plugin;

	public OnMeJoin(CrazyReference plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onMeJoinEvent(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (p.getUniqueId().equals(UUID.fromString("53c178a3-be85-49dd-9001-969ac86068d2"))) {
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				p.sendMessage("");
				p.sendMessage(ChatColor.BLUE + "This server is using your plugin " + PluginInfo.name + ".");
				p.sendMessage(ChatColor.BLUE + "The version is " + PluginInfo.version + ".");
				p.sendMessage(ChatColor.BLUE + "And bStats is " + PluginInfo.bStats + ".");
				p.sendMessage("");
			}, 20);
		}
	}
}
