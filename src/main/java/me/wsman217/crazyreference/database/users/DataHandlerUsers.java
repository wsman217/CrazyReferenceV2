package me.wsman217.crazyreference.database.users;

import me.wsman217.crazyreference.database.DataBase;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataHandlerUsers {

    private DataBase db;

    public DataHandlerUsers(DataBase db) {
        this.db = db;
    }

    public DataHandlerUsers generateTables() {
        Connection conn = this.db.getConnection();
        try {
            DatabaseMetaData dbmd = conn.getMetaData();
            String databaseType = dbmd.getDriverName();
            System.out.println(databaseType);
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS users(id INTEGER " + (databaseType.equalsIgnoreCase("MariaDB") ? "AUTO_INCREMENT" : "IDENTITY") + " PRIMARY KEY," +
                    " uuid VARCHAR(36))");
            ps.execute();
            ps.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }
}
