package Parsing;

public class ArrayUtils {
    public static <T> T[] combine(T[] a, T[] b){
    	@SuppressWarnings("unchecked")
        T[] arr = (T[])new Object[a.length + b.length];
        
        for (int x = 0; x < a.length; x++) {
        	arr[x] = a[x];
        }
        
        for (int y = 0; y < b.length; y++) {
        	arr[y] = b[y];
        }
        
        return arr;
    }
}
