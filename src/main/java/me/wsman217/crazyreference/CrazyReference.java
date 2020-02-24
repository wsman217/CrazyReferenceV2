package me.wsman217.crazyreference;

import me.wsman217.crazyreference.commands.CommandGetCode;
import me.wsman217.crazyreference.commands.CommandRedeemCode;
import me.wsman217.crazyreference.database.Database;
import me.wsman217.crazyreference.tools.FileManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyReference extends JavaPlugin {

    public Metrics bStats;
    private static CrazyReference instance;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        instance = this;
        Database db = new Database().openDatabaseConnection();
        bStats = new Metrics(this);

        fileManager = FileManager.getInstance().logInfo(true).setup(this);

        initCommands();
    }

    private void initCommands() {
        this.getCommand("getcode").setExecutor(new CommandGetCode());
        this.getCommand("redeemcode").setExecutor(new CommandRedeemCode());
    }

    private void initListeners() {
        PluginManager pman = this.getServer().getPluginManager();

    }

    public static CrazyReference getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
