/*package Spaceshot;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class PlayController {
	private boolean[] TasteIsDrueck = new boolean[40];
	private boolean angeschlossen = false, xbox = false;
	private Controller controller;
	private Component[] tasten, sticks;
	private float xAxe, yAxe, RxAxe, RyAxe, RT, LT, HS;
	
	public PlayController(){
		for(int i = 0; i > TasteIsDrueck.length; i++){
			TasteIsDrueck[i] = false;
		}

		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
	    
	    for(int i = 0; i < controllers.length; i++){
	        Controller controller2 = controllers[i];
	        if(controller2.getType() == Controller.Type.STICK || controller2.getType() == Controller.Type.GAMEPAD || 
	        	controller2.getType() == Controller.Type.WHEEL || controller2.getType() == Controller.Type.FINGERSTICK){
	        	controller = controller2;
	        	System.out.println(controller.getName());
	        	if(controller.getName().contains("XBOX") ||
	        	   controller.getName().contains("Xbox")){xbox = true;}
	        	angeschlossen = true;
	        }
	    }
	    if(angeschlossen){
	    	if(controller != null){
    			Component[] components = controller.getComponents();
    			tasten = new Component[20];
    			sticks = new Component[20];
    			
    			int wert = 0;
    			boolean taste = false;
    			int wert2 = 0;
    			
    			for(int j = 0; j < components.length; j++){
    				taste = false;
    				for(int z = 0; z < 20; z++){
    					if(components[j].getIdentifier().getName().contains(String.valueOf(z))){
    						tasten[wert] = components[j];
    						wert += 1;
    						taste = true;
    						break;
    					}
    				}
    				if(!taste){
    					sticks[wert2] = components[j];
						wert2 += 1;
    				}
    			}
    		}
	    }
	}
	
	public boolean TasteIsDrueck(int i){
		if(i >= 0 && Main.TastenCooldown <= 0){return TasteIsDrueck[i];} 
		return false;
	}
	
	public float xAxe(){return xAxe;}
	public float yAxe(){return yAxe;}
	
	public float RxAxe(){return RxAxe;}
	public float RyAxe(){return RyAxe;}
	
	public String what(){try{return controller.getName();}catch(Exception e){return "?";}}
	
	public boolean Angeschlossen(){return angeschlossen;}
	public void setAngeschlossen(boolean b){angeschlossen = b;}
	
	public Controller getKontroller(){return controller;}
	public void setKontroller(Controller c){controller = c;}
	
	public boolean isXBox(){return xbox;}
	
	public void update(){
		for(int i = 0; i < TasteIsDrueck.length; i++){
			TasteIsDrueck[i] = false;
		}
		
		if(angeschlossen){
			if(!controller.poll()){
				angeschlossen = false;
			}
			
			controller.poll();
			
			for(int i = 0; i < tasten.length; i++){
				if(tasten[i] != null){
					if(tasten[i] != null && tasten[i].getPollData() != 0){
						TasteIsDrueck[Integer.valueOf(tasten[i].getIdentifier().getName())] = true;
					}
				}
			}
			
			xAxe = 0;
			yAxe = 0;
			RxAxe = 0;
			RyAxe = 0;
			RT = 0;
			LT = 0;
			HS = 0;
			for(int i = 0; i < sticks.length; i++){
				if(sticks[i] != null){
					if(sticks[i].getIdentifier().getName().equals("x")){
						xAxe = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("y")){
						yAxe = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("rx")){
						RxAxe = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("ry")){
						RyAxe = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("z")){
						LT = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("rz")){
						RT = sticks[i].getPollData();
					}
					if(sticks[i].getIdentifier().getName().equals("pov")){
						HS = sticks[i].getPollData();
					}
				}
				if(xAxe >= 0.2){TasteIsDrueck[20] = true;} 
				else if(xAxe <= -0.2){TasteIsDrueck[21] = true;}
				if(yAxe >= 0.2){TasteIsDrueck[22] = true;}
				else if(yAxe <= -0.2){TasteIsDrueck[23] = true;}
				
				if(RxAxe >= 0.2){TasteIsDrueck[24] = true;} 
				else if(RxAxe <= -0.2){TasteIsDrueck[25] = true;}
				if(RyAxe >= 0.2){TasteIsDrueck[26] = true;}
				else if(RyAxe <= -0.2){TasteIsDrueck[27] = true;}
				
				if(Main.Mac){
					if(RT >= 0.2){TasteIsDrueck[28] = true;} 
					
					if(LT >= 0.2){TasteIsDrueck[29] = true;}
				}else{
					if(LT <= -0.2){TasteIsDrueck[28] = true;} 
					
					if(LT >= 0.2){TasteIsDrueck[29] = true;}
				
					//Oben Links
		            if(HS == 0.125){
		            	TasteIsDrueck[30] = true;
		            	TasteIsDrueck[32] = true;
		            //Oben
		            }else if(HS == 0.25){
		            	TasteIsDrueck[30] = true;
		            	
		            //Oben Rechts
		            }else if(HS == 0.375){
		            	TasteIsDrueck[30] = true;
		            	TasteIsDrueck[33] = true;
		            	
		            //Rechts
		            }else if(HS == 0.5){
		            	TasteIsDrueck[33] = true;
		            
		            //Rechts Unten
		            }else if(HS == 0.625){
		            	TasteIsDrueck[33] = true;
		            	TasteIsDrueck[31] = true;
		            	
		            //Unten
		            }else if(HS == 0.75){
		            	TasteIsDrueck[31] = true;
		            
		            //Unten Links
		            }else if(HS == 0.875){
		            	TasteIsDrueck[31] = true;
		            	TasteIsDrueck[32] = true;
		            	
		            //Links
		            }else if(HS == 1){
		            	TasteIsDrueck[32] = true;
		            }
				}
			}
		}
	}
}*/
