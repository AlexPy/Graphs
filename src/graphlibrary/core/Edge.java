package graphlibrary.core;
/**
 * Класс хранит структуру ориентированного ребра 
 */
public class Edge implements Comparable<Edge> {
    /**
     * Начальная вершина
     */
    private int v1;
    /**
     * Конечная вершина
     */
    private int v2;
    /**
     * Вес ребра
     */
    private int weight;
    
    /**
     * Используется для сортировки ребер в порядке неубывания
     */
    public int compareTo(Edge compareEdge) {

        int compareWeight = ((Edge) compareEdge).getWeight();

        return this.weight - compareWeight;

    }
    //конструктор
    public Edge(int v1, int v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }
    
    /**
     * Возвращает представление ребра в виде v1->v2=weight
     */
    public String toString() {
        return v1 + "->" + v2 + "=" + weight;
    }

    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }

    public void setV1(int v1) {
        this.v1 = v1;
    }

    public void setV2(int v2) {
        this.v2 = v2;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
