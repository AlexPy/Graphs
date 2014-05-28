package graphlibrary.util;

/**
 * Класс реализует структуру непересекающихся множеств для работы с опр. алгоритмами
 */
public class DisjointSet {

  public static int[] createSet(int size) {
    int[] p = new int[size];
    for (int i = 0; i < size; i++)
      p[i] = i;
    return p;
  }

  public static int root(int[] p, int x) {
    return x == p[x] ? x : (p[x] = root(p, p[x]));
  }

  public static void unite(int[] p, int a, int b) {
    a = root(p, a);
    b = root(p, b);
    if (a != b)
      p[a] = b;
  }
  
  public static void main(String[] args) {
    int[] p = createSet(10);
    System.out.println(false == (root(p, 0) == root(p, 9)));
    unite(p, 0, 9);
    System.out.println(true == (root(p, 0) == root(p, 9)));
  }
}