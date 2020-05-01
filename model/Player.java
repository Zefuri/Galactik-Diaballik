package model;

import model.enums.ActionType;

public class Player {
	private final Team team;
	private String name;
	private Case position;
	private boolean ballPossession;
	private boolean playerSelected;

	public Player(Team team, String name, Case position, boolean ballPossession) {
		this.team = team;
		this.name = name;
		this.position = position;
		this.ballPossession = ballPossession;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public Case getPosition() {
		return this.position;
	}
	
	public boolean hasBall() {
		return this.ballPossession;
	}
	
	public boolean canMove(char direction) {
		return team.getStadium().playerCanMove(this, direction);
	}
	
	public boolean canPass(Player nextPlayer) {
		return team.getStadium().playerCanPass(this, nextPlayer);
	}
	
	public Action move(char direction) {
		if (canMove(direction)) {
			Case prevPos = this.position;
			
			switch (direction) {
	        	case ModelConstants.UP:
	        		this.position = new Case(this.position.getX() - 1, this.position.getY());
	        		break;
	        		
	        	case ModelConstants.DOWN:
	        		this.position = new Case(this.position.getX() + 1, this.position.getY());
	        		break;
	        		
	        	case ModelConstants.RIGHT:
	        		this.position = new Case(this.position.getX(), this.position.getY() + 1);
	        		break;
	        	
	        	case ModelConstants.LEFT:
	        		this.position = new Case(this.position.getX(), this.position.getY() - 1);
	        		break;
			}
			
			team.getStadium().move(this, direction);
			
			return new Action(ActionType.MOVE, this, null, prevPos, this.position);
		} else {
			//Voir si c'est vraiment utile 
			throw new RuntimeException("You can not move on the clicked case.");
		}
	}
	
	public Action pass(Player nextPlayer) {
		if (canPass(nextPlayer)) {
			team.getStadium().pass(this, nextPlayer);
			
			return new Action(ActionType.PASS, this, nextPlayer, this.position, nextPlayer.getPosition());
		} else {
			//Voir si c'est vraiment utile
			throw new RuntimeException("You can not pass the ball to the selected player.");
		}
	}

	public void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}

	public boolean isATeammate(Player player) {
		return this.team == player.team;
	}
	
	public void setPlayerSelected(boolean selected) {
		this.playerSelected = selected;
	}
	
	public boolean playerSelected() {
		return this.playerSelected;
	}
}
