package me.wsman217.crazyreference.prizes;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.prizes.exceptions.ItemNotFoundException;
import me.wsman217.crazyreference.tools.FileManager;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PrizeManagerTemp {

    private FileConfiguration prizeYML = CrazyReference.getInstance().getFileManager().getFile(FileManager.Files.PRIZES);
    public ArrayList<ItemStack> itemPrizes = new ArrayList<>();
    public ArrayList<String> commands = new ArrayList<>();

    public PrizeManagerTemp initPrizes(String referredType) {
        for (String key : Objects.requireNonNull(prizeYML.getConfigurationSection("Referrer.Items")).getKeys(false))
            try {
                itemPrizes.add(new ItemPrize(referredType + ".Items." + key + ".").item);
            } catch (ItemNotFoundException e) {
                e.printStackTrace();
            }
        commands.addAll(prizeYML.getStringList(referredType + ".Commands"));
        return this;
    }

    public class ItemPrize {
        public ItemStack item;

        public ItemPrize(String path) throws ItemNotFoundException {
            Material mat = Material.matchMaterial(Objects.requireNonNull(prizeYML.getString(path + "Item")));
            if (mat == null)
                //TODO decide if i really want this to be an exception or just a print to the console in red letters
                throw new ItemNotFoundException("The item at path: \"" + path + "Item\" is an invalid item type.");
            ItemStack item = new ItemStack(mat);
            item.setAmount(prizeYML.getInt(path + "Amount"));

            List<String> lore = Objects.requireNonNull((prizeYML.getStringList(path + "Lore")));
            ItemMeta im = item.getItemMeta();
            assert im != null;
            im.setLore(lore);
            im.setDisplayName(ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(prizeYML.getString(path + "Name"))));

            for (String str : prizeYML.getStringList(path + "Enchants")) {
                String enchantName = str.substring(0, str.indexOf(':'));
                int level = GenericTools.isNumber(str.substring(str.indexOf(":") + 1)) ? Integer.parseInt(str.substring(str.indexOf(":") + 1)) : 1;
                im.addEnchant(Objects.requireNonNull(Enchantment.getByName(enchantName)), level, true);
            }
            im.setCustomModelData(prizeYML.getInt(path + "CustomModelData"));
            item.setItemMeta(im);

            this.item = item;
        }
    }
}
