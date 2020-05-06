package model;

public class NoPlayerFoundException extends Exception {
	public NoPlayerFoundException() {
		
	}
	
	public NoPlayerFoundException(String message) {
		super(message);
	}
}
