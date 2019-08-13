package Spaceshot;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener{
	private static int cooldown[] = new int[1060];
	private static boolean[] keys = new boolean[1060];
	
	public static boolean isKeyDown(int keyCode){
		return isKeyDownCd(keyCode,0);
	}
	public static boolean isKeyDownCd(int keyCode){
		return isKeyDownCd(keyCode,30);
	}
	public static boolean isKeyDownCd(int keyCode, int cd){
		if(keyCode >= 0 && keyCode < keys.length){
			if(cooldown[keyCode] > 0){
				cooldown[keyCode] -= Main.timeSinceLastFrame;
			
			}else if(cooldown[keyCode] <= 0 && keys[keyCode] == true){
				setCooldown(keyCode,cd);
				Main.f.setMouse(false);
				return true;
			}
		}
		return false;
	}
	
	public static void KeyReset(int keyCode){keys[keyCode] = false;}
	public static void setKey(int keyCode){keys[keyCode] = true;}
	
	public static void setCooldown(int keyCode) {
		setCooldown(keyCode,15);
	}
	public static void setCooldown(int keyCode, int cd) {
		cooldown[keyCode] = cd;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode >= 0 && keyCode < keys.length){keys[keyCode]=true;}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode >= 0 && keyCode < keys.length){keys[keyCode]=false;}
	}
	
	//unnoetig
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
