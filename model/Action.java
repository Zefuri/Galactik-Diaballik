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
	
	public String toString(){
//		int numNextPlayer;
//		
//		switch (type) {
//			case MOVE:
//				numNextPlayer = (int) (nextPlayer.getName().charAt(nextPlayer.getName().length() - 1) - '0');
//				
//				if (previousCase.getX() + 1 == nextCase.getX()) {
//					return numNextPlayer + "D";
//				} else if (previousCase.getX() - 1 == nextCase.getX()) {
//					return numNextPlayer + "U";
//				} else if (previousCase.getY() + 1 == nextCase.getY()) {
//					return numNextPlayer + "R";
//				} else {
//					return +numNextPlayer + "L";
//				}
//				
//			case PASS:
//				numNextPlayer = (int) (nextPlayer.getName().charAt(nextPlayer.getName().length() - 1) - '0');
//				return numNextPlayer + "P";
//				
//			case END_TURN:
//				return "END";
//		}
//		
//		return "FAIL";
		
		return ("Type : " + this.type + "\nCase depart : " + this.previousCase + "\nCase arrivee : " + this.nextCase + "\nJoueur depart : " + this.previousPlayer.getName() + "\nJoueur arrivee : " + this.nextPlayer.getName() + "\n\n");
	}
}
