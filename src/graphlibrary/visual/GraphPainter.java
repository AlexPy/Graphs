package graphlibrary.visual;

import graphlibrary.core.Edge;
import graphlibrary.core.Graph;
import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;


public class GraphPainter
{
   public static Graph graph = null;
   
   public static void DrawGraph(Graph x)
   {
       GraphPainter.graph = x;
       DrawFrame frame = new DrawFrame();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.setVisible(true);
               
            
         
   }
}


class DrawFrame extends JFrame
{
   public DrawFrame()
   {
      setTitle("Graph");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

   
      DrawComponent component = new DrawComponent();
      add(component);
   }

   public static final int DEFAULT_WIDTH = 1200;
   public static final int DEFAULT_HEIGHT = 700;
}

class DrawComponent extends JComponent
{
    private static int[][] clast = new int[100][2];
    private static int counter = 0;
    
   private static double dist(int x1, int y1, int x2, int y2){
       
       return (Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1) ));
   }
    
   private static int[] getCoords(int n){
       int[] answer = new int[2];
       Random m = new Random();
       int x1 = m.nextInt(700);
       int y1 = m.nextInt(700);
       boolean flag = false;
       while (!flag){
           flag = true;
       for (int i = 0; i < counter; i++){
       if (dist(x1, y1, clast[i][0], clast[i][0]) < 200){
               flag = false;
       }
       }
       if (!flag){
         x1 = m.nextInt(700);
          y1 = m.nextInt(700);    
       }
       }
       answer[0] = x1;
       answer[1] = y1;
       clast[counter][0] = x1;
       clast[counter][1] = y1;
       return answer;
   } 
   
   public void paintComponent(Graphics g)
   {
       
      int n = GraphPainter.graph.getE();
      int m = GraphPainter.graph.getW();
  
      Graphics2D g2 = (Graphics2D) g;
      LinkedList<Edge> elist = GraphPainter.graph.getVlist();
      int[][] coord = new int[n][2];
      String chars = "①②③④⑤⑥⑦⑧⑨";
      System.out.println(n);
      for (int i = 0; i < n; i++){
          int[] x = DrawComponent.getCoords(i);
          coord[i][0] = x[0];
          coord[i][1] = x[1];
          g2.drawString(String.valueOf(chars.charAt(i)), x[0] + 10, x[1] + 10);    
      }
      
      for (int i = 0; i < m; i++){
          if (m % 2 == 1) continue;
          int x1 = coord[elist.get(i).getV1() - 1][0];
          int y1 = coord[elist.get(i).getV1() - 1][1];
          int x2 = coord[elist.get(i).getV2() - 1][0];
          int y2 = coord[elist.get(i).getV2() - 1][1];
          
          
          g2.draw(new Line2D.Double(x1,y1,x2,y2));
          g2.drawString(String.valueOf(elist.get(i).getWeight()), x1 + ((x2 - x1) / 2) + 10, y1 + ((y2 - y1) / 2)) ;  
          System.out.println(String.valueOf(elist.get(i).getWeight()));
      }
      
      

      }
}
