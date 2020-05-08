package controller.ai;

import java.util.ArrayList;
import java.util.Collection;

import model.*;
import model.enums.ActionType;
import model.enums.MoveDirection;

public class MinMax {
    Stadium stadium;
    Team team;
    int maxDepth;

    public MinMax(Team team, int maxDepth) {
        this.stadium = team.getStadium();
        this.team = team;
        this.maxDepth = maxDepth;
    }

    // les fonctions play et minmax pourait etre reunies en une seule 
    // mais il faudrait alors retourner des tuples ce qui aurait un cout non negligeable
    public Turn play() {
        Turn bestTurn = null;
        int bestTurnValue = Integer.MIN_VALUE;

        for (Turn t : getPossibleTurns(team)) {
            doTurn(t);
            int turnValue = minmax(team.getEnemyTeam(), maxDepth);
            undoTurn(t);

            if (turnValue > bestTurnValue) {
                bestTurnValue = turnValue;
                bestTurn = t;
            }
        }

        return bestTurn;
    }

    // attention team != this.team
    private int minmax(Team team, int depth) {
        if (this.team.isWinner()) {
            return Integer.MAX_VALUE;
        } else if (this.team.getEnemyTeam().isWinner()) {
            return Integer.MIN_VALUE;
        }

        if (depth == 0) {
            return heuristic();
        } 

        boolean maximizingTeam = team == this.team;
        int value = maximizingTeam ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Turn t : getPossibleTurns(team)) {
            doTurn(t);
            // changer pour decrementer seulement quand maximizingTeam == true
            int turnValue = minmax(team.getEnemyTeam(), depth-1);
            undoTurn(t);

            if (maximizingTeam) {
                value = Math.max(value, turnValue);
            } else {
                value = Math.min(value, turnValue);
            }
        }

        return value;
    }

    // guillaume dit que l'heuristic pourait etre approfondie
    private int heuristic() {
        int allyBallProgression = team.getBallPlayer().getPosition().getX();
        int enemyBallProgression = 6 - team.getEnemyTeam().getBallPlayer().getPosition().getX();
        return allyBallProgression - enemyBallProgression;
    }

    // devrait etre deplace dans le model
    private void doAction(Action action) {
        stadium.actionPerformed(action);
    }

    // devrait etre deplace dans le model
    private void undoAction(Action action) {
        doAction(action.inverse());
    }

    // devrait etre deplace dans le model
    private void doTurn(Turn turn) {
        for (Action a : turn.getActions()) {
            doAction(a);
        }
    }

    // devrait etre deplace dans le model
    private void undoTurn(Turn turn) {
        doTurn(turn.inverse());
    }

    // attention team != this.team
    private Collection<Turn> getPossibleTurns(Team team) {
        ArrayList<Turn> possibleTurns = new ArrayList<>();
        expandPossibleTurns(team, new Turn(team), possibleTurns);
        return possibleTurns;
    }

    // attention team != this.team
    private void expandPossibleTurns(Team team, Turn currentTurn, Collection<Turn> possibleTurns) {
        Collection<Action> possibleActions = getPossibleActions(team, currentTurn);

        // si la aucune action n'est possible alors le tour est complet
        // peut etre le ramplacer par un turn.isComplet() pour plus de clairte
        if (possibleActions.isEmpty()) {
            possibleTurns.add(currentTurn);
        } else {
            for (Action a : possibleActions) {
                doAction(a);

                Turn turn = currentTurn.copy();
                turn.addAction(a);

                expandPossibleTurns(team, turn, possibleTurns);

                undoAction(a);
            }
        }
    }
   
    // turn est uniquement utilise pour savoir le nombre de deplacements de passes restants
    // attention team != this.team
    private Collection<Action> getPossibleActions(Team team, Turn turn) {
        ArrayList<Action> possibleActions = new ArrayList<>();

        // ! il est possible de faire plusieurs END_TURN, a corriger

        if (turn.getNbMoveLeft() > 0 || turn.getNbPassLeft() > 0) {
            Action action = new Action(ActionType.END_TURN, null, null, null, null);
            possibleActions.add(action);
        }

        if (turn.getNbMoveLeft() > 0) {
            for (Player p : team.getPlayers()) {
                // il serait plus pratique de surcharger le constructeur de Action

                if (p.canMove(MoveDirection.UP)) {
                    Case nextCase = new Case(p.getPosition().getX()-1, p.getPosition().getY());
                    Action action = new Action(ActionType.MOVE, null, p, p.getPosition(), nextCase);
                    possibleActions.add(action);
                }

                if (p.canMove(MoveDirection.DOWN)) {
                    Case nextCase = new Case(p.getPosition().getX()+1, p.getPosition().getY());
                    Action action = new Action(ActionType.MOVE, null, p, p.getPosition(), nextCase);
                    possibleActions.add(action);
                }

                if (p.canMove(MoveDirection.LEFT)) {
                    Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY()-1);
                    Action action = new Action(ActionType.MOVE, null, p, p.getPosition(), nextCase);
                    possibleActions.add(action);
                }

                if (p.canMove(MoveDirection.RIGHT)) {
                    Case nextCase = new Case(p.getPosition().getX(), p.getPosition().getY()+1);
                    Action action = new Action(ActionType.MOVE, null, p, p.getPosition(), nextCase);
                    possibleActions.add(action);
                }
            }
        }

        if (turn.getNbPassLeft() > 0) {
            Player ballPlayer = team.getBallPlayer();

            for (Player p : team.getPlayers()) {
                if (ballPlayer.canPass(p)) {
                    Action action = new Action(ActionType.PASS, ballPlayer, p, ballPlayer.getPosition(), p.getPosition());
                    possibleActions.add(action);
                }
            }
        }

        return possibleActions;
    }
}