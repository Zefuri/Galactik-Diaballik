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

	//return player number with ball
	public int ballNumber(){
		int n=-1;
		
		Player[] equip;
		if(num == 0){
			equip = stadium.getSnowKids();
		}else{
			equip = stadium.getShadows();
		}
		
		for(int i=0; i!=equip.length && n==-1; i++){
			if(equip[i].getBallPossession()){
				n=i;
			}
		}
		
		return n;
	}

	//return true if playerOne can make pass at playerTwo
	public boolean canPass(Player playerOne, Player playerTwo){
		boolean b = false;
		if(stadium.pass(playerOne, playerTwo)){
			b = stadium.pass(playerTwo, playerOne);
		}
		return b;
	}
	
	//number of possibility pass
	public int passNumber(){
		int n = 0;
		Player[] equip;
		if(num == 0){
			equip = stadium.getSnowKids();
		}else{
			equip = stadium.getShadows();
		}
		
		int ball = ballNumber();
		
		for(int i=0; i!=equip.length; i++){
			if(i!=ball && canPass(equip[ball], equip[i])){
				n++;
			}
		}
		return n;
	}
	
	//return true if player can move up
	public boolean canUp(Player player){
		boolean b = false;
		if(stadium.move(player,'U')){
			b=stadium.move(player,'D');
		}
		return b;
	}

	//return true if player can move down	
	public boolean canDown(Player player){
		boolean b = false;
		if(stadium.move(player,'D')){
			b=stadium.move(player,'U');
		}
		return b;
	}

	//return true if player can move left	
	public boolean canLeft(Player player){
		boolean b = false;
		if(stadium.move(player,'L')){
			b=stadium.move(player,'R');
		}
		return b;
	}
	
	//return true if player can move right
	public boolean canRight(Player player){
		boolean b = false;
		if(stadium.move(player,'R')){
			b=stadium.move(player,'L');
		}
		return b;
	}
	
	//return number of moves that can make player
	public int moveNumber(Player player){
		int n = 0;
		if(canUp(player)){n++;}
		if(canDown(player)){n++;}
		if(canLeft(player)){n++;}
		if(canRight(player)){n++;}
		return n;
	}
	
	//return true if player can move
	public boolean canMove(Player player){
		return moveNumber(player) != 0;
	}
	
	//return number of players can move
	public int movePlayerNumber(){
		int n = 0;
		Player[] equip;
		if(num == 0){
			equip = stadium.getSnowKids();
		}else{
			equip = stadium.getShadows();
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
		
		Player[] equip; //player list of this equip
		if(num == 0){
			equip = stadium.getSnowKids();
		}else{
			equip = stadium.getShadows();
		}

		//choose: 0-end, 1-pass, 2-move
		int k = r.nextInt(3);
		int l; //second random number
		while(k!=0 && (d!=0||p!=0)){
			if(k==1 && p!=0 && passNumber()!=0){ //pass
				//random player selection
				k = r.nextInt(equip.length);
				while(k==ball || !canPass(equip[ball],equip[k])){
					k = r.nextInt(equip.length);
				}
				//act
				stadium.pass(equip[ball],equip[k]);
				p--;
			}else if(k==2 && d!=0 && movePlayerNumber()!=0){ //move
				//random player selection
				k = r.nextInt(equip.length);
				while(k==ball || !canMove(equip[k])){
					k = r.nextInt(equip.length);
				}
				//random direction choose
				l = r.nextInt(4);
				while((l==0 && !canLeft(equip[k]))||(l==1 && !canUp(equip[k]))||(l==2 && !canRight(equip[k]))||(l==3 && !canDown(equip[k]))){
					l= r.nextInt(4);
				}
				//act
				if(l==0){
					stadium.move(equip[k],'L');
				}else if(l==1){
					stadium.move(equip[k],'U');
				}else if(l==2){
					stadium.move(equip[k],'R');
				}else{
					stadium.move(equip[k],'D');
				}
				d--;
			}
			k = r.nextInt(3); //nextAction
		}
		return true;
	}
}
