/*//package controller.ai;
import model.Stadium;
import model.Player;
import controller.AI.RandomAI_1;

class MinMaxBall {
//--------------------------------------------------- Parametres ---------------------------------------------------
	int checkingDepth;
	Stadium now;
	int player;
	
	int zeroHigh;
	String[] one;
	int[] oneHigh;
	String[] two;
	int[] twoHigh;
	String[] three;
	int[] threeHigh;
	
	String[] worstActs;
	int worstAvancement;
	
	RandomAI_1 tools;
	int ball;

//---------------------------------------------------------------------------------  Constructeur ------------------------------------------------------------
	public MinMaxBall(int player, int depth, Stadium stade) {
		checkingDepth = depth;
		now = stade;
		this.player = player;
		
		tools = new RandomAI_1(this.player,now);
		ball = tools.ballNumber();

		zeroHigh = this.ballAvance(tools.equip()[ball].getI());
		this.initOne();
		this.initTwo();
		this.initThree();
		
		worstAvancement = this.Progress();
		this.initWorstActs();
	}
//--------------------------------------------------- initialisations ----------------------------------------------------------------
	public void initOne(){		
		one = new String[tools.passNumber() + tools.moveNumber()];
		oneHigh = new int[tools.passNumber() + tools.moveNumber()];
		int oneNum = 0;
		
		for(int check = 0; check != tools.equip().length; check++){
			//pass
			if(check != ball   &&   tools.canPass(tools.equip()[ball], tools.equip()[check])){
				one[oneNum] = ""+check+"P";
				oneHigh[oneNum] = ballAvance(tools.equip()[check].getI());
				oneNum++;
			}
			
			//depl
			if(check != ball   &&   tools.canUp(tools.equip()[check])){
				one[oneNum] = ""+check+"U";
				oneHigh[oneNum] = ballAvance(tools.equip()[ball].getI());
				oneNum++;
			}
			if(check != ball   &&   tools.canDown(tools.equip()[check])){
				one[oneNum] = ""+check+"D";
				oneHigh[oneNum] = ballAvance(tools.equip()[ball].getI());
				oneNum++;
			}
			if(check != ball   &&   tools.canLeft(tools.equip()[check])){
				one[oneNum] = ""+check+"L";
				oneHigh[oneNum] = ballAvance(tools.equip()[ball].getI());
				oneNum++;
			}
			if(check != ball   &&   tools.canRight(tools.equip()[check])){
				one[oneNum] = ""+check+"R";
				oneHigh[oneNum] = ballAvance(tools.equip()[ball].getI());
				oneNum++;
			}
		}
	}
	
	public void initTwo(){
		String stockage = "";
		String stockageHigh = "";
		int ball2;
		boolean verifPass;
		boolean verifPast;
		boolean verifBack;
		
		for(int actLook = 0; actLook != one.length; actLook++){
		
			exec(one[actLook]);
				ball2 = tools.ballNumber();
				for(int check = 0; check != tools.equip().length; check++){
				
					verifPass = one[actLook].charAt(1) != 'P'; //You can't make two pass
					if(verifPass){
						//depl + pass or depl + depl
					
						if(check != ball2   &&   tools.canPass(tools.equip()[ball2], tools.equip()[check])){
							stockage += one[actLook]+check+"P";
							stockageHigh += tools.equip()[check].getI();
						}
						
						verifPast = check != (int)(one[actLook].charAt(0)-'0');
						
						verifBack = verifPast   ||   one[actLook].charAt(1) != reverse('U'); //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canUp(tools.equip()[check])){
							stockage += one[actLook]+check+"U";
							stockageHigh += tools.equip()[ball].getI();
						}
						
						verifBack = verifPast   ||   one[actLook].charAt(1) != reverse('D'); //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canDown(tools.equip()[check])){
							stockage += one[actLook]+check+"D";
							stockageHigh += tools.equip()[ball].getI();
						}
						
						verifBack = verifPast   ||   one[actLook].charAt(1) != reverse('L');	 //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canLeft(tools.equip()[check])){
							stockage += one[actLook]+check+"L";
							stockageHigh += tools.equip()[ball].getI();
						}
						
						verifBack = verifPast   ||   one[actLook].charAt(1) != reverse('R');	 //You can't back
						if(verifBack   &&   check != ball2   &&   tools.canRight(tools.equip()[check])){
							stockage += one[actLook]+check+"R";
							stockageHigh += tools.equip()[ball].getI();
						}
						
					}else{
						//pass + depl: only if depl is to the last player with ball
						
						if(check == ball   &&   tools.canUp(tools.equip()[check])){
							stockage += one[actLook]+check+"U";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						if(check == ball   &&   tools.canDown(tools.equip()[check])){
							stockage += one[actLook]+check+"D";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						if(check == ball   &&   tools.canLeft(tools.equip()[check])){
							stockage += one[actLook]+check+"L";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
						if(check == ball   &&   tools.canRight(tools.equip()[check])){
							stockage += one[actLook]+check+"R";
							stockageHigh += tools.equip()[ball2].getI();
						}
						
					}
				}
			undo(one[actLook]);
			
			two = new String[stockage.length()/4];
			twoHigh = new int[stockageHigh.length()];
			
			for(int look = 0; look != stockage.length(); look += 4){
				two[look/4] = ""+stockage.charAt(look)+stockage.charAt(look+1)+stockage.charAt(look+2)+stockage.charAt(look+3);
				twoHigh[look/4] = ballAvance((int)(stockageHigh.charAt(look/4)-'0'));
			}
		}
	}
	
	public void initThree(){
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


	public void initWorstActs(){
		int size = numberOfWorst();
		worstActs = new String[size];
		size = 0;
		for(int look = 1; size != worstActs.length   &&   look != 1+one.length+two.length+three.length; look++){
		
			if(look-1 < one.length   &&   worstAvancement == oneHigh[look-1]){
				worstActs[size] = one[look-1];
				size++;
				
			}else if(-1 < look-1-one.length   &&   look-1-one.length < two.length   &&   worstAvancement == twoHigh[look-1-one.length]){
				worstActs[size] = two[look-1-one.length];
				size++;
				
			}else if(-1 < look-1-one.length-two.length   &&   worstAvancement == threeHigh[look-1-one.length-two.length]){
				worstActs[size] = three[look-1-one.length-two.length];
				size++;
			}
		}
	}
	
//--------------------------------------------------------- Stadium modifications ------------------------------------------------------------
	
	public void exec(String instruction){
		int number = -1;
		
		for(int look = 0; look != instruction.length(); look += 2){
			number = (int)(instruction.charAt(look)-'0');
			
			if(instruction.charAt(look+1) == 'P')
				now.pass(tools.equip()[ball], tools.equip()[number]);
			else
				now.move(tools.equip()[number], instruction.charAt(look+1));
		}
	}
	
	public char reverse(char deplacement){
		if(deplacement == 'U')
			return 'D';
		else if (deplacement == 'R')
			return 'L';
		else if (deplacement == 'D')
			return 'U';
		else if(deplacement == 'L')
			return 'R';
		else
			return 'P';
	}
	
	public void undo(String instruction){
		int number = -1;
		
		for(int look = instruction.length()-1; look != -1; look -= 2){
			number = (int)(instruction.charAt(look-1)-'0');
			
			if(instruction.charAt(look) == 'P')
				now.pass(tools.equip()[number], tools.equip()[ball]);
			else
				now.move(tools.equip()[number], reverse(instruction.charAt(look)));
		}
	}

//------------------------------------------------------------- Minimum Evaluation -------------------------------------------------------------

	public int minList(int[] intList){
		int min = intList[0];
		for(int look = 1; look != intList.length; look++){
			if(min > intList[look])
				min = intList[look];
		}
		return min;
	}
	
	public int min2(int first, int second){
		if(first < second)
			return first;
		return second;
	}
	
	public int min4(int first, int second, int third, int fourth){
		return min2(min2(first, second), min2(third, fourth));
	}

//--------------------------------------------------------- Get --------------------------------------------------------------------
	public String[] getWorstActs(){
		return worstActs;
	}
	
	public int getWorstAvancement(){
		return worstAvancement;
	}
	
//---------------------------------------------------------- Avancement Check -----------------------------------------------------------------
	
	public int Progress(){
		MaxMinBall maxCheck;
		
		for(int tester = 0; tester != 1+one.length+two.length+three.length; tester++){
			if(tester == 0){
				//you not play
				maxCheck = new MaxMinBall(1-player, checkingDepth-1, now);
				zeroHigh = maxCheck.Progress();
				
			}else if(tester-1 < one.length){
				//you play one act
				exec(one[tester-1]);
				maxCheck = new MaxMinBall(1-player, checkingDepth-1, now);
				oneHigh[tester-1] = maxCheck.Progress();
				undo(one[tester-1]);
			
			}else if(tester-1-one.length < two.length){
				//you play two act
				exec(two[tester-1-one.length]);
				maxCheck = new MaxMinBall(1-player, checkingDepth-1, now);
				twoHigh[tester-1-one.length] = maxCheck.Progress();
				undo(two[tester-1-one.length]);
			
			}else{
				//you play three act
				exec(three[tester-1-one.length-two.length]);
				maxCheck = new MaxMinBall(1-player, checkingDepth-1, now);
				threeHigh[tester-1-one.length-two.length] = maxCheck.Progress();
				undo(three[tester-1-one.length-two.length]);
			}
		}
		
		return minAvancement();
	}
	
	//return 6-ballPosition if Player 0 and return 0+ballPosition if Player 1
	public int ballAvance(int ballPosition){
		RandomAI_1  opponentTools = new RandomAI_1(1-player,now);
		int opponentBallHigh = opponentTools.equip()[opponentTools.ballNumber()].getI();
		if(tools.equipNumber() == 0)
			return (tools.equip().length-1 - ballPosition) - opponentBallHigh;
		return (ballPosition) - (opponentTools.equip().length-1 - opponentBallHigh);
	}

	public int minAvancement(){
		return min4(minList(oneHigh), minList(twoHigh), minList(threeHigh), zeroHigh);
	}
	
	public int numberOfWorst(){
		int size = 0;
		
		if(worstAvancement == zeroHigh)
			size++;
			
		for(int look = 1; look != 1+one.length+two.length+three.length; look++){

			if(look-1 < one.length   &&   worstAvancement == oneHigh[look-1]){
				size++;
			}else if(-1 < look-1-one.length   &&   look-1-one.length < two.length   &&   worstAvancement == twoHigh[look-1-one.length]){
				size++;
				
			}else if(-1 < look-1-one.length-two.length   &&   worstAvancement == threeHigh[look-1-one.length-two.length]){
				size++;
			}
		}
		return size;
	}

//---------------------------------------------------------- Main ---------------------------------------------------------
	public static void main(String args[]){
		//tests
	}
}*/
