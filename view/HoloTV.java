package view;

import javax.swing.JFrame;

import model.Stadium;

public class HoloTV implements Runnable {
	private JFrame frame;
	private Stadium stadium;
	
	private GamePanel gamePanel;
	
	public HoloTV(Stadium stadium) {
		this.frame = new JFrame();
		this.stadium = stadium;
		this.gamePanel = new GamePanel(stadium);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
	}

}
