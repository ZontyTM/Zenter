package Spaceshot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
	
public class Slide_control {
	private BufferedImage[] schieberegler = new BufferedImage[3];
	private int x, y, width, height, page, punktx, wert, cooldown;
	private double netzX, netzY;
	private boolean deaktiviert, deaktiviert2, ausgewaehlt, gedrueckt, isgedrueckt;
	private String name;
	private float scale;
	private Schrift text, Swert;
	private Color c;

	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, "", 0, 0, -1, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, int page, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, "", 0, 0, page, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, what, 0, 0, -1, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int page, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, what, 0, 0, page, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, int xminus, int yminus, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, "", xminus, yminus, -1, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, int xminus, int yminus, int page, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, "", xminus, yminus, page, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int xminus, int yminus, int wert){
		this(name, c, x, y, buttontext, scale, netzX, netzY, what, xminus, yminus, -1, wert);
	}
	
	public Slide_control(String name, Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int xminus, int yminus, int page, int wert){ 
		try {
			schieberegler[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Schieberegler/" + buttontext + "A.png"));
			schieberegler[1] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Schieberegler/" + buttontext + "D.png"));
			schieberegler[2] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Schieberegler/" + buttontext + ".png"));
		} catch (IOException e) {e.printStackTrace();}
		if(what.contains("Normal")){
			this.x = x;
			this.punktx = x+20;
			this.y = y;
			
		}else{
			this.x = Main.StandardWidth/2-(int)(schieberegler[2].getWidth()*scale)/2+x;
			this.punktx = (int) ((wert*5*scale)+(this.x+20*scale));
			this.y = Main.StandardHeight/2-(int)(schieberegler[2].getHeight()*scale)/2+y;
		}
		if(xminus != 0){
			this.x = Main.StandardWidth - (int)(schieberegler[2].getWidth()*scale) + xminus;
			this.punktx = (int) ((wert*5*scale)+(this.x+20*scale));
		}
		if(yminus != 0){
			this.y = Main.StandardHeight - (int)(schieberegler[2].getHeight()*scale) + yminus;
		}
		this.deaktiviert = false;
		this.deaktiviert2 = false;
		this.width = (int)(schieberegler[0].getWidth()*scale);
		this.height = (int)(schieberegler[0].getHeight()*scale);
		this.name = name;
		if(what.contains("SchriftOben")){
			this.text = new Schrift(new String[]{name}, this.x, this.y-23, 0.3*scale, c);
		}else{
			this.text = new Schrift(new String[]{name}, this.x+(int)(schieberegler[2].getWidth()*scale)/2, true, (int) (this.y+this.height/2-60*scale), true, 0.45*scale, c);
		}
		this.scale = scale;
		this.netzX = netzX;
		this.netzY = netzY;
		if(page != -1){this.page = page;}
		this.wert = wert;
		this.Swert = new Schrift(new String[]{String.valueOf(wert)}, this.x+(int)(schieberegler[2].getWidth()*scale)/2, true, (int) (this.y+this.height/2+60*scale), true, 0.45*scale, c);
		this.c = c;
	}
	
	public void set(){
		if(ausgewaehlt){
			if(Keyboard.isKeyDownCd(Main.steuerung[10].getKey(),3)){
				if(wert > 0 && wert <= 100){
					wert -=1;
					punktx = (int) ((wert*5*scale)+(x+20*scale));
					Swert = new Schrift(new String[]{String.valueOf(wert)}, x+(int)(schieberegler[2].getWidth()*scale)/2, true, (int) (y+(int)(schieberegler[0].getHeight()*scale)/2+60*scale), true, 0.45*scale, c);
					cooldown = 6;
					if(!isgedrueckt){
						cooldown = 30;
						Main.PlaySound(Main.click);
						isgedrueckt = true;
					}
				}
				
			}else if(Keyboard.isKeyDownCd(Main.steuerung[11].getKey(),3)){
				if(wert >= 0 && wert < 100){
					wert +=1;
					punktx = (int) ((wert*5*scale)+(x+20*scale));
					Swert = new Schrift(new String[]{String.valueOf(wert)}, x+(int)(schieberegler[2].getWidth()*scale)/2, true, (int) (y+(int)(schieberegler[0].getHeight()*scale)/2+60*scale), true, 0.45*scale, c);
					cooldown = 6;
					if(!isgedrueckt){
						cooldown = 30;
						Main.PlaySound(Main.click);
						isgedrueckt = true;
					}
				}
				
			}else if(isgedrueckt && cooldown <= 0){
				Main.PlaySound(Main.click);
				isgedrueckt = false;
			}
			
			if(cooldown > 0){cooldown -= Main.timeSinceLastFrame;}
			
		}else{
			if(isgedrueckt){
				if(Main.f.MousePress()){
					int temp = (int) (Main.f.getMousePos(0));
					if(temp >= x+20*scale+(int)(schieberegler[0].getWidth()*scale)/2 && temp <= x+(int)(schieberegler[2].getWidth()*scale)-20*scale-(int)(schieberegler[0].getWidth()*scale)/2){
						punktx = (int) (Main.f.getMousePos(0))-(int)(schieberegler[0].getWidth()*scale)/2;
						wert = (int)((punktx-(x+20*scale)+2.5*scale)/(5*scale));
						Swert = new Schrift(new String[]{String.valueOf(wert)}, x+(int)(schieberegler[2].getWidth()*scale)/2, true, (int) (y+(int)(schieberegler[0].getHeight()*scale)/2+60*scale), true, 0.45*scale, c);
					}
				}else{
					Main.PlaySound(Main.click);
					isgedrueckt = false;
				}
			}else{
				if(!deaktiviert){
					try{
						if(Main.f.getMousePos(0) >= punktx && Main.f.getMousePos(0) <= punktx+width && Main.f.getMousePos(1) >= y && Main.f.getMousePos(1) <= y+height && Main.f.MousePress()){
							//Main.f.MouseReset();
							Main.PlaySound(Main.click);
							
							isgedrueckt = true;
						}
					}catch(Exception e){}
				}
			}
		}
	}
	
	public BufferedImage getPunkt(){
		if(deaktiviert2){return schieberegler[1];}
		if(ausgewaehlt){return schieberegler[0];}
		if(!deaktiviert && Main.f.getMouse()){
			try{
				if(Main.f.getMousePos(0) >= punktx && Main.f.getMousePos(0) <= punktx+width && Main.f.getMousePos(1) >= y && Main.f.getMousePos(1) <= y+height){
					return schieberegler[0];
				}
			}catch(Exception e){}
			return schieberegler[1];
		}
		return schieberegler[1];
	}
	
	public BufferedImage getLine(){
		return schieberegler[2];
	}
	
	public void setX(int i){x = i;}
	public void setY(int i){y = i;}
	
	public int getRealX(){return x;}
	public int getRealY(){return y;}
	
	public int getRealPunktX(){return punktx;}
	
	public boolean isRegel(){return isgedrueckt;}
	
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public void setWidth(int i){width = i;}
	public void setHeight(int i){height = i;}
	
	public String getName(){return name;}
	
	public void setDeaktiviert(){deaktiviert = true;}
	public void ResetDeaktiviert(){deaktiviert = false;}
	
	public void setDeaktiviert2(){if(!deaktiviert){deaktiviert2 = true;}}
	public void ResetDeaktiviert2(){deaktiviert2 = false;}
	
	public void setAusgewaehlt(boolean bol){ausgewaehlt = bol;}
	public boolean isAusgewaehlt(){return ausgewaehlt;}
	public void setGedrueckt(boolean bol){gedrueckt = bol;}
	public boolean isGedrueckt(){return gedrueckt;}

	public float getScale(){return scale;}
	
	public double getNX(){return netzX;}
	public double getNY(){return netzY;}
	
	public void setNX(double i){netzX = i;}
	public void setNY(double i){netzY = i;}
	
	public int getPage(){return page;}
	
	public Schrift getText(){return text;}
	
	public int getWert(){return wert;}
	public Schrift getSWert(){return Swert;}
}
