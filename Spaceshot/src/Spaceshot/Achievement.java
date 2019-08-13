package Spaceshot;

public class Achievement {
	private static String text[][] = {												
			{"Get startet","Killer!","You were faster!","One Follower less","Shoot back"}, 	//first blood, purple, blue, red, yellow kills (1000)
			{"Beginner!", "Kill them all!", "It's too easy", "Holy crap!"},  				//kills in one round (100, 250, 500, 1000)
			{"Blocked!","Let them do your work"},											//block a shot (100) & enemies killed (200) by yellow
			{"Just kill them!", "What are you doing?!", "Are you AFK?"},					//let enemies through (100, 500, 1000)
			{"Game Over", "Are you even trying?", "Mega Noob"},								//die (1, 500, 1000)
			{"Double!", "Nice one!", "Crazy", "MULTI KILL"},								//multi kills with piercing (2, 3, 4, 5)
			{"That's how you do it!", "Easy Peasy!"},										//kill a boss (1, 50)
			{"My first coin!", "Gotta catch them all!", "Do you really need that many?"},	//earn coins (1, 1000, 5000)
			{"Am I rich?", "I am rich!"},													//have coins (1000, 2000)
			{"Let's go shopping", "Make it easier", "I want it all!"},						//buy upgrades (1, 10, 25)
			{"Full entertainment", "Make your own rules", "I like the music!", "SPACESHOT"}	//fullscreen, change the configs, 100% music, click on "Spaceshot" in main menu
			};
	private static int end[][] = {
			{1, 1, 750, 625, 500, 375}, //x//first blood, purple, blue, red, yellow kills (1000)
			{1, 100, 250, 500, 1000},//kills in one round (100, 250, 500, 1000)
			{1, 100, 200},//block a shot (100) & enemies killed (200) by yellow
			{1, 100, 500, 1000},//let enemies through (100, 500, 1000)
			{1, 1, 500, 1000},//die (1, 500, 1000)
			{1, 2, 3, 4, 5},//multi kills with piercing (2, 3, 4, 5)
			{1, 1, 50},//kill a boss (1, 50)
			{1, 1, 1000, 5000},//earn coins (1, 1000, 5000)
			{1, 1000, 2000},//have coins (1000, 2000)
			{1, 1, 10, 25},//buy upgrades (1, 10, 25)
			{1, 1, 1, 1, 1} //x//fullscreen, change the configs, 100% music, click on "Spaceshot" in main menu
			};
	private static int progress[] = new int[text.length];
	public static void setLev(int x, int b) {
//		for (int i = 0; i < progress.length; i++) {
//			progress[i] = new int[text[i].length];
//		}
		end[x][0] = b;
	}
	public static int getLev(int x) {
		return end[x][0];
	}
	
	private static void test(int x, boolean b) {
		if(end[x][0] != -1 && progress[x] >= getEnd(x)) {
			if(b){Main.PlaySound(Main.achievement);}
			end[x][0]++;
			if(end[x][0] >= (end[x].length)) {
				end[x][0] = -1;
				
			}else if((x == 0) || ((x == 2 && progress[x] <= 1) && (progress[x] < getEnd(x)))  || (x == (count()-1))) {
				progress[x] = 0;
				
			}else if(progress[x] >= getEnd(x)){
				test(x,b);
			}
		}
	}
	
	public static void fset(int x, int b) {
		progress[x] = b;
		if((x != 0) && (x != (count()-1))) {
			test(x,false);
		}
	}

	public static void set(int x, int y) {
		set(x,y,true);
	}
	public static void set(int x, int y, boolean b) {
		progress[x] = y;
		test(x,b);
	}
	
	public static void addM(int x, int y) {
		addM(x,y,true);
	}
	public static void addM(int x, int y, boolean b) {
		if(progress[x] != -1) {
			set(x,(progress[x]+y),b);
		}
	}
	
	public static void fadd(int x) {
		addM(x,1,false);
	}
	
	
	public static void add(int x) {
		addM(x,1,true);
	}
	public static void add(int x, int y) {
		if(y == (end[x][0])) {
			add(x);
		}
	}
	
	public static int get(int x) {
		return progress[x];
	}
	
	public static int count() {
		return text.length;
	}
	public static int count(int x) {
		return text[x].length;
	}

	private static int end(int x) {
		if(end[x][0] == -1) {
			return (end[x].length-1);
		}else {
			return end[x][0];
		}
	}
	public static String getText(int x) {
		return text[x][end(x)-1];
	}
	public static int getEnd(int x) {
		return end[x][end(x)];
	}
	public static boolean isEnd(int x) {
		if(end[x][0] == -1) {
			return true;
		}else {
			return false;
		}
	}
}
