package Spaceshot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Schrift {
	private BufferedImage zeichen[];
	private int x[], y[], width, height, startwidth, startheight;
	private double scale;
	private boolean repaint;
	private String text;
	
	public Schrift(String s[], double scale){
		this(s, 0, false, 0, false, scale);
	}
	
	public Schrift(String s[], int x, int y, double scale){
		this(s, x, false, y, false, scale);
	}
	
	public Schrift(String s[], int x, int y, double scale, Color c){
		this(s, x, false, y, false, scale, c);
	}
	
	public Schrift(String s[], int x, boolean mittex, int y, boolean mittey, double scale){
		this(s, x, mittex, y, mittey, scale, Color.BLACK);
	}
	
	public Schrift(String s[], int x, boolean mittex, int y, boolean mittey, double scale, Color c){
		int temp = Button.getLang();
		if(s.length == 1) {
			temp = 0;
		}
		char[] text = s[temp].toCharArray();
		
		this.text = s[temp];
		this.zeichen = new BufferedImage[text.length];
		this.x = new int[text.length];
		this.y = new int[text.length];
		String datei = null;
		this.width = 0;
		int highY = 0;
		for(int i = 0; i < text.length; i++){
			if(text[i] != ' '){
				this.y[i] = y;
				if(text[i] == '"'){datei = "Anführungsstriche";}
				else if(text[i] == '\\'){datei = "Backslash";}
				else if(text[i] == ':'){datei = "Doppelpunkt"; this.y[i] = (int)(y + 20*scale);}
				else if(text[i] == '.'){datei = "Punkt"; this.y[i] = (int)(y + 56*scale);}
				else if(text[i] == '|'){datei = "Gerader Slash";}
				else if(text[i] == '+'){datei = "Plus";}
				else if(text[i] == '/'){datei = "Slash";}
				else if(text[i] == '*'){datei = "Sternchen";}
				else if(text[i] == 'ß'){datei = "sz";}
				else if(text[i] == 'Ä'){datei = "Ae"; this.y[i] = (int)(y - 20*scale);}
				else if(text[i] == 'ä'){datei = "ae (K)";}
				else if(text[i] == 'Ö'){datei = "Oe"; this.y[i] = (int)(y - 20*scale);}
				else if(text[i] == 'ö'){datei = "oe (K)";}
				else if(text[i] == 'Ü'){datei = "Ue"; this.y[i] = (int)(y - 20*scale);}
				else if(text[i] == 'ü'){datei = "ue (K)";}
				else if(text[i] == '?'){datei = "Fragezeichen";}
				else if(text[i] == '-'){datei = "-"; this.y[i] = (int)(y + 30*scale);}
				else{
					if(Character.isLetter(text[i]) && Character.isLowerCase(text[i])){
						datei = text[i] + " (K)";
						if(text[i] == 't'){this.y[i] = (int)(y + 10*scale);}
						else if(text[i] != 'b' && text[i] != 'd' && text[i] != 'f' && text[i] != 'h' && text[i] != 'i' && text[i] != 'k' && text[i] != 'l'){
							this.y[i] = (int)(y + 20*scale);
						}
						
					}else{
						datei = String.valueOf(text[i]);
					}
				}
				
				try {
					zeichen[i] = ImageIO.read(Schrift.class.getClassLoader().getResourceAsStream("Textures/Schrift/" + datei + ".png"));
				} catch (IOException e) {
					System.out.println(text[i]);
					e.printStackTrace();
				}
				this.x[i] = x + this.width;
				this.width += (zeichen[i].getWidth()*scale-17*scale);
				
				if(y + zeichen[i].getHeight()*scale >= highY){
					highY = (int)(y + zeichen[i].getHeight()*scale);
				}
				
				if(c != Color.BLACK){
					for (int z = 0; z < zeichen[i].getWidth(); z++) {
				    	for (int j = 0; j < zeichen[i].getHeight(); j++) {
				    		Color pixelfarbe = new Color(zeichen[i].getRGB(z, j), true);
				    		int rgb = new Color(c.getRed(), c.getGreen(), c.getBlue(), pixelfarbe.getAlpha()).getRGB();
				    		zeichen[i].setRGB(z, j, rgb);
				    	}
				    }
				}
			
			}else{
				this.width += (int)(60*scale);
			}
		}
		this.height = highY - y;
		this.scale = scale;
		this.startwidth = this.width;
		this.startheight = this.height;
		
		if(mittex){
			for(int i = 0; i < zeichen.length; i++){
				this.x[i] -= this.width/2;
			}
		}
		if(mittey){
			for(int i = 0; i < zeichen.length; i++){
				this.y[i] -= this.height/2;
			}
		}
		repaint = true;
	}
	
	public BufferedImage getImage(int i){return zeichen[i];}
	public int getZeichenAnzahl(){return zeichen.length;}
	
	public int getX(int i){return x[i];}
	public int getY(int i){return y[i];}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	
	public void setXY(int wertX, int wertY, boolean b){
		for(int i = 1; i < x.length; i++){
			x[i] -= x[0];
		}
		x[0] -= x[0];
		for(int i = 0; i < x.length; i++){
			if(b){x[i] += (wertX - startwidth/2);}
			else{x[i] += wertX;}
		}
		
		for(int i = 1; i < y.length; i++){
			y[i] -= y[0];
		}
		y[0] -= y[0];
		for(int i = 0; i < y.length; i++){
			if(b){y[i] += (wertY - startheight/2);}
			else{y[i] += wertY;}
		}
	}
	
	public double getScale(){return scale;}
	
	public void setScale(double scaleX, double scaleY){
		for(int i = 0; i < zeichen.length; i++){
			x[i] *= scaleX;
			y[i] *= scaleY;
		}
		width = (int)(startwidth * scaleX);
		height = (int)(startheight * scaleY);
	}
	
	public void setRepaint(boolean b){repaint = b;}
	public boolean getRepaint(){return repaint;}
	
	public String asString(){return text;}
}
