package model;

import static java.lang.Math.abs;

public class Stadium {
    public static final int teamOne = 0;
    public static final int teamTwo = 1;
    Player[][] board = new Player[7][7];
    Player[] snowKids = new Player[7];
    Player[] shadows = new Player[7];
    private int turn;
    private int nbPass;
    private int nbMove;

    public Stadium(){
        this.initTeam(snowKids, teamOne);
        this.initTeam(shadows, teamTwo);
        this.resetBoard();
        this.turn = 0;
        this.nbMove = 0;
        this.nbPass = 0;
    }
    //----------------------------------------------------getters-------------------------------------------------------

    public int getNbPass() {
        return nbPass;
    }

    public int getNbMove() {
        return nbMove;
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
        return this.whatsInTheBox(i, j).getBallPossession();
    }

    public boolean isEmpty(int i, int j){ //is i j empty
        return this.whatsInTheBox(i, j) == null;
    }

    public Player[] getOpponent(int team){
        if(team == teamOne){
            return shadows;
        }
        else{
            if(team == teamTwo){
                return snowKids;
            }
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------

    public void initTeam(Player[] team,int nbTeam){
        for (int i = 0; i<7; i++){
            team[i] = new Player(i, nbTeam);
        }
    }

    public void resetBoard(){  //initialising board
        for(int i = 0; i < 7; i++){
            board[6][i] = snowKids[i];
            snowKids[i].movePlayer(6,i);
            board[0][i] = shadows[i];
            shadows[i].movePlayer(0,i);
        }
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
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
                case 'U':
                    if (i > 0 && this.isEmpty(i - 1, j)) {
                        simpleMove(player, i - 1, j);
                        moved = true;
                    }
                    break;
                case 'D':
                    if (i < 6 && this.isEmpty(i + 1, j)) {
                        simpleMove(player, i + 1, j);
                        moved = true;
                    }
                    break;
                case 'L':
                    if (j > 0 && this.isEmpty(i, j - 1)) {
                        simpleMove(player, i, j - 1);
                        moved = true;
                    }
                    break;
                case 'R':
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
                return 1;//same line left
            } else {//right
                return 2;//same line right
            }
        } else {
            if (playerOne.getJ() == playerTwo.getJ()) {// same column
                if (playerTwo.getI() < playerOne.getI()) {  //up
                    return 3;//same column up
                }
                else{//down
                    return 4;//same column down
                }
            }
            else{//diag check
                int diffI = playerOne.getI() - playerTwo.getI();
                int diffJ = playerOne.getJ() - playerTwo.getJ();
                if (diffJ == diffI){
                    if (diffJ > 0){//bottom right
                        return 5;
                    }
                    else{//top left
                        return 6;
                    }
                }
                else{
                    if(diffJ == -diffI){
                        if (diffJ > 0){//top right
                            return 7;
                        }
                        else{//bottom left
                            return 8;
                        }
                    }
                }
            }
        }
        return 0;
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
                for (int i = 0;(!intercepted) && i < 7; i++) {
                    Player checking = opponent[i];
                    if (this.direction(playerOne, checking) == dir){
                         int distFriend = abs(playerTwo.getI() - playerOne.getI()) + abs(playerTwo.getJ() - playerOne.getJ());
                         int distOpp = abs(checking.getI() - playerOne.getI()) + abs(checking.getJ() - playerOne.getJ());
                         if (distFriend > distOpp){
                             intercepted = true;
                         }
                    }
                }
                if(!intercepted){
                    simplePass(playerOne, playerTwo);
                }
            }else{intercepted = true;}
        }else{intercepted = true;}
        return !intercepted;
    }

    public boolean antiplay(int team){
        Player[] playerlist;
        boolean result = false;
        if (team == teamOne){
            playerlist = this.getSnowKids();
        }
        else{
            if(team == teamTwo){
                playerlist = this.getShadows();
            }
            else{
                return false;
            }
        }
        int contact = 0;
        boolean leftFriend;
        boolean rightFriend;
        for (int k = 0; k < 7; k++) {
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
        if (team == teamOne) {
            playerlist = this.getSnowKids();
            limit = 0;
        } else {
            if (team == teamTwo) {
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

	private char getMoveDirection(Player player, int i, int j){
        int playerI = player.getI();
        int playerJ = player.getJ();
        char result = 'X';
        if (playerJ == j){
            if (playerI == i - 1){
                result = 'U';
            }
            else{
                if (playerI == i + 1) {
                    result = 'D';
                }
            }
        }
        else{
            if (playerI == i){
                if (playerJ == j - 1){
                    result = 'L';
                }
                else{
                    if (playerJ == j + 1) {
                        result = 'R';
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
        switch(action.getActionType()){
            case 0:
                if ( this.nbMove == 2 ){
                    return -1;
                }
                Player player = whatsInTheBox(action.getFirstI(),action.getFirstJ());
                char dir = getMoveDirection(player, action.getSecondI(), action.getSecondJ());
                move(player, dir);
                this.nbMove++;
                break;
            case 1:
                if ( this.nbPass == 1 ){
                    return -1;
                }
                Player firstPlayer = whatsInTheBox(action.getFirstI(),action.getFirstJ());
                Player secondPlayer = whatsInTheBox(action.getSecondI(),action.getSecondJ());
                pass(firstPlayer, secondPlayer);
                this.nbPass++;
                break;
            case 2:
                if ((this.nbMove + this.nbPass ) == 0){
                    return -1;
                }
                this.resetTurnVariables();
                this.turn++;
                break;
            default:
                return - 1;
        }
        if (this.nbPass == 1 && this.nbMove == 2){
            this.resetTurnVariables();
            this.turn++;
        }
        return this.whosTurn();
    }
}

