package model;

import model.enums.ActionResult;
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
		return this.team.getStadium();
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

	public Case calculateNewPosition(MoveDirection direction) {
        int newX = getPosition().getX();
		int newY = getPosition().getY();
		
		switch (direction) {
			case UP:
				newX--;
				break;
				
			case DOWN:
				newX++;
				break;
				
			case LEFT:
				newY--;
				break;
				
			case RIGHT:
				newY++;
				break;
		}
		
		return new Case(newX, newY);
	}
	
	public boolean canMove(MoveDirection direction) {
		return team.getStadium().playerCanMove(this, direction);
	}
	
	public ActionResult move(MoveDirection direction) {
		if (canMove(direction)) {
			Case newPosition = calculateNewPosition(direction);

			Action moveAction = new Action(ActionType.MOVE, this, this, this.position, newPosition);
			
			return this.getStadium().actionPerformed(moveAction);
		} else {
			//Voir si c'est vraiment utile 
			throw new RuntimeException("You can not move on the clicked case.");
		}
	}
	
	public boolean canPass(Player nextPlayer) {
		return team.getStadium().playerCanPass(this, nextPlayer);
	}
	
	public ActionResult pass(Player nextPlayer) {
		if (canPass(nextPlayer)) {
			Action passAction = new Action(ActionType.PASS, this, nextPlayer, this.position, nextPlayer.getPosition());
		
			return this.getStadium().actionPerformed(passAction);
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
	
	public boolean canPlay() {
		//We return a boolean informing if it is the player current turn
		return this.team.isCurrentlyPlaying();
	}
	
	public boolean canBeSelected() {
		return this.canPlay() && (this.getStadium().getNbMovesDone() != ModelConstants.MAX_MOVES_PER_TOUR);
	}
	
	public boolean canBeSelectedForPass() {
		return this.canPlay() && (this.getStadium().getNbPassesDone() != ModelConstants.MAX_PASSES_PER_TOUR);
	}
	
	public boolean equals(Player second){
		return name.equals(second.getName());
	}
}
