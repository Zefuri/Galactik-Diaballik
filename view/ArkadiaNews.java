package view;

import java.awt.Graphics;

import javax.swing.JComponent;

import model.Stadium;

public class ArkadiaNews extends JComponent {
	private Stadium stadium;
	
	public ArkadiaNews(Stadium stadium) {
		this.stadium = stadium;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
	}
	
	
}
