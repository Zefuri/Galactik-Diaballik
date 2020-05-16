package controller.ai;

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

public class CoupAction {
	Stadium stadium;
	Team team;
	Player ballPlayer;
	
	boolean canAccess;
	int alpha;
	int beta;
	
	ArrayList<ArrayList<Action>> one;
	int[] oneHigh;
	ArrayList<ArrayList<Action>> two;
	int[] twoHigh;
	ArrayList<ArrayList<Action>> three;
	int[] threeHigh;
	
	ArrayList<ArrayList<Action>> acts;
	int avancement;
	
	ToolsBallAction tools;

	public CoupAction(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		ballPlayer = team.getBallPlayer();
		
		canAccess = true;
		this.alpha = alpha;
		this.beta = beta;
		
		tools = new ToolsBallAction();
		
	}
	
	
	public int numberOfAction() {
		return one.size() + two.size() + three.size();
	}
	
	
	public ArrayList<ArrayList<Action>> getOne() {
		return one;
	}
	

	public ArrayList<ArrayList<Action>> getTwo() {
		return two;
	}


	public ArrayList<ArrayList<Action>> getThree() {
		return three;
	}
	
	
	public boolean canAccess(){
		return canAccess;
	}
	
	
	public int getAlpha() {
		return alpha;
	}
	
	
	public int getBeta() {
		return beta;
	}
	
	
	public void setAlpha(int newAlpha) {
		alpha = newAlpha;
	}
	
	
	public void setBeta(int newBeta) {
		beta = newBeta;
	}
	
	
	public ArrayList<ArrayList<Action>> getActs() {
		return acts;
	}
	
	
	public int getAvancement() {
		return avancement;
	}
	
	
	public void init() {
		initOne();
		oneHigh = new int[one.size()];
		initTwo();
		twoHigh = new int[two.size()];
		initThree();
		threeHigh = new int[three.size()];
	}
	
	
	public void initValueMin() {
		avancement = Integer.MAX_VALUE;
		oneHigh = initValueMin(one);
		twoHigh = initValueMin(two);
		threeHigh = initValueMin(three);
	}
	
	
	public int[] initValueMin(ArrayList<ArrayList<Action>> actionList) {
		int[] valueList = new int[actionList.size()];
		
		int index = -1;
		for(ArrayList<Action> action : actionList){
			index++;
			exec(action);
				valueList[index] = (-1) * tools.ballAvance(team);
				if(valueList[index] < avancement) {
					avancement = valueList[index];
				}
			undo(action);
		}
		
		return valueList;
	}
	
	
	public void initValueMax() {
		avancement = Integer.MIN_VALUE;
		oneHigh = initValueMax(one);
		twoHigh = initValueMax(two);
		threeHigh = initValueMax(three);
	}
	
	
	public int[] initValueMax(ArrayList<ArrayList<Action>> actionList) {
		int[] valueList = new int[actionList.size()];
		
		int index = -1;
		for(ArrayList<Action> action : actionList){
			index++;
			exec(action);
				valueList[index] = tools.ballAvance(team);
				if(valueList[index] > avancement) {
					avancement = valueList[index];
				}
			undo(action);
		}
		
		return valueList;
	}
	
	
	public void initOne() {	
		one = new ArrayList<>();
		ArrayList<Action> actionOne;
		
		for(Player p : team.getPlayers()) {
			
			if(!p.equals(ballPlayer)){
			
				//pass
				if(stadium.playerCanPass(ballPlayer, p)) {
					actionOne = new ArrayList<>();
					Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
					Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
					actionOne.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
					one.add(actionOne);
				}
				
				//depl
				oneCanMove(p);
			}
		}
	}
	
	
	public void oneCanMove(Player player) {
		oneCanMoveDirection(player, MoveDirection.UP);
		oneCanMoveDirection(player, MoveDirection.RIGHT);
		oneCanMoveDirection(player, MoveDirection.DOWN);
		oneCanMoveDirection(player, MoveDirection.LEFT);
	}


	public void oneCanMoveDirection(Player player, MoveDirection direction) {
		if(stadium.playerCanMove(player, direction)){
			ArrayList<Action> actionOne = new ArrayList<>();
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			actionOne.add(new Action(ActionType.MOVE, null, player, previousCase, tools.nextCase(player.getPosition(), direction)));
			one.add(actionOne);
		}
	}
	

	public void initTwo() {
		two = new ArrayList<>();
		
		Player firstPlayer;
		Player ballPlayer2;
		boolean verifPass;
		boolean sameFirstPlayer;
		boolean lessPlayer;
		boolean verifBack;
		
		for(ArrayList<Action> actLook : one) {
			exec(actLook);
			for(Action a: actLook) {
			
				firstPlayer = actLook.get(0).getNextPlayer();
				ballPlayer2 = team.getBallPlayer();
				verifPass = actLook.get(0).getType() != ActionType.PASS; //You can't make two pass
				
				if(verifPass){
				//depl + pass or depl + depl

					for(Player p : team.getPlayers()) {
						
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
							ArrayList<Action> actionTwo = new ArrayList<>();
							actionTwo.add(actLook.get(0));
							Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
							Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
							actionTwo.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
							two.add(actionTwo);
						}
						
						lessPlayer = p.getNumero() < firstPlayer.getNumero();
						if(!lessPlayer   &&   !p.equals(ballPlayer)) {
							
							twoCanMove(actLook, p, firstPlayer);
							
						} else if(!p.equals(ballPlayer)) {
							
							tryRemplace1(actLook.get(0), p, actLook.get(0).getPreviousCase());
							
						}
					}
				}else{
				//pass + depl: only if depl is to the last player with ball
					twoCanMovePreviousBall(actLook.get(0));
				}
			}
			undo(actLook);
		}
	}
	
	
	public void twoCanMove(ArrayList<Action> actLook, Player player, Player firstPlayer) {
		twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.UP);
		twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.RIGHT);
		twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.DOWN);
		twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.LEFT);
	}
	
	
	public void twoCanMoveDirection(ArrayList<Action> actLook, Player player, Player firstPlayer, MoveDirection deplacement) {
		
		boolean sameFirstPlayer = player.equals(firstPlayer);
		boolean verifBack = !sameFirstPlayer   ||   actLook.get(0).inverse().getDirection() != deplacement; //You can't back
		
		if(verifBack   &&   stadium.playerCanMove(player, deplacement)) {
			
			ArrayList<Action> actionTwo = new ArrayList<>();
			actionTwo.add(actLook.get(0));
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			actionTwo.add(new Action(ActionType.MOVE, null, player, previousCase, tools.nextCase(player.getPosition(), deplacement)));
			
			two.add(actionTwo);
		}
	}
	

	public void tryRemplace1(Action action, Player player, Case previousCase) {
		tryRemplace2(action, player, previousCase, MoveDirection.UP);
		tryRemplace2(action, player, previousCase, MoveDirection.RIGHT);
		tryRemplace2(action, player, previousCase, MoveDirection.DOWN);
		tryRemplace2(action, player, previousCase, MoveDirection.LEFT);
	}
	
	
	public void tryRemplace2(Action action, Player player, Case previousCase, MoveDirection direction) {
		
		if(stadium.playerCanMove(player, direction)) {
		
			Case nextDirectionCase = tools.nextCase(player.getPosition(), direction);
			boolean canRemplace = nextDirectionCase.getX() == previousCase.getX()   &&   nextDirectionCase.getY() == previousCase.getY();
			
			if(canRemplace) {
				ArrayList<Action> actionTwo = new ArrayList<>();
				actionTwo.add(action);
				Case previousPlayerCase = new Case(player.getPosition().getX(), player.getPosition().getY());
				actionTwo.add(new Action(ActionType.MOVE, null, player, previousPlayerCase, previousCase));
				two.add(actionTwo);
			}
		
		}
	}
	

	public void twoCanMovePreviousBall(Action action) {
		twoCanMovePreviousBallDeplacement(action, MoveDirection.UP);
		twoCanMovePreviousBallDeplacement(action, MoveDirection.RIGHT);
		twoCanMovePreviousBallDeplacement(action, MoveDirection.DOWN);
		twoCanMovePreviousBallDeplacement(action, MoveDirection.LEFT);
	}

							
	public void twoCanMovePreviousBallDeplacement(Action action, MoveDirection deplacement) {
		
		if(stadium.playerCanMove(ballPlayer, deplacement)){
			ArrayList<Action> actionTwo = new ArrayList<>();
			actionTwo.add(action);
			Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
			actionTwo.add(new Action(ActionType.MOVE, null, ballPlayer, previousCase, tools.nextCase(ballPlayer.getPosition(), deplacement)));
			 
			 two.add(actionTwo);
		}	
	}
	
	
	public void initThree() {
		three = new ArrayList<>();
		
		Player firstPlayer;
		Player ballPlayer2;
		boolean verifPass;
		boolean verifBack;
		boolean sameFirstPlayer;
		boolean verifCondition;
		
		for(ArrayList<Action> actLook : two) {
			exec(actLook);
				ballPlayer2 = team.getBallPlayer();
				verifPass = actLook.get(0).getType() != ActionType.PASS && actLook.get(1).getType() != ActionType.PASS; //You can't make two pass
				
				if(verifPass) {
				//depl + depl + pass
					
					for(Player p : team.getPlayers()) {
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
							ArrayList<Action> actionThree = new ArrayList<>();
			 				actionThree.add(actLook.get(0));
			 				actionThree.add(actLook.get(1));
			 				Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
			 				Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
			 				actionThree.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
			 
			 				three.add(actionThree);
						}
					}
				
					
				} else if(actLook.get(0).getType() == ActionType.PASS) {
				//pass + depl +depl: same condition of 2 for
					firstPlayer = actLook.get(1).getNextPlayer();

					for(Player p : team.getPlayers()) {
						threeCanMove(actLook, p, firstPlayer);
					}
					
				} else {
				//depl + pass + depl: only if the pass is to the player moved and now you move the player before with ball
				
					verifCondition = actLook.get(0).getNextPlayer().equals(actLook.get(1).getNextPlayer());
					
					if(verifCondition){
						threeCanMovePreviousBall(actLook);
					}
				}

			undo(actLook);	
		}
	}


	public void threeCanMove(ArrayList<Action> actLook, Player player, Player firstPlayer){
		threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.UP);
		threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.RIGHT);
		threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.DOWN);
		threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.LEFT);
	}
	

	public void threeCanMoveDeplacement(ArrayList<Action> actLook, Player player, Player firstPlayer, MoveDirection deplacement) {
		
		boolean sameFirstPlayer = player.equals(firstPlayer);
		boolean verifBack = !sameFirstPlayer   ||   actLook.get(1).inverse().getDirection() != deplacement; //You can't back
		
		if(verifBack   &&   !player.equals(team.getBallPlayer())   &&   stadium.playerCanMove(player, deplacement)) {
			ArrayList<Action> actionThree = new ArrayList<>();
			actionThree.add(actLook.get(0));
			actionThree.add(actLook.get(1));
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			actionThree.add(new Action(ActionType.MOVE, null, player, previousCase, tools.nextCase(player.getPosition(), deplacement)));
			
			three.add(actionThree);
		}
		
	}


	public void threeCanMovePreviousBall(ArrayList<Action> action) {
		threeCanMovePreviousBallDeplacement(action, MoveDirection.UP);
		threeCanMovePreviousBallDeplacement(action, MoveDirection.RIGHT);
		threeCanMovePreviousBallDeplacement(action, MoveDirection.DOWN);
		threeCanMovePreviousBallDeplacement(action, MoveDirection.LEFT);
	}
	
	
	public void threeCanMovePreviousBallDeplacement(ArrayList<Action> action, MoveDirection deplacement) {
		
		if(stadium.playerCanMove(ballPlayer, deplacement)) {
			ArrayList<Action> actionThree = new ArrayList<>();
			actionThree.add(action.get(0));
			actionThree.add(action.get(1));
			Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
			actionThree.add(new Action(ActionType.MOVE, null, ballPlayer, previousCase, tools.nextCase(ballPlayer.getPosition(), deplacement)));
			 
			 three.add(actionThree);
		}
	}
	
	
	public void initActs() {
		acts = new ArrayList<>();
		for(int i = 0; i != numberOfAction(); i++) {
		
			if(i < one.size()   &&   avancement == oneHigh[i]) {
				acts.add(one.get(i));
			} else if(-1 < i-one.size()   &&   i-one.size() < two.size()   &&   avancement == twoHigh[i-one.size()]) {
				acts.add(two.get(i-one.size()));
			} else if(-1 < i-one.size()-two.size()   &&   avancement == threeHigh[i-one.size()-two.size()]) {
				acts.add(three.get(i-one.size()-two.size()));
			}
		}
	}
	
	
	public void exec(int actionNumero) {
		if(actionNumero < one.size()) {
			exec(one.get(actionNumero));
		} else if(actionNumero - one.size() < two.size()) {
			exec(two.get(actionNumero - one.size()));
		} else {
			exec(three.get(actionNumero - one.size() - two.size()));
		}
	}
	
	
	public void exec(ArrayList<Action> instruction) {
		for(Action action : instruction) {
			exec(action);
		}
	}
	
	
	public void exec(Action action) {
		
		if(action.getType() == ActionType.PASS) {
			stadium.pass(action.getPreviousPlayer(), action.getNextPlayer());
		} else {
			stadium.move(action.getNextPlayer(), action.getDirection());
		}
	}
	
	
	public void undo(int actionNumero) {
		if(actionNumero < one.size()) {
			undo(one.get(actionNumero));
		} else if(actionNumero - one.size() < two.size()) {
			undo(two.get(actionNumero - one.size()));
		} else {
			undo(three.get(actionNumero - one.size() - two.size()));
		}
	}
	
	
	public void undo(ArrayList<Action> instruction) {
		for(int i = instruction.size()-1; i != -1; i--) {
			exec(instruction.get(i).inverse());
		}
	}
	
	
	public void reportMax(int actionNumero, int value) {
		if(value > beta) {
			canAccess = false;
		}
		
		if(actionNumero == 0 || value > avancement) {
			avancement = value;
		}
		
		report(actionNumero, value);
	}
	
	
	public void reportMin(int actionNumero, int value) {
		if(value < alpha) {
			canAccess = false;
		}
	
		if(actionNumero == 0 || value < avancement) {
			avancement = value;
		}
		
		report(actionNumero, value);
	}
	
	
	public void report(int actionNumero, int value) {
		if(actionNumero < one.size()) {
			oneHigh[actionNumero] = value;
		} else if(actionNumero - one.size() < two.size()) {
			twoHigh[actionNumero - one.size()] = value;
		} else {
			threeHigh[actionNumero - one.size() - two.size()] = value;
		}
	}
}
