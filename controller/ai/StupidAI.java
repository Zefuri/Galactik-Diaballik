//package ai;
//
//import model.Stadium;
//import model.enums.MoveDirection;
//import model.Player;
//
//public class StupidAI extends PlayerType {
//
//    public StupidAI(int number, Stadium stade, Position position) {
//        super(number, stade, position);
//    }
//
//    public void play() {
//        Player[] players = equip();
//        Player ballPlayer = null;
//
//        for (Player p : players) {
//            if (p.hasBall()) {
//                ballPlayer = p;
//                break;
//            }
//        }
//
//        // si le joueur qui a la balle peut faire une passe en avant, alors il la fait
//        for (Player p : players) {
//            if (isInFrontOf(p, ballPlayer) && canPass(ballPlayer, p)) {
//                stadium.pass(ballPlayer, p);
//                return;
//            }
//        }
//
//        // sinon faire avancer un joueur
//        for (Player p : players) {
//            if (canMoveForward(p)) {
//                stadium.move(p, position == Position.BOTTOM ? MoveDirection.UP : MoveDirection.DOWN);
//                return;
//            }
//        }
//    }
//        
//    private boolean isInFrontOf(Player p1, Player p2) {
//        if (position == Position.BOTTOM) {
//            return p1.getI() < p2.getI();
//        } else {
//            return p1.getI() > p2.getI();
//        }
//    }
//
//    private boolean canMoveForward(Player player) {
//        if (position == Position.BOTTOM) {
//            return canMove(player, -1, 0);
//        } else {
//            return canMove(player, +1, 0);
//        }
//    }
//
//    private boolean canMove(Player player, int di, int dj) {
//        if (player.getBallPossession()) {
//            return false;
//        }
//
//        int newI = player.getI() + di;
//        int newJ = player.getJ() + dj;
//
//        if (newI < 0 || newI > 6 || newJ < 0 || newJ > 6) {
//            return false;
//        }
//
//        for (Player p : getAllPlayers()) {
//            if (newI == p.getI() && newJ == p.getJ()) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    public boolean canPass(Player p1, Player p2) {
//        if (!(p1.getBallPossession() && p1.isATeammate(p2) && p1 != p2)) {
//            return false;
//        }
//
//        int di = p2.getI() - p1.getI();
//        int dj = p2.getJ() - p1.getJ();
//
//        // la pass est bien orthogonal ou diagonal
//        if (!(di == 0 || dj == 0 || Math.abs(di) == Math.abs(dj))) {
//            return false;
//        }
//
//        // il n'y a pas d'enemie entre les deux joueurs
//        for (Player e : getEnemyTeam(equipNum)) {
//            int dei = e.getI() - p1.getI();
//            int dej = e.getJ() - p1.getJ();
//
//            double dLength = vecLength(di, dj);
//            double deLength = vecLength(dei, dej);
//
//            // si deux vecteurs pointent dans la meme direction alors (a.b)/(a*b) = cos(0) = 1
//            if ((di*dei + dj*dej) / (dLength*deLength) == 1 && deLength < dLength) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private Player[] getEnemyTeam(int teamID) {
//        Player[] enemies = new Player[7];
//        int index = 0;
//
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 7; j++) {
//                Player p = stadium.whatsInTheBox(i, j);
//
//                if (p != null && p.getTeam() != teamID) {
//                    enemies[index++] = p;
//                }
//            }
//        }
//
//        return enemies;
//    }
//
//    private Player[] getAllPlayers() {
//        Player[] players = new Player[7*2];
//        int index = 0;
//
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 7; j++) {
//                Player p = stadium.whatsInTheBox(i, j);
//
//                if (p != null) {
//                    players[index++] = p;
//                }
//            }
//        }
//
//        return players;
//    }
//
//    private double vecLength(int i, int j) {
//        return Math.sqrt(i*i + j*j);
//    }
//}