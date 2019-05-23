package Forming;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductionRule {
	
	private String _from;
	
	private ArrayList<TransitionRule> _rules = new ArrayList<>();
	
	// Can this be an end state?
	private boolean _isEnd;
	
	public ProductionRule(String _from, boolean isEndState, TransitionRule... tRules) {
		this._from = _from;
		this._isEnd = isEndState;
		this._rules = new ArrayList<TransitionRule>(Arrays.asList(tRules));
	}
	
	
	
	public String getFrom() {
		return _from;
	}

	public ArrayList<TransitionRule> getTransitions() {
		return this._rules;
	}

	@Override
	public String toString() {
		
		return "";
		
	}



	public boolean isEndState() {
		return this._isEnd;
	}
	
	
	
	
}
