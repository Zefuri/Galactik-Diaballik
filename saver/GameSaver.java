package saver;

import model.Case;
import model.ModelConstants;
import model.Player;
import model.Stadium;
import model.enums.TeamPosition;

public class GameSaver {
	private Stadium stadium;
	
	public GameSaver(Stadium stadium) {
		this.stadium = stadium;
	}
	
	private StringBuilder saveCurrentBoard() {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
			for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
				Player currPlayer = this.stadium.getPlayer(new Case(i,j));
				
				if (currPlayer == null) {
					//No player on this case, we add the empty symbol
					builder.append(SaverConstants.EMPTY_CASE);
				} else {
					//We check either the player is on the first team or the second and if he has the ball
					if (currPlayer.getTeam().equals(this.stadium.getTeam(TeamPosition.TOP))) {
						if (currPlayer.hasBall()) {
							builder.append(SaverConstants.TOP_TEAM_PLAYER_WITH_BALL);
						} else {
							builder.append(SaverConstants.TOP_TEAM_PLAYER_ALONE);
						}
					} else {
						if (currPlayer.hasBall()) {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_WITH_BALL);
						} else {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_ALONE);
						}
					}
				}
				
				builder.append(SaverConstants.LISIBILITY_BLANK);
			}
			
			builder.append(SaverConstants.NEXT_LINE);
		}
		
		System.out.println(builder.toString());
		
		return builder;
	}
	
	public void generateJSON() {
		StringBuilder filledBuilder = this.saveCurrentBoard();
		
		JSONObject obj = new JSONObject();
	}
}
