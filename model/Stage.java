package model;

public class Stage {
    public static final int empty = 0, ballSnowKid = 1, snowKid = 2, ballShadow = 3, shadow = 4;
    int[][] board = new int[7][7];

    public void resetBoard(){  //initialising board
        this.board[0] = new int[]{shadow, shadow, shadow, ballShadow, shadow, shadow, shadow};
        for (int i = 1; i < 6; i++){
            this.board[i] = new int[]{empty, empty, empty, empty, empty, empty, empty};
        }
        this.board[6] = new int[]{snowKid, snowKid, snowKid, ballSnowKid, snowKid, snowKid, snowKid};
    }

    public int whatsInTheBox(int i, int j){ //what's in i j
        return this.board[i][j];
    }

    public boolean isABallHere(int i, int j){ //is there a ball in i j
        return this.board[i][j] % 2 != 0;
    }

    public boolean isEmpty(int i, int j){ //is i j empty
        return this.board[i][j] == 0;
    }

    public void simpleMove(int i, int j, int nextI, int nextJ, int player){ //move player at i j to nextI nextJ, no matter what
        this.board[nextI][nextJ] = player;
        this.board[i][j] = empty;
    }

    public int whichTeam(int i, int j){
        int n = this.whatsInTheBox(i,j);
        if (n == ballSnowKid || n == snowKid){
            return snowKid;
        }
        if (n == ballShadow || n == shadow){
            return shadow;
        }
        return empty;
    }


    public void move(int i, int j, char move) { //this should do a proper move    move piece at i j, with move
        int player = whatsInTheBox(i, j);
        if (!this.isABallHere(i,j)) {
            switch (move) {
                case 'U':
                    if (i > 0 && this.isEmpty(i - 1, j))
                        simpleMove(i, j, i - 1, j, player);
                    break;
                case 'D':
                    if (i < 6 && this.isEmpty(i + 1, j))
                        simpleMove(i, j, i + 1, j, player);
                    break;
                case 'L':
                    if (j > 0 && this.isEmpty(i, j - 1))
                        simpleMove(i, j, i, j - 1, player);
                    break;
                case 'R':
                    if (j < 6 && this.isEmpty(i, j + 1))
                        simpleMove(i, j, i, j + 1, player);
                    break;
                default:
                    System.out.println("wrong move input in move function");
                    break;
            }
        }
    }

    public int direction(int i, int j, int nextI, int nextJ) {
        if (i == nextI) {//same line
            if (nextJ < j) {  //left
                return 1;//same line left
            } else {//right
                return 2;//same line right
            }
        } else {
            if (j == nextJ) {// same column
                if (nextI < i) {  //up
                    return 3;//same column up
                }
                else{//down
                    return 4;//same column down
                }
            }
            else{//diag check
                int diffI = i - nextI;
                int diffJ = j - nextJ;
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

    public void simplePass(int i, int j, int nextI, int nextJ){ //player at i j pass the ball to nextI nextJ
        this.board[i][j]++;
        this.board[nextI][nextJ]--;
    }


    public void pass(int i, int j, int nextI, int nextJ) { //player at i j pass the ball to nextI nextJ
        int team = this.whichTeam(i, j);
        if (team != empty && team == this.whichTeam(nextI, nextJ) && this.isABallHere(i, j)){
            boolean intercepted = false;
            int dir = this.direction(i, j, nextI, nextJ);
            int incI, incJ; //incrementation parameters
            switch (dir){
                case 1://same line on the left
                    incI = 0;
                    incJ = -1;
                    break;
                case 2://same line on the right
                    incI = 0;
                    incJ = 1;
                    break;
                case 3://same column up
                    incI = -1;
                    incJ = 0;
                    break;
                case 4://same column down
                    incI = 1;
                    incJ = 0;
                    break;
                case 5:// bottom right
                    incI = 1;
                    incJ = 1;
                    break;
                case 6://top left
                    incI = -1;
                    incJ = -1;
                    break;
                case 7://top right
                    incI = -1;
                    incJ = 1;
                    break;
                case 8://bottom left
                    incI = 1;
                    incJ = -1;
                    break;
                default:
                    incI = 0;
                    incJ = 0;
                    break;
            }


            int box;
            for(int startI = i, startJ = j; ((startI != nextI) && (startJ != nextJ)); startI += incI, startJ=+ incJ){
                box = this.whatsInTheBox(startI, startJ);
                if (box != empty && box != team) {
                    intercepted = true;
                    break;
                }
            }
            if (!intercepted) {
                simplePass(i, j, nextI, nextJ);
            }
        }
    }
}

