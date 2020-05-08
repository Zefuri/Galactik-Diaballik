package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

import model.Stadium;
import model.enums.ActionType;

import patterns.Observable;
import patterns.Observer;

public class GamePanel extends JPanel implements Observable {
	private Stadium stadium;	
	private ArkadiaNews arkadiaNews;
	private JPanel gameControlPanel;
	
	private JLabel nbTurn;
	private JLabel whosturn;
	private JLabel nbPassRemaining;
	private JLabel nbMoveRemaining;
	private JButton undoButton;
	private JButton resetTurnButton;
	private JButton endTurnButton;
	
	public GamePanel(Stadium stadium) {
		super(new BorderLayout());
		this.stadium = stadium;
		this.arkadiaNews = new ArkadiaNews(stadium);
		this.add(this.arkadiaNews, BorderLayout.CENTER);
		
		this.setMargin();
		this.createGameControlPanel();
	}
	
	public ArkadiaNews getArkadiaNews() {
		return this.arkadiaNews;
	}
	
	public void updateGamePanelInfos() {
		this.nbTurn.setText("Tour " + (this.stadium.getTurnIndex() + 1) + " :");
		this.whosturn.setText(this.stadium.getCurrentTeamTurn().getName() + ", à vous !");
		this.nbPassRemaining.setText("Passe : " + (1 - this.stadium.getNbPassesDone()));
		this.nbMoveRemaining.setText("Déplacements : " + (2 - this.stadium.getNbMovesDone()));
	}

	@Override
	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void notify(Object object) {
		for(Observer observer : observers) {
			observer.update(object);
		}
	}
	
	private int getNbTeam() {
		return this.stadium.getTurnIndex() % 2;
	}
	
	public void showEndGamePopUp(String teamName) {
		int input = JOptionPane.showOptionDialog(null, "The team \"" + teamName + "\" won the game!", "Game over", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		
		if (input == JOptionPane.OK_OPTION || input == JOptionPane.CANCEL_OPTION || input == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
	}
	
	public void showAntiPlayPopUp(String teamName) {
		int input = JOptionPane.showOptionDialog(null, "The enemy team made an antiplay: The team \"" + teamName + "\" won the game!", "Game over", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
		
		if (input == JOptionPane.OK_OPTION || input == JOptionPane.CANCEL_OPTION || input == JOptionPane.CLOSED_OPTION) {
			System.exit(0);
		}
	}
	
	private void setMargin() {
		JPanel northPanel = new JPanel();
		northPanel.setBorder(new EmptyBorder(new Insets(this.getWidth()/20, this.getWidth()/20, this.getWidth()/20, 0)));
		this.add(northPanel, BorderLayout.NORTH);
		
		JPanel southPanel = new JPanel();
		southPanel.setBorder(new EmptyBorder(new Insets(this.getWidth()/20, this.getWidth()/20, this.getWidth()/20, 0)));
		this.add(southPanel, BorderLayout.SOUTH);
		
		JPanel westPanel = new JPanel();
		westPanel.setBorder(new EmptyBorder(new Insets(this.getWidth()/20, this.getWidth()/20, this.getWidth()/20, 0)));
		this.add(westPanel, BorderLayout.WEST);
	}
	
	private void createGameControlPanel() {
		this.gameControlPanel = new JPanel(new GridBagLayout());
		
		this.createNbTurnLabel();
		this.createWhoSTurnLabel();
		this.createNbPassRemainingLabel();
		this.createNbMoveRemainingLabel();
		this.createUndoButton();
		this.createResetTurnButton();
		this.createEndTurnButton();
		
		this.add(this.gameControlPanel, BorderLayout.EAST);
	}
	
	private void createNbTurnLabel() {
		// Création et placement du JLabel annonçant le numéro du tour
		this.nbTurn = new JLabel("Tour " + (this.stadium.getTurnIndex() + 1) + " :");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(this.getWidth()/20, this.getWidth()/20, this.getWidth()/20, this.getWidth()/20);
		gbc.anchor = GridBagConstraints.CENTER;
		
		this.gameControlPanel.add(this.nbTurn, gbc);
	}
	
	private void createWhoSTurnLabel() {
		// Création et placement du JLabel annonçant à quelle équipe jouer
		this.whosturn = new JLabel(this.stadium.getCurrentTeamTurn().getName() + ", à vous !");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(0, this.getWidth()/20, this.getWidth()/20, this.getWidth()/20);
		gbc.anchor = GridBagConstraints.CENTER;
		
		this.gameControlPanel.add(this.whosturn, gbc);
	}
	
	private void createNbPassRemainingLabel() {
		// Création et placement du JLabel annonçant le nombre de passes restantes
		this.nbPassRemaining = new JLabel("Passe : " + (1 - this.stadium.getNbPassesDone()));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.ipady = this.getHeight()/5;
		gbc.anchor = GridBagConstraints.WEST;
		
		this.gameControlPanel.add(this.nbPassRemaining, gbc);
	}
	
	private void createNbMoveRemainingLabel() {
		// Création et placement du JLabel annonçant le nombre de déplacements restants
		this.nbMoveRemaining = new JLabel("Déplacements : " + (2 - this.stadium.getNbMovesDone()));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.ipady = this.getHeight()/5;
		gbc.anchor = GridBagConstraints.WEST;
		
		this.gameControlPanel.add(this.nbMoveRemaining, gbc);
	}
	
	private void createUndoButton() {
		// Création et placement du JButton permettant le retour en arrière
		this.undoButton = new JButton(" << ");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.ipady = this.getHeight()/5;
		gbc.anchor = GridBagConstraints.CENTER;
		
		this.gameControlPanel.add(this.undoButton, gbc);
	}
	
	private void createResetTurnButton() {
		// Création et placement du JButton permettant l'annulation du tour
		this.resetTurnButton = new JButton(" Reset ");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.ipady = this.getHeight()/5;
		gbc.anchor = GridBagConstraints.CENTER;
		
		this.gameControlPanel.add(this.resetTurnButton, gbc);
	}
	
	private void createEndTurnButton() {
		// Création et placement du JButton permettant la fin du tour
		this.endTurnButton = new JButton("Fin du tour !");
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		gbc.gridheight = 2;
		gbc.ipady = (this.getHeight()/5) * 2;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		this.gameControlPanel.add(this.endTurnButton, gbc);
		
		// Ajout de la fonction "observeur/observé" au endTurnButton permettant de notifier la partie controller
		this.endTurnButton.addActionListener(actionEvent -> notify(ActionType.END_TURN));
	}
}
