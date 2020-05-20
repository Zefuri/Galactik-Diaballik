package view;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.enums.GameResult;
import model.enums.UserInput;

import patterns.Observable;
import patterns.ObservableHandler;
import patterns.Observer;

public class EndGamePanel extends JPanel implements Observable {
	private ObservableHandler observableHandler;
	private final VisualResources visualResources = VisualResources.getInstance();
	
	private String teamName;
	
	private GridBagConstraints gbc;
	
	private JLabel resultLabel;
	private JLabel resultDescriptionLabel;
	private JButton mainMenuButton;
	private JButton newGameButton;
	private JButton replayButton;
	
	public EndGamePanel() {
		super(new GridBagLayout());
		
		this.observableHandler = new ObservableHandler();
		this.gbc = new GridBagConstraints();
	}
	
	public void initialize(GameResult gameResult, String teamName) {
		this.setGameResult(gameResult, teamName);
		this.createReplayButton();
		this.createNewGameButton();
		this.createMainMenuButton();
	}
	
	public void setGameResult(GameResult gameResult, String teamName) {
		this.teamName = teamName;
		
		switch (gameResult) {
		case DEFEAT:
			this.resultLabel = new JLabel("Défaite ...");
			this.resultDescriptionLabel = new JLabel("Les " + this.teamName + " se sont inclinés face à leurs adversaires ...");
			
			this.resultLabel.setForeground(this.visualResources.customRed);
			break;
			
		case DEFEAT_ANTIPLAY:
			this.resultLabel = new JLabel("Défaite ...");
			this.resultDescriptionLabel = new JLabel("Les " + this.teamName + " ont fait un antijeu les menant à leur perte ...");
			
			this.resultLabel.setForeground(this.visualResources.customRed);
			break;
			
		case VICTORY:
			this.resultLabel = new JLabel("VICTOIRE !");
			this.resultDescriptionLabel = new JLabel("Les " + this.teamName + " ont été supérieurs à leurs adversaires !");
			
			this.resultLabel.setForeground(this.visualResources.customBlue);
			break;
			
		case VICTORY_ANTIPLAY:
			this.resultLabel = new JLabel("VICTOIRE !");
			this.resultDescriptionLabel = new JLabel("Les " + this.teamName + " ont gagné suite à l'antijeu de leurs adversaires !");
			
			this.resultLabel.setForeground(this.visualResources.customBlue);
			break;
		}
		
		this.resultLabel.setFont(this.visualResources.customFontSuperItal.deriveFont(80f));
		this.resultLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
		
		this.resultDescriptionLabel.setFont(this.visualResources.customFontItal);
		this.resultDescriptionLabel.setBorder(new EmptyBorder(0, 0, 0, 20));
		
		this.gbc.gridx = 0;
		this.gbc.gridy = 0;
		this.gbc.gridwidth = 2;
		this.gbc.anchor = GridBagConstraints.CENTER;
		
		this.add(this.resultLabel, this.gbc);
		
		this.gbc.gridx = 0;
		this.gbc.gridy = 1;
		this.gbc.gridwidth = 2;
		this.gbc.insets = new Insets(0, 0, 50, 0);
		this.gbc.anchor = GridBagConstraints.CENTER;
		
		this.add(this.resultDescriptionLabel, this.gbc);
	}
	
	private void createReplayButton() {
		this.replayButton = new JButton("Revoir la partie");
		this.replayButton.setFont(this.visualResources.customFontItal);
		this.replayButton.setPreferredSize(new Dimension(300, 50));
		
		this.gbc.gridx = 0;
		this.gbc.gridy = 2;
		this.gbc.gridwidth = 1;
		this.gbc.insets = new Insets(0, 0, 50, 20);
		this.gbc.fill = GridBagConstraints.BOTH;
		
		this.add(this.replayButton, this.gbc);
		
		this.replayButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_REWIND));
	}
	
	private void createNewGameButton() {
		this.newGameButton = new JButton("Nouvelle partie");
		this.newGameButton.setFont(this.visualResources.customFontItal);
		this.newGameButton.setPreferredSize(new Dimension(300, 50));
		
		this.gbc.gridx = 1;
		this.gbc.gridy = 2;
		this.gbc.gridwidth = 1;
		this.gbc.insets = new Insets(0, 0, 50, 0);
		this.gbc.fill = GridBagConstraints.BOTH;

		this.add(this.newGameButton, this.gbc);
		
		this.newGameButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_PLAY));
	}
	
	private void createMainMenuButton() {
		this.mainMenuButton = new JButton("Menu principal");
		this.mainMenuButton.setFont(this.visualResources.customFontItal);
		
		this.gbc.gridx = 0;
		this.gbc.gridy = 3;
		this.gbc.gridwidth = 2;
		this.gbc.fill = GridBagConstraints.BOTH;
		
		this.add(this.mainMenuButton, this.gbc);
		
		this.mainMenuButton.addActionListener(actionEvent -> notify(UserInput.CLICKED_MAIN_MENU));
	}
	
	@Override
	public void addObserver(Observer observer) {
		observableHandler.addObserver(observer);
	}

	@Override
	public void notify(Object object) {
		observableHandler.notify(object);
	}
}