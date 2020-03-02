package me.wsman217.crazyreference.database.invites;

import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
                    "id INTEGER " + (db.isSQLite() ? "" : "UNSIGNED") + " PRIMARY KEY " + db.getAutoInc() + ", " +
                    "referer INTEGER(11)" + (db.isSQLite() ? "," : " UNSIGNED, ") +
                    "referee INTEGER(11)" + (db.isSQLite() ? "," : " UNSIGNED, ") +
                    "date " + (db.isSQLite() ? "TEXT" : "TIMESTAMP") + ", " +
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

    public static class InviteHolder {
        private Timestamp time;
        private String referrer, referee;

        public InviteHolder(String referrer, String referee, Timestamp time) {
            this.time = time;
            this.referrer = referrer;
            this.referee = referee;
        }

        public String getTime() {
            return time.toString();
        }

        public String getReferrer() {
            return referrer;
        }

        public String getReferee() {
            return referee;
        }
    }
}
