package controller.ai;

import java.util.ArrayList;

import model.Stadium;
import model.Team;
import model.Action;
import model.enums.TeamPosition;

public class MaxMinBallAction {
	Stadium stadium;
	Team team;
	CoupAction coup;


	public MaxMinBallAction(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupAction(stadium, team, alpha, beta);
		
	}
	
	
	public ArrayList<ArrayList<Action>> getMustActs(){
		return coup.getActs();
	}
	
	
	public int getMustAvancement(){
		return coup.getAvancement();
	}
	
	
	public void progress(int checkingDepth) {
		coup.init();
		
		if(checkingDepth != 0) {
			MinMaxBallAction minCheck;
			
			for(int i = 0; coup.canAccess()   &&   i != coup.numberOfAction(); i++) {
			
				if(i != 0 && coup.getAvancement() > coup.getAlpha()) {
					coup.setAlpha(coup.getAvancement());
				}
			
				coup.exec(i);
					minCheck = new MinMaxBallAction(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
					minCheck.progress(checkingDepth-1);
					coup.reportMax(i, minCheck.getWorstAvancement());
				coup.undo(i);
			}
			
		} else {
			coup.initValueMax();
		}
		
		coup.initActs();
	}

	
	public static void main(String args[]){
		//tests
	}
}