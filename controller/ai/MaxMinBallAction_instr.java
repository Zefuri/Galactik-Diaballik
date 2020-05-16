package controller.ai;

import java.util.ArrayList;

import model.Stadium;
import model.Team;
import model.Action;
import model.enums.TeamPosition;

public class MaxMinBallAction_instr {
	Stadium stadium;
	Team team;
	CoupAction coup;


	public MaxMinBallAction_instr(Stadium stadium, Team team) {
		this.stadium = stadium;
		this.team = team;
		this.coup = new CoupAction(stadium, team);
		System.out.println(stadium.toString());
		
	}
	
	
	public ArrayList<ArrayList<Action>> getMustActs(){
		return coup.getMustActs();
	}
	
	
	public int getMustAvancement(){
		return coup.getMustAvancement();
	}
	
	
	public void progress(int checkingDepth) {
		coup.init();
		
		if(checkingDepth != 0) {
			MinMaxBallAction_instr minCheck;
			
			for(int i = 0; i != coup.numberOfAction(); i++) {
			
				coup.exec(i);
					minCheck = new MinMaxBallAction_instr(stadium, team.getEnemyTeam());
					minCheck.progress(checkingDepth-1);
					coup.report(i, minCheck.getWorstAvancement());
				coup.undo(i);
			}
			
		} else {
			coup.initValueMax();
		}
		
		coup.initMax();
	}

	
	public static void main(String args[]){
		//tests
		Stadium stadium = new Stadium();
		MaxMinBallAction_instr test = new MaxMinBallAction_instr(stadium, stadium.getTeam(TeamPosition.BOTTOM));
		test.progress((int)(args[0].charAt(args[0].length()-1)-'0'));
		System.out.println("Pour une prévision de: "+args[0]);
		System.out.println("Obtenu par: ");
		for(ArrayList<Action> turn : test.getMustActs()) {
			for(Action action : turn) {
				System.out.print(action.toString());
			}
			System.out.print(" - ");
		}
		System.out.println("");	
	}
}
