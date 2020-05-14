package controller.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Action;
import model.Case;
import model.Player;
import model.Stadium;
import model.enums.ActionResult;
import model.enums.ActionType;
import model.enums.MoveDirection;
import patterns.Observable;
import patterns.Observer;
import view.HoloTV;

//import ai.StupidAI;
//import ai.PlayerType.Position;

public class MouseAction extends MouseAdapter implements Observer {
	private HoloTV holoTV;
	private Stadium stadium;
	private Case clickedCase;
	private Case playerAloneCase;
	private Case playerWithBallCase;

	//private StupidAI ai;
	private int clickNumber = 0;
	
	public MouseAction(HoloTV holoTV, Stadium stadium) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		this.playerAloneCase = null;
		this.playerWithBallCase = null;

		//this.ai = new StupidAI(0, stadium, Position.BOTTOM);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
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

		// provisoir
//		clickNumber++;
//
//		if (clickNumber%3 == 0 && clickNumber != 0) {
//			ai.play();
//		}
		
		holoTV.getArkadiaNews().repaint();
		holoTV.updateGameInfos();
		
		if (result == ActionResult.WIN) {
			//TODO Impl�menter le passage � l'�cran de fin
			System.out.println("Team \"" + stadium.getPlayer(playerWithBallCase).getTeam().getName() + "\" have won the match!");
			holoTV.getGamePanel().showEndGamePopUp(stadium.getPlayer(playerWithBallCase).getTeam().getName());
		}
		
		if (result == ActionResult.ANTIPLAY) {
			//TODO Impl�menter le passage � l'�cran de fin
			System.out.println("The enemy team made an antiplay: Team \"" + stadium.getPlayer(playerWithBallCase).getTeam().getName() + "\" have won the match!");
			holoTV.getGamePanel().showAntiPlayPopUp(stadium.getPlayer(playerWithBallCase).getTeam().getName());
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
			//There is a player only on the clicked case
			if (playerWithBallCase != null) {
				//Do a pass if possible
				Player previousOwner = stadium.getPlayer(playerWithBallCase);
				Player futureOwner = stadium.getPlayer(clickedCase);
				result = previousOwner.pass(futureOwner);
				
				//TODO remettre le if / else if en 1 bloc apr�s impl�mentation de l'�cran de fin
				
				if (result == ActionResult.DONE) {
					stadium.getPlayer(playerWithBallCase).setIfSelected(false);
					clearPlayers();
				} else if (result == ActionResult.WIN) {
					stadium.getPlayer(playerWithBallCase).setIfSelected(false);
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
				} else if (result == ActionResult.ANTIPLAY) {
					throw new RuntimeException("Antiplay detected!");
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
		
		switch((ActionType) object) {	
			case END_TURN : // following code is executed when the "end of turn" button is pressed
				res = this.stadium.endTurn();
				break;
				
			case UNDO :
				res = this.stadium.undoAction();
				break;
			
			case RESET :
				res = this.stadium.resetTurn();
				break;
		}
		
		if (res == ActionResult.ERROR) {
			System.err.println("You need to do at least one action before doing that.");
		}
		
		this.holoTV.updateGameInfos();
		clearSelectedPlayer();
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
