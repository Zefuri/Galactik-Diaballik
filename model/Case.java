package model;

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

	void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return this.x;
	}
	
	void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return this.y;
	}
}
