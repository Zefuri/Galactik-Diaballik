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
import view.Repainter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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
            	
                Thread thread1 = new Thread() {
                    @Override
                    public void run() {
                        holoTV.switchToGamePanel();
                    }
                };
//                holoTV.switchToGamePanel();
                thread1.run();

                BallActionAI_1 AI1 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.TOP));
                BallActionAI_1 AI2 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));

                Thread thread = new Thread() {
                    @Override
                    public void run() {

                        holoTV.getArkadiaNews().repaint();
                    }
                };

                for (int i = 0 ; i < 10; i++) {
                    ArrayList<Action> AI1Actions = AI1.randomPlay();
                    for(Action currentAction : AI1Actions) {
                        stadium.actionPerformedAI(currentAction);

                        thread.run();

                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<Action> AI2Actions = AI2.play(0);
                    for(Action currentAction : AI2Actions) {
                        stadium.actionPerformedAI(currentAction);

                        thread.run();

                        try {
                            sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

//                for (int i = 0; i < 10; i++) {
//                    ArrayList<Action> AI1Actions = AI1.randomPlay();
//                    for(Action currentAction : AI1Actions) {
//                        stadium.actionPerformedAI(currentAction);
//
//                        Repainter.getInstance().isRepainted = false;
//                        holoTV.getArkadiaNews().repaint();
//                        while (!Repainter.getInstance().isRepainted) {
//                            continue;
//                        }
//
//                        try {
//                            sleep(50);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    ArrayList<Action> AI2Actions = AI2.play(0);
//                    for(Action currentAction : AI2Actions) {
//                        stadium.actionPerformedAI(currentAction);
//
//                        Repainter.getInstance().isRepainted = false;
//                        holoTV.getArkadiaNews().repaint();
//                        while (!Repainter.getInstance().isRepainted) {
//                            continue;
//                        }
//
//                        try {
//                            sleep(50);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

//                Timer timer = new Timer(2000, new ActionListener() {
//                    public void actionPerformed(ActionEvent e) {
//                        ArrayList<Action> AI1Actions = AI1.randomPlay();
//                        for(Action currentAction : AI1Actions) {
//                            stadium.actionPerformedAI(currentAction);
//                        }
//
//                        ArrayList<Action> AI2Actions = AI2.play(1);
//                        for(Action currentAction : AI2Actions) {
//                            stadium.actionPerformedAI(currentAction);
//                        }
//
//                        holoTV.getArkadiaNews().repaint();
//                    }
//                });
//
//                timer.start();

                // TODO : somehow finish a game
                break;
			case CLICKED_MAIN_MENU:
				holoTV.switchToMainMenuPanel();
				break;
			case CLICKED_REWIND:
				System.out.println("Fonctionnalité en cours d'implémentation");
				break;
        }
    }
}
