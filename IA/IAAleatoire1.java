package IA;
import java.util.Random;
import model.Stadium;
import model.Player;

class IAAleatoire extends Joueur {
	Random r;

	JoueurIA(int n, Stadium s) {
		super(n, s);
		r = new Random();
	}

	@Override
	boolean tempsEcoule() {
		int p = 1; //number of passes remaining
		int d = 2; //number of moves remaining
		int k = -1; //choose number
		int n = -1; //number of player with ball
		
		//maximum: 2moves & 1pass
		while(k!=0 && (d!=0||p!=0)){
			//choose: 0-end, 1-move, 2-pass
			k = r.nextInt(3);
			if(k==1 && d!=0){
				//equip recuperation
				if(num = 0){
					Player[] equip = stade.getSnowKids();
				}else{
					Player[] equip = stade.getshadows();
				}
				
				//number recuperation of player with ball
				n = -1;
				for(int i=0; i!=equip.length && n==-1; i++){
					if(equip[i].getBallPossession()){
						n=i;
					}
				}
				
			}
		}
		return true;
	}
}
