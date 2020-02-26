package me.wsman217.crazyreference.database.balance;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.PrepareItemCraftEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataHandlerBalance {

    private DataBase db;

    public DataHandlerBalance(DataBase db) {
        this.db = db;
    }

    public DataHandlerBalance generateTables() {
        Connection conn = this.db.getConnection();
        try {
            GenericTools.sendConsoleMessageWithVerbose(ChatColor.WHITE + "Balance table generating.");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS balance(" +
                    "id INTEGER " + db.getAutoInc(conn) + " PRIMARY KEY, " +
                    "user_id INTEGER, " +
                    "balance DOUBLE NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public boolean contains(Player p) {
        Connection conn = this.db.getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("");
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean insert(Player p, double addTo) {
        return true;
    }
}
