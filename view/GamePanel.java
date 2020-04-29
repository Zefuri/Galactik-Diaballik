package view;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import model.Stadium;

public class GamePanel extends JPanel {
	private ArkadiaNews arkadiaNews;
	
	public GamePanel(Stadium stadium) {
		super(new BorderLayout());
		this.arkadiaNews = new ArkadiaNews(stadium);
		this.add(this.arkadiaNews, BorderLayout.CENTER);
	}
	
	public ArkadiaNews getArkadiaNews() {
		return this.arkadiaNews;
	}
}
