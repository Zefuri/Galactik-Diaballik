package model;

import static java.lang.Math.abs;
import model.ModelConstants;

public class Stadium {
    Player[][] board = new Player[7][7];
    Player[] snowKids = new Player[7];
    Player[] shadows = new Player[7];
    private int turn;
    private int nbPass;
    private int nbMove;

    public Stadium(){
        this.initTeam(snowKids, ModelConstants.TEAM_ONE);
        this.initTeam(shadows, ModelConstants.TEAM_TWO);
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
        return (!this.isEmpty(i, j) && this.whatsInTheBox(i, j).getBallPossession());
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

    public void initTeam(Player[] team,int nbTeam){
        for (int i = 0; i < ModelConstants.BOARD_SIZE; i++){
            team[i] = new Player(i, nbTeam);
        }
    }

    public void resetBoard(){  //initialising board
        for(int i = 0; i < ModelConstants.BOARD_SIZE; i++){
            board[6][i] = snowKids[i];
            snowKids[i].movePlayer(6,i);
            board[0][i] = shadows[i];
            shadows[i].movePlayer(0,i);
        }
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
                board[i][j] = null;
            }
        }
        board[0][3].setBallPossession(true);
        board[6][3].setBallPossession(true);
    }

    public void simpleMove(Player player, int nextI, int nextJ){ //move player at i j to nextI nextJ, no matter what
        this.board[player.getI()][player.getJ()] = null;
        player.movePlayer(nextI, nextJ);
        this.board[nextI][nextJ] = player;

    }

    public boolean move(Player player, char move) { //this should do a proper move    move piece at i j, with move
    	boolean moved = false;
        int i = player.getI();
        int j = player.getJ();
        
        if (!this.isABallHere(i, j)) {
            switch (move) {
                case ModelConstants.UP:
                    if (i > 0 && this.isEmpty(i - 1, j)) {
                        simpleMove(player, i - 1, j);
                        moved = true;
                    }
                    break;
                    
                case ModelConstants.DOWN:
                    if (i < 6 && this.isEmpty(i + 1, j)) {
                        simpleMove(player, i + 1, j);
                        moved = true;
                    }
                    break;
                    
                case ModelConstants.LEFT:
                    if (j > 0 && this.isEmpty(i, j - 1)) {
                        simpleMove(player, i, j - 1);
                        moved = true;
                    }
                    break;
                    
                case ModelConstants.RIGHT:
                    if (j < 6 && this.isEmpty(i, j + 1)) {
                        simpleMove(player, i, j + 1);
                        moved = true;
                    }
                    break;
                    
                default:
                    System.out.println("wrong move input in move function");
            }
        }
        
        return moved;
    }

    public int direction(Player playerOne, Player playerTwo) {
        if (playerOne.getI() == playerTwo.getI()) {//same line
            if (playerTwo.getJ() < playerOne.getJ()) {  //left
                return ModelConstants.DIR_LEFT;//same line left
            } else {//right
                return ModelConstants.DIR_RIGHT;//same line right
            }
        } else {
            if (playerOne.getJ() == playerTwo.getJ()) {// same column
                if (playerTwo.getI() < playerOne.getI()) {  //up
                    return ModelConstants.DIR_UP;//same column up
                }
                else{//down
                    return ModelConstants.DIR_DOWN;//same column down
                }
            }
            else{//diag check
                int diffI = playerOne.getI() - playerTwo.getI();
                int diffJ = playerOne.getJ() - playerTwo.getJ();
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

    public void simplePass(Player playerOne, Player playerTwo){ //player at i j pass the ball to nextI nextJ
        playerOne.setBallPossession(false);
        playerTwo.setBallPossession(true);
    }


    public boolean pass(Player playerOne, Player playerTwo) { //player at i j pass the ball to nextI nextJ
        boolean intercepted = false;
        
        if (playerOne.isATeammate(playerTwo) && (playerOne.getBallPossession()) && !(playerTwo.getBallPossession())){
            int dir = direction(playerOne, playerTwo);
            
            if (dir != 0) {
                Player[] opponent = this.getOpponent(playerOne.getTeam());
               
                for (int i = 0;(!intercepted) && i < ModelConstants.BOARD_SIZE; i++) {
                    Player checking = opponent[i];
                    
                    if (this.direction(playerOne, checking) == dir){
                         int distFriend = abs(playerTwo.getI() - playerOne.getI()) + abs(playerTwo.getJ() - playerOne.getJ());
                         int distOpp = abs(checking.getI() - playerOne.getI()) + abs(checking.getJ() - playerOne.getJ());
                         
                         if (distFriend > distOpp){
                             intercepted = true;
                         }
                    }
                }
                
                if (!intercepted) {
                    simplePass(playerOne, playerTwo);
                }
            } else {
            	intercepted = true;
            }
        } else{
        	intercepted = true;
        }
        
        return !intercepted;
    }

    public boolean antiplay(int team){
        Player[] playerlist;
        boolean result = false;
        if (team == ModelConstants.TEAM_ONE){
            playerlist = this.getSnowKids();
        }
        else{
            if(team == ModelConstants.TEAM_TWO){
                playerlist = this.getShadows();
            }
            else{
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
            int i = checking.getI();
            int j = checking.getJ();
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
        if (contact >= 3){
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
        for (int k = 0; k < 6; k++){
            checking = playerlist[k];
            if (( checking.getI() == limit ) && checking.getBallPossession()){
                return true;
            }
        }
        return false;
    }

	public String toString(){
		//board read
		StringBuilder game = new StringBuilder();
		
		for(int abscisse = 0; abscisse != board.length; abscisse++){
			for(int ordonnee = 0; ordonnee != board.length; ordonnee++){
				game.append("|");
				
				if(board[abscisse][ordonnee] == null){
					game.append("___");
					
				}else if(board[abscisse][ordonnee].getTeam() == 0   &&   !board[abscisse][ordonnee].getBallPossession()){
					game.append("a").append(board[abscisse][ordonnee].getNum()).append(".");
					
				}else if(board[abscisse][ordonnee].getTeam() == 1   &&   !board[abscisse][ordonnee].getBallPossession()){
					game.append("b").append(board[abscisse][ordonnee].getNum()).append(".");
					
				}else if(board[abscisse][ordonnee].getTeam()==0 && board[abscisse][ordonnee].getBallPossession()){
					game.append("a").append(board[abscisse][ordonnee].getNum()).append("*");
					
				}else{
					game.append("b").append(board[abscisse][ordonnee].getNum()).append("*");
				}
				
			}
			
			game.append("|\n");
			
		}		
		
		return game.toString();
	}

	public char getMoveDirection(Player player, int i, int j){
        int playerI = player.getI();
        int playerJ = player.getJ();
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

	private void resetTurnVariables(){
        this.nbMove = 0;
        this.nbPass = 0;
    }

	public int normalTurn(Action action){ //what controller must use
        int result = 0;
        
        switch(action.getActionType()){
            case 0:
                if (this.nbMove == ModelConstants.MAX_MOVES_PER_TOUR){
                    result = -1;
                    break;
                }
                
                Player player = whatsInTheBox(action.getFirstI(), action.getFirstJ());
                char dir = getMoveDirection(player, action.getSecondI(), action.getSecondJ());
                move(player, dir);
                this.nbMove++;
                
                break;
                
            case 1:
                if (this.nbPass == ModelConstants.MAX_PASSES_PER_TOUR){
                    result = -1;
                    break;
                }
                
                Player firstPlayer = whatsInTheBox(action.getFirstI(), action.getFirstJ());
                Player secondPlayer = whatsInTheBox(action.getSecondI(), action.getSecondJ());
                pass(firstPlayer, secondPlayer);
                this.nbPass++;
                
                break;
                
            case 2:
                if ((this.nbMove + this.nbPass) == 0){
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
        
        if (this.nbPass == ModelConstants.MAX_PASSES_PER_TOUR && this.nbMove == ModelConstants.MAX_MOVES_PER_TOUR){
            this.resetTurnVariables();
            this.turn++;
        }
        
        if (this.isAWin(action.getWhosturn())) {
            result = ModelConstants.WIN;
        }
        
        if (this.antiplay(action.getWhosturn())) {
            result = ModelConstants.ANTIPLAY;
        }

        return result;
    }
}

