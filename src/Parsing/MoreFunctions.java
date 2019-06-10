package Parsing;

public class MoreFunctions {

	@FunctionalInterface
	public interface TwoParamFunction<A, B, R> {
	    public R apply(A a, B b);
	}
	

	@FunctionalInterface
	public interface ThreeParamFunction<A, B, C, R> {
	    public R apply(A a, B b, C c);
	}
	
	@FunctionalInterface
	public interface FourParamFunction<A, B, C, D, R> {
	    public R apply(A a, B b, C c, D d);
	}
	
	
}
