package model;

import static java.lang.Math.abs;

public class Stadium {
    Player[][] board = new Player[7][7];
    Player[] snowKids = new Player[7];
    Player[] shadows = new Player[7];
    private int turn;
    private int nbPass;
    private int nbMove;

    public Stadium(){
        this.initTeam(snowKids, new Team("snowKids", this, ModelConstants.TEAM_ON_BOT), 6);
        this.initTeam(shadows, new Team("shadows", this, ModelConstants.TEAM_ON_TOP), 0);
        this.resetBoard();
        this.turn = 0;
        this.nbMove = 0;
        this.nbPass = 0;
    }
    
    //----------------------------------------------------getters-------------------------------------------------------

    public int getTurn(){
        return this.turn;
    }

    public int getNbPass() {
        return this.nbPass;
    }

    public int getNbMove() {
        return this.nbMove;
    }

    public int getNbAction(){
        return this.nbMove + this.nbPass;
    }

    public Player[] getSnowKids(){
        return this.snowKids;
    }

    public Player[] getShadows(){
        return this.shadows;
    }

    public int whosTurn(){
        return this.turn % 2;
    }

    public int whoJustPlayed(){
        return (this.turn - 1) % 2;
    }

    public Player whatsInTheBox(int i, int j){ //what's in i j
        return this.board[i][j];
    }

    public boolean isABallHere(int i, int j){ //is there a ball in i j
        return (!this.isEmpty(i, j) && this.whatsInTheBox(i, j).hasBall());
    }

    public boolean isAPlayerOnly(int i, int j) {
    	return (!this.isEmpty(i, j) && !isABallHere(i,j));
    }
    
    public boolean isEmpty(int i, int j){ //is i j empty
        return this.whatsInTheBox(i, j) == null;
    }

    public Player[] getOpponent(int team) {
        if (team == ModelConstants.TEAM_ONE) {
            return shadows;
        }
        else {
            if (team == ModelConstants.TEAM_TWO) {
                return snowKids;
            }
        }
        
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void initTeam(Player[] team, Team t, int x) {
        for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
        	boolean ballPossession = false;
        	
        	if (i == 3) {
        		ballPossession = true;
        	}
        	
            team[i] = new Player(t, "undefined", new Case(x, i), ballPossession);
        }
    }

    public void resetBoard() {  //initializing board
        for(int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
            board[6][i] = snowKids[i];
           // snowKids[i].move(6, i);
            board[0][i] = shadows[i];
          //  shadows[i].move(0, i);
        }
        
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
                board[i][j] = null;
            }
        }
        
        board[0][3].setBallPossession(true);
        board[6][3].setBallPossession(true);
    }
    
    public void move(Player player, char direction) {
    	if (playerCanMove(player, direction)) {
    		simpleMove(player, direction);
    	}
    }

    private void simpleMove(Player player, char direction){ //move player at position in the selected direction
    	// /!\ Caution: Please use the playerCanMove() function before using this one/!\
    	if (playerCanMove(player, direction)) {
	    	Case playerPos = player.getPosition();
	    	
	        this.board[playerPos.getX()][playerPos.getY()] = null;
	        
	        switch (direction) {
	        	case ModelConstants.UP:
	        		this.board[playerPos.getX() - 1][playerPos.getY()] = player;
	        		break;
	        	
	        	case ModelConstants.DOWN:
	        		this.board[playerPos.getX() + 1][playerPos.getY()] = player;
	        		break;
	        		
	        	case ModelConstants.RIGHT:
	        		this.board[playerPos.getX()][playerPos.getY() + 1] = player;
	        		break;
	        	
	        	case ModelConstants.LEFT:
	        		this.board[playerPos.getX()][playerPos.getY() - 1] = player;
	        		break;
	        }
    	} else {
    		throw new RuntimeException("You did not use the playerCanMove() function as mentionned!!!");
    	}
    }
    
    public boolean playerCanMove(Player player, char move) {
    	boolean canMove = false;
        int i = player.getPosition().getX();
        int j = player.getPosition().getY();
        
        if (!this.isABallHere(i, j)) {
            switch (move) {
                case ModelConstants.UP:
                    if (i > 0 && this.isEmpty(i - 1, j)) {
                        canMove = true;
                    }
                    
                    break;
                    
                case ModelConstants.DOWN:
                    if (i < 6 && this.isEmpty(i + 1, j)) {
                        canMove = true;
                    }
                    
                    break;
                    
                case ModelConstants.LEFT:
                    if (j > 0 && this.isEmpty(i, j - 1)) {
                        canMove = true;
                    }
                    
                    break;
                    
                case ModelConstants.RIGHT:
                    if (j < 6 && this.isEmpty(i, j + 1)) {
                        canMove = true;
                    }
                    
                    break;
                    
                default:
                    System.out.println("wrong move input in move function");
            }
        }
        
        return canMove;
    }

    public int direction(Player playerOne, Player playerTwo) {
    	Case playerOnePos = playerOne.getPosition();
    	Case playerTwoPos = playerTwo.getPosition();
    	int playerOneX = playerOnePos.getX();
    	int playerOneY = playerOnePos.getY();
    	int playerTwoX = playerTwoPos.getX();
    	int playerTwoY = playerTwoPos.getY();
    	
        if (playerOneX == playerTwoX) {//same line
            if (playerTwoY < playerOneY) {  //left
                return ModelConstants.DIR_LEFT;//same line left
            } else {//right
                return ModelConstants.DIR_RIGHT;//same line right
            }
        } else {
            if (playerOneY == playerTwoY) {// same column
                if (playerTwoX < playerOneX) {  //up
                    return ModelConstants.DIR_UP;//same column up
                }
                else{//down
                    return ModelConstants.DIR_DOWN;//same column down
                }
            }
            else{//diag check
                int diffI = playerOneX - playerTwoX;
                int diffJ = playerOneY - playerTwoY;
                if (diffJ == diffI) {
                    if (diffJ > 0) {//bottom right
                        return ModelConstants.DIR_BOT_RIGHT;
                    }
                    else {//top left
                        return ModelConstants.DIR_TOP_LEFT;
                    }
                }
                else{
                    if(diffJ == -diffI) {
                        if (diffJ > 0) {//top right
                            return ModelConstants.DIR_TOP_RIGHT;
                        }
                        else {//bottom left
                            return ModelConstants.DIR_BOT_LEFT;
                        }
                    }
                }
            }
        }
        
        return ModelConstants.INVALID_DIR;
    }

    private void simplePass(Player playerOne, Player playerTwo){ //player at i j pass the ball to nextI nextJ
        playerOne.setBallPossession(false);
        playerTwo.setBallPossession(true);
    }

    public boolean playerCanPass(Player playerOne, Player playerTwo) { //player at i j pass the ball to nextI nextJ
        boolean canPass = true;
        
        if (playerOne.isATeammate(playerTwo) && (playerOne.hasBall()) && !(playerTwo.hasBall())){
            int dir = direction(playerOne, playerTwo);
            
            if (dir != 0) {
                Team opponents = playerOne.getTeam().getEnemyTeam();
               
                while (opponents.getPlayers().hasNext()) {
                	if (!canPass) {
                		break;
                	}
                	
                	Player currOpponent = opponents.getPlayers().next();
                	
                	if (this.direction(playerOne, currOpponent) == dir) {
                        int distFriend = abs(playerTwo.getPosition().getX() - playerOne.getPosition().getX()) + abs(playerTwo.getPosition().getY() - playerOne.getPosition().getY());
                        int distOpp = abs(currOpponent.getPosition().getX() - playerOne.getPosition().getX()) + abs(currOpponent.getPosition().getY() - playerOne.getPosition().getY());
                        
                        if (distFriend > distOpp){
                            canPass = false;
                        }
                   }
                }
            } else {
            	canPass = false;
            }
        } else{
        	canPass = false;
        }
        
        return canPass;
    }

    public void pass(Player playerOne, Player playerTwo) { //player at i j pass the ball to nextI nextJ
        if (playerCanPass(playerOne, playerTwo)) {
        	simplePass(playerOne, playerTwo);
        }
    }

    public boolean antiplay(int team) {
        Player[] playerlist;
        boolean result = false;
        
        if (team == ModelConstants.TEAM_ONE){
            playerlist = this.getSnowKids();
        }
        else {
            if (team == ModelConstants.TEAM_TWO){
                playerlist = this.getShadows();
            }
            else {
                return false;
            }
        }
        
        int contact = 0;
        boolean leftFriend;
        boolean rightFriend;
        
        for (int k = 0; k < ModelConstants.BOARD_SIZE; k++) {
            leftFriend = false;
            rightFriend = false;
            Player checking = playerlist[k];
            int i = checking.getPosition().getX();
            int j = checking.getPosition().getY();
            
            if (j <= 0 || ((i > 0 && checking.isATeammate(this.whatsInTheBox(i - 1, j - 1))) || checking.isATeammate(this.whatsInTheBox(i, j - 1)) || (i < 6 && checking.isATeammate(this.whatsInTheBox(i + 1, j - 1))))){ //if there is a mate to the left
                leftFriend = true;
            }
            
            if (j >= 6 || ((i > 0 && checking.isATeammate(this.whatsInTheBox(i - 1, j + 1))) || checking.isATeammate(this.whatsInTheBox(i, j + 1)) || (i < 6 && checking.isATeammate(this.whatsInTheBox(i + 1, j + 1))))){ // if there is a mate to the right
                rightFriend = true;
            }
            
            if ((i > 0 && (!this.isEmpty(i - 1, j) && !checking.isATeammate(this.whatsInTheBox(i - 1, j + 1)))) || (i < 6 && (!this.isEmpty(i + 1, j) && !checking.isATeammate(this.whatsInTheBox(i + 1, j + 1))))){ //if there is opponent in contact up or down
                contact++;
            }
            
            if (!(leftFriend && rightFriend)){
                return false;
            }
        }
        
        if (contact >= 3) {
            result = true;
        }
        return result;
    }

    public boolean isAWin(int team) {
        Player[] playerlist;
        int limit;
        
        if (team == ModelConstants.TEAM_ONE) {
            playerlist = this.getSnowKids();
            limit = 0;
        } else {
            if (team == ModelConstants.TEAM_TWO) {
                playerlist = this.getShadows();
                limit = 6;
            } else {
                return false;
            }
        }
        
        Player checking;
        
        for (int k = 0; k < 6; k++) {
            checking = playerlist[k];
            
            if ((checking.getPosition().getX() == limit) && checking.hasBall()) {
                return true;
            }
        }
        
        return false;
    }

	public String toString(){
		//board read
		StringBuilder game = new StringBuilder();
		
//		for(int abscisse = 0; abscisse != board.length; abscisse++){
//			for(int ordonnee = 0; ordonnee != board.length; ordonnee++){
//				game.append("|");
//				
//				if(board[abscisse][ordonnee] == null){
//					game.append("___");
//					
//				} else if(board[abscisse][ordonnee].getTeam() == 0   &&   !board[abscisse][ordonnee].hasBall()){
//					game.append("a").append(board[abscisse][ordonnee].getNum()).append(".");
//					
//				} else if(board[abscisse][ordonnee].getTeam() == 1   &&   !board[abscisse][ordonnee].hasBall()){
//					game.append("b").append(board[abscisse][ordonnee].getNum()).append(".");
//					
//				} else if(board[abscisse][ordonnee].getTeam()==0 && board[abscisse][ordonnee].hasBall()){
//					game.append("a").append(board[abscisse][ordonnee].getNum()).append("*");
//					
//				} else{
//					game.append("b").append(board[abscisse][ordonnee].getNum()).append("*");
//				}
//				
//			}
//			
//			game.append("|\n");
//			
//		}		
		
		return game.toString();
	}

	public char getMoveDirection(Player player, int i, int j){
        int playerI = player.getPosition().getX();
        int playerJ = player.getPosition().getY();
        char result = ModelConstants.ERROR;
        
        if (playerJ == j){
            if (playerI == i - 1){
                result = ModelConstants.DOWN;
            }
            else{
                if (playerI == i + 1) {
                    result = ModelConstants.UP;
                }
            }
        }
        else{
            if (playerI == i){
                if (playerJ == j - 1){
                    result = ModelConstants.RIGHT;
                }
                else{
                    if (playerJ == j + 1) {
                        result = ModelConstants.LEFT;
                    }
                }
            }
        }
        
        return result;
    }

	private void resetTurnVariables() {
        this.nbMove = 0;
        this.nbPass = 0;
	}

	public int normalTurn(Action action) { //what controller must use
        int result = 0;

        int playing = this.whosTurn();

        switch(action.getType()) {
            case ModelConstants.ACTION_MOVE:
                Player player = action.getMovedPlayer();
                
                if (this.nbMove == ModelConstants.MAX_MOVES_PER_TOUR || (player.getTeam().getPosition() != playing)) {
                    result = -1;
                    break;
                }

                char dir = action.getDirection();
                move(player, dir);
                this.nbMove++;
                
                break;

            case ModelConstants.ACTION_PASS:
                Player firstPlayer = action.getPreviousPlayer();
                Player secondPlayer = action.getNextPlayer();
                
                if (this.nbPass == ModelConstants.MAX_PASSES_PER_TOUR || (firstPlayer.getTeam().getPosition() != playing)|| (secondPlayer.getTeam().getPosition() != playing)) {
                    result = -1;
                    break;
                }

                pass(firstPlayer, secondPlayer);
                this.nbPass++;
                
                break;

            case 2:
                if ((this.nbMove + this.nbPass) == 0) {
                    result = -1;
                    break;
                }
                
                this.resetTurnVariables();
                this.turn++;
                
                break;

            default:
                result = -1;
                break;
        }
        
        if (this.nbPass == ModelConstants.MAX_PASSES_PER_TOUR && this.nbMove == ModelConstants.MAX_MOVES_PER_TOUR) {
            this.resetTurnVariables();
            this.turn++;
        }
        
        //TODO revoir
        
//        if (this.isAWin(action.getWhosturn())) {
//            result = ModelConstants.WIN;
//        }
//        
//        if (this.antiplay(action.getWhosturn())) {
//            result = ModelConstants.ANTIPLAY;
//        }

        return result;
	}
}

