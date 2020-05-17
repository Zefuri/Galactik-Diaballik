package controller.ai;

import java.util.ArrayList;
import java.util.Random;

import model.Stadium;
import model.Team;
import model.Player;
import model.Case;
import model.enums.ActionType;
import model.enums.TeamPosition;
import model.Action;

public class BallStringAI_1{
	
	MaxMinBallString brain;
	Random randomgene;
	Team team;
	
	public BallStringAI_1(Stadium stadium, Team team) {
		this.team = team;
		brain = new MaxMinBallString(stadium, team, Integer.MIN_VALUE, Integer.MAX_VALUE);
		randomgene = new Random();
	}
	
	public ArrayList<Action> play(int depth) {
		//creation of result ArrayList
		ArrayList<Action> chooseActs = new ArrayList<>();
		
		//calcul of Must possibility turn
		brain.progress(depth);
		
		//take a random Turn on the list of MustActs
		String[] mustActs = brain.getMustActs();
		int chooseActNumber = randomgene.nextInt(mustActs.length);
		String chooseAct = mustActs[chooseActNumber];
		
		//creation of Action of chooseAct thanks to String of chooseAct
		Action action;
		ActionType actionType;
		Player previousPlayer;
		Player nextPlayer;
		Case previousCase;
		Case nextCase;
		for(int i = 0; i != chooseAct.length(); i = i+2){
			nextPlayer = team.playerOfInt((int)(chooseAct.charAt(i) - '0'));
			
			if(chooseAct.charAt(i+1) == 'P') {
			
				actionType = ActionType.PASS;
				previousPlayer = team.getBallPlayer();
				previousCase = previousPlayer.getPosition();
				nextCase = nextPlayer.getPosition();
				
			} else {
			
				actionType = ActionType.MOVE;
				previousPlayer = null;
				previousCase = nextPlayer.getPosition();
				if(chooseAct.charAt(i+1) == 'U'){
					nextCase = new Case(previousCase.getX()-1 , previousCase.getY());
				} else if(chooseAct.charAt(i+1) == 'D') {
					nextCase = new Case(previousCase.getX()+1 , previousCase.getY());
				} else if(chooseAct.charAt(i+1) == 'R') {
					nextCase = new Case(previousCase.getX() , previousCase.getY()+1);
				} else {
					nextCase = new Case(previousCase.getX() , previousCase.getY()-1);
				}		
			}
			
			action = new Action(actionType, previousPlayer, nextPlayer, previousCase, nextCase);
			chooseActs.add(action);
						
		}
		
		//End Turn add to end of ArrayList
		action = new Action(ActionType.END_TURN, null, null, null, null);
		chooseActs.add(action);
		
		return chooseActs;		
	}
	
	public static void main(String args[]){
		//test
		Stadium stadium = new Stadium();
		BallStringAI_1 test = new BallStringAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));
		
		ArrayList acts = test.play((int)(args[0].charAt(0)-'0'));
		for(int i = 0; i != acts.size(); i++){
			System.out.print(acts.get(i).toString());
		}
		
		System.out.println("");
	}
}
