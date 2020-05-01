package model;

public class Player {
	private final int team;
	private final int number;
	private boolean ballPossession;
	private boolean selectedPlayer;
	private int i;
	private int j;

	public Player(int num, int team) {
		this.team = team;
		this.ballPossession = false;
		this.number = num;
		this.selectedPlayer = false;
	}

	public Player(int num, int team, int x, int y) {
		this.team = team;
		this.ballPossession = false;
		this.i = x;
		this.j = y;
		this.number = num;
		this.selectedPlayer = false;
	}

	public int getNum() {
		return this.number;
	}

	public int getI(){
		return this.i;
	}

	public int getJ(){
		return this.j;
	}

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
	
	public void setBallPossession(boolean ballPossession) {
		this.ballPossession = ballPossession;
	}

	public boolean isATeammate(Player player) {
		return this.team == player.team;
	}
	
	public void setPlayerSelected(final boolean selected) {
		this.selectedPlayer = selected;
	}
	
	public boolean playerSelected() {
		return this.selectedPlayer;
	}
}
