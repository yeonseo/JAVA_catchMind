package draw;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class DrowController implements Initializable {
	static ArrayList<VertexClass> arPt = new ArrayList<VertexClass>();
    static boolean down = false;

    
    
	@FXML Canvas canvas;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	public void handlerCuoserAction(MouseEvent event) {
		DrawCanvas drow = new DrawCanvas(arPt);
		GraphicsContext g = canvas.getGraphicsContext2D();
		
		if(event.getEventType()==MouseEvent.MOUSE_CLICKED) {
			System.out.println("Hey!");
			System.out.println("X : "+ event.getX() + ", Y : "+event.getY()+" No Dorw");
			arPt.add(new VertexClass(event.getX(),event.getY(),false, null));
		}
		if(event.getEventType()==MouseEvent.MOUSE_DRAGGED ) {
			System.out.println("Hey!");
			System.out.println("X : "+ event.getX() + ", Y : "+event.getY()+" Dorw");
			arPt.add(new VertexClass(event.getX(),event.getY(),true, null));
			
		}
		drow.paint(g);
		if(event.getEventType()==MouseEvent.MOUSE_RELEASED ) {
			System.out.println("Hey!");
			System.out.println("X : "+ event.getX() + ", Y : "+event.getY()+" No Dorw");
			arPt.add(new VertexClass(event.getX(),event.getY(),false, null));
			
		}
		drow.paint(g);
	}
	
}


class DrawCanvas extends Canvas{
	ArrayList<VertexClass> arPt;
	DrawCanvas(ArrayList<VertexClass> arPt) {
	      this.arPt = arPt;
	}
	
	public void paint(GraphicsContext g) {
		g.setLineWidth(2.0);
		g.setStroke(Color.RED);
		
		 for (int i = 0 ; i < arPt.size() - 1; i++) {
			 if (arPt.get(i).draw) {
				 g.strokeLine(arPt.get(i).x, arPt.get(i).y, arPt.get(i+1).x, arPt.get(i+1).y);
//				 System.out.println(arPt.get(i).x+" "+arPt.get(i).y+" "+arPt.get(i+1).x+" "+arPt.get(i+1).y);
			 }
		 }
	}
}

