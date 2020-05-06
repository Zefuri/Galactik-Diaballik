package controller.ai;
import model.Stadium;
import model.Team;
import model.Player;
import model.enums.MoveDirection;
import model.enums.TeamPosition;
import model.ModelConstants;

class MaxMinBall {
//--------------------------------------------------- Parametres ---------------------------------------------------
	int checkingDepth;
	Stadium stadium;
	Team team;
	Player ballPlayer;
	
	String[] one;
	int[] oneHigh;
	String[] two;
	int[] twoHigh;
	String[] three;
	int[] threeHigh;
	
	String[] mustActs;
	int mustAvancement;

//---------------------------------------------------------------------------------  Constructeur ------------------------------------------------------------
	public MaxMinBall(Stadium stadium, Team team, int depth) {
		checkingDepth = depth;
		this.stadium = stadium;
		this.team = team;
		
		ballPlayer = this.team.getBallPlayer();

		this.initOne();
		this.initTwo();
//		this.initThree();
		
//		mustAvancement = this.Progress();
//		this.initMustActs();
		
		System.out.println("ONE:");
		for(int i = 0; i != one.length; i++){
			System.out.print(one[i]+"_");
			System.out.print(""+oneHigh[i]);
			System.out.print(" - ");
		}
		System.out.println("");
		System.out.println("One size: "+one.length);
		System.out.println("");
		
		System.out.println("TWO:");
		for(int i = 0; i != two.length; i++){
			System.out.print(two[i]+"_");
			System.out.print(""+twoHigh[i]);
			System.out.print(" - ");
		}
		System.out.println("");
		System.out.println("Two size: "+two.length);
		System.out.println("");
		
/**		System.out.println("THREE:");
		for(int i = 0; i != three.length; i++){
//			System.out.print(three[i]+"_");
			System.out.print(""+threeHigh[i]);
			System.out.print(" - ");
		}*/
//		System.out.println("Three size: "+three.length);

//		System.out.println(" The better act with depth "+depth+" give progress: "+mustAvancement);
//		System.out.println(" For this you can: ");
//		for(int i = 0; i != mustActs.length; i++)
//			System.out.print(mustActs[i]+" - ");
//		System.out.println("");
	}
//--------------------------------------------------- initialisations ----------------------------------------------------------------
	public void initOne(){		
		one = new String[team.numberOfPossibilityPass() + team.movePlayerNumber()];
		oneHigh = new int[one.length];
		int oneNum = 0;
		int numberOfPlayer = -1;
		
		for(Player p : team.getPlayers()) {
			numberOfPlayer++;
		
			//pass
			if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)) {
				one[oneNum] = ""+numberOfPlayer+"P";
				oneHigh[oneNum] = ballAvance(p.getPosition().getX());
				oneNum++;
			}
			
			//depl
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
				one[oneNum] = ""+numberOfPlayer+"U";
				oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
				one[oneNum] = ""+numberOfPlayer+"D";
				oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
				one[oneNum] = ""+numberOfPlayer+"L";
				oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
			if(!p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
				one[oneNum] = ""+numberOfPlayer+"R";
				oneHigh[oneNum] = ballAvance(ballPlayer.getPosition().getX());
				oneNum++;
			}
		}
	}
	
	public void initTwo(){
		String stockage = "";
		String stockageHigh = "";
		int numberOfPlayer;
		
		Player ballPlayer2;
		boolean verifPass;
		boolean sameFirstPlayer;
		boolean verifBack;
		
		for(int actLook = 0; actLook != one.length; actLook++) {
		
			numberOfPlayer = -1;
			exec(one[actLook]);
			
				ballPlayer2 = team.getBallPlayer();
				verifPass = one[actLook].charAt(1) != 'P'; //You can't make two pass
				
				if(verifPass){
				//depl + pass or depl + depl

					for(Player p : team.getPlayers()) {
						numberOfPlayer++;
						
						if(!p.equals(ballPlayer)   &&   stadium.playerCanPass(ballPlayer, p)){
							stockage += one[actLook]+numberOfPlayer+"P";
							stockageHigh += p.getPosition().getX();
						}
						
						sameFirstPlayer = numberOfPlayer == (int)(one[actLook].charAt(0)-'0');
						
						verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('U'); //You can't back
						if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.UP)) {
							stockage += one[actLook]+numberOfPlayer+"U";
							stockageHigh += ballPlayer.getPosition().getX();
						}
						
						verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('D'); //You can't back
						if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.DOWN)) {
							stockage += one[actLook]+numberOfPlayer+"D";
							stockageHigh += ballPlayer.getPosition().getX();
						}
						
						verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('L');	 //You can't back
						if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.LEFT)) {
							stockage += one[actLook]+numberOfPlayer+"L";
							stockageHigh += ballPlayer.getPosition().getX();
						}
						
						verifBack = !sameFirstPlayer   ||   one[actLook].charAt(1) != reverse('R');	 //You can't back
						if(verifBack   &&   !p.equals(ballPlayer)   &&   stadium.playerCanMove(p, MoveDirection.RIGHT)) {
							stockage += one[actLook]+numberOfPlayer+"R";
							stockageHigh += ballPlayer.getPosition().getX();
						}
						
					}
				}else{
				//pass + depl: only if depl is to the last player with ball		
						String ballNum = ""+ballPlayer.getName().charAt(ballPlayer.getName().length()-1);
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.UP)){
							stockage += one[actLook]+ballNum+"U";
							stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.DOWN)){
							stockage += one[actLook]+ballNum+"D";
							stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.LEFT)){
							stockage += one[actLook]+ballNum+"L";
							stockageHigh += ballPlayer2.getPosition().getX();
						}
						
						if(stadium.playerCanMove(ballPlayer, MoveDirection.RIGHT)){
							stockage += one[actLook]+ballNum+"R";
							stockageHigh += ballPlayer2.getPosition().getX();
						}
						
				}
			undo(one[actLook]);
		}
	
		two = new String[stockage.length()/4];
		twoHigh = new int[stockageHigh.length()];
		
		for(int i = 0; i != stockage.length(); i += 4){
			two[i/4] = ""+stockage.charAt(i)+stockage.charAt(i+1)+stockage.charAt(i+2)+stockage.charAt(i+3);
			twoHigh[i/4] = ballAvance((int)(stockageHigh.charAt(i/4)-'0'));
		}
	}
	
/*	public void initThree(){
		String stockage = "";
		String stockageHigh = "";
		int ball2;
		boolean verifBack;
		boolean verifPast;
		boolean verifCondition;
		
		for(int actLook = 0; actLook != two.length; actLook++){
		
			exec(two[actLook]);
				ball2 = tools.ballNumber();
				for(int check = 0; check != tools.equip().length; check++){
				
					if(two[actLook].charAt(1) == 'P'){
						//pass + depl +depl: same condition of 2 = if depl is to the last player with ball
						
						verifPast = check != (int)(two[actLook].charAt(2)-'0');
						
						verifBack = verifPast   ||   two[actLook].charAt(3) != reverse('U'); //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canUp(tools.equip()[check])){
							stockage += two[actLook]+check+"U";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						verifBack = verifPast   ||   two[actLook].charAt(3) != reverse('D'); //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canDown(tools.equip()[check])){
							stockage += two[actLook]+check+"D";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						verifBack = verifPast   ||   two[actLook].charAt(3) != reverse('L'); //You can't back	
						if(verifBack   &&   check != ball2   &&   tools.canLeft(tools.equip()[check])){
							stockage += two[actLook]+check+"L";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						verifBack = verifPast   ||   two[actLook].charAt(3) != reverse('R'); //You can't back	
						if(verifBack   &&   check != ball2   &&   tools.canRight(tools.equip()[check])){
							stockage += two[actLook]+check+"R";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
					}else if(two[actLook].charAt(3) == 'P'){
						//depl + pass + depl: only if the pass is to the player moved and now you move the player with ball
						verifCondition = (two[actLook].charAt(0) == two[actLook].charAt(2));
						if(verifCondition){
							if(check == ball   &&   tools.canUp(tools.equip()[check])){
								stockage += two[actLook]+check+"U";
								stockageHigh += tools.equip()[ball2].getI();
							}
							
							if(check == ball   &&   tools.canDown(tools.equip()[check])){
								stockage += two[actLook]+check+"D";
								stockageHigh += tools.equip()[ball2].getI();
							}
							
							if(check == ball   &&   tools.canLeft(tools.equip()[check])){
								stockage += two[actLook]+check+"L";
								stockageHigh += tools.equip()[ball2].getI();
							}
							
							if(check != ball   &&   tools.canRight(tools.equip()[check])){
								stockage += two[actLook]+check+"R";
								stockageHigh += tools.equip()[ball2].getI();
							}
						}
						
					}else{
						//depl + depl + pass
						
						if(check != ball   &&   tools.canPass(tools.equip()[ball], tools.equip()[check])){
							stockage += two[actLook]+check+"P";
							stockageHigh += tools.equip()[check].getI();
						}
						
					}
				}
			undo(two[actLook]);
			
			three = new String[stockage.length()/6];
			threeHigh = new int[stockageHigh.length()];
			
			for(int look = 0; look != stockage.length(); look += 6){
				three[look/6] = ""+stockage.charAt(look)+stockage.charAt(look+1)+stockage.charAt(look+2)+stockage.charAt(look+3)+stockage.charAt(look+4)+stockage.charAt(look+5);
				threeHigh[look/6] = ballAvance((int)(stockageHigh.charAt(look/6)-'0'));
			}
		}
	}

	public void initMustActs(){
		int size = numberOfMust();
		mustActs = new String[size];
		size = 0;
		for(int look = 1; size != mustActs.length   &&   look != 1+one.length+two.length+three.length; look++){
		
			if(look-1 < one.length   &&   mustAvancement == oneHigh[look-1]){
				mustActs[size] = one[look-1];
				size++;
				
			}else if(-1 < look-1-one.length   &&   look-1-one.length < two.length   &&   mustAvancement == twoHigh[look-1-one.length]){
				mustActs[size] = two[look-1-one.length];
				size++;
				
			}else if(-1 < look-1-one.length-two.length   &&   mustAvancement == threeHigh[look-1-one.length-two.length]){
				mustActs[size] = three[look-1-one.length-two.length];
				size++;
			}
		}
	}*/
	
//--------------------------------------------------------- MoveDirection Tools ------------------------------------------------------------
	
	public char reverse(char move){
		if(move == 'U')
			return 'D';
		else if (move == 'R')
			return 'L';
		else if (move == 'D')
			return 'U';
		else
			return 'R';
	}
	
	public MoveDirection moveOfChar(char deplacement) {
		if(deplacement == 'D')
			return MoveDirection.DOWN;
		else if (deplacement == 'L')
			return MoveDirection.LEFT;
		else if (deplacement == 'U')
			return MoveDirection.UP;
		else
			return MoveDirection.RIGHT;
	}
	
//--------------------------------------------------------- Stadium Modifications ------------------------------------------------------------
	
	public void exec(String instruction){
		int number = -1;
		
		for(int i = 0; i != instruction.length(); i += 2){
			number = (int)(instruction.charAt(i)-'0');
			
			if(instruction.charAt(i+1) == 'P')
				stadium.pass(ballPlayer, team.playerOfInt(number));
			else
				stadium.move(team.playerOfInt(number), moveOfChar(instruction.charAt(i+1)));
		}
	}
	
	public void undo(String instruction){
		int number = -1;
		
		for(int i = instruction.length()-1; i != -1; i -= 2){
			number = (int)(instruction.charAt(i-1)-'0');
			
			if(instruction.charAt(i) == 'P')
				stadium.pass(team.playerOfInt(number), ballPlayer);
			else
				stadium.move(team.playerOfInt(number), moveOfChar(reverse(instruction.charAt(i))));
		}
	}

//------------------------------------------------------------- Maximum Evaluation -------------------------------------------------------------

	public int maxList(int[] intList){
		int max = intList[0];
		for(int i = 1; i != intList.length; i++){
			if(max < intList[i])
				max = intList[i];
		}
		return max;
	}
	
	public int max2(int first, int second){
		if(first > second)
			return first;
		return second;
	}
	
	public int max3(int first, int second, int third){
		return max2(max2(first, second), third);
	}

//--------------------------------------------------------- Get --------------------------------------------------------------------
	public String[] getMustActs(){
		return mustActs;
	}
	
	public int getMustAvancement(){
		return mustAvancement;
	}
	
//---------------------------------------------------------- Avancement Check -----------------------------------------------------------------
	
/*	public int Progress(){
		if(checkingDepth != 0){
			MinMaxBall minCheck;
			
			for(int tester = 0; tester != 1+one.length+two.length+three.length; tester++){
				if(tester == 0){
					//you not play
					minCheck = new MinMaxBall(1-player, checkingDepth, stadium);
					zeroHigh = minCheck.Progress();
					
				}else if(tester-1 < one.length){
					//you play one act
					exec(one[tester-1]);
					minCheck = new MinMaxBall(1-player, checkingDepth, stadium);
					oneHigh[tester-1] = minCheck.Progress();
					undo(one[tester-1]);
				
				}else if(tester-1-one.length < two.length){
					//you play two act
					exec(two[tester-1-one.length]);
					minCheck = new MinMaxBall(1-player, checkingDepth, stadium);
					twoHigh[tester-1-one.length] = minCheck.Progress();
					undo(two[tester-1-one.length]);
				
				}else{
					//you play three act
					exec(three[tester-1-one.length-two.length]);
					minCheck = new MinMaxBall(1-player, checkingDepth, stadium);
					threeHigh[tester-1-one.length-two.length] = minCheck.Progress();
					undo(three[tester-1-one.length-two.length]);
				}
			}
			
		}
		
		System.out.println(stadium.toString());
		return maxAvancement();
	}*/
	
	//return the avancement of team ball (6 - ball position if team bottom and 0 + ball position if team top)
	public int teamBallAvance(int ballPosition){
		if(team.getPosition() == TeamPosition.TOP)
			return ballPosition;
		return (ModelConstants.BOARD_SIZE - 1) - ballPosition;
	}
	
	//return the difference between the avancement of two ball
	public int ballAvance(int ballPosition){
		return teamBallAvance(ballPosition) - teamBallAvance(team.getEnemyTeam().getBallPlayer().getPosition().getX());
	}

/*	public int maxAvancement(){
		return max3(maxList(oneHigh), maxList(twoHigh), maxList(threeHigh));
	}
	
	public int numberOfMust(){
		int size = 0;
		
		if(mustAvancement == zeroHigh)
			size++;
			
		for(int look = 1; look != 1+one.length+two.length+three.length; look++){

			if(look-1 < one.length   &&   mustAvancement == oneHigh[look-1]){
				size++;
			}else if(-1 < look-1-one.length   &&   look-1-one.length < two.length   &&   mustAvancement == twoHigh[look-1-one.length]){
				size++;
				
			}else if(-1 < look-1-one.length-two.length   &&   mustAvancement == threeHigh[look-1-one.length-two.length]){
				size++;
			}
		}
		return size;
	}*/

//---------------------------------------------------------- Main ---------------------------------------------------------
	public static void main(String args[]){
		//tests
		Stadium stade = new Stadium();
//		stade.move(stade.getSnowKids()[0],'U');
		MaxMinBall test = new MaxMinBall(stade,stade.getTeam(TeamPosition.BOTTOM),0);
	}
}
