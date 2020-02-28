package me.wsman217.crazyreference.database;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.tools.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
    private Connection connection;
    private CrazyReference plugin = CrazyReference.getInstance();

    public DataBase() {
        this.openDatabaseConnection();
    }

    /**
     * Open the connection to the database. Should stay open until the plugin disables.
     */
    public void openDatabaseConnection() {
        FileConfiguration config = plugin.getConfig();
        try {
            if (config.getBoolean("Database.SQLite")) {
                try {
                    Class.forName("org.sqlite.JDBC");
                } catch (ClassNotFoundException classNotFoundException) {
                    classNotFoundException.printStackTrace();
                }
                File file = new File(
                        plugin.getDataFolder().toString() + File.separator + "CrazyReference.db");
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
                        "jdbc:mysql://" + config.getString("Database.SQL.Host") + ":" + config.getString("Database.SQL.Port") + "/"
                                + config.getString("Database.SQL.Database"),
                        config.getString("Database.SQL.User"), config.getString("Database.SQL.Password"));
            }
        } catch (SQLException sQLException) {
            System.out.println("ERROR CONNECTING TO DATABASE!");
            sQLException.printStackTrace();
        }
    }

    /**
     * Close the connection to the database.
     */
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException sQLException) {
            System.out.println("COULD NOT CLOSE CONNECTION!");
            sQLException.printStackTrace();
        }
    }

    /**
     * Get the connection to the database.
     *
     * @return the connection to the database.
     */
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

    /**
     * Gets the keyword for an auto incrementing integer in the given SQL syntax.
     *
     * @return the correct keyword for the given SQL syntax.
     */
    public String getAutoInc() {
        return isSQLite() ? "AUTOINCREMENT" : "AUTO_INCREMENT";
    }

    /**
     * Test if the database is an SQLite connection of SQL connection.
     *
     * @return true if the database is SQLite and false if its not.
     */
    public boolean isSQLite() {
        return plugin.getFileManager().getFile(FileManager.Files.CONFIG).getBoolean("Database.SQLite");
    }
}