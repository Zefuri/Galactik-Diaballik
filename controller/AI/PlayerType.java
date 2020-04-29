package AI;
import model.Stadium;

// Commun class for all player: AI or human
abstract class PlayerType {
	Stadium stadium;
	int num;

	PlayerType(int n, Stadium p) {
		num = n;
		stadium = p;
	}

	int num() {
		return num;
	}

	//For AI
	boolean timeOut() {
		return false;
	}

	//For human
	boolean play(int i, int j) {
		return false;
	}
}
