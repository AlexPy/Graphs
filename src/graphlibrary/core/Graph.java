package graphlibrary.core;

import graphlibrary.util.DisjointSet;
import graphlibrary.util.GraphAppearanceInterface;
import graphlibrary.util.GraphReader;
import graphlibrary.util.IMatrixGraphManager;
import graphlibrary.util.SListGraphManager;
import graphlibrary.util.VListGraphManager;
import graphlibrary.util.VMatrixGraphManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;

public class Graph implements GraphAppearanceInterface {

    /**
     * Несуществующий вес ребра
     */
    public static final int INF = 100000000;

    //номера представлений графа
    public static final int MATRIX_V = 1;
    public static final int MATRIX_I = 2;
    public static final int LIST_V = 3;
    public static final int LIST_S = 4;

    //по умолчанию граф представлен как матрица смежности
    private static final int DEFAULT_APPEARANCE = MATRIX_V;

    //менеджер для хранения представления
    private GraphAppearanceInterface manager = null;
    //номер текущего представления
    private int current_appearance;
    /**
     * количество ребер
     */
    private int W;
    /**
     * количество вершин
     */
    private int E;
    /**
     * матрица смежности
     */
    private int[][] vmatrix;
    /**
     * список ребер
     */
    private LinkedList<Edge> vlist;
    /**
     * список смежности
     */
    private LinkedList<Edge>[] slist;
    /**
     * матрица инцидентности
     */
    private int[][] imatrix;

    //эти два списка хранят первую и вторую долю графа в виде списка номеров вершин
    //нужно в 4 лабе
    private static LinkedList<Integer> d1;
    private static LinkedList<Integer> d2;

//массивы для 4 лабы
    static int[] match;
    static int[] p;
    static int[] base;
    static int[] q;
    static boolean[] blossom;

    //конструкторы
    public Graph(int appearance) {
        current_appearance = appearance;
        updateGraphAppearance();
    }

    public Graph() {
        current_appearance = DEFAULT_APPEARANCE;
        updateGraphAppearance();
    }

    //массивы для 4 лабы
    private static int[] mt;
    private static boolean[] used;

    public int getW() {
        return W;
    }

    public int getE() {
        return E;
    }

    public int[][] getVmatrix() {
        return vmatrix;
    }

    public LinkedList<Edge> getVlist() {
        return vlist;
    }

    public int[][] getImatrix() {
        return imatrix;
    }

    /**
     * Добавляет ребро в граф
     *
     * @param isOriented true, если ребро ориентировано
     */
    public void addEdge(int v1, int v2, int w, boolean isOriented) {

        vlist.add(new Edge(v1, v2, w));
        if (!isOriented) {
            vlist.add(new Edge(v2, v1, w));
        }
        vmatrix[v1 - 1][v2 - 1] = w;
        if (!isOriented) {
            vmatrix[v2 - 1][v1 - 1] = w;
        }
        slist[v1 - 1].add(new Edge(v1, v2, w));
        if (!isOriented) {
            slist[v2 - 1].add(new Edge(v2, v1, w));
        }

    }

    /**
     * Возвращает минимальное остовное дерево(MST) данного графа. Для поиска
     * используется алгоритм Борувки.
     */
    public Graph getMSTbyBoruvka() {
        //создали новый граф, в него сохраним результат
        Graph newGraph = new Graph();
        int cost = 0;
        //список ребер для результата
        LinkedList<Edge> res = new LinkedList<Edge>();

        //создаем копию текущего списка ребер
        LinkedList<Edge> list = new LinkedList<Edge>(vlist);

        int[] repm = new int[E];
        //нужно удалить все повторяющиеся ребра
        //поскольку граф задается в неориентированном виде
        int p[] = DisjointSet.createSet(E);
        int n = list.size() / 2;
        for (int i = 0; i < n; i++) {
            list.remove(1 + i);
        }
        //очередь для обхода в ширину
        LinkedList<Integer> ss = new LinkedList<Integer>();
        while (res.size() < E - 1) {

            for (int i = 0; i < E; i++) {
                repm[i] = -1;
            }

            for (int i = 0; i < list.size(); i++) {
                int rep1 = DisjointSet.root(p, list.get(i).getV1() - 1);
                int rep2 = DisjointSet.root(p, list.get(i).getV2() - 1);
                System.out.println("Смотрим ребро " + (list.get(i).getV1()) + " - " + (list.get(i).getV2()));

                if (rep1 != rep2) {
                    System.out.println("Вершины лежат в разных компонентах");
                    if (repm[rep1] == -1) {
                        repm[rep1] = i;

                    } else if (list.get(i).getWeight() < list.get(repm[rep1]).getWeight()) {
                        repm[rep1] = i;

                    } else if (list.get(i).getWeight() < list.get(repm[rep2]).getWeight()) {
                        repm[rep2] = i;

                    }

                }

            }

            //     System.out.println("=" + list.size());
            for (int j = 0; j < E; j++) {
                if (repm[j] != -1) {
                    int rep1 = DisjointSet.root(p, list.get(repm[j]).getV1() - 1);
                    int rep2 = DisjointSet.root(p, list.get(repm[j]).getV2() - 1);
                    System.out.println("Добавляем ребро " + (list.get(repm[j]).getV1()) + " - " + (list.get(repm[j]).getV2()));
                    System.out.println("Объединяем вершины " + (rep1 + 1) + " и " + (rep2 + 1));
                    DisjointSet.unite(p, rep1, rep2);
                    res.add(list.get(repm[j]));
                    cost += list.get(repm[j]).getWeight();
                    ss.add(repm[j]);

                }
            }

        }

        newGraph.initGraph(E, res.size());

        for (int i = 0; i < res.size(); i++) {
            newGraph.addEdge(res.get(i).getV1(), res.get(i).getV2(), res.get(i).getWeight(), false);
        }

        System.out.println("BORUVKA MST COST = " + cost);

        return newGraph;
    }

    //возвращает truе если массив содержит хотя бы одну -1
    private boolean containsMinusOne(int[] arr) {
        boolean flag = false;
        for (int i = 0; i < E; i++) {
            if (arr[i] == -1) {
                flag = true;
            }
        }
        return flag;
    }

    //возвращает truе если массив содержит хотя бы один ноль
    private boolean containsZero(int[] arr) {
        boolean flag = false;
        for (int i = 0; i < E; i++) {
            if (arr[i] == 0) {
                flag = true;
            }
        }
        return flag;
    }

     //метод возвращает кол-во компонент связности графа
    //раскрашивает из каждой незакрашенной вершины граф обходом в ширину
    //количество цветов = количество компонент связности
    private int checkComps(LinkedList<Edge>[] slist) {
        int comps = 0;
        LinkedList<Edge>[] s = new LinkedList[E];
        for (int i = 0; i < E; i++) {
            s[i] = new LinkedList<Edge>(slist[i]);
        }

        int[] check = new int[E];
        for (int i = 0; i < E; i++) {
            check[i] = -1;
        }

        while (containsMinusOne(check)) {
            //   System.out.println("Новая компонента");
            comps++;
            LinkedList<Integer> ss = new LinkedList<Integer>();
            int v = -1;
            for (int i = 0; i < E; i++) {
                if (check[i] == -1) {
                    v = i;
                    break;
                }
            }
            check[v] = 1;
            ss.add(v);
            while (ss.size() != 0) {
                v = ss.get(0);
                ss.remove(0);
                //  System.out.println("Покрасили " + (v + 1));
                /**
                 * for (int j = 0; j < s[v].size(); j++){
                 * System.out.print(s[v].get(j).getV2() + " "); }
            System.out.println();
                 */

                for (int j = 0; j < s[v].size(); j++) {
                    if (check[s[v].get(j).getV2() - 1] == -1) {
                        ss.add(s[v].get(j).getV2() - 1);
                    }

                }

                check[v] = 1;
            }

        }

        return comps;
    }

    //возвращает true если граф двудольный
    //сохраняет доли в d1 и d2
    public boolean checkBigraph() {
        d1 = new LinkedList<Integer>();
        d2 = new LinkedList<Integer>();
        int comps = 0;
        LinkedList<Edge>[] s = new LinkedList[E];
        for (int i = 0; i < E; i++) {
            s[i] = new LinkedList<Edge>(slist[i]);
        }

        int[] check = new int[E];
        for (int i = 0; i < E; i++) {
            check[i] = 0;
        }

        while (containsZero(check)) {
            LinkedList<Integer> ss = new LinkedList<Integer>();
            int v = -1;
            for (int i = 0; i < E; i++) {
                if (check[i] == 0) {
                    v = i;
                    break;
                }
            }
            check[v] = 1;
            ss.add(v);
            while (ss.size() != 0) {
                v = ss.get(0);
                ss.remove(0);
                for (int j = 0; j < s[v].size(); j++) {
                    if (check[s[v].get(j).getV2() - 1] == 0) {
                        check[s[v].get(j).getV2() - 1] = -check[v];
                        ss.add(s[v].get(j).getV2() - 1);
                    } else {
                        if (check[v] == check[s[v].get(j).getV2() - 1]) {
                            return false;
                        }

                    }
                }

            }

        }
        for (int i = 0; i < E; i++) {
            if (check[i] == 1) {
                System.out.println("В первой доле вершина" + (i + 1));
                d1.add(i);
            } else {
                d2.add(i);
            }
        }

        return true;
    }

    public boolean try_kuhn(int v) {

        if (used[v]) {
            return false;
        }
        used[v] = true;
        for (int i = 0; i < slist[d1.get(v)].size(); ++i) {
            int to2 = slist[d1.get(v)].get(i).getV2() - 1;
            int to = 0;
            for (int j = 0; j < d2.size(); j++) {
                if (d2.get(j) == to2) {
                    to = j;
                    break;
                }
            }
            if (mt[to] == -1 || try_kuhn(mt[to])) {
                mt[to] = v;
                return true;
            }
        }
        return false;
    }

    private int lca(int a, int b) {
        for (int i = 0; i < E; i++) {
            used[i] = false;
        }
        // поднимаемся от вершины a до корня, помечая все чётные вершины
        for (;;) {
            a = base[a];
            used[a] = true;
            if (match[a] == -1) {
                break; // дошли до корня
            }
            a = p[match[a]];
        }
        // поднимаемся от вершины b, пока не найдём помеченную вершину
        for (;;) {
            b = base[b];
            if (used[b]) {
                return b;
            }
            b = p[match[b]];
        }
    }

    private void mark_path(int v, int b, int children) {
        while (base[v] != b) {
            blossom[base[v]] = blossom[base[match[v]]] = true;
            p[v] = children;
            children = match[v];
            v = p[match[v]];
        }
    }

    private int find_path(int root) {
        for (int i = 0; i < E; i++) {
            p[i] = -1;
            used[i] = false;
        }
        for (int i = 0; i < E; ++i) {
            base[i] = i;
        }

        used[root] = true;
        int qh = 0, qt = 0;
        q[qt++] = root;
        while (qh < qt) {
            int v = q[qh++];
            for (int i = 0; i < slist[v].size(); ++i) {
                int to = slist[v].get(i).getV2() - 1;
                if (base[v] == base[to] || match[v] == to) {
                    continue;
                }
                if (to == root || match[to] != -1 && p[match[to]] != -1) {
                    int curbase = lca(v, to);
                    for (int j = 0; i < E; i++) {
                        blossom[j] = false;
                    }
                    mark_path(v, curbase, to);
                    mark_path(to, curbase, v);

                    for (int i1 = 0; i1 < E; ++i1) {
                        if (blossom[base[i1]]) {
                            base[i1] = curbase;
                            if (!used[i1]) {
                                used[i1] = true;
                                q[qt++] = i1;
                            }
                        }
                    }
                } else if (p[to] == -1) {
                    p[to] = v;
                    if (match[to] == -1) {
                        return to;
                    }
                    to = match[to];
                    used[to] = true;
                    q[qt++] = to;
                }
            }
        }
        return -1;
    }

    //Алгоритм Эдмондса для 4 лабы
    public void showPByEddmonds() {
         //массив ответа
        //match[i] соответствует вершине i 
        match = new int[E];
        q = new int[E];
        p = new int[E];
        base = new int[E];
        used = new boolean[E];
        blossom = new boolean[E];
        for (int i = 0; i < E; i++) {
            match[i] = -1;
        }

        for (int i = 0; i < E; ++i) {
            if (match[i] == -1) {
                int v = find_path(i);
                while (v != -1) {
                    int pv = p[v];
                    int ppv = match[pv];
                    match[v] = pv;
                    match[pv] = v;
                    v = ppv;
                }
            }
        }
        int[] ch = new int[E];

        for (int i = 0; i < E; i++) {
            if (match[i] != -1) {
                if (ch[i] == 0 && ch[match[i]] == 0) {
                    System.out.println((i + 1) + " - " + (match[i] + 1));
                }
                ch[i] = 1;
                ch[match[i]] = 1;
            }
        }
    }

    //Алгоритм Куна для 4 лабы
    public void showPByKun() {
        int n, k;
        n = d1.size();
        k = d2.size();
        System.out.println(n);
        System.out.println(k);

        mt = new int[E];
        used = new boolean[E];

        for (int i = 0; i < k; i++) {
            mt[i] = -1;
        }

        for (int v = 0; v < n; ++v) {

            for (int i = 0; i < n; i++) {
                used[i] = false;
            }
            try_kuhn(v);
        }

        for (int i = 0; i < k; ++i) {
            if (mt[i] != -1) {
                System.out.println((d1.get(mt[i]) + 1) + " " + (d2.get(i) + 1) + "\n");
            }

        }
    }

    //Алгоритм Рида
    public void showEulerByRid() {
        int start = 0;

        System.out.println("=Начинаем искать эйлеров цикл по Риду=");
        int eq1 = 0;
        int eq2 = 0;
        for (int i = 0; i < E; i++) {
            if (slist[i].size() % 2 == 0) {
                eq2++;
            } else {
                eq1++;
            }
        }
        System.out.println("Нечетных " + eq1);
        System.out.println("четных " + eq2);
        if (eq1 == 2) {
            for (int i = 0; i < E; i++) {
                if (slist[i].size() % 2 != 0) {
                    start = i;
                    break;
                }
            }

        } else if (eq1 > 0) {
            System.out.println("Не существует эйлерова цикла");
        }

        LinkedList<Edge> list = new LinkedList<Edge>(vlist);
        LinkedList<Edge>[] s = new LinkedList[E];
        for (int i = 0; i < E; i++) {
            s[i] = new LinkedList<Edge>(slist[i]);
        }

        int n = list.size() / 2;
        for (int i = 0; i < n; i++) {
            list.remove(1 + i);
        }
        LinkedList<Integer> HEAD = new LinkedList<Integer>();
        LinkedList<Integer> TAIL = new LinkedList<Integer>();

        int v = start;

        HEAD.addLast(v);

        while (HEAD.size() != 0) {
            while (s[HEAD.getLast()].size() > 0) {
                int v1 = HEAD.getLast();
                int v2 = s[v1].get(0).getV2() - 1;
                HEAD.addLast(v2);

                s[v1].remove(0);
                for (int j1 = 0; j1 < s[v2].size(); j1++) {
                    if (s[v2].get(j1).getV2() == (v1 + 1)) {
                        s[v2].remove(j1);
                    }
                }

            }

            while (HEAD.size() != 0 && s[HEAD.getLast()].size() == 0) {
                System.out.println("Идем в вершину " + (HEAD.getLast() + 1));
                TAIL.addLast(HEAD.getLast());
                HEAD.removeLast();
            }

        }
    }

    //Алгоритм Флёри
    public void showEulerByFleury() {
        int start = 0;
        int eq1 = 0;
        int eq2 = 0;
        for (int i = 0; i < E; i++) {
            if (slist[i].size() % 2 == 0) {
                eq2++;
            } else {
                eq1++;
            }
        }
        System.out.println("Нечетных " + eq1);
        System.out.println("четных " + eq2);
        if (eq1 == 2) {
            for (int i = 0; i < E; i++) {
                if (slist[i].size() % 2 != 0) {
                    start = i;
                    break;
                }
            }

        } else if (eq1 > 0) {
            System.out.println("Не существует эйлерова цикла");
        }
        System.out.println("=Начинаем искать эйлеров цикл по Флёри=");
        LinkedList<Edge> list = new LinkedList<Edge>(vlist);
        LinkedList<Edge>[] s = new LinkedList[E];
        for (int i = 0; i < E; i++) {
            s[i] = new LinkedList<Edge>(slist[i]);
        }

        //System.out.println("NOW " + checkComps(s));
        int n = list.size() / 2;
        for (int i = 0; i < n; i++) {
            list.remove(1 + i);
        }

        //начинаем цикл с вершины start
        int v = start;
        //длина цикла - количество ребер в текущем графе
        //в эйлеров цикл должно войти каждое ребро
        for (int i = 0; i < list.size(); i++) {

            int v1 = v;

            /**
             * for (int j = 0; j < s[v1].size(); j++){
             * System.out.print(s[v1].get(j).getV2() + " "); }
            System.out.println();
             */
            //пока еще не знаем какое ребро выбрать
            int num = 0;

            //цикл по всем ребрам инцидентным текущей вершине
            for (int j = 0; j < s[v1].size(); j++) {
                //смотрим сколько компонент связности сейчас есть
                int q1 = checkComps(s);
            //    System.out.println("NOW1 " + q1);

                LinkedList<Edge>[] s1 = new LinkedList[E];
                for (int i1 = 0; i1 < E; i1++) {
                    s1[i1] = new LinkedList<Edge>(s[i1]);
                }

                int vs = s1[v1].get(j).getV2() - 1;

                for (int j1 = 0; j1 < s1[vs].size(); j1++) {
                    if (s1[vs].get(j1).getV2() == (v1 + 1)) {
                        s1[vs].remove(j1);
                    }
                }

                s1[v1].remove(j);
                //удалили текущее ребро и снова смотрим сколько компонент
                int q2 = checkComps(s1);

                //System.out.println("NOW2 " + q2);
                //если не изменилось - не мост
                //можно добавлять в эйлеров цикл
                if (q1 == q2) {
                    num = j;
                    break;

                }

            }

            int v2 = s[v1].get(num).getV2() - 1;
            s[v1].remove(num);
            System.out.println("Идем из вершины " + (v1 + 1) + " в вершину " + (v2 + 1));
            v = v2;

            //System.out.println("=" + v1);
            for (int j = 0; j < s[v].size(); j++) {
                if (s[v].get(j).getV2() == (v1 + 1)) {
                    s[v].remove(j);
                }
            }

        }
        System.out.println("Эйлеров цикл найден!");
    }

    /**
     * Возвращает минимальное остовное дерево(MST) данного графа. Для поиска
     * используется алгоритм Краскала.
     */
    public Graph getMSTbyKraskal() {
        Graph newGraph = new Graph();
        int cost = 0;
        LinkedList<Edge> res = new LinkedList<Edge>();
        LinkedList<Edge> wlist = new LinkedList<Edge>(vlist);
        Edge[] array = wlist.toArray(new Edge[wlist.size()]);
        Arrays.sort(array);

        int p[] = DisjointSet.createSet(E);

        for (int i = 0; i < W; ++i) {
            int a = array[i].getV1() - 1;
            int b = array[i].getV2() - 1;
            int l = array[i].getWeight();

            if (DisjointSet.root(p, a) != DisjointSet.root(p, b)) {
                cost += l;
                System.out.println("=" + l);

                res.add(array[i]);
                DisjointSet.unite(p, a, b);
            }

        }
        newGraph.initGraph(E, res.size());
        for (int i = 0; i < res.size(); i++) {
            newGraph.addEdge(res.get(i).getV1(), res.get(i).getV2(), res.get(i).getWeight(), false);
        }
        System.out.println("KRASKAL MST COST = " + cost);
        return newGraph;

    }

    /**
     * Возвращает минимальное остовное дерево(MST) данного графа. Для поиска
     * используется алгоритм Прима.
     */
    public Graph getMSTbyPrima() {
        Graph newGraph = new Graph();
        LinkedList<Edge> nlist = new LinkedList<Edge>();

        boolean[] used = new boolean[E];
        int[] min_e = new int[E];
        for (int i = 0; i < E; i++) {
            min_e[i] = INF;
        }
        int[] sel_e = new int[E];
        for (int i = 0; i < E; i++) {
            sel_e[i] = -1;
        }
        min_e[0] = 0;
        int cost = 0;

        for (int i = 0; i < E; i++) {
            int v = -1;
            for (int j = 0; j < E; j++) {
                if (!used[j] && (v == -1 || min_e[j] < min_e[v])) {
                    v = j;
                }
            }

            if (min_e[v] == INF) {
                System.out.println("No MST!");
                return null;
            }

            used[v] = true;
            if (sel_e[v] != -1) {
                nlist.add(new Edge(v + 1, sel_e[v] + 1, vmatrix[v][sel_e[v]]));
                cost += vmatrix[v][sel_e[v]];
            }

            for (int to = 0; to < E; to++) {
                if (vmatrix[v][to] < min_e[to]) {
                    min_e[to] = vmatrix[v][to];
                    sel_e[to] = v;
                }
            }

        }

        newGraph.initGraph(E, nlist.size());
        for (int i = 0; i < nlist.size(); i++) {
            newGraph.addEdge(nlist.get(i).getV1(), nlist.get(i).getV2(), nlist.get(i).getWeight(), false);
        }
        System.out.println("PRIMA MST COST = " + cost);
        return newGraph;
    }
    //сменить представление графа
    public void changeAppearance(int NAME) {
        current_appearance = NAME;
        updateGraphAppearance();
    }

    ;

private void updateGraphAppearance() {
        if (current_appearance == MATRIX_V) {
            manager = new VMatrixGraphManager(this);
        }
        if (current_appearance == LIST_V) {
            manager = new VListGraphManager(this);
        }
        if (current_appearance == MATRIX_I) {
            manager = new IMatrixGraphManager(this);
        }
        if (current_appearance == LIST_S) {
            manager = new SListGraphManager(this);
        }
    }

    ;

    public GraphAppearanceInterface getManager() {
        return manager;
    }
    
    //инициализирует граф с E вершинами и W ребрами
    private void initGraph(int E, int W) {
        this.E = E;
        this.W = W;
        vmatrix = new int[E][E];
        slist = new LinkedList[E];
        vlist = new LinkedList<Edge>();
        for (int i = 0; i < E; i++) {
            slist[i] = new LinkedList<Edge>();
        }
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < E; j++) {
                vmatrix[i][j] = Graph.INF;
            }
        }
        imatrix = new int[E][W];
        for (int i = 0; i < E; i++) {
            for (int j = 0; j < W; j++) {
                imatrix[i][j] = 0;
            }
        }

    }

    /**
     * Читает граф из источника
     */
    public void readGraph(GraphSource c) {
        int n = GraphReader.getVertixesAmount(c);
        LinkedList<Edge> elist = GraphReader.getEdges(c);
        E = n;
        W = elist.size();
        initGraph(E, W);

        for (int i = 0; i < W; i++) {
            slist[elist.get(i).getV1() - 1].add(elist.get(i));
        }

        for (int i = 0; i < elist.size(); i++) {
            int v1 = elist.get(i).getV1();
            int v2 = elist.get(i).getV2();
            int w = elist.get(i).getWeight();
            vmatrix[v1 - 1][v2 - 1] = w;
        }

        vlist = elist;

        for (int i = 0; i < elist.size(); i++) {
            imatrix[elist.get(i).getV1() - 1][i] = 1;
            imatrix[elist.get(i).getV2() - 1][i] = -1;

        }
    }

    public Edge isEnable(int v1, int v2) {
        return manager.isEnable(v1, v2);
    }

    public void outputGraph(GraphSource c) {
        manager.outputGraph(c);
    }

    public LinkedList<Edge>[] getSlist() {
        return slist;
    }
}
