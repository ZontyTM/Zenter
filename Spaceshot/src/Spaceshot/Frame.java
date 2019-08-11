package Spaceshot;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Frame extends JFrame{
	
	private boolean click = false, press = false, Mouse = true, fps = false;
	private float MousePos[] = new float[2], fpsCd;
	private double scaleX = 1, scaleY = 1;
	private int MouseCd, mPressCd, Tfps;
	private static Graphics g;
	private static Graphics2D g2d;
	private static Cursor leer, cursor;
	private BufferStrategy strat;
	private Player p;
	private Background b;
	private Font font;
	
	public Frame(Background b, Player p){
		super("Spaceshot");
		addKeyListener(new Keyboard());
		addMouseListener(new MouseHandler());
		
		this.p = p;
		this.b = b;
		
		BufferedImage bild = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bild.createGraphics();
		g.setColor(new Color(0, 0, 0, 0));
		g.fillRect(0, 0, 32, 32);
		g.dispose();
		
		this.font = g.getFont().deriveFont(36f);
				
		leer = Toolkit.getDefaultToolkit().createCustomCursor(bild, new Point(0,0), "Leer");
		try {
			cursor = Toolkit.getDefaultToolkit().createCustomCursor(ImageIO.read(Background.class.getClassLoader().getResourceAsStream("Textures/Cursor.png")), new Point(0,0), "Cursor");
		} catch (Exception e) {}
	}
	
	public void setCursorWeg(){setCursor(leer);}
	public void setCursor(){setCursor(cursor);}
	public void ResetCursor(){setCursor(null);}
	
	public Background getBilder(){return b;}

	public void makeStrat(){
		createBufferStrategy(2);
		strat = getBufferStrategy();
	}
	
	public void repaintScreen(){
		g = strat.getDrawGraphics();
		draw(g);
		g.dispose();
		strat.show();
	}
	
	public boolean MousePress(){return press;}
	public boolean MousePressCd(){
		return MousePressCd(40);
	}
	public boolean MousePressCd(int cd){
		if(mPressCd > 0){
			mPressCd -= Main.timeSinceLastFrame;
		
		}else if(mPressCd <= 0 && press == true){
			mPressCd = cd;
			return true;
		}
		return false;
	}
	public boolean MouseClick(){return click;}
	public void MouseReset(){click = false;}
	
	public void Mouse() {
		int temp[] = new int[2];
		temp[0] = (int)(MousePos[0]);
		temp[1] = (int)(MousePos[1]);
		try{
			MousePos[0] = (float)((getMousePosition().getX()-Main.edgeX)/(scaleX-0.005));
			MousePos[1] = (float)((getMousePosition().getY()-Main.edgeY)/(scaleY-0.005));
		}catch(Exception e){}
		
		if(((int)(MousePos[0]) == temp[0]) && ((int)(MousePos[1]) == temp[1]) && !press){
			if(MouseCd <= 0){MouseCd = 2000;}
		}else{
			MouseCd = -1;
			Mouse = true;
		}
		
        if(MouseCd > 0 && Mouse){
        	MouseCd -= Main.timeSinceLastFrame;
        	if(MouseCd <= 0){
        		Mouse = false;
        	}
        }
        
        if(!Mouse){setCursorWeg();}
        else{setCursor();}
	}
	public boolean getMouse(){return Mouse;}
	public void setMouse(boolean b){Mouse = b;}
	public int getMousePos(int i) {
		return (int)(MousePos[i]);
	}
	
	public double getScale(int i) {
		double temp[] = {scaleX, scaleY};
		return temp[i];
	}
	public void setScale(double x, double y) {
		this.scaleX = x;
		this.scaleY = y;
	}
    
	private void draw(Graphics g){
		g2d = (Graphics2D) g;

        g2d.scale(scaleX, scaleY);
		
		if(Main.menu != -1) {
			BildDraw((int)b.getBackgroundX(), 0, b.getBackground(b.getActbg()));
			BildDraw((int)b.getBackgroundX2(), 0, b.getBackground(b.getActbg()));
		}
		
		if(Main.menu == 0){
			TextDraw(Main.SHead[0]);
			
			for(int i = 0; i < Main.Buttons.length; i++){
				ButtonDraw(Main.Buttons[i]);
			}
			
			//BildDraw(Main.Buttons[3].getButton());
			//BildDraw(Main.BGame[3].getText());
		}else if(Main.menu == 0.1){
			TextDraw(Main.SHead[1]);

			for (int i = 0; i < Main.BShop.length-1; i++) {
				if(i == 8 && Main.pow[i].getCd()<0) {
					ButtonDraw(Main.BShop[i], Main.pow[i].getPowerup(),true);
				}else {
					ButtonDraw(Main.BShop[i], Main.pow[i].getPowerup(),false);
				}
			}
			ButtonDraw(Main.BShop[Main.BShop.length-1]);
			
			for (int i = 0; i < Shop.getLength(); i++) {
				if(Shop.isSale(i)) {
					BildDraw(Main.BShop[i].getX(), Main.BShop[i].getY()-5, Shop.getSale());
				}
				
				for (int j = 0; j < Shop.getReroll(i); j++) {
					BildDraw(Main.StandardWidth/2-Shop.getUpgrade(1).getWidth()/2-100+(j*100), Main.BShop[i].getY()+10, Shop.getUpgrade(1));
				}
				for (int j = Shop.getReroll(i); j < 3; j++) {
					BildDraw(Main.StandardWidth/2-Shop.getUpgrade(0).getWidth()/2-100+(j*100), Main.BShop[i].getY()+10, Shop.getUpgrade(0));
				}
				
				int distance = Main.StandardWidth/2+170;
				for (int c = 0; c < 5; c++) {
					for (int j = 0; j < Shop.getCost(i,c); j++) {
						if(j%10 == 0) {
							distance+=15;
						}else if(j%5 == 0) {
							distance+=10;
						}
						distance += 5;
						BildDraw(distance, Main.BShop[i].getY()+20, p.getCoin(c), 0.6f);
					}
				}
			}
			
			int temp = 2;
			if(Main.p.getCoinC()) {
				temp = 3;
			}else {
				BildDraw(Main.StandardWidth/2+190, Main.BShop[Shop.getLength()].getY()+10, p.getCoin(0), 0.6f);
			}
			BildDraw(Main.StandardWidth/2-Shop.getUpgrade(temp).getWidth()/2, Main.BShop[Shop.getLength()].getY()-5, Shop.getUpgrade(temp));
			
			int distance = 5;
			for (int c = 0; c < 5; c++) {
				for (int i = 0; i < p.getCoins(c); i++) {
					if(i%10 == 0) {
						distance+=15;
					}else if(i%5 == 0) {
						distance+=10;
					}
					distance += 5;
					BildDraw(distance, 30, p.getCoin(c), 0.6f);
				}
			}
		
		}else if(Main.menu == 1){
			TextDraw(Main.SHead[6]);
			for(int i = 0; i < Main.BRaumschiff.length; i++){
				ButtonDraw(Main.BRaumschiff[i]);
			}

			for (int i = 0; i < 2; i++) {
				BildDraw(p.getX(), p.getY(), p.getRaumschiff(i), 0.75f);
			}
			
			for(int i = 0; i < Main.SRColor.length; i++){
				SchiebereglerDraw(Main.SRColor[i]);
			}
			
		}else if(Main.menu == 1.1){
			
			if(p.getLives() != 0) {

				TextDraw(Main.BGame[0].getText());
				
				for (int i = 0; i < Main.bullets.size(); i++) {
					BildDraw(Main.bullets.get(i).getX(), Main.bullets.get(i).getY(), Main.bullets.get(i).getPic(), Main.bullets.get(i).getScale());
				}
				if(Main.enemys.size() != 0) {
					for (int i = (Main.enemys.size()-1); i >= 0; i--) {
						BildDraw(Main.enemys.get(i).getX(), Main.enemys.get(i).getY(), Main.enemys.get(i).getPic(), Main.enemys.get(i).getScale());
						if(Main.enemys.get(i).isBoss()) {
							int tempX = Main.enemys.get(i).getX()+(8-Main.enemys.get(i).getMaxLife())*10;
							if(Enemy.getCount() > 1) {g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));}
							else {g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));}
							
							BildDraw(tempX, Main.enemys.get(i).getY()-20,Main.enemys.get(i).getBossbarBg(), 20*Main.enemys.get(i).getMaxLife(),
									Main.enemys.get(i).getScale()*3);
							
							BildDraw(tempX, Main.enemys.get(i).getY()-20,Main.enemys.get(i).getBossbar(), 
									20*Main.enemys.get(i).getLife(), Main.enemys.get(i).getScale()*3);
							
							BildDraw(tempX-1, Main.enemys.get(i).getY()-20-1,Main.enemys.get(i).getBossbarFg(), 
									(Main.enemys.get(i).getMaxLife()*0.125f), 2f);
							
							g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
							
							if(Enemy.getCount() > 1) {
								g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Main.enemys.get(i).getActLightning()));
								BildDraw(Main.enemys.get(i).getX(), Main.enemys.get(i).getY(), Main.enemys.get(i).getLightning(), Main.enemys.get(i).getScale());
								g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
							}
						}
					}
				}
				
				int cnt = 0;
				for (int i = 0; i < Main.pow.length; i++) {
					if(Main.pow[i].exist()) {
						if(!Main.pow[i].hasPlayer()) {
							if(i == 8 && Main.pow[i].getCd()<0) {
								BildDrawFlip(Main.pow[i].getX(),Main.pow[i].getY(), Main.pow[i].getPowerup());
							}else {
								BildDraw(Main.pow[i].getX(), Main.pow[i].getY(), Main.pow[i].getPowerup());
							}
						}else {
							BildDraw(20, 180 + cnt * (48 + 7), Main.pow[i].getPowerup(), 0.75f);
							TextDraw((48 + 30),187 + cnt * (48 + 7), String.valueOf((int)(Main.pow[i].getCd())), Color.YELLOW);
							cnt += 1;
						}
					}
				}
				for (int i = 0; i < 2; i++) {
					BildDraw(p.getX(), p.getY(), p.getRaumschiff(i), p.getScale());
					if(p.isHit()) {
						break;
					}
				}
				if(Main.pow[1].hasPlayer()) {BildDraw(p.getX()+(int)((p.getRaumschiff().getWidth()-p.getShield().getWidth())*p.getScale()/2)-10, 
						p.getY()+(int)((p.getRaumschiff().getHeight()-p.getShield().getHeight())*p.getScale()/2), 
						p.getShield(), p.getScale());}
				
				for (int i = 1; i <= p.getLives(); i++) {
					BildDraw(20+(i-1)*60, 20, p.getHeart(i), 0.45f);
				}
				if(Main.pow[1].hasPlayer()) {BildDraw(20+(p.getLives())*60, 20, p.getHeart(p.getLives()+1), 0.45f);}
				
				for (int i = 0; i < p.getShots(); i++) {
					BildDraw(27+i*10, 95, p.getShot(), 0.5f);
				}
				
				int distance = 5;
				for (int c = 0; c < 5; c++) {
					for (int i = 0; i < p.getCoins(c); i++) {
						if(i%10 == 0) {
							distance+=15;
						}else if(i%5 == 0) {
							distance+=10;
						}
						distance += 5;
						BildDraw(distance, 150, p.getCoin(c), 0.6f);
					}
				}
				
			}else {
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, p.getCd()*0.2f));
				BildDraw(p.getX()-(int)((p.getExplosion().getWidth()/2)*p.getScale()), 
						p.getY()-(int)((p.getExplosion().getHeight()/2)*p.getScale()), 
						p.getExplosion(), p.getScale());
			}
			/*if(Main.bullets.size() != 0) {
				g2d.setColor(Color.WHITE);
				g2d.drawRect((int)(Main.bullets.get(0).getBounding().x*scaleX)+Main.edgeX,(int)(Main.bullets.get(0).getBounding().y*scaleY)+Main.edgeY,(int)(Main.bullets.get(0).getBounding().getWidth()*scaleX),(int)(Main.bullets.get(0).getBounding().getHeight()*scaleY));
			}*/
			//g2d.drawRect((int)(p.getBounding().x*scaleX)+Main.edgeX,(int)(p.getBounding().y*scaleY)+Main.edgeY,(int)(p.getBounding().getWidth()*scaleX),(int)(p.getBounding().getHeight()*scaleY));
			//g2d.drawOval((int)((p.getX()+33*0.75f)*scaleX)+Main.edgeX, (int)((p.getY()+50*0.75f)*scaleY)+Main.edgeY, (int)(202*0.75f*scaleX), (int)(155*0.75f*scaleY));

			//TextDraw(new Schrift("X: " + p.getX() + "  Y: " + p.getY(),20,20,1f,Color.WHITE));
		
		}else if(Main.menu == 2){
			TextDraw(Main.SHead[2]);
			for(int i = 0; i < Main.BOptionsMenu.length; i++){
				ButtonDraw(Main.BOptionsMenu[i]);
			}
			
		}else if(Main.menu == 2.1){
			TextDraw(Main.SHead[3]);
			ButtonDraw(Main.BOptions[0]);
			
			if(Main.lastseite != 1 && Mouse){
				for(int i = 0; i < Main.BSideSelection.length; i++){
					ButtonDraw(Main.BSideSelection[i]);
				}
			}
			
			for(int w = 0; w < Main.steuerung.length; w++){
				if(Main.seite == Main.steuerung[w].getButton().getPage()){
					KeyDraw(Main.steuerung[w]);
				}
			}
			
			for(int i = 0; i < Main.BOptions.length; i++){
				ButtonDraw(Main.BOptions[i]);
			}
			
		}else if(Main.menu == 2.2){
			TextDraw(Main.SHead[4]);
			for(int i = 0; i < Main.BSounds.length; i++){ButtonDraw(Main.BSounds[i]);}	
			
			for(int i = 0; i < Main.SRMaster.length; i++){SchiebereglerDraw(Main.SRMaster[i]);}
			
		}else if(Main.menu == 2.3){
			TextDraw(Main.SHead[5]);
			for(int i = 0; i < Main.BGrafik.length; i++){
				ButtonDraw(Main.BGrafik[i]);
			}
			
		}else if(Main.menu == -1){
			BildDraw(0, 0, b.getBackground(7));
			for(int i = 0; i < Main.BEnde.length; i++){ButtonDraw(Main.BEnde[i]);}
			TextDraw(Main.SEnde[0]);
			if(!Mouse){for(int i = 1; i < Main.SEnde.length; i++){TextDraw(Main.SEnde[i]);}}
		}
		
		//TextDraw(20, 100,"X: " + MousePos[0] + "  Y: " + MousePos[1]);
		//TextDraw(new Schrift("Time: " + Main.dif,20,20,1f,Color.WHITE));
		//TextDraw(20, 100,"X: " + Main.buttonX + "  Y: " + Main.buttonY);
		if(Keyboard.isKeyDownCd(KeyEvent.VK_F3)) {
			if(fps) {fps = false;}
			else {fps = true;}
		}
		if(fps) {
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
			g2d.setColor(Color.BLACK);
			g2d.fillRect((int)(Main.edgeX/scaleX), (int)(Main.edgeY/scaleY),Main.StandardWidth, Main.StandardHeight);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
			
			if(fpsCd > 0) {fpsCd-=Main.timeSinceLastFrame;}
			else {
				fpsCd = 0.5f; 
				Tfps = (int)(1/(Main.timeSinceLastFrame));
			}
			for (int i = 0; i < Achievement.count(); i++) {
				Color tempC = null;
				if(Achievement.isEnd(i)) {tempC = new Color(100, 255, 100);}
				TextDraw(10, 200+i*30, Achievement.getText(i) + " (" + Achievement.get(i) + "/" + Achievement.getEnd(i) + ")", 0.7f, tempC);
			}
			if(Main.enemys.size() != 0 && Enemy.getBoss()) {
				TextDraw(10, 710,"Count: " + Enemy.getCount());
				TextDraw(10, 760,"Live: " + Main.enemys.get(0).getLife());
			}
			TextDraw(10, 810,"Cd: " + Main.p.getCd2());
			TextDraw(10, 860,"FPS: " + Tfps);
			TextDraw(10, 910,"Time: " + Main.time);
			TextDraw(10, 960,"Dif: " + Main.dif);
			TextDraw(10, 1010,"Kills: " + Main.p.getKills());
		}
	}
	
	private class MouseHandler implements MouseListener{
		public void mouseClicked(MouseEvent m) {}
		public void mouseEntered(MouseEvent m) {}
		public void mouseExited(MouseEvent m) {}
		public void mousePressed(MouseEvent m) {press = true;}
		public void mouseReleased(MouseEvent m) {press = false; click = true;}
	}

	public void BildDrawFlip(int x, int y, BufferedImage img){
		BildDrawFlip(x,y,img,1);
	}
	public void BildDrawFlip(int x, int y, BufferedImage img, double scale){
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		img = op.filter(img, null);
		
		BildDraw(x,y,img, scale);
	}
	public void BildDraw(int x, int y, BufferedImage img){
		BildDraw(x,y,img,1);
	}
	public void BildDraw(int x, int y, BufferedImage img, double scale){
		if(x >= -img.getWidth() && x <= Main.StandardWidth && y >= -img.getHeight() && y <= Main.StandardHeight) {
			AffineTransform at = AffineTransform.getTranslateInstance(x+Main.edgeX/scaleX, y+Main.edgeY/scaleY);
			at.scale(scale, scale);
			g2d.drawImage(img, at, null);
		}
	}
	public void BildDraw(int x, int y, BufferedImage img, double iscaleX, double iscaleY){
		if(x >= -img.getWidth() && x <= Main.StandardWidth && y >= -img.getHeight() && y <= Main.StandardHeight) {
			System.out.println();
			AffineTransform at = AffineTransform.getTranslateInstance(x+Main.edgeX/scaleX, y+Main.edgeY/scaleY);
			at.scale(iscaleX, iscaleY);
			g2d.drawImage(img, at, null);
		}
	}
	
	public void ButtonDraw(Button b){
		ButtonDraw(b,null,false);
	}
	public void ButtonDraw(Button b, BufferedImage img, boolean bol){
		AffineTransform at;
		if(bol) {
			at = AffineTransform.getScaleInstance(-1, 1);
			at.translate(-img.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			img = op.filter(img, null);
		}
		if(b.isAusgewaehlt() || b.isOver()) {
			at = AffineTransform.getTranslateInstance(b.getX()+Main.edgeX/scaleX-b.getWidth()*0.05f, b.getY()+Main.edgeY/scaleY-b.getHeight()*0.05f);
			at.scale(b.getScale()*1.1, b.getScale()*1.1);
		}else {
			at = AffineTransform.getTranslateInstance(b.getX()+Main.edgeX/scaleX, b.getY()+Main.edgeY/scaleY);
			at.scale(b.getScale(), b.getScale());
		}
		if(img==null) {
			g2d.drawImage(b.getButton(), at, null);
		}else {
			g2d.drawImage(img, at, null);
		}
		if(b.getName() != null){TextDraw(b.getText());}
        if(b.getName2() != null){TextDraw(b.getText2());}
	}
    public void ButtonKeyDraw(Button b, Schrift text){
		ButtonDraw(b);
    	TextDraw(text);
        TextDraw(b.getText());
    }
    public void ButtonKeyDraw(Button b, Schrift text, Schrift textu){
		AffineTransform at = AffineTransform.getTranslateInstance(b.getX()+Main.edgeX/scaleX, b.getY()+Main.edgeY/scaleY);
		at.scale(b.getScale(), b.getScale());
		g2d.drawImage(b.getButton(), at, null);
        TextDraw(b.getText());;
        TextDraw(text);
        TextDraw(textu);
    } 
    
    public void SchiebereglerDraw(Slide_control s){
		AffineTransform at = AffineTransform.getTranslateInstance(s.getRealX()+Main.edgeX/scaleX, s.getRealY()+Main.edgeY/scaleY);
		at.scale(s.getScale(), s.getScale());
		g2d.drawImage(s.getLine(), at, null);
		
		AffineTransform at2 = AffineTransform.getTranslateInstance(s.getRealPunktX()+Main.edgeX/scaleX, s.getRealY()+Main.edgeY/scaleY);
		at2.scale(s.getScale(), s.getScale());
		g2d.drawImage(s.getPunkt(), at2, null);
        
		TextDraw(s.getText());
		TextDraw(s.getSWert());
    }
    
    public void TextDraw(Schrift text){
    	if(text != null){
    		//text.setScale(scaleX, scaleY);
    		
    		for(int i = 0; i < text.getZeichenAnzahl(); i++){
    			AffineTransform at = AffineTransform.getTranslateInstance(text.getX(i)+Main.edgeX/scaleX, text.getY(i)+Main.edgeY/scaleY);
    			at.scale(text.getScale(), text.getScale());
    			g2d.drawImage(text.getImage(i), at, null);
    		}
    	}
    }
    public void TextDraw(int x, int y, String s) {
    	TextDraw(x, y, s, 1, null);
    }
    public void TextDraw(int x, int y, String s, Color c) {
    	TextDraw(x, y, s, 1, c);
    }
    public void TextDraw(int x, int y, String s, double scale) {
    	TextDraw(x, y, s, scale, null);
    }
    public void TextDraw(int x, int y, String s, double scale, Color c){
    	AffineTransform at = AffineTransform.getTranslateInstance(x+Main.edgeX/scaleX, y+Main.edgeY/scaleY);
    	at.scale(scale, scale);
        g2d.setFont(font.deriveFont(at));
    	if(c != null) {g2d.setColor(c);}
    	else {g2d.setColor(Color.WHITE);}
    	g2d.drawString(s, 0, (int)(30*scale));
    }
    
    public void KeyDraw(Control st){
    	if(!st.getBol()){
    		ButtonKeyDraw(st.getButton(), st.getSchrift());
        }
        else{
        	Main.SSteuerung.setXY(st.getButton().getX()+st.getButton().getWidth()/2, st.getButton().getY()+st.getButton().getHeight()/2, true);
    		ButtonKeyDraw(st.getButton(), Main.SSteuerung);
    	}
    }
}
