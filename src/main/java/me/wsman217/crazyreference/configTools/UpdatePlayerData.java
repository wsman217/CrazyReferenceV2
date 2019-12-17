package me.wsman217.crazyreference.configTools;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.wsman217.crazyreference.CrazyReference;

class UpdatePlayerData {

	int numbOfReferals;

	FileConfiguration config;

	UUID nameToChange;

	CrazyReference plugin;

	File file;

	public UpdatePlayerData(UUID fileName, UUID nameToChange, CrazyReference plugin) {

		this.nameToChange = nameToChange;
		this.plugin = plugin;

		file = new File(plugin.getDataFolder() + "/PlayerData/" + fileName.toString() + ".yml");
		config = YamlConfiguration.loadConfiguration(file);
	}

	public boolean addPlayer() {

		numbOfReferals = config.getInt("Referals.Amount") + 1;

		int turnTo = config.getInt("Referals.Amount") == 0 ? 1 : numbOfReferals;

		config.set("Referals.Amount", turnTo);

		Long time = System.currentTimeMillis();

		String pname = plugin.getServer().getOfflinePlayer(nameToChange).getName();

		if (config.getConfigurationSection("Referals." + numbOfReferals) == null) {
			config.createSection("Referals." + numbOfReferals);
			config.createSection("Referals." + numbOfReferals + ".Name");
			config.createSection("Referals." + numbOfReferals + ".Time");
			config.createSection("Referals." + numbOfReferals + ".PName");
		}

		config.set("Referals." + numbOfReferals + ".Name", nameToChange.toString());
		config.set("Referals." + numbOfReferals + ".Time", time);
		config.set("Referals." + numbOfReferals + ".PName", pname);

		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (config.getString("Referals." + numbOfReferals + ".Name").equalsIgnoreCase(nameToChange.toString())
				&& config.getLong("Referals." + numbOfReferals + ".Time") == time)
			return true;
		return false;
	}

	public boolean removePlayer() {

		String name;
		long time;
		String pname;
		boolean found = false;

		numbOfReferals = config.getInt("Referals.Amount");
		int total = numbOfReferals;

		for (int i = 1; i <= numbOfReferals; i++) {

			if (found == true) {
				name = config.getString("Referals." + i + ".Name");
				time = config.getLong("Referals." + i + ".Time");
				pname = config.getString("Referals." + i + ".PName");

				config.set("Referals." + i, null);

				config.set("Referals." + (i - 1) + ".Name", name);
				config.set("Referals." + (i - 1) + ".Time", time);
				config.set("Referals." + (i - 1) + ".PName", pname);
			}

			if (found == false) {
				if (config.getString("Referals." + i + ".Name").equals(nameToChange.toString())) {
					config.set("Referals.Amount", total == 0 ? 1 : (total - 1));
					found = true;
					config.set("Referals." + i, null);
				}
			}
		}
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return found;
	}
}