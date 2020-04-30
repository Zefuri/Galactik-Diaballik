package Listeners;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Player;
import model.Stadium;
import view.HoloTV;

public class MouseAction extends MouseAdapter {
	private HoloTV holoTV;
	private Stadium stadium;
	private Player targetedPlayer;
	private CasePosition casePosition;
	
	public MouseAction(HoloTV holoTV, Stadium stadium) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		this.targetedPlayer = null;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		super.mouseClicked(e);
		
		//We get the position of the case (meaning its position in the game grid)
		try {
			this.casePosition = getCasePosition(e.getX(), e.getY());
		} catch (IllegalStateException ex) {
			//The gameboard is a square, so the player is able to click on the right/bottom of the screen
			System.out.println("Please click on a gameboard case !");
		}
		
		
	}
	
	private CasePosition getCasePosition(int x, int y) {
		int caseSize = holoTV.getCaseSize();
		int xValue = x / caseSize;
		int yValue = y / caseSize;
		
		if (xValue > 6 || yValue > 6) {
			throw new IllegalStateException();
		}
		
		return new CasePosition(xValue, yValue);
	}
}
