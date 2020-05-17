package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class MaxMinBallString_instr{
	Stadium stadium;
	Team team;
	CoupString coup;
	
	public MaxMinBallString_instr(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupString(stadium, team, alpha, beta);
		System.out.println(stadium.toString());
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
			MinMaxBallString_instr minCheck;
			
			for(int i = 0; coup.canAccess()   &&   i != coup.numberOfAction(); i++){
			
				if(i != 0 && coup.getAvancement() > coup.getAlpha()) {
					coup.setAlpha(coup.getAvancement());
				}
			
				coup.exec(i);
					minCheck = new MinMaxBallString_instr(stadium, team.getEnemyTeam(), coup.getAlpha(), coup.getBeta());
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
		Stadium stadium = new Stadium();
		MaxMinBallString_instr test = new MaxMinBallString_instr(stadium, stadium.getTeam(TeamPosition.BOTTOM), Integer.MIN_VALUE, Integer.MAX_VALUE);
		test.progress((int)(args[0].charAt(args[0].length()-1)-'0'));
		System.out.println("Pour une prévision de: "+args[0]);
		System.out.println("Obtenu par: ");
		for(String turn : test.getMustActs()) {
			System.out.print(turn+" - ");
		}
		System.out.println("");	
	}
}
