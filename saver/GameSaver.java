package saver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import model.Case;
import model.ModelConstants;
import model.Player;
import model.Stadium;
import model.enums.TeamPosition;

public class GameSaver {
	private Stadium stadium;
	
	private static final String savesPath = System.getProperty("user.dir") + "\\saves\\";
	private static final String savePrefix = "save-";
	private static final String saveSuffix = ".sv";
	
	private String currentSavePath;
	
	public GameSaver(Stadium stadium) {
		this.stadium = stadium;
	}
	
	public GameSaver(Stadium stadium, String currentSavePath) {
		this.stadium = stadium;
		this.currentSavePath = currentSavePath;
		
		System.out.println(currentSavePath);
	}
	
	private StringBuilder saveCurrentBoard() {
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < ModelConstants.BOARD_SIZE; i++) {
			for (int j = 0; j < ModelConstants.BOARD_SIZE; j++) {
				Player currPlayer = this.stadium.getPlayer(new Case(i,j));
				
				if (currPlayer == null) {
					//No player on this case, we add the empty symbol
					builder.append(SaverConstants.EMPTY_CASE);
				} else {
					//We check either the player is on the first team or the second and if he has the ball
					if (currPlayer.getTeam().equals(this.stadium.getTeam(TeamPosition.TOP))) {
						if (currPlayer.hasBall()) {
							builder.append(SaverConstants.TOP_TEAM_PLAYER_WITH_BALL);
						} else {
							builder.append(SaverConstants.TOP_TEAM_PLAYER_ALONE);
						}
					} else {
						if (currPlayer.hasBall()) {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_WITH_BALL);
						} else {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_ALONE);
						}
					}
				}
				
				builder.append(SaverConstants.LISIBILITY_BLANK);
			}
			
			builder.append(SaverConstants.NEXT_LINE);
		}
		
		System.out.println(builder.toString());
		
		return builder;
	}
	
	private String generateSaveName() {
		int index = 1;
		String savePath = savesPath + savePrefix + index + saveSuffix;
		File f = new File(savePath);
		
		while (f.isFile()) { 
		    savePath = savesPath + savePrefix + ++index + saveSuffix;
		    f = new File(savePath);
		}
		
		return savePath;
	}
	
	public void overwriteSave() {
		if (!(new File(this.currentSavePath).delete())) {
			throw new RuntimeException("An error has occurred during the savefile deletion!");
		}
		
		save(Paths.get(this.currentSavePath));
	}
	
	public void saveToFile() {
		save(Paths.get(this.generateSaveName()));
	}
	
	private void save(Path path) {
		StringBuilder board = saveCurrentBoard();
		
		try {
			Files.createDirectories(path.getParent());
			Files.createFile(path);
			
			FileOutputStream fos = new FileOutputStream(path.toString());
			
			fos.write(board.toString().getBytes());
			fos.close();
			
			this.setCurrentSavePath(path.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCurrentSavePath() {
		return this.currentSavePath;
	}
	
	public void setCurrentSavePath(String path) {
		this.currentSavePath = path;
	}
}
