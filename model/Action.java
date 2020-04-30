package model;

public class Action {
    private final int whosturn;
    private final int actionType;//0 move, 1 pass, 2 end of turn
    private final int firstI, firstJ, secondI, secondJ;
    private final int nbTurn;
    private final int nbAction;

    public Action(int whosturn, int actionType, int firstI, int firstJ, int secondI, int secondJ, int nbTurn, int nbAction){
        this.whosturn = whosturn;
        this.actionType = actionType;
        this.firstI = firstI;
        this.firstJ = firstJ;
        this.secondI = secondI;
        this.secondJ = secondJ;
        this.nbTurn = nbTurn;
        this.nbAction = nbAction;

    }

    public int getActionType(){
        return this.actionType;
    }

    public int getWhosturn(){
        return this.whosturn;
    }

    public int getFirstI() {
        return firstI;
    }

    public int getFirstJ() {
        return firstJ;
    }

    public int getSecondI() {
        return secondI;
    }

    public int getSecondJ() {
        return secondJ;
    }

    public int getNbAction() {
        return nbAction;
    }

    public int getNbTurn() {
        return nbTurn;
    }
}
