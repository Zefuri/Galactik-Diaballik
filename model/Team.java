package model;

import java.util.ArrayList;
import java.util.Collection;

import model.enums.MoveDirection;
import model.enums.TeamPosition;

public class Team {
	private String name;
	private ArrayList<Player> players;
	private Stadium stadium;
	private TeamPosition position;
	private Player ballPlayer;
	private int butLine;

	public Team(String name, TeamPosition position, Stadium stadium, boolean toInit) {
		this.name = name;
		this.position = position;
		this.stadium = stadium;
		this.players = new ArrayList<>();
		
		if (toInit) {
			initializePlayers();
		}
	}
	
	public void initializePlayers() {
		this.players.clear();
		
		for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
			Player p;
			
			if (position == TeamPosition.TOP) {
				p = new Player("TOP_" + i);
				p.setPosition(new Case(0, i));
				this.butLine = 6;
			} else {
				p = new Player("BOT_" + i);
				p.setPosition(new Case(6, i));
				this.butLine = 0;
			}
			
			if (i == 3) {
				p.setBallPossession(true);
				ballPlayer = p;
			}
			
			addPlayer(p);
		}
	}
	
	public void replace() {
		for (Player p : this.getPlayers()) {
			int index = p.getNumero();
			p.setBallPossession(false);
			
			if (position == TeamPosition.TOP) {
				p.setPosition(new Case(0, index));
			} else {
				p.setPosition(new Case(6, index));
			}
			
			if (index == 3) {
				p.setBallPossession(true);
			}
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
	
	public boolean isCurrentlyPlaying() {
		return (this.equals(this.stadium.getCurrentTeamTurn()) || this.stadium.isCheatModActivated());
	}
	
	public Player getBallPlayer() throws IllegalStateException {
		return ballPlayer;
	}
	
	public void setBallPlayer(Player player) {
		ballPlayer = player;
	}
	
	public int getButLine() {
		return butLine;
	}
	
	//number of possibility pass
	public int numberOfPossibilityPass(){
		int number = 0;
		
		for(Player p : this.players){
			if(!p.hasBall()  &&  stadium.playerCanPass(getBallPlayer(), p)){
				number++;
			}
		}
		
		return number;
	}
	
	//return number of moves that can make player
	public int playerMovesNumber(Player player){
		int number = 0;
		
		if(!player.hasBall()){
			if(stadium.playerCanMove(player, MoveDirection.UP)){
				number++;
			}
			
			if(stadium.playerCanMove(player, MoveDirection.RIGHT)){
				number++;
			}
			
			if(stadium.playerCanMove(player, MoveDirection.DOWN)){
				number++;
			}
			
			if(stadium.playerCanMove(player, MoveDirection.LEFT)){
				number++;
			}
		}
		
		return number;
	}
	
	//Return the number of move can execute the team
	public int movesNumber(){
		int number = 0;
		
		for(Player p : this.players){
			number += playerMovesNumber(p);
		}
		
		return number;
	}
	
	//return number of players can move
	public int movePlayerNumber(){
		int number = 0;
		
		for(Player p : this.players){
			if(playerMovesNumber(p) != 0){
				number ++;
			}
		}
		
		return number;
	}

	//return the player correspondant at numero on the team
	public Player playerOfInt(int numero) {
		return players.get(numero);
	}
	
	public int size(){
		return players.size();
	}
	
	public Team getEnemyTeam() {
		return stadium.getTeam(position == TeamPosition.TOP ? TeamPosition.BOTTOM : TeamPosition.TOP);
	}
	
	public boolean equals(Team t) {
		//Voir si c'est suffisant
		return this.position == t.getPosition();
	}

	public boolean isWinner() {
		return stadium.isAWin(position);
	}
	
	public Player getPlayerFromName(String name) {
		//System.out.println("given name : " + name);
		
		for (Player p : this.getPlayers()) {
			if (p.hasTheSameName(name)) {
				return p;
			}
		}
		
		throw new IllegalStateException("There must be a player with the given name!");
	}
}
