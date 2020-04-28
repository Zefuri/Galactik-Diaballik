import controller.MouseAction;
import model.Stadium;
import view.GameWindow;

public class GalactikDiaballik {
	public static void main() {
		Stadium stadium = new Stadium();
		GameWindow gameWindow = new GameWindow(stadium);
		MouseAction mouseAction = new MouseAction(gameWindow, stadium);
		
		gameWindow.run();
	}
}
