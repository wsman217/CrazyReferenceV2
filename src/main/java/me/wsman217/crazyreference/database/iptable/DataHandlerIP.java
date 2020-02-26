package me.wsman217.crazyreference.database.iptable;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class DataHandlerIP {

    private DataBase db;

    public DataHandlerIP(DataBase db) {
        this.db = db;
    }

    public DataHandlerIP generateTables() {
        Connection conn = this.db.getConnection();
        try {
            GenericTools.sendConsoleMessageWithVerbose(ChatColor.WHITE + "IP table generating.");
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ip_table(" +
                    "id INTEGER " + db.getAutoInc(conn) + " PRIMARY KEY, " +
                    "user_id INTEGER, " +
                    "address VARCHAR(16) NOT NULL, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ArrayList<UUID> getUsersByIp(String ip) {
        Connection conn = this.db.getConnection();
        ArrayList<UUID> uuids = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ip_table WHERE address='" + ip + "'");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                for (String str : rs.getString("address").split(","))
                    uuids.add(UUID.fromString(str));
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uuids;
    }

    public void insert(String ip, UUID id) {
        Connection conn = this.db.getConnection();
        try {
            PreparedStatement getIds = conn.prepareStatement("SELECT * FROM ip_table WHERE address='" + ip + "'");
            ResultSet idRs = getIds.executeQuery();
            String ids = "";
            if (idRs.next())
                ids = idRs.getString("address");
            PreparedStatement replaceInto = conn.prepareStatement("REPLACE INTO ip_table VALUES(?,?)");
            replaceInto.setString(1, ip);
            if (ids.length() > 0)
                ids += ",";
            ids += id;
            replaceInto.setString(2, ids);
            replaceInto.execute();

            getIds.close();
            replaceInto.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> findById(UUID id) {
        Connection conn = this.db.getConnection();
        ArrayList<String> ips = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM ip_tables WHERE id LIKE'%" + id.toString() + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                ips.add(rs.getString("ip"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ips;
    }
}
