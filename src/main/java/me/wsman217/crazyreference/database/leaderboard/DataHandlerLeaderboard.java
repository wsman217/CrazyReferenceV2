package me.wsman217.crazyreference.database.leaderboard;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataHandlerLeaderboard {
    private DataBase db;

    public DataHandlerLeaderboard(DataBase db) {
        this.db = db;
    }

    public DataHandlerLeaderboard generateTables() {
        Connection conn = this.db.getConnection();
        try {
            GenericTools.sendConsoleMessageWithVerbose(ChatColor.WHITE + "Leaderboard table generating.");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS leaderboard(" +
                    "user_id INTEGER(11) " + (db.isSQLite() ? "" : "UNSIGNED") + " PRIMARY KEY, " +
                    "total_references INTEGER, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}
