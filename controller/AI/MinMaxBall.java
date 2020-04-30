package controller.AI;
import model.Stadium;
import model.Player;
import controller.AI.RandomAI_1;

class MinMaxBall {
	int checkingDepth;
	Stadium now;
	
	String[] one;
	String[] two;
	String[] three;
	int[] avancement;
	
	RandomAI_1 tools;
	int ball;

	public MinMaxBall(int depth, Stadium stade) {		
		checkingDepth = depth;
		now = stade;
		
		tools = new RandomAI_1(now.whosTurn(),now);
		ball = tools.ballNumber();

		this.initOne();
		this.initTwo();
		this.initThree();
		this.initAvancement();
		
		
		System.out.println("ONE:");
		for(int i = 0; i != one.length; i++){
			System.out.print(one[i]+" - ");
		}
		System.out.println("La taille de one: "+one.length);
		System.out.println("");
		
		System.out.println("TWO:");
		for(int i = 0; i != two.length; i++){
			System.out.print(two[i]+" - ");
		}
		System.out.println("La taille de two: "+two.length);
		System.out.println("");
		
		System.out.println("THREE:");
		for(int i = 0; i != three.length; i++){
			System.out.print(three[i]+" - ");
		}
		System.out.println("La taille de three: "+three.length);
		System.out.println("");
	}
	
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
		else
			return 'R';
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
	
	public int numberMoveAfter(String instruction){
		exec(instruction);
		int number = tools.moveNumber();
		undo(instruction);
		return number;
	}
	
	public int numberPassAfter(String instruction){
		exec(instruction);
		int number = tools.passNumber();
		undo(instruction);
		return number;
	}
	
	public void initOne(){		
		one = new String[tools.passNumber() + tools.moveNumber()];
		int oneNum = 0;
		
		for(int check = 0; check != tools.equip().length; check++){
			if(check != ball   &&   tools.canPass(tools.equip()[ball], tools.equip()[check])){
				one[oneNum] = ""+check+"P";
				oneNum++;
			}
			if(check != ball   &&   tools.canUp(tools.equip()[check])){
				one[oneNum] = ""+check+"U";
				oneNum++;
			}
			if(check != ball   &&   tools.canDown(tools.equip()[check])){
				one[oneNum] = ""+check+"D";
				oneNum++;
			}
			if(check != ball   &&   tools.canLeft(tools.equip()[check])){
				one[oneNum] = ""+check+"L";
				oneNum++;
			}
			if(check != ball   &&   tools.canRight(tools.equip()[check])){
				one[oneNum] = ""+check+"R";
				oneNum++;
			}
		}
	}
	
	public void initTwo(){
		String stockage = "";
		int ball2;
		
		for(int actLook = 0; actLook != one.length; actLook++){
		
			exec(one[actLook]);
				ball2 = tools.ballNumber();
				for(int check = 0; check != tools.equip().length; check++){
					if(one[actLook].charAt(1) != 'P'   &&   check != ball2   &&   tools.canPass(tools.equip()[ball2], tools.equip()[check]))
						stockage += one[actLook]+check+"P";
						
					if(check != ball2   &&   tools.canUp(tools.equip()[check]))
						stockage += one[actLook]+check+"U";
						
					if(check != ball2   &&   tools.canDown(tools.equip()[check]))
						stockage += one[actLook]+check+"D";
						
					if(check != ball2   &&   tools.canLeft(tools.equip()[check]))
						stockage += one[actLook]+check+"L";
						
					if(check != ball2   &&   tools.canRight(tools.equip()[check]))
						stockage += one[actLook]+check+"R";
				}
			undo(one[actLook]);
			
			two = new String[stockage.length()/4];
			
			for(int look = 0; look != stockage.length(); look += 4)
				two[look/4] = ""+stockage.charAt(look)+stockage.charAt(look+1)+stockage.charAt(look+2)+stockage.charAt(look+3);
		}
	}
	
	public void initThree(){
		String stockage = "";
		int ball2;
		
		for(int actLook = 0; actLook != two.length; actLook++){
		
			exec(two[actLook]);
				ball2 = tools.ballNumber();
				for(int check = 0; check != tools.equip().length; check++){
					if(two[actLook].charAt(1) != 'P'   &&   two[actLook].charAt(3) != 'P'   &&   check != ball2   &&   tools.canPass(tools.equip()[ball2], tools.equip()[check]))
						stockage += two[actLook]+check+"P";
						
					if((two[actLook].charAt(1) == 'P'   ||   two[actLook].charAt(3) == 'P')   &&   check != ball2   &&   tools.canUp(tools.equip()[check]))
						stockage += two[actLook]+check+"U";
						
					if((two[actLook].charAt(1) == 'P'   ||   two[actLook].charAt(3) == 'P')   &&   check != ball2   &&   tools.canDown(tools.equip()[check]))
						stockage += two[actLook]+check+"D";
						
					if((two[actLook].charAt(1) == 'P'   ||   two[actLook].charAt(3) == 'P')   &&   check != ball2   &&   tools.canLeft(tools.equip()[check]))
						stockage += two[actLook]+check+"L";
						
					if((two[actLook].charAt(1) == 'P'   ||   two[actLook].charAt(3) == 'P')   &&   check != ball2   &&   tools.canRight(tools.equip()[check]))
						stockage += two[actLook]+check+"R";
				}
			undo(two[actLook]);
			
			three = new String[stockage.length()/6];
			
			for(int look = 0; look != stockage.length(); look += 6)
				three[look/6] = ""+stockage.charAt(look)+stockage.charAt(look+1)+stockage.charAt(look+2)+stockage.charAt(look+3)+stockage.charAt(look+4)+stockage.charAt(look+5);
		}
	}
	
	public void initAvancement(){
		avancement = new int[one.length + two.length + three.length+1];
		//TODO
	}

	public static void main(String args[]){
		//tests
		Stadium stade = new Stadium();
//		stade.move(stade.getSnowKids()[0],'U');
		MinMaxBall test = new MinMaxBall(0,stade);
	}
}
