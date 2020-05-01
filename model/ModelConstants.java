package model;

public class ModelConstants {
	public static final int ACTION_MOVE = 0;
	public static final int ACTION_PASS = 1;
	public static final int ACTION_END_OF_TURN = 1;
	public static final int TEAM_ONE = 0;
    public static final int TEAM_TWO = 1;
    public static final int BOARD_SIZE = 7;
    public static final int INVALID_DIR = 0;
    public static final int DIR_LEFT = 1;
    public static final int DIR_RIGHT = 2;
    public static final int DIR_UP = 3;
    public static final int DIR_DOWN = 4;
    public static final int DIR_BOT_RIGHT = 5;
    public static final int DIR_TOP_LEFT = 6;
    public static final int DIR_TOP_RIGHT = 7;
    public static final int DIR_BOT_LEFT = 8;
    public static final int MAX_PASSES_PER_TOUR = 1;
    public static final int MAX_MOVES_PER_TOUR = 2;
    public static final int WIN = 1;
    public static final int ANTIPLAY = 2;
    
    public static final char UP = 'U';
    public static final char DOWN = 'D';
    public static final char LEFT = 'L';
    public static final char RIGHT = 'R';
    public static final char ERROR = 'X';
    
    private ModelConstants() {
    	
    }
}
