package model;

public class Case {
	private int x, y;
	
	public Case() {
		this.x = -1;
		this.y = -1;
	}
	
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
		if (this.x == -1 || this.y == -1) {
			throw new IllegalStateException("Selected case has not been instanciated properly.");
		}
		
		return this.x;
	}
	
	void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		if (this.x == -1 || this.y == -1) {
			throw new IllegalStateException("Selected case has not been instanciated properly.");
		}
		
		return this.y;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("Case (" + this.x + "," + this.y + ")");
		
		return s.toString();
	}
	
	public boolean equals(Case c) {
		return ((this.x == c.getX()) && (this.y == c.getY()));
	}
}
