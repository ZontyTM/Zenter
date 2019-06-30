package Spaceshot;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Main {
		static boolean Windows, Mac,  gibtEs = false, SteuerungSet = false, set = false;
		
		static float timeSinceLastFrame, time, dif = 2;
		
		static int FrameX = 1280, FrameY = 720, SedgeY, SedgeX, edgeY, edgeX, whatmixer, seite = 1, lastseite = 1, 
				StandardWidth = 1920, StandardHeight = 1080, what, cooldown = 0, volume = 40,
				displayMode, vollbild = 0, cdevice = 0, color[] = {127,127,127};
		
		static double buttonY = 1, buttonX = 1, buttonY2, buttonX2, buttonYA, buttonXA, menu = 0;
		
		static long thisFrame;
		
		static Control[] steuerung = new Control[12];
		static Button Buttons[] = new Button[5], BShop[] = new Button[10], BOptions[] = new Button[3], BEnde[] = new Button[2], AllBOption[], BRaumschiff[] = new Button[2], BSteuerung[] = new Button[steuerung.length],
				AllBStart[], BSideSelection[] = new Button[2], BOptionsMenu[] = new Button[4], BSounds[] = new Button[2], BGrafik[] = new Button[9], BGame[] = new Button[1];
		static Slide_control SRMaster[] = new Slide_control[1], SRColor[] = new Slide_control[3];
		static Schrift SEnde[] = new Schrift[3], SSteuerung, SHead[] = new Schrift[7];
		//static PlayController kontroller = new PlayController();
		static Player p = new Player();
		static List<Bullet> bullets = new LinkedList<Bullet>();
		static List<Enemy> enemys = new LinkedList<Enemy>();
        static List<Line> line = new LinkedList<Line>();
    	static List<Mixer> zmixers = new LinkedList<Mixer>();
 	    static String szmixers[];
		static Powerup pow[] = new Powerup[10];
		static Background b = new Background();
		static Mixer audio;
		static String audiogeret = null;
		static File file, click, shot, explosion;
		static Frame f;
		static DisplayMode displayModes[];
		static GraphicsDevice devices[];
		static Random r = new Random();
	
	public static void main(String[] args) {
        if(System.getProperty("os.name").contains("Windows")){
        	Windows = true; 
        	file = new File(".save");
        	SedgeY = 31;
        	SedgeX = 8;
        }
        else if(System.getProperty("os.name").contains("Mac")){
        	Mac = true; 
        	file = new File("save");
        	SedgeY = 0;
        	SedgeX = 0;
        }
    	edgeY = SedgeY;
    	edgeX = SedgeX;
        
        Frame();
        
		long lastFrame = System.currentTimeMillis();
		
		setLang();
		
		setControl();
		
		Shop.set();

		loadSettings();
		
		setSound();
		
		for (int i = 0; i < pow.length; i++) {
			pow[i] = new Powerup(i);
		}
		
		setButtons();
        
        p.setPos((int)(Main.StandardWidth/1.4-(p.getRaumschiff().getWidth()/2*0.75f)),(int)(Main.StandardHeight/2-(p.getRaumschiff().getHeight()/2*0.75f)));
		p.updateRaumschiff();
		
		while(true){
			thisFrame = System.currentTimeMillis();
			timeSinceLastFrame = ((float)(thisFrame-lastFrame))/1000f;
			lastFrame = thisFrame;
			//System.out.println(timeSinceLastFrame);
			
			f.Mouse();
			f.MouseReset();
            
            f.setScale(((float)f.getWidth()-(2*Main.edgeX))/Main.StandardWidth, ((float)f.getHeight()-(Main.edgeY+Main.edgeX))/Main.StandardHeight);
			
    		//kontroller.update();
            
    		if(menu == 0){
            	Mainmenu();
            	
    		}else if(menu == 0.1){
            	Shop();

        		if(Keyboard.isKeyDown(KeyEvent.VK_F7) && Keyboard.isKeyDownCd(KeyEvent.VK_C)) {
        			Main.p.addCoin(100);
        		}
            	
    		}else if(menu == 1){
            	ShipSelection();
            	
    		}else if(menu == 1.1){
            	Game();
            	
            	if(Keyboard.isKeyDown(KeyEvent.VK_F7) && Keyboard.isKeyDownCd(KeyEvent.VK_S)) {
        			Main.p.addShot(100);
        		}
            	if(Keyboard.isKeyDown(KeyEvent.VK_F7) && Keyboard.isKeyDownCd(KeyEvent.VK_L)) {
        			Main.p.addLive(100);
        		}
                
            }else if(menu == 2){
				Settings();
            
            }else if(menu == 2.1){
            	ControlSettings();
            	
            }else if(menu == 2.2){
            	SoundSettings();
            	
            }else if(menu == 2.3){
            	GraficSettings();
            	
            }else if(menu == -1){
            	Exit();
            }

			b.updateBackgroundX(b.getActbg());
    		Fullscreen();
			
    		float wait = 5-timeSinceLastFrame*5;
    		if(wait < 1) {
    			wait = 1;
    		}
			try {
				Thread.sleep((long)(wait));
			}catch (InterruptedException e){e.printStackTrace();}
			
			//try{
				f.repaintScreen(); 
			//}catch(Exception e){e.printStackTrace();}
		}
	}
	
	private static void Fullscreen() {
		//Vollbild
		if(Keyboard.isKeyDownCd(steuerung[0].getKey())){
			Keyboard.KeyReset(steuerung[0].getKey());
			if(devices[cdevice].getFullScreenWindow() == null && vollbild == 0){
				//Vollbild
				f.dispose();
				f.setUndecorated(true);
				f.setVisible(true);
				f.makeStrat();
    			f.setResizable(false);
    			devices[cdevice].setFullScreenWindow(f);
    			devices[cdevice].setDisplayMode(displayModes[displayMode]);
    			edgeX = 0;
    			edgeY = 0;
    			
			}else if(vollbild == 1){
				//Fenster
    			if(devices[cdevice].getDisplayMode().getHeight() < f.getHeight() && devices[cdevice].getDisplayMode().getWidth() < f.getWidth()) {
					f.setSize(f.getWidth(), f.getHeight());
        			f.setLocation(0, 0);
        			vollbild = -2;
    			}else {
					f.setSize(f.getWidth()-2*edgeX, f.getHeight()-edgeY-edgeX);
        			f.setLocation(f.getX()+edgeX, f.getY()+edgeY);
        			vollbild = -1;
    			}
    			f.dispose();
				f.setUndecorated(true);
				f.setVisible(true);
				f.makeStrat();
    			f.setResizable(true);
    			//f.setLocationRelativeTo(null);
    			devices[cdevice].setFullScreenWindow(null);
    			edgeX = 0;
    			edgeY = 0;
				
    		}else{
				//Fenster mit Rahmen
    			f.dispose();
				f.setUndecorated(false);
				f.setVisible(true);
				f.makeStrat();
    			f.setResizable(true);
    	    	edgeY = SedgeY;
    	    	edgeX = SedgeX;
    			if(vollbild == -1){
    				vollbild = 1;
					f.setSize(f.getWidth()+2*edgeX, f.getHeight()+edgeY+edgeX);
        			f.setLocation(f.getX()-edgeX, f.getY()-edgeY);
    			}else if(vollbild == -2){
    				vollbild = 1;
    			}else{
        			f.setLocationRelativeTo(null);
        			devices[cdevice].setFullScreenWindow(null);
    			}
    		}
		}
	}
	private static void Frame() {
		f = new Frame(b,p);
        
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(FrameX+2*edgeX, FrameY+edgeY+edgeX);
		f.setUndecorated(false);
		f.setResizable(true);
		f.setVisible(true);
		f.setLocationRelativeTo(null);
		
		GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		devices = environment.getScreenDevices();
		displayModes = devices[cdevice].getDisplayModes();
		displayMode = displayModes.length-1;
		
		devices[cdevice].setFullScreenWindow(null);
		
		f.makeStrat();
	}
	private static void setControl() {
		steuerung[0] = new Control(new String[]{"Vollbild","Fullscreen"}, KeyEvent.VK_F11, 1, 1);
        steuerung[1] = new Control(new String[]{"Schießen","Shoot"}, KeyEvent.VK_SPACE, 1, 2);
        steuerung[2] = new Control(new String[]{"Hoch","Up"}, KeyEvent.VK_W, 1, 3);
        steuerung[3] = new Control(new String[]{"Runter","Down"}, KeyEvent.VK_S, 1, 4);
        steuerung[4] = new Control(new String[]{"Links","Left"}, KeyEvent.VK_A, 1, 5);
        steuerung[5] = new Control(new String[]{"Rechts","Right"}, KeyEvent.VK_D, 1, 6);
        
        steuerung[6] = new Control(new String[]{"ESC"}, KeyEvent.VK_ESCAPE, 2, 1);
        steuerung[7] = new Control(new String[]{"Bestätigen","Confirm"}, KeyEvent.VK_ENTER, 2, 2);
        steuerung[8] = new Control(new String[]{"Menu: Hoch","Menu: Up"}, KeyEvent.VK_UP, 2, 3);
        steuerung[9] = new Control(new String[]{"Menu: Runter","Menu: Down"}, KeyEvent.VK_DOWN, 2, 4);
        steuerung[10] = new Control(new String[]{"Menu: Links","Menu: Left"}, KeyEvent.VK_LEFT, 2, 5);
        steuerung[11] = new Control(new String[]{"Menu: Rechts","Menu: Right"}, KeyEvent.VK_RIGHT, 2, 6);
        
		for(int i = 0; i < steuerung.length; i++){
			if(steuerung[i].getButton().getPage() > lastseite){
				lastseite = steuerung[i].getButton().getPage();
			}
			
			if(steuerung[i].getPlace() == 1 || steuerung[i].getPlace() == 3 || steuerung[i].getPlace() == 5){
				for(int j = 0; j < steuerung.length; j++){
					if(steuerung[i].getPlace() == (steuerung[j].getPlace()-1) && steuerung[i].getButton().getPage() == (steuerung[j].getButton().getPage())){
						steuerung[i].getButton().setX(StandardWidth/2 - steuerung[i].getButton().getWidth()/2 - 240);
						steuerung[i].getButton().getText().setXY(steuerung[i].getButton().getX(), steuerung[i].getButton().getY()-23, false);
						steuerung[i].getButton().setNX(1);
					}
				}
			}
			
			BSteuerung[i] = steuerung[i].getButton();
		}
	}
	private static void setSound() {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
	    for(int i = 0; i < mixers.length; i++){
	    	Mixer.Info mixerInfo = mixers[i];
	        Mixer mixer = AudioSystem.getMixer(mixerInfo);
	        if(!mixer.getMixerInfo().getName().contains("Mikrofon") && !mixer.getMixerInfo().getName().contains("Mic")){
		        Line.Info[] lineinfos = mixer.getTargetLineInfo();
			    if(lineinfos.length == 0 && i != 0){zmixers.add(mixer);}
		        for(Line.Info lineinfo : lineinfos){
		        	try {
		        		Line thisline = mixer.getLine(lineinfo);
			        	if(String.valueOf(thisline.getLineInfo()).contains("target port")){
			                line.add(thisline);
			                thisline.open();
			                if(thisline.isControlSupported(FloatControl.Type.VOLUME)){
			                	FloatControl control = (FloatControl) thisline.getControl(FloatControl.Type.VOLUME);  
			                    JProgressBar pb = new JProgressBar();
			                    control.setValue((float) (volume*0.01));
			                    int value = (int) (control.getValue()*100);
			                    pb.setValue(value);
			                }
			        	}
		    		} catch (Exception e) {System.err.println(e);}
		        }
	        }	
	    }
	    
	    whatmixer = 0;
	    szmixers = new String[zmixers.size()];
	    for(int i = 0; i < szmixers.length; i++){
	    	char temp[] = zmixers.get(i).getMixerInfo().getName().toCharArray();
	    	szmixers[i] = "";
	    	for(int k = 0; k < temp.length; k++){
	    		if(temp[k] == ' ' && temp[k+1] == '('){
	    			break;
	    		}else{
	    			szmixers[i] = szmixers[i] + "" + temp[k];
	    		}
	    	}
	    	if(szmixers[i].equals(audiogeret)){whatmixer = i;}
	    }
	    if(audiogeret == null){
	    	audiogeret = szmixers[whatmixer];
	    }
        audio = zmixers.get(whatmixer);
        
        click = new File("Sounds/click.wav");
        shot = new File("Sounds/shot_sound_0.wav");
        explosion = new File("Sounds/explosion.wav");
	}
	private static void setButtons() {
		SHead[0] = new Schrift(new String[]{"Spaceshot"}, StandardWidth/2, true, StandardHeight/2-350, true, 2, new Color(195, 195, 195));
		Buttons[0] = new Button(new String[]{"Spiel starten", "Start Game"}, 0, -100, "", 1, 1, 1, "Hin1") ;
		Buttons[1] = new Button(new String[]{"Einstellungen", "Preferences"}, 0, 100, "", 1, 1, 2, "Hin2");
		Buttons[2] = new Button(new String[]{"Beenden", "Quit"}, 0, 300, "", 1, 1, 3, "Back-1");
		Buttons[3] = new Button(null, -275, -80, "Icons/shop_icon", 0.5f, 1, 4, "Pic End Mid Hin0.1");
		Buttons[4] = new Button(null, -125, -80, "Icons/flag_"+Button.getLang(), 0.25f, 2, 4, "Pic End Mid");
		
		SHead[1] = new Schrift(new String[]{"Laden","Shop"}, StandardWidth/2, true, StandardHeight/2-400, true, 1.5, new Color(195, 195, 195));
		for (int i = 0; i <= Shop.getLength(); i++) {
			BShop[i] = new Button(-180-64, -230+i*80, pow[i].getPowerup().getWidth(), pow[i].getPowerup().getHeight(), 1, (i+1));
		}
		BShop[Shop.getLength()+1] = new Button(new String[]{"Zurück", "Back"}, -80, -80, "", 0.75f, 1, (Shop.getLength()+2), "End Back0");
		
		BSideSelection[0] = new Button(null, 225, -80, "R", 0.75f, 0, 0, "Normal EndY");
		BSideSelection[1] = new Button(null, 125, -80, "L", 0.75f, 0, 0, "Normal EndY");
		
		BOptions[0] = new Button(new String[]{"Zurück", "Back"}, -80, -80, "", 0.75f, 3, 4, "End Back2");
		BOptions[1] = new Button(new String[]{"Zurücksetzen", "Reset"}, -240, -80, "", 0.75f, 1, 4, "EndY");
		BOptions[2] = new Button(new String[]{"Letzes", "Last"}, 240, -80, "", 0.75f, 2, 4, "EndY");
		
		SHead[2] = new Schrift(new String[]{"Einstellungen","Preferences"}, StandardWidth/2, true, StandardHeight/2-330, true, 1.5, new Color(195, 195, 195));
		BOptionsMenu[0] = new Button(new String[]{"Steuerung", "Controls"}, 0, -100, "", 1, 1, 1, "Hin2.1");
		BOptionsMenu[1] = new Button(new String[]{"Ton", "Sounds"}, 0, 100, "", 1, 1, 2, "Hin2.2");
		BOptionsMenu[2] = new Button(new String[]{"Grafik", "Graphics"}, 0, 300, "", 1, 1, 3, "Hin2.3");
		BOptionsMenu[3] = new Button(new String[]{"Zurück", "Back"}, -80, -80, "", 0.75f, 1, 4, "End Back0");
		
		SHead[3] = new Schrift(new String[]{"Steuerung","Controls"}, StandardWidth/2, true, StandardHeight/2-350, true, 2, new Color(195, 195, 195));
		SSteuerung = new Schrift(new String[]{"Drücke eine beliebige Taste","Press any key"}, 0, 0, 0.27);

		SHead[4] = new Schrift(new String[]{"Ton","Sounds"}, StandardWidth/2, true, StandardHeight/2-350, true, 2, new Color(195, 195, 195));
		SRMaster[0] = new Slide_control("Master", new Color(195, 195, 195), 0, 0, "Schieberegler", 1, 1, 1, volume);
        BSounds[1] = new Button(new String[]{szmixers[whatmixer]}, 0, 200, "", 1f, 1, 2);
		BSounds[0] = new Button(new String[]{"Zurück","Back"}, -80, -80, "", 0.75f, 1, 3, "End Back2");

		SHead[5] = new Schrift(new String[]{"Grafik","Graphics"}, StandardWidth/2, true, StandardHeight/2-350, true, 2, new Color(195, 195, 195));
        BGrafik[1] = new Button(new String[]{String.valueOf(displayModes[displayMode].getWidth())}, new String[]{"Breite:","Width:"}, -275, -100, "", 0.85f, 1, 1);
        BGrafik[2] = new Button(new String[]{String.valueOf(displayModes[displayMode].getHeight())}, new String[]{"Höhe:","Height:"}, 275, -100, "", 0.85f, 2, 1);
        BGrafik[3] = new Button(new String[]{String.valueOf(displayModes[displayMode].getRefreshRate())}, new String[]{"FPS:"}, -275, 75, "", 0.85f, 1, 2);
        BGrafik[4] = new Button(new String[]{String.valueOf(cdevice)}, new String[]{"Monitor:","Screen:"}, 275, 75, "", 0.85f, 2, 2);
		BGrafik[5] = new Button(null, 225, -80, "R", 0.75f, 1, 4, "Normal EndY");
		BGrafik[6] = new Button(null, 125, -80, "L", 0.75f, 0, 4, "Normal EndY");
        BGrafik[7] = new Button(new String[]{"Vollbild","Fullscreen"}, new String[]{"Vollbild:","Fullscreen:"}, -275, 250, "", 0.85f, 1, 3);
        BGrafik[8] = new Button(new String[]{b.getName()}, new String[]{"Hintergrund:","Background:"}, 275, 250, "", 0.85f, 2, 3);
		if(vollbild == 1){
			BGrafik[7].setText(new String[]{"Fenster","Window"});
			for(int i = 1; i <= 6; i++){
				BGrafik[i].setVisible(true);
			}
		}
		BGrafik[0] = new Button(new String[]{"Zurück","Back"}, -80, -80, "", 0.75f, 2, 4, "End Back2");
        
        SEnde[0] = new Schrift(new String[]{"Willst du wirklich beenden?","Do you really want to quit?"}, StandardWidth/2, true, StandardHeight/2-100, true, 0.75, new Color(195, 195, 195));
        BEnde[0] = new Button(new String[]{"Ja","Yes"}, -240, 100, "", 0.85f, 1, 1, "Hin-1");
        BEnde[1] = new Button(new String[]{"Nein","No"}, 240, 100, "", 0.85f, 2, 1, "Back0");
        
        BRaumschiff[0] = new Button(new String[]{"Zurück","Back"}, -80, -80, "", 0.75f, 2, 4, "End Back0");
        BRaumschiff[1] = new Button(new String[]{"Starten","Start"}, -100-BRaumschiff[0].getWidth(), -80, "", 0.75f, 1, 4, "End Hin1.1");
        SHead[6] = new Schrift(new String[]{"Raumschiffauswahl","Spaceship Selection"}, StandardWidth/2, true, StandardHeight/2-330, true, 1.5, new Color(195, 195, 195));
        SRColor[0] = new Slide_control("Rot", Color.red, -300, -100, "Schieberegler", 1, 1, 1, (int)(color[0]/2.54));
        SRColor[1] = new Slide_control("Grün", Color.GREEN, -300, 100, "Schieberegler", 1, 1, 2, (int)(color[1]/2.54));
        SRColor[2] = new Slide_control("Blau", Color.BLUE, -300, 300, "Schieberegler", 1, 1, 3, (int)(color[2]/2.54));

        BGame[0] = new Button(new String[]{"X"}, Color.WHITE, -1, -StandardHeight+90, "Quadrat", 0.75f, 1, 1, "Back1");
	}
	
	private static void setLang() {
        Scanner s;
        file.setReadable(true);
        
        Button.setLang(1);
		if(file.exists()){
			try {
				s = new Scanner(file);
				Button.setLang(s.nextInt());
			} catch (Exception e) {
			}
		}
        file.setReadable(false);
	}
	private static void loadSettings() {
        Scanner s;
        file.setReadable(true);
        
		if(file.exists()){
			try {
				s = new Scanner(file);
				s.nextInt();
				for(int i = 0; i < steuerung.length; i++){
					steuerung[i].setKey(s.nextInt());
				}
				volume = s.nextInt();
				audiogeret = s.next();
				displayMode = s.nextInt();
				vollbild = s.nextInt();
				for(int i = 0; i < color.length; i++) {
					color[i] = s.nextInt();
				}
				p.setCoin(s.nextInt());
				for (int j = 0; j < Shop.getLength(); j++) {
					Shop.setReroll(j, s.nextInt());
				}
				Shop.setAll();
				p.setCoinC(s.nextBoolean());
				s.close();
			} catch (Exception e) {
				saveSettings();
			}
		}else {
			saveSettings();
		}
        file.setReadable(false);
	}
	public static void saveSettings(){
        FileWriter fw;
        file.setWritable(true);
		try {
			file.createNewFile();
			fw = new FileWriter(file);
			fw.write(""+Button.getLang());
			fw.write("\n"+steuerung[0].getKey());
			for(int i = 1; i < steuerung.length; i++){
				fw.write("\n"+steuerung[i].getKey());
			}
			fw.write("\n"+volume);
			fw.write("\n"+audiogeret);
			fw.write("\n"+displayMode);
			fw.write("\n"+vollbild);
			for(int i = 0; i < color.length; i++){
				fw.write("\n"+color[i]);
			}
			fw.write("\n"+p.getCoins());
			for(int i = 0; i < Shop.getLength(); i++) {
				fw.write("\n"+Shop.getReroll(i));
			}
			fw.write("\n"+p.getCoinC());
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        file.setWritable(false);
	}
	
	private static void Mainmenu() {
		Netz(Buttons);
    	//setScale(Buttons);
    	if(Buttons[2].isDrueck()){
    		SEnde[1] = new Schrift(new String[]{"(" + steuerung[7].getKeyName() + ")"}, BEnde[0].getX()+BEnde[0].getWidth()/2, true, BEnde[0].getY()+BEnde[0].getHeight()/2+35, true, 0.3*BEnde[0].getScale());
    		SEnde[2] = new Schrift(new String[]{"(" + steuerung[6].getKeyName() + ")"}, BEnde[1].getX()+BEnde[1].getWidth()/2, true, BEnde[1].getY()+BEnde[1].getHeight()/2+35, true, 0.3*BEnde[1].getScale());
    	}
    	if(Buttons[3].isDrueck()) {
    		if(Shop.getUpgrade(0) == null) {
        		Shop.setUpgrade();
    		}
    	}
    	Buttons[1].isDrueck();
    	Buttons[0].isDrueck();
    	
    	if(Buttons[4].isDrueck()){
    		Button.setLang(Button.getLang()+1);
    		Buttons[4].setPic("Icons/flag_"+Button.getLang());
    	}
	}
	private static void Shop() {
		pow[8].updateCoin();
		Netz(BShop);
		if(BShop[Shop.getLength()+1].isDrueck() && pow[9].hasPlayer()) {
			Shop.resetSale();
			Shop.setCost();
			menu = 1.1;
		}
		for (int i = 0; i < Shop.getLength(); i++) {
			if(BShop[i].isDrueck()) {
				Shop.addReroll(i);
			}
		}
		if(BShop[Shop.getLength()].isDrueck()) {
			if(Main.p.getCoins() >= 300) {
				Main.p.setCoin(Main.p.getCoins()-300);
				p.setCoinC(true);
			}
		}
	}
	private static void ShipSelection() {
		//setScale(BRaumschiff);
		//setScale(SRColor);
		
    	Netz(BRaumschiff,SRColor);
    	
    	BRaumschiff[0].isDrueck();
        if(BRaumschiff[1].isDrueck()) {
        	saveSettings();
        	p.setX(100);
        	time = 0;
        	dif = 2;
		}
    	
    	for(int i = 0; i < SRColor.length; i++) {
    		SRColor[i].set();
        	if(SRColor[i].isRegel()){
        		color[i] = (int)(SRColor[i].getWert()*2.55);
        		p.updateRaumschiff();
        	}
    	}
	}
	private static void Game() {
		p.update();
		if(p.getLives() != 0) {
			for (int i = 0; i < pow.length; i++) {
				pow[i].update();
			}
			BGame[0].isDrueck();
			for(int i = 0; i < bullets.size(); i++){
				bullets.get(i).update();
			}
			for(int i = 0; i < enemys.size(); i++){
				enemys.get(i).update();
			}
			
			time += timeSinceLastFrame;
	    	if(time%(dif) >= 0 && time%(dif) <= 0.1 && !set) {
	    		if(!Enemy.getBoss()) {
	    			enemys.add(new Enemy(r.nextInt(4)+1));
					
					if((!p.getCoinC() && r.nextInt(4) == 0) || (p.getCoinC() && r.nextInt(3) == 0)) {
						if(!pow[8].exist()) {
							pow[8].respawn();;
						}
					}
	    		}
				
				if(p.getKills() >= 100) {
					if(!Shop.all() && r.nextInt(30) == 0) {
						pow[9].respawn();
					}
					if(!Enemy.getBoss() && r.nextInt(50) == 0) {//40
						enemys.add(new Enemy(5));
					}
				}
				
				if(r.nextInt(15) == 0) {
					if(Shop.getOne() != -1 && pow[Shop.getLast()] == null) {
						pow[Shop.getLast()].respawn();;
					}
				}
	    		if(dif>0.5) {dif-=timeSinceLastFrame/10;}
	    		set = true;
	    		
	    	}else if(!(time%(dif) >= 0 && time%(dif) <= 0.1) && set){
	    		set = false;
	    	}
		}
	}
	private static void Settings() {
		//setScale(BOptionsMenu);
		
    	Netz(BOptionsMenu);
    	
    	for(int i = 0; i < BOptionsMenu.length; i++){
    		BOptionsMenu[i].isDrueck();
    	}
	}
	private static void ControlSettings() {
		for(int i = 0; i < steuerung.length; i++){
			if(seite == steuerung[i].getButton().getPage()){
				steuerung[i].set();
			}
		}
    	
    	AllBOption = setArray(AllBOption, BSteuerung, BOptions);
    	
		//setScale(AllBOption);
		
		//setScale(BSideSelection);
    	
    	NetzMitSeiten(AllBOption, BSteuerung, BOptions);
    
    	if(BOptions[0].isDrueck()){
			for(int i = 0; i < steuerung.length; i++){
				steuerung[i].getButton().ResetDeaktiviert(); 
				steuerung[i].setKey2(steuerung[i].getKey());
				steuerung[i].setBol(false);
			}
			saveSettings();
    	}
    	
    	if(f.getMouse()){
    		if(BSideSelection[0].isDrueck()){seite += 1;}
        	if(BSideSelection[1].isDrueck()){seite -= 1;}
        	if(lastseite == 1){BSideSelection[0].setDeaktiviert(); BSideSelection[1].setDeaktiviert();}
        	else if(seite == lastseite){BSideSelection[0].setDeaktiviert(); BSideSelection[1].ResetDeaktiviert();}
        	else if(seite == 1){BSideSelection[0].ResetDeaktiviert(); BSideSelection[1].setDeaktiviert();}
        	else{BSideSelection[0].ResetDeaktiviert(); BSideSelection[1].ResetDeaktiviert();}
    	}
    	
    	if(BOptions[1].isDrueck()){
    		for(int j = 0; j < steuerung.length; j++){steuerung[j].setKey2(steuerung[j].getKey()); steuerung[j].setKey(steuerung[j].getDefaultKey());}
    	}
    	if(BOptions[2].isDrueck()){
    		for(int j = 0; j < steuerung.length; j++){steuerung[j].setKey(steuerung[j].getKey2());}
    	}
	}
	private static void SoundSettings() {
		//setScale(BSounds);
		//setScale(SRMaster);
		
    	Netz(BSounds, SRMaster);
    	
    	if(BSounds[0].isDrueck()){
    		volume = SRMaster[0].getWert();
    		audiogeret = BSounds[1].getText().asString();
			saveSettings();
    	}
    	
    	if(BSounds[1].isDrueck()){
//    		Mixer.Info mixers2[] = AudioSystem.getMixerInfo();
//    		System.out.println("|");
//    	    for(int i = 0; i < mixers2.length; i++){
//    	    	Mixer.Info mixerInfo = mixers2[i];
//    	        Mixer mixer = AudioSystem.getMixer(mixerInfo);
//    		    System.out.println(mixer.getMixerInfo().getName());
//    		}
//    	    System.out.println("");
    		if(whatmixer == szmixers.length-1){whatmixer = 0;}
    		else{whatmixer = whatmixer + 1;}
            BSounds[1].setText(new String[]{szmixers[whatmixer]});
            audio = zmixers.get(whatmixer);
    	}
    	
    	SRMaster[0].set();
    	
    	if(SRMaster[0].isRegel()){
            try {
				line.get(whatmixer).open();
                if(line.get(whatmixer).isControlSupported(FloatControl.Type.VOLUME)){
                	FloatControl control = (FloatControl) line.get(whatmixer).getControl(FloatControl.Type.VOLUME);  
                    JProgressBar pb = new JProgressBar();
                    control.setValue((float) (SRMaster[0].getWert()*0.01));
                    int value = (int) (control.getValue()*100);
                    pb.setValue(value);
                }
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
    	}
	}
	private static void GraficSettings() {
		//setScale(BGrafik);
    	Netz(BGrafik);
    	
    	BGrafik[0].isDrueck();
    	
    	if(BGrafik[1].isDrueck() || BGrafik[2].isDrueck() || BGrafik[3].isDrueck() || BGrafik[5].isDrueck()){
    		if(displayMode >= displayModes.length-1){displayMode = -1;}
    		displayMode += 1;
    		BGrafik[1].setText(new String[]{String.valueOf(displayModes[displayMode].getWidth())});
    		BGrafik[2].setText(new String[]{String.valueOf(displayModes[displayMode].getHeight())});
    		BGrafik[3].setText(new String[]{String.valueOf(displayModes[displayMode].getRefreshRate())});
    		
    	}else if(BGrafik[6].isDrueck()){
    		if(displayMode <= 0){displayMode = displayModes.length;}
    		displayMode -= 1;
    		BGrafik[1].setText(new String[]{String.valueOf(displayModes[displayMode].getWidth())});
    		BGrafik[2].setText(new String[]{String.valueOf(displayModes[displayMode].getHeight())});
    		BGrafik[3].setText(new String[]{String.valueOf(displayModes[displayMode].getRefreshRate())});
    	}
    	
    	if(BGrafik[4].isDrueck()){
    		if(cdevice >= devices.length-1){cdevice = -1;}
    		cdevice += 1;
    		BGrafik[4].setText(new String[]{String.valueOf(cdevice)});
    		displayModes = devices[cdevice].getDisplayModes();
    		displayMode = displayModes.length-1;
    		BGrafik[1].setText(new String[]{String.valueOf(displayModes[displayMode].getWidth())});
    		BGrafik[2].setText(new String[]{String.valueOf(displayModes[displayMode].getHeight())});
    		BGrafik[3].setText(new String[]{String.valueOf(displayModes[displayMode].getRefreshRate())});
    	}
    	
    	if(BGrafik[7].isDrueck()){
    		if(vollbild == 0){
    			vollbild = 1; 
    			BGrafik[7].setText(new String[]{"Fenster","Window"});
    			for(int i = 1; i <= 6; i++){
    				BGrafik[i].setVisible(true);
    			}
        		if(devices[cdevice].getFullScreenWindow() != null){Keyboard.setKey(steuerung[0].getKey());}
    		}
    		else if(vollbild == 1 || vollbild == -1){
        		if(vollbild == -1){Keyboard.setKey(steuerung[0].getKey());}
    			vollbild = 0;
    			BGrafik[7].setText(new String[]{"Vollbild","Fullscreen"});
    			for(int i = 1; i <= 6; i++){
    				BGrafik[i].setVisible(false);
    			}
    		}
    	}
    	if(BGrafik[8].isDrueck()){
    		if(b.getActbg() == 6) {
    			b.setActbg(0);
    		}else {
        		b.setActbg(b.getActbg()+1);
    		}
			BGrafik[8].setText(new String[]{b.getName()});
    	}
	}
	private static void Exit() {
		//setScale(BEnde);
    	if(BEnde[0].isDrueck()){System.exit(0);}
    	if(BEnde[1].isDrueck()){buttonX = 1; buttonY = 3;}
	}
	
	public static void PlaySound(File f){
		try{
    		Clip clip = AudioSystem.getClip(audio.getMixerInfo());
			clip.open(AudioSystem.getAudioInputStream(f));
			clip.start();
		}catch(Exception e){System.err.println(e);}
	}
	
	public static Button[] setArray(Button[] buttons, Button[] buttonsS, Button[] buttonsO){
		Control.HMPage = 0;
		for(int i = 0; i < buttonsS.length; i++){
			if(seite == buttonsS[i].getPage()){
				Control.HMPage += 1;
			}
		}
		
		buttons = new Button[buttonsO.length + Control.HMPage];
		for(int j = 0; j < buttonsO.length; j++){
			buttons[j] = buttonsO[j];
		}
		int allwert = buttonsO.length;
		for(int j = 0; j < buttonsS.length; j++){
			if(seite == buttonsS[j].getPage()){
				buttons[allwert] = buttonsS[j];
				allwert += 1;
			}
		}
		return buttons;
	}
	
	public static void Netz(Button[] buttons){
		Netz(buttons, null);
	}
	
	public static void Netz(Button[] buttons, Slide_control[] schieberegler){
		//Hoch
    	if(Keyboard.isKeyDownCd(steuerung[8].getKey())){
    		what = 1;
    		
        //Runter
    	}else if(Keyboard.isKeyDownCd(steuerung[9].getKey())){
    		what = 2;

		//Links
		}else if(Keyboard.isKeyDownCd(steuerung[10].getKey())){
			what = 4;

    	//Rechts
		}else if(Keyboard.isKeyDownCd(steuerung[11].getKey())){
			what = 3;
		}
		
		if(what != 0){
			if(f.getMouse()){f.setMouse(false);}
			gibtEs = false;
			buttonX2 = -1;
			buttonY2 = -1;
		}
		
		if(what == 1){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() < buttonY){
        			gibtEs = true;
        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
        			else if(buttonY2 < buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
        		}
			}
			if(schieberegler != null){
				for(int i = 0; i < schieberegler.length; i++){
	        		if(schieberegler[i].getNX() >= (buttonX-0.5) && schieberegler[i].getNX() <= (buttonX+0.5) && schieberegler[i].getNY() < buttonY){
	        			gibtEs = true;
	        			if(buttonY2 == -1){buttonY2 = schieberegler[i].getNY();}
	        			else if(buttonY2 < schieberegler[i].getNY()){buttonY2 = schieberegler[i].getNY();}
	        		}
				}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() < buttonY){
	        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
	        			else if(buttonY2 > buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
	        		}
				}
			}
		
		}else if(what == 2){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() > buttonY){
        			gibtEs = true;
        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
        			else if(buttonY2 > buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
        		}
			}
			if(schieberegler != null){
				for(int i = 0; i < schieberegler.length; i++){
	        		if(schieberegler[i].getNX() >= (buttonX-0.5) && schieberegler[i].getNX() <= (buttonX+0.5) && schieberegler[i].getNY() > buttonY){
	        			gibtEs = true;
	        			if(buttonY2 == -1){buttonY2 = schieberegler[i].getNY();}
	        			else if(buttonY2 > schieberegler[i].getNY()){buttonY2 = schieberegler[i].getNY();}
	        		}
				}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() < buttonY){
	        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
	        			else if(buttonY2 > buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
	        		}
				}
			}

		}else if(what == 3){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() > (buttonX + 0.5)){
        			gibtEs = true;
        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
        			else if(buttonX2 > buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
        		}
			}
			if(schieberegler != null){
				for(int i = 0; i < schieberegler.length; i++){
	        		if(schieberegler[i].getNY() == buttonY && schieberegler[i].getNX() > (buttonX + 0.5)){
	        			gibtEs = true;
	        			if(buttonX2 == -1){buttonX2 = schieberegler[i].getNX();}
	        			else if(buttonX2 > schieberegler[i].getNX()){buttonX2 = schieberegler[i].getNX();}
	        		}
				}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() < (buttonX - 0.5)){
	        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
	        			else if(buttonX2 > buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
	        		}
				}
			}
		
		}else if(what == 4){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() < (buttonX - 0.5)){
        			gibtEs = true;
        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
        			else if(buttonX2 < buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
        		}
			}
			if(schieberegler != null){
				for(int i = 0; i < schieberegler.length; i++){
	        		if(schieberegler[i].getNY() == buttonY && schieberegler[i].getNX() < (buttonX - 0.5)){
	        			gibtEs = true;
	        			if(buttonX2 == -1){buttonX2 = schieberegler[i].getNX();}
	        			else if(buttonX2 < schieberegler[i].getNX()){buttonX2 = schieberegler[i].getNX();}
	        		}
				}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() > (buttonX + 0.5)){
	        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
	        			else if(buttonX2 < buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
	        		}
				}
			}
		}
		
		if(what != 0){
			if(what == 1 || what == 2){
				if(buttonY2 != -1){buttonY = buttonY2;}
			}
			if(what == 3 || what == 4){
				if(buttonX2 != -1){buttonX = buttonX2;}
			}
        	what = 0;
		}
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].ResetDeaktiviert2();
			buttons[i].setAusgewaehlt(false);
    	}
    	
		if(schieberegler != null){
			for(int i = 0; i < schieberegler.length; i++){
				schieberegler[i].ResetDeaktiviert2();
				schieberegler[i].setAusgewaehlt(false);
	    	}
		}
		
		if(!SteuerungSet){
	    	for(int i = 0; i < buttons.length; i++){
	    		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() == buttonY && !(f.getMouse())){
	    			for(int j = 0; j < buttons.length; j++){
	    				if(j != i){
	    					buttons[j].setDeaktiviert2();
	    				}
	    			}
	    			buttons[i].setAusgewaehlt(true);
					
					if(Keyboard.isKeyDownCd(steuerung[7].getKey(),100)){
						buttons[i].setGedrueckt(true);
					}
	    		}
	    	}
	    	
	    	if(schieberegler != null){
		    	for(int i = 0; i < schieberegler.length; i++){
		    		if(schieberegler[i].getNX() >= (buttonX-0.5) && schieberegler[i].getNX() <= (buttonX+0.5) && schieberegler[i].getNY() == buttonY && !(f.getMouse())){
		    			for(int j = 0; j < schieberegler.length; j++){
		    				if(j != i){
		    					schieberegler[j].setDeaktiviert2();
		    				}
		    			}
		    			schieberegler[i].setAusgewaehlt(true);
		    		}
		    	}
	    	}
		}
	}
	public static void NetzMitSeiten(Button[] buttons, Button[] buttonsS, Button[] buttonsO){
		if(cooldown <= 0){
    		//Hoch
        	if(Keyboard.isKeyDownCd(steuerung[8].getKey())){
        		if(f.getMouse()){
        			f.setMouse(false);
        		}else{
            		what = 1;
        		}
        		
        		cooldown = 15;
        		
            //Runter
        	}else if(Keyboard.isKeyDownCd(steuerung[9].getKey())){
        		if(f.getMouse()){
        			f.setMouse(false);
        		}else{
            		what = 2;
        		}
        		
        		cooldown = 15;

        	//Links
    		}else if(Keyboard.isKeyDownCd(steuerung[10].getKey())){
    			if(f.getMouse()){
        			f.setMouse(false);
        		}else{
            		what = 4;
        		}
		
    			cooldown = 15;
    			

        	//Rechts
			}else if(Keyboard.isKeyDownCd(steuerung[11].getKey())){
				if(f.getMouse()){
        			f.setMouse(false);
        		}else{
            		what = 3;
        		}
    		
				cooldown = 15;
    		}	
    	
    	}else{
    		cooldown -= timeSinceLastFrame;
    	}
		
		if(what != 0){
			gibtEs = false;
			buttonX2 = -1;
			buttonY2 = -1;
		}
		
		if(what == 1){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() < buttonY){
        			gibtEs = true;
        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
        			else if(buttonY2 < buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
        		}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() > buttonY){
	        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
	        			else if(buttonY2 < buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
	        		}
				}
			}
		
		}else if(what == 2){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() > buttonY){
        			gibtEs = true;
        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
        			else if(buttonY2 > buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
        		}
			}
			if(!gibtEs){
				for(int i = 0; i < buttons.length; i++){
	        		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() < buttonY){
	        			if(buttonY2 == -1){buttonY2 = buttons[i].getNY();}
	        			else if(buttonY2 > buttons[i].getNY()){buttonY2 = buttons[i].getNY();}
	        		}
				}
			}

		}else if(what == 3){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() > (buttonX + 0.5)){
        			gibtEs = true;
        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
        			else if(buttonX2 > buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
        		}
			}
			if(!gibtEs){
				if(seite != lastseite){
					for(int i = 0; i < buttonsS.length; i++){
						if(buttonsS[i].getNX() >= (buttonX-0.5) && buttonsS[i].getNX() <= (buttonX+0.5) && buttonsS[i].getNY() == buttonY && !(f.getMouse())){
							gibtEs = true;
							seite += 1;
							buttons = setArray(buttons, buttonsS, buttonsO);
							for(int j = 0; j < buttons.length; j++){
				        		if(buttons[j].getNY() == buttonY && buttons[j].getNX() < (buttonX - 0.5)){
				        			if(buttonX2 == -1){buttonX2 = buttons[j].getNX();}
				        			else if(buttonX2 > buttons[j].getNX()){buttonX2 = buttons[j].getNX();}
				        		}
							}
							break;
						}
					}
				}
				if(!gibtEs){
					for(int i = 0; i < buttonsO.length; i++){
		        		if(buttonsO[i].getNY() == buttonY && buttonsO[i].getNX() < (buttonX - 0.5)){
		        			if(buttonX2 == -1){buttonX2 = buttonsO[i].getNX();}
		        			else if(buttonX2 > buttonsO[i].getNX()){buttonX2 = buttonsO[i].getNX();}
		        		}
					}
				}
			}
		
		}else if(what == 4){
			for(int i = 0; i < buttons.length; i++){
        		if(buttons[i].getNY() == buttonY && buttons[i].getNX() < (buttonX - 0.5)){
        			gibtEs = true;
        			if(buttonX2 == -1){buttonX2 = buttons[i].getNX();}
        			else if(buttonX2 < buttons[i].getNX()){buttonX2 = buttons[i].getNX();}
        		}
			}
			if(!gibtEs){
				if(seite != 1){
					for(int i = 0; i < buttonsS.length; i++){
						if(buttonsS[i].getNX() >= (buttonX-0.5) && buttonsS[i].getNX() <= (buttonX+0.5) && buttonsS[i].getNY() == buttonY && !(f.getMouse())){
							gibtEs = true;
							seite -= 1;
							buttons = setArray(buttons, buttonsS, buttonsO);
							for(int j = 0; j < buttons.length; j++){
				        		if(buttons[j].getNY() == buttonY && buttons[j].getNX() > (buttonX + 0.5)){
				        			if(buttonX2 == -1){buttonX2 = buttons[j].getNX();}
				        			else if(buttonX2 < buttons[j].getNX()){buttonX2 = buttons[j].getNX();}
				        		}
							}
							break;
						}
					}
				}
				if(!gibtEs){
					for(int i = 0; i < buttonsO.length; i++){
		        		if(buttonsO[i].getNY() == buttonY && buttonsO[i].getNX() > (buttonX + 0.5)){
		        			if(buttonX2 == -1){buttonX2 = buttonsO[i].getNX();}
		        			else if(buttonX2 < buttonsO[i].getNX()){buttonX2 = buttonsO[i].getNX();}
		        		}
					}
				}
			}
		}
		
		if(what != 0){
			if(what == 1 || what == 2){
				if(buttonY2 != -1){buttonY = buttonY2;}
			}
			if(what == 3 || what == 4){
				if(buttonX2 != -1){buttonX = buttonX2;}
			}
        	what = 0;
		}
		
		for(int i = 0; i < buttons.length; i++){
			buttons[i].ResetDeaktiviert2();
			buttons[i].setAusgewaehlt(false);
    	}
    	
		if(!SteuerungSet){
			for(int i = 0; i < buttons.length; i++){
	    		if(buttons[i].getNX() >= (buttonX-0.5) && buttons[i].getNX() <= (buttonX+0.5) && buttons[i].getNY() == buttonY && !(f.getMouse())){
	    			for(int j = 0; j < buttons.length; j++){
	    				if(j != i){
	    					buttons[j].setDeaktiviert2();
	    				}
	    			}
	    			buttons[i].setAusgewaehlt(true);
					
					if(Keyboard.isKeyDownCd(steuerung[7].getKey(),100)){
						buttons[i].setGedrueckt(true);
					}
	    		}
	    	}
		}
	}
}
