package saver;

import java.awt.Desktop;
import java.awt.FileDialog;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Action;
import model.Case;
import model.Player;
import model.Stadium;
import model.Team;
import model.Turn;
import model.enums.ActionType;
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
        this.stadium.getHistory().removeLastTurn();
        
        int currLine = 0;
        
        while(scanner.hasNextLine()) {
            String line = scanner.nextLine();

            if (currLine == 0) {
            	//We read the board
            	String board;
            	
            	try {
					board = new JSONObject(line).getString("board");
				} catch (NoSuchElementException e) {
					e.printStackTrace();
					return;
				} catch (ParseException e) {
					e.printStackTrace();
					return;
				}
            	
	            int index = 0; 
	            int currColumn = 0;
	            int realLine = 0;
	            int topPlayerIndex = 0;
	            int botPlayerIndex = 0;
	            String val = "";
	            
	            while (index < board.length()) {
	            	if (board.charAt(index) == ' ') {
	            		//We read a space so we go to next column
	            		if (val.matches("[P][A][1][0-9]") || val.matches("[P][B][1][0-9]")) {
	            			addPlayerIfNeeded(val, realLine, currColumn, topTeam, topPlayerIndex);
	            			topPlayerIndex++;
	            		} else if (val.matches("[P][A][2][0-9]") || val.matches("[P][B][2][0-9]")) {
	            			addPlayerIfNeeded(val, realLine, currColumn, botTeam, botPlayerIndex);
	            			botPlayerIndex++;
	            		}
	            		
	            		val = "";
	            		currColumn++;
	            	} else if (board.charAt(index) == '\n') { 
	            		//We read a newLine character so we go to next line and first column
	            		currColumn = 0;
	            		realLine++;
	            	} else {
	            		val += board.charAt(index);
	            	}
	            	
	            	index++;
	            }
            } else {
            	//We read all the saved turns
            	try {
					JSONObject jsonTurn = new JSONObject(line);
					
					int turnIndex = jsonTurn.getInt("Indice");
					int nbMovesDone = jsonTurn.getInt("NbMovesDone");
					int nbPassesDone = jsonTurn.getInt("NbPassesDone");
					TeamPosition teamPosition = (jsonTurn.getString("Team").equals("TOP") ? TeamPosition.TOP : TeamPosition.BOTTOM);
					Team currTeam = (teamPosition == TeamPosition.TOP ? this.topTeam : this.botTeam);
					
					JSONArray jsonActions = jsonTurn.getJSONArray("Actions");
					
					JSONObject jsonAction1 = new JSONObject(jsonActions.getString(0));
					JSONObject jsonAction2 = new JSONObject(jsonActions.getString(1));
					JSONObject jsonAction3 = new JSONObject(jsonActions.getString(2));
					
					//We create a turn with the current team
					Turn tour = new Turn(currTeam);
					
					//tour.setNbMovesDone(nbMovesDone);
					//tour.setNbPassesDone(nbPassesDone);
					
					//We create a new action if it exists, and we add it to the current tour
					if (jsonAction1.length() != 0) {
						ActionType typeAction1 = (jsonAction1.getString("Type").equals("MOVE") ? ActionType.MOVE : ActionType.PASS);
						Case caseDepart1 = new Case(jsonAction1.getString("CaseDepart"));
						Case caseArrivee1 = new Case(jsonAction1.getString("CaseArrivee"));
						String nomJoueurDepart1 = jsonAction1.getString("Joueur1");
						String nomJoueurArrivee1 = jsonAction1.getString("Joueur2");
						
						Action action1 = new Action(typeAction1, currTeam.getPlayerFromName(nomJoueurDepart1), currTeam.getPlayerFromName(nomJoueurArrivee1), caseDepart1, caseArrivee1);
						tour.addAction(action1);
						
						if (jsonAction2.length() != 0) {
							ActionType typeAction2 = (jsonAction2.getString("Type").equals("MOVE") ? ActionType.MOVE : ActionType.PASS);
							Case caseDepart2 = new Case(jsonAction2.getString("CaseDepart"));
							Case caseArrivee2 = new Case(jsonAction2.getString("CaseArrivee"));
							String nomJoueurDepart2 = jsonAction2.getString("Joueur1");
							String nomJoueurArrivee2 = jsonAction2.getString("Joueur2");
							
							Action action2 = new Action(typeAction2, currTeam.getPlayerFromName(nomJoueurDepart2), currTeam.getPlayerFromName(nomJoueurArrivee2), caseDepart2, caseArrivee2);
							tour.addAction(action2);
							
							if (jsonAction3.length() != 0) {
								ActionType typeAction3 = (jsonAction3.getString("Type").equals("MOVE") ? ActionType.MOVE : ActionType.PASS);
								Case caseDepart3 = new Case(jsonAction3.getString("CaseDepart"));
								Case caseArrivee3 = new Case(jsonAction3.getString("CaseArrivee"));
								String nomJoueurDepart3 = jsonAction3.getString("Joueur1");
								String nomJoueurArrivee3 = jsonAction3.getString("Joueur2");
								
								Action action3 = new Action(typeAction3, currTeam.getPlayerFromName(nomJoueurDepart3), currTeam.getPlayerFromName(nomJoueurArrivee3), caseDepart3, caseArrivee3);
								tour.addAction(action3);
							}
						}
					}
					
					//we add the tour to the history
					this.stadium.getHistory().addTurn(tour);
					
				} catch (ParseException e) {
					e.printStackTrace();
				}
            }
            
            currLine++;
        }    
        
        scanner.close();
        System.out.println(this.stadium.getHistory());
	}
	
	private void addPlayerIfNeeded(String val, int currLine, int currColumn, Team team, int playerIndex) {
		switch (val.substring(0, val.length() - 1)) {
			case SaverConstants.TOP_TEAM_PLAYER_ALONE: {
				Player player = new Player("TOP_" + val.substring(val.length() - 1));
				player.setPosition(currLine, currColumn);
				
				team.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
				
			case SaverConstants.TOP_TEAM_PLAYER_WITH_BALL: {
				Player player = new Player("TOP_" + val.substring(val.length() - 1));
				player.setPosition(currLine, currColumn);
				player.setBallPossession(true);
				
				team.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
			
			case SaverConstants.BOT_TEAM_PLAYER_ALONE: {
				Player player = new Player("BOT_" + val.substring(val.length() - 1));
				player.setPosition(currLine, currColumn);
				
				team.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
				
			case SaverConstants.BOT_TEAM_PLAYER_WITH_BALL: {
				Player player = new Player("BOT_" + val.substring(val.length() - 1));
				player.setPosition(currLine, currColumn);
				player.setBallPossession(true);
				
				team.addPlayer(player);
				
				//System.out.println(player);
				break;
			}
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
