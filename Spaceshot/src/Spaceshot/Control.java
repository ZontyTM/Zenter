package Spaceshot;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Control {
	private int Dkey, key, key2, place;
	private boolean bol;
	private Button but;
	private String name[];
	public static int HMPage;
	private Schrift text;
	
	public Control(String name[], int key, int page, int place){
		this.name = name;
		this.key = key;
		this.key2 = key;
		this.Dkey = key;
		this.bol = false;
		this.place = place;
		//System.out.println(name[1]);
		if(place == 1){
			this.but = new Button(name, new Color(195, 195, 195), -240, -100, "", 0.85f, 1.5, 1, "SchriftOben", page);
			
		}else if(place == 2){
			this.but = new Button(name, new Color(195, 195, 195), 240, -100, "", 0.85f, 2, 1, "SchriftOben", page);
			
		}else if(place == 3){
			this.but = new Button(name, new Color(195, 195, 195), -240, 75, "", 0.85f, 1.5, 2, "SchriftOben", page);
			
		}else if(place == 4){
			this.but = new Button(name, new Color(195, 195, 195), 240, 75, "", 0.85f, 2, 2, "SchriftOben", page);
			
		}else if(place == 5){
			this.but = new Button(name, new Color(195, 195, 195), -240, 250, "", 0.85f, 1.5, 3, "SchriftOben", page);
			
		}else if(place == 6){
			this.but = new Button(name, new Color(195, 195, 195), 240, 250, "", 0.85f, 2, 3, "SchriftOben", page);
			
		}
	}
	
	public int getKey(){return key;}
	public String getKeyName(){
		int i = key;
		String text = "?";
		
		if(i <= 1024){
			if(i == KeyEvent.VK_SPACE){text = "Leertaste";}
		    else if(i == KeyEvent.VK_ESCAPE){text = "Esc";}
		    else if(i == KeyEvent.VK_UP){text = "Pfeiltaste (hoch)";}
		    else if(i == KeyEvent.VK_DOWN){text = "Pfeiltaste (runter)";}
		    else if(i == KeyEvent.VK_RIGHT){text = "Pfeiltaste (rechts)";}
		    else if(i == KeyEvent.VK_LEFT){text = "Pfeiltaste (links)";}
		    else if(i == KeyEvent.VK_ENTER){text = "Enter";}
		    else if(i == KeyEvent.VK_BACK_SPACE){text = "Rücktaste";}
		    else{
	    	    text = KeyEvent.getKeyText(i);
		    }
		}/*else{
			i -= 1025;
			if(Main.kontroller.isXBox()){
        		if(i == 0){text = "A";}
        		else if(i == 1){text = "B"; }
        		else if(i == 2){text = "X";}
        		else if(i == 3){text = "Y";}
        		else if(i == 4){text = "LB";}
        		else if(i == 5){text = "RB";}
				
				if(Main.Windows){
					if(i == 6){text = "Back";}
					if(i == 7){text = "Start";}
					if(i == 8){text = "LS";}
					if(i == 9){text = "RS";}
					if(i == 30){text = "Oben (HS)";}
					if(i == 31){text = "Unten (HS)";}
					if(i == 32){text = "Links (HS)";}
					if(i == 33){text = "Rechts (HS)";}
				}
				
				if(Main.Mac){
					if(i == 6){text = "LS";}
					if(i == 7){text = "RS";}
					if(i == 8){text = "Start";}
					if(i == 9){text = "Back";}
					if(i == 10){text = "XBox";}
					if(i == 11){text = "Oben (HS)";}
					if(i == 12){text = "Unten (HS)";}
					if(i == 13){text = "Links (HS)";}
					if(i == 14){text = "Rechts (HS)";}
				}
				
        		if(i == 20){text = "Rechts (LS)";}
				if(i == 21){text = "Links (LS)";}
				if(i == 22){text = "Unten (LS)";}
				if(i == 23){text = "Oben (LS)";}
				if(i == 24){text = "Rechts (RS)";}
				if(i == 25){text = "Links (RS)";}
				if(i == 26){text = "Unten (RS)";}
				if(i == 27){text = "Oben (RS)";}
				if(i == 28){text = "RT";}
				if(i == 29){text = "LT";}
				if(i == 30){text = "Oben (D-Pad)";}
				if(i == 31){text = "Unten (D-Pad)";}
				if(i == 32){text = "Links (D-Pad)";}
				if(i == 33){text = "Rechts (D-Pad)";}
        	
        	}else{
        		if(i < 20){text = "Taste " + i;}
        		if(i == 20){text = "Rechts (LS)";}
				if(i == 21){text = "Links (LS)";}
				if(i == 22){text = "Unten (LS)";}
				if(i == 23){text = "Oben (LS)";}
				if(i == 24){text = "Rechts (RS)";}
				if(i == 25){text = "Links (RS)";}
				if(i == 26){text = "Unten (RS)";}
				if(i == 27){text = "Oben (RS)";}
				if(i == 28){text = "RT";}
				if(i == 29){text = "LT";}
				if(i == 30){text = "Oben (D-Pad)";}
				if(i == 31){text = "Unten (D-Pad)";}
				if(i == 32){text = "Links (D-Pad)";}
				if(i == 33){text = "Rechts (D-Pad)";}
        	}
		}*/
		return text;
	}
	public void setKey(int i){
		key = i;
		String keyname = getKeyName();
		
		if(i <= 1024){
	    	try{
	    		this.text = new Schrift(new String[]{keyname}, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
	    	}catch(Exception e){
				this.text = new Schrift(new String[]{"Unbekannt","Unknown"}, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
	    	}
		}else{
			this.text = new Schrift(new String[]{keyname}, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
		}
	
	}
	
	public int getDefaultKey(){return Dkey;}
	
	public int getKey2(){return key2;}
	public void setKey2(int i){key2 = i;}
	
	public boolean getBol(){return bol;}
	public void setBol(boolean b){bol = b;}
	
	public Button getButton(){return but;}
	
	public String getName(){return name[Button.getLang()];}
	
	public int getPlace(){return place;}
	
	public Schrift getSchrift(){return text;}
	
	public void set(){
		if(Keyboard.isKeyDownCd(Main.steuerung[5].getKey())){
			if(bol){
				bol = false;
				Main.SteuerungSet = false;
				for(int w = 0; w < Main.AllBOption.length; w++){
					Main.AllBOption[w].ResetDeaktiviert();
				}
			}
		}
		if(bol){
			for(int i = 0; i < 1024; i++){
				if(Keyboard.isKeyDownCd(i)){
					bol = false;
					Main.SteuerungSet = false;
					key = i;
					String text;
					if(i == KeyEvent.VK_SPACE){text = "Leertaste";}
		    	    else if(i == KeyEvent.VK_ESCAPE){text = "Esc";}
		    	    else if(i == KeyEvent.VK_UP){text = "Pfeiltaste (hoch)";}
		    	    else if(i == KeyEvent.VK_DOWN){text = "Pfeiltaste (runter)";}
		    	    else if(i == KeyEvent.VK_RIGHT){text = "Pfeiltaste (rechts)";}
		    	    else if(i == KeyEvent.VK_LEFT){text = "Pfeiltaste (links)";}
		    	    else if(i == KeyEvent.VK_ENTER){text = "Enter";}
		    	    else if(i == KeyEvent.VK_BACK_SPACE){text = "Rücktaste";}
		    	    else{
		        	    text = KeyEvent.getKeyText(i);
		    	    }

			    	try{
			    		this.text = new Schrift(new String[]{text}, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
			    	}catch(Exception e){
						this.text = new Schrift(new String[]{"Unbekannt","Unknown"}, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
			    	}
					for(int w = 0; w < Main.AllBOption.length; w++){
						Main.AllBOption[w].ResetDeaktiviert();
					}
	    			Achievement.add(10, 2);
				}
			}
			
			/*for(int i = 0; i < 40; i++){
				if(Main.kontroller.TasteIsDrueck(i)){
					bol = false;
					Main.SteuerungSet = false;
					Keyboard.setCooldown(i);
					key = i+1025;
					String text = "?";
	            	if(Main.kontroller.isXBox()){
	            		if(i == 0){text = "A";}
	            		else if(i == 1){text = "B";}
	            		else if(i == 2){text = "X";}
	            		else if(i == 3){text = "Y";}
	            		else if(i == 4){text = "LB";}
	            		else if(i == 5){text = "RB";}
	    				
	    				if(Main.Windows){
	    					if(i == 6){text = "Back";}
	    					if(i == 7){text = "Start";}
	    					if(i == 8){text = "LS";}
	    					if(i == 9){text = "RS";}
	    					if(i == 30){text = "Oben (HS)";}
	    					if(i == 31){text = "Unten (HS)";}
	    					if(i == 32){text = "Links (HS)";}
	    					if(i == 33){text = "Rechts (HS)";}
	    				}
	    				
	    				if(Main.Mac){
	    					if(i == 6){text = "LS";}
	    					if(i == 7){text = "RS";}
	    					if(i == 8){text = "Start";}
	    					if(i == 9){text = "Back";}
	    					if(i == 10){text = "XBox";}
	    					if(i == 11){text = "Oben (HS)";}
	    					if(i == 12){text = "Unten (HS)";}
	    					if(i == 13){text = "Links (HS)";}
	    					if(i == 14){text = "Rechts (HS)";}
	    				}
	    				
	            		if(i == 20){text = "Rechts (LS)";}
	    				if(i == 21){text = "Links (LS)";}
	    				if(i == 22){text = "Unten (LS)";}
	    				if(i == 23){text = "Oben (LS)";}
	    				if(i == 24){text = "Rechts (RS)";}
	    				if(i == 25){text = "Links (RS)";}
	    				if(i == 26){text = "Unten (RS)";}
	    				if(i == 27){text = "Oben (RS)";}
	    				if(i == 28){text = "RT";}
	    				if(i == 29){text = "LT";}
	    				if(i == 30){text = "Oben (D-Pad)";}
	    				if(i == 31){text = "Unten (D-Pad)";}
	    				if(i == 32){text = "Links (D-Pad)";}
	    				if(i == 33){text = "Rechts (D-Pad)";}
	            	
	            	}else{
	            		if(i < 20){text = "Taste " + i;}
	            		if(i == 20){text = "Rechts (LS)";}
	    				if(i == 21){text = "Links (LS)";}
	    				if(i == 22){text = "Unten (LS)";}
	    				if(i == 23){text = "Oben (LS)";}
	    				if(i == 24){text = "Rechts (RS)";}
	    				if(i == 25){text = "Links (RS)";}
	    				if(i == 26){text = "Unten (RS)";}
	    				if(i == 27){text = "Oben (RS)";}
	    				if(i == 28){text = "RT";}
	    				if(i == 29){text = "LT";}
	    				if(i == 30){text = "Oben (D-Pad)";}
	    				if(i == 31){text = "Unten (D-Pad)";}
	    				if(i == 32){text = "Links (D-Pad)";}
	    				if(i == 33){text = "Rechts (D-Pad)";}
	            	}
					this.text = new Schrift(text, but.getX()+but.getWidth()/2, true, but.getY()+but.getHeight()/2, true, 0.4);
					for(int w = 0; w < Main.AllBOption.length; w++){
						Main.AllBOption[w].ResetDeaktiviert();
					}
				}
			}*/
		}
		
		if(but.isDrueck()){
			if(bol){
				bol = false;
				Main.SteuerungSet = false;
				for(int w = 0; w < Main.AllBOption.length; w++){
					Main.AllBOption[w].ResetDeaktiviert();
				}
			}else{
				bol = true;
				Main.SteuerungSet = true;
				for(int w = 0; w < Main.AllBOption.length; w++){
					if(Main.AllBOption[w].getName() != but.getName()){
						Main.AllBOption[w].setDeaktiviert();
					}
				}
			}
			
		}
	}
}
