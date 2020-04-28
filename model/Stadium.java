package model;

import static java.lang.Math.abs;

public class Stadium {
    Player[][] board = new Player[7][7];
    Player[] snowKids = new Player[7];
    Player[] shadows = new Player[7];

    public Stadium(){
        this.initTeam(snowKids, "Snowkids");
        this.initTeam(shadows, "Shadows");
        this.resetBoard();
    }

    public void initTeam(Player[] team, String teamName){
        for (int i = 0; i<7; i++){
            team[i] = new Player(teamName);
        }
    }

    public Player[] getSnowKids(){
        return this.snowKids;
    }

    public Player[] getShadows(){
        return this.shadows;
    }

    public Player[] getOpponent(String team){
        if(team.equals("Snowkids")){
            return shadows;
        }
        else{
            if(team.equals("Shadows")){
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

    public void move(Player player, char move) { //this should do a proper move    move piece at i j, with move
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
                    break;
            }
        }
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
                int diffJ = playerOne.getJ() - playerTwo.getI();
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


    public void pass(Player playerOne, Player playerTwo) { //player at i j pass the ball to nextI nextJ
        if(playerOne.isATeammate(playerTwo) && (playerOne.getBallPossession()) && !(playerTwo.getBallPossession())){
            boolean intercepted = false;
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
    }
}

