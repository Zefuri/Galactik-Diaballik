package model;

import java.util.ArrayList;

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
}
