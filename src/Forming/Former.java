package Forming;

import Parsing.GraphwizExec;

public class Former {
	public static void main(String[] args) {
		//generateNFA();
		generateNFAFromString("hello");
		//DFA dfa = DFA.fromGraphVizStringToDFA(GraphwizExec.loadTextFile("DFA.gv"));
		//GraphwizExec.openDotExeCodeString(dfa);
	}
	
	@SuppressWarnings({"unchecked"})
	private static void testGrammar() {
		ProductionRule<String> pr = new ProductionRule<String>("S", false, new TransitionRule<String>("a", "A"), new TransitionRule<String>("b", "A"));
		
		ProductionRule<String> pr2 = new ProductionRule<String>("A", true, new TransitionRule<String>("b", "B"));
		
		ProductionRule<String> pr3 = new ProductionRule<String>("B", true, new TransitionRule<String>("b", "C"));
		
		ProductionRule<String> pr4 = new ProductionRule<String>("C", true, new TransitionRule<String>("a", "D"));
		
		ProductionRule<String> pr5 = new ProductionRule<String>("D", true, ((TransitionRule<String>[])new TransitionRule[0]));
		
		Grammar grammar = new Grammar(new Alfabet("a","b"), "S", pr, pr2, pr3, pr4, pr5);
	
		//System.out.println("Valid: "+ grammar.isValidString("a"));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("abba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
	}
	
	private static void generateNFAFromString(String matchingString) {
		DFA dfa = DFA.GenerateDFA(matchingString);
		
		System.out.println("Valid string: "+ dfa.isValidString("ababba"));
		
		boolean thisWorks = GraphwizExec.openDotExeCodeString(dfa);
		
	}
}
