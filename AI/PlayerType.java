package AI;
import model.Stadium;

// Commun class for all player : AI or human
abstract class PlayerType {
	Stadium stade;
	int num;

	PlayerType(int n, Stadium p) {
		num = n;
		stade = p;
	}

	int num() {
		return num;
	}

	//For AI
	boolean timeout() {
		return false;
	}

	//For humain
	boolean play(int i, int j) {
		return false;
	}
}
