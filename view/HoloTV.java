package view;

import javax.swing.JFrame;
import listeners.MouseAction;
import model.Stadium;
import patterns.Observer;

import static java.lang.Thread.sleep;

public class HoloTV implements Runnable {
	private JFrame frame;
	private Stadium stadium;

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	private MainMenuPanel mainMenuPanel;
	private GameModePanel gameModePanel;
	private GamePanel gamePanel;
	
	public HoloTV(Stadium stadium) {
		this.stadium = stadium;
	}

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void run() {
		// Create the main window
		this.frame = new JFrame("Galactik Diaballik");

		// create the main menu panel
		mainMenuPanel = new MainMenuPanel();

		// create the game mode selection panel
		gameModePanel = new GameModePanel();

		// Create the GamePanel and himself create and add an ArkadiaNews
		this.gamePanel = new GamePanel(stadium);

		// Add the panel to the frame
		this.frame.add(this.mainMenuPanel);
		
		// When red X is clicked
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Give it a default size and lets roll
        frame.setSize(900, 810);
        frame.setVisible(true);
	}
	
	public void addArkadiaNewsMouseListener(MouseAction mouseAction) {
		this.gamePanel.getArkadiaNews().addMouseListener(mouseAction);
	}
	
	public ArkadiaNews getArkadiaNews() {
		return this.gamePanel.getArkadiaNews();
	}
	
	public void updateGameInfos() {
		this.gamePanel.updateGamePanelInfos();
	}
	
	public int getCaseSize() {
		return this.gamePanel.getArkadiaNews().getCaseSize();
	}
	
	public int getScreenWidth() {
		return this.frame.getWidth();
	}
	
	public int getScreenHeight() {
		return this.frame.getHeight();
	}

	public void addObserverMainMenuPanel(Observer observer) {
		mainMenuPanel.addObserver(observer);
	}

	public void addObserverGameModePanel(Observer observer) {
		gameModePanel.addObserver(observer);
	}

	/*
	clear the frame entirely and add the game mode selection panel
	 */
	public void switchToGameModePanel() {
		frame.getContentPane().removeAll();
		frame.add(gameModePanel);
		frame.validate();  // very important
	}

	public void switchToGamePanel() {
		frame.getContentPane().removeAll();
		frame.add(gamePanel);
		frame.validate();  // very important
	}
}
