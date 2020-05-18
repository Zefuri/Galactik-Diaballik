package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class MinMaxBallString{
	Stadium stadium;
	Team team;
	CoupString coup;

	public MinMaxBallString(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupString(stadium, team, alpha, beta);
	}
	
	
	public String[] getWorstActs(){
		return coup.getActs();
	}
	
	
	public int getWorstAvancement(){
		return coup.getAvancement();
	}
	
	
	public void progress(int checkingDepth){
		coup.init();
		
		if(checkingDepth != 0){
			MaxMinBallString maxCheck;
			
			for(int i = 0; coup.canAccess() && i != coup.numberOfAction(); i++){
			
				if(i != 0 && coup.getAvancement() < coup.getBeta()) {
					coup.setBeta(coup.getAvancement());
				}
			
				coup.exec(i);
					maxCheck = new MaxMinBallString(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
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
