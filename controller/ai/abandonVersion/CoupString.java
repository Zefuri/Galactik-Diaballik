package controller.ai.abandonVersion;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class CoupString{
	Stadium stadium;
	Team team;
	Player ballPlayer;
	
	boolean canAccess;
	int alpha;
	int beta;
	
	String[] one;
	int[] oneHigh;
	String[] two;
	int[] twoHigh;
	String[] three;
	int[] threeHigh;
	
	String[] acts;
	int avancement;

	ToolsBallString tools;
	
	public CoupString(Stadium stadium, Team team, int alpha, int beta) {
		System.out.println(stadium.toString());
		this.stadium = stadium;
		this.team = team;
		ballPlayer = team.getBallPlayer();
		
		canAccess = true;
		this.alpha = alpha;
		this.beta = beta;
		
		tools = new ToolsBallString();
	}
	
	
	public int numberOfAction() {
		return one.length + two.length + three.length;
	}
	

	public String[] getOne(){
		return one;
	}
	

		public String[] getTwo(){
		return two;
	}


	public String[] getThree(){
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
	
	
	public String[] getActs() {
		return acts;
	}
	
	
	public int getAvancement() {
		return avancement;
	}
	
	
	public void init() {
		initOne();
		oneHigh = new int[one.length];
		initTwo();
		twoHigh = new int[two.length];
		initThree();
		threeHigh = new int[three.length];
	}
	
	
	public void initValueMin() {
		avancement = Integer.MAX_VALUE;
		oneHigh = initValueMin(one);
		twoHigh = initValueMin(two);
		threeHigh = initValueMin(three);
	}


	public int[] initValueMin(String[] actionList){
		int[] valueList = new int[actionList.length];
		
		int index = -1;
		for(String action : actionList){
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


	public int[] initValueMax(String[] actionList){
		int[] valueList = new int[actionList.length];
		
		int index = -1;
		for(String action : actionList){
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
	
	
	public void initOne(){	
		one = new String[team.numberOfPossibilityPass() + team.movesNumber()];
		int oneNum = 0;
		int numberOfPlayer = -1;
		
		for(Player p : team.getPlayers()) {
			numberOfPlayer++;
			
			if(!p.equals(ballPlayer)){
			
				//pass
				if(stadium.playerCanPass(ballPlayer, p)) {
					one[oneNum] = ""+numberOfPlayer+"P";
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
			index++;
		}
		
		return index;
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
						}
						
						lessPlayer = numberOfPlayer < firstPlayer;
						if(!lessPlayer   &&   !p.equals(ballPlayer)) {
						
							stockage += twoCanMove(firstPlayer, numberOfPlayer, p, actLook);
							
						} else if(!p.equals(ballPlayer)) {
							
							previousPositionFirst = previousPosition(team.playerOfInt(firstPlayer), one[actLook].charAt(1));
							stockage += remplace1(previousPositionFirst, actLook, p, one[actLook].charAt(1));
							
						}
					}
				}else{
				//pass + depl: only if depl is to the last player with ball
						String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
						
						stockage += twoCanMovePreviousBall(actLook, ballNum);
						
				}
			undo(one[actLook]);
		}
	
		two = new String[stockage.length()/4];
		
		for(int i = 0; i != stockage.length(); i += 4){
			two[i/4] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3);
		}
	}
	
	
	public String twoCanMove(int firstPlayer, int numberOfPlayer, Player player, int action){
		
		String result = twoCanMoveDirection(firstPlayer, numberOfPlayer, player, action, 'U');
		result += twoCanMoveDirection(firstPlayer, numberOfPlayer, player, action, 'R');
		result += twoCanMoveDirection(firstPlayer, numberOfPlayer, player, action, 'D');
		result += twoCanMoveDirection(firstPlayer, numberOfPlayer, player, action, 'L');
		
		return result;
	}
	
	public String twoCanMoveDirection(int firstPlayer, int numberOfPlayer, Player player, int action, char deplacement){
		String result = "";
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   one[action].charAt(1) != tools.reverse(deplacement); //You can't back
		
		if(verifBack   &&   stadium.playerCanMove(player, tools.moveOfChar(deplacement))) {
			result = one[action]+numberOfPlayer+""+deplacement;
		}
		
		return result;
	}


	public String remplace1(int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementOne) {
		String result = "";
		if(deplacementOne == 'U') {
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'L');
		} else if(deplacementOne == 'R') {
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'D');
		} else if(deplacementOne == 'D') {
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'D');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'L');
		} else {
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'D');
			result += remplace2(previousPositionFirst, numPreviousAction, player, 'L');
		}
		
		return result;
	}
	
	
	public String remplace2(int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementTwo) {
		String result = "";
		int[] nextPosition = previousPosition(player, tools.reverse(deplacementTwo));
		boolean canRemplace = nextPosition[0] == previousPositionFirst[0]   &&   nextPosition[1] == previousPositionFirst[1];
		
		if(canRemplace) {
			result += one[numPreviousAction]+player.getName().charAt(player.getName().length()-1)+""+deplacementTwo;
		}
		
		return result;
	}
	

	public String twoCanMovePreviousBall(int index, String ballNumber) {
		String result = twoCanMovePreviousBallDeplacement(index, ballNumber, 'U');
		result += twoCanMovePreviousBallDeplacement(index, ballNumber, 'R');
		result += twoCanMovePreviousBallDeplacement(index, ballNumber, 'D');
		result += twoCanMovePreviousBallDeplacement(index, ballNumber, 'L');
		
		return result;
	}

							
	public String twoCanMovePreviousBallDeplacement(int index, String ballNumber, char deplacement) {
		String result = "";
		if(stadium.playerCanMove(ballPlayer, tools.moveOfChar(deplacement))){
			result += one[index]+ballNumber+""+deplacement;
		}
		return result;	
	}
	
	
	public int[] previousPosition(Player player, char deplacement){
		return previousPosition(player.getPosition().getX(), player.getPosition().getY(), deplacement);
	}
	
	
	public int[] previousPosition(int actualX, int actualY, char deplacement){
		int[] result = new int[2];
		result[0] = previousX(actualX, deplacement);
		result[1] = previousY(actualY, deplacement);
		return result;
	}
	
	
	public int previousY(int actualY, char deplacement){
		if(deplacement == 'R') {
			return actualY - 1;
		}else if(deplacement == 'L'){
			return actualY + 1;
		}
		return actualY;
	}
	
	
	public int previousX(int actualX, char deplacement){
		if(deplacement == 'D') {
			return actualX - 1;
		}else if(deplacement == 'U'){
			return actualX + 1;
		}
		return actualX;
	}
	

	public void initThree() {
		String stockage = "";
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
						}
					}
				
					
				} else if(two[actLook].charAt(1) == 'P') {
				//pass + depl +depl: same condition of 2 for
					firstPlayer = (int)(two[actLook].charAt(2)-'0');

					for(Player p : team.getPlayers()) {
						numberOfPlayer++;
						stockage += threeCanMove(numberOfPlayer, firstPlayer, actLook, p);
					}
					
				} else {
				//depl + pass + depl: only if the pass is to the player moved and now you move the player before with ball
					String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
					
					verifCondition = (two[actLook].charAt(0) == two[actLook].charAt(2));
					if(verifCondition){
						stockage += threeCanMovePreviousBall(actLook, ballNum);
					}
				}

			undo(two[actLook]);	
		}
		
		three = new String[stockage.length()/6];
		
		for(int i = 0; i != stockage.length(); i += 6) {
			three[i/6] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3)+stockage.charAt(i+4)+stockage.charAt(i+5);
		}
	}
		
		
	public String threeCanMove(int numberOfPlayer, int firstPlayer, int index, Player player){
		String result = threeCanMoveDeplacement(numberOfPlayer, firstPlayer, index, player, 'U');
		result += threeCanMoveDeplacement(numberOfPlayer, firstPlayer, index, player, 'R');
		result += threeCanMoveDeplacement(numberOfPlayer, firstPlayer, index, player, 'D');
		result += threeCanMoveDeplacement(numberOfPlayer, firstPlayer, index, player, 'L');
		
		return result;
	}
	

	public String threeCanMoveDeplacement(int numberOfPlayer, int firstPlayer, int index, Player player, char deplacement) {
		String result = "";
		
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   two[index].charAt(3) != tools.reverse(deplacement); //You can't back
		if(verifBack   &&   !player.equals(team.getBallPlayer())   &&   stadium.playerCanMove(player, tools.moveOfChar(deplacement))) {
			result += two[index]+numberOfPlayer+""+deplacement;
		}
		
		return result;
	}
	
	
		public String threeCanMovePreviousBall(int index, String ballNum) {
		String result = threeCanMovePreviousBallDeplacement(index, ballNum, 'U');
		result += threeCanMovePreviousBallDeplacement(index, ballNum, 'R');
		result += threeCanMovePreviousBallDeplacement(index, ballNum, 'D');
		result += threeCanMovePreviousBallDeplacement(index, ballNum, 'L');
		
		return result;
	}
	
	
	public String threeCanMovePreviousBallDeplacement(int index, String ballNum, char deplacement) {
		String result = "";
		
		if(stadium.playerCanMove(ballPlayer, tools.moveOfChar(deplacement))) {
			result += two[index]+ballNum+deplacement;
		}
		
		return result;
	}


	public void initActs(){
		acts = new String[numberOfActs()];
		int actNumber = 0;
		for(int i = 0; actNumber != acts.length   &&   i != one.length+two.length+three.length; i++){
		
			if(i < one.length   &&   avancement == oneHigh[i]){
				acts[actNumber] = one[i];
				actNumber++;
				
			}else if(-1 < i-one.length   &&   i-one.length < two.length   &&   avancement == twoHigh[i-one.length]){
				acts[actNumber] = two[i-one.length];
				actNumber++;
				
			}else if(-1 < i-one.length-two.length   &&   avancement == threeHigh[i-one.length-two.length]){
				acts[actNumber] = three[i-one.length-two.length];
				actNumber++;
			}
		}
	}
	
	
	public int numberOfActs(){
		int numberOfActs = 0;
			
		for(int i = 0; i != one.length+two.length+three.length; i++){

			if(i < one.length   &&   avancement == oneHigh[i]){
				numberOfActs++;
			}else if(-1 < i-one.length   &&   i-one.length < two.length   &&   avancement == twoHigh[i-one.length]){
				numberOfActs++;
				
			}else if(-1 < i-one.length-two.length   &&   avancement == threeHigh[i-one.length-two.length]){
				numberOfActs++;
			}
		}
		
		return numberOfActs;
	}
	
	
	public void exec(int actionNumero) {
		if(actionNumero < one.length) {
			exec(one[actionNumero]);
		} else if(actionNumero - one.length < two.length) {
			exec(two[actionNumero - one.length]);
		} else {
			exec(three[actionNumero - one.length - two.length]);
		}
	}
	
	
	public void exec(String instruction){
		int number = -1;
		
		for(int i = 0; i != instruction.length(); i += 2){
			number = (int)(instruction.charAt(i)-'0');
			
			if(instruction.charAt(i+1) == 'P')
				stadium.simplePass(ballPlayer, team.playerOfInt(number));
			else
				stadium.simpleMove(team.playerOfInt(number), tools.moveOfChar(instruction.charAt(i+1)));
		}
	}
	
	
	public void undo(int actionNumero) {
		if(actionNumero < one.length) {
			undo(one[actionNumero]);
		} else if(actionNumero - one.length < two.length) {
			undo(two[actionNumero - one.length]);
		} else {
			undo(three[actionNumero - one.length - two.length]);
		}
	}
	
	
	public void undo(String instruction){
		int number = -1;
		
		for(int i = instruction.length()-1; i != -1; i -= 2){
			number = (int)(instruction.charAt(i-1)-'0');
			
			if(instruction.charAt(i) == 'P')
				stadium.pass(team.playerOfInt(number), ballPlayer);
			else
				stadium.simpleMove(team.playerOfInt(number), tools.moveOfChar(tools.reverse(instruction.charAt(i))));
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
		if(actionNumero < one.length) {
			oneHigh[actionNumero] = value;
		} else if(actionNumero - one.length < two.length) {
			twoHigh[actionNumero - one.length] = value;
		} else {
			threeHigh[actionNumero - one.length - two.length] = value;
		}
	}
}
