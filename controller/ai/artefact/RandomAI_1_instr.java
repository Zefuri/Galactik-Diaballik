package controller.ai;

import java.util.Random;
import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;

public class RandomAI_1_instr extends PlayerType {
	Random randomgene;

	public RandomAI_1_instr(Stadium stade, TeamPosition position) {
		super(stade, position);
		randomgene = new Random();
	}

	@Override
	boolean timeOut() {
	
		System.out.println(team.getName()+" :");
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
				System.out.println("Passe: "+team.getBallPlayer().getName()+" -> "+playerChoose.getName());
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
					System.out.println("Move: "+playerChoose.getName()+" "+directionChoose);
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
	}
}
