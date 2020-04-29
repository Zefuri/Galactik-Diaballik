package model;

public class Action {
    private int whosturn;
    private int actionType;//0 move, 1 pass, 2 end of turn
    private int firstI, firstJ, secondI, secondJ;

    public Action(int whosturn, int actionType, int firstI, int firstJ, int secondI, int secondJ){
        this.whosturn = whosturn;
        this.actionType = actionType;
        this.firstI = firstI;
        this.firstJ = firstJ;
        this.secondI = secondI;
        this.secondJ = secondJ;
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
}
