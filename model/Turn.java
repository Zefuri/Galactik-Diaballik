package model;

import model.enums.ActionResult;
import model.enums.ActionType;

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
		this.actions[this.nbMove + this.nbPass] = action;
		
		if (action.getType() == ActionType.PASS) {
			this.nbPass++;
		} else if (action.getType() == ActionType.MOVE) {
			this.nbMove++;
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
	
	public ActionResult undo() {
		ActionResult res = ActionResult.DONE;
		
		if(this.nbMove + this.nbPass > 0) {
			Action actionToDelete = this.actions[(this.nbPass + this.nbMove) - 1];
			
			if(actionToDelete.getType() == ActionType.PASS) {
				actionToDelete.getPreviousPlayer().setBallPossession(true);
				actionToDelete.getNextPlayer().setBallPossession(false);
				this.nbPass--;
			} else {
				actionToDelete.getPreviousPlayer().setPosition(actionToDelete.getPreviousCase());
				this.nbMove--;
			}
		} else {
			res = ActionResult.ERROR;
		}
		
		return res;
	}
	
	public ActionResult deleteActions() {
		ActionResult res = ActionResult.DONE;
		
		while(this.nbMove + this.nbPass > 0 && res == ActionResult.DONE) {
			res = undo();
		}
		
		return res;
	}
}
