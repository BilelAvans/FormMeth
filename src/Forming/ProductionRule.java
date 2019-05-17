package Forming;

import java.util.Arrays;

public class ProductionRule {
	
	private String _from;
	
	private TransitionRule[] _rules;
	
	public ProductionRule(String _from, TransitionRule... tRules) {
		this._from = _from;
		this._rules = tRules;
	}
	
	
	
	public String getFrom() {
		return _from;
	}

	public TransitionRule[] getTransitions() {
		return this._rules;
	}

	@Override
	public String toString() {
		
		
		if (_to.length == 0)
			return "";
		if (_to.length == 1)
			return _from + " -> "+ _to[0];
		else {
			String buildString = _from +" -> { ";
			
			for (String str: _to) {
				_from += str + ", ";
			}
			
			return buildString;
		}
		
	}
	
	
	
	
}
