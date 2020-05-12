package model;

import model.enums.ActionType;
import model.enums.MoveDirection;

public class Action {
    private final ActionType type;
    private Player previousPlayer, nextPlayer;
    private Case previousCase, nextCase;

    /* We remove the "public" attribute in order not to access the constructor from an outer package
     * 
     * PASS : new Action(ActionType.PASS, joueurEmetteur, joueurCible, caseEmettrice, caseCible);
     * MOVE : new Action(ActionType.MOVE, joueurBougeant, joueurBougeant, caseInitiale, caseCible);
     * END_TURN : new Action(ActionType.END_TURN, null, null, null, null);
     */
    Action(ActionType actionType, Player previousPlayer, Player nextPlayer, Case previousCase, Case nextCase) {
        this.type = actionType;
        this.previousPlayer = previousPlayer;
        this.nextPlayer = nextPlayer;
        this.previousCase = previousCase;
        this.nextCase = nextCase;
    }

    public ActionType getType() {
        return this.type;
    }

    public Player getPreviousPlayer() {
    	//Player which has the ball before the pass
    	return this.previousPlayer;
    }
    
    public Player getNextPlayer() {
    	//Player which has the ball after the pass
    	return this.nextPlayer;
    }
    
    public Player getMovedPlayer() {
    	//Player which has moved
    	if (this.type == ActionType.MOVE) {
    		return nextPlayer;
    	} else {
    		throw new IllegalStateException("Do not call the getMovedPlayer() function if the action type is not \"MOVE\".");
    	}
    }
    
    public Case getPreviousCase() {
    	return this.previousCase;
    }
    
    public Case getNextCase() {
    	return this.nextCase;
    }
    
    public MoveDirection getDirection() {
    	if (type == ActionType.MOVE) {
	    	MoveDirection direction;
	    	
	    	if (previousCase.getX() < nextCase.getX()) {
	    		direction = MoveDirection.DOWN;
	    	} else if (previousCase.getX() > nextCase.getX()) {
	    		direction = MoveDirection.UP;
	    	} else if (previousCase.getY() < nextCase.getY()) {
	    		direction = MoveDirection.RIGHT;
	    	} else if (previousCase.getY() > nextCase.getY()) {
	    		direction = MoveDirection.LEFT;
	    	} else {
	    		throw new IllegalStateException("You did not move!");
	    	}
	    	
	    	return direction;
    	} else {
    		throw new IllegalStateException("Do not call the getDirection() function if the action type is not \"MOVE\".");
    	}
    }
}
