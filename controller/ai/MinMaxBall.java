package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

class MinMaxBall {
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

	public MinMaxBall(Stadium stadium, Team team, int depth) {
		checkingDepth = depth;
		this.stadium = stadium;
		this.team = team;
		
		ballPlayer = this.team.getBallPlayer();

		this.initOne();
		this.initTwo();
		this.initThree();
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
				oneNum++;
			}
			
			//depl
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
				one[oneNum] = ""+numberOfPlayer+"U";
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
				one[oneNum] = ""+numberOfPlayer+"D";
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
				one[oneNum] = ""+numberOfPlayer+"L";
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
				one[oneNum] = ""+numberOfPlayer+"R";
				oneNum++;
			}
		}
	}
	
	public void initTwo(){
		String stockage = "";
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
						}
						
						lessPlayer = numberOfPlayer < firstPlayer;
						if(!lessPlayer){
						
							sameFirstPlayer = numberOfPlayer == firstPlayer;
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('U'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
								stockage += one[actLook]+numberOfPlayer+"U";
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('D'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
								stockage += one[actLook]+numberOfPlayer+"D";
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('L');	 //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
								stockage += one[actLook]+numberOfPlayer+"L";
							}
							
							verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('R');	 //You can't back
							if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
								stockage += one[actLook]+numberOfPlayer+"R";
							}
							
						}else{
						
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() + 2;
							if(one[actLook].charAt(1) == 'U'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"U";
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() - 2;
							if(one[actLook].charAt(1) == 'R'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"R";
							}
							
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() - 2;
							if(one[actLook].charAt(1) == 'D'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"D";
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() + 2;
							if(one[actLook].charAt(1) == 'L'   &&   canRemplace) {
								stockage += one[actLook]+numberOfPlayer+"L";
							}
							
						}
					}
				}else{
				//pass + depl: only if depl is to the last player with ball
						String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.UP)){
							stockage += one[actLook]+ballNum+"U";
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.DOWN)){
							stockage += one[actLook]+ballNum+"D";
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.LEFT)){
							stockage += one[actLook]+ballNum+"L";
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.RIGHT)){
							stockage += one[actLook]+ballNum+"R";
						}
						
				}
			undo(one[actLook]);
		}
	
		two = new String[stockage.length()/4];
		twoHigh = new int[two.length];
		
		for(int i = 0; i != stockage.length(); i += 4){
			two[i/4] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3);
		}
	}
	
	public void initThree() {
		String stockage = "";
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
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('D'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)){
								stockage += two[actLook]+numberOfPlayer+"D";
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('L'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)){
								stockage += two[actLook]+numberOfPlayer+"L";
							}
								
							verifBack = !sameFirstPlayer   ||   two[actLook].charAt(3) != reverse('R'); //You can't back
							if(verifBack   &&   !p.equals(ballPlayer2)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)){
								stockage += two[actLook]+numberOfPlayer+"R";
							}
						
						} else {
						
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() + 2;
							if(two[actLook].charAt(3) == 'U'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"U";
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() - 2;
							if(two[actLook].charAt(3) == 'R'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"R";
							}
							
							canRemplace =  p.getPosition().getX() == team.playerOfInt(firstPlayer).getPosition().getX() - 2;
							if(two[actLook].charAt(3) == 'D'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"D";
							}
							
							canRemplace =  p.getPosition().getY() == team.playerOfInt(firstPlayer).getPosition().getY() + 2;
							if(two[actLook].charAt(3) == 'L'   &&   canRemplace) {
								stockage += two[actLook]+numberOfPlayer+"L";
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
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.RIGHT)){
							stockage += two[actLook]+ballNum+"R";
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.DOWN)){
							stockage += two[actLook]+ballNum+"D";
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.LEFT)){
							stockage += two[actLook]+ballNum+"L";
						}
						
					}
				}

			undo(two[actLook]);	
		}
		
		three = new String[stockage.length()/6];
		threeHigh = new int[three.length];
		
		for(int i = 0; i != stockage.length(); i += 6) {
			three[i/6] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3)+stockage.charAt(i+4)+stockage.charAt(i+5);
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

	public int minList(int[] intList){
		int min = intList[0];
		for(int i = 1; i != intList.length; i++){
			if(min > intList[i])
				min = intList[i];
		}
		return min;
	}
	
	public int min2(int first, int second){
		if(first < second)
			return first;
		return second;
	}
	
	public int min3(int first, int second, int third){
		return min2(min2(first, second), third);
	}
	
	public int Progress(){
		MaxMinBall maxCheck;
		
		for(int i = 0; i != one.length+two.length+three.length; i++){

			if(i < one.length){
				//you play one act
				exec(one[i]);
					maxCheck = new MaxMinBall(stadium, team.getEnemyTeam(), checkingDepth-1);
					oneHigh[i] = maxCheck.Progress();
				undo(one[i]);
			
			}else if(i-one.length < two.length){
				//you play two act
				exec(two[i-one.length]);
					maxCheck = new MaxMinBall(stadium, team.getEnemyTeam(), checkingDepth-1);
					twoHigh[i-one.length] = maxCheck.Progress();
				undo(two[i-one.length]);
			
			}else{
				//you play three act
				exec(three[i-one.length-two.length]);
					maxCheck = new MaxMinBall(stadium, team.getEnemyTeam(), checkingDepth-1);
					threeHigh[i-one.length-two.length] = maxCheck.Progress();
				undo(three[i-one.length-two.length]);
			}
			
		}
		
		return minAvancement();
	}

	public int minAvancement(){
		return min3(minList(oneHigh), minList(twoHigh), minList(threeHigh));
	}

	public static void main(String args[]){
		//tests
	}
}
