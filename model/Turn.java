package model;

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
}
