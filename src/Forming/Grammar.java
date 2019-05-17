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
		
		System.out.println("Is valid ???? "+ productionRulesAreInTuneWithNonTerminalsymbols());
	}
	
	public boolean productionRulesAreInTuneWithNonTerminalsymbols() {
		for (char symbol: _nonTerminalSymbols.getAllSigns()) {
			boolean contains = true;
			
			for (ProductionRule pr: _rules) {
				ArrayList<String> set = new ArrayList<String>();
				set.add(pr.getFrom());
				
				for (String str: pr.getTo()) {
					set.add(str);
				}
				
				if (set.contains(Character.toString(symbol))) break;
				else contains = false;
			}
			
			return contains;
		}
		
		return true;
	}
	
	
}
