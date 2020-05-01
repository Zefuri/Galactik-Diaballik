package model;

import model.enums.ActionType;
import model.enums.MoveDirection;

public class Player {
	private final String name;
	private Team team;
	private Case position;
	private boolean ballPossession;

	public Player(String name) {
		this.name = name;
		this.position = new Case(-1, -1);
		this.ballPossession = false;
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

	public boolean getBallPossession() {
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
        if (getBallPossession()) {
            return false;
		}

		Case newPosition = calculateNewPosition(direction);
		
        if (newPosition.getX() < 0 || newPosition.getX() > 6 || newPosition.getY() < 0 || newPosition.getY() > 6) {
            return false;
		}
		
		if (getStadium().getPlayerAt(newPosition) != null) {
			return false;
		}

        return true;
	}
	
	public Action move(MoveDirection direction) {
		if (canMove(direction)) {
			Case newPosition = calculateNewPosition(direction);
			setPosition(newPosition);
			return getStadium().getLastAction();
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
}
