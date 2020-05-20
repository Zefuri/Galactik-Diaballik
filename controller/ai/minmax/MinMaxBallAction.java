package controller.ai.minmax;

import java.util.ArrayList;

import model.Stadium;
import model.Team;
import model.Action;

public class MinMaxBallAction {
	Stadium stadium;
	Team team;
	CoupAction coup;


	public MinMaxBallAction(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupAction(stadium, team, alpha, beta);
	}
	
	
	public boolean canFinish() {
		return coup.canFinish();
	}
	
	
	public ArrayList<ArrayList<Action>> getWorstActs(){
		return coup.getActs();
	}
	
	
	public int getWorstAvancement(){
		return coup.getAvancement();
	}
	
	
	public void progress(int checkingDepth) {
		coup.init();
		
		if(!canFinish()){	
			if(checkingDepth != 0) {
				MaxMinBallAction maxCheck;
				
				for(int i = 0; coup.canAccess()   &&   i != coup.numberOfAction(); i++) {
				
					if(i != 0 && coup.getAvancement() < coup.getBeta()) {
						coup.setBeta(coup.getAvancement());
					}
				
					coup.exec(i);
						maxCheck = new MaxMinBallAction(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
						maxCheck.progress(checkingDepth-1);
						if(maxCheck.canFinish()){
							coup.reportMin(i, Integer.MAX_VALUE);
						} else {
							coup.reportMin(i, maxCheck.getMustAvancement());
						}
					coup.undo(i);
				}
				
			} else {
				coup.initValueMin();
			}
			
			coup.initActs();
		}
	}

	
	public static void main(String args[]){
		//tests
	}
}
