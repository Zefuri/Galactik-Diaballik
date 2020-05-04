package model;

public class Turn {
	private Action actions[];
	private Team team;
	private int nbPass;
	private int nbMove;

	public Turn(Team team) {
		this.actions = new Action[3];
		this.team = team;
		this.nbPass = 0;
		this.nbMove = 0;
	}
	
	public void addAction(Action action) {
		switch(action.getType()) {
			case PASS:
				if(this.nbPass < 1) {
					this.actions[(this.actions.length) - 1] = action;
					this.nbPass++;
				} else {
					System.err.println("Pass cannot be done : all the pass for this turn are already done.");
				}
				break;
			case MOVE:
				if(this.nbMove < 2) {
					this.actions[(this.actions.length) - 1] = action;
					this.nbMove++;
				} else {
					System.err.println("Move cannot be done : all the move for this turn are already done.");
				}
				break;
			default:
				System.err.println("Not An Action : this action type is not recognized.");
		}
	}
	
	public Action[] getActions() {
		return this.actions;
	}
	
	public Action getAction(int index) {
		return this.actions[index];
	}
	
	public Team getTeam() {
		return this.team;
	}
	
	public int getNbPassDone() {
		return this.nbPass;
	}
	
	public int getNbPassLeft() {
		return ModelConstants.MAX_PASSES_PER_TOUR - this.nbPass;
	}
	
	public int getNbMoveDone() {
		return this.nbMove;
	}
	
	public int getNbMoveLeft() {
		return ModelConstants.MAX_MOVES_PER_TOUR - this.nbMove;
	}
}
