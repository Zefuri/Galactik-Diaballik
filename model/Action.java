package model;

import model.enums.ActionType;
import model.enums.MoveDirection;

public class Action {
    private final ActionType type;
    private Player previousPlayer, nextPlayer;
    private Case previousCase, nextCase;

    public Action(ActionType actionType, Player previousPlayer, Player nextPlayer, Case previousCase, Case nextCase) {
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

    public Action inverse() {
		switch (type) {
			case MOVE:
				return new Action(ActionType.MOVE, null, nextPlayer, nextCase, previousCase);
			case PASS:
				return new Action(ActionType.PASS, nextPlayer, previousPlayer, nextCase, previousCase);
			case END_TURN:
				return new Action(ActionType.END_TURN, null, null, null, null);
		}

		return null;
	}
}
