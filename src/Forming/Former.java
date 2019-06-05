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
		ProductionRule pr = new ProductionRule("S", false, new TransitionRule<String>("a", "A"), new TransitionRule<String>("b", "A"));
		
		ProductionRule pr2 = new ProductionRule("A", true, new TransitionRule<String>("b", "B"));
		
		ProductionRule pr3 = new ProductionRule("B", true, new TransitionRule<String>("b", "C"));
		
		ProductionRule pr4 = new ProductionRule("C", true, new TransitionRule<String>("a", "D"));
		
		ProductionRule pr5 = new ProductionRule("D", true, ((TransitionRule<String>[])new TransitionRule[0]));
		
		Grammar grammar = new Grammar(new Alfabet("a","b"), "S", pr, pr2, pr3, pr4, pr5);
	
		//System.out.println("Valid: "+ grammar.isValidString("a"));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("abba", true));
		
		System.out.println("Valid: "+ grammar.isValidString("ababba", true));
	}
	
	private static void generateNFAFromString(String matchingString) {
		//DFA dfa = DFA.GenerateDFA(matchingString);
		DFA dfa2 = DFA.GenerateDFA("abc");
		Grammar gram = dfa2.toGrammar();
		System.out.println(gram.toGrammarString());
		//DFA dfaJoined = (DFA)dfa.addDFA(dfa2);
		
		//boolean thisWorks = GraphwizExec.openDotExeCodeString(dfaJoined);
		
	}
}
