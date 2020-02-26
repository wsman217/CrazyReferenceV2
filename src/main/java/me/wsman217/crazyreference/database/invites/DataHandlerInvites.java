package me.wsman217.crazyreference.database.invites;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.database.balance.DataHandlerBalance;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataHandlerInvites {
    private DataBase db;

    public DataHandlerInvites(DataBase db) {
        this.db = db;
    }

    public DataHandlerInvites generateTables() {
        Connection conn = this.db.getConnection();
        try {
            GenericTools.sendConsoleMessageWithVerbose(ChatColor.WHITE + "Invite table generating.");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS invites(" +
                    "id INTEGER " + db.getAutoInc(conn) + " PRIMARY KEY, " +
                    "referer INTEGER, " +
                    "referee INTEGER, " +
                    "FOREIGN KEY(referer) REFERENCES users(id)," +
                    "FOREIGN KEY(referee) REFERENCES users(id))");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}
