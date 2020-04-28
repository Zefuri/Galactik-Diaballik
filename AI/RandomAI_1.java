package AI;
import java.util.Random;
import model.Stadium;
import model.Player;

class RandomAI_1 extends PlayerType {
	Random r;

	RandomAI_1(int n, Stadium s) {
		super(n, s);
		r = new Random();
	}

	public int ballNumber(){
		int n=-1;
		
		int[] equip;
		if(num == 0){
			equip = getSnowKids();
		}else{
			equip = getShadows();
		}
		
		for(int i=0; i!=equip.length && n==-1; i++){
			if(equip[i].getBallPossession()){
				n=i;
			}
		}
		
		return n;
	}

	public boolean canPass(Player playerOne, Player playerTwo){
		boolean b = false;
		if(stadium.pass(playerOne, playerTwo)){
			b = stadium.pass(playerTwo, playerOne);
		}
		return b;
	}
	
	public int passNumber(){
		int n = 0;
		int[] equip;
		if(num == 0){
			equip = getSnowKids();
		}else{
			equip = getShadows();
		}
		
		int ball = ballNumber();
		
		for(int i=0; i!=equip.length; i++){
			if(i!=ball && canPass(equip[ball], equip[i])){
				n++;
			}
		}
	}
	
	public int moveNumber(Player player){
		int n = 0;
		boolean b;
		if(stadium.move(player,'U')){
			n++;
			b=stadium.move(player,'D');
		}
		if(stadium.move(player,'D')){
			n++;
			b=stadium.move(player,'U');
		}
		if(stadium.move(player,'L')){
			n++;
			b=stadium.move(player,'R');
		}
		if(stadium.move(player,'R')){
			n++;
			b=stadium.move(player,'L');
		}
		return n;
	}
	
	public boolean canMove(Player player){
		return moveNumber(player) != 0;
	}
	
	public int movePlayerNumber(){
		int n = 0;
		int[] equip;
		if(num == 0){
			equip = getSnowKids();
		}else{
			equip = getShadows();
		}
		
		int ball = ballNumber();
		
		for(int i=0; i!=equip.length; i++){
			if(i!=ball && canMove(equip[i])){
				n++;
			}
		}
		return n;
	}

	@Override
	boolean timeOut() {
		int p = 1; //number of passes remaining
		int d = 2; //number of moves remaining
		int ball = ballNumber(); //number of player with ball
		
		int[] equip; //player list of this equip
		if(num == 0){
			equip = getSnowKids();
		}else{
			equip = getShadows();
		}

		//choose: 0-end, 1-pass, 2-move
		int k = r.nextInt(3);
		while(k!=0 && (d!=0||p!=0)){
			if(k==1 && p!=0 && passNumber()!=0){
				k = r.nextInt(passNumber);
				while(!canPass()){}
				p--;
			}else if(k==2 && d!=0 && movePlayerNumber()!=0){
				d--;
			}
			k = r.nextInt(3); //nextAction
		}
		return true;
	}
}
