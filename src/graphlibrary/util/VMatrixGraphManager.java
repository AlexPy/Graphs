package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
import graphlibrary.core.Graph;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Конкретный интерфейс представления графа в виде матрицы смежности
 */
public class VMatrixGraphManager implements GraphAppearanceInterface{
    private Graph graph;

    //вывод матрицы смежности
    public void outputGraph(GraphSource c){
        try {
            BufferedWriter fm = new BufferedWriter(new FileWriter(c.getFilename()));
            fm.write("N = " + graph.getE());
            fm.newLine();
            for (int i = 0; i < graph.getE(); i++){
                for (int j = 0; j < graph.getE(); j++){
                    fm.write(graph.getVmatrix()[i][j] + " ");
                }
                fm.newLine();
            
            }
                fm.flush();
            fm.close();
            
            
            } catch (IOException ex) {
            Logger.getLogger(VMatrixGraphManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    };
    
    public Edge isEnable(int v1, int v2){
        if (graph.getVmatrix()[v1][v2] != Graph.INF)
            return new Edge(v1, v2, graph.getVmatrix()[v1][v2]);
        else
            return null;
    }

    public VMatrixGraphManager(Graph graph) {
        this.graph = graph;
    }
    
}
