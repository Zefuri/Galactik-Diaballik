package model;

import model.enums.ActionResult;
import model.enums.ActionType;

public class Turn {
	private Action actions[];
	private Team team;
	private int nbPass;
	private int nbMove;
	private boolean cheatModActivated;


	public Turn(Team team) {
		this.actions = new Action[]{null, null, null};
		this.team = team;
		this.nbPass = 0;
		this.nbMove = 0;
		this.cheatModActivated = false;
	}

	public Turn(Team team, boolean cheat) {
		this.actions = new Action[3];
		this.team = team;
		this.nbPass = 0;
		this.nbMove = 0;
		this.cheatModActivated = cheat;
	}
	
	public void addAction(Action action) {
		this.actions[this.nbMove + this.nbPass] = action;
		
		if (action.getType() == ActionType.PASS && !cheatModActivated) {
			this.nbPass++;
		} else if (action.getType() == ActionType.MOVE && !cheatModActivated) {
			this.nbMove++;
		}
	}
	
	public Action[] getActions() {
		return this.actions;
	}
	
	public Action getAction(int index) {
		return this.actions[index];
	}
	
	public Action getFirstAction() {
		return this.getAction(0);
	}
	
	public Action getSecondAction() {
		return this.getAction(1);
	}
	
	public Action getThirdAction() {
		return this.getAction(2);
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
		
		if (this.nbMove + this.nbPass > 0) {
			Action actionToDelete = this.actions[(this.nbPass + this.nbMove) - 1];
			
			if(actionToDelete.getType() == ActionType.PASS) {
				actionToDelete.getPreviousPlayer().setBallPossession(true);
				actionToDelete.getNextPlayer().setBallPossession(false);
				if(!cheatModActivated) {
					this.nbPass--;
				}
			} else {
				actionToDelete.getPreviousPlayer().setPosition(actionToDelete.getPreviousCase());
				if(!cheatModActivated) {
					this.nbMove--;
				}
			}
			
			//We clear the action properly (better for the save) if we are not in visualization mode
			if (!this.team.getStadium().isInVisualisationMode()) {
				this.actions[this.nbPass + this.nbMove] = null;
			}
		} else {
			res = ActionResult.ERROR;
		}
		
		return res;
	}
	
	public void redo() {
		Action actionToPerform = this.actions[this.nbPass + this.nbMove];
		
		if (actionToPerform.getType() == ActionType.PASS) {
			actionToPerform.getPreviousPlayer().setBallPossession(false);
			actionToPerform.getNextPlayer().setBallPossession(true);
			this.nbPass++;
		} else {
			actionToPerform.getPreviousPlayer().setPosition(actionToPerform.getNextCase());
			this.nbMove++;
		}
	}
	
	public ActionResult deleteActions() {
		ActionResult res = ActionResult.ERROR;
		
		while (this.nbMove + this.nbPass > 0) {
			res = undo();
		}
		
		return res;
	}

	public Turn inverse() {
		Turn inverseTurn = new Turn(team);

        for (int i = 2; i >= 0; i--) {
			Action a = actions[i];

			if (a.getType() != ActionType.END_TURN) {
				inverseTurn.addAction(a.inverse());
			}
		}

		if (inverseTurn.nbMove + inverseTurn.nbPass < 3) {
			inverseTurn.addAction(new Action(ActionType.END_TURN, null, null, null, null));
		}

		return inverseTurn;
	}

	public Turn copy() {
		Turn res = new Turn(team);

		res.actions[0] = actions[0];
		res.actions[1] = actions[1];
		res.actions[2] = actions[2];
		res.nbPass = nbPass;
		res.nbMove = nbMove;

		return res;
	}

	public String toString(){
		StringBuilder builder = new StringBuilder();
		
		for(Action a : actions){
			builder.append((a != null) ? a.toString() : "NA\n\n");
		}
		
		return builder.toString();
	}
	
	public void setNbMovesDone(int nbMoves) {
		this.nbMove = nbMoves;
	}
	
	public void setNbPassesDone(int nbPasses) {
		this.nbPass = nbPasses;
	}
	
	public boolean undoGoesToPreviousTour() {
		return this.nbPass + this.nbMove == 0;
	}
	
	public boolean redoGoesToNextTour() {
		if (this.nbMove + this.nbPass == 3) {
			return true;
		}
		
		return (this.actions[this.nbPass + this.nbMove] == null);
	}

	public void switchCheatModActivated() {
		this.cheatModActivated = !cheatModActivated;
	}
	
	public boolean isEmpty() {
		return this.actions[0] == null && this.actions[1] == null && this.actions[2] == null;
	} 
}
