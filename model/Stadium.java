package model;

import static java.lang.Math.abs;

public class Stadium {
    public static final int teamOne = 0;
    public static final int teamTwo = 1;
    Player[][] board = new Player[7][7];
    Player[] snowKids = new Player[7];
    Player[] shadows = new Player[7];
    int turn;
    int nbPass;
    int nbMove;

    public Stadium(){
        this.initTeam(snowKids, teamOne);
        this.initTeam(shadows, teamTwo);
        this.resetBoard();
        this.turn = 0;
        this.nbMove = 0;
        this.nbPass = 0;
    }

    public void initTeam(Player[] team,int nbTeam){
        for (int i = 0; i<7; i++){
            team[i] = new Player(nbTeam);
        }
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

    public void resetBoard(){  //initialising board
        for(int i = 0; i < 7; i++){
            board[0][i] = snowKids[i];
            snowKids[i].movePlayer(0,i);
        }
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = null;
            }
        }
        for(int i = 0; i < 7; i++){
            board[6][i] = shadows[i];
            shadows[i].movePlayer(6,i);
        }
        board[0][3].setBallPossession(true);
        board[6][3].setBallPossession(true);
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

    public void simpleMove(Player player, int nextI, int nextJ){ //move player at i j to nextI nextJ, no matter what
        this.board[player.getI()][player.getJ()] = null;
        player.movePlayer(nextI, nextJ);
        this.board[nextI][nextJ] = player;

    }

    public boolean move(Player player, char move) { //this should do a proper move    move piece at i j, with move
        int i = player.getI();
        int j = player.getJ();
        if (!this.isABallHere(i, j)) {
            switch (move) {
                case 'U':
                    if (i > 0 && this.isEmpty(i - 1, j))
                        simpleMove(player, i - 1, j);
                    break;
                case 'D':
                    if (i < 6 && this.isEmpty(i + 1, j))
                        simpleMove(player, i + 1, j);
                    break;
                case 'L':
                    if (j > 0 && this.isEmpty(i, j - 1))
                        simpleMove(player, i, j - 1);
                    break;
                case 'R':
                    if (j < 6 && this.isEmpty(i, j + 1))
                        simpleMove(player, i, j + 1);
                    break;
                default:
                    System.out.println("wrong move input in move function");
                    return false;
            }
            return true;
        }
        return false;
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
        if(playerOne.isATeammate(playerTwo) && (playerOne.getBallPossession()) && !(playerTwo.getBallPossession())){
            int dir = direction(playerOne, playerTwo);
            if(dir != 0) {
                Player[] opponent = this.getOpponent(playerOne.getTeam());
                for(int i = 0;(!intercepted) && i < 7; i++) {
                    Player checking = opponent[i];
                    if (this.direction(playerOne, checking) == dir){
                         int distFriend = abs(playerTwo.getI() - playerOne.getI()) + abs(playerTwo.getJ() - playerOne.getJ());
                         int distOpp = abs(checking.getI() - playerOne.getI()) + abs(checking.getJ() - playerOne.getJ());
                         if (distFriend < distOpp){
                             intercepted = true;
                         }
                    }
                }
                if(!intercepted){
                    simplePass(playerOne, playerTwo);
                }
            }
        }
        return !intercepted;
    }

    public boolean antiplay(int team){
        Player[] playerlist;
        boolean result = true;
        if (team == teamOne){
            playerlist = snowKids;
        }
        else{
            if(team == teamTwo){
                playerlist = shadows;
            }
            else{
                return false;
            }
        }
        for(int i = 0; i < 7; i++){
            Player checking = playerlist[i];
            int i = checking.getI();
            int j = checking.getJ();
            if (j > 0){
                if (i > 0){

                }               //TODO

                if (checking.getI() < 6){

                }
            }
        }
    }

    public void isAWin(){
        //TODO
    }

    public void normalTurn(){
        //TODO
    }
}

