package me.wsman217.crazyreference.data;

public class LeaderboardStorage {
	
	private String player_name;
	private int total_referrals;

	public LeaderboardStorage(String player_name, int total_referrals) {
		this.player_name = player_name;
		this.total_referrals = total_referrals;
	}
	
	public String getPlayerName() {
		return player_name;
	}
	
	public int getTotalReferrals() {
		return total_referrals;
	}
}
