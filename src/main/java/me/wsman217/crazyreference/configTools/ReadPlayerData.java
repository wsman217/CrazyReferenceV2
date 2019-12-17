package me.wsman217.crazyreference.configTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.wsman217.crazyreference.CrazyReference;

class ReadPlayerData {

	int numbOfReferals;

	int totalReferals;

	FileConfiguration config;

	List<String> values = new ArrayList<String>();
	List<OfflinePlayer> referals = new ArrayList<OfflinePlayer>();

	CrazyReference plugin;

	public ReadPlayerData(UUID fileName, CrazyReference plugin) {
		this.plugin = plugin;

		File file = new File(plugin.getDataFolder() + "/PlayerData/" + fileName.toString() + ".yml");

		config = YamlConfiguration.loadConfiguration(file);

		totalReferals = config.getInt("Referals.Amount");
		numbOfReferals = totalReferals;
	}
	
	public boolean isOverLimit() {

		int limit = plugin.getConfig().getInt("Settings.MaxAmountOfOpenReferences");
		return limit > totalReferals ? false : true;
	}
	
	public List<String> findPlayer(UUID fileName, UUID nameToLookFor) {
		values.clear();
		for (int i = 1; i <= numbOfReferals; i++) {
			if (config.getString("Referals." + i + ".Name").equals(nameToLookFor.toString())) {
				values.add(0, config.getString("Referals." + i + ".Name"));
				values.add(1, Long.toString(config.getLong("Referals." + i + ".Time")));
				values.add(2, fileName.toString());
			}
		}
		return values;
	}

	public List<String> returnValues(UUID fileName) {
		values.clear();
		for (int i = 1; i <= numbOfReferals; i++) {
				values.add(0, config.getString("Referals." + i + ".Name"));
				values.add(1, Long.toString(config.getLong("Referals." + i + ".Time")));
				values.add(2, fileName.toString());
		}
		return values.isEmpty() ? null : values;
	}

	@SuppressWarnings("deprecation")
	public List<OfflinePlayer> getListOfPlayers() {

		referals.clear();

		for (int i = 0; i < totalReferals; i++) {
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(config.getString("Referals." + (i + 1) + ".PName"));
			referals.add((i), p);
		}
		return referals.isEmpty() ? null : referals;
	}
}
