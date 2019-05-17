package Forming;

public class Former {
	public static void main(String[] args) {
		ProductionRule rule = new ProductionRule("S", "b", new String[]{ "A" });
		
		Grammar grammar = new Grammar(new Alfabet("S","A"), new Alfabet("A","B"), new ProductionRule[] { rule }, "S");
	
		
		//System.out.println();
	}
}
