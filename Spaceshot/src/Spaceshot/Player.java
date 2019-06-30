package Spaceshot;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.imageio.ImageIO;

public class Player {
	private float scale, cd = -1;
	private int x, y, act, hitbox[][] = {{32, 48, 200, 77},{23, 17, 135, 127}}, lives, shots, kills, coins[] = new int[5];
	private BufferedImage raumschiff[] = new BufferedImage[2], Oraumschiff, heart[] = new BufferedImage[3], shot, shield, coin[] = new BufferedImage[5],
			explosion[] = new BufferedImage[3];
	private Rectangle bounding;
	private boolean hit, getShot, getHeal, coinC;
	
	public Player(){
		this.scale = 0.75f;
		setTexture();
		setCoin(0);
		reset();
		try {
			for (int i = 0; i < heart.length; i++) {
				heart[i] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Icons/heart_" + i + ".png"));
			}
			shot = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Icons/shot.png"));
			shield = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Powerups/shield_0.png"));
			for (int i = 0; i < coin.length; i++) {
				coin[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Icons/coin_"+ i +".png"));
			}
			
		}catch(Exception e) {e.printStackTrace();}
	}
	public void update() {
		if(lives > 0) {
			if(kills%100 == 0 && !getShot) {
				addShot();
				getShot = true;
			}else if(kills%100 != 0 && getShot) {
				getShot = false;
			}
			
			if(lives < 3 && kills != 0){
				if(kills%50 == 0 && !getHeal) {
					addLive();
					getHeal = true;
				}else if(kills%50 != 0 && getHeal) {
					getHeal = false;
				}
			}
			
			if(shots > 0) {
				int temp = 0;
				if(Main.pow[5] != null && Main.pow[5].hasPlayer() && Main.pow[6] != null && Main.pow[6].hasPlayer()) {temp = 12;}
				else if(Main.pow[5] != null && Main.pow[5].hasPlayer()) {temp = 1;}
				else if(Main.pow[6] != null && Main.pow[6].hasPlayer()) {temp = 2;}
				if(!(Main.pow[7] != null && Main.pow[7].hasPlayer())) {
					if(Keyboard.isKeyDownCd(Main.steuerung[1].getKey())){
						Main.PlaySound(Main.shot);
						if(!(Main.pow[6] != null && Main.pow[6].hasPlayer())) {shots -= 1;}
						if(Main.pow[3] != null && Main.pow[3].hasPlayer()) {
							for (int i = 0; i < 2; i++) {
								Main.bullets.add(new Bullet(this.x+(int)(this.raumschiff[0].getWidth()*0.75f)/2,this.y+(int)(this.raumschiff[0].getHeight()*0.75f)/2-33+66*i,900,temp));
							}
						}else {
							Main.bullets.add(new Bullet(this.x+(int)(this.raumschiff[0].getWidth()*0.75f)/2,this.y+(int)(this.raumschiff[0].getHeight()*0.75f)/2,900,temp));
						}
					}
				}else if (shots > 0){
					if(Keyboard.isKeyDown(Main.steuerung[1].getKey())){
						//Main.PlaySound(Main.shot);
						Main.bullets.add(new Bullet(this.x+(int)(this.raumschiff[0].getWidth()*0.75f)/2,this.y+(int)(this.raumschiff[0].getHeight()*0.75f)/2,900,3));
					}
				}
			}
			
			if(Keyboard.isKeyDown(Main.steuerung[2].getKey())) {
				this.y -= 1;
				this.y -= 450*Main.timeSinceLastFrame;
		        if(this.y<-this.hitbox[act][1]*scale){this.y=-(int)(this.hitbox[act][1]*scale);}
			}
			if(Keyboard.isKeyDown(Main.steuerung[3].getKey())) {
				this.y += 1;
				this.y += 450*Main.timeSinceLastFrame;
		        if(this.y>(Main.StandardHeight-(raumschiff[0].getHeight()-this.hitbox[act][1]*scale)*0.75f)){this.y=(int)(Main.StandardHeight-(raumschiff[0].getHeight()-this.hitbox[act][1]*scale)*0.75f);}
			}
			
			if(Keyboard.isKeyDown(Main.steuerung[4].getKey())) {
				this.x -= 1;
				this.x -= 250*Main.timeSinceLastFrame;
		        if(this.x<-this.hitbox[act][0]*scale){this.x=-(int)(this.hitbox[act][0]*scale);}
			}
			
			if(Keyboard.isKeyDown(Main.steuerung[5].getKey())) {
				this.x += 625*Main.timeSinceLastFrame;
		        if(this.x>(Main.StandardWidth-(raumschiff[0].getWidth()-this.hitbox[act][0]*scale)*0.75f)){this.x=(int)(Main.StandardWidth-(raumschiff[0].getWidth()-this.hitbox[act][0]*scale)*0.75f);}
			}
			
			if((this.x >= (200*Main.timeSinceLastFrame-this.hitbox[act][0])) && !Keyboard.isKeyDown(Main.steuerung[10].getKey())) {
				this.x-=200*Main.timeSinceLastFrame;
			}
			
			this.bounding.x = this.x+(int)(this.hitbox[act][0]*scale);
			this.bounding.y = this.y+(int)(this.hitbox[act][1]*scale);
			for (int i = 0; i < Main.enemys.size(); i++) {
				if(cd == -1 && Collison(bounding, Main.enemys.get(i))) {
					Main.enemys.get(i).setKill();
					hit = true;
					break;
				}
			}
			
			if(hit) {
				updateHit();
			}
					
		}else {
			if(cd > 0) {
				cd -= Main.timeSinceLastFrame*5;
				if(cd <= 4 && cd > 3 && act != 1) {this.act = 1;}
				else if(cd <= 3 && act != 2) {this.act = 2;}
			}else {
				reset();
				Main.menu = 0;
			}
		}
	}
	
	private void reset() {
		this.act = 0;
		this.x = 400;
		this.y = Main.StandardHeight/2-(int)(raumschiff[0].getHeight()/2*scale);
		this.cd = -1;
		this.hit = false;
		this.lives = 1;
		this.shots = 0;
		this.kills = 0;
		this.getShot = false;
	}
	private void setTexture() {
		try {
			for (int i = 0; i < raumschiff.length; i++) {
				raumschiff[i] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Spaceship/" + act + "Spaceship" + i + ".png"));
			}
			Oraumschiff = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Spaceship/" + act + "Spaceship1.png"));
		}catch(Exception e) {e.printStackTrace();}
		
		this.bounding = new Rectangle(this.x+(int)(this.hitbox[act][0]*scale), this.y+(int)(this.hitbox[act][1]*scale), (int)(this.hitbox[act][2]*scale), (int)(this.hitbox[act][3]*scale));
	}
	private void setExplosion() {
		try {
			for (int i = 0; i < explosion.length; i++) {
				explosion[i] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Spaceship/explosion_" + i + ".png"));
			}
		}catch(Exception e) {e.printStackTrace();}
		this.act = 0;
	}
	public void setCoin(int c) {
		for (int j = 0; j < coins.length; j++) {
			coins[j] = 0;
		}
		float i = (int)c;
		coins[0] = (int)i/300;
		i = i%300;
		coins[1] = (int)i/100;
		i = i%100;
		coins[2] = (int)i/25;
		i = i%25;
		coins[3] = (int)i/5;
		i = i%5;
		coins[4] = (int)i;
		//System.out.println(coins[0] + " " + coins[1]+ " " + coins[2]+ " " + coins[3] + " " + 
			//	(coins[0]*300+coins[1]*100+coins[2]*25+coins[3]*5+coins[4]));
	}
	private static double Pyth(int a, int b) {
		return Math.sqrt(Math.pow(a, 2)+ Math.pow(b, 2));
	}
	
	public void updateRaumschiff() {
		Color c = new Color(Main.color[0], Main.color[1], Main.color[2]);
		for (int z = 0; z < raumschiff[1].getWidth(); z++) {
	    	for (int j = 0; j < raumschiff[1].getHeight(); j++) {
	    		Color pixelfarbe = new Color(Oraumschiff.getRGB(z, j), true);
	    		int red = c.getRed();
	    		if(pixelfarbe.getRed()-(255-c.getRed()) < 0) {red = 0;}
	    		else {red = pixelfarbe.getRed()-(255-c.getRed());}
	    		int green = c.getGreen();
	    		if(pixelfarbe.getGreen()-(255-c.getGreen()) < 0) {green = 0;}
	    		else {green = pixelfarbe.getGreen()-(255-c.getGreen());}
	    		int blue = c.getBlue();
	    		if(pixelfarbe.getBlue()-(255-c.getBlue()) < 0) {blue = 0;}
	    		else {blue = pixelfarbe.getBlue()-(255-c.getBlue());}
	    		int rgb = new Color(red, green, blue, pixelfarbe.getAlpha()).getRGB();
	    		raumschiff[1].setRGB(z, j, rgb);
	    	}
	    }
	}
	public static boolean Collison(Rectangle b, Enemy e) {
		if(b.x <= (e.getX()+e.getWidth()) && (b.x+b.getWidth()) >= (e.getX())
				&& b.y <= (e.getY()+e.getHeight()) && (b.y+b.getHeight()) >= (e.getY())) {
			if(((b.x <= (e.getX()+e.getWidth()/2) && (b.x+b.getWidth()) >= (e.getX()+e.getWidth()/2))
				|| (b.y <= e.getMidY() && (b.y+b.getHeight()) >= e.getMidY()))
				|| Pyth((b.x-e.getMidX()), (b.y-e.getMidY())) <= e.getWidth()/2
				|| Pyth((b.x-e.getMidX()), (b.y+(int)(b.getHeight()-e.getMidY()))) <= e.getWidth()/2
				|| Pyth((b.x+(int)(b.getWidth())-e.getMidX()), (b.y-e.getMidY())) <= e.getWidth()/2
				|| Pyth((b.x+(int)(b.getWidth())-e.getMidX()), (b.y+(int)(b.getHeight())-e.getMidY())) <= e.getWidth()/2
				){
				return true;
			}
		}
		return false;
	}
	public static boolean Collison(Rectangle b, Rectangle c) {
		if(b.x <= (c.getX()+c.getWidth()) && (b.x+b.getWidth()) >= (c.getX())
				&& b.y <= (c.getY()+c.getHeight()) && (b.y+b.getHeight()) >= (c.getY())){
			return true;
		}
		return false;
	}
	
	public BufferedImage getRaumschiff(){
		return raumschiff[0];
	}
	public BufferedImage getRaumschiff(int i){
		return raumschiff[i];
	}
	public BufferedImage getExplosion() {
		return explosion[act];
	}
	public BufferedImage getShot(){
		return shot;
	}
	public BufferedImage getHeart(int i){
		if(Main.pow[1].exist() && Main.pow[1].hasPlayer() && i == (lives+1)) {
			return heart[1];
		}else if(i <= 3 && i <= lives) {
			return heart[0];
		}else if(i > 3) {
			return heart[2];
		}
		return null;
	}
	public BufferedImage getCoin(int i) {
		return coin[4-i];
	}
	public BufferedImage getShield(){
		return shield;
	}
	public Rectangle getBounding() {
		return this.bounding;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
	public float getScale() {
		if(lives != 0) {return this.scale;}
		else {return 1+(5-cd)/2;}
	}
	public float getCd() {
		if(cd > 0) {return cd;}
		return 0;
	}
	public float getCd2() {
		return cd;
	}
	public int getShots(){
		return shots;
	}
	public int getLives(){
		return lives;
	}
	public int getKills(){
		return kills;
	}
	public int getCoins(){
		return (coins[0]*300+coins[1]*100+coins[2]*25+coins[3]*5+coins[4]);
	}
	public int getCoins(int i){
		return coins[i];
	}
	public boolean getCoinC(){
		return coinC;
	}
	
	public void setHit() {
		hit = true;
	}
	private void updateHit() {
		if(cd == -1) {
			cd = 2;
			if(!Main.pow[1].hasPlayer()) {lives -= 1;}
			else {
				Main.pow[1].rexist();
			}
			if(lives == 0) {
				Main.saveSettings();
				cd = 5;
				setExplosion();
				Main.PlaySound(Main.explosion);
				x+=raumschiff[0].getWidth()/2*scale;
				y+=raumschiff[0].getHeight()/2*scale;
				Main.enemys = new LinkedList<Enemy>();
				Enemy.setBoss(false);
				Main.bullets = new LinkedList<Bullet>();
				for (int i = 0; i < Main.pow.length; i++) {
					if(Main.pow[i].exist()) {
						Main.pow[i].rexist();
					}
				}
			}
		}else if(cd <= 0) {
			System.out.println("test");
			hit = false;
			cd = -1;
			
		}else if(cd > 0) {
			cd -= Main.timeSinceLastFrame;
		}
	}
	public boolean isHit() {
		if(cd == -1) {
			return false;
		}else if(cd >= 1 && cd%0.4 >= 0 && cd%0.4 < 0.1) {
			return true;
		}
		return false;
	}
	
	public void setX(int x) {
		setPos(x,this.y);
	}
	public void setY(int y) {
		setPos(this.x,y);
	}
	public void setPos(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void switchRaumschiff() {
		setRaumschiff((act+1));
	}
	public void setRaumschiff(int i) {
		if(i >= 2) {i = 0;}
		if(i != this.act) {
			this.act = i;
			setTexture();
			updateRaumschiff();
		}
	}
	public void setCoinC(boolean b){
		coinC = b;
	}
	
	public void addShot() {
		if(!(Main.pow[6] != null && Main.pow[6].hasPlayer()) 
		&& !(Main.pow[7] != null && Main.pow[7].hasPlayer())) {
			shots += 1;
		}
	}
	public void addShot(int i) {
		shots += i;
	}
	public void addLive() {
		lives += 1;
	}
	public void addLive(int i) {
		lives += i;
	}
	public void addKill() {
		kills += 1;
	}
	public void addCoin() {
		setCoin(getCoins()+1);
	}
	public void addCoin(int i) {
		setCoin(getCoins()+i);
	}
}
