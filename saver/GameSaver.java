package saver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Action;
import model.Case;
import model.ModelConstants;
import model.Player;
import model.Stadium;
import model.Turn;
import model.enums.TeamPosition;

public class GameSaver {
	private Stadium stadium;
	
	private static final String savesPath = System.getProperty("user.dir") + "\\saves\\";
	private static final String savePrefix = "save-";
	private static final String saveSuffix = ".sv";
	
	private String currentSavePath;
	
	private int previousTurnIndex;
	
	public GameSaver(Stadium stadium) {
		this.stadium = stadium;
		this.previousTurnIndex = -1;
	}
	
	public GameSaver(Stadium stadium, String currentSavePath) {
		this.stadium = stadium;
		this.currentSavePath = currentSavePath;
		this.previousTurnIndex = -1;
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
							builder.append(SaverConstants.TOP_TEAM_PLAYER_WITH_BALL + currPlayer.getNumero());
						} else {
							builder.append(SaverConstants.TOP_TEAM_PLAYER_ALONE + currPlayer.getNumero());
						}
					} else {
						if (currPlayer.hasBall()) {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_WITH_BALL + currPlayer.getNumero());
						} else {
							builder.append(SaverConstants.BOT_TEAM_PLAYER_ALONE + currPlayer.getNumero());
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
	
	private JSONObject generateJSONBoard() {
		JSONObject jsonBoard = new JSONObject();
		jsonBoard.put("board", this.saveCurrentBoard().toString());
		
		return jsonBoard;
	}
	
	private JSONObject generateJSONTurn() {
		JSONObject jsonTurn = new JSONObject();
		JSONObject jsonAction1 = new JSONObject();
		JSONObject jsonAction2 = new JSONObject();
		JSONObject jsonAction3 = new JSONObject();
		
		Turn tour = this.stadium.getHistory().getLast();
		Action action1 = tour.getFirstAction();
		Action action2 = tour.getSecondAction();
		Action action3 = tour.getThirdAction();
		
		if (action1 != null) {
			jsonAction1.put("Type", action1.getType());
			jsonAction1.put("Joueur1", (action1.getPreviousPlayer() != null) ? action1.getPreviousPlayer().getName() : "");
			jsonAction1.put("Joueur2", (action1.getNextPlayer() != null) ? action1.getNextPlayer().getName() : "");
			jsonAction1.put("CaseDepart", (action1.getPreviousCase() != null) ? action1.getPreviousCase().JSONed() : "");
			jsonAction1.put("CaseArrivee", (action1.getNextCase() != null) ? action1.getNextCase().JSONed() : "");

			if (action2 != null) {
				jsonAction2.put("Type", action2.getType());
				jsonAction2.put("Joueur1", (action2.getPreviousPlayer() != null) ? action2.getPreviousPlayer().getName() : "");
				jsonAction2.put("Joueur2", (action2.getNextPlayer() != null) ? action2.getNextPlayer().getName() : "");
				jsonAction2.put("CaseDepart", (action2.getPreviousCase() != null) ? action2.getPreviousCase().JSONed() : "");
				jsonAction2.put("CaseArrivee", (action2.getNextCase() != null) ? action2.getNextCase().JSONed() : "");
				
				if (action3 != null) {
					jsonAction3.put("Type", action3.getType());
					jsonAction3.put("Joueur1", (action3.getPreviousPlayer() != null) ? action3.getPreviousPlayer().getName() : "");
					jsonAction3.put("Joueur2", (action3.getNextPlayer() != null) ? action3.getNextPlayer().getName() : "");
					jsonAction3.put("CaseDepart", (action3.getPreviousCase() != null) ? action3.getPreviousCase().JSONed() : "");
					jsonAction3.put("CaseArrivee", (action3.getNextCase() != null) ? action3.getNextCase().JSONed() : "");
				}
			}
		}
		
		JSONArray jsonActions = new JSONArray();
		jsonActions.put(jsonAction1.toString());
		jsonActions.put(jsonAction2.toString());
		jsonActions.put(jsonAction3.toString());
		
		jsonTurn.put("Indice", this.stadium.getHistory().getCurrentTurnIndex());
		jsonTurn.put("Team", tour.getTeam().getPosition());
		jsonTurn.put("Actions", jsonActions);
		
		return jsonTurn;
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
		if (this.previousTurnIndex == -1) {
			this.previousTurnIndex = this.stadium.getHistory().getCurrentTurnIndex();
		}
		save(Paths.get(this.currentSavePath));
	}
	
	public void saveToFile() {
		save(Paths.get(this.generateSaveName()));
	}
	
	private void save(Path path) {
		JSONObject jsonBoard = this.generateJSONBoard();
		JSONObject jsonTurn = this.generateJSONTurn();
		
		try {
			Files.createDirectories(path.getParent());
			
			if (!Files.exists(path)) {
				//The save file does not exist: we add the board at the beginning
				Files.write(path, Arrays.asList(jsonBoard.toString()), StandardCharsets.UTF_8, StandardOpenOption.CREATE);
			} else {
				//The save file exists, we replace the first line (board) by the new board
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			    lines.set(0, jsonBoard.toString());
			    Files.write(path, lines, StandardCharsets.UTF_8);
			}
			
			int currentTurnIndex = jsonTurn.getInt("Indice");
			
			if (this.previousTurnIndex == currentTurnIndex) {
				//If the index is the same, we just did a step in the current turn, so we suppress the older version of this turn
				List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
			    lines.set(lines.size() - 1, jsonTurn.toString());
			    Files.write(path, lines, StandardCharsets.UTF_8);
			} else {
				Files.write(path, Arrays.asList(jsonTurn.toString()), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
				this.previousTurnIndex = currentTurnIndex;
			}
			
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
