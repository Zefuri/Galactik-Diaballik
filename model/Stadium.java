package model;

import static java.lang.Math.abs;

import model.enums.TeamPosition;
import model.enums.ActionType;

public class Stadium {
    private Team topTeam;
    private Team bottomTeam;

    public Stadium(){
        topTeam = null;
        bottomTeam = null;
    }
    
    public void setTeam(TeamPosition position, Team team) {
        if (position == TeamPosition.TOP) {
            topTeam = team;
        } else {
            bottomTeam = team;
        }

        initTeam(position);
    }

    public Team getTeam(TeamPosition position) {
        return position == TeamPosition.TOP ? topTeam : bottomTeam;
    }

    public void reset() {
        initTeam(TeamPosition.TOP);
        initTeam(TeamPosition.BOTTOM);
    }
    
    private void initTeam(TeamPosition position) {
        int y = position == TeamPosition.TOP ? 0 : ModelConstants.BOARD_SIZE - 1;
        int x = 0;

        for (Player p : getTeam(position).getPlayers()) {
            p.setPosition(x, y);
            p.setBallPossession(x == 3);
            x++;
        }
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
        
        if (playerOne.isATeammate(playerTwo) && (playerOne.getBallPossession()) && !(playerTwo.getBallPossession())){
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
            
            if ((checking.getPosition().getX() == limit) && checking.getBallPossession()) {
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
            case MOVE:
                Player player = action.getMovedPlayer();
                
                if (this.nbMove == ModelConstants.MAX_MOVES_PER_TOUR || (player.getTeam().getPosition() != playing)) {
                    result = -1;
                    break;
                }

                char dir = action.getDirection();
                move(player, dir);
                this.nbMove++;
                
                break;

            case PASS:
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

