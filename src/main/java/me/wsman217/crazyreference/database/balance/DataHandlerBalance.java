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

    /**
     * Generates the balance table in the correct SQL database.
     *
     * @return instance of the class.
     */
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

    /**
     * Checks if the given player is in the database, there will only be one instance of each player in the database as it is a one-to-one relationship.
     *
     * @param p The player to search the database for.
     * @return true if the player is in the database and false otherwise.
     */
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

    /**
     * Adds a player into the database with a given starting balance, make sure to check if the player is already in the database before using this method.
     *
     * @param p           Player to insert.
     * @param startingBal The balance to insert the player with.
     */
    public void insert(Player p, double startingBal) {
        Connection conn = this.db.getConnection();

        DataHandlerUsers userHandler = CrazyReference.userHandler;
        boolean inUsers = userHandler.contains(p);
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO balance (user_id, balance) " +
                    "VALUES (" + (inUsers ? userHandler.getID(p) : userHandler.insert(p)) + ", " +
                    startingBal + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the players current balance from the database.
     *
     * @param p The player that will be searched for.
     * @return the balance of the player or -1 if the player is not in the database.
     */
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

    /**
     * Updates the players balance without checking if it is a withdraw or deposit.
     *
     * @param p      Player to update.
     * @param amount Amount to add/subtract from the balance.
     * @return the new balance or -1 if the balance goes below 0 or if the player is not in the database.
     */
    private int updateBal(Player p, int amount) {
        Connection conn = this.db.getConnection();
        int bal = -1;
        try {
            int prevBal = getBal(p);
            if (prevBal <= -1)
                return -1;
            bal = amount + prevBal;
            if (bal < 0)
                return -1;
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

    /**
     * Deposit given amount of money in a players balance. Uses the internal {@link me.wsman217.crazyreference.database.balance.DataHandlerBalance#updateBal(Player p, int amount)} method.
     *
     * @param p             The player to deposit into.
     * @param depositAmount The deposit amount.
     * @return the updated balance or -1 if the depositAmount is below 0.
     */
    public int deposit(Player p, int depositAmount) {
        if (depositAmount < 0)
            return -1;
        return updateBal(p, depositAmount);
    }

    /**
     * Withdraw given amount of money in a players balance. Uses the internal {@link me.wsman217.crazyreference.database.balance.DataHandlerBalance#updateBal(Player p, int amount)} method.
     *
     * @param p              The player to withdraw from.
     * @param withdrawAmount The withdraw amount.
     * @return the updated balance or -1 if the withdrawAmount is above 0.
     */
    public int withdraw(Player p, int withdrawAmount) {
        if (withdrawAmount > 0)
            return -1;
        return updateBal(p, withdrawAmount);
    }

    /**
     * Checks if the player has enough in their balance to withdraw given amount.
     *
     * @param p              The player to check.
     * @param withdrawAmount The amount to check for.
     * @return true if the player can withdraw that much without going below 0 or false if not, will also return false if the player is not in the database.
     */
    public boolean hasEnough(Player p, int withdrawAmount) {
        int bal = getBal(p);
        if (bal < 0)
            return false;
        return bal - Math.abs(withdrawAmount) >= 0;
    }
}
