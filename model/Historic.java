package model;

import java.util.ArrayList;

import model.enums.ActionResult;

public class Historic {
    ArrayList<Turn> historicList;
    private int currentTurnIndex;

    public Historic(){
         this.historicList = new ArrayList<Turn>();
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

    public void newTurn(Team currentTeamTurn) {
        this.historicList.add(new Turn(currentTeamTurn));
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
    	return this.historicList.get(currentTurnIndex).undo();
    }
    
    public ActionResult resetCurrentTurn() {
    	Turn currentTurn = this.historicList.get(this.currentTurnIndex);
    	return currentTurn.deleteActions();
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
