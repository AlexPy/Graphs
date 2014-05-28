package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
import graphlibrary.core.Graph;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Конкретный интерфейс представления графа в виде простого списка ребер
 */
public class VListGraphManager implements GraphAppearanceInterface {

    private Graph graph;

    public void outputGraph(GraphSource c) {
        try {
            BufferedWriter fm = new BufferedWriter(new FileWriter(c.getFilename()));
            fm.write("N = " + graph.getE());
            fm.newLine();
            for (int i = 0; i < graph.getVlist().size(); i++) {
                int v1 = graph.getVlist().get(i).getV1();
                int v2 = graph.getVlist().get(i).getV2();
                int w = graph.getVlist().get(i).getWeight();

                fm.write(v1 + "->" + v2 + " = " + w);
                fm.newLine();
            }
            fm.flush();
            fm.close();


        } catch (IOException ex) {
            Logger.getLogger(VMatrixGraphManager.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    ;
    
//есть ли ребро v1-v2 в нашем списке
    public Edge isEnable(int v1, int v2) {
        boolean f = false;
        for (int i = 0; i < graph.getVlist().size(); i++) {
            if (v1 == graph.getVlist().get(i).getV1()
                    && v2 == graph.getVlist().get(i).getV2()) {
                return new Edge(v1, v2, graph.getVlist().get(i).getWeight());

            }


        }
        return null;
    }

    public VListGraphManager(Graph graph) {
        this.graph = graph;
    }
}
