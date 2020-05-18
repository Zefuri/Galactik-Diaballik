package controller.ai;

import model.Stadium;
import model.Team;
import model.Player;
import model.Case;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

public class ToolsBallAction {
	
	
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


	
	public Case nextCase(Case previousCase, MoveDirection deplacement){
		Case result;
		
		if(deplacement == MoveDirection.UP) {
			result = new Case(previousCase.getX()-1, previousCase.getY());
		} else if(deplacement == MoveDirection.RIGHT) {
			result = new Case(previousCase.getX(), previousCase.getY()+1);
		} else if(deplacement == MoveDirection.DOWN) {
			result = new Case(previousCase.getX()+1, previousCase.getY());
		} else {
			result = new Case(previousCase.getX(), previousCase.getY()-1);
		}
		
		return result;
	}
}
