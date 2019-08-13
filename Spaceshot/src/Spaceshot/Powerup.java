package Spaceshot;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Powerup {
	private int x, y, type, speed;
	private float cdType[][] = {
			{0},
			{10,20,30,40},
			{4,6,8,10},
			{10,15,20,25},
			{0},
			{5,10,15,17.5f},
			{5,7.5f,10,12.5f},
			{2,3,4,5},
			{0},
			{0}},
			cd;
	private boolean hasPlayer = false, exist;
	private BufferedImage pic[];
	private static String[] what = {"heal","shield_icon","slowmo","double_cannons","extra_ammo","piercing","infinity","ultimate","coin_0","shop_sale"};
	private Random r = new Random();
	private Rectangle bounding;
	
	public Powerup(int type) {
		this.type = type;
		try {
			if(type!=8) {
				pic = new BufferedImage[1];
				if(type == 9) {
					this.pic[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Powerups/"+ what[type] +".png"));
				}else {
					this.pic[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Powerups/"+ what[type] +"_"+ Shop.getReroll(type) +".png"));
				}
			}else {
				pic = new BufferedImage[19];
				cd = 18;
				for (int i = 0; i < pic.length; i++) {
					this.pic[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Powerups/Coin/coin_" + i + ".png"));
				}
			}
		} catch (IOException e) {e.printStackTrace();}
		this.x = Main.StandardWidth;
		this.bounding = new Rectangle(this.x, this.y, pic[0].getWidth(), pic[0].getHeight());
	}
	public void respawn(int x, int y) {
		this.speed = r.nextInt(150)+150;
		this.y = y;
		this.bounding.y = this.y;
		if(x!=-1) {
			this.x = x;
			this.bounding.x = this.x;
		}
		exist = true;
	}
	
	public void respawn() {
		respawn(-1,r.nextInt(Main.StandardHeight-(int)(this.pic[0].getHeight())));
	}
	
	public void update() {
		if(exist) {
			if(!hasPlayer) {
				updateCoin();
				
				this.x -= speed*Main.timeSinceLastFrame;
				this.bounding.x = this.x;
				if(x <= -pic[0].getWidth()) {
					rexist();
				}
				
				if(Player.Collison(bounding,Main.p.getBounding())) {
					if(type == 0) {
						Main.p.addLive();
						if(Shop.getReroll(type) >= 2) {Main.p.addLive();}
					}else if(type == 4) {
						if(Shop.getReroll(type) >= 2) {Main.p.addShot(2);}
						else{Main.p.addShot(1);}
					}else if(type == 8) {
						Main.p.addCoin();
					}else {
						hasPlayer = true;
						if(type != 9) {cd = cdType[type][Shop.getReroll(type)];}
						if(type == 3) {
							Main.p.setRaumschiff(1);
						}else if(type == 9) {
							if(Shop.getUpgrade(0) == null) {
					    		Shop.setUpgrade();
							}
							Shop.setSale();
							Main.menu = 0.1;
						}
					}
					if(type == 0 || type == 4 || type == 8) {rexist();}
				}
			}else {
				setCd();
			}
		}
	}
	public void updateCoin() {
		if(type == 8) {
			if(cd >= -18) {cd -= Main.timeSinceLastFrame*40;}
			if(cd < -18) {cd = 18;}
		}
	}
	public void resetCd() {
		cd = 0;
		setCd();
	}
	
	private void setCd() {
		//System.out.println(cd);
		//if(type==1) {System.out.println(cd);}
		if(cd >= 0) {
			cd -= Main.timeSinceLastFrame;
			if(Main.pow[2] != null && Main.pow[2].hasPlayer()) {
				Main.timeSinceLastFrame*=0.75;
			}
		}else {
			if(type == 3) {
				Main.p.setRaumschiff(0);
			}
			rexist();
		}
	}
	public float getCd() {
		return cd;
	}
	
	public boolean exist() {
		return exist;
	}
	public void rexist() {
		this.x = Main.StandardWidth;
		exist = false;
		hasPlayer = false;
	}
	
	public boolean hasPlayer() {
		return hasPlayer;
	}
	
	public Rectangle getBounding() {
		return bounding;
	}
	public int getType() {
		return type;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public static String getWhat(int i) {
		return what[i];
	}
	public BufferedImage getPowerup() {
		if(type == 8) {
			if(cd < 0) {
				return pic[(int)-cd];
			}
			return pic[(int)(cd)];
		}
		return pic[0];
	}
	public void updatePowerup() {
		try {
			this.pic[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Powerups/"+ what[type] +"_"+ Shop.getReroll(type) +".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
