package me.wsman217.crazyreference;

import me.wsman217.crazyreference.commands.CommandGetCode;
import me.wsman217.crazyreference.commands.CommandRedeemCode;
import me.wsman217.crazyreference.databases.iptable.DataBase;
import me.wsman217.crazyreference.handlers.JoinEvent;
import me.wsman217.crazyreference.prizes.PrizeManager;
import me.wsman217.crazyreference.prizes.PrizeManagerTemp;
import me.wsman217.crazyreference.tools.FileManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class CrazyReference extends JavaPlugin {

    public Metrics bStats;
    private static CrazyReference instance;
    private FileManager fileManager;

    public static PrizeManagerTemp referrerPrizes;
    public static PrizeManagerTemp referralPrizes;

    public static boolean isIpEnabled = true;

    public static me.wsman217.crazyreference.databases.iptable.DataHandler ipHandler;

    @Override
    public void onEnable() {
        instance = this;
        bStats = new Metrics(this);

        fileManager = FileManager.getInstance().logInfo(true).setup(this);

        initCommands();
        initListeners();

        referrerPrizes = new PrizeManagerTemp().initPrizes("Referrer");
        referralPrizes = new PrizeManagerTemp().initPrizes("Referral");

        //TODO add a turn off of ips
        DataBase ipTable = new DataBase();
        ipHandler = new me.wsman217.crazyreference.databases.iptable.DataHandler(ipTable).generateTables();
    }

    private void initCommands() {
        Objects.requireNonNull(this.getCommand("getcode")).setExecutor(new CommandGetCode());
        Objects.requireNonNull(this.getCommand("redeemcode")).setExecutor(new CommandRedeemCode());
    }

    private void initListeners() {
        PluginManager pman = this.getServer().getPluginManager();
        pman.registerEvents(new JoinEvent(), this);
    }

    public static CrazyReference getInstance() {
        return instance;
    }

    public FileManager getFileManager() {
        return fileManager;
    }
}
