package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
import graphlibrary.core.Graph;
import java.io.*;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Конкретный интерфейс представления графа в виде матрицы инцидентности
 */
public class IMatrixGraphManager implements GraphAppearanceInterface{
    private Graph graph;

    /**
     * Вывод матрицы инцидентности
     */
    public void outputGraph(GraphSource c){
        try {
            BufferedWriter fm = new BufferedWriter(new FileWriter(c.getFilename()));
            fm.write("N = " + graph.getE());
            fm.newLine();
            for (int i = 0; i < graph.getE(); i++){
                for (int j = 0; j < graph.getW(); j++){
                    fm.write(graph.getImatrix()[i][j] + " ");
                }
                fm.newLine();
            
            }
                fm.flush();
            fm.close();
            
            
            } catch (IOException ex) {
            Logger.getLogger(VMatrixGraphManager.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    };
    /**
     * Заглушка, матрица инцидентности нигде больше не будет использоваться(кроме как вывода в файл в 1 лабе для проверки)
     * Поэтому нет смысла писать этот метод
     */
    public Edge isEnable(int v1, int v2){
        return null;
    }

    public IMatrixGraphManager(Graph graph) {
        this.graph = graph;
    }
    
}
