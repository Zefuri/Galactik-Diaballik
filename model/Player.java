package model;

public class Player {
	private String team;
	private boolean ballPossession;
	private int i;
	private int j;

	public Player(String team) {
		this.team = team;
		this.ballPossession = false;
	}

	public Player(String team, int x, int y) {
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
