package Forming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class ProductionRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2131980293807351290L;

	private String _from;
	
	private ArrayList<TransitionRule<String>> _rules = new ArrayList<>();
	
	// Can this be an end state?
	private boolean _isEnd;
	
	@SafeVarargs
	public ProductionRule(String _from, boolean isEndState, TransitionRule<String>... tRules) {
		this._from = _from;
		this._isEnd = isEndState;
		this._rules = new ArrayList<TransitionRule<String>>(Arrays.asList(tRules));
	}
	
	public String getFrom() {
		return _from;
	}

	public ArrayList<TransitionRule<String>> getTransitions() {
		return this._rules;
	}


	public boolean isEndState() {
		return this._isEnd;
	}

	public void addTransitionRule(TransitionRule<String> transitionRule) {
		this._rules.add(transitionRule);
		
	}

	public String toProductionRuleString(String[] endStates) {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		for (TransitionRule<String> rule: _rules) {
			
			var list = new ArrayList<String>(Arrays.asList(endStates));
			str.append(rule.toProductionRuleString(this.getFrom(), list)+"\n");
		}
		
		return str.toString();
	}

	@Override
	public boolean equals(Object obj) {
		ProductionRule other = (ProductionRule)obj;
		
		return this._from.equals(other.getFrom());
	}

	public void setEndState(boolean b) {
		this._isEnd = b;
		
	}
	
	
	
}
