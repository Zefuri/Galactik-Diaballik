package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

class MaxMinBall {
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
	
	String[] mustActs;
	int mustAvancement;

	public MaxMinBall(Stadium stadium, Team team, int depth) {
		checkingDepth = depth;
		this.stadium = stadium;
		this.team = team;
		
		ballPlayer = this.team.getBallPlayer();

		this.initOne();
		this.initTwo();
		this.initThree();
		
		mustAvancement = this.Progress();
		this.initMustActs();		
	}
	
	public void initOne(){	
		one = new String[team.numberOfPossibilityPass() + team.movesNumber()];
		oneHigh = new int[one.length];
		int oneNum = 0;
		int numberOfPlayer = -1;
		
		for(Player p : team.getPlayers()) {
			numberOfPlayer++;
		
			//pass
			if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
				one[oneNum] = ""+numberOfPlayer+"P";
				if(checkingDepth == 0)
					oneHigh[oneNum] = ballAvance(p.getPosition().getX());
				oneNum++;
			}
			
			//depl
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
				one[oneNum] = ""+numberOfPlayer+"U";
				if(checkingDepth == 0)
					oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
				one[oneNum] = ""+numberOfPlayer+"D";
				if(checkingDepth == 0)
					oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
				one[oneNum] = ""+numberOfPlayer+"L";
				if(checkingDepth == 0)
					oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
				one[oneNum] = ""+numberOfPlayer+"R";
				if(checkingDepth == 0)
					oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
		}
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
						if(!lessPlayer){
						
							sameFirstPlayer = numberOfPlayer == firstPlayer;
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('U'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
								stockage += one[actLook]+numberOfPlayer+"U";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('D'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
								stockage += one[actLook]+numberOfPlayer+"D";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('L');	 //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
								stockage += one[actLook]+numberOfPlayer+"L";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('R');	 //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
								stockage += one[actLook]+numberOfPlayer+"R";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
						}else{
						
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() + 2;
							if(one[actLook].charAt(1) == 'U'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"U";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() - 2;
							if(one[actLook].charAt(1) == 'R'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"R";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() - 2;
							if(one[actLook].charAt(1) == 'D'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"D";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() + 2;
							if(one[actLook].charAt(1) == 'L'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"L";
								stockageHigh += ballPlayer.getPosition().getX();
							}
							
						}
					}
				}else{
				//pass + depl: only if depl is to the last player with ball
						String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.UP)){
							stockage += one[actLook]+ballNum+"U";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.DOWN)){
							stockage += one[actLook]+ballNum+"D";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.LEFT)){
							stockage += one[actLook]+ballNum+"L";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.RIGHT)){
							stockage += one[actLook]+ballNum+"R";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
				}
			undo(one[actLook]);
		}
	
		two = new String[stockage.length()/4];
		twoHigh = new int[two.length];
		
		for(int i = 0; i != stockage.length(); i += 4){
			two[i/4] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3);
			if(checkingDepth == 0)
				twoHigh[i/4] = ballAvance((int)(stockageHigh.charAt(i/4)-'0'));
		}
	}
	
	public void initThree() {
		String stockage = "";
		String stockageHigh = "";
		int numberOfPlayer;
		Player ballPlayer2;
		boolean verifPass;
		boolean verifBack;
		int firstPlayer;
		boolean sameFirstPlayer;
		boolean lessPlayer;
		boolean canRemplace;
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
						
						lessPlayer = numberOfPlayer < firstPlayer;
						if(!lessPlayer){
							
							sameFirstPlayer = numberOfPlayer == firstPlayer;

							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('U'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.UP)){
								stockage += two[actLook]+numberOfPlayer+"U";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer2.getPosition().getX();
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('D'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)){
								stockage += two[actLook]+numberOfPlayer+"D";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer2.getPosition().getX();
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('L'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)){
								stockage += two[actLook]+numberOfPlayer+"L";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer2.getPosition().getX();
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('R'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)){
								stockage += two[actLook]+numberOfPlayer+"R";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer2.getPosition().getX();
							}
						
						} else {
						
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() + 2;
							if(two[actLook].charAt(3) == 'U'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"U";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() - 2;
							if(two[actLook].charAt(3) == 'R'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"R";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() - 2;
							if(two[actLook].charAt(3) == 'D'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"D";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() + 2;
							if(two[actLook].charAt(3) == 'L'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"L";
								if(checkingDepth == 0)
									stockageHigh += ballPlayer.getPosition().getX();
							}
							
						}
					}
					
				} else {
				//depl + pass + depl: only if the pass is to the player moved and now you move the player before with ball
					String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
					
					verifCondition = (two[actLook].charAt(0) == two[actLook].charAt(2));
					if(verifCondition){
					
						if(stadium.playerCanMove(ballPlayer, MoveDirection.UP)){
							stockage += two[actLook]+ballNum+"U";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.RIGHT)){
							stockage += two[actLook]+ballNum+"R";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.DOWN)){
							stockage += two[actLook]+ballNum+"D";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.LEFT)){
							stockage += two[actLook]+ballNum+"L";
							if(checkingDepth == 0)
								stockageHigh += ballPlayer2.getPosition().getX();
						}
						
					}
				}

			undo(two[actLook]);	
		}
		
		three = new String[stockage.length()/6];
		threeHigh = new int[three.length];
		
		for(int i = 0; i != stockage.length(); i += 6) {
			three[i/6] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3)+stockage.charAt(i+4)+stockage.charAt(i+5);
			if(checkingDepth == 0)
				threeHigh[i/6] = ballAvance((int)(stockageHigh.charAt(i/6)-'0'));
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
	
	public char reverse(char move){
		if(move == 'U')
			return 'D';
		else if (move == 'R')
			return 'L';
		else if (move == 'D')
			return 'U';
		else
			return 'R';
	}
	
	public MoveDirection moveOfChar(char deplacement) {
		if(deplacement == 'D')
			return MoveDirection.DOWN;
		else if (deplacement == 'L')
			return MoveDirection.LEFT;
		else if (deplacement == 'U')
			return MoveDirection.UP;
		else
			return MoveDirection.RIGHT;
	}
	
	public void exec(String instruction){
		int number = -1;
		
		for(int i = 0; i != instruction.length(); i += 2){
			number = (int)(instruction.charAt(i)-'0');
			
			if(instruction.charAt(i+1) == 'P')
				stadium.pass(ballPlayer, team.playerOfInt(number));
			else
				stadium.move(team.playerOfInt(number), moveOfChar(instruction.charAt(i+1)));
		}
	}
	
	public void undo(String instruction){
		int number = -1;
		
		for(int i = instruction.length()-1; i != -1; i -= 2){
			number = (int)(instruction.charAt(i-1)-'0');
			
			if(instruction.charAt(i) == 'P')
				stadium.pass(team.playerOfInt(number), ballPlayer);
			else
				stadium.move(team.playerOfInt(number), moveOfChar(reverse(instruction.charAt(i))));
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
	
	public int Progress(){
		if(checkingDepth != 0){
			MinMaxBall minCheck;
			
			for(int i = 0; i != one.length+two.length+three.length; i++){
			
				if(i < one.length){
					//you play one act
					exec(one[i]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth);
						oneHigh[i] = minCheck.Progress();
					undo(one[i]);
				
				}else if(i-one.length < two.length){
					//you play two act
					exec(two[i-one.length]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth);
						twoHigh[i-one.length] = minCheck.Progress();
					undo(two[i-one.length]);
				
				}else{
					//you play three act
					exec(three[i-one.length-two.length]);
						minCheck = new MinMaxBall(stadium, team.getEnemyTeam(), checkingDepth);
						threeHigh[i-one.length-two.length] = minCheck.Progress();
					undo(three[i-one.length-two.length]);
				}
			}
			
		}

		return maxAvancement();
	}
	
	//return the avancement of team ball (6 - ball position if team bottom and 0 + ball position if team top)
	public int teamBallAvance(Team team, int ballPosition){
		if(team.getPosition() == TeamPosition.TOP)
			return ballPosition;
		return (ModelConstants.BOARD_SIZE - 1) - ballPosition;
	}
	
	//return the difference between the avancement of two ball
	public int ballAvance(int ballPosition){
		return teamBallAvance(team, ballPosition) - teamBallAvance(team.getEnemyTeam(), team.getEnemyTeam().getBallPlayer().getPosition().getX());
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
