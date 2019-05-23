package Forming;

import java.util.ArrayList;

import Parsing.GraphwizExec;

public class Former {
	public static void main(String[] args) {
		generateNFA();
	}
	
	private static void testGrammar() {
		ProductionRule pr = new ProductionRule("S", false, new TransitionRule("a", "A"), new TransitionRule("b", "A"));
		
		ProductionRule pr2 = new ProductionRule("A", true, new TransitionRule("b", "B"));
		
		ProductionRule pr3 = new ProductionRule("B", true, new TransitionRule("b", "C"));
		
		ProductionRule pr4 = new ProductionRule("C", true, new TransitionRule("a", "D"));
		
		ProductionRule pr5 = new ProductionRule("D", true, new TransitionRule[] { });
		
		Grammar grammar = new Grammar(new Alfabet("a","b"), "S", pr, pr2, pr3, pr4, pr5);
	
		//System.out.println("Valid: "+ grammar.isValidString("a"));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("abba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
	}
	
	private static void generateNFA() {
		
		
		GraphwizExec graph = new GraphwizExec();
		boolean thisWorks = graph.openDotExeCodeString(new DFA());
		
	}
}
