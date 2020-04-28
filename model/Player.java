package model;

public class Player {
	private String team;
	private boolean ballPossession;
	
	public Player(String team) {
		this.team = team;
		this.ballPossession = false;
	}
	
	public String getTeam() {
		return team;
	}
	
	public boolean getBallPossession() {
		return ballPossession;
	}
	
	public void setTeam(String team) {
		this.team = team;
	}
	
	public void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}
}
