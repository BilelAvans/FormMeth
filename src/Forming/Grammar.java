package Forming;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

	private Alfabet _nonTerminalSymbols;
	private Alfabet _alfabet;
	private ProductionRule[] _rules;
	private String	_startSymbol;
	
	
	public Grammar(Alfabet _nonTerminalSymbols, Alfabet _alfabet, ProductionRule[] _rules, String _startSymbol) {
		this._nonTerminalSymbols = _nonTerminalSymbols;
		this._alfabet = _alfabet;
		this._rules = _rules;
		this._startSymbol = _startSymbol;
		
		System.out.println("Is valid grammar: "+ productionRulesAreInTuneWithNonTerminalsymbols());
	}
	
	public boolean productionRulesAreInTuneWithNonTerminalsymbols() {
		boolean canStart = false;
		
		for (String symbol: _nonTerminalSymbols.getAllSigns()) {
			
			for (ProductionRule pr: _rules) {
				// Check for start
				canStart = canStart | pr.getFrom().equals(this._startSymbol);
				
				ArrayList<String> set = new ArrayList<String>();
				set.add(pr.getFrom());
				
				for (String str: pr.getTo()) {
					set.add(str);
				}
				
				if (set.contains(symbol)) break;
				else return false;
			}
			
		}
		
		return true && canStart;
	}
	
	
}
