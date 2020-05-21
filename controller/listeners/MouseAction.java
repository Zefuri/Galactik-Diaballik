package listeners;


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import ai.BallActionAI_1;

import model.Action;
import model.Case;
import model.Player;
import model.Stadium;
import model.enums.ActionResult;
import model.enums.ActionType;
import model.enums.GameResult;
import model.enums.MoveDirection;
import model.enums.TeamPosition;

import patterns.Observer;
import saver.GameSaver;
import view.HoloTV;

public class MouseAction extends MouseAdapter implements Observer {
	private HoloTV holoTV;
	private Stadium stadium;

	private GameSaver gameSaver;

	private Case clickedCase;
	private Case playerAloneCase;
	private Case playerWithBallCase;

	private BallActionAI_1 AI;
	
	private boolean visualisationMode;
	
	public MouseAction(HoloTV holoTV, Stadium stadium, boolean withAI, GameSaver gameSaver) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		this.gameSaver = gameSaver;
		this.playerAloneCase = null;
		this.playerWithBallCase = null;
		this.visualisationMode = stadium.isInVisualisationMode();

		if (withAI) { // initialize AI if needed
			AI = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (!this.visualisationMode) {
			ActionResult result = null;
			
			//We get the position of the case (meaning its position in the game grid)
			try {
				this.clickedCase = getCase(e.getY(), e.getX());
			} catch (IllegalStateException ex) {
				//The gameboard is a square, so the player is able to click on the right/bottom of the screen
				System.out.println("Please click on a gameboard case!");
			}
			
			try {
				result = performRequestedAction();
			} catch (IllegalStateException ex) {
				//Means that the user performed an undoable action
				System.out.println(ex.toString());
				//ex.printStackTrace();
			} catch (RuntimeException ex) {
				//Means that the user performed a doable action but an error occurred
				System.out.println(ex.toString());
				//ex.printStackTrace();
			}
	
			// provisoire
	//		clickNumber++;
	//
	//		if (clickNumber%3 == 0 && clickNumber != 0) {
	//			ai.play();
	//		}
			
			holoTV.getArkadiaNews().repaint();
			holoTV.updateGameInfos();
			
			if (result == ActionResult.WIN) {
				if(stadium.getCurrentTeamTurn() == stadium.getTeam(TeamPosition.BOTTOM) && AI != null) {
					holoTV.switchToEndGamePanel(GameResult.DEFEAT, stadium.getTeam(TeamPosition.TOP).getName());
				} else {
					holoTV.switchToEndGamePanel(GameResult.VICTORY, stadium.getCurrentTeamTurn().getName());
				}
				clearSelectedPlayer();
			}
			
			if (result == ActionResult.ANTIPLAY_TOP && AI != null) {
				holoTV.switchToEndGamePanel(GameResult.DEFEAT_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
				clearSelectedPlayer();
			} else if(result == ActionResult.ANTIPLAY_TOP) {
				holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getTeam(TeamPosition.BOTTOM).getName());
				clearSelectedPlayer();
			} else if(result == ActionResult.ANTIPLAY_BOT) {
				holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
				clearSelectedPlayer();
			}
		} else {
			//We are in visualization mode, so we do not want the user to perform actions other than undo/redo
			System.err.println("You might not press anything else than the undo/redo action buttons!");
		}
	}
	
	private Case getCase(int x, int y) {
		int caseSize = holoTV.getCaseSize();
		int xValue = x / caseSize;
		int yValue = y / caseSize;
		
		if (xValue > 6 || yValue > 6) {
			throw new IllegalStateException();
		}
		
		return new Case(xValue, yValue);
	}
	
	private ActionResult performRequestedAction() {
		//Verify if the user click must perform a doable action
		ActionResult result = null;
		
		if (stadium.hasABall(clickedCase)) {
			//There is a player with a ball on the clicked case
			if (playerWithBallCase != null) {
				throw new IllegalStateException("You can not pass the ball to yourself or an enemy player.");
			} else {
				//Change the actual player if possible
				Player clickedPlayer = stadium.getPlayer(clickedCase);
				
				if (clickedPlayer.canBeSelectedForPass()) {
					if (playerAloneCase != null) {
						stadium.getPlayer(playerAloneCase).setIfSelected(false);
					}
					
					setPlayerWithBallCase(clickedCase);
					clickedPlayer.setIfSelected(true);
				} else {
					throw new IllegalStateException("The chosen player is not in the current playing team, or you have already made a pass this turn.");
				}
			}
		} else if (stadium.hasAPlayerOnly(clickedCase)) {
			// There is a player only on the clicked case
			if (playerWithBallCase != null) {
				// Do a pass if possible
				Player previousOwner = stadium.getPlayer(playerWithBallCase);
				Player futureOwner = stadium.getPlayer(clickedCase);
				result = previousOwner.pass(futureOwner);
				
				//TODO remettre le if / else if en 1 bloc apr�s impl�mentation de l'�cran de fin
				
				if (result == ActionResult.DONE) {
					stadium.getPlayer(playerWithBallCase).setIfSelected(false);
					clearPlayers();
					this.gameSaver.overwriteSave();
				} else if (result == ActionResult.WIN) {
					stadium.getPlayer(playerWithBallCase).setIfSelected(false);
					this.gameSaver.overwriteSave();
				} else {
					throw new IllegalStateException("Either it is not your turn, or the two players are not aligned or have an opponent between them.");
				}
			} else {
				//Change the actual player and change the selection value 
				Player clickedPlayer = stadium.getPlayer(clickedCase);
				
				if (clickedPlayer.canBeSelected()) {
					if (playerAloneCase != null) {
						stadium.getPlayer(playerAloneCase).setIfSelected(false);
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
				Player player = stadium.getPlayer(playerAloneCase);
				MoveDirection direction = stadium.getMoveDirection(player, clickedCase);
				
				if (direction == null) {
					throw new IllegalStateException("Either it is not your turn, or the selected case is not situated next to the player.");
				}
				
				result = player.move(direction);
				
				if (result == ActionResult.DONE) {
					setPlayerAloneCase(clickedCase);
					this.gameSaver.overwriteSave();
				} else if (result == ActionResult.ANTIPLAY_TOP || result == ActionResult.ANTIPLAY_BOT) {
					this.gameSaver.overwriteSave();
					System.out.println("Antiplay detected!");
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
	
	private void setPlayerWithBallCase(Case c) {
		this.playerWithBallCase = new Case(c);
		this.playerAloneCase = null;
	}
	
	private void setPlayerAloneCase(Case c) {
		this.playerWithBallCase = null;
		this.playerAloneCase = new Case(c);
	}
	
	private void clearPlayers() {
		this.playerWithBallCase = null;
		this.playerAloneCase = null;
	}

	@Override
	public void update(Object object) {
		ActionResult res = ActionResult.DONE;
		
		clearSelectedPlayer();
		
		switch((ActionType) object) {	
			case END_TURN : // following code is executed when the "end of turn" button is pressed
				res = this.stadium.endTurn();
				
				gameSaver.overwriteSave();

				if (AI != null) {
					ArrayList<Action> actions = AI.play(1);
          
					ActionResult result;
					int indexAction = 0;
					Action currentAction = actions.get(indexAction);
					
					while(((result = stadium.actionPerformedAI(currentAction)) == ActionResult.DONE) && indexAction < actions.size()) {
						indexAction++;
						currentAction = actions.get(indexAction);
					}
					
					if(result == ActionResult.WIN) {
						holoTV.switchToEndGamePanel(GameResult.DEFEAT, stadium.getTeam(TeamPosition.TOP).getName());
					}

					if(result == ActionResult.ANTIPLAY_TOP) {
						holoTV.switchToEndGamePanel(GameResult.DEFEAT_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
					} else if(result == ActionResult.ANTIPLAY_BOT) {
						holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
					}
					
					holoTV.getArkadiaNews().repaint();
					holoTV.updateGameInfos();
				}

				break;
				
			case UNDO :
				if (!this.visualisationMode) {
					res = this.stadium.undoAction();
					gameSaver.overwriteSave();
				} else {
					res = this.stadium.undoAction();
					
					if (res == ActionResult.ERROR) {
						this.holoTV.getGamePanel().showFirstTurnReachedPopup();
					}
				}
				
				break;
			
			case RESET :
				res = this.stadium.resetTurn();
				gameSaver.overwriteSave();
			
				break;
				
			case REDO:
				if (!this.stadium.redoAction()) {
					this.holoTV.getGamePanel().showLastTurnReachedPopup();
				}
				break;
		}
		
		if (res == ActionResult.ERROR) {
			System.err.println("You need to do at least one action before doing that.");
		}
		
		this.holoTV.updateGameInfos();
	}
	
	private void clearSelectedPlayer() {
		if (playerWithBallCase != null) {
			stadium.getPlayer(playerWithBallCase).setIfSelected(false);
		} else if (playerAloneCase != null) {
			stadium.getPlayer(playerAloneCase).setIfSelected(false);
		}
		
		clearPlayers();
		
		holoTV.getArkadiaNews().repaint();
	}
}
