package controller;

import controller.ai.BallActionAI_1;
import controller.listeners.MouseAction;
import model.Action;
import model.Stadium;
import model.enums.TeamPosition;
import model.enums.UserInput;
import patterns.Observer;
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
        switch ((UserInput) object) {
            case CLICKED_PLAY: // context : MainMenuPanel
                holoTV.switchToGameModePanel();
                break;

            case CLICKED_SETTINGS: // context : MainMenuPanel
                System.out.println("Functionality not yet implemented");
                break;

            case CLICKED_QUIT: // context : MainMenuPanel
                holoTV.getFrame().dispose();
                break;

            case CLICKED_PVP: // context : GameModePanel
                MouseAction mouseAction = new MouseAction(holoTV, stadium);
                holoTV.addArkadiaNewsMouseListener(mouseAction);
                holoTV.getGamePanel().addObserver(mouseAction);
                holoTV.switchToGamePanel();
                break;

            case CLICKED_PVC: // context : GameModePanel
                System.out.println("user chose pvc");
                // TODO : create a MouseAction with an IA
                break;

            case CLICKED_CVC: // context : GameModePanel
                holoTV.switchToGamePanel();
                try {
                    wait(2000);
                } catch (Exception e) {
//                    e.printStackTrace();
                }

                BallActionAI_1 AI1 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.TOP));
                BallActionAI_1 AI2 = new BallActionAI_1(stadium, stadium.getTeam(TeamPosition.BOTTOM));

                for(int i = 0; i < 100; i++) {
                    ArrayList<Action> AI1Actions = AI1.play(1);
                    for(Action currentAction : AI1Actions) {
                        stadium.actionPerformed(currentAction);
                        holoTV.getArkadiaNews().repaint();
                        try {
                            wait(1000);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    }

                    ArrayList<Action> AI2Actions = AI2.play(0);
                    for(Action currentAction : AI2Actions) {
                        stadium.actionPerformed(currentAction);
                        holoTV.getArkadiaNews().repaint();
                        try {
                            wait(1000);
                        } catch (Exception e) {
//                            e.printStackTrace();
                        }
                    }
                }
                break;
        }
    }
}
