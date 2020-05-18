import listeners.MouseAction;
import model.Stadium;
import view.HoloTV;

public class GalactikDiaballik {
	public static void main(String[] args) {
		Stadium stadium = new Stadium();
		HoloTV holoTV = new HoloTV(stadium);
		Technoid technoid = new Technoid(holoTV, stadium);

		holoTV.run();
		holoTV.addObserverGameModePanel(technoid);
		holoTV.addObserverMainMenuPanel(technoid);
	}
}
