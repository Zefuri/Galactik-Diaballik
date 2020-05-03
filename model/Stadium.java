package model;

import static java.lang.Math.abs;

import model.enums.TeamPosition;
import model.enums.ActionResult;
import model.enums.ActionType;
import model.enums.MoveDirection;

public class Stadium {
    private Team topTeam;
    private Team bottomTeam;
    
    private int nbMoves;
    private int nbPasses;
    private int currentTurn;

    public Stadium() {
        topTeam = new Team("snowKids", TeamPosition.TOP, this);
        bottomTeam = new Team("shadows", TeamPosition.BOTTOM, this);
      
        nbMoves = 0;
        nbPasses = 0;
        currentTurn = 0;
    }
    
    public Team getTeam(TeamPosition position) {
        return position == TeamPosition.TOP ? topTeam : bottomTeam;
    }

    public void reset() {
        topTeam.initialize();
        bottomTeam.initialize();
    }
    
    public Player getPlayer(Case position) {
    	for (Player p : topTeam.getPlayers()) {
    		Case currPos = p.getPosition();
    		
    		if (currPos.getX() == position.getX()) {
    			if (currPos.getY() == position.getY()) {
    				return p;
    			}
    		}
    	}
    	
    	for (Player p : bottomTeam.getPlayers()) {
    		Case currPos = p.getPosition();
    		
    		if (currPos.getX() == position.getX()) {
    			if (currPos.getY() == position.getY()) {
    				return p;
    			}
    		}
    	}
    	
    	return null;
    }
    
    public boolean hasABall(Case position) {
    	Player p ;
		
    	if ((p = getPlayer(position)) != null) {
    		if (p.hasBall()) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public boolean hasAPlayerOnly(Case position) {
    	Player p ;
		
    	if ((p = getPlayer(position)) != null) {
    		if (!p.hasBall()) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    public void move(Player player, MoveDirection direction) {
    	if (playerCanMove(player, direction)) {
    		simpleMove(player, direction);
    	}
    }

    private void simpleMove(Player player, MoveDirection direction){ //move player at position in the selected direction
    	// /!\ Caution: Please use the playerCanMove() function before using this one/!\
    	if (playerCanMove(player, direction)) {
	    	Case playerPos = player.getPosition();
	    	
	        switch (direction) {
	        	case UP:
	        		player.getPosition().setX(playerPos.getX() - 1);
	        		player.getPosition().setX(playerPos.getX());
	        		break;
	        	
	        	case DOWN:
	        		player.getPosition().setX(playerPos.getX() + 1);
	        		player.getPosition().setX(playerPos.getX());
	        		break;
	        		
	        	case RIGHT:
	        		player.getPosition().setX(playerPos.getX());
	        		player.getPosition().setX(playerPos.getX() + 1);
	        		break;
	        	
	        	case LEFT:
	        		player.getPosition().setX(playerPos.getX());
	        		player.getPosition().setX(playerPos.getX() - 1);
	        		break;
	        }
    	} else {
    		throw new RuntimeException("You did not use the playerCanMove() function as mentionned!!!");
    	}
    }
    
    public boolean playerCanMove(Player player, MoveDirection direction) {
    	boolean canMove = true;
        int i = player.getPosition().getX();
        int j = player.getPosition().getY();
        
        if (!player.hasBall()) {
        	for (Player currPlayer : player.getTeam().getPlayers()) {
        		//For each player of the ally team, we check if he is not badly positioned
	            switch (direction) {
	                case UP:
	                    if (i <= 0 || currPlayer.getPosition().equals(new Case(i - 1, j))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case DOWN:
	                    if (i >= 6 || currPlayer.getPosition().equals(new Case(i + 1, j))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case LEFT:
	                    if (j <= 0 || currPlayer.getPosition().equals(new Case(i, j - 1))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case RIGHT:
	                    if (j >= 6 || currPlayer.getPosition().equals(new Case(i, j + 1))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                default:
	                    System.out.println("wrong move input in move function");
	            }
	            
	            if (!canMove) {
	            	break;
	            }
        	}
        	
        	for (Player currPlayer : player.getTeam().getEnemyTeam().getPlayers()) {
        		//For each player of the enemy team, we check if he is not badly positioned
        		switch (direction) {
	                case UP:
	                    if (i <= 0 || currPlayer.getPosition().equals(new Case(i - 1, j))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case DOWN:
	                    if (i >= 6 || currPlayer.getPosition().equals(new Case(i + 1, j))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case LEFT:
	                    if (j <= 0 || currPlayer.getPosition().equals(new Case(i, j - 1))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                case RIGHT:
	                    if (j >= 6 || currPlayer.getPosition().equals(new Case(i, j + 1))) {
	                        canMove = false;
	                    }
	                    
	                    break;
	                    
	                default:
	                    System.out.println("wrong move input in move function");
	            }
	            
	            if (!canMove) {
	            	break;
	            }
        	}
        }
        
        return canMove;
    }

    public MoveDirection direction(Player playerOne, Player playerTwo) {
    	Case playerOnePos = playerOne.getPosition();
    	Case playerTwoPos = playerTwo.getPosition();
    	int playerOneX = playerOnePos.getX();
    	int playerOneY = playerOnePos.getY();
    	int playerTwoX = playerTwoPos.getX();
    	int playerTwoY = playerTwoPos.getY();
    	
        if (playerOneX == playerTwoX) {//same line
            if (playerTwoY < playerOneY) {  //left
                return MoveDirection.LEFT;//same line left
            } else {//right
                return MoveDirection.RIGHT;//same line right
            }
        } else {
            if (playerOneY == playerTwoY) {// same column
                if (playerTwoX < playerOneX) {  //up
                    return MoveDirection.UP;//same column up
                }
                else{//down
                    return MoveDirection.DOWN;//same column down
                }
            }
            else{//diag check
                int diffI = playerOneX - playerTwoX;
                int diffJ = playerOneY - playerTwoY;
                if (diffJ == diffI) {
                    if (diffJ > 0) {//bottom right
                        return MoveDirection.DOWN_RIGHT;
                    }
                    else {//top left
                        return MoveDirection.UP_LEFT;
                    }
                }
                else{
                    if(diffJ == -diffI) {
                        if (diffJ > 0) {//top right
                            return MoveDirection.UP_RIGHT;
                        }
                        else {//bottom left
                            return MoveDirection.DOWN_LEFT;
                        }
                    }
                }
            }
        }
        
        return null;
    }

    private void simplePass(Player playerOne, Player playerTwo){ //player at i j pass the ball to nextI nextJ
        playerOne.setBallPossession(false);
        playerTwo.setBallPossession(true);
    }

    public boolean playerCanPass(Player playerOne, Player playerTwo) { //player at i j pass the ball to nextI nextJ
        boolean canPass = true;
        
        if (playerOne.isATeammate(playerTwo) && (playerOne.hasBall()) && !(playerTwo.hasBall())){
            MoveDirection dir = direction(playerOne, playerTwo);
            
            if (dir != null) {
                Team opponents = playerOne.getTeam().getEnemyTeam();
               
                for (Player currOpponent : opponents.getPlayers()){
                	if (!canPass) {
                		break;
                	}
                	
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

    public void pass(Player playerOne, Player playerTwo) { //player playerOne passes the ball to playerTwo
        if (playerCanPass(playerOne, playerTwo)) {
        	simplePass(playerOne, playerTwo);
        }
    }

    public boolean antiplay(TeamPosition team) {
        Team playerList = this.getTeam(team);
        boolean result = false;
        
        int contact = 0;
        
        for (Player currPlayer : playerList.getPlayers()) { 
            boolean leftFriend = allyOnTheLeft(currPlayer);
            boolean rightFriend = allyOnTheRight(currPlayer);
            
            //No neighbor on the left nor the right, so no antiplay
            if (!leftFriend || !rightFriend){
                return false;
            }
            
            if (inContactWithOpponent(currPlayer)) {
            	contact++;
            }
        }
        
        if (contact >= 3) {
            result = true;
        }
        
        return result;
    }
    
    private boolean allyOnTheLeft(Player p) {
    	Case playerPos = p.getPosition();
    	boolean allyOnTheLeft = false;
    	
    	for (Player currPlayer : p.getTeam().getPlayers()) {
    		Case currPlayerPos = currPlayer.getPosition();
    		
    		if (currPlayerPos.getY() <= 0) {
    			allyOnTheLeft = true;
    		} else if (currPlayerPos.getX() == playerPos.getX()) {
    			if (currPlayerPos.getY() == playerPos.getY() - 1) {
    				allyOnTheLeft = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX() - 1) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() - 1) {
    				allyOnTheLeft = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX() + 1) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() - 1) {
    				allyOnTheLeft = true;
    			}
    		}
    		
    		if (allyOnTheLeft) {
    			break;
    		}
    	}
    	
    	return allyOnTheLeft;
    }
    
    private boolean allyOnTheRight(Player p) {
    	Case playerPos = p.getPosition();
    	boolean allyOnTheRight = false;
    	
    	for (Player currPlayer : p.getTeam().getPlayers()) {
    		Case currPlayerPos = currPlayer.getPosition();
    		
    		if (currPlayerPos.getY() >= 6) {
    			allyOnTheRight = true;
    		} else if (currPlayerPos.getX() == playerPos.getX() - 1) {
    			if (currPlayerPos.getY() == playerPos.getY() + 1) {
    				allyOnTheRight = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX()) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() + 1) {
    				allyOnTheRight = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX() + 1) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() + 1) {
    				allyOnTheRight = true;
    			}
    		}
    		
    		if (allyOnTheRight) {
    			break;
    		}
    	}
    	
    	return allyOnTheRight;
    }
    
    private boolean inContactWithOpponent(Player p) {
    	Case playerPos = p.getPosition();
    	boolean contactWithOpponent = false;
    	
    	for (Player currPlayer : p.getTeam().getEnemyTeam().getPlayers()) {
    		Case currPlayerPos = currPlayer.getPosition();
    		
    		if (currPlayerPos.getX() == playerPos.getX() - 1) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() + 1) {
    				contactWithOpponent = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX() + 1) {
    			if (currPlayerPos.getY() == currPlayerPos.getY() + 1) {
    				contactWithOpponent = true;
    			}
    		}
    		
    		if (contactWithOpponent) {
    			break;
    		}
    	}
    	
    	return contactWithOpponent;
    }

    public boolean isAWin(TeamPosition team) {
        Team playerList = getTeam(team);
        int limit;
        
        if (team == TeamPosition.TOP) {
        	limit = 0;
        } else {
            if (team == TeamPosition.BOTTOM) {
                limit = 6;
            } else {
                return false;
            }
        }
        
        for (Player currPlayer : playerList.getPlayers()) {
            if ((currPlayer.getPosition().getX() == limit) && currPlayer.hasBall()) {
                return true;
            }
        }
        
        return false;
    }

	public String toString(){
		//board read
		StringBuilder game = new StringBuilder();
		
		for(int abscisse = 0; abscisse != ModelConstants.BOARD_SIZE; abscisse++) {
			for(int ordonnee = 0; ordonnee != ModelConstants.BOARD_SIZE; ordonnee++) {
				game.append("|");
				
				boolean hasPlayer = false;
				
				for (Player currTopPlayer : topTeam.getPlayers()) {
					Case position = currTopPlayer.getPosition();
					
					if (position.getX() == abscisse) {
						if (position.getY() == ordonnee) {
							hasPlayer = true;
							
							if (currTopPlayer.hasBall()) {
								game.append("a").append("num(?)").append("*");
							} else {
								game.append("a").append("num(?)").append(".");
							}
						}
					}
				}
				
				for (Player currBotPlayer : bottomTeam.getPlayers()) {
					Case position = currBotPlayer.getPosition();
					
					if (position.getX() == abscisse) {
						if (position.getY() == ordonnee) {
							hasPlayer = true;
							
							if (currBotPlayer.hasBall()) {
								game.append("b").append("num(?)").append("*");
							} else {
								game.append("b").append("num(?)").append(".");
							}
						}
					}
				}
				
				if (!hasPlayer) {
					game.append("___");
				}
			}
			
			game.append("|\n");
			
		}		
		
		return game.toString();
	}

	public MoveDirection getMoveDirection(Player player, Case pos){
        int playerI = player.getPosition().getX();
        int playerJ = player.getPosition().getY();
        int i = pos.getX();
        int j = pos.getY();
        
        MoveDirection result = null;
        
        if (playerJ == j){
            if (playerI == i - 1){
                result = MoveDirection.DOWN;
            }
            else{
                if (playerI == i + 1) {
                    result = MoveDirection.UP;
                }
            }
        }
        else{
            if (playerI == i){
                if (playerJ == j - 1){
                    result = MoveDirection.RIGHT;
                }
                else{
                    if (playerJ == j + 1) {
                        result = MoveDirection.LEFT;
                    }
                }
            }
        }
        
        return result;
    }

	private void resetTurnVariables() {
        this.nbMoves = 0;
        this.nbPasses = 0;
	}
	
	public TeamPosition getCurrentTeamTurn() {
		if (this.getTurn() % 2 == 0) {
			return TeamPosition.TOP;
		} else {
			return TeamPosition.BOTTOM;
		}
	}
	
	public int getTurn() {
		return this.currentTurn;
	}

	public ActionResult actionPerformed(Action action) { //what controller must use
        ActionResult done = ActionResult.DONE;
        TeamPosition currentTeam = getCurrentTeamTurn();

        switch(action.getType()) {
            case MOVE:
                Player player = action.getMovedPlayer();
                
                if (this.nbMoves == ModelConstants.MAX_MOVES_PER_TOUR || (player.getTeam().getPosition() != currentTeam)) {
                    done = ActionResult.ERROR;
                    break;
                }

                MoveDirection dir = action.getDirection();
                move(player, dir);
                this.nbMoves++;
                
                break;

            case PASS:
                Player firstPlayer = action.getPreviousPlayer();
                Player secondPlayer = action.getNextPlayer();
                
                if (this.nbPasses == ModelConstants.MAX_PASSES_PER_TOUR || (firstPlayer.getTeam().getPosition() != currentTeam) || (secondPlayer.getTeam().getPosition() != currentTeam)) {
                    done = ActionResult.ERROR;
                    break;
                }

                pass(firstPlayer, secondPlayer);
                this.nbPasses++;
                
                break;

            case END_TURN:
                if ((this.nbMoves + this.nbPasses) == 0) {
                    //You can not end your turn without performing at least 1 action
                	done = ActionResult.ERROR;
                    break;
                }
                
                this.resetTurnVariables();
                this.currentTurn++;
                
                break;

            default:
                throw new IllegalStateException("Please select a valid action type!");
        }
        
        if (this.nbPasses == ModelConstants.MAX_PASSES_PER_TOUR && this.nbMoves == ModelConstants.MAX_MOVES_PER_TOUR) {
            this.resetTurnVariables();
            this.currentTurn++;
        }
       
        if (this.isAWin(currentTeam)) {
        	done = ActionResult.WIN;
        }
        
        if (this.antiplay(currentTeam)) {
        	done = ActionResult.ANTIPLAY;
        }

        return done;
	}

	public int getNbPasses() {
		return this.nbPasses;
	}
	
	public int getNbMoves() {
		return this.nbMoves;
	}
}

