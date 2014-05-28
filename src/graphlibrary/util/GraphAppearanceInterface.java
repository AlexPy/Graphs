package graphlibrary.util;

import graphlibrary.core.GraphSource;
import graphlibrary.core.Edge;
/**
 * Абстрактный интерфейс для какого-то представления графа
 * Любое представление должно уметь выводить себя и проверять смежность вершин
 */
public interface GraphAppearanceInterface {
    //метод выводит граф в файл исходя из конкретного представления
    //в каждой реализации он переопределен
    public void outputGraph(GraphSource c);
    //данный метод вроде не используется ни в какой конкретной реализации
    //но где-то реализован
    Edge isEnable(int v1, int v2);
}
