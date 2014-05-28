package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
import graphlibrary.core.Graph;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Конкретный интерфейс представления графа в виде списка смежности
 */
public class SListGraphManager implements GraphAppearanceInterface {

    private Graph graph;
    //вывод списка смежности
    public void outputGraph(GraphSource c) {
        try {
            BufferedWriter fm = new BufferedWriter(new FileWriter(c.getFilename()));
            fm.write("N = " + graph.getE());
            fm.newLine();
            for (int i = 0; i < graph.getE(); i++) {
                fm.write(String.valueOf(i + 1) + ": ");
                for (int j = 0; j < graph.getSlist()[i].size(); j++) {
                    fm.write(String.valueOf(graph.getSlist()[i].get(j).getV2()) + " ");
                }

                fm.newLine();
            }
            fm.flush();
            fm.close();


        } catch (IOException ex) {
            Logger.getLogger(VMatrixGraphManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ;
    //не будет использоваться
    //заглушка
    public Edge isEnable(int v1, int v2) {
        //!!
        return null;
    }

    public SListGraphManager(Graph graph) {
        this.graph = graph;
    }
}
