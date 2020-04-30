package controller.AI;
import java.util.Random;
import model.Stadium;
import model.Player;

class RandomAI_1 extends PlayerType {
	Random randomgene;

	RandomAI_1(int numberEquip, Stadium stade) {
		super(numberEquip, stade);
		randomgene = new Random();
	}

	@Override
	boolean timeOut() {
		int passes = 1; //number of passes remaining
		int moves = 2; //number of moves remaining
		int ball = ballNumber(); //number of player with ball
		int exMoveNumber = -1; //number of first move player
		int exMoveDirection = -1; //direction of first move player
		
		Player[] equip; //player list of this equip
		if(equipNumber() == 0)
			equip = stadium.getSnowKids();
		else
			equip = stadium.getShadows();

		int random1 = randomgene.nextInt(11); //choose: 0-end, 1..5-pass, 6..10-move
		int random2; //second random number
		
		while(random1 != 0   &&   (moves != 0  ||  passes != 0)){
			if(random1 < 6   &&   passes != 0   &&   passNumber() != 0){
				//pass
				//random player selection
				random1 = randomgene.nextInt(equip.length);
				
				while(random1 == ball   ||   !canPass(equip[ball],equip[random1]))
					random1 = (random1+1) % (equip.length); //not using random to haven't got infinity circle
				
				//act
				stadium.pass(equip[ball],equip[random1]);
				passes--;
				
			}else if(random1 > 5   &&   moves != 0   &&   movePlayerNumber() != 0){
				 //move
				 
				 if(exMoveNumber == -1   ||   movePlayerNumber() != 1   ||   ball == exMoveNumber   ||   moveNumber(equip[exMoveNumber]) != 1){
					//first move or second move and can move other of back
				 
					//random player selection
					random1 = randomgene.nextInt(equip.length);
					
					while(random1 == ball  ||   !canMove(equip[random1])   ||   (exMoveNumber != -1   &&   random1 == exMoveNumber   &&   moveNumber(equip[exMoveNumber]) == 1))
						random1 = (random1+1) % (equip.length); //not using random to haven't got infinity circle
					
					//random direction choose
					random2 = randomgene.nextInt(4);
					
					while((random2 == 0   &&   !canLeft(equip[random1]))   ||   (random2 == 1   &&   !canUp(equip[random1]))   ||   (random2 == 2   &&   !canRight(equip[random1]))   ||   (random2 == 3   &&   !canDown(equip[random1]))   ||   (random1 == exMoveNumber   &&   random2 == (exMoveDirection+2) % 4))
						random2= (random2+1) % 4; //not using random to haven't got infinity circle
					
					//act
					if(random2 == 0)
						stadium.move(equip[random1],'L');
					else if(random2==1)
						stadium.move(equip[random1],'U');
					else if(random2==2)
						stadium.move(equip[random1],'R');
					else
						stadium.move(equip[random1],'D');
				}
				
					moves--;
			}
						
			random1 = randomgene.nextInt(11); //nextAction
		}
		
		return true;
	}
	
	public static void main(String args[]){
		//tests
	}
}
