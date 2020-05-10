//package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class ToolsBall {
	
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
	
	
	//return the avancement of team ball (6 - ball position if team bottom and 0 + ball position if team top)
	public int teamBallAvance(Team team, int ballPosition){
		if(team.getPosition() == TeamPosition.TOP)
			return ballPosition;
		return (ModelConstants.BOARD_SIZE - 1) - ballPosition;
	}
	
	
	//return the difference between the avancement of two ball
	public int ballAvance(Team team, int ballPosition){
		return teamBallAvance(team, ballPosition) - teamBallAvance(team.getEnemyTeam(), team.getEnemyTeam().getBallPlayer().getPosition().getX());
	}


	public String[] remplace1(Team team, int checkingDepth, String[] one, int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementOne) {
		String[] stockage;
		String[] result = new String[2];
		result[0] = "";	result[1] = "";
		if(deplacementOne == 'U') {
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'U');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'R');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'L');
			result[0] += stockage[0];	result[1] += stockage[1];
		} else if(deplacementOne == 'R') {
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'U');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'R');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'D');
			result[0] += stockage[0];	result[1] += stockage[1];
		} else if(deplacementOne == 'D') {
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'D');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'R');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'L');
			result[0] += stockage[0];	result[1] += stockage[1];
		} else {
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'U');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'D');
			result[0] += stockage[0];	result[1] += stockage[1];
			stockage = remplace2(team, checkingDepth, one, previousPositionFirst, numPreviousAction, player, 'L');
			result[0] += stockage[0];	result[1] += stockage[1];
		}
		
		return result;
	}
	
	
	public String[] remplace2(Team team, int checkingDepth, String[] one, int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementTwo) {
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		int[] nextPosition = previousPosition(player, reverse(deplacementTwo));
		boolean canRemplace = nextPosition[0] == previousPositionFirst[0];
		canRemplace = canRemplace   &&   nextPosition[1] == previousPositionFirst[1];
		
		if(canRemplace) {
			result[0] += one[numPreviousAction]+player.getName().charAt(player.getName().length()-1)+""+deplacementTwo;
			if(checkingDepth == 0)
				result[1] += team.getBallPlayer().getPosition().getX();
		}
		
		return result;
	}
	
	
	public String[] twoCanMove(Player ballPlayer, Stadium stadium, int checkingDepth, String[] one, int firstPlayer, int numberOfPlayer, Player player, int action){
		String[] result = new String[2];
		result[0] = "";
		result[1] = "";
		String[] testDeplacement;
		
		testDeplacement = twoCanMoveDirection(ballPlayer, stadium, checkingDepth, one, firstPlayer, numberOfPlayer, player, action, 'U');
			result[0] += testDeplacement[0];
			result[1] += testDeplacement[1];
		testDeplacement = twoCanMoveDirection(ballPlayer, stadium, checkingDepth, one, firstPlayer, numberOfPlayer, player, action, 'R');
			result[0] += testDeplacement[0];
			result[1] += testDeplacement[1];
		testDeplacement = twoCanMoveDirection(ballPlayer, stadium, checkingDepth, one, firstPlayer, numberOfPlayer, player, action, 'D');
			result[0] += testDeplacement[0];
			result[1] += testDeplacement[1];
		testDeplacement = twoCanMoveDirection(ballPlayer, stadium, checkingDepth, one, firstPlayer, numberOfPlayer, player, action, 'L');
			result[0] += testDeplacement[0];
			result[1] += testDeplacement[1];
		
		return result;
	}
	
	public String[] twoCanMoveDirection(Player ballPlayer, Stadium stadium, int checkingDepth, String[] one, int firstPlayer, int numberOfPlayer, Player player, int action, char deplacement){
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   one[action].charAt(1) != reverse(deplacement); //You can't back
		
		if(verifBack   &&   stadium.playerCanMove(player, moveOfChar(deplacement))) {
			result[0] = one[action]+numberOfPlayer+""+deplacement;
			if(checkingDepth == 0){
				result[1] = ""+ballPlayer.getPosition().getX();
			}
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
	

	public String[] twoCanMovePreviousBall(Team team, Stadium stadium, int checkingDepth, String[] one, int index, Player ballPlayer, String ballNumber) {
		String[] result = new String[2];
		result[0] = "";	result[1] = "";
		
		String[] view;
		view = twoCanMovePreviousBallDeplacement(team, stadium, checkingDepth, one, index, ballPlayer, ballNumber, 'U');
		result[0] += view[0]; result[1] += view[1];
		view = twoCanMovePreviousBallDeplacement(team, stadium, checkingDepth, one, index, ballPlayer, ballNumber, 'R');
		result[0] += view[0]; result[1] += view[1];
		view = twoCanMovePreviousBallDeplacement(team, stadium, checkingDepth, one, index, ballPlayer, ballNumber, 'D');
		result[0] += view[0]; result[1] += view[1];
		view = twoCanMovePreviousBallDeplacement(team, stadium, checkingDepth, one, index, ballPlayer, ballNumber, 'L');
		result[0] += view[0]; result[1] += view[1];
		
		return result;
	}

							
	public String[] twoCanMovePreviousBallDeplacement(Team team, Stadium stadium, int checkingDepth, String[] one, int index, Player ballPlayer, String ballNumber, char deplacement) {
		String[] result = new String[2];
		result[0] = "";	result[1] = "";
		if(stadium.playerCanMove(ballPlayer, moveOfChar(deplacement))){
			result[0] += one[index]+ballNumber+""+deplacement;
			if(checkingDepth == 0)
				result[1] += team.getBallPlayer().getPosition().getX();
		}
		return result;	
	}
		
		
	public String[] threeCanMove(Team team, Stadium stadium, int checkingDepth, String[] two, int numberOfPlayer, int firstPlayer, int index, Player player){
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		
		String[] view = threeCanMoveDeplacement(team,stadium, checkingDepth, two, numberOfPlayer, firstPlayer, index, player, 'U');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMoveDeplacement(team,stadium, checkingDepth, two, numberOfPlayer, firstPlayer, index, player, 'R');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMoveDeplacement(team,stadium, checkingDepth, two, numberOfPlayer, firstPlayer, index, player, 'D');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMoveDeplacement(team,stadium, checkingDepth, two, numberOfPlayer, firstPlayer, index, player, 'L');
		result[0] += view[0]; result[1] += view[1];
		
		return result;
	}
	

	public String[] threeCanMoveDeplacement(Team team, Stadium stadium, int checkingDepth, String[] two, int numberOfPlayer, int firstPlayer, int index, Player player, char deplacement) {
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   two[index].charAt(3) != reverse(deplacement); //You can't back
		if(verifBack   &&   !player.equals(team.getBallPlayer())   &&   stadium.playerCanMove(player, moveOfChar(deplacement))) {
			result[0] += two[index]+numberOfPlayer+""+deplacement;
			if(checkingDepth == 0)
				result[1] += team.getBallPlayer().getPosition().getX();
		}
		
		return result;
	}
	
	
		public String[] threeCanMovePreviousBall(Team team, Player ballPlayer, Stadium stadium, int checkingDepth, String[] two, int index, String ballNum) {
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		
		String[] view = threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, checkingDepth, two, index, ballNum, 'U');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, checkingDepth, two, index, ballNum, 'R');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, checkingDepth, two, index, ballNum, 'D');
		result[0] += view[0]; result[1] += view[1];
		view = threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, checkingDepth, two, index, ballNum, 'L');
		result[0] += view[0]; result[1] += view[1];
		
		return result;
	}
	
	
	public String[] threeCanMovePreviousBallDeplacement(Team team, Player ballPlayer, Stadium stadium, int checkingDepth, String[] two, int index, String ballNum, char deplacement) {
		String[] result = new String[2];
		result[0] = ""; result[1] = "";
		
		if(stadium.playerCanMove(ballPlayer, moveOfChar(deplacement))) {
			result[0] += two[index]+ballNum+deplacement;
			if(checkingDepth == 0)
				result[1] += team.getBallPlayer().getPosition().getX();
		}
		
		return result;
	}
}
