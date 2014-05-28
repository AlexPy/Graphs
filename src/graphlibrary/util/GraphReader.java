package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Используется для чтения графа из файла
 */
public class GraphReader {
    //возвращает список ребер графа в файле
    //если ребро задано видом v1 - v2 = X
    //то это неориентированное ребро и будет дублировано как v1 - v2 = X и v2 - v1 = X
    //если как v1 -> v2 = X
    //то не будет дублировано(ориентированное)
    public static LinkedList<Edge> getEdges(GraphSource c) {
        try {
            LinkedList<Edge> elist = new LinkedList<Edge>();
            BufferedReader fm = new BufferedReader(new FileReader(c.getFilename()));
            String f = fm.readLine();
            f = fm.readLine();
            boolean flag = false;
            while (f != null) {
                flag = false;
                if (f.contains("->")) {
                    f = f.replace("->", ",");
                } else {
                    f = f.replace("-", ",");
                    flag = true;
                }

                f = f.replace("=", ",");

                String[] xx = f.split(",");
                int v1 = Integer.valueOf(xx[0].trim());
                int v2 = Integer.valueOf(xx[1].trim());
                int weight = Integer.valueOf(xx[2].trim());
                elist.add(new Edge(v1, v2, weight));
                if (flag) {
                    elist.add(new Edge(v2, v1, weight));
                }

                f = fm.readLine();

            }
            return elist;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, null, ex);
        }


        return null;
    }
    //возвращает количество вершин графа в файле(по сути парсит только первую строку вида N = ?)
    public static int getVertixesAmount(GraphSource c) {
        try {
            BufferedReader fm = new BufferedReader(new FileReader(c.getFilename()));
            String f = fm.readLine();
            f = f.replace("N", "");
            f = f.replace("=", "");
            f = f.trim();
            return (Integer.valueOf(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(GraphReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;

    }
}
