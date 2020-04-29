package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import model.Player;
import model.Stadium;
import view.HoloTV;

public class MouseAction extends MouseAdapter {
	private HoloTV holoTV;
	private Stadium stadium;
	private Player targetedPlayer;
	
	public MouseAction(HoloTV holoTV, Stadium stadium) {
		this.holoTV = holoTV;
		this.stadium = stadium;
		this.targetedPlayer = null;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		super.mouseClicked(e);
	}
}
