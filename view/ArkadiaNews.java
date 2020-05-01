package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;

import model.Player;
import model.Stadium;
import model.ModelConstants;

public class ArkadiaNews extends JComponent {
	private Stadium stadium;
	
	private int caseSize;
	
	public ArkadiaNews(Stadium stadium) {
		this.stadium = stadium;
	}

	@Override
	protected void paintComponent(Graphics g) {
		// A few base variables
        Graphics2D drawable = (Graphics2D) g;
        int windowWidth = getSize().width;
        int windowHeight = getSize().height;
        
        //To keep squared cases, we take the minimum between screen width and screen height
        this.caseSize = Math.min(windowWidth, windowHeight) / 7;
        
        // Drawing all players and outlines of the stadium
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
            	Color ball = Color.WHITE;
            	
            	// Recover the player
                Player p = this.stadium.whatsInTheBox(i, j);
                
             // Draw current player (change color if selected)
                if (p != null) {
                	// Draw current player (change color if selected)
                	if (p.playerSelected()) {
                		drawable.setColor(Color.YELLOW);
                	} else {
                		drawable.setColor(Color.BLACK);
                	}
	                drawable.fillOval(caseSize * j, caseSize * i, caseSize, caseSize);
	                
	              
                	drawable.setStroke(new BasicStroke(2));
                	
                	switch (p.getTeam().getPosition()) {
	                	case ModelConstants.TEAM_ONE:

	                		drawable.setColor(Color.RED);
	                		ball = Color.BLACK;
	                		break;
	                		
	                	case ModelConstants.TEAM_TWO:
	                		drawable.setColor(Color.CYAN);
	                		ball = Color.WHITE;
	                		break;
	                }
	                
	                // Fill current player with the good color
	                drawable.fillOval(caseSize * j, caseSize * i, caseSize, caseSize);
	                
	                // Draw the outline of the player                	
                	if (p.playerSelected()) {
                		drawable.setColor(Color.YELLOW);
                	} else {
                		drawable.setColor(Color.BLACK);
                	}
                	
	                drawable.drawOval(caseSize * j, caseSize * i, caseSize, caseSize);
	                
	                // Draw the ball if necessary
	                if (p.hasBall()) {
	                	drawable.setColor(Color.BLACK);
	                	drawable.setStroke(new BasicStroke(4));
	                	drawable.drawOval((caseSize * j) + caseSize/6, (caseSize * i) + caseSize/6, caseSize - (caseSize/3), caseSize - (caseSize/3));
	                	drawable.setColor(ball);
	                	drawable.fillOval((caseSize * j) + caseSize/6, (caseSize * i) + caseSize/6, caseSize - (caseSize/3), caseSize - (caseSize/3));
	                }
                }
                
                // Draw outlines
                drawable.setStroke(new BasicStroke(1));
                drawable.setColor(Color.BLACK);
                drawable.drawLine(caseSize * (j + 1), caseSize * i, caseSize * (j + 1), caseSize * (i + 1));
                drawable.drawLine(caseSize * j, caseSize * (i + 1), caseSize * (j + 1), caseSize * (i + 1));
            }
        }
	}
	
	public int getCaseSize() {
		return this.caseSize;
	}
}
