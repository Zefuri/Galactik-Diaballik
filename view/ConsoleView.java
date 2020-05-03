package view;

import java.util.Scanner;

import model.Player;
import model.Stadium;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.Case;
import model.ModelConstants;

public class ConsoleView {
    private Stadium stadium;

    public ConsoleView(Stadium stadium) {
        this.stadium = stadium;
    }

    public void display() {
        for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
                Player player = stadium.getPlayer(new Case(i, j));

                if (player == null) {
                        System.out.print(".");
                } else if (player.getTeam().getPosition() == TeamPosition.TOP) {
                        System.out.print(player.hasBall() ? "N" : "n");
                } else if (player.getTeam().getPosition() == TeamPosition.BOTTOM) {
                        System.out.print(player.hasBall() ? "H" : "h");
                }
            }

            System.out.println();
        }
    }

    public static void main(String[] args) {
        Stadium stadium = new Stadium();
        ConsoleView consoleView = new ConsoleView(stadium);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Utilisation :");
        System.out.println();
        System.out.println("deplacement = d [position] [direction]");
        System.out.println("passe = p [depuis] [vers]");
        System.out.println();
        System.out.println("position, depuis, vers = coordonnées d'échequier");
        System.out.println("a7 b7 c7 d7 e7 f7 g7");
        System.out.println("a6 b6 c6 d6 e6 f6 g6");
        System.out.println("a5 b5 c5 d5 e5 f5 g5");
        System.out.println("a4 b4 c4 d4 e4 f4 g4");
        System.out.println("a3 b3 c3 d3 e3 f3 g3");
        System.out.println("a2 b2 c2 d2 e2 f2 g2");
        System.out.println("a1 b1 c1 d1 e1 f1 g1");
        System.out.println();
        System.out.println("direction = u | d | l | r");
        System.out.println();

        while (true) {
            consoleView.display();
            System.out.println();

            String command = scanner.next();

            if (command.equals("d")) {
                String position = scanner.next();
                int i = 6 - ((int)position.charAt(1) - (int)'1');
                int j = (int)position.charAt(0) - (int)'a';

                MoveDirection direction = getDirection(scanner.next().toUpperCase().charAt(0));

                Player player = stadium.getPlayer(new Case(i, j));
                stadium.move(player, direction);
            } else if (command.equals("p")) {
                String from = scanner.next();
                int fromI =  6 - ((int)from.charAt(1) - (int)'1');
                int fromJ = (int)from.charAt(0) - (int)'a';

                String to = scanner.next();
                int toI = 6 - ((int)to.charAt(1) - (int)'1');
                int toJ = (int)to.charAt(0) - (int)'a';

                Player fromPlayer = stadium.getPlayer(new Case(fromI, fromJ));
                Player toPlayer = stadium.getPlayer(new Case(toI, toJ));
                stadium.pass(fromPlayer, toPlayer);
            }
        }
    }
    
    private static MoveDirection getDirection(char direction) {
    	switch(direction) {
    		case 'U':
    			return MoveDirection.UP;
    			
    		case 'D':
    			return MoveDirection.DOWN;
    			
    		case 'L':
    			return MoveDirection.LEFT;
    			
    		case 'R':
    			return MoveDirection.RIGHT;
    			
    		default:
    			throw new IllegalStateException("Wrong input direction.");
    	}
    }
}