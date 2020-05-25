package model;

import model.enums.ActionResult;
import model.enums.ActionType;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import java.util.HashMap;
import static java.lang.Math.abs;

public class Stadium {
    private Team topTeam;
    private Team bottomTeam;
	private String[][] board;    

	private Case clickedCase;
	private Case playerAloneCase;
	private Case playerWithBallCase;

    private boolean cheatModActivated;
    private Historic history;

    private boolean visualisationMode;
    
    public Stadium() {
        topTeam = new Team("snowKids", TeamPosition.TOP, this, true);
        bottomTeam = new Team("shadows", TeamPosition.BOTTOM, this, true);
        board = new String [ModelConstants.BOARD_SIZE] [ModelConstants.BOARD_SIZE];

        this.history = new Historic(this);
        this.history.newTurn(getCurrentTeamTurn(), cheatModActivated);
        this.visualisationMode = false;
    }
    
    public void initBoard() {
        for(int x = 0; x != board.length; x++) {
        	for(int y = 0; y != board[x].length; y++) {
        		board[x][y] = "";
        	}
        }
        
        for(Player p : topTeam.getPlayers()){
        	setBoard(p, "O");
        }
        
        for(Player p : bottomTeam.getPlayers()){
        	setBoard(p, "O");
        }
    }
    
    public void setBoard(Player player, String value) {
    	setBoard(player.getPosition().getX(), player.getPosition().getY(), value);
    }
    
    public void setBoard(int line, int colunm, String value) {
    	board[line][colunm] = value;
    }
    
    public void resetStadium() {
    	this.reset();

        this.cheatModActivated = false;
        this.history = new Historic(this);
        this.history.newTurn(getCurrentTeamTurn(), cheatModActivated);
    }
    
    public Team getTeam(TeamPosition position) {
        return position == TeamPosition.TOP ? topTeam : bottomTeam;
    }
    
    public void loadTopTeam(Team topTeam) {
    	this.topTeam = topTeam;
    }
    
    public void loadBotTeam(Team botTeam) {
    	this.bottomTeam = botTeam;
    }

    public void reset() {
        topTeam.initializePlayersOption();
        bottomTeam.initializePlayersOption();
    }
    
    public void replaceTeam() {
    	topTeam.replaceOption();
    	bottomTeam.replaceOption();
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
			return p.hasBall();
    	}
    	
    	return false;
    }
    
    public boolean hasAPlayerOnly(Case position) {
    	Player p ;
		
    	if ((p = getPlayer(position)) != null) {
			return !p.hasBall();
    	}
    	
    	return false;
    }

    public void move(Player player, MoveDirection direction) {
    	if (playerCanMove(player, direction) || cheatModActivated) {
    		simpleMove(player, direction);
    	}
    }
    
    public void simpleMoveAI(Player player, MoveDirection direction){
        	setBoard(player, "");
        	simpleMove(player, direction);
        	setBoard(player,"O");
    }

    public void simpleMove(Player player, MoveDirection direction){ //move player at position in the selected direction
    	// /!\ Caution: Please use the playerCanMove() function before using this one/!\
    	Case playerPos = player.getPosition();

		switch (direction) {
			case UP:
				player.getPosition().setX(playerPos.getX() - 1);
				break;
			case DOWN:
				player.getPosition().setX(playerPos.getX() + 1);
				break;
			case RIGHT:
				player.getPosition().setY(playerPos.getY() + 1);
				break;
			case LEFT:
				player.getPosition().setY(playerPos.getY() - 1);
				break;
			default:
				throw new IllegalStateException("Wrong input direction");
		}
    }


    public boolean booleanCanMove(MoveDirection direction, Player currPlayer, int i, int j) {
    	boolean canMove = true;
    	
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
				canMove = false;
				break;
		}
		
		return canMove;
	}
	
	public boolean playerCanMoveAI(Player player){
		return playerCanMoveAI(player, MoveDirection.UP) || playerCanMoveAI(player, MoveDirection.RIGHT) || playerCanMoveAI(player, MoveDirection.DOWN) || playerCanMoveAI(player, MoveDirection.LEFT);
	}
	
	    public boolean playerCanMoveAI(Player player, MoveDirection direction) {
    	boolean canMove = true;
    	
		switch (direction) {
			case UP:
				if (player.getPosition().getX() <= 0 || board[player.getPosition().getX()-1][player.getPosition().getY()].equals("O")) {
					canMove = false;
				}
				break;
				
			case DOWN:
				if (player.getPosition().getX() >= ModelConstants.BOARD_SIZE-1 || board[player.getPosition().getX()+1][player.getPosition().getY()].equals("O")) {
					canMove = false;
				}

				break;

			case LEFT:
				if (player.getPosition().getY() <= 0 || board[player.getPosition().getX()][player.getPosition().getY()-1].equals("O")) {
					canMove = false;
				}

				break;

			case RIGHT:
				if (player.getPosition().getY() >= 6 || board[player.getPosition().getX()][player.getPosition().getY()+1].equals("O")) {
					canMove = false;
				}
				break;

			default:
				System.out.println("wrong move input in move function");
				canMove = false;
				break;
		}
		
		return canMove;
	}

	public boolean playerCanMove(Player player){
		return playerCanMove(player, MoveDirection.UP) || playerCanMove(player, MoveDirection.RIGHT) || playerCanMove(player, MoveDirection.DOWN) || playerCanMove(player, MoveDirection.LEFT);
	}

    public boolean playerCanMove(Player player, MoveDirection direction) {
    	boolean canMove = true;
        int i = player.getPosition().getX();
        int j = player.getPosition().getY();
        
        if (player.hasBall()) {
			canMove = false;
		} 
		
		if (canMove) {
        	for (Player currPlayer : player.getTeam().getPlayers()) {
        		//For each player of the ally team, we check if he is not badly positioned
	            canMove = booleanCanMove(direction, currPlayer, i, j);
	            
				if (!canMove) {
					break;
				}
			}
		}
        	
		if (canMove) {
        	for (Player currPlayer : player.getTeam().getEnemyTeam().getPlayers()) {
        		//For each player of the enemy team, we check if he is not badly positioned
        		canMove = booleanCanMove(direction, currPlayer, i, j);
        		
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

    public void simplePass(Player playerOne, Player playerTwo){ //player at i j pass the ball to nextI nextJ
    	playerOne.getTeam().setBallPlayer(playerTwo);
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
        if (playerCanPass(playerOne, playerTwo) || (cheatModActivated && playerOne.isATeammate(playerTwo))) {
        	simplePass(playerOne, playerTwo);
        }
    }

    public boolean antiplay(Team team) {
        boolean result = false;
        
        int contact = 0;
		if (!allyMakeAStraightLine(team)){
			return false;
		}

        for (Player currPlayer : team.getPlayers()) {
            if (inContactWithOpponent(currPlayer)) {
            	contact++;
            }
        }
        
        if (contact >= 3) {
            result = true;
        }
        
        return result;
    }

    private boolean allyMakeAStraightLine(Team team) {
		boolean result = true;
		HashMap<Integer, Integer> index = new HashMap<>();
		for (Player currPlayer : team.getPlayers()) {
			Case position = currPlayer.getPosition();
			index.putIfAbsent(position.getY(), position.getX());
		}
		if (index.size() != 7) {
			result = false;
		}else{
			for (int i = 1; i < 6; i++)  {
				int before = index.get(i - 1);
				int after = index.get( i + 1);
				int now = index.get(i);
					if ((!(before + 1 == now || before == now || before - 1 == now)) ||
							!(after + 1 == now || after == now || after - 1 == now)){
						result = false;
						break;
				}
			}
		}
		return result;
	}
    
    private boolean inContactWithOpponent(Player p) {
    	Case playerPos = p.getPosition();
    	boolean contactWithOpponent = false;
    	
    	for (Player currPlayer : p.getTeam().getEnemyTeam().getPlayers()) {
    		Case currPlayerPos = currPlayer.getPosition();
    		
    		if (currPlayerPos.getX() == playerPos.getX() - 1) {
    			if (currPlayerPos.getY() == playerPos.getY()) {
    				contactWithOpponent = true;
    			}
    		} else if (currPlayerPos.getX() == playerPos.getX() + 1) {
    			if (currPlayerPos.getY() == playerPos.getY()) {
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
        	limit = 6;
        } else {
            if (team == TeamPosition.BOTTOM) {
                limit = 0;
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

				hasPlayer = isHasPlayer(game, abscisse, ordonnee, hasPlayer, topTeam);

				hasPlayer = isHasPlayer(game, abscisse, ordonnee, hasPlayer, bottomTeam);

				if (!hasPlayer) {
					game.append("______");
				}
			}
			
			game.append("|\n");
			
		}		
		
		return game.toString();
	}

	private boolean isHasPlayer(StringBuilder game, int abscissa, int ordinate, boolean hasPlayer, Team bottomTeam) { //to string method
		for (Player currBotPlayer : bottomTeam.getPlayers()) {
			Case position = currBotPlayer.getPosition();

			if (position.getX() == abscissa) {
				if (position.getY() == ordinate) {
					hasPlayer = true;

					if (currBotPlayer.hasBall()) {
						//game.append("b").append("num(?)").append("*");
						game.append(currBotPlayer.getName()).append("*");
					} else {
						//game.append("b").append("num(?)").append(".");
						game.append(currBotPlayer.getName()).append(".");
					}
				}
			}
		}
		return hasPlayer;
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
	
	public ActionResult endTurn() {
		return actionPerformed(new Action(ActionType.END_TURN, null, null, null, null));
	}
	
	public Team getCurrentTeamTurn() {
		if (this.getTurnIndex() % 2 == 0) {
			return topTeam;
		} else {
			return bottomTeam;
		}
	}
	
	public int getTurnIndex() {
		return this.history.getCurrentTurnIndex();
	}

	public ActionResult actionPerformed(Action action) { //what controller must use
        ActionResult done = ActionResult.DONE;
        Turn currentTurn = this.history.getLast();

		switch (action.getType()) {
			case MOVE:
				Player player = action.getMovedPlayer();
				MoveDirection dir = action.getDirection();
				
				if (currentTurn.getNbMoveDone() >= ModelConstants.MAX_MOVES_PER_TOUR
						|| ((player.getTeam().getPosition() != currentTurn.getTeam().getPosition()) && !cheatModActivated)
						|| !playerCanMove(player, dir)) {
					done = ActionResult.ERROR;
				} else {
					move(player, dir);
					currentTurn.addAction(action);
					unselectPlayerIfNeeded(player);
				}
				
				break;
				
			case PASS:
				Player firstPlayer = action.getPreviousPlayer();
				Player secondPlayer = action.getNextPlayer();
				
				if (currentTurn.getNbPassDone() == ModelConstants.MAX_PASSES_PER_TOUR
						|| ((firstPlayer.getTeam().getPosition() != currentTurn.getTeam().getPosition())  && !cheatModActivated)
						|| ((secondPlayer.getTeam().getPosition() != currentTurn.getTeam().getPosition())  && !cheatModActivated)
						|| !playerCanPass(firstPlayer, secondPlayer)) {
					done = ActionResult.ERROR;
				} else {
					pass(firstPlayer, secondPlayer);
					currentTurn.addAction(action);
				}
				
				break;
				
			case END_TURN:
				if ((currentTurn.getNbMoveDone() + currentTurn.getNbPassDone() == 0) && !cheatModActivated) {
					//You can not end your turn without performing at least 1 action
					done = ActionResult.ERROR;
				} else {
					this.history.nextTurn();
					this.history.newTurn(getCurrentTeamTurn(), cheatModActivated);
				}
				
				break;
				
			default:
				throw new IllegalStateException("Please select a valid action type!");
		}
        /*
         * Certainly useless now
         * 
        if (this.nbPasses == ModelConstants.MAX_PASSES_PER_TOUR && this.nbMoves == ModelConstants.MAX_MOVES_PER_TOUR) {
            this.resetTurnVariables();
            this.currentTurnIndex++;
        }
       	*/
        if (this.isAWin(currentTurn.getTeam().getPosition())) {
        	done = ActionResult.WIN;
        }
        
        if (this.antiplay(this.getCurrentTeamTurn())) {
        	done = ActionResult.ANTIPLAY_CURRENT;
        } else if(this.antiplay(this.getNotPlayingTeam())){
        	done = ActionResult.ANTIPLAY;
        }
        
        return done;
	}
	
	public ActionResult actionPerformedAI(Action action) { //what controller must use
        ActionResult done = ActionResult.DONE;
        Turn currentTurn = this.history.getLast();

		switch (action.getType()) {
			case MOVE:
				Player player = action.getMovedPlayer();
				MoveDirection dir = action.getDirection();
					simpleMove(player, dir);
					currentTurn.addAction(action);
					unselectPlayerIfNeeded(player);
				
				break;
				
			case PASS:
				Player firstPlayer = action.getPreviousPlayer();
				Player secondPlayer = action.getNextPlayer();
					simplePass(firstPlayer, secondPlayer);
					currentTurn.addAction(action);
				
				break;
				
			case END_TURN:
					this.history.nextTurn();
					this.history.newTurn(getCurrentTeamTurn(), cheatModActivated);
				
				break;
				
			default:
				throw new IllegalStateException("Please select a valid action type!");
		}
		
        if (this.isAWin(currentTurn.getTeam().getPosition())) {
        	done = ActionResult.WIN;
        }
        
        if (this.antiplay(this.getCurrentTeamTurn())) {
        	done = ActionResult.ANTIPLAY_CURRENT;
        } else if(this.antiplay(this.getNotPlayingTeam())){
        	done = ActionResult.ANTIPLAY;
        }

        return done;
	}
	
	public Team getNotPlayingTeam() {
		Team res = null;

		switch (getCurrentTeamTurn().getPosition()) {
			case TOP:
				res = getTeam(TeamPosition.BOTTOM);
				break;
			case BOTTOM:
				res = getTeam(TeamPosition.TOP);
				break;
		}

		return res;
	}
	
	private void unselectPlayerIfNeeded(Player p) {
		if (getNbPassesDone() == ModelConstants.MAX_MOVES_PER_TOUR) {
			p.setIfSelected(false);
		}
	}

	public int getNbPassesDone() {
		return this.history.getLast().getNbPassDone();
	}
	
	public int getNbMovesDone() {
		return this.history.getLast().getNbMoveDone();
	}
	
	public ActionResult undoAction() {
		return this.history.undoLastAction();
	}
	
	public boolean redoAction() {
		return this.history.redoNextAction();
	}
	
	public ActionResult resetTurn() {
		return this.history.resetCurrentTurn();
	}

	public boolean isCheatModActivated() {
		return cheatModActivated;
	}

	public void switchCheatModActivated() {
		this.cheatModActivated = !cheatModActivated;
		this.history.getLast().switchCheatModActivated();
	}

	public ActionResult performRequestedAction() {
		//Verify if the user click must perform a doable action
		ActionResult result = null;

		if (hasABall(clickedCase)) {
			//There is a player with a ball on the clicked case
			if (playerWithBallCase != null) {
				throw new IllegalStateException("You can not pass the ball to yourself or an enemy player.");
			} else {
				//Change the actual player if possible
				Player clickedPlayer = getPlayer(clickedCase);

				if (clickedPlayer.canBeSelectedForPass()) {
					if (playerAloneCase != null) {
						getPlayer(playerAloneCase).setIfSelected(false);
					}

					setPlayerWithBallCase(clickedCase);
					clickedPlayer.setIfSelected(true);
				} else {
					throw new IllegalStateException("The chosen player is not in the current playing team, or you have already made a pass this turn.");
				}
			}
		} else if (hasAPlayerOnly(clickedCase)) {
			//There is a player only on the clicked case
			if (playerWithBallCase != null) {
				//Do a pass if possible
				Player previousOwner = getPlayer(playerWithBallCase);
				Player futureOwner = getPlayer(clickedCase);
				result = previousOwner.pass(futureOwner);

				//TODO remettre le if / else if en 1 bloc apr�s impl�mentation de l'�cran de fin

				if (result == ActionResult.DONE) {
					getPlayer(playerWithBallCase).setIfSelected(false);
					clearPlayers();
				} else if (result == ActionResult.WIN) {
					getPlayer(playerWithBallCase).setIfSelected(false);
				} else {
					throw new IllegalStateException("Either it is not your turn, or the two players are not aligned or have an opponent between them.");
				}
			} else {
				//Change the actual player and change the selection value
				Player clickedPlayer = getPlayer(clickedCase);

				if (clickedPlayer.canBeSelected()) {
					if (playerAloneCase != null) {
						getPlayer(playerAloneCase).setIfSelected(false);
					}

					setPlayerAloneCase(clickedCase);
					clickedPlayer.setIfSelected(true);
				} else {
					throw new IllegalStateException("The chosen player is not in the current playing team, or you have already made two moves this turn.");
				}
			}
		} else {
			//The case is empty
			if (playerAloneCase != null) {
				//If the selected case is next to the player case, we move the player
				Player player = getPlayer(playerAloneCase);
				MoveDirection direction = getMoveDirection(player, clickedCase);

				if (direction == null) {
					throw new IllegalStateException("Either it is not your turn, or the selected case is not situated next to the player.");
				}

				result = player.move(direction);

				if (result == ActionResult.DONE) {
					setPlayerAloneCase(clickedCase);
				} else if (result == ActionResult.ANTIPLAY) {
					throw new RuntimeException("Antiplay detected!");
				} else {
					throw new IllegalStateException("Either it is not your turn, or the selected case is not situated next to the player.");
				}
			} else if (playerWithBallCase != null) {
				clearSelectedPlayer();
				throw new IllegalStateException("You can not move a player that has the ball.");
			} else {
				throw new IllegalStateException("You must select a player before selecting an empty case!");
			}
		}

		return result;
	}

	public Case getClickedCase() {
		return clickedCase;
	}

	public void setClickedCase(Case clickedCase) {
		this.clickedCase = clickedCase;
	}

	public Case getPlayerAloneCase() {
		return playerAloneCase;
	}

	public Case getPlayerWithBallCase(){
    	return playerWithBallCase;
	}

	public void setPlayerWithBallCase(Case c) {
    	if(c != null) {
			this.playerWithBallCase = new Case(c);
		}
    	else{
    		this.playerWithBallCase = null;
		}
		this.playerAloneCase = null;
	}

	private void clearPlayers() {
		this.playerWithBallCase = null;
		this.playerAloneCase = null;
	}

	public void setPlayerAloneCase(Case c) {
		this.playerWithBallCase = null;
		if(c != null) {
			this.playerAloneCase = new Case(c);
		}
		else{
			this.playerAloneCase = null;
		}
	}

	public void clearSelectedPlayer() {
		if (this.playerWithBallCase != null) {
			getPlayer(this.playerWithBallCase).setIfSelected(false);
		} else if (this.playerAloneCase != null) {
			getPlayer(this.playerAloneCase).setIfSelected(false);
		}

		clearPlayers();

		//holoTV.getArkadiaNews().repaint();
	}

	
	public Historic getHistory() {
		return this.history;
	}
	
	public boolean isInVisualisationMode() {
		return this.visualisationMode;
	}
	
	public void setVisualisaionMode(boolean visu) {
		this.visualisationMode = visu;
	}
}

