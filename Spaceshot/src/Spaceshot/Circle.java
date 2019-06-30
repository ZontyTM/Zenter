package Spaceshot;

public class Circle {
	private float mitteX;
	private float mitteY;
	private float radius;
	private float width;
	private float height;
	
	Circle(float x, float y, float width, float height){
		this.width = width;
		this.height = height;
		mitteX = x + width;  
		mitteY = y + height;
		radius = height/2;
	}
	public float getX(){
		return mitteX;
	}
	public float getY(){
		return mitteY;
	}
	public float getRadius(){
		return radius;
	}
	public void setX(float x){
		mitteX = x + width;
	}
	public void setY(float y){
		mitteY = y + height;
	}
	public void setRadius(float i){
		radius = i;
	}
}
