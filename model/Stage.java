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
            }
        }
    }

    public void simplePass(int i, int j, int nextI, int nextJ){ //player at i j pass the ball to nextI nextJ
        this.board[i][j]++;
        this.board[nextI][nextJ]--;
    }

    public void pass(int i, int j, int nextI, int nextJ){ //player at i j pass the ball to nextI nextJ,

    }
}
