package Spaceshot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Shop {
	private static int thing[], count, last, reroll[],
	cost[][][] = {
			{{64}, {192}, {386}},
			{{8}, {22}, {42}},
			{{78}, {236}, {472}},
			{{28}, {86}, {172}},
			{{58}, {172}, {342}},
			{{14}, {42}, {86}},
			{{36}, {108}, {214}},
			{{122}, {364}, {728}}
			};
	private static float sale[];
	private static BufferedImage upgrade[] = new BufferedImage[4], pSale;
	private static Random r = new Random();
	private static boolean all;
	
	public static void set() {
		int chance[] = {11,19,8,16,12,18,15,3};
		count = 0;
		for (int i = 0; i < chance.length; i++) {
			count+=chance[i];
		}
		int temp[] = new int[chance.length];
		for (int j = 0; j < temp.length; j++) {
			temp[j] = j;
		}
		thing = new int[count];
		int cnt = 0;
		for (int i = 0; i < temp.length; i++) {
			for (int c = 0; c < chance[i]; c++) {
				thing[cnt] = temp[i];
				cnt++;
			}
		}
		reroll = new int[chance.length];
		for (int i = 0; i < chance.length; i++) {
			reroll[i] = 0;
		}
		resetSale();
	}
	public static void setUpgrade() {
		try {
			for (int i = 0; i < upgrade.length; i++) {
				upgrade[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Shop/case_" + i + ".png"));
			}
			pSale = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Shop/item_sale.png"));
		} catch (IOException e) {e.printStackTrace();}
		int temp[][][] = new int[cost.length][3][5];
		for (int z = 0; z < temp.length; z++) {
			for (int y = 0; y < temp[z].length; y++) {
        		for (int j = 0; j < temp[z][y].length; j++) {
        			temp[z][y][j] = 0;
        		}
        		float i = (int)(cost[z][y][0]*sale[z]);
        		temp[z][y][0] = (int)i/300;
        		i = i%300;
        		temp[z][y][1] = (int)i/100;
        		i = i%100;
        		temp[z][y][2] = (int)i/25;
        		i = i%25;
        		temp[z][y][3] = (int)i/5;
        		i = i%5;
        		temp[z][y][4] = (int)i;
    		}
		}
		cost = temp;
	}
	public static BufferedImage getUpgrade(int i) {
		return upgrade[i];
	}
	public static BufferedImage getSale() {
		return pSale;
	}
	
	public static int getOne() {
		last = thing[r.nextInt(count)];
		if(reroll[last]<r.nextInt(4)) {
			last = -1;
		}
		return last;
	}
	public static int getLast() {
		return last;
	}
	public static int getLength() {
		return reroll.length;
	}
	public static int getReroll(int i) {
		return reroll[i];
	}
	public static void addReroll(int i) {
		if(!all) {
			if(reroll[i] < 3) {
				//System.out.println(Main.p.getCoins() + " " + cost[i][reroll[i]]);
				if(Main.p.getCoins() >= getCost(i)) {
					Main.p.setCoin(Main.p.getCoins()-getCost(i));
					reroll[i]++;
					Main.pow[i].updatePowerup();
				}
			}
			
			if(!all) {setAll();}

			if(Main.pow[9] != null && Main.pow[9].hasPlayer()) {
				Shop.resetSale();
				Shop.setCost();
				Main.menu = 1.1;
			}
		}
	}
	public static void setReroll(int i, int x) {
		reroll[i] = x;
	}
	public static void setCost() {
		for (int j = 0; j < cost.length; j++) {
			if(reroll[j] != 3) {
				if(sale[j] < 1) {
					float i = getCost(j);
					cost[j][reroll[j]][0] = (int)i/300;
	        		i = i%300;
	        		cost[j][reroll[j]][1] = (int)i/100;
	        		i = i%100;
	        		cost[j][reroll[j]][2] = (int)i/25;
	        		i = i%25;
	        		cost[j][reroll[j]][3] = (int)i/5;
	        		i = i%5;
	        		cost[j][reroll[j]][4] = (int)i;
				}
			}
		}
	}
	public static int getCost(int i, int x){
		if(reroll[i] != 3) {
			return (cost[i][reroll[i]][x]);
		}else {
			return -1;
		}
	}
	public static int getCost(int i){
		if(reroll[i] != 3) {
			int temp = (cost[i][reroll[i]][0]*300
					+cost[i][reroll[i]][1]*100
					+cost[i][reroll[i]][2]*25
					+cost[i][reroll[i]][3]*5
					+cost[i][reroll[i]][4]);
			return (int)(temp*sale[i]);
		}
		return -1;
		
	}
	public static boolean isSale(int i) {
		if(sale[i] < 1) {
			return true;
		}return false;
	}
	public static void setSale() {
		//System.out.println(Main.p.getKills());
		int temp[] = new int[3];
		for (int j = 0; j < 3; j++) {
			boolean nbreaken = true;
			while(nbreaken) {
				temp[j] = r.nextInt(sale.length);
				nbreaken = false;
				for (int b = 0; b < j; b++) {
					if(temp[b] == temp[j]) {
						nbreaken = true;
					}
				}
				sale[temp[j]] = 1-(int)(Main.p.getKills()/100)*0.05f;
			}
		}
		setCost();
	}
	public static void resetSale() {
		sale = new float[reroll.length];
		for (int i = 0; i < sale.length; i++) {
			sale[i] = 1;
		}
	}
	
	public static int getCount() {
		int temp = 0;
		for (int j = 0; j < reroll.length; j++) {
			temp+=reroll[j];
		}
		Achievement.set(9, temp);
		if(Main.p.getCoinC()) {
			Achievement.add(9);
		}
		return temp;
		
	}
	
	public static void setAll() {
		if(getCount() == 25) {
			all = true;
		}else {
			all = false;
		}
	}
	public static boolean all() {
		return all;
	}
}
