package application.draw;
import java.awt.*;

import java.awt.event.*;

import java.util.*;

import javax.swing.*;
public class FreeLineDrow {
      static ArrayList<Vertex> arPt = new ArrayList<Vertex>();
      static boolean down = false;
      public static void main(String[] args) {
             JFrame frame = new JFrame("First Swing Application");
             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             frame.setPreferredSize(new Dimension(400,300));
             frame.setLocation(500, 500);
 
             Container cp = frame.getContentPane();
             DrawPanel surface = new DrawPanel(arPt);
             cp.add(surface);
           

             surface.addMouseListener(new MouseAdapter() {
                   public void mousePressed(MouseEvent event) {
                         if (event.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                                arPt.add(new Vertex(event.getX(), event.getY(), false));
                                System.out.println(event.getX() + "," + event.getY() + " : no draw");
                         }
                   }
             });
            
             surface.addMouseMotionListener(new MouseMotionAdapter() {
                   public void mouseDragged(MouseEvent event) {
                         if (event.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
                                arPt.add(new Vertex(event.getX(), event.getY(), true));
                                System.out.println(event.getX() + "," + event.getY() + " : draw");
                         }
                         surface.repaint();
                   }
             });

            
             frame.pack();
             frame.setVisible(true);
      }
}

class Vertex {
      int x; int y;
      boolean draw;
      Vertex(int x, int y, boolean draw) {
             this.x = x;  this.y = y; this.draw = draw;
      }
}


class DrawPanel extends JPanel {
      ArrayList<Vertex> arPt;
      DrawPanel(ArrayList<Vertex> arPt) {
             this.arPt = arPt;
      }

      public void paint(Graphics g) {
             g.setColor(Color.PINK);
             for (int i = 0 ; i < arPt.size() - 1; i++) {
                   if (arPt.get(i + 1).draw) {
                         g.drawLine(arPt.get(i).x, arPt.get(i).y, arPt.get(i + 1).x, arPt.get(i + 1).y);
                   }
             }
      }
}

