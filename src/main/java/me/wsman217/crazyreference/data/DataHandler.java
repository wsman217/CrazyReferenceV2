package me.wsman217.crazyreference.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.wsman217.crazyreference.CrazyReference;

public class DataHandler {

	// private CrazyReference plugin;
	private DataBase db;

	public DataHandler(DataBase db) {
		// this.plugin = CrazyReference.getInstance();
		this.db = db;
	}

	public void generateTables() {
		Connection connection = this.db.getConnection();

		try {
			PreparedStatement ps = connection.prepareStatement(
					"CREATE TABLE IF NOT EXISTS reference_leaderboard(uuid VARCHAR(40) PRIMARY KEY, player_name VARCHAR(40), total_referrals INTEGER)");
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertIntoTable(Player p, int total) {
		Connection connection = this.db.getConnection();
		String statement = "INSERT INTO reference_leaderboard VALUES(?,?,?)";
		try {
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.setString(1, p.getUniqueId().toString());
			ps.setString(2, p.getName());
			ps.setInt(3, total);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteFromTable(String lookFor) {
		Connection connection = this.db.getConnection();
		String statement = "DELETE FROM reference_leaderboard WHERE UPPER(player_name)='" + lookFor.toUpperCase() + "'";
		try {
			PreparedStatement ps = connection.prepareStatement(statement);
			ps.execute();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void changeTotal(String p, int newTotal) {
		@SuppressWarnings("deprecation")
		OfflinePlayer player = CrazyReference.getInstance().getServer().getOfflinePlayer(p);
		Connection connection = this.db.getConnection();
		String statement = "REPLACE INTO reference_leaderboard VALUES(?,?,?)";
		try {
			PreparedStatement ps1 = connection.prepareStatement(statement);
			ps1.setString(1, player.getUniqueId().toString());
			ps1.setString(2, player.getName());
			ps1.setInt(3, newTotal);
			ps1.execute();
			ps1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateOnReferredJoin(Player p) {
		Connection connection = this.db.getConnection();
		int total = 0;

		String statement1 = "SELECT * FROM reference_leaderboard WHERE UPPER(player_name)='" + p.getName().toUpperCase()
				+ "'";
		String statement2 = "REPLACE INTO reference_leaderboard VALUES(?,?,?)";

		try {
			PreparedStatement ps1 = connection.prepareStatement(statement1);
			ResultSet rs1 = ps1.executeQuery();
			if (rs1.next()) {
				total = rs1.getInt("total_referrals");
			}
			ps1.close();
			rs1.close();

			PreparedStatement ps2 = connection.prepareStatement(statement2);
			ps2.setString(1, p.getUniqueId().toString());
			ps2.setString(2, p.getName());
			ps2.setInt(3, (total + 1));
			ps2.execute();
			ps2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<LeaderboardStorage> getLeaderboard() {
		Connection connection = this.db.getConnection();
		
		ArrayList<LeaderboardStorage> leaderboard = new ArrayList<LeaderboardStorage>();
		
		String statement = "SELECT * FROM reference_leaderboard ORDER BY total_referrals DESC";
		
		try {
			PreparedStatement ps = connection.prepareStatement(statement);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				leaderboard.add(new LeaderboardStorage(rs.getString("player_name"), rs.getInt("total_referrals")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return leaderboard.isEmpty() ? null : leaderboard;
	}
}
