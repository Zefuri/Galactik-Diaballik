package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import model.Player;
import model.Stadium;
import model.Case;
import model.ModelConstants;

public class ArkadiaNews extends JComponent {
	private Stadium stadium;
	
	private int caseSize;
	
	private Image snowKidPlayer;
	private Image snowKidBall;
	private Image shadowPlayer;
	private Image shadowBall;
	
	public ArkadiaNews(Stadium stadium) {
		this.stadium = stadium;
		this.setBackground(VisualResources.getInstance().customGrassGreen);
		
		try {
			this.snowKidPlayer = ImageIO.read(getClass().getResourceAsStream("/resources/images/snowKidPlayer.png"));
			this.snowKidBall = ImageIO.read(getClass().getResourceAsStream("/resources/images/snowKidBall.png"));
			this.shadowPlayer = ImageIO.read(getClass().getResourceAsStream("/resources/images/shadowPlayer.png"));
			this.shadowBall = ImageIO.read(getClass().getResourceAsStream("/resources/images/shadowBall.png"));
		} catch (IOException e) {
			System.err.println("Les images n'ont pas pu etre chargees : ");
			e.printStackTrace();
		}
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
        for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
            	// Drawing all case
            	drawable.setColor(VisualResources.getInstance().customGrassGreen);
            	drawable.fillRect(caseSize * j, caseSize * i, caseSize, caseSize);
            	
            	// Recover the player
                Player p = this.stadium.getPlayer(new Case(i, j));
                
                Image ball = this.snowKidBall;
                
                // Draw current player (change color if selected)
                if (p != null) {
                	switch (p.getTeam().getPosition()) {
	                	case TOP:
	                		drawable.drawImage(this.snowKidPlayer, caseSize * j, caseSize * i, caseSize, caseSize, null);
	                		break;
	                	case BOTTOM:
	                		drawable.drawImage(this.shadowPlayer, caseSize * j, caseSize * i, caseSize, caseSize, null);
	                		ball = this.shadowBall;
	                		break;
	                }
	                
	                // Draw the outline of the player
                	if (p.isSelected()) {
                		drawable.setStroke(new BasicStroke(2));
                		drawable.setColor(Color.YELLOW);
                		drawable.drawOval(caseSize * j, caseSize * i, caseSize, caseSize);
                	}

	                // Draw the ball
	                if(p.hasBall()) {
	                	drawable.drawImage(ball, (caseSize * j) + caseSize/6, (caseSize * i) + caseSize/6, caseSize - (caseSize/3), caseSize - (caseSize/3), null);
	                }
                }
            }
        }
        // Draw outlines
        drawable.setStroke(new BasicStroke(1));
        drawable.setColor(Color.BLACK);
        
        drawable.drawLine(0, 0, 0, caseSize * 7);
        drawable.drawLine(0, 0, caseSize * 7, 0);
        
        for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
            for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
		        drawable.drawLine(caseSize * (j + 1), caseSize * i, caseSize * (j + 1), caseSize * (i + 1));
		        drawable.drawLine(caseSize * j, caseSize * (i + 1), caseSize * (j + 1), caseSize * (i + 1));
            }
        }

        Repainter.getInstance().isRepainted = true;
	}
	
	public int getCaseSize() {
		return this.caseSize;
	}
}
