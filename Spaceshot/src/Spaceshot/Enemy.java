package Spaceshot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Enemy {
	private static boolean boss, bossIsStart;
	private static int count, spawned;
	private boolean isBoss; 
	private float scale, act, deadCd = 1, cd, actLightning;
	private int x, y, type, speed, live;
	private BufferedImage pic[] = new BufferedImage[3], dead, lightning[];
	private Random r = new Random();

	public Enemy(int type) {
		this(0,0,0,type,false);
	}
	public Enemy(int x, int y, int speed, int type, boolean b) {
		if(type == 5) {
			this.scale = 2.5f;
		}else {
			this.scale = 1.25f;
		}
		this.type = type;
		try {
			if(type == 5) {
				this.type = r.nextInt(4)+1;
				count = 16;
				this.isBoss = true;
				boss = true;
				live = 3+(this.type*2);
				//Main.p.addShot(live);
				this.lightning = new BufferedImage[20];
				actLightning = 0;
				for (int i = 0; i < 24; i++) {
					this.lightning[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Monster/lightning/lightning_" + (i+1) + ".png"));
				}
			}
			for (int i = 0; i < pic.length; i++) {
				this.pic[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Monster/" + this.type + "Monster" + (i+1) + ".png"));
			}
			this.dead = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Monster/" + this.type + "MonsterDead.png"));
		} catch (IOException e) {e.printStackTrace();}
		reset(x, y, speed, b);
		if(!boss) {Main.p.addShot();}
	}
	private void reset() {
		reset(0,0,0, false);
	}
	private void reset(int x, int y, int speed, boolean b) {
		this.act = Main.timeSinceLastFrame;
		if(!b) {
			if(type == 2) {
				this.speed = r.nextInt(450)+450;
			}else if(type == 4){
				this.speed = r.nextInt(150)+200;
			}else {
				this.speed = r.nextInt(300)+300;
			}
		}else {
			this.speed = speed;
		}
		if(isBoss) {
			this.x = Main.StandardWidth-(int)(this.pic[0].getWidth()/2*scale)+175;
			this.y = Main.StandardHeight/2-(int)(this.pic[0].getHeight()/2*scale);
			if(count != 16) {
				count++;
				if(count == 0) {count++;}
			}
			spawned = 0;
		}else {
			if(!b) {
				this.x = Main.StandardWidth+x;
				this.y = r.nextInt(Main.StandardHeight-(int)(this.pic[0].getHeight()*scale));
			}else {
				this.x = Main.StandardWidth-(int)(this.pic[0].getWidth()/2*scale)+x;
				this.y = Main.StandardHeight/2-(int)(this.pic[0].getHeight()/2*scale)+y;
			}
		}
	}
	
	public void update() {
		if(deadCd == 1) {
			if(!(!bossIsStart && isBoss)) {
				if(isBoss && spawned != count) {
					int temp[][] = {{100,0},{50,-100},{50,100},{-20,-180},{-20,180},{-125,-155},{-125,155},{-65,-285},
							{65,285},{-225,0},{-175,-215},{-175,215},{-200,-370},{-200,370},{-325,-450},{-325,450}};
					for (int i = spawned; i < count; i++) {
						if(spawned == i && x <= Main.StandardWidth+temp[i][0]) {
							Main.enemys.add(new Enemy(0,temp[i][1],this.speed,type,true));
							spawned++;
						}
					}
				}
				if(isBoss && count == 0) {
					this.speed+=250;
					count--;
				}
				if(type == 3 || (isBoss && count == -1)) {
					float speedx = (Main.p.getX()+Main.p.getRaumschiff().getWidth()/2) - getMidX();
					float speedy = (Main.p.getY()+Main.p.getRaumschiff().getHeight()/2) - getMidY();
					
					float speed = (float) Math.sqrt(speedx*speedx + speedy*speedy);
					
					if(speed != 0){
						int neg = 1;
						if(speedy > 0) {neg = -1; speedy *= -1;}
						speedy /= speed;
						speedy *= this.speed*Main.timeSinceLastFrame;
						y += neg*(int)(speedy);
					}
				}
				if(type == 4) {
					if(this.cd <= 0) {
						float temp = 1.8f;
						if(isBoss) {
							temp = 3.6f;
						}
						Main.bullets.add(new Bullet(this.x+(int)(this.pic[0].getWidth()*scale)/2,this.y+(int)(this.pic[0].getHeight()*scale)/2,900,4,temp));
						this.cd = 6-speed/50;
					}else {
						this.cd -= Main.timeSinceLastFrame;
					}
				}
				this.x -= this.speed*Main.timeSinceLastFrame;
				this.act += this.speed/150*Main.timeSinceLastFrame;
				if((int)act > 2) {this.act = Main.timeSinceLastFrame;}
				if(isBoss) {
					this.actLightning += this.speed/75*Main.timeSinceLastFrame;
					if((int)actLightning > 23) {this.actLightning = Main.timeSinceLastFrame;}
				}
				if(this.x <= -(this.pic[0].getWidth()*scale)) {
					if(boss && !isBoss) {
						Main.enemys.remove(this);
					}else {
						if(isBoss) {
							int temp = (2+(int)(count/5));
							if(temp < 2) {
								temp = 2;
							}
							Main.p.addShot(temp);
						}
						reset();
					}
				}
				for (int j = 0; j < Main.bullets.size(); j++) {
					if(!(Main.bullets.get(j).getType() == 4 && type == 4) && Player.Collison(Main.bullets.get(j).getBounding(), this)) {
						if(bossIsStart && !isBoss) {
							count--;
						}
						setKill();
						if(Main.bullets.get(j).getType() != 1 && Main.bullets.get(j).getType() != 12) {Main.bullets.remove(j);}
					}else if (Main.bullets.get(j).getType() == 4 && Player.Collison(Main.bullets.get(j).getBounding(), Main.p.getBounding())){
						Main.bullets.remove(j);
						Main.p.setHit();
					}
				}
			}else if(!bossIsStart && isBoss && Main.enemys.size() == 1){
				bossIsStart = true;
				//System.out.println("test");
			}
		}else {
			this.deadCd -= Main.timeSinceLastFrame;
			if((int)(this.deadCd) == 1) {
				this.deadCd = 1;
			}
			if(this.deadCd <= 0) {
				if(boss && isBoss) {
					boss = false;
					bossIsStart = false;
				}
				Main.enemys.remove(this);
			}
			for (int j = 0; j < Main.bullets.size(); j++) {
				if(Player.Collison(Main.bullets.get(j).getBounding(), this)) {
					if(Main.bullets.get(j).getType() != 1 && Main.bullets.get(j).getType() != 12) {Main.bullets.remove(j);}
				}
			}
		}
	}
	public void setKill() {
		if(boss && isBoss && live > 1) {
			deadCd = 3;
			live--;
		}else {
			if(deadCd == 1) {
				this.deadCd -= Main.timeSinceLastFrame;
				Main.p.addKill();
			}
		}
	}
	
	public boolean isBoss() {
		return isBoss;
	}
	
	public int getLive() {
		return live;
	}
	public static boolean getBoss() {
		return boss;
	}
	public static int getCount() {
		return count;
	}
	public static void setBoss(boolean b) {
		boss = b;
	}
	public float getScale() {
		return scale;
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
	public float getWidth() {
		return pic[0].getWidth()*scale;
	}
	public float getHeight() {
		return pic[0].getHeight()*scale;
	}
	public int getMidX() {
		return (int)(x+getWidth()/2);
	}
	public int getMidY() {
		return (int)(y+getHeight()/2);
	}
	public BufferedImage getPic() {
		if(deadCd != 1) {
			return dead;
		}
		if((int)act > 2) {
			return pic[2];
		}
		return pic[(int)act];
	}
	public BufferedImage getLightning() {
		return lightning[(int) actLightning];
	}
}
