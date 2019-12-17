package me.wsman217.crazyreference.configTools;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;

public class configManager {

	CrazyReference plugin;

	CreateNewPlayerData newPData;
	UpdatePlayerData updatePFile;
	ReadPlayerData rPData;

	/**
	 * Initialize this class
	 * @param plugin
	 */
	public configManager(CrazyReference plugin) {
		this.plugin = plugin;
	}

	/**
	 * Create a new data file for that player
	 * @param p
	 */
	public void createNewPlayer(Player p) {
		newPData = new CreateNewPlayerData(p, plugin);
	}

	/**
	 * Add a name to a player's file
	 * @param playerFile
	 * @param nameToAdd
	 * @return true if the name was successfully added to the player's file; false otherwise
	 */
	public boolean addToPlayerFile(UUID playerFile, UUID nameToAdd) {
		updatePFile = new UpdatePlayerData(playerFile, nameToAdd, plugin);
		return updatePFile.addPlayer();
	}

	/**
	 * Remove a name from a player's data file
	 * @param playerFile
	 * @param nameToRemove
	 * @return true if the name was successfully removed to the player's file; false otherwise
	 */
	public boolean removePlayerFromFile(UUID playerFile, UUID nameToRemove) {
		updatePFile = new UpdatePlayerData(playerFile, nameToRemove, plugin);
		return updatePFile.removePlayer();
	}

	/**
	 * Get all of the information in a player's data file
	 * @param playerFile
	 * @param nameToLookFor
	 * @return List<String> of all the information if there is nothing in it, it will return null
	 */
	public List<String> readPlayerData(UUID playerFile, UUID nameToLookFor) {
		rPData = new ReadPlayerData(playerFile, plugin);
		return rPData.findPlayer(playerFile, nameToLookFor);
	}

	/**
	 * Get a list of only player's from a data file
	 * @param playerFile
	 * @return List<OfflinePlayer> of all the players inside a data file
	 */
	public List<OfflinePlayer> getPlayerList(UUID playerFile) {
		rPData = new ReadPlayerData(playerFile, plugin);
		return rPData.getListOfPlayers();
	}

	/**
	 * Check if a player is over the referal limit
	 * @param playerFile
	 * @return true if the player is over; false otherwise
	 */
	public boolean isOverReferalLimit(UUID playerFile) {
		rPData = new ReadPlayerData(playerFile, plugin);
		return rPData.isOverLimit();
	}

	/**
	 * Check if a player has a data file
	 * @param player
	 * @return true if the player has a data file; false otherwise
	 */
	public boolean hasDataFile(UUID player) {
		File file = new File(plugin.getDataFolder() + "/PlayerData/" + player.toString() + ".yml");

		if (file.exists())
			return true;
		else
			return false;
	}
}