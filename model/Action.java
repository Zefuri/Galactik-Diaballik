package model;

public class Action {
    private final int type;
    private Player previousPlayer, nextPlayer;
    private Case previousCase, nextCase;

    //We remove the "public" attribute in order not to access the constructor from an outer package
    Action(int actionType, Player previousPlayer, Player nextPlayer, Case previousCase, Case nextCase) {
        this.type = actionType;
        this.previousPlayer = previousPlayer;
        this.nextPlayer = nextPlayer;
        this.previousCase = previousCase;
        this.nextCase = nextCase;
    }

    public int getType() {
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
    	if (this.type == ModelConstants.ACTION_MOVE) {
    		return previousPlayer;
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
    
    public char getDirection() {
    	if (type == ModelConstants.ACTION_MOVE) {
	    	char direction = ModelConstants.ERROR;
	    	
	    	if (previousCase.getX() < nextCase.getX()) {
	    		direction = ModelConstants.DOWN;
	    	} else if (previousCase.getX() > nextCase.getX()) {
	    		direction = ModelConstants.UP;
	    	} else if (previousCase.getY() < nextCase.getY()) {
	    		direction = ModelConstants.RIGHT;
	    	} else if (previousCase.getY() > nextCase.getY()) {
	    		direction = ModelConstants.LEFT;
	    	} else {
	    		throw new IllegalStateException("The selected case is not situated next to the player!");
	    	}
	    	
	    	return direction;
    	} else {
    		throw new IllegalStateException("Do not call the getDirection() function if the action type is not \"MOVE\".");
    	}
    }
}
