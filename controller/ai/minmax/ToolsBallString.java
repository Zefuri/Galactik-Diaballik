package controller.ai.minmax;

import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class ToolsBallString {
	
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
}
