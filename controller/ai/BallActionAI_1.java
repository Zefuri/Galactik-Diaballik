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

public class BallActionAI_1{
	
	MaxMinBallAction brain;
	Random randomgene;
	Team team;
	
	public BallActionAI_1(Stadium stadium, Team team) {
		this.team = team;
		brain = new MaxMinBallAction(stadium, team, Integer.MIN_VALUE, Integer.MAX_VALUE);
		randomgene = new Random();
	}
	
	public ArrayList<Action> play(int depth) {
		//creation of result ArrayList
		ArrayList<Action> chooseActs = new ArrayList<>();
		
		//calcul of Must possibility turn
		brain.progress(depth);
		
		//take a random Turn on the list of MustActs
		ArrayList<ArrayList<Action>> mustActs = brain.getMustActs();
		int chooseActsNumber = randomgene.nextInt(mustActs.size());
		ArrayList<Action> tmp = mustActs.get(chooseActsNumber);
		
		//copy tmp in chooseActs
		for(Action action : tmp){
			chooseActs.add(action);
		}
		
		//End Turn add to end of ArrayList
		Action action = new Action(ActionType.END_TURN, null, null, null, null);
		chooseActs.add(action);
		
		return chooseActs;		
	}
	
	public static void main(String args[]){
		//test
		Stadium stadium = new Stadium();
		BallActionAI_1 test = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));
		
		ArrayList acts = test.play((int)(args[0].charAt(0)-'0'));
		for(int i = 0; i != acts.size(); i++){
			System.out.print(acts.get(i).toString());
		}
		
		System.out.println("");
	}
}
