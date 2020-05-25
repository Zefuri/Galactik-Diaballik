import controller.ai.BallActionAI_1;
import controller.listeners.MouseAction;

import model.Action;
import model.Stadium;
import model.enums.ActionResult;
import model.enums.GameResult;
import model.enums.TeamPosition;
import model.enums.UserInput;

import patterns.Observer;

import saver.GameLoader;
import saver.GameSaver;

import view.HoloTV;
import javax.swing.*;
import java.util.ArrayList;


public class Technoid implements Observer {

    private final HoloTV holoTV;
    private Stadium stadium;

    private BallActionAI_1 AI1;
    private BallActionAI_1 AI2;
    private ArrayList<Action> AIActions;
    private Timer timer;
    private boolean firstAIsTurn;

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

                holoTV.switchToGamePanel();

                AI1 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.TOP));
                AI2 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));

                AIActions = new ArrayList<>();
                timer = new Timer(200, new EVETimer(this)); // AIs play every 0.2 seconds
                timer.start();

                break;
			case CLICKED_MAIN_MENU:
				holoTV.switchToMainMenuPanel();
				break;
        }
    }

    /*
    method is called by the timer to make the AIs play in an AI vs AI context
     */
    public void playAI() {
        if (AIActions.size() == 0) { // if it's the beginning of a new turn
            if (firstAIsTurn) { // generate the actions
                AIActions = AI1.play(1);
            } else {
                AIActions = AI2.play(0);
            }
        }

        ActionResult actionResult = stadium.actionPerformedAI(AIActions.get(0)); // perform the first action in queue...
        AIActions.remove(0); // ...and remove it

        holoTV.getArkadiaNews().repaint(); // show the new move
        holoTV.updateGameInfos();

        // test all win scenarios
        if (actionResult == ActionResult.WIN) {
            timer.stop();
            holoTV.switchToEndGamePanel(GameResult.VICTORY, !firstAIsTurn ? stadium.getTeam(TeamPosition.TOP).getName() : stadium.getTeam(TeamPosition.BOTTOM).getName());
        }

        if (actionResult == ActionResult.ANTIPLAY) {
            timer.stop();
            holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, firstAIsTurn ? stadium.getTeam(TeamPosition.TOP).getName() : stadium.getTeam(TeamPosition.BOTTOM).getName());
        }

        if (actionResult == ActionResult.ANTIPLAY_CURRENT) {
            timer.stop();
            holoTV.switchToEndGamePanel(GameResult.VICTORY_ANTIPLAY, firstAIsTurn ? stadium.getTeam(TeamPosition.BOTTOM).getName() : stadium.getTeam(TeamPosition.TOP).getName());
        }

        // when no more actions are left, switch player
        if (AIActions.size() == 0) {
            firstAIsTurn = !firstAIsTurn;
        }
    }
}
