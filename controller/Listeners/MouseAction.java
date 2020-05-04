package controller.Listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Player;
import model.Stadium;
import view.HoloTV;

import controller.AI.StupidAI;
import controller.AI.PlayerType.Position;

public class MouseAction extends MouseAdapter {
	private HoloTV holoTV;
	private Stadium stadium;
	private Player targetedPlayer;
	private Case clickedCase;
	private Case playerAloneCase;
	private Case playerWithBallCase;

	private StupidAI ai;
	private int clickNumber = 0;
	
	public MouseAction(HoloTV holoTV, Stadium stadium) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		this.targetedPlayer = null;
		this.playerAloneCase = null;
		this.playerWithBallCase = null;

		this.ai = new StupidAI(0, stadium, Position.BOTTOM);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		//We get the position of the case (meaning its position in the game grid)
		try {
			this.clickedCase = getCase(e.getY(), e.getX());
		} catch (IllegalStateException ex) {
			//The gameboard is a square, so the player is able to click on the right/bottom of the screen
			System.out.println("Please click on a gameboard case!");
		}
		
		try {
			performRequestedAction();
		} catch (IllegalStateException ex) {
			//Means that the user performed an undoable action
			ex.printStackTrace();
		} catch (RuntimeException ex) {
			//Means that the user performed a doable action but an error occurred
			ex.printStackTrace();
		}

		// provisoir
		clickNumber++;

		if (clickNumber%3 == 0 && clickNumber != 0) {
			ai.play();
		}
		
		holoTV.getArkadiaNews().repaint();
		holoTV.updateGameInfos();
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
	
	private void performRequestedAction() {
		//Verify if the user click must perform a doable action
		
		if (stadium.isABallHere(clickedCase.getX(), clickedCase.getY())) {
			//There is a player with a ball on the clicked case
			if (playerWithBallCase != null) {
				throw new IllegalStateException("You can not pass the ball to yourself.");
			} else {
				//Change the actual player
				if (playerAloneCase != null) {
					stadium.whatsInTheBox(playerAloneCase.getX(), playerAloneCase.getY()).setPlayerSelected(false);
				}
				
				setPlayerWithBallCase(clickedCase.getX(), clickedCase.getY());
				stadium.whatsInTheBox(clickedCase.getX(), clickedCase.getY()).setPlayerSelected(true);
			}
		} else if (stadium.isAPlayerOnly(clickedCase.getX(), clickedCase.getY())) {
			//There is a player only on the clicked case
			if (playerWithBallCase != null) {
				//Do a pass if possible
				Player previousOwner = stadium.whatsInTheBox(playerWithBallCase.getX(), playerWithBallCase.getY());
				Player futureOwner = stadium.whatsInTheBox(clickedCase.getX(), clickedCase.getY());
				
				if (stadium.pass(previousOwner, futureOwner)) {
					stadium.whatsInTheBox(playerWithBallCase.getX(), playerWithBallCase.getY()).setPlayerSelected(false);
					clearPlayers();
				} else {
					throw new IllegalStateException("Either the two players are not aligned, or an opponent is between them.");
				}
			} else {
				//Change the actual player and change the selection value
				if (playerAloneCase != null) {
					stadium.whatsInTheBox(playerAloneCase.getX(), playerAloneCase.getY()).setPlayerSelected(false);
				}
				if (playerWithBallCase != null) {
					stadium.whatsInTheBox(playerWithBallCase.getX(), playerWithBallCase.getY()).setPlayerSelected(false);
				}
				
				setPlayerAloneCase(clickedCase.getX(), clickedCase.getY());
				stadium.whatsInTheBox(clickedCase.getX(), clickedCase.getY()).setPlayerSelected(true);
			}
		} else {
			//The case is empty
			if (playerAloneCase != null) {
				//If the selected case is next to the player case, we move the player
				Player p = stadium.whatsInTheBox(playerAloneCase.getX(), playerAloneCase.getY());
				char c = stadium.getMoveDirection(p, clickedCase.getX(), clickedCase.getY());
				
				if (stadium.move(p, c)) {
					playerAloneCase = new Case(clickedCase.getX(), clickedCase.getY());
				} else {
					throw new IllegalStateException("The selected case is not situated next to the player!");
				}
			} else if (playerWithBallCase != null) {
				stadium.whatsInTheBox(playerWithBallCase.getX(), playerWithBallCase.getY()).setPlayerSelected(false);
				clearPlayers();
				throw new IllegalStateException("You can not move a player that has the ball.");
			} else {
				stadium.whatsInTheBox(playerAloneCase.getX(), playerAloneCase.getY()).setPlayerSelected(false);
				clearPlayers();
				throw new IllegalStateException("You must select a player before selecting an empty case!");
			}
		}
	}
	
	private void setPlayerWithBallCase(int x, int y) {
		this.playerWithBallCase = new Case(x, y);
		this.playerAloneCase = null;
	}
	
	private void setPlayerAloneCase(int x, int y) {
		this.playerWithBallCase = null;
		this.playerAloneCase = new Case(x, y);
	}
	
	private void clearPlayers() {
		this.playerWithBallCase = null;
		this.playerAloneCase = null;
	}
}
