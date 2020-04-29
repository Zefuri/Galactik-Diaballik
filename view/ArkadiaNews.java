package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import model.Player;
import model.Stadium;

public class ArkadiaNews extends JComponent {
	private Stadium stadium;
	
	public ArkadiaNews(Stadium stadium) {
		this.stadium = stadium;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// A few base variables
        Graphics2D drawable = (Graphics2D) g;
        int windowWidth = getSize().width;
        int windowHeight = getSize().height;
        
        // Dimensions of a case
        int caseWidth = windowWidth/7;
        int caseHeight = windowHeight/7;
        
        // Drawing all players and outlines of the stadiums
        for(int i = 0; i < 7; i++) {
            for(int j = 0; j < 7; j++) {
            	Color ball = Color.WHITE;
            	
            	// Recover the player
                Player p = this.stadium.whatsInTheBox(i, j);
                
                if(p != null) {
                	// Draw current player
                	drawable.setColor(Color.BLACK);
	                drawable.drawOval(caseWidth * i, caseHeight * j, caseWidth, caseHeight);
	                
	                switch(p.getTeam()) {
	                	case Stadium.teamOne:
	                		drawable.setColor(Color.RED);
	                		ball = Color.BLACK;
	                		break;
	                	case Stadium.teamTwo:
	                		drawable.setColor(Color.CYAN);
	                		ball = Color.WHITE;
	                		break;
	                }
	                
	                // Fill current player with the good color
	                drawable.fillOval(caseWidth * i, caseHeight * j, caseWidth, caseHeight);
	                
	                // Draw the ball if necessary
	                if(p.getBallPossession()) {
	                	drawable.setColor(Color.BLACK);
	                	drawable.drawOval((caseWidth * i) + (caseWidth/6), (caseHeight * j) + (caseHeight/6), caseWidth - (caseWidth/3), caseHeight - (caseHeight/3));
	                	drawable.setColor(ball);
	                	drawable.fillOval((caseWidth * i) + (caseWidth/6), (caseHeight * j) + (caseHeight/6), caseWidth - (caseWidth/3), caseHeight - (caseHeight/3));
	                }
                }
                
                // Draw outlines
                drawable.setColor(Color.BLACK);
                drawable.drawLine(caseWidth * i, caseHeight * (j + 1), caseWidth * (i + 1), caseHeight * (j + 1));
                drawable.drawLine(caseWidth * (i + 1), caseHeight * j, caseWidth * (i + 1), caseHeight * (j + 1));
            }
        }
	}	
}
