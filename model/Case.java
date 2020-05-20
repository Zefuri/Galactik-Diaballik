package model;

import java.util.regex.Pattern;

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
	
	public Case(String s) {
		this.parseJSONedCase(s);
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
	
	public String JSONed() {
		return "(" + this.x + "," + this.y + ")";
	}
	
	public void parseJSONedCase(String jsonedCase) {
		if (Pattern.matches("[(][0-9][,][0-9][)]", jsonedCase)) {
			this.x = Integer.parseInt("" + jsonedCase.charAt(1));
			this.y = Integer.parseInt("" + jsonedCase.charAt(3));
		} else {
			throw new IllegalStateException("The given case does not have the good format!");
		}
	}
	
	public boolean equals(Case c) {
		return ((this.x == c.getX()) && (this.y == c.getY()));
	}
}
