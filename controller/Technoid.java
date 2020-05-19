import listeners.MouseAction;
import model.Stadium;
import model.enums.UserInput;
import patterns.Observer;
import saver.GameLoader;
import saver.GameSaver;
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

            case CLICKED_LOAD: { // context : MainMenuPanel
            	GameLoader gameLoader = new GameLoader(stadium);
            	
            	if (gameLoader.loadData()) {
	            	stadium.loadTopTeam(gameLoader.getTopTeam());
	            	stadium.loadBotTeam(gameLoader.getBotTeam());
	            	
	            	GameSaver gameSaver = new GameSaver(stadium, gameLoader.getCurrentSavePath());
	            	
	                MouseAction mouseAction = new MouseAction(holoTV, stadium, gameSaver);
	                holoTV.addArkadiaNewsMouseListener(mouseAction);
	                holoTV.getGamePanel().addObserver(mouseAction);
	                holoTV.switchToGamePanel();
            	} else {
            		System.err.println("Either the user has cancelled the loading, or an error has occurred.");
            	}
            	
                break;
            }

            case CLICKED_QUIT: // context : MainMenuPanel
                holoTV.getFrame().dispose();
                break;

            case CLICKED_PVP: // context : GameModePanel
            	//We also add the gameSaver and save the initial state
            	GameSaver gameSaver = new GameSaver(stadium);
            	gameSaver.saveToFile();
            	
                MouseAction mouseAction = new MouseAction(holoTV, stadium, gameSaver);
                holoTV.addArkadiaNewsMouseListener(mouseAction);
                holoTV.getGamePanel().addObserver(mouseAction);
                holoTV.switchToGamePanel();
                break;

            case CLICKED_PVC: // context : GameModePanel
                System.out.println("user chose pvc");
                // TODO : create a MouseAction with an IA
                break;

            case CLICKED_CVC: // context : GameModePanel
                System.out.println("user chose cvc");
                // TODO : loop on two AIs
                break;
        }
    }
}
