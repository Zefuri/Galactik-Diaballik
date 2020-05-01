package Listeners;

public class Case {
	private int x, y;
	
	public Case(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Case(Case c) {
		this.x = c.getX();
		this.y = c.getY();
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
