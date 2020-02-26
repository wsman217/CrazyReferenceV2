package me.wsman217.crazyreference.database.balance;

import me.wsman217.crazyreference.CrazyReference;
import me.wsman217.crazyreference.database.DataBase;
import me.wsman217.crazyreference.database.users.DataHandlerUsers;
import me.wsman217.crazyreference.tools.GenericTools;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                    "user_id INTEGER(11) " + (db.isSQLite() ? "" : "UNSIGNED") + " PRIMARY KEY, " +
                    "bal INTEGER NOT NULL, " +
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
            PreparedStatement ps = conn.prepareStatement("SELECT uuid, user_id FROM balance INNER JOIN " +
                    "users ON (balance.user_id=users.id) WHERE uuid='" + p.getUniqueId().toString() + "'");
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void insert(Player p, double startingBal) {
        Connection conn = this.db.getConnection();

        DataHandlerUsers userHandler = CrazyReference.userHandler;
        boolean inUsers = userHandler.contains(p);
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO balance (user_id, balance) " +
                    "VALUES (" + (inUsers ? userHandler.getID(p) : userHandler.insert(p)) + ", " +
                    "" + startingBal + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBal(Player p) {
        Connection conn = this.db.getConnection();
        int bal = -1;
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT bal FROM balance INNER JOIN " +
                    "users ON (balance.user_id=users.id) WHERE uuid='" + p.getUniqueId().toString() + "'");
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                bal = rs.getInt("bal");
            ps.close();
            rs.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bal;
    }

    public int updateBal(Player p, int toAdd) {
        Connection conn = this.db.getConnection();
        int bal = -1;
        try {
            int prevBal = getBal(p);
            if (prevBal <= -1)
                return bal;
            bal = toAdd + prevBal;
            int id = CrazyReference.userHandler.getID(p);
            if (id <= -1)
                id = CrazyReference.userHandler.insert(p);
            PreparedStatement ps = conn.prepareStatement("UPDATE balance" +
                    "SET balance = " + bal +
                    "WHERE user_id = " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bal;
    }
}
