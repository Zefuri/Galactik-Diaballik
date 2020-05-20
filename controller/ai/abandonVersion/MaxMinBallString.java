package ai.abandonVersion;

import model.Stadium;
import model.Team;

public class MaxMinBallString{
	Stadium stadium;
	Team team;
	CoupString coup;
	
	public MaxMinBallString(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupString(stadium, team, alpha, beta);
	}
	
	
	public String[] getMustActs(){
		return coup.getActs();
	}
	
	
	public int getMustAvancement(){
		return coup.getAvancement();
	}
	
	
	public void progress(int checkingDepth){
		coup.init();
				
		if(checkingDepth != 0){
			MinMaxBallString minCheck;
			
			for(int i = 0; coup.canAccess()   &&   i != coup.numberOfAction(); i++){
			
				if(i != 0 && coup.getAvancement() > coup.getAlpha()) {
					coup.setAlpha(coup.getAvancement());
				}
			
				coup.exec(i);
					minCheck = new MinMaxBallString(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
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
