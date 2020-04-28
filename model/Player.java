package model;

public class Player {
	private int team;
	private boolean ballPossession;
	private int i;
	private int j;

	public Player(int team) {
		this.team = team;
		this.ballPossession = false;
	}

	public Player(int team, int x, int y) {
		this.team = team;
		this.ballPossession = false;
		this.i = x;
		this.j = y;
	}

	public int getI(){return this.i;}

	public int getJ(){return this.j;}

	public void movePlayer(int x, int y){
		this.i = x;
		this.j = y;
	}

	public int getTeam() {
		return team;
	}
	
	public boolean getBallPossession() {
		return ballPossession;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	public void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}

	public boolean isATeammate(Player player) {
		return this.team == player.team;
	}
}
