package controller;

import controller.ai.BallActionAI_1;
import controller.listeners.MouseAction;

import model.Action;
import model.Stadium;
import model.enums.TeamPosition;
import model.enums.UserInput;

import patterns.Observer;

import saver.GameLoader;
import saver.GameSaver;

import view.HoloTV;

import java.util.ArrayList;

import static java.lang.Thread.sleep;


public class Technoid implements Observer {

    private final HoloTV holoTV;
    private Stadium stadium;

    public Technoid(HoloTV holoTV, Stadium stadium) {
        this.holoTV = holoTV;
        this.stadium = stadium;
    }

    @Override
    public void update(Object object) {
    	GameSaver gameSaver;
    	
        switch ((UserInput) object) {
            case CLICKED_PLAY: // context : MainMenuPanel
                holoTV.switchToGameModePanel();
                break;

            case CLICKED_LOAD: { // context : MainMenuPanel
            	GameLoader gameLoader = new GameLoader(stadium);
            	
            	if (gameLoader.loadData()) {
	            	stadium.loadTopTeam(gameLoader.getTopTeam());
	            	stadium.loadBotTeam(gameLoader.getBotTeam());
	            	
	            	gameSaver = new GameSaver(stadium, gameLoader.getCurrentSavePath());
	            	
	                MouseAction mouseAction = new MouseAction(holoTV, stadium, false, gameSaver);
	                holoTV.addArkadiaNewsMouseListener(mouseAction);
	                holoTV.getGamePanel().addObserver(mouseAction);
	                holoTV.switchToGamePanel();
            	} else {
            		System.err.println("Either the user has cancelled the loading, or an error has occurred.");
            	}
            	
                break;
            }
            
            case CLICKED_VISU: {
            	GameLoader gameLoader = new GameLoader(stadium);
            	
            	if (gameLoader.loadData()) {
	            	stadium.loadTopTeam(gameLoader.getTopTeam());
	            	stadium.loadBotTeam(gameLoader.getBotTeam());

	            	stadium.replaceTeam();
	            	
	            	stadium.getHistory().setToFirstTurn();
	            	
	            	stadium.setVisualisaionMode(true);
	            
	                MouseAction mouseAction = new MouseAction(holoTV, stadium, false, null);
	                holoTV.addArkadiaNewsMouseListener(mouseAction);
	                holoTV.getGamePanel().addObserver(mouseAction);
	                holoTV.switchToGamePanel();
	                holoTV.getGamePanel().overwriteComponents();
	                
	                //TODO Si besoin mettre le stadium et la holotv en mode visu
            	} else {
            		System.err.println("Either the user has cancelled the loading, or an error has occurred.");
            	}
            	
            	break;
            }

            case CLICKED_QUIT: // context : MainMenuPanel
                holoTV.stopMusic();
                holoTV.getFrame().dispose();
                break;

            case CLICKED_PVP: // context : GameModePanel
            	//We also add the gameSaver and save the initial state
            	gameSaver = new GameSaver(stadium);
            	gameSaver.saveToFile();
            	
                MouseAction mouseActionNoAI = new MouseAction(holoTV, stadium, false, gameSaver);
                
                stadium.resetStadium();
                holoTV.addArkadiaNewsMouseListener(mouseActionNoAI);
                holoTV.getGamePanel().addObserver(mouseActionNoAI);
                holoTV.updateGameInfos();
                holoTV.switchToGamePanel();
                break;

              case CLICKED_PVC: // context : GameModePanel
                //We also add the gameSaver and save the initial state
                gameSaver = new GameSaver(stadium);
                gameSaver.saveToFile();

                  MouseAction mouseActionWithAI = new MouseAction(holoTV, stadium, true, gameSaver);

                  stadium.resetStadium();
                  holoTV.addArkadiaNewsMouseListener(mouseActionWithAI);
                  holoTV.getGamePanel().addObserver(mouseActionWithAI);
                  holoTV.updateGameInfos();
                  holoTV.switchToGamePanel();
                  break;

              case CLICKED_CVC: // context : GameModePanel
                stadium.resetStadium();
                holoTV.updateGameInfos();
                  break;
            
              case CLICKED_MAIN_MENU:
                holoTV.switchToMainMenuPanel();
                break;
                }
    }
}
