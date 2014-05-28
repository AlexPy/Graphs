package graphlibrary;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Graph;

public class Main {

    /**
     * Сохраняет граф в разных представлениях в текстовые файлы
     * @param x Заданный граф
     */
    public static void processGraph(Graph x){
        x.changeAppearance(Graph.MATRIX_V);
        x.outputGraph(new GraphSource("outputV.txt"));
        x.changeAppearance(Graph.MATRIX_I);
        x.outputGraph(new GraphSource("outputI.txt"));
        x.changeAppearance(Graph.LIST_V);
        x.outputGraph(new GraphSource("outputL.txt"));
        x.changeAppearance(Graph.LIST_S);
        x.outputGraph(new GraphSource("outputS.txt"));
        
    }
    
    public static void main(String[] args) {
        //Создаем пустой граф
        Graph graph = new Graph();
      
        //Читаем граф из файла
        graph.readGraph(new GraphSource("двудольныйграф"));
        
        //Сохраняем его в файлы
        processGraph(graph);
        //конец 1 лабы
        
        
        
        //начало 4 лабы
        if (graph.checkBigraph()){
            System.out.println("Граф двудольный");
            graph.showPByKun();
        }
        else
            {
            System.out.println("Граф не двудольный");
            graph.showPByEddmonds();
        
        }
        //конец 4 лабы
      
        
    }
}
