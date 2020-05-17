package controller.ai;

import java.util.ArrayList;

import model.Stadium;
import model.Team;
import model.Action;

public class MinMaxBallAction_instr {
	Stadium stadium;
	Team team;
	CoupAction coup;


	public MinMaxBallAction_instr(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupAction(stadium, team, alpha, beta);
	}
	
	
	public ArrayList<ArrayList<Action>> getWorstActs(){
		return coup.getActs();
	}
	
	
	public int getWorstAvancement(){
		return coup.getAvancement();
	}
	
	
	public void progress(int checkingDepth) {
		coup.init();
		
		if(checkingDepth != 0) {
			MaxMinBallAction_instr maxCheck;
			
			for(int i = 0; coup.canAccess()   &&   i != coup.numberOfAction(); i++) {
			
				if(i != 0 && coup.getAvancement() < coup.getBeta()) {
					coup.setBeta(coup.getAvancement());
				}
			
				coup.exec(i);
					maxCheck = new MaxMinBallAction_instr(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
					maxCheck.progress(checkingDepth-1);
					coup.reportMin(i, maxCheck.getMustAvancement());
				coup.undo(i);
			}
			
		} else {
			coup.initValueMin();
		}
		
		coup.initActs();
	}

	
	public static void main(String args[]){
		//tests
	}
}
