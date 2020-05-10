package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class MaxMinBall{
	int checkingDepth;
	Stadium stadium;
	Team team;
	Player ballPlayer;
	String[] one;
	int[] oneHigh;
	String[] two;
	int[] twoHigh;
	String[] three;
	int[] threeHigh;
	ToolsBall tools;
	
	String[] mustActs;
	int mustAvancement;

	public MaxMinBall(Stadium stadium, Team team, int depth) {
		System.out.println(stadium.toString());
		checkingDepth = depth;
		this.stadium = stadium;
		this.team = team;
		ballPlayer = team.getBallPlayer();
		
		tools = new ToolsBall();
		
		initOne();
		initTwo();
		initThree();
		
		System.out.println("Taille de One: "+one.length);
		System.out.println("Taille de Two: "+two.length);
		System.out.println("Taille de Three: "+three.length);
	}


	public void initOne(){	
		one = new String[team.numberOfPossibilityPass() + team.movesNumber()];
		oneHigh = new int[one.length];
		int oneNum = 0;
		int numberOfPlayer = -1;
		
		for(Player p : team.getPlayers()) {
			numberOfPlayer++;
			
			if(!p.equals(ballPlayer)){
			
				//pass
				if(stadium.playerCanPass(ballPlayer, p)) {
					one[oneNum] = ""+numberOfPlayer+"P";
					if(checkingDepth == 0)
						oneHigh[oneNum] = tools.ballAvance(team, p.getPosition().getX());
					oneNum++;
				}
				
				//depl
				oneNum = oneCanMove(p, oneNum, numberOfPlayer);
			}
		}
	}
	
	
	public int oneCanMove(Player player, int oneNum, int numberOfPlayer){
		int index = oneNum;
		index = oneCanMoveDirection(player, index, numberOfPlayer, 'U');
		index = oneCanMoveDirection(player, index, numberOfPlayer, 'R');
		index = oneCanMoveDirection(player, index, numberOfPlayer, 'D');
		index = oneCanMoveDirection(player, index, numberOfPlayer, 'L');
		return index;
	}


	public int oneCanMoveDirection(Player player, int oneNum, int numberOfPlayer, char direction){
		int index = oneNum;
		if(stadium.playerCanMove(player, tools.moveOfChar(direction))){
			one[index] = ""+numberOfPlayer+""+direction;
			if(checkingDepth == 0)
				oneHigh[index] = tools.ballAvance(team, ballPlayer.getPosition().getX());
			index++;
		}
		
		return index;
	}
	

	public void initTwo(){
		String stockage = "";
		String stockageHigh = "";
		int numberOfPlayer;
		
		Player ballPlayer2;
		boolean verifPass;
		int firstPlayer;
		boolean sameFirstPlayer;
		boolean lessPlayer;
		boolean canRemplace;
		boolean verifBack;
		String[] remplaceStock;
		int[] nextPosition;
		int[] previousPositionFirst;
		
		for(int actLook = 0; actLook != one.length; actLook++) {
		
			numberOfPlayer = -1;
			exec(one[actLook]);
			
				firstPlayer = (int)(one[actLook].charAt(0)-'0');
				ballPlayer2 = team.getBallPlayer();
				verifPass = one[actLook].charAt(1) != 'P'; //You can't make two pass
				
				if(verifPass){
				//depl + pass or depl + depl

					for(Player p : team.getPlayers()) {
						numberOfPlayer++;
						
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)){
							stockage += one[actLook]+numberOfPlayer+"P";
							if(checkingDepth == 0)
								stockageHigh += p.getPosition().getX();
						}
						
						lessPlayer = numberOfPlayer < firstPlayer;
						if(!lessPlayer   &&   !p.equals(ballPlayer)) {
						
							String[] evaluation = tools.twoCanMove(ballPlayer, stadium, checkingDepth, one, firstPlayer, numberOfPlayer, p, actLook);
							stockage += evaluation[0];
							stockageHigh += evaluation[1];
							
						} else if(!p.equals(ballPlayer)) {
							
							previousPositionFirst = tools.previousPosition(team.playerOfInt(firstPlayer), one[actLook].charAt(1));
							remplaceStock = tools.remplace1(team, checkingDepth, one, previousPositionFirst, actLook, p, one[actLook].charAt(1));
							stockage += remplaceStock[0];
							stockageHigh += remplaceStock[1];
							
						}
					}
				}else{
				//pass + depl: only if depl is to the last player with ball
						String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
						
						remplaceStock = tools.twoCanMovePreviousBall(team, stadium, checkingDepth, one, actLook, ballPlayer, ballNum);
						stockage += remplaceStock[0];
						stockageHigh += remplaceStock[1];
						
				}
			undo(one[actLook]);
		}
	
		two = new String[stockage.length()/4];
		twoHigh = new int[two.length];
		
		for(int i = 0; i != stockage.length(); i += 4){
			two[i/4] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3);
			if(checkingDepth == 0)
				twoHigh[i/4] = tools.ballAvance(team, (int)(stockageHigh.charAt(i/4)-'0'));
		}
	}
	

	public void initThree() {
		String stockage = "";
		String stockageHigh = "";
		String[] view;
		int numberOfPlayer;
		Player ballPlayer2;
		boolean verifPass;
		boolean verifBack;
		int firstPlayer;
		boolean sameFirstPlayer;
		boolean verifCondition;
		
		for(int actLook = 0; actLook != two.length; actLook++) {
			numberOfPlayer = -1;
			exec(two[actLook]);
			
				ballPlayer2 = team.getBallPlayer();
				verifPass = two[actLook].charAt(1) != 'P' && two[actLook].charAt(3) != 'P'; //You can't make two pass
				
				if(verifPass) {
				//depl + depl + pass
					
					for(Player p : team.getPlayers()) {
						numberOfPlayer++;
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
							stockage += two[actLook]+numberOfPlayer+"P";
							if(checkingDepth == 0)
								stockageHigh += p.getPosition().getX();
						}
					}
				
					
				} else if(two[actLook].charAt(1) == 'P') {
				//pass + depl +depl: same condition of 2 for
					firstPlayer = (int)(two[actLook].charAt(2)-'0');

					for(Player p : team.getPlayers()) {
						numberOfPlayer++;
						view = tools.threeCanMove(team, stadium, checkingDepth, two, numberOfPlayer, firstPlayer, actLook, p);
						stockage += view[0];
						stockageHigh += view[1];
					}
					
				} else {
				//depl + pass + depl: only if the pass is to the player moved and now you move the player before with ball
					String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
					
					verifCondition = (two[actLook].charAt(0) == two[actLook].charAt(2));
					if(verifCondition){
						view = tools.threeCanMovePreviousBall(team, ballPlayer, stadium, checkingDepth, two, actLook, ballNum);
						stockage += view[0];
						stockageHigh += view[1];
					}
				}

			undo(two[actLook]);	
		}
		
		three = new String[stockage.length()/6];
		threeHigh = new int[three.length];
		
		for(int i = 0; i != stockage.length(); i += 6) {
			three[i/6] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3)+stockage.charAt(i+4)+stockage.charAt(i+5);
			if(checkingDepth == 0)
				threeHigh[i/6] = tools.ballAvance(team, (int)(stockageHigh.charAt(i/6)-'0'));
		}
	}


	public void initMustActs(){
		mustActs = new String[numberOfMust()];
		int mustActNumber = 0;
		for(int i = 0; mustActNumber != mustActs.length   &&   i != one.length+two.length+three.length; i++){
		
			if(i < one.length   &&   mustAvancement == oneHigh[i]){
				mustActs[mustActNumber] = one[i];
				mustActNumber++;
				
			}else if(-1 < i-one.length   &&   i-one.length < two.length   &&   mustAvancement == twoHigh[i-one.length]){
				mustActs[mustActNumber] = two[i-one.length];
				mustActNumber++;
				
			}else if(-1 < i-one.length-two.length   &&   mustAvancement == threeHigh[i-one.length-two.length]){
				mustActs[mustActNumber] = three[i-one.length-two.length];
				mustActNumber++;
			}
		}
	}
	
	
	public void exec(String instruction){
		int number = -1;
		
		for(int i = 0; i != instruction.length(); i += 2){
			number = (int)(instruction.charAt(i)-'0');
			
			if(instruction.charAt(i+1) == 'P')
				stadium.pass(ballPlayer, team.playerOfInt(number));
			else
				stadium.move(team.playerOfInt(number), tools.moveOfChar(instruction.charAt(i+1)));
		}
	}
	
	
	public void undo(String instruction){
		int number = -1;
		
		for(int i = instruction.length()-1; i != -1; i -= 2){
			number = (int)(instruction.charAt(i-1)-'0');
			
			if(instruction.charAt(i) == 'P')
				stadium.pass(team.playerOfInt(number), ballPlayer);
			else
				stadium.move(team.playerOfInt(number), tools.moveOfChar(tools.reverse(instruction.charAt(i))));
		}
	}


	public int maxList(int[] intList){
		int max = intList[0];
		for(int i = 1; i != intList.length; i++){
			if(max < intList[i])
				max = intList[i];
		}
		return max;
	}
	
	
	public int max2(int first, int second){
		if(first > second)
			return first;
		return second;
	}
	
	
	public int max3(int first, int second, int third){
		return max2(max2(first, second), third);
	}
	
	
	public String[] getMustActs(){
		return mustActs;
	}
	
	
	public int getMustAvancement(){
		return mustAvancement;
	}
	
	
	public void progress(int checkingDepth){
		if(checkingDepth != 0){
			MinMaxBall minCheck;
			
			for(int i = 0; i != one.length+two.length+three.length; i++){
			
				if(i < one.length){
					//you play one act
					exec(one[i]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth-1);
						minCheck.progress(checkingDepth-1);
						oneHigh[i] = minCheck.getWorstAvancement();
					undo(one[i]);
				
				}else if(i-one.length < two.length){
					//you play two act
					exec(two[i-one.length]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth-1);
						minCheck.progress(checkingDepth-1);
						twoHigh[i-one.length] = minCheck.getWorstAvancement();
					undo(two[i-one.length]);
				
				}else{
					//you play three act
					exec(three[i-one.length-two.length]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth-1);
						minCheck.progress(checkingDepth-1);
						threeHigh[i-one.length-two.length] = minCheck.getWorstAvancement();
					undo(three[i-one.length-two.length]);
				}
			}
			
		}
		
		mustAvancement = maxAvancement();
		this.initMustActs();
	}


	public int maxAvancement(){
		return max3(maxList(oneHigh), maxList(twoHigh), maxList(threeHigh));
	}
	
	
	public int numberOfMust(){
		int numberOfMust = 0;
			
		for(int i = 0; i != one.length+two.length+three.length; i++){

			if(i < one.length   &&   mustAvancement == oneHigh[i]){
				numberOfMust++;
			}else if(-1 < i-one.length   &&   i-one.length < two.length   &&   mustAvancement == twoHigh[i-one.length]){
				numberOfMust++;
				
			}else if(-1 < i-one.length-two.length   &&   mustAvancement == threeHigh[i-one.length-two.length]){
				numberOfMust++;
			}
		}
		
		return numberOfMust;
	}

	
	public static void main(String args[]){
		//tests	
	}
}
