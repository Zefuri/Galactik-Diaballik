package controller.listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Case;
import model.Stadium;
import model.enums.ActionResult;
import model.enums.ActionType;
import patterns.Observer;
import view.HoloTV;

//import ai.StupidAI;
//import ai.PlayerType.Position;

public class MouseAction extends MouseAdapter implements Observer {
	private HoloTV holoTV;
	private Stadium stadium;

	//private StupidAI ai;
	private int clickNumber = 0;
	
	public MouseAction(HoloTV holoTV, Stadium stadium) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		stadium.setPlayerAloneCase(null);
		stadium.setPlayerWithBallCase(null);

		//this.ai = new StupidAI(0, stadium, Position.BOTTOM);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
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
			System.out.println("Team \"" + stadium.getPlayer(stadium.getPlayerWithBallCase()).getTeam().getName() + "\" have won the match!");
			holoTV.getGamePanel().showEndGamePopUp(stadium.getPlayer(stadium.getPlayerWithBallCase()).getTeam().getName());
		}
		
		if (result == ActionResult.ANTIPLAY) {
			//TODO Impl�menter le passage � l'�cran de fin
			System.out.println("The enemy team made an antiplay: Team \"" + stadium.getPlayer(stadium.getPlayerWithBallCase()).getTeam().getName() + "\" have won the match!");
			holoTV.getGamePanel().showAntiPlayPopUp(stadium.getPlayer(stadium.getPlayerWithBallCase()).getTeam().getName());
		}

		// TODO : make the AI play
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
				break;
				
			case UNDO :
				res = this.stadium.undoAction();
				break;
			
			case RESET :
				res = this.stadium.resetTurn();
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
	}

}
