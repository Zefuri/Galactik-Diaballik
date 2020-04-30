package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Stadium;

public class GamePanel extends JPanel {
	private Stadium stadium;	
	private ArkadiaNews arkadiaNews;
	private JPanel gameControlPanel;
	
	private JLabel nbTurn;
	private JLabel whosturn;
	private JLabel nbPassRemaining;
	private JLabel nbMoveRemaining;
	private JButton endTurnButton;
	
	public GamePanel(Stadium stadium) {
		super(new BorderLayout());
		this.stadium = stadium;
		this.arkadiaNews = new ArkadiaNews(stadium);
		this.add(this.arkadiaNews, BorderLayout.CENTER);
		
		this.gameControlPanel = new JPanel(new GridLayout(5, 0));
		
		this.nbTurn = new JLabel("Tour " + (stadium.getTurn() + 1) + " :");
		this.gameControlPanel.add(this.nbTurn);
		
		this.whosturn = new JLabel("Joueur " + (stadium.whosTurn() + 1) + ", à toi !");
		this.gameControlPanel.add(this.whosturn);
		
		this.nbPassRemaining = new JLabel("Passe : " + (1 - stadium.getNbPass()));
		this.gameControlPanel.add(this.nbPassRemaining);
		
		this.nbMoveRemaining = new JLabel("Déplacements : " + (2 - stadium.getNbMove()));
		this.gameControlPanel.add(this.nbMoveRemaining);
		
		this.endTurnButton = new JButton("Fin du tour !");
		this.gameControlPanel.add(this.endTurnButton);
		
		this.add(this.gameControlPanel, BorderLayout.EAST);
	}
	
	public ArkadiaNews getArkadiaNews() {
		return this.arkadiaNews;
	}
	
	public void updateGamePanelInfos() {
		this.nbTurn.setText("Tour " + (stadium.getTurn() + 1) + " :");
		this.whosturn.setText("Joueur " + (stadium.whosTurn() + 1) + ", à toi !");
		this.nbPassRemaining.setText("Passe : " + (1 - stadium.getNbPass()));
		this.nbMoveRemaining.setText("Déplacements : " + (2 - stadium.getNbMove()));
	}
}
