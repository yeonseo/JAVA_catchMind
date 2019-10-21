package Model;

import javafx.scene.paint.Color;

public class DrowInfoVO {
	private int no;
	private double x; 
	private double y;
	private boolean draw;
	private int color;
	
	
	public DrowInfoVO(double x, double y, boolean draw, int color) {
		super();
		this.x = x;
		this.y = y;
		this.draw = draw;
		this.color = color;
	}
	public DrowInfoVO(int no, double d, double e, boolean draw,int color) {
           this.no = no; this.x = d;  this.y = e; this.draw = draw; this.color = color;
    }
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public boolean isDraw() {
		return draw;
	}
	public void setDraw(boolean draw) {
		this.draw = draw;
	}
	public Color getColor(int color) {
		Color penColor = Color.BLACK;
		switch(color) {
		case 0 : penColor = Color.BLACK; break;
		case 1 : penColor = Color.RED; break;
		case 2 : penColor = Color.BLUE; break;
		case 3 : penColor = Color.BLACK; break;
		
		}
		return penColor;
	}
	public int getDBColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
    
    
}
