package model;

import java.util.ArrayList;
import java.util.ListIterator;

import model.enums.TeamPosition;

public class Team {
	private Stadium stadium;
	private String name;
	private ArrayList<Player> players;
	private TeamPosition position;

	public Team(String name, Stadium stadium, TeamPosition position) {
		this.name = name;
		this.stadium = stadium;
		this.position = position;
		this.players = new ArrayList<>();
	}
	
	public void addPlayer(Player player) {
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
	
	public ListIterator<Player> getPlayers() {
		return players.listIterator();
	}
	
	public Player getBallPlayer() throws IllegalStateException {
		Player playerWithBall = null;
		
		while (getPlayers().hasNext()) {
			if (playerWithBall != null) {
				break;
			}
			
			Player currPlayer = getPlayers().next();
			
			if (currPlayer.hasBall()) {
				playerWithBall = currPlayer;
			}
		}

		if (playerWithBall == null) {
			throw new IllegalStateException("No player with ball found!");
		}
		
		return playerWithBall;
	}
	
	public Team getEnemyTeam() {
		//TODO
		return null;
	}
}
