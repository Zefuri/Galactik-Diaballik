package controller;

import controller.listeners.MouseAction;
import model.Stadium;
import model.enums.UserInput;
import patterns.Observer;
import view.HoloTV;

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
                break;

            case CLICKED_CVC: // context : GameModePanel
                System.out.println("user chose cvc");
                break;
        }
    }
}
