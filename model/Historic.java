package model;

import java.util.ArrayList;

import model.enums.ActionResult;

public class Historic {
    private ArrayList<Turn> historicList;
    private int currentTurnIndex;
    private Stadium stadium;

    public Historic(Stadium stadium){
         this.historicList = new ArrayList<>();
         this.stadium = stadium;
         currentTurnIndex = 0;
    }

    public Turn get(int index){
        return historicList.get(index);
    }

    public void cancelLastTurn(){
        currentTurnIndex--;
    }

    public Turn getLast(){
        return historicList.get(currentTurnIndex);
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public void nextTurn(){
        this.currentTurnIndex++;
    }

    public void newTurn(Team currentTeamTurn, boolean cheat) {
        this.historicList.add(new Turn(currentTeamTurn, cheat));
    }
    
    public void removeLastTurn() {
    	this.historicList.remove(this.historicList.size() - 1);
    	this.currentTurnIndex--;
    }
    
    public void addTurn(Turn turn) {
    	this.historicList.add(turn);
    	this.currentTurnIndex++;
    }
    
    public ActionResult undoLastAction() {
    	if (!this.stadium.isInVisualisationMode()) {
    		return this.historicList.get(currentTurnIndex).undo();
    	} else {
    		if (this.historicList.get(currentTurnIndex).undoGoesToPreviousTour()) {
    			if (currentTurnIndex != 0) {
    				return this.historicList.get(--currentTurnIndex).undo();
    			} else {
    				//We already are on the first tour, so we throw an error
    				return ActionResult.ERROR;
    			}
    		} else {
    			return this.historicList.get(currentTurnIndex).undo();
    		}
    	}
    }
    
    public boolean redoNextAction() {
    	//Usable only in visualization mode
    	if (this.historicList.get(currentTurnIndex).redoGoesToNextTour()) {
    		if (this.currentTurnIndex == this.historicList.size() - 1 || this.historicList.get(currentTurnIndex + 1).isEmpty()) {
    			//We are on the last tour, so we do not do anything
    			System.err.println("You reached the last tour.");
    			return false;
    		} else {
    			this.historicList.get(++currentTurnIndex).redo();
    		}
    	} else {
    		this.historicList.get(currentTurnIndex).redo();
    	}
    	
    	return true;
    }
    
    public ActionResult resetCurrentTurn() {
    	Turn currentTurn = this.historicList.get(this.currentTurnIndex);
    	return currentTurn.deleteActions();
    }
    
    public void setToFirstTurn() {
    	this.currentTurnIndex = 0;

        for (Turn turn : this.historicList) {
            turn.setNbMovesDone(0);
            turn.setNbPassesDone(0);
        }
    }
    
    //For testing
    public String toString() {
    	StringBuilder builder = new StringBuilder();
    	
    	for (Turn t : this.historicList) {
    		builder.append(t.toString());
    	}
    	
    	return builder.toString();
    }
}
