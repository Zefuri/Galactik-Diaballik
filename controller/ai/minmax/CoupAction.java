package ai.minmax;

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
	
	boolean canFinish;
	
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

	//Constructeur
	public CoupAction(Stadium stadium, Team team, int alpha, int beta) {
		this.stadium = stadium;
		this.team = team;
		this.ballPlayer = team.getBallPlayer();
		
		this.one = new ArrayList<>();
		this.two = new ArrayList<>();
		this.three = new ArrayList<>();
		this.acts = new ArrayList<>();
		
		this.canFinish = false;
		this.canAccess = true;
		this.alpha = alpha;
		this.beta = beta;
		
		this.tools = new ToolsBallAction();
	}
	
	//return true if can make a final action
	public boolean canFinish() {
		return canFinish;
	}
	
	//return number of possibility action
	public int numberOfAction() {
		return one.size() + two.size() + three.size();
	}
	
	//return trun with numero reference
	public ArrayList<Action> getTurn(int numero) {
		if(numero < one.size()) {
			return one.get(numero);
		} else if(numero - one.size() < two.size()) {
			return two.get(numero - one.size());
		} else {
			return three.get(numero - one.size() - two.size());
		}
	}
	
	//return list of turn can make with one act
	public ArrayList<ArrayList<Action>> getOne() {
		return one;
	}
	
	//return list of turn can make with two acts
	public ArrayList<ArrayList<Action>> getTwo() {
		return two;
	}

	//return list of turn can make with three acts
	public ArrayList<ArrayList<Action>> getThree() {
		return three;
	}
	
	//return boolean for alpha beta cut
	public boolean canAccess(){
		return canAccess;
	}
	
	//return value for alpha cut
	public int getAlpha() {
		return alpha;
	}
	
	//return value for beta cut
	public int getBeta() {
		return beta;
	}
	
	//set value for alpha cut
	public void setAlpha(int newAlpha) {
		alpha = newAlpha;
	}
	
	//set value for beta cut
	public void setBeta(int newBeta) {
		beta = newBeta;
	}
	
	//return list of acts to have the must or worst result
	public ArrayList<ArrayList<Action>> getActs() {
		return acts;
	}
	
	//return value of must or worst acts
	public int getAvancement() {
		return avancement;
	}
	
	//research all possibility turn
	public void init() {
		initOne(); 
		if(!canFinish) { initTwo();
			if(!canFinish) { initThree();
		}	}
		
		oneHigh = new int[one.size()];
		twoHigh = new int[two.size()];
		threeHigh = new int[three.size()];
	}
	
	//calculate all value to adverse turn
	public void initValueMin() {
		avancement = Integer.MAX_VALUE;
		oneHigh = initValueMin(one);
		twoHigh = initValueMin(two);
		threeHigh = initValueMin(three);
	}
	
	//calculate value to adverse turn in actionList
	private int[] initValueMin(ArrayList<ArrayList<Action>> actionList) {
		int[] valueList = new int[actionList.size()];
		
		int index = -1;
		for(ArrayList<Action> action : actionList){
			index++;
			exec(action);
				if(stadium.antiplay(team)) {
					valueList[index] = Integer.MAX_VALUE;
				} else {
					valueList[index] = (-1) * tools.ballAvance(team);
				}
				
				if(valueList[index] < avancement) {
					avancement = valueList[index];
				}
			undo(action);
		}
		
		return valueList;
	}
	
	//calculate all value to adverse turn
	public void initValueMax() {
		avancement = Integer.MIN_VALUE;
		oneHigh = initValueMax(one);
		twoHigh = initValueMax(two);
		threeHigh = initValueMax(three);
	}
	
	//calculate value to adverse turn in actionList
	private int[] initValueMax(ArrayList<ArrayList<Action>> actionList) {
		int[] valueList = new int[actionList.size()];
		
		int index = -1;
		for(ArrayList<Action> action : actionList){
			index++;
			exec(action);
				if(stadium.antiplay(team)) {
					valueList[index] = Integer.MIN_VALUE;
				} else {
					valueList[index] = tools.ballAvance(team);
				}
				
				if(valueList[index] > avancement) {
					avancement = valueList[index];
				}
			undo(action);
		}
		
		return valueList;
	}
	
	//reseash turn with one action
	private void initOne() {
		ArrayList<Action> actionOne;
		
		for(int i = 0; !canFinish   &&   i != team.getPlayers().size(); i++) {
			Player p = team.playerOfInt(i);
			if(!p.equals(ballPlayer)) {
			
				//pass
				if(stadium.playerCanPass(ballPlayer, p)) {
					actionOne = new ArrayList<>();
					Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
					Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
					actionOne.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
					one.add(actionOne);
					
					if(p.getPosition().getX() == team.getButLine()) {
						canFinish = true;
						acts.add(tools.clone(actionOne));
					}
				}
				
				//depl
				oneCanMove(p);
			}
		}
	}
	
	//research all action: depl
	private void oneCanMove(Player player) {
		if(!canFinish) { oneCanMoveDirection(player, MoveDirection.UP);
			if(!canFinish) { oneCanMoveDirection(player, MoveDirection.RIGHT);
				if(!canFinish) { oneCanMoveDirection(player, MoveDirection.DOWN);
					if(!canFinish) { oneCanMoveDirection(player, MoveDirection.LEFT);
		}	}	}	}
	}

	//test action: depl with the direction
	private void oneCanMoveDirection(Player player, MoveDirection direction) {
		if(stadium.playerCanMove(player, direction)){
			ArrayList<Action> actionOne = new ArrayList<>();
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			actionOne.add(new Action(ActionType.MOVE, player, player, previousCase, tools.nextCase(player.getPosition(), direction)));
			one.add(actionOne);
			exec(actionOne);
				if(stadium.antiplay(team.getEnemyTeam())){
					canFinish = true;
					acts.add(tools.clone(actionOne));
				}
			undo(actionOne);
		}
	}
	
	//reseash turn with two actions
	private void initTwo() {
		
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

					for(int i = 0; !canFinish   &&   i != team.getPlayers().size(); i++) {
						Player p = team.playerOfInt(i);
						
						//depl + pass
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
							ArrayList<Action> actionTwo = new ArrayList<>();
							actionTwo.add(actLook.get(0));
							Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
							Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
							actionTwo.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
							two.add(actionTwo);
							
							if(p.getPosition().getX() == team.getButLine()) {
								canFinish = true;
								acts.add(tools.clone(actionTwo));
							}
						}
						
						if(!canFinish) {
							//depl + depl
							lessPlayer = p.getNumero() < firstPlayer.getNumero();
							if(!lessPlayer   &&   !p.equals(ballPlayer)) {
								
								twoCanMove(actLook, p, firstPlayer);
								
							} else if(!p.equals(ballPlayer)) {
								
								tryRemplace1(actLook.get(0), p, actLook.get(0).getPreviousCase());
								
							}
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
	
	//research all action: depl_1 + depl_2 with number of player in depl_1 < number of player in depl_2
	private void twoCanMove(ArrayList<Action> actLook, Player player, Player firstPlayer) {
		twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.UP);
		if(!canFinish) { twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.RIGHT);
			if(!canFinish) { twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.DOWN);
				if(!canFinish) { twoCanMoveDirection(actLook, player, firstPlayer, MoveDirection.LEFT);
		}	}	}
	}
	
	
	//test action: depl_1 + depl_2 with number of player in depl_1 < number of player in depl_2 in the deplacement
	private void twoCanMoveDirection(ArrayList<Action> actLook, Player player, Player firstPlayer, MoveDirection deplacement) {
		
		boolean sameFirstPlayer = player.equals(firstPlayer);
		boolean verifBack = !sameFirstPlayer   ||   actLook.get(0).inverse().getDirection() != deplacement; //You can't back
		
		if(verifBack   &&   stadium.playerCanMove(player, deplacement)) {
			
			ArrayList<Action> actionTwo = new ArrayList<>();
			actionTwo.add(actLook.get(0));
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			
			Action newAction = new Action(ActionType.MOVE, player, player, previousCase, tools.nextCase(player.getPosition(), deplacement));
			
			actionTwo.add(newAction);
			two.add(actionTwo);
			
			exec(newAction);
				if(stadium.antiplay(team.getEnemyTeam())){
					canFinish = true;
					acts.add(actionTwo);
				}
			undo(newAction);
		}
	}
	
	//research action: depl_1 + depl_2 with number of player in depl_1 > number of player in depl_2
	//this actions are only remplace first player
	private void tryRemplace1(Action action, Player player, Case previousCase) {
		tryRemplace2(action, player, previousCase, MoveDirection.UP);
		if(!canFinish) { tryRemplace2(action, player, previousCase, MoveDirection.RIGHT);
			if(!canFinish) { tryRemplace2(action, player, previousCase, MoveDirection.DOWN);
				if(!canFinish) { tryRemplace2(action, player, previousCase, MoveDirection.LEFT);
		}	}	}
	}
	
	//test action: depl_1 + depl_2 with number of player in depl_1 >= number of player in depl_2 in the deplacement
	private void tryRemplace2(Action action, Player player, Case previousCase, MoveDirection direction) {
		
		if(stadium.playerCanMove(player, direction)) {
		
			Case nextDirectionCase = tools.nextCase(player.getPosition(), direction);
			boolean canRemplace = nextDirectionCase.getX() == previousCase.getX()   &&   nextDirectionCase.getY() == previousCase.getY();
			
			if(canRemplace) {
				ArrayList<Action> actionTwo = new ArrayList<>();
				actionTwo.add(action);
				Case previousPlayerCase = new Case(player.getPosition().getX(), player.getPosition().getY());
			
				Action newAction = new Action(ActionType.MOVE, player, player, previousPlayerCase, previousCase);
			
				actionTwo.add(newAction);
				two.add(actionTwo);
			
				exec(newAction);
					if(stadium.antiplay(team.getEnemyTeam())){
						canFinish = true;
						acts.add(actionTwo);
					}
				undo(newAction);
			}
		
		}
	}
	
	//research action: pass + depl
	//this action are only move the last ballPlayer
	private void twoCanMovePreviousBall(Action action) {
		if(!canFinish) { twoCanMovePreviousBallDeplacement(action, MoveDirection.UP);
			if(!canFinish) { twoCanMovePreviousBallDeplacement(action, MoveDirection.RIGHT);
				if(!canFinish) { twoCanMovePreviousBallDeplacement(action, MoveDirection.DOWN);
					if(!canFinish) { twoCanMovePreviousBallDeplacement(action, MoveDirection.LEFT);
		}	}	}	}
	}

	//test action: pass + depl in the deplacement
	private void twoCanMovePreviousBallDeplacement(Action action, MoveDirection deplacement) {
		
		if(stadium.playerCanMove(ballPlayer, deplacement)){
			ArrayList<Action> actionTwo = new ArrayList<>();
			actionTwo.add(action);
			Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
		
			Action newAction = new Action(ActionType.MOVE, ballPlayer, ballPlayer, previousCase, tools.nextCase(ballPlayer.getPosition(), deplacement));
		
			actionTwo.add(newAction);
			 two.add(actionTwo);
			
			exec(newAction);
				if(stadium.antiplay(team.getEnemyTeam())){
					canFinish = true;
					acts.add(actionTwo);
				}
			undo(newAction);
		}	
	}
	
	//reseash turn with three actions
	public void initThree() {
		
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
					
					for(int i = 0; !canFinish   &&   i != team.getPlayers().size(); i++) {
						Player p = team.playerOfInt(i);
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
							ArrayList<Action> actionThree = new ArrayList<>();
			 				actionThree.add(actLook.get(0));
			 				actionThree.add(actLook.get(1));
			 				Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
			 				Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY());
			 				actionThree.add(new Action(ActionType.PASS, ballPlayer, p, previousCase, nextCase));
			 				three.add(actionThree);
			 				
							if(p.getPosition().getX() == team.getButLine()) {
								canFinish = true;
								acts.add(tools.clone(actionThree));
							}
						}
					}
				
					
				} else if(actLook.get(0).getType() == ActionType.PASS) {
				//pass + depl +depl: same condition of 2 for
					firstPlayer = actLook.get(1).getNextPlayer();

					for(int i = 0; !canFinish   &&   i != team.getPlayers().size(); i++) {
						Player p = team.playerOfInt(i);
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
	
	//research action: pass + depl_1 + depl_2
	//this action are only move the last ballPlayer in depl_1
	private void threeCanMove(ArrayList<Action> actLook, Player player, Player firstPlayer){
		threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.UP);
		if(!canFinish) { threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.RIGHT);
			if(!canFinish) { threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.DOWN);
				if(!canFinish) { threeCanMoveDeplacement(actLook, player, firstPlayer, MoveDirection.LEFT);
		}	}	}
	}
	
	//test action: pass + depl_1 + depl_2 in the deplacement
	private void threeCanMoveDeplacement(ArrayList<Action> actLook, Player player, Player firstPlayer, MoveDirection deplacement) {
		
		boolean sameFirstPlayer = player.equals(firstPlayer);
		boolean verifBack = !sameFirstPlayer   ||   actLook.get(1).inverse().getDirection() != deplacement; //You can't back
		
		if(verifBack   &&   !player.equals(team.getBallPlayer())   &&   stadium.playerCanMove(player, deplacement)) {
			ArrayList<Action> actionThree = new ArrayList<>();
			actionThree.add(actLook.get(0));
			actionThree.add(actLook.get(1));
			Case previousCase = new Case(player.getPosition().getX(), player.getPosition().getY());
		
			Action newAction = new Action(ActionType.MOVE, player, player, previousCase, tools.nextCase(player.getPosition(), deplacement));
		
			actionThree.add(newAction);
			three.add(actionThree);
			
			exec(newAction);
				if(stadium.antiplay(team.getEnemyTeam())){
					canFinish = true;
					acts.add(actionThree);
				}
			undo(newAction);
		}
		
	}

	//research action: depl_1 + pass + depl_2
	//this action are to pass at player in depl_1 and move on depl_2 the last ballPlayer
	private void threeCanMovePreviousBall(ArrayList<Action> action) {
		if(!canFinish) { threeCanMovePreviousBallDeplacement(action, MoveDirection.UP);
			if(!canFinish) { threeCanMovePreviousBallDeplacement(action, MoveDirection.RIGHT);
				if(!canFinish) { threeCanMovePreviousBallDeplacement(action, MoveDirection.DOWN);
					if(!canFinish) { threeCanMovePreviousBallDeplacement(action, MoveDirection.LEFT);
		}	}	}	}
	}
	
	//test action: depl_1 + pass + depl_2 in deplacement
	private void threeCanMovePreviousBallDeplacement(ArrayList<Action> action, MoveDirection deplacement) {
		
		if(stadium.playerCanMove(ballPlayer, deplacement)) {
			ArrayList<Action> actionThree = new ArrayList<>();
			actionThree.add(action.get(0));
			actionThree.add(action.get(1));
			Case previousCase = new Case(ballPlayer.getPosition().getX(), ballPlayer.getPosition().getY());
			
			Action newAction = new Action(ActionType.MOVE, ballPlayer, ballPlayer, previousCase, tools.nextCase(ballPlayer.getPosition(), deplacement));
		
			actionThree.add(newAction);
			three.add(actionThree);
			
			exec(newAction);
				if(stadium.antiplay(team.getEnemyTeam())){
					canFinish = true;
					acts.add(actionThree);
				}
			undo(newAction);
		}
	}
	
	//research must or worst acts with must or worst avancement and add to acts
	public void initActs() {
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
	
	//execute all action in turn with actionNumero reference
	public void exec(int actionNumero) {
		if(actionNumero < one.size()) {
			exec(one.get(actionNumero));
		} else if(actionNumero - one.size() < two.size()) {
			exec(two.get(actionNumero - one.size()));
		} else {
			exec(three.get(actionNumero - one.size() - two.size()));
		}
	}
	
	//execute all action in instruction 
	private void exec(ArrayList<Action> instruction) {
		for(Action action : instruction) {
			exec(action);
		}
	}
	
	//execute action
	private void exec(Action action) {
		if(action.getType() == ActionType.PASS) {
			stadium.simplePass(action.getPreviousPlayer(), action.getNextPlayer());
		} else {
			stadium.simpleMove(action.getNextPlayer(), action.getDirection());
		}
	}
	
	//undo all action in turn with actionNumero reference
	public void undo(int actionNumero) {
		if(actionNumero < one.size()) {
			undo(one.get(actionNumero));
		} else if(actionNumero - one.size() < two.size()) {
			undo(two.get(actionNumero - one.size()));
		} else {
			undo(three.get(actionNumero - one.size() - two.size()));
		}
	}
	
	//undo all action in instruction 
	private void undo(ArrayList<Action> instruction) {
		for(int i = instruction.size()-1; i != -1; i--) {
			undo(instruction.get(i));
		}
	}

	//undo action
	private void undo(Action action) {
		exec(action.inverse());
	}
	
	//compare with beta and maybe write value in place to reference actionNumero
	public void reportMax(int actionNumero, int value) {
		if(value > beta) {
			canAccess = false;
		}
		
		if(actionNumero == 0 || value > avancement) {
			avancement = value;
		}
		
		report(actionNumero, value);
	}
	
	//compare with alpha and maybe write value in place to reference actionNumero
	public void reportMin(int actionNumero, int value) {
		if(value < alpha) {
			canAccess = false;
		}
	
		if(actionNumero == 0 || value < avancement) {
			avancement = value;
		}
		
		report(actionNumero, value);
	}
	
	//write value in place to reference actionNumero
	private void report(int actionNumero, int value) {
		if(actionNumero < one.size()) {
			oneHigh[actionNumero] = value;
		} else if(actionNumero - one.size() < two.size()) {
			twoHigh[actionNumero - one.size()] = value;
		} else {
			threeHigh[actionNumero - one.size() - two.size()] = value;
		}
	}
}
