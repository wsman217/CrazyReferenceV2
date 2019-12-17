package me.wsman217.crazyreference.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import me.wsman217.crazyreference.CrazyReference;

public class DataBase {
	private Connection connection;
	private CrazyReference plugin = CrazyReference.getInstance();

	public void openDatabaseConnection() {
		FileConfiguration config = plugin.getConfig();
		try {
			if (config.getBoolean("SQLite")) {
				try {
					Class.forName("org.sqlite.JDBC");
				} catch (ClassNotFoundException classNotFoundException) {
					classNotFoundException.printStackTrace();
				}
				File file = new File(
						String.valueOf(plugin.getDataFolder().toString()) + File.separator + "playernotes.db");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException iOException) {
						iOException.printStackTrace();
					}
				}
				connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
			} else {
				connection = DriverManager.getConnection(
						"jdbc:mysql://" + config.getString("SQL.Host") + ":" + config.getString("SQL.Port") + "/"
								+ config.getString("SQL.Database"),
						config.getString("SQL.User"), config.getString("SQL.Password"));
			}
		} catch (SQLException sQLException) {
			System.out.println("ERROR CONNECTING TO DATABASE!");
			sQLException.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			connection.close();
		} catch (SQLException sQLException) {
			System.out.println("COULD NOT CLOSE CONNECTION!");
			sQLException.printStackTrace();
		}
	}

	public Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				this.openDatabaseConnection();
			}
		} catch (SQLException sQLException) {
			// empty catch block
		}
		return connection;
	}
}
