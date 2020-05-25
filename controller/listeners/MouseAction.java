package controller.listeners;

import controller.ai.BallActionAI_1;
import model.Action;
import model.Case;
import model.Stadium;
import model.enums.*;
import patterns.Observer;
import saver.GameSaver;
import view.HoloTV;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MouseAction extends MouseAdapter implements Observer {
	private HoloTV holoTV;
	private Stadium stadium;
	private GameSaver gameSaver;

	private BallActionAI_1 AI;
	private boolean isAITurn;
	private ArrayList<Action> AIActions;
	private Timer timer;
	
	private boolean visualisationMode;
	
	public MouseAction(HoloTV holoTV, Stadium stadium, boolean withAI, GameSaver gameSaver) {
		this.holoTV = holoTV;
		this.stadium = stadium;
    
		stadium.setPlayerAloneCase(null);
		stadium.setPlayerWithBallCase(null);
    
		this.gameSaver = gameSaver;
		this.visualisationMode = stadium.isInVisualisationMode();

		if (withAI) { // initialize AI if needed
			AI = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM)); // setup the AI as the bottom player
			isAITurn = false; // player starts
			AIActions = new ArrayList<>();
//			timer = new Timer(300, new PVETimer(this)); // AI plays every 0.3 seconds
//			timer.start();
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (!this.visualisationMode) {
			if (!isAITurn) {
				ActionResult result = null;

				//We get the position of the case (meaning its position in the game grid)
				try {
					stadium.setClickedCase(getCase(e.getY(), e.getX()));
				} catch (IllegalStateException ex) {
					//The gameboard is a square, so the player is able to click on the right/bottom of the screen
					System.out.println("Please click on a gameboard case!");
				}

				try {
					result = stadium.performRequestedAction();
					gameSaver.overwriteSave();
				} catch (IllegalStateException ex) {
					//Means that the user performed an undoable action
					System.out.println(ex.toString());
					//ex.printStackTrace();
				} catch (RuntimeException ex) {
					//Means that the user performed a doable action but an error occurred
					System.out.println(ex.toString());
					//ex.printStackTrace();
				}

				holoTV.getArkadiaNews().repaint();
				holoTV.updateGameInfos();

				if (result == ActionResult.WIN) {
					if(stadium.getCurrentTeamTurn() == stadium.getTeam(TeamPosition.BOTTOM) && AI != null) {
						holoTV.switchToEndGamePanel(GameResult.DEFEAT, stadium.getTeam(TeamPosition.TOP).getName());
					} else {
						holoTV.switchToEndGamePanel(GameResult.VICTORY, stadium.getCurrentTeamTurn().getName());
					}
					stadium.clearSelectedPlayer();
				}

				if (result == ActionResult.ANTIPLAY_CURRENT && AI != null) {
					holoTV.switchToEndGamePanel(GameResult.DEFEAT_ANTIPLAY, stadium.getCurrentTeamTurn().getName());
					stadium.clearSelectedPlayer();
				} else if(result == ActionResult.ANTIPLAY_CURRENT) {
					holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getNotPlayingTeam().getName());
					stadium.clearSelectedPlayer();
				} else if(result == ActionResult.ANTIPLAY) {
					holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getCurrentTeamTurn().getName());
					stadium.clearSelectedPlayer();
				}
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

	@Override
	public void update(Object object) {
		ActionResult res = ActionResult.DONE;
		
		stadium.clearSelectedPlayer();
		
		switch((ActionType) object) {	
			case END_TURN : // following code is executed when the "end of turn" button is pressed
				res = this.stadium.endTurn();
				
				gameSaver.overwriteSave();
        
				if (AI != null && res != ActionResult.ERROR) {
					isAITurn = true;
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
				System.out.println("redo");
				break;

			case CHEAT :
				stadium.switchCheatModActivated();
				holoTV.getGamePanel().cheatModColorToggle(stadium.isCheatModActivated());
				break;
		}
		
		if (res == ActionResult.ERROR) {
			System.err.println("You need to do at least one action before doing that.");
		}
		
		this.holoTV.updateGameInfos();
		this.holoTV.getGamePanel().repaint(); //Added in last case
	}

	/*
	Makes the AI play. function should be called every few milliseconds by the repainter
	 */
	public void playAI() {
		if (isAITurn) {
			// if we got no actions available then it's the beginning of the AIs turn.
			if (AIActions.size() == 0) {
				AIActions = AI.play(0); // generate the next actions
			}

			ActionResult actionResult = stadium.actionPerformedAI(AIActions.get(0)); // perform the first action in queue...
			gameSaver.overwriteSave();
			AIActions.remove(0); // ...and remove it

			holoTV.getArkadiaNews().repaint(); // show the new move
			holoTV.updateGameInfos();

			// check if WIN and switch to the end panel
			if(actionResult == ActionResult.WIN) {
				timer.stop();
				holoTV.switchToEndGamePanel(GameResult.DEFEAT, stadium.getTeam(TeamPosition.TOP).getName());
			}

			// check if ANTIPLAY and switch to the end panel
			if(actionResult == ActionResult.ANTIPLAY) {
				timer.stop();
				holoTV.switchToEndGamePanel(GameResult.DEFEAT_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
			} else if(actionResult == ActionResult.ANTIPLAY_CURRENT) {
				timer.stop();
				holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getTeam(TeamPosition.TOP).getName());
			}

			// if this was the last action then switch back to the player
			if (AIActions.size() == 0) {
				isAITurn = false;
			}
		}
	}
}
