package controller;

import model.enums.UserInput;
import patterns.Observer;
import view.HoloTV;

public class Technoid implements Observer {

    private HoloTV holoTV;

    public Technoid(HoloTV holoTV) {
        this.holoTV = holoTV;
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
                System.out.println("user chose pvp");
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
