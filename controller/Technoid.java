package controller;

import controller.ai.BallActionAI_1;
import controller.listeners.MouseAction;
import model.Action;
import model.Stadium;
import model.enums.TeamPosition;
import model.enums.UserInput;
import patterns.Observer;
import view.HoloTV;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Technoid implements Observer {

    private final HoloTV holoTV;
    private Stadium stadium;

    public Technoid(HoloTV holoTV, Stadium stadium) {
        this.holoTV = holoTV;
        this.stadium = stadium;
    }

    @Override
    public void update(Object object) {
        switch ((UserInput) object) {
            case CLICKED_PLAY: // context : MainMenuPanel
                holoTV.switchToGameModePanel();
                break;

            case CLICKED_SETTINGS: // context : MainMenuPanel
                System.out.println("Functionality not yet implemented");
                break;

            case CLICKED_QUIT: // context : MainMenuPanel
                holoTV.stopMusic();
                holoTV.getFrame().dispose();
                break;

            case CLICKED_PVP: // context : GameModePanel
                MouseAction mouseActionNoAI = new MouseAction(holoTV, stadium, false);
                holoTV.addArkadiaNewsMouseListener(mouseActionNoAI);
                holoTV.getGamePanel().addObserver(mouseActionNoAI);
                holoTV.switchToGamePanel();
                break;

            case CLICKED_PVC: // context : GameModePanel
                MouseAction mouseActionWithAI = new MouseAction(holoTV, stadium, true);
                holoTV.addArkadiaNewsMouseListener(mouseActionWithAI);
                holoTV.getGamePanel().addObserver(mouseActionWithAI);
                holoTV.switchToGamePanel();
                break;

            case CLICKED_CVC: // context : GameModePanel
                holoTV.switchToGamePanel();

                BallActionAI_1 AI1 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.TOP));
                BallActionAI_1 AI2 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));

                Timer timer = new Timer(2000, new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ArrayList<Action> AI1Actions = AI1.randomPlay();
                        for(Action currentAction : AI1Actions) {
                            stadium.actionPerformedAI(currentAction);
                        }

                        ArrayList<Action> AI2Actions = AI2.play(1);
                        for(Action currentAction : AI2Actions) {
                            stadium.actionPerformedAI(currentAction);
                        }

                        holoTV.getArkadiaNews().repaint();
                    }
                });

                timer.start();

                // TODO : somehow finish a game
                break;
        }
    }
}
