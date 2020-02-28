package me.wsman217.crazyreference;

import me.wsman217.crazyreference.commands.CommandGetCode;
import me.wsman217.crazyreference.commands.CommandRedeemCode;
import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.database.balance.DataHandlerBalance;
import me.wsman217.crazyreference.database.invites.DataHandlerInvites;
import me.wsman217.crazyreference.database.iptable.DataHandlerIP;
import me.wsman217.crazyreference.database.leaderboard.DataHandlerLeaderboard;
import me.wsman217.crazyreference.database.users.DataHandlerUsers;
import me.wsman217.crazyreference.handlers.JoinEvent;
import me.wsman217.crazyreference.tools.FileManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CrazyReference extends JavaPlugin {

    public Metrics bStats;
    private static CrazyReference instance;
    private FileManager fileManager;
    private DataBase db;

    public static DataHandlerIP ipHandler;
    public static DataHandlerUsers userHandler;
    public static DataHandlerBalance balanceHandler;
    public static DataHandlerInvites inviteHandler;
    public static DataHandlerLeaderboard leaderboardHandler;

    /**
     * Where Spigot will enter the plugin.
     */
    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        instance = this;
        bStats = new Metrics(this);

        fileManager = FileManager.getInstance().logInfo(true).setup(this);

        initCommands();
        initListeners();
        initDatabases();
    }

    /**
     * Where Spigot will exit the plugin.
     */
    @Override
    public void onDisable() {
        db.closeConnection();
    }

    /**
     * Initialize all the commands and command objects as needed.
     */
    private void initCommands() {
        this.getCommand("getcode").setExecutor(new CommandGetCode());
        this.getCommand("redeemcode").setExecutor(new CommandRedeemCode());
    }

    /**
     * Initialize all the listeners and listener objects as needed.
     */
    private void initListeners() {
        PluginManager pman = this.getServer().getPluginManager();
        pman.registerEvents(new JoinEvent(), this);
    }

    /**
     * Initialize all of the databases, generate the tables, and create the database objects.
     */
    private void initDatabases() {
        db = new DataBase();
        userHandler = new DataHandlerUsers(db).generateTables();
        ipHandler = new DataHandlerIP(db).generateTables();
        balanceHandler = new DataHandlerBalance(db).generateTables();
        inviteHandler = new DataHandlerInvites(db).generateTables();
        leaderboardHandler = new DataHandlerLeaderboard(db).generateTables();
    }

    /**
     * Gets the CrazyReference instance.
     *
     * @return the CrazyReference instance.
     */
    public static CrazyReference getInstance() {
        return instance;
    }

    /**
     * Gets the FileManager instance.
     *
     * @return the FileManager instance.
     */
    public FileManager getFileManager() {
        return fileManager;
    }
}
