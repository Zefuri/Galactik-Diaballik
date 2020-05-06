//package controller.ai;

import java.util.Random;
import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;

class RandomAI_1 extends PlayerType {
	Random randomgene;

	public RandomAI_1(Stadium stade, TeamPosition position) {
		super(stade, position);
		randomgene = new Random();
	}

	@Override
	boolean timeOut() {
	
		int passes = 1; //number of passes remaining
		int moves = 2; //number of moves remaining
		Player firstPlayerMove = null; //player make the first move
		MoveDirection firstMoveDirection = null; //direction of first move player

		int chooseAct = randomgene.nextInt(11); //choose: 0-end, 1..5-pass, 6..10-move
		int numberOfPlayerChoose;
		Player playerChoose;
		int numberOfDirectionChoose;
		MoveDirection directionChoose;
		
		boolean firstAct = true; //You can't pass your turn
		
		while((firstAct   ||   chooseAct != 0)   &&   (moves != 0  ||  passes != 0)){
			if(chooseAct < 6   &&   passes != 0   &&   team.numberOfPossibilityPass() != 0){
				//pass
				
				//random player selection
				numberOfPlayerChoose = randomgene.nextInt(team.size());
				playerChoose = team.playerOfInt(numberOfPlayerChoose);
				while(playerChoose.hasBall()   ||   !stadium.playerCanPass(team.getBallPlayer(), playerChoose)){
					numberOfPlayerChoose = (numberOfPlayerChoose+1) % (team.size()); //not using random to haven't got infinity circle
					playerChoose = team.playerOfInt(numberOfPlayerChoose);
				}
				//act
				stadium.pass(team.getBallPlayer(),playerChoose);
				passes--;
				
			}else if(chooseAct > 5   &&   moves != 0   &&   team.movePlayerNumber() != 0){
				 //move
				 
				 if(firstPlayerMove == null   ||   team.movePlayerNumber() != 1   ||   firstPlayerMove.hasBall()   ||   team.playerMovesNumber(firstPlayerMove) != 1) {
					//first move or can choose other player or can move other of back
				 
					//random player selection
					numberOfPlayerChoose = randomgene.nextInt(team.size());
					playerChoose = team.playerOfInt(numberOfPlayerChoose);
					
					while(playerChoose.hasBall()  ||   !stadium.playerCanMove(playerChoose)   ||   (firstPlayerMove != null   &&   playerChoose.equals(firstPlayerMove)   &&   team.playerMovesNumber(playerChoose) == 1)) {
						numberOfPlayerChoose = (numberOfPlayerChoose+1) % (team.size()); //not using random to haven't got infinity circle
						playerChoose = team.playerOfInt(numberOfPlayerChoose);
					}
					
					//random direction choose
					numberOfDirectionChoose = randomgene.nextInt(4);
					switch(numberOfDirectionChoose){
						case 0:
							directionChoose = MoveDirection.UP;
							break;
						case 1:
							directionChoose = MoveDirection.RIGHT;
							break;
						case 2:
							directionChoose = MoveDirection.DOWN;
							break;
						case 3:
							directionChoose = MoveDirection.LEFT;
							break;
			            default:
            			    throw new IllegalStateException("Failure in random");
					}
					
					while(!stadium.playerCanMove(playerChoose, directionChoose)   ||   (firstPlayerMove != null   &&   playerChoose.equals(firstPlayerMove)   &&   directionChoose == firstMoveDirection)) {
						numberOfDirectionChoose = (numberOfDirectionChoose+1) % 4; //not using random to haven't got infinity circle
						switch(numberOfDirectionChoose){
							case 0:
								directionChoose = MoveDirection.UP;
								break;
							case 1:
								directionChoose = MoveDirection.RIGHT;
								break;
							case 2:
								directionChoose = MoveDirection.DOWN;
								break;
							case 3:
								directionChoose = MoveDirection.LEFT;
								break;
					        default:
		        			    new IllegalStateException("Wrong input direction");
						}
					}
					
					//act
					stadium.move(playerChoose,directionChoose);
					firstPlayerMove = playerChoose;
					firstMoveDirection = directionChoose;						
				}
				
					moves--;
			}
				
			firstAct = false;
			chooseAct = randomgene.nextInt(11); //nextAction
		}
		
		return true;
}
	
	public static void main(String args[]){
		//tests
		Stadium stadium = new Stadium();
		RandomAI_1 player1 = new RandomAI_1(stadium, TeamPosition.BOTTOM);
		RandomAI_1 player2 = new RandomAI_1(stadium, TeamPosition.TOP);

		System.out.println(stadium.toString());
		
		int COUPMAX = 20;
		Random r = new Random();
		int coups = r.nextInt(COUPMAX)+1;
		System.out.println("On va jouer "+coups+" coups");
		boolean b;
		for(int i=0; i!=coups; i++){
			b = player1.timeOut();
			System.out.println(stadium.toString());
			b = player2.timeOut();
			System.out.println(stadium.toString());
		}
	}
}
