package Spaceshot;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Bullet {
	private float scale, act;
	private int x, y, speed, hitbox[] = {3,7,30,9}, type, ract, kills;
	private BufferedImage pic[] = new BufferedImage[3];
	private Rectangle bounding;
	
	Bullet(int x, int y, int speed){
		this(x, y, speed, 0);
	}
	Bullet(int x, int y, int speed, int type){
		this(x,y,speed,type,1.8f);
	}
	Bullet(int x, int y, int speed, int type, float scale){	
		this.act = Main.timeSinceLastFrame;
		this.ract = 1;
		this.speed = speed;
		try {
			for (int i = 0; i < pic.length; i++) {
				this.pic[i] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Projectiles/shot_" + type + "_" + i + ".png"));
			}
		} catch (IOException e) {e.printStackTrace();}
		this.type = type;
		this.scale = scale;
		this.x = x-(int)(pic[0].getWidth()*scale)/2;
		this.y = y-(int)(pic[0].getHeight()*scale)/2;
		this.bounding = new Rectangle(this.x+(int)(this.hitbox[0]*scale), this.y+(int)(this.hitbox[1]*scale), (int)(this.hitbox[2]*scale), (int)(this.hitbox[3]*scale));
	}
	
	public void update(){
		if(scale >= 1.8f) {
			Move();
			if((int)act > 2) {
				this.ract = -1;
				Move();
			}
			else if((int)act <= 0) {
				this.ract = 1;
				Move();
			}
			if(this.type == 4) {
				this.x -= speed*Main.timeSinceLastFrame;
				if(this.x <= -pic[0].getWidth()) {Main.bullets.remove(this);}
			}else {
				this.x += speed*Main.timeSinceLastFrame;
				if(this.x >= Main.StandardWidth) {Main.bullets.remove(this);}
			}
			
			for (int i = 0; i < Main.bullets.size(); i++) {
				if(type != 3 && Main.bullets.get(i).getType() != 3 && i != Main.bullets.indexOf(this) && Player.Collison(bounding, Main.bullets.get(i).getBounding())){
					Achievement.add(2, 1); //block a shot (100)
					Main.PlaySound(Main.explosion);
					Main.bullets.get(i).setScale(1.79f);
					setScale(1.79f);
				}
			}
			this.bounding.x = this.x+(int)(this.hitbox[0]*scale);
		}else {
			scale -= Main.timeSinceLastFrame*4;
			if(scale <= 0) {
				Main.bullets.remove(this);
			}
		}
	}
	private void Move() {
		this.act += (this.speed/50*Main.timeSinceLastFrame)*this.ract;
	}
	
	public Rectangle getBounding() {
		return bounding;
	}
	public float getScale() {
		return scale;
	}
	public void setScale(float i) {
		scale = i;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getType() {
		return type;
	}
	public BufferedImage getPic() {
		return pic[(int)(act)];
	}
	
	public void addKill() {
		kills++;
		if(kills > Achievement.get(5)) {Achievement.set(5, kills);}
	}
}
