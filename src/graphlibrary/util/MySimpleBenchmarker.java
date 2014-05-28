
package graphlibrary.util;

public class MySimpleBenchmarker {
   private static long counter = 0;
   private static String name = "";
   
   
   public static void START(String s){
       counter = System.currentTimeMillis();
       name = s;
   }
   
   public static void MARK(){
       
   }
   
   public static void STOP(){
       System.out.println("TOTAL TIME FOR " + name + " = " + (System.currentTimeMillis() - counter));
       counter = 0;
   }
   
}
