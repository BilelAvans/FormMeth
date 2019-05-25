package Forming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class Grammar {

	private Alfabet _nonTerminalSymbols;
	private Alfabet _alfabet;
	private ArrayList<ProductionRule> _rules = new ArrayList<ProductionRule>();
	private String	_startSymbol;
	
	
	public Grammar(Alfabet _alfabet, String _startSymbol, ProductionRule... _rules) {
		
		this._alfabet = _alfabet;
		this._rules = new ArrayList<ProductionRule>(Arrays.asList(_rules));
		this._nonTerminalSymbols = new Alfabet((String[])this._rules.stream().map(ProductionRule::getFrom).toArray(String[]::new));
		this._startSymbol = _startSymbol;
	}
	
	public boolean isValidString(String str, boolean mustEnd) {
		for(char c: str.toCharArray()) {
			// Definitely no if it's not even in our alfabet.
			if (!_alfabet.hasSign(Character.toString(c))) return false;
			
			// Loop our string until we reach the end
			// If we're out of chars and and an end state then it's a winner.
			int[] counter = { 0 };
				
			
			// Get Start Rule
			Optional<ProductionRule> start = _rules.stream().filter((ProductionRule pr) -> pr.getFrom().equals(this._startSymbol)).findAny();
						
			while (!start.isEmpty()) {
				// Our new start will be set with the end terminal symbol on the transition
				if (str.length() == counter[0] && start.get().isEndState()){
					// Check if we reached an end state
					return true;
				}		
				
				// Find a path through the maze
				Optional<TransitionRule> rule = start.get().getTransitions().stream().filter((TransitionRule pr) -> pr.getSign().equals(Character.toString(str.charAt(counter[0])))).findAny();
				
				if (!rule.isEmpty()) {
					// Set next char position
					counter[0]++;

					start = _rules.stream().filter((ProductionRule pr) -> pr.getFrom().equals(rule.get().getGoTo())).findAny();			
				} else {
					int highestMatch = 0;
					String[] newString = { "" };
					
					for (int count = 0; count < str.length(); count++) {
						String anString = "";
						
						for (int anotherCounter = 0; anotherCounter < count+1; anotherCounter++) {
							anString = str.charAt(count - anotherCounter) + anString;
						}
						
						try {
							if (isValidString(anString, true)){
								highestMatch = count + 1;
								newString[0] = anString;
							}
						} catch (StackOverflowError e) {
							return false;
						}
						
					}
					// Can't go back
					if (highestMatch < 1) {
						start = _rules.stream().filter((ProductionRule pr) -> pr.getFrom().equals(this._startSymbol)).findAny();	
					} else {
						start = _rules.stream().filter((ProductionRule pr) -> pr.getFrom().equals(this._startSymbol)).findAny();	
						ArrayList<Optional<TransitionRule>> rule2 = new ArrayList<>(1);
						rule2.add(null);

						for (int[] count = { 0 }; count[0] < highestMatch; count[0]++) {
							
							rule2.set(0, start.get().getTransitions().stream().filter((TransitionRule pr) -> pr.getSign().equals(Character.toString(newString[0].charAt(count[0])))).findAny());
							
							if (!(rule2.get(0) == null) && !rule2.get(0).isEmpty()) {
								start = _rules.stream().filter((ProductionRule pr) -> pr.getFrom().equals(rule2.get(0).get().getGoTo())).findAny();
							}
						}
					}
				}
			} 
				
		}
		
		return false;
	}
	
	public boolean productionRulesAreInTuneWithNonTerminalsymbols() {
		boolean canStart = false;
		
//		for (String symbol: _nonTerminalSymbols.getAllSigns()) {
//			
//			for (ProductionRule pr: _rules) {
//				// Check for start
//				canStart = canStart | pr.getFrom().equals(this._startSymbol);
//				
//				ArrayList<String> set = new ArrayList<String>();
//				set.add(pr.getFrom());
//				
//				for (String str: pr.getTo()) {
//					set.add(str);
//				}
//				
//				if (set.contains(symbol)) break;
//				else return false;
//			}
//			
//		}
		
		return canStart;
	}

	public Alfabet get_nonTerminalSymbols() {
		return _nonTerminalSymbols;
	}

	public void set_nonTerminalSymbols(Alfabet _nonTerminalSymbols) {
		this._nonTerminalSymbols = _nonTerminalSymbols;
	}

	public Alfabet get_alfabet() {
		return _alfabet;
	}

	public void set_alfabet(Alfabet _alfabet) {
		this._alfabet = _alfabet;
	}

	public ArrayList<ProductionRule> get_rules() {
		return _rules;
	}

	public void set_rules(ArrayList<ProductionRule> _rules) {
		this._rules = _rules;
	}

	public String get_startSymbol() {
		return _startSymbol;
	}

	public void set_startSymbol(String _startSymbol) {
		this._startSymbol = _startSymbol;
	}
	
	
	
	
	
}
