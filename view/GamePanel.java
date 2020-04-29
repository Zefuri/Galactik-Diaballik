package view;

import javax.swing.JPanel;

import model.Stadium;

public class GamePanel extends JPanel {
	private ArkadiaNews arkadiaNews;
	
	public GamePanel(Stadium stadium) {
		this.arkadiaNews = new ArkadiaNews(stadium);
	}
}
