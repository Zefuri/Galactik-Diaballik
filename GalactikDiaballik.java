import model.Stadium;
import view.HoloTV;
import controller.Technoid;

public class GalactikDiaballik {
	public static void main(String[] args) {
		Stadium stadium = new Stadium();
		HoloTV holoTV = new HoloTV(stadium);
		Technoid technoid = new Technoid(holoTV, stadium);
		
		holoTV.addObserverMainMenuPanel(technoid);
		holoTV.addObserverGameModePanel(technoid);
		holoTV.addObserverEndGamePanel(technoid);
		
		holoTV.run(); // must be the last line
	}
}
