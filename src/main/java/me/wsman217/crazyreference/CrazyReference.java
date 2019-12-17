package me.wsman217.crazyreference;

import org.bukkit.plugin.java.JavaPlugin;

import me.wsman217.crazyreference.commands.ReferAdminCommand;
import me.wsman217.crazyreference.commands.ReferCommand;
import me.wsman217.crazyreference.commands.ReferLeaderboardCommand;
import me.wsman217.crazyreference.configTools.configManager;
import me.wsman217.crazyreference.data.DataBase;
import me.wsman217.crazyreference.data.DataHandler;
import me.wsman217.crazyreference.listeners.ListenerPlayerJoin;
import me.wsman217.crazyreference.listeners.OnMeJoin;
import me.wsman217.crazyreference.tools.FileManager;
import me.wsman217.crazyreference.tools.GiveRewards;
import me.wsman217.crazyreference.tools.Metrics;

public class CrazyReference extends JavaPlugin {

	// This is the main class

	public configManager cMan;
	public GiveRewards giveRewards;
	private static CrazyReference instance;
	public Metrics bStats;
	private FileManager fileManager;

	private DataBase db;
	private DataHandler dh;

	@Override
	public void onEnable() {
		instance = this;
		bStats = new Metrics(this);

		saveDefaultConfig();

		fileManager = FileManager.getInstance().logInfo(true).setup(this);

		// Initialize some of my classes
		// Yes I know these shouldn't be public but I honestly didn't care to much
		cMan = new configManager(this);
		giveRewards = new GiveRewards(this);

		// Register events
		getServer().getPluginManager().registerEvents(new ListenerPlayerJoin(this), this);
		getServer().getPluginManager().registerEvents(new OnMeJoin(this), this);

		// Register the command /refer
		getCommand("refer").setExecutor(new ReferCommand(this));

		if (getConfig().getBoolean("Settings.LeaderboardEnabled")) {
			db = new DataBase();
			dh = new DataHandler(db);
			db.openDatabaseConnection();
			dh.generateTables();
		}
		getCommand("referleaderboard").setExecutor(new ReferLeaderboardCommand());
		getCommand("referadmin").setExecutor(new ReferAdminCommand(this));
	}

	@Override
	public void onDisable() {
	}

	public static CrazyReference getInstance() {
		return instance;
	}

	public FileManager getFileManager() {
		if (fileManager == null)
			throw new NullPointerException("File manger for plugin CrazyReference was null");
		return fileManager;
	}

	public DataHandler getDataHandler() {
		return dh;
	}
}