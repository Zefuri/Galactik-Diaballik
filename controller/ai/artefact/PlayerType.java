package ai.artefact;

import model.Stadium;
import model.Team;
import model.enums.TeamPosition;

//import model.Player;
//
//// Common class for all player: ai or human
public abstract class PlayerType {

	Team team;
	Stadium stadium;

	public PlayerType(Stadium stadium, TeamPosition position) {
		this.stadium = stadium;
		team = this.stadium.getTeam(position);
	}	
	
	//For ai
	boolean timeOut() {
		return false;
	}

	//For human
	boolean play(int abs, int ord) {
		return false;
	}
}
