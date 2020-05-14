package view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Stadium;
import model.enums.ActionType;

import patterns.Observable;
import patterns.ObservableHandler;
import patterns.Observer;

public class GamePanel extends JPanel implements Observable {
	private final ObservableHandler observableHandler;
	private final VisualResources visualResources = VisualResources.getInstance();

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

		observableHandler = new ObservableHandler();

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
		
		this.changeTeamTurnColor();
		this.whosturn.setText(this.stadium.getCurrentTeamTurn().getName() + ", à vous !");
		
		this.nbPassRemaining.setText("Passe : " + (1 - this.stadium.getNbPassesDone()));
		this.nbMoveRemaining.setText("Déplacements : " + (2 - this.stadium.getNbMovesDone()));
	}
	
	private void changeTeamTurnColor() {
		switch(this.stadium.getCurrentTeamTurn().getName()) {
			case "snowKids" :
				this.whosturn.setForeground(this.visualResources.customBlue);
				break;
			case "shadows" :
				this.whosturn.setForeground(this.visualResources.customRed);
				break;
		}
	}
	
	@Override
	public void addObserver(Observer observer) {
		observableHandler.addObserver(observer);
	}

	@Override
	public void notify(Object object) {
		observableHandler.notify(object);
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
		this.gameControlPanel = new JPanel(new GridLayout(4, 1, 0, this.getWidth()/20));
		
		this.createTurnPanel();
		this.createActionsRemainingPanel();
		this.createTurnButtonsPanel();
		this.createEndTurnButtonPanel();
		
		this.add(this.gameControlPanel, BorderLayout.EAST);
	}
	
	private void createTurnPanel() {
		JPanel turnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Création et placement du JLabel annonçant le numéro du tour
		this.nbTurn = new JLabel("Tour " + (this.stadium.getTurnIndex() + 1) + " :");
		this.nbTurn.setFont(this.visualResources.customFontItal);
		this.nbTurn.setBorder(new EmptyBorder(0, this.getWidth()/20, 0, this.getWidth()/20));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		turnPanel.add(this.nbTurn, gbc);
		
		// Création et placement du JLabel annonçant à quelle équipe jouer
		this.whosturn = new JLabel(this.stadium.getCurrentTeamTurn().getName() + ", à vous !");
		this.whosturn.setFont(this.visualResources.customFontItal);
		this.whosturn.setBorder(new EmptyBorder(0, this.getWidth()/20, 0, this.getWidth()/20));
		
		this.changeTeamTurnColor();
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		turnPanel.add(this.whosturn, gbc);
		
		this.gameControlPanel.add(turnPanel);
	}
	
	private void createActionsRemainingPanel() {
		JPanel actionsRemainingPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Création et placement du JLabel annonçant le nombre de passes restantes
		this.nbPassRemaining = new JLabel("Passe : " + (1 - this.stadium.getNbPassesDone()));
		this.nbPassRemaining.setFont(this.visualResources.customFont);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		
		actionsRemainingPanel.add(this.nbPassRemaining, gbc);
		
		// Création et placement du JLabel annonçant le nombre de déplacements restants
		this.nbMoveRemaining = new JLabel("Déplacements : " + (2 - this.stadium.getNbMovesDone()));
		this.nbMoveRemaining.setFont(this.visualResources.customFont);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.ipady = this.getWidth()/20;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		
		actionsRemainingPanel.add(this.nbMoveRemaining, gbc);
		
		this.gameControlPanel.add(actionsRemainingPanel);
	}
	
	private void createTurnButtonsPanel() {
		JPanel turnButtons = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Création et placement du JButton permettant le retour en arrière
		this.undoButton = new JButton();
		this.undoButton.setIcon(new ImageIcon(this.visualResources.backwardIconImage));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		
		turnButtons.add(this.undoButton, gbc);

		// Création et placement du JButton permettant l'annulation du tour
		this.resetTurnButton = new JButton();
		this.resetTurnButton.setIcon(new ImageIcon(this.visualResources.resetIconImage));
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		
		turnButtons.add(this.resetTurnButton, gbc);

		this.gameControlPanel.add(turnButtons);
	}
	
	private void createEndTurnButtonPanel() {
		JPanel endTurnButtonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Création et placement du JButton permettant la fin du tour
		this.endTurnButton = new JButton("Fin du tour !");
		this.endTurnButton.setFont(this.visualResources.customFontSuperItal);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		endTurnButtonPanel.add(this.endTurnButton, gbc);

		this.gameControlPanel.add(endTurnButtonPanel);
		
		// Ajout de la fonction "observeur/observé" au endTurnButton permettant de notifier la partie controller
		this.endTurnButton.addActionListener(actionEvent -> notify(ActionType.END_TURN));
	}
}
