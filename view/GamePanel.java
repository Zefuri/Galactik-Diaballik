package view;

import model.Stadium;
import model.enums.ActionType;
import patterns.Observable;
import patterns.ObservableHandler;
import patterns.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GamePanel extends JPanel implements Observable {
	private ObservableHandler observableHandler;
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
	private JButton redoButton;
	private JButton endTurnButton;
	private JButton cheatModButton;
	
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
		this.whosturn.setText(this.stadium.getCurrentTeamTurn().getName() + ", a vous !");
		
		this.nbPassRemaining.setText("Passe : " + (1 - this.stadium.getNbPassesDone()));
		this.nbMoveRemaining.setText("Deplacements : " + (2 - this.stadium.getNbMovesDone()));
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
	
	public void showFirstTurnReachedPopup() {
		JOptionPane.showMessageDialog(null, "You reached the first turn of this game!");
	}
	
	public void showLastTurnReachedPopup() {
		JOptionPane.showMessageDialog(null, "You reached the last turn of this game!");
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
		this.gameControlPanel = new JPanel(new GridLayout(5, 1));
		
		this.createTurnPanel();
		this.createActionsRemainingPanel();
		this.createTurnButtonsPanel();
		this.createEndTurnButtonPanel();
		this.createCheatModPanel();
		
		this.add(this.gameControlPanel, BorderLayout.EAST);
	}
	
	private void createTurnPanel() {
		JPanel turnPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Creation et placement du JLabel annoncant le numero du tour
		this.nbTurn = new JLabel("Tour " + (this.stadium.getTurnIndex() + 1) + " :");
		this.nbTurn.setFont(this.visualResources.customFontItal);
		this.nbTurn.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		turnPanel.add(this.nbTurn, gbc);
		
		// Cr�ation et placement du JLabel annon�ant � quelle �quipe jouer
		this.whosturn = new JLabel(this.stadium.getCurrentTeamTurn().getName() + ", � vous !");
		this.whosturn.setFont(this.visualResources.customFontItal);
		this.whosturn.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		this.changeTeamTurnColor();
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		turnPanel.add(this.whosturn, gbc);
		
		this.gameControlPanel.add(turnPanel);
	}
	
	private void createActionsRemainingPanel() {
		JPanel actionsRemainingPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Cr�ation et placement du JLabel annon�ant le nombre de passes restantes
		this.nbPassRemaining = new JLabel("Passe : " + (1 - this.stadium.getNbPassesDone()));
		this.nbPassRemaining.setFont(this.visualResources.customFont);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		
		actionsRemainingPanel.add(this.nbPassRemaining, gbc);
		
		// Creation et placement du JLabel annoncant le nombre de deplacements restants
		this.nbMoveRemaining = new JLabel("Deplacements : " + (2 - this.stadium.getNbMovesDone()));
		this.nbMoveRemaining.setFont(this.visualResources.customFont);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE_LEADING;
		
		actionsRemainingPanel.add(this.nbMoveRemaining, gbc);
		
		this.gameControlPanel.add(actionsRemainingPanel);
	}
	
	private void createTurnButtonsPanel() {
		JPanel turnButtons = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Cr�ation et placement du JButton permettant le retour en arri�re
		this.undoButton = new JButton();
		this.undoButton.setIcon(new ImageIcon(this.visualResources.backwardIconImage));
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 20, 0, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		
		turnButtons.add(this.undoButton, gbc);

		// Cr�ation et placement du JButton permettant l'annulation du tour
		this.resetTurnButton = new JButton();
		this.resetTurnButton.setIcon(new ImageIcon(this.visualResources.resetIconImage));
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 10, 0, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		
		turnButtons.add(this.resetTurnButton, gbc);
		
		//Creation et placement du JButton permettant de rejouer une action (en avant), invisible au depart
		this.redoButton = new JButton();
		this.redoButton.setIcon(new ImageIcon(this.visualResources.forwardIconImage));
		this.redoButton.setVisible(false);
		
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.5;
		
		turnButtons.add(this.redoButton, gbc);

		this.gameControlPanel.add(turnButtons);
		
		this.undoButton.addActionListener(actionEvent -> notify(ActionType.UNDO));
		this.resetTurnButton.addActionListener(actionEvent -> notify(ActionType.RESET));
		this.redoButton.addActionListener(actionEvent -> notify(ActionType.REDO));
	}
	
	private void createEndTurnButtonPanel() {
		JPanel endTurnButtonPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Cr�ation et placement du JButton permettant la fin du tour
		this.endTurnButton = new JButton("Fin du tour !");
		this.endTurnButton.setFont(this.visualResources.customFontSuperItal);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 20, 0, 20);
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;
		
		endTurnButtonPanel.add(this.endTurnButton, gbc);

		this.gameControlPanel.add(endTurnButtonPanel);
		
		// Ajout de la fonction "observeur/observ�" au endTurnButton permettant de notifier la partie controller
		this.endTurnButton.addActionListener(actionEvent -> notify(ActionType.END_TURN));
	}

	private void createCheatModPanel() {
		JPanel cheatModPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		this.cheatModButton = new JButton("Cheat Mod");
		this.cheatModButton.setFont(this.visualResources.customFontSuperItal);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.ABOVE_BASELINE;

		cheatModPanel.add(this.cheatModButton, gbc);

		this.gameControlPanel.add(cheatModPanel);

		this.cheatModButton.addActionListener(actionEvent -> notify(ActionType.CHEAT));
	}

	public void cheatModColorToggle(boolean isCheatMod) {
		if (isCheatMod) {
			this.cheatModButton.setBackground(Color.GREEN);
		} else {
			this.cheatModButton.setBackground(null);
		}
	}
	
	public void overwriteComponents() {
		this.endTurnButton.setVisible(false);
		this.resetTurnButton.setVisible(false);
		this.redoButton.setVisible(true);
	}
}
