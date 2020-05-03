package model;

import java.util.ArrayList;
import java.util.Collection;

import model.enums.TeamPosition;

public class Team {
	private String name;
	private ArrayList<Player> players;
	private Stadium stadium;
	private TeamPosition position;

	public Team(String name) {
		this.name = name;
		this.players = new ArrayList<>();
	}
	
	public void addPlayer(Player player) {
		player.setTeam(this);
		getPlayers().add(player);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Stadium getStadium() {
		return this.stadium;
	}
	
	public TeamPosition getPosition() {
		return this.position;
	}
	
	public Collection<Player> getPlayers() {
		return players;
	}
	
	public Player getBallPlayer() throws IllegalStateException {
		for (Player p : players) {
			if (p.hasBall()) {
				return p;
			}
		}
		
		throw new IllegalStateException("No player with ball found!");
	}
	
	public Team getEnemyTeam() {
		return stadium.getTeam(position == TeamPosition.TOP ? TeamPosition.BOTTOM : TeamPosition.TOP);
	}
}
