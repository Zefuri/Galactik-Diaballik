package view;

import javax.sound.sampled.*;
import javax.swing.JFrame;

import javax.swing.SwingUtilities;

import controller.ai.BallActionAI_1;
import controller.listeners.MouseAction;

import model.Stadium;
import model.enums.ActionResult;
import model.enums.GameResult;

import model.enums.TeamPosition;
import patterns.Observer;

import java.awt.event.MouseListener;
import java.io.*;

public class HoloTV implements Runnable {
	private JFrame frame;
	private SourceDataLine audioLine;

	private MainMenuPanel mainMenuPanel;
	private GameModePanel gameModePanel;
	private GamePanel gamePanel;
	private EndGamePanel endGamePanel;

	private Stadium stadium;

	public HoloTV(Stadium stadium) {
		this.stadium = stadium;

		// create the main menu panel
		this.mainMenuPanel = new MainMenuPanel();

		// create the game mode selection panel
		this.gameModePanel = new GameModePanel();

		// Create the GamePanel and himself create and add an ArkadiaNews
		this.gamePanel = new GamePanel(stadium);

		// Create the end game panel
		this.endGamePanel = new EndGamePanel();
	}

	public JFrame getFrame() {
		return frame;
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	@Override
	public void run() {
		// Create the main window
		this.frame = new JFrame("Galactik Diaballik");

		// Add the panel to the frame
		this.frame.add(this.mainMenuPanel);

		// When red X is clicked
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Give it a default size and lets roll
		frame.setSize(950, 740);
		frame.setVisible(true);

		playMusic();

	}

	/*
	function reads the music using SourceDataLine. Other method uses Clip class
	This method uses less memory as it doesn't load the entire audio file but reads it progressively.
	However, their no extensive control on the audio playback
	 */
	private void playMusic() {
		try {
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream("/resources/musics/full_soundtrack.wav"));
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			audioLine = (SourceDataLine) AudioSystem.getLine(info);

			audioLine.open(format);
			audioLine.start();

			int BUFFER_SIZE = 4096;

			byte[] bytesBuffer = new byte[BUFFER_SIZE];
			int bytesRead;

			while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
				audioLine.write(bytesBuffer, 0, bytesRead);
			}

			audioLine.drain();
			audioLine.close();
			audioStream.close();
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void stopMusic() {
		audioLine.stop();
	}

	public void addArkadiaNewsMouseListener(MouseAction mouseAction) {
		//We suppress the already existant mouseListeners
		MouseListener[] mouseListeners = this.gamePanel.getArkadiaNews().getMouseListeners();
		for (MouseListener mouseListener : mouseListeners) {
			this.gamePanel.getArkadiaNews().removeMouseListener(mouseListener);
		}

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
	public void addObserverEndGamePanel(Observer observer) {
		endGamePanel.addObserver(observer);
	}

	/*
	clear the frame entirely and then add the game mode selection panel
	 */
	public void switchToGameModePanel() {
		frame.getContentPane().removeAll();
		frame.add(gameModePanel);
		frame.validate();  // very important
		SwingUtilities.updateComponentTreeUI(frame);
	}

	public void switchToGamePanel() {
		frame.getContentPane().removeAll();
		frame.add(gamePanel);
		frame.validate();  // very important
		SwingUtilities.updateComponentTreeUI(frame);
	}

	public void switchToMainMenuPanel() {
		frame.getContentPane().removeAll();
		frame.add(mainMenuPanel);
		frame.validate();  // very important
		SwingUtilities.updateComponentTreeUI(frame);
	}

	public void switchToEndGamePanel(GameResult gameResult, String teamName) {
		// create the end game panel
		this.endGamePanel.setGameResult(gameResult, teamName);

		// replacing the current panel
		frame.getContentPane().removeAll();
		frame.add(endGamePanel);
		frame.validate();
		SwingUtilities.updateComponentTreeUI(frame);
	}

	public boolean switchToGoodPanel(ActionResult actionResult, BallActionAI_1 AI) {
		boolean panelSwitched = false;

		if (actionResult == ActionResult.WIN) {
			if (stadium.getCurrentTeamTurn() == stadium.getTeam(TeamPosition.BOTTOM) && AI != null) {
				this.switchToEndGamePanel(GameResult.DEFEAT, stadium.getTeam(TeamPosition.TOP).getName());
			} else {
				this.switchToEndGamePanel(GameResult.VICTORY, stadium.getCurrentTeamTurn().getName());
			}
			stadium.clearSelectedPlayer();
			panelSwitched = true;
		}

		if (actionResult == ActionResult.ANTIPLAY_CURRENT && AI != null) {
			this.switchToEndGamePanel(GameResult.DEFEAT_ANTIPLAY, stadium.getCurrentTeamTurn().getName());
			stadium.clearSelectedPlayer();
			panelSwitched = true;
		} else if (actionResult == ActionResult.ANTIPLAY_CURRENT) {
			this.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getNotPlayingTeam().getName());
			stadium.clearSelectedPlayer();
			panelSwitched = true;
		} else if (actionResult == ActionResult.ANTIPLAY) {
			this.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, stadium.getCurrentTeamTurn().getName());
			stadium.clearSelectedPlayer();
			panelSwitched = true;
		}

		return panelSwitched;
	}
}
