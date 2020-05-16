//package controller.ai;

import java.util.ArrayList;

import model.Stadium;
import model.Team;
import model.Player;
import model.Case;
import model.Action;
import model.ModelConstants;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.enums.ActionType;

public class MinMaxBallAction_instr {
	Stadium stadium;
	Team team;
	CoupAction coup;


	public MinMaxBallAction_instr(Stadium stadium, Team team) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupAction(stadium, team);
	}
	
	
	public ArrayList<ArrayList<Action>> getWorstActs(){
		return coup.getWorstActs();
	}
	
	
	public int getWorstAvancement(){
		return coup.getWorstAvancement();
	}
	
	
	public void progress(int checkingDepth) {
		coup.init();
		
		if(checkingDepth != 0) {
			MaxMinBallAction_instr maxCheck;
			
			for(int i = 0; i != coup.numberOfAction(); i++) {
			
				coup.exec(i);
					maxCheck = new MaxMinBallAction_instr(stadium, team.getEnemyTeam());
					maxCheck.progress(checkingDepth-1);
					coup.report(i, maxCheck.getMustAvancement());
				coup.undo(i);
			}
			
		} else {
			coup.initValueMin();
		}
		
		coup.initMin();
	}

	
	public static void main(String args[]){
		//tests
	}
}
