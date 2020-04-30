import controller.Listeners.MouseAction;
import model.Stadium;
import view.HoloTV;

public class GalactikDiaballik {
	public static void main(String[] args) {
		Stadium stadium = new Stadium();
		HoloTV holoTV = new HoloTV(stadium);
		MouseAction mouseAction = new MouseAction(holoTV, stadium);

		holoTV.run();
		holoTV.addArkadiaNewsMouseListener(mouseAction);
	}
}
