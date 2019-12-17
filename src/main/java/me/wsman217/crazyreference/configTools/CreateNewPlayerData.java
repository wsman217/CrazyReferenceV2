package me.wsman217.crazyreference.configTools;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;

class CreateNewPlayerData {

	UUID playerID;
	String fileSeperator = System.getProperty("file.seperator");

	String absolutePath = "PlayerData\\";
	String changingPath;

	YamlConfiguration config;

	private File folder;

	CrazyReference plugin;

	public CreateNewPlayerData(Player p, CrazyReference plugin) {
		this.plugin = plugin;

		playerID = p.getUniqueId();
		changingPath = absolutePath + playerID.toString() + ".yml";
		folder = new File(plugin.getDataFolder(), changingPath);
		if (!folder.exists()) {
			folder.getParentFile().mkdirs();
		}
		try {
			folder.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		config = YamlConfiguration.loadConfiguration(folder);
		addSections();
		try {
			config.save(folder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addSections() {
		config.createSection("Referals");
		config.createSection("Referals.Amount");
		config.set("Referals.Amount", 0);
	}
}