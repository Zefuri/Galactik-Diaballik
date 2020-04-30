package controller.AI;
import model.Stadium;
import model.Player;

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

	//return Player list represent equip
	public Player[] equip(){
		Player[] equip;
		
		if(equipNumber() == 0)
			equip = stadium.getSnowKids();
		else
			equip = stadium.getShadows();
			
		return equip;
	}

	//return player number with ball
	public int ballNumber(){
		int number = -1;
		
		Player[] equip = equip();
		
		for(int check = 0; check != equip.length   &&   number == -1; check++){
			if(equip[check].getBallPossession())
				number = check;
		}
		
		return number;
	}

	//return true if playerOne can make pass at playerTwo
	public boolean canPass(Player playerOne, Player playerTwo){
		boolean can = false;
		
		if(stadium.pass(playerOne, playerTwo))
			can = stadium.pass(playerTwo, playerOne);
			
		return can;
	}
	
	//number of possibility pass
	public int passNumber(){
		int number = 0;
		
		Player[] equip = equip();
		
		int ball = ballNumber();
		
		for(int check = 0; check != equip.length; check++){
			if(check != ball   &&   canPass(equip[ball], equip[check]))
				number++;
		}
		
		return number;
	}
	
	//return true if player can move up
	public boolean canUp(Player player){
		boolean can = false;
		
		if(stadium.move(player,'U'))
			can = stadium.move(player,'D');
			
		return can;
	}

	//return true if player can move down	
	public boolean canDown(Player player){
		boolean can = false;
		
		if(stadium.move(player,'D'))
			can = stadium.move(player,'U');
			
		return can;
	}

	//return true if player can move left	
	public boolean canLeft(Player player){
		boolean can = false;
		
		if(stadium.move(player,'L'))
			can=stadium.move(player,'R');
		
		return can;
	}
	
	//return true if player can move right
	public boolean canRight(Player player){
		boolean can = false;
		
		if(stadium.move(player,'R'))
			can=stadium.move(player,'L');
		
		return can;
	}
	
	//return number of moves that can make player
	public int moveNumber(Player player){
		int number = 0;
		
		if(canUp(player))		number++;
		if(canDown(player))	number++;
		if(canLeft(player))		number++;
		if(canRight(player))	number++;
		
		return number;
	}
	
	//return number of totally moves
	public int moveNumber(){
		int number = 0;
		
		Player[] equip;
		if(equipNumber() == 0)
			equip = stadium.getSnowKids();
		else
			equip = stadium.getShadows();
		
		int ball = ballNumber();
		
		for(int check = 0; check != equip.length; check++){
			if(check != ball)
				number += moveNumber(equip[check]);
		}
		
		return number;
	}
	
	//return true if player can move
	public boolean canMove(Player player){
		return moveNumber(player) != 0;
	}
	
	//return number of players can move
	public int movePlayerNumber(){
		int number = 0;
		
		Player[] equip;
		if(equipNumber() == 0)
			equip = stadium.getSnowKids();
		else
			equip = stadium.getShadows();
		
		int ball = ballNumber();
		
		for(int check = 0; check != equip.length; check++){
			if(check != ball   &&   canMove(equip[check]))
				number++;
		}
		
		return number;
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
