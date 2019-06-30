package Spaceshot;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Background{
	private float bildX, bildX2;
	private BufferedImage background[] = new BufferedImage[8];
	private String name[] = {"Default","Blue","Red","Gray","Colorful","Fade","Epic","End"};
	private Random r = new Random();
	private int actbg = r.nextInt(7);
	
	public Background(){
		try {	
			background[actbg] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Background/background_" + actbg + ".png"));
			background[7] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Background/background_" + 7 + ".png"));
		}catch(IOException e){e.printStackTrace();}
	}
	
	public BufferedImage getBackground(int i){
		return background[i];
	}
	public float getBackgroundX(){
		return bildX;
	}
	public float getBackgroundX2(){
		return bildX2;
	}
	public void updateBackgroundX(int i) {
		//bildX-=3;
		if(Main.menu == 1.1) {
			bildX-=300*Main.timeSinceLastFrame;
		}else {
			bildX-=150*Main.timeSinceLastFrame;
		}
		try{
			bildX2 = bildX+background[i].getWidth();
			if(bildX < -(background[i].getWidth())){bildX+=background[i].getWidth();}
		}catch(Exception e) {}
	}

	public String getName(){
		return name[actbg];
	}
	public int getActbg(){
		return actbg;
	}
	public void setActbg(int i){
		actbg = i;
		try {
			background[actbg] = ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Background/background_" + actbg + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
