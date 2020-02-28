package me.wsman217.crazyreference.database.users;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataHandlerUsers {

    private DataBase db;

    public DataHandlerUsers(DataBase db) {
        this.db = db;
    }

    public DataHandlerUsers generateTables() {
        Connection conn = this.db.getConnection();
        try {
            GenericTools.sendConsoleMessageWithVerbose(ChatColor.WHITE + "User table generating.");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users(" +
                    "id INTEGER " + (db.isSQLite() ? "" : "UNSIGNED ") + "PRIMARY KEY " + db.getAutoInc(conn) + ", " +
                    "uuid VARCHAR(36) NOT NULL)");
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
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE uuid='" + p.getUniqueId().toString() + "'");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insert(Player p) {
        Connection conn = this.db.getConnection();
        int output = -1;
        try {
            if (!contains(p)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO users (uuid) VALUES(?)");
                ps.setString(1, p.getUniqueId().toString());
                ps.execute();
                ps.close();
                PreparedStatement select = conn.prepareStatement("SELECT id FROM users WHERE id=LAST_INSERT_ID()");
                ResultSet rs = select.executeQuery();
                if (rs.next())
                    output = rs.getInt("id");
                select.close();
                rs.close();
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    public int getID(Player p) {
        Connection conn = this.db.getConnection();
        int id = -1;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE uuid='" + p.getUniqueId().toString() + "'");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                id = rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
