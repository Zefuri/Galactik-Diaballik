package saver;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.swing.JFrame;

import model.Case;
import model.Player;
import model.Stadium;
import model.Team;
import model.enums.TeamPosition;

public class GameLoader {
	private static final String savesPath = System.getProperty("user.dir") + "\\saves\\";
	private String currentSavePath;
	
	private Stadium stadium;
	
	private Team topTeam, botTeam;
	
	public GameLoader(Stadium stadium) {
		this.stadium = stadium;
		this.currentSavePath = null;
		this.topTeam = null;
		this.botTeam = null;
	}
	
	public void loadData() {
		//We create the 'saves' directory if it does not exist
		try {
			Path path = Paths.get(savesPath);
			Files.createDirectories(path);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//We generate a save selection window
		FileDialog fileDialog = new FileDialog(new JFrame(), "Please select a save to load.", FileDialog.LOAD);
		fileDialog.setDirectory(savesPath);
		fileDialog.setVisible(true);
		
		String fileName = fileDialog.getFile();
		String filePath = fileDialog.getDirectory() + fileName;
		
		if (fileName == null) {
			System.out.println("User cancelled the choice.");
			return;
		}
		
		this.currentSavePath = filePath;
		
		//We open the save file
		File save = new File(filePath);
	    
		//We read the file and fill the needed informations
        Scanner scanner = null;
        
		try {
			scanner = new Scanner(save);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
     
        if (scanner == null) throw new RuntimeException();
        
        this.topTeam = new Team("snowKids", TeamPosition.TOP, this.stadium, false);
        this.botTeam = new Team("shadows", TeamPosition.BOTTOM, this.stadium, false);
        
        int currLine = 0;
        
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            int index = 0; 
            int currColumn = 0;
            String val = "";
            
            while (index < line.length()) {
            	if (line.charAt(index) == ' ') {
            		//We read a space so we go to next column
            		addPlayerIfNeeded(val, currLine, currColumn, topTeam, botTeam);
            		val = "";
            		currColumn++;
            	} else {
            		val += line.charAt(index);
            	}
            	
            	index++;
            }
            
            currLine++;
        }    
        
        scanner.close();
	}
	
	private void addPlayerIfNeeded(String val, int currLine, int currColumn, Team topTeam, Team botTeam) {
		switch (val) {
			case SaverConstants.TOP_TEAM_PLAYER_ALONE: {
				Player player = new Player("TOP_" + currColumn);
				player.setPosition(currLine, currColumn);
				
				topTeam.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
				
			case SaverConstants.TOP_TEAM_PLAYER_WITH_BALL: {
				Player player = new Player("TOP_" + currColumn);
				player.setPosition(currLine, currColumn);
				player.setBallPossession(true);
				
				topTeam.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
			
			case SaverConstants.BOT_TEAM_PLAYER_ALONE: {
				Player player = new Player("BOT_" + currColumn);
				player.setPosition(currLine, currColumn);
				
				botTeam.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
				
			case SaverConstants.BOT_TEAM_PLAYER_WITH_BALL: {
				Player player = new Player("BOT_" + currColumn);
				player.setPosition(currLine, currColumn);
				player.setBallPossession(true);
				
				botTeam.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
				
			default:
				//System.out.println("Pas de joueur sur la " + new Case(currLine, currColumn));
		}
	}
	
	public String getCurrentSavePath() {
		return this.currentSavePath;
	}
	
	public Team getTopTeam() {
		return this.topTeam;
	}
	
	public Team getBotTeam() {
		return this.botTeam;
	}
}
