import controller.MouseAction;
import model.Stadium;
import view.HoloTV;

public class GalactikDiaballik {
	public static void main() {
		Stadium stadium = new Stadium();
		HoloTV gameWindow = new HoloTV(stadium);
		MouseAction mouseAction = new MouseAction(gameWindow, stadium);
		
		gameWindow.run();
	}
}
