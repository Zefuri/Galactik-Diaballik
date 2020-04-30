package AI;
import model.Stadium;

// Commun class for all player: AI or human
abstract class PlayerType {
	Stadium stadium;
	int equipNum;

	PlayerType(int number, Stadium stade) {
		equipNum = number;
		stadium = stade;
	}

	int equipNumber() {
		return equipNum;
	}

	//For AI
	boolean timeOut() {
		return false;
	}

	//For human
	boolean play(int abs, int ord) {
		return false;
	}
}
