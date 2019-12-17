package me.wsman217.crazyreference.tools;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.wsman217.crazyreference.CrazyReference;

public class GiveRewards {

	CrazyReference plugin;
	FileConfiguration config;

	public GiveRewards(CrazyReference plugin) {
		this.plugin = plugin;
		config = plugin.getConfig();
	}

	public void giveRewards(Player referee, Player referer) {
		plugin.cMan.removePlayerFromFile(referer.getUniqueId(), referee.getUniqueId());
		refereeReward(referee);
		refererReward(referer);
	}

	private void refereeReward(Player referee) {

		String path = "Rewards.OneBeingRefered";
		String path2 = "Rewards.OneBeingRefered.Items";
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for (String s : config.getConfigurationSection(path2).getKeys(false)) {
			String itemPath = path2 + "." + s;

			ItemStack reward = new ItemStack(Material.matchMaterial(config.getString(itemPath + ".Material")),
					config.getInt(itemPath + ".Amount"));

			ItemMeta meta = reward.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemPath + ".Name")));
			meta.setLore(getLore(itemPath));

			for (String t : config.getStringList(itemPath + ".Enchants")) {
				if (getEnchantFromString(t) == null) {
					plugin.getLogger().log(Level.WARNING, "Enchantment is null for path " + path2 + ".Enchants");
					continue;
				}
				meta.addEnchant(getEnchantFromString(t), getLevelFromString(t), true);
			}
			reward.setItemMeta(meta);
			items.add(reward);
			referee.getPlayer().getInventory().addItem(reward);
			referee.getPlayer().updateInventory();
		}

		ItemStack[] item = new ItemStack[items.size()];
		item = items.toArray(item);

		referee.getPlayer().getInventory().addItem(item);
		referee.getPlayer().updateInventory();

		for (String s : config.getStringList(path + ".Commands")) {
			s = s.replaceAll("%player%", referee.getName());
			doCommands(s);
		}

		for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
				.getStringList("Rewards.OneBeingReferred")) {
			referee.sendMessage(translateColors(s));
		}
	}

	private boolean doCommands(String cmd) {
		plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
		return true;
	}

	private void refererReward(Player referer) {
		String path = "Rewards.OneThatRefered";
		String path2 = "Rewards.OneThatRefered.Items";
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();

		for (String s : config.getConfigurationSection(path2).getKeys(false)) {
			String itemPath = path2 + "." + s;

			ItemStack reward = new ItemStack(Material.matchMaterial(config.getString(itemPath + ".Material")),
					config.getInt(itemPath + ".Amount"));

			ItemMeta meta = reward.getItemMeta();
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(itemPath + ".Name")));
			meta.setLore(getLore(itemPath));

			for (String t : config.getStringList(itemPath + ".Enchants")) {
				if (getEnchantFromString(t) == null) {
					plugin.getLogger().log(Level.WARNING, "Enchantment is null for path " + path2 + ".Enchants");
					continue;
				}
				meta.addEnchant(getEnchantFromString(t), getLevelFromString(t), true);
			}
			reward.setItemMeta(meta);
			items.add(reward);
		}

		ItemStack[] item = new ItemStack[items.size()];
		item = items.toArray(item);

		referer.getInventory().addItem(item);
		referer.updateInventory();

		for (String s : plugin.getFileManager().getFile(FileManager.Files.MESSAGE)
				.getStringList("Rewards.OneThatRefered")) {
			referer.sendMessage(translateColors(s));
		}
		
		for (String s : config.getStringList(path + ".Commands")) {
			s = s.replaceAll("%player%", referer.getName());
			doCommands(s);
		}
	}

	public ArrayList<String> getLore(String itemPath) {
		ArrayList<String> lore = new ArrayList<String>();

		for (String s : config.getStringList(itemPath + ".Lore"))
			lore.add(ChatColor.translateAlternateColorCodes('&', s));

		return lore;
	}

	@SuppressWarnings("deprecation")
	private Enchantment getEnchantFromString(String list) {
		Enchantment enchant = Enchantment.getByName(list.substring(0, list.indexOf(":")).toUpperCase());

		return enchant == null ? null : enchant;
	}

	private int getLevelFromString(String list) {
		int level = Integer.parseInt(list.substring(list.indexOf(":") + 1, list.length()));

		return level == 0 ? null : level;
	}

	private String translateColors(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
