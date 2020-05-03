package model;

import java.util.ArrayList;
import java.util.Collection;

import model.enums.TeamPosition;

public class Team {
	private String name;
	private ArrayList<Player> players;
	private Stadium stadium;
	private TeamPosition position;

	public Team(String name, TeamPosition position, Stadium stadium) {
		this.name = name;
		this.position = position;
		this.stadium = stadium;
		initialize();
	}
	
	public void initialize() {
		players = new ArrayList<>();
		
		for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
			Player p;
			
			if (position == TeamPosition.TOP) {
				p = new Player("TOP_" + i);
				p.setPosition(new Case(0, i));
			} else {
				p = new Player("BOT_" + i);
				p.setPosition(new Case(6, i));
			}
			
			if (i == 3) {
				p.setBallPossession(true);
			}
			
			addPlayer(p);
		}
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
		return this.players;
	}
	
	public Player getBallPlayer() throws IllegalStateException {
		for (Player p : this.players) {
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
