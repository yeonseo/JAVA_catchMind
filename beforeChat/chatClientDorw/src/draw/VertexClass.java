package draw;

import javafx.scene.paint.Color;

public class VertexClass {
	public double x; public double y;
	public boolean draw;
	public Color color = Color.BLACK;
    public VertexClass(double d, double e, boolean draw,Color color) {
           this.x = d;  this.y = e; this.draw = draw; this.color = color;
    }    
}
