package model;

import model.enums.ActionType;
import model.enums.MoveDirection;

public class Player {
	private final String name;
	private Team team;
	private Case position;
	private boolean ballPossession;
	private boolean selected;

	public Player(String name) {
		this.name = name;
		this.position = new Case();
		this.ballPossession = false;
		this.selected = false;
	}
	
	public String getName() {
		return this.name;
	}
	
	void setTeam(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return this.team;
	}

	public Stadium getStadium() {
		return getTeam().getStadium();
	}

	void setPosition(int x, int y) {
		this.getPosition().setX(x);
		this.getPosition().setY(y);
	}

	void setPosition(Case position) {
		this.position = position;
	}
	
	public Case getPosition() {
		return this.position;
	}
	
	void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}

	public boolean hasBall() {
		return this.ballPossession;
	}

	Case calculateNewPosition(MoveDirection direction) {
        int newX = getPosition().getX();
		int newY = getPosition().getY();
		
		switch (direction) {
			case UP:
				newY--;
				break;
				
			case DOWN:
				newY++;
				break;
				
			case LEFT:
				newX--;
				break;
				
			case RIGHT:
				newX++;
				break;
		}

		return new Case(newX, newY);
	}
	
	public boolean canMove(MoveDirection direction) {
		return team.getStadium().playerCanMove(this, direction);
	}
	
	public Action move(MoveDirection direction) {
		if (canMove(direction)) {
			Case newPosition = calculateNewPosition(direction);
			Action moveAction = new Action(ActionType.MOVE, this, this, this.position, newPosition);
			setPosition(newPosition);
			
			return moveAction;
		} else {
			//Voir si c'est vraiment utile 
			throw new RuntimeException("You can not move on the clicked case.");
		}
	}
	
	public boolean canPass(Player nextPlayer) {
		return team.getStadium().playerCanPass(this, nextPlayer);
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

	public boolean isATeammate(Player player) {
		return this.getTeam() == player.getTeam();
	}
	
	public boolean isSelected() {
		return this.selected;
	}
	
	public void setIfSelected(boolean selected) {
		this.selected = selected;
	}
}
