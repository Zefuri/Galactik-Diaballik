package controller.ai;

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
	public int teamBallAvance(Team team){
		if(team.getPosition() == TeamPosition.TOP)
			return team.getBallPlayer().getPosition().getX();
		return (ModelConstants.BOARD_SIZE - 1) - team.getBallPlayer().getPosition().getX();
	}
	
	//return the difference between the avancement of two ball
	public int ballAvance(Team team){
		return teamBallAvance(team) - teamBallAvance(team.getEnemyTeam());
	}


	public String remplace1(Team team, String[] one, int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementOne) {
		String result = "";
		if(deplacementOne == 'U') {
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'L');
		} else if(deplacementOne == 'R') {
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'D');
		} else if(deplacementOne == 'D') {
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'D');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'R');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'L');
		} else {
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'U');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'D');
			result += remplace2(team, one, previousPositionFirst, numPreviousAction, player, 'L');
		}
		
		return result;
	}
	
	
	public String remplace2(Team team, String[] one, int[] previousPositionFirst, int numPreviousAction, Player player, char deplacementTwo) {
		String result = "";
		int[] nextPosition = previousPosition(player, reverse(deplacementTwo));
		boolean canRemplace = nextPosition[0] == previousPositionFirst[0]   &&   nextPosition[1] == previousPositionFirst[1];
		
		if(canRemplace) {
			result += one[numPreviousAction]+player.getName().charAt(player.getName().length()-1)+""+deplacementTwo;
		}
		
		return result;
	}
	
	
	public String twoCanMove(Player ballPlayer, Stadium stadium, String[] one, int firstPlayer, int numberOfPlayer, Player player, int action){
		
		String result = twoCanMoveDirection(ballPlayer, stadium, one, firstPlayer, numberOfPlayer, player, action, 'U');
		result += twoCanMoveDirection(ballPlayer, stadium, one, firstPlayer, numberOfPlayer, player, action, 'R');
		result += twoCanMoveDirection(ballPlayer, stadium, one, firstPlayer, numberOfPlayer, player, action, 'D');
		result += twoCanMoveDirection(ballPlayer, stadium, one, firstPlayer, numberOfPlayer, player, action, 'L');
		
		return result;
	}
	
	public String twoCanMoveDirection(Player ballPlayer, Stadium stadium, String[] one, int firstPlayer, int numberOfPlayer, Player player, int action, char deplacement){
		String result = "";
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   one[action].charAt(1) != reverse(deplacement); //You can't back
		
		if(verifBack   &&   stadium.playerCanMove(player, moveOfChar(deplacement))) {
			result = one[action]+numberOfPlayer+""+deplacement;
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
	

	public String twoCanMovePreviousBall(Team team, Stadium stadium, String[] one, int index, Player ballPlayer, String ballNumber) {
		String result = twoCanMovePreviousBallDeplacement(team, stadium, one, index, ballPlayer, ballNumber, 'U');
		result += twoCanMovePreviousBallDeplacement(team, stadium, one, index, ballPlayer, ballNumber, 'R');
		result += twoCanMovePreviousBallDeplacement(team, stadium, one, index, ballPlayer, ballNumber, 'D');
		result += twoCanMovePreviousBallDeplacement(team, stadium, one, index, ballPlayer, ballNumber, 'L');
		
		return result;
	}

							
	public String twoCanMovePreviousBallDeplacement(Team team, Stadium stadium, String[] one, int index, Player ballPlayer, String ballNumber, char deplacement) {
		String result = "";
		if(stadium.playerCanMove(ballPlayer, moveOfChar(deplacement))){
			result += one[index]+ballNumber+""+deplacement;
		}
		return result;	
	}
		
		
	public String threeCanMove(Team team, Stadium stadium, String[] two, int numberOfPlayer, int firstPlayer, int index, Player player){
		String result = threeCanMoveDeplacement(team,stadium, two, numberOfPlayer, firstPlayer, index, player, 'U');
		result += threeCanMoveDeplacement(team,stadium, two, numberOfPlayer, firstPlayer, index, player, 'R');
		result += threeCanMoveDeplacement(team,stadium, two, numberOfPlayer, firstPlayer, index, player, 'D');
		result += threeCanMoveDeplacement(team,stadium, two, numberOfPlayer, firstPlayer, index, player, 'L');
		
		return result;
	}
	

	public String threeCanMoveDeplacement(Team team, Stadium stadium, String[] two, int numberOfPlayer, int firstPlayer, int index, Player player, char deplacement) {
		String result = "";
		
		boolean sameFirstPlayer = numberOfPlayer == firstPlayer;
		boolean verifBack = !sameFirstPlayer   ||   two[index].charAt(3) != reverse(deplacement); //You can't back
		if(verifBack   &&   !player.equals(team.getBallPlayer())   &&   stadium.playerCanMove(player, moveOfChar(deplacement))) {
			result += two[index]+numberOfPlayer+""+deplacement;
		}
		
		return result;
	}
	
	
		public String threeCanMovePreviousBall(Team team, Player ballPlayer, Stadium stadium, String[] two, int index, String ballNum) {
		String result = threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, two, index, ballNum, 'U');
		result += threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, two, index, ballNum, 'R');
		result += threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, two, index, ballNum, 'D');
		result += threeCanMovePreviousBallDeplacement(team, ballPlayer, stadium, two, index, ballNum, 'L');
		
		return result;
	}
	
	
	public String threeCanMovePreviousBallDeplacement(Team team, Player ballPlayer, Stadium stadium, String[] two, int index, String ballNum, char deplacement) {
		String result = "";
		
		if(stadium.playerCanMove(ballPlayer, moveOfChar(deplacement))) {
			result += two[index]+ballNum+deplacement;
		}
		
		return result;
	}
}
