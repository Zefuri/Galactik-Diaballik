package view;

import javax.sound.sampled.*;
import javax.swing.JFrame;
import controller.listeners.MouseAction;
import model.Stadium;
import model.enums.GameResult;
import patterns.Observer;

import static java.lang.Thread.sleep;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

public class HoloTV implements Runnable {
	private JFrame frame;
	private Stadium stadium;

	private MainMenuPanel mainMenuPanel;
	private GameModePanel gameModePanel;
	private GamePanel gamePanel;
	private EndGamePanel endGamePanel;
	
	public HoloTV(Stadium stadium) {
		this.stadium = stadium;

		// create the main menu panel
		this.mainMenuPanel = new MainMenuPanel();

		// create the game mode selection panel
		this.gameModePanel = new GameModePanel();

		// Create the GamePanel and himself create and add an ArkadiaNews
		this.gamePanel = new GamePanel(stadium);
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
        frame.setSize(950, 800);
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
			SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

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
	public void addObserverEndGamePanel(Observer observer) {
		gameModePanel.addObserver(observer);
	}

	/*
	clear the frame entirely and then add the game mode selection panel
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
	
	public void switchToEndGamePanel(GameResult gameResult, String teamName) {
		// create the end game panel
		this.endGamePanel = new EndGamePanel(gameResult, teamName);
		
		// replacing the current panel
		frame.getContentPane().removeAll();
		frame.add(endGamePanel);
		frame.validate();
	}
}
