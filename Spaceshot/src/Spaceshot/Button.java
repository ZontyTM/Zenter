package Spaceshot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
	
public class Button {
	private BufferedImage[] button = new BufferedImage[3];
	private int x, y, width, height, what, page;
	private static int lang;
	private double netzX, netzY, menu;
	private boolean deaktiviert, deaktiviert2, ausgewaehlt, gedrueckt, visible;
	private String name[], name2[];
	private float scale;
	private Schrift text, text2;
	

	public Button(int x, int y, int width, int height, double netzX, double netzY){
		this(null, null, null, null, Main.StandardWidth/2-width+x, Main.StandardHeight/2-height+y, null, 1, netzX, netzY, "Normal", -1);
		this.width = width;
		this.height = height;
	}
	
	public Button(String name[], int x, int y, String buttontext, float scale, double netzX, double netzY){
		this(name, Color.BLACK, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, "", -1);
	}
	
	public Button(String name[], int x, int y, String buttontext, float scale, double netzX, double netzY, int page){
		this(name, Color.BLACK, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, "", page);
	}
	
	public Button(String name[], int x, int y, String buttontext, float scale, double netzX, double netzY, String what){
		this(name, Color.BLACK, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, what, -1);
	}
	
	public Button(String name[], int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int page){
		this(name, Color.BLACK, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, what, page);
	}
	
	public Button(String name[], String name2[], int x, int y, String buttontext, float scale, double netzX, double netzY){
		this(name, Color.BLACK, name2, new Color(195, 195, 195), x, y, buttontext, scale, netzX, netzY, "", -1);
	}
	
	public Button(String name[], String name2[], int x, int y, String buttontext, float scale, double netzX, double netzY, int page){
		this(name, Color.BLACK, name2, new Color(195, 195, 195), x, y, buttontext, scale, netzX, netzY, "", page);
	}
	
	public Button(String name[], String name2[], int x, int y, String buttontext, float scale, double netzX, double netzY, String what){
		this(name, Color.BLACK, name2, new Color(195, 195, 195), x, y, buttontext, scale, netzX, netzY, what, -1);
	}
	
	public Button(String name[], String name2[], int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int page){
		this(name, Color.BLACK, name2, new Color(195, 195, 195), x, y, buttontext, scale, netzX, netzY, what, page);
	}
	
	public Button(String name[], Color c, int x, int y, String buttontext, float scale, double netzX, double netzY){
		this(name, c, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, "", -1);
	}
	
	public Button(String name[], Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, int page){
		this(name, c, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, "", page);
	}
	
	public Button(String name[], Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what){
		this(name, c, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, what, -1);
	}
	
	public Button(String name[], Color c, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int page){
		this(name, c, null, Color.BLACK, x, y, buttontext, scale, netzX, netzY, what, page);
	}
	
	public Button(String name[], Color c, String name2[], Color c2, int x, int y, String buttontext, float scale, double netzX, double netzY){
		this(name, c, name2, c2, x, y, buttontext, scale, netzX, netzY, "", -1);
	}
	
	public Button(String name[], Color c, String name2[], Color c2, int x, int y, String buttontext, float scale, double netzX, double netzY, int page){
		this(name, c, name2, c2, x, y, buttontext, scale, netzX, netzY, "", page);
	}
	
	public Button(String name[], Color c, String name2[], Color c2, int x, int y, String buttontext, float scale, double netzX, double netzY, String what){
		this(name, c, name2, c2, x, y, buttontext, scale, netzX, netzY, what, -1);
	}
	
	public Button(String[] name, Color c, String[] name2, Color c2, int x, int y, String buttontext, float scale, double netzX, double netzY, String what, int page){
		if(!what.contains("Pic")){
			if(buttontext != null) {
				try {
					button[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Buttons/Button" + buttontext + "A.png"));
					button[1] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Buttons/Button" + buttontext + "D.png"));
				} catch (IOException e) {e.printStackTrace();}
				try {
					button[2] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/Buttons/Button" +buttontext + "KD.png"));
				} catch (Exception e) {
					button[2] = button[1];
				}
			}	
		}else {
			this.what += 3;
			setPic(buttontext);
		}
		
		if(what.contains("Normal")){
			this.x = x;
			this.y = y;
			
		}else{
			if(!what.contains("EndX")){this.x = Main.StandardWidth/2-(int)(button[0].getWidth()*scale)/2+x;}
			if(!what.contains("EndY")){this.y = Main.StandardHeight/2-(int)(button[0].getHeight()*scale)/2+y;}
		}
		int mid = 1;
		if(what.contains("Mid")){
			mid = 2;
		}
		if(what.contains("EndX")){
			this.x = Main.StandardWidth - (int)(button[0].getWidth()*scale)/mid + x;
		}else if(what.contains("EndY")){
			this.y = Main.StandardHeight - (int)(button[0].getHeight()*scale)/mid + y;
		}else if(what.contains("End")){
			this.x = Main.StandardWidth - (int)(button[0].getWidth()*scale)/mid + x;
			this.y = Main.StandardHeight - (int)(button[0].getHeight()*scale)/mid + y;
		}
		
		if(what.contains("Back")){
			this.what += 2;
		
		}else if(what.contains("Hin")){
			this.what += 1;
		}
		if(what.contains("Back") || what.contains("Hin")){
			char temp[] = what.toCharArray();
			String temp2 = "";
			for(int i = 0; i < temp.length; i++){
				if(!temp2.equals("")){temp2 += temp[i];}
				if(what.contains("Hin")){
					if(i >= 3 && temp[i-3] == 'H' && temp[i-2] == 'i' && temp[i-1] == 'n'){
						temp2 += temp[i];
					}
				}else if(what.contains("Back")){
					if(i >= 4 && temp[i-4] == 'B' && temp[i-3] == 'a' && temp[i-2] == 'c' && temp[i-1] == 'k'){
						temp2 += temp[i];
					}
				}
			}
			this.menu = Double.valueOf(temp2);
		}
		this.deaktiviert = false;
		this.deaktiviert2 = false;
		this.visible = false;
		if(buttontext != null) {
			this.width = (int)(button[0].getWidth()*scale);
			this.height = (int)(button[0].getHeight()*scale);
		}
		this.name = name;
		if(name != null) {
			if(what.contains("SchriftOben")){
				this.text = new Schrift(name, this.x, this.y-(int)(27*scale), 0.3*scale, c);
			}else{
				this.text = new Schrift(name, this.x+this.width/2, true, this.y+this.height/2, true, 0.45*scale, c);
			}
			if(name2 != null){
				this.name2 = name2;
				this.text2  = new Schrift(name2, this.x, this.y-(int)(27*scale), 0.3*scale, c2);
			}
		}
		this.scale = scale;
		this.netzX = netzX;
		this.netzY = netzY;
		if(page != -1){this.page = page;}
	}
	
	public boolean isDrueck(){
		if(((what == 1 || what == 4) && Keyboard.isKeyDownCd(Main.steuerung[7].getKey())) || ((what == 2 || what == 5) && Keyboard.isKeyDownCd(Main.steuerung[6].getKey()))){
			setNetz();
			return true;
		}
		if(deaktiviert2 || visible){return false;}
		if(gedrueckt){
			gedrueckt = false;
			Keyboard.setCooldown(Main.steuerung[6].getKey());
			setNetz();
			Main.PlaySound(Main.click);
			return true;
		}
		if(!deaktiviert){
			try{
				if(isOver() && Main.f.MousePressCd()){
					//Main.f.MouseReset();
					setNetz();
					Main.PlaySound(Main.click);
					return true;
				}
			}catch(Exception e){}
		}
		return false;
	}
	private void setNetz() {
		if(what == 1 || what == 4){
			Main.buttonXA = netzX;
			Main.buttonYA = netzY;
			Main.buttonX = 1;
			Main.buttonY = 1;
		
		}else if(what == 2 || what == 5) {
			Main.buttonX = Main.buttonXA;
			Main.buttonY = Main.buttonYA;
		}
		if(what != 0 && what != 3){
			Main.menu = menu;
		}
		if(what == 2 || what == 5){
			Main.saveSettings();
		}
	}
	
	public BufferedImage getButton(){
		if(visible){return null;}
		if(what >= 3) {return button[0];}
		if(deaktiviert2){return button[1];}
		if(ausgewaehlt){return button[0];}
		if(!deaktiviert){
			//System.out.println((x+Main.edgeX) + " " + (x+width+Main.edgeX) + " " + (y+Main.edgeY) + " " + (y+height+Main.edgeY));
			try{
				if(isOver()){
					return button[0];
				}
			}catch(Exception e){}
			return button[1];
		}
		return button[2];
	}
	
	public boolean isOver() {
		if(Main.f.getMousePos(0) >= x && Main.f.getMousePos(0) <= (x+width)
				&& Main.f.getMousePos(1) >= y && Main.f.getMousePos(1) <= (y+height)
				&& Main.f.getMouse()){
			return true;
		}return false;
	}
	
	public void setX(int i){x = i;}
	public void setY(int i){y = i;}
	
	public int getX(){return x;}
	public int getY(){return y;}
	
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public void setWidth(int i){width = i;}
	public void setHeight(int i){height = i;}
	
	public String getName(){
		if(name != null) {
			if(name.length == 1) {
				return name[0];
			}
			return name[lang];
		}else {
			return null;
		}
	}
	public String getName2(){
		if(name2 != null) {
			if(name2.length == 1) {
				return name2[0];
			}
			return name2[lang];
		}else {
			return null;
		}
	}
	
	public void setDeaktiviert(){deaktiviert = true;}
	public void ResetDeaktiviert(){deaktiviert = false;}
	
	public void setDeaktiviert2(){if(!deaktiviert){deaktiviert2 = true;}}
	public void ResetDeaktiviert2(){deaktiviert2 = false;}
	
	public void setVisible(boolean b){visible = b;}
	public boolean getVisible(){return visible;}
	
	public void setAusgewaehlt(boolean bol){ausgewaehlt = bol;}
	public boolean isAusgewaehlt(){return ausgewaehlt;}
	public void setGedrueckt(boolean bol){gedrueckt = bol;}
	public boolean isGedrueckt(){return gedrueckt;}

	public float getScale(){return scale;}
	
	public double getNX(){return netzX;}
	public double getNY(){return netzY;}
	
	public void setNX(double i){netzX = i;}
	public void setNY(double i){netzY = i;}
	
	public int getWhat(){return what;}
	
	public int getPage(){return page;}
	
	public Schrift getText(){if(visible){return null;} return text;}
	public void setText(String s[]){text = new Schrift(s, x+(int)(button[0].getWidth()*scale)/2, true, y+(int)(button[0].getHeight()*scale)/2, true, 0.45*scale);}
	public Schrift getText2(){if(visible){return null;} return text2;}
	public void setText2(String s[]){text2 = new Schrift(s, x, (int) (y-23*scale), 0.3*scale);}
	
	public void setPic(String text){
		try {
			button[0] = ImageIO.read(Button.class.getClassLoader().getResourceAsStream("Textures/" + text + ".png"));
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public static void setLang(int l) {
		if(l>1) {
			lang = 0;
		}else {
			lang = l;
		}
	}
	public static int getLang() {
		return lang;
	}
}
