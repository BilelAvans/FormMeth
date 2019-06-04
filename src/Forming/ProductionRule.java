package Forming;

import java.util.ArrayList;
import java.util.Arrays;

public class ProductionRule<T> {
	
	private T _from;
	
	private ArrayList<TransitionRule<T>> _rules = new ArrayList<>();
	
	// Can this be an end state?
	private boolean _isEnd;
	
	@SafeVarargs
	public ProductionRule(T _from, boolean isEndState, TransitionRule<T>... tRules) {
		this._from = _from;
		this._isEnd = isEndState;
		this._rules = new ArrayList<TransitionRule<T>>(Arrays.asList(tRules));
	}
	
	
	
	public T getFrom() {
		return _from;
	}

	public ArrayList<TransitionRule<T>> getTransitions() {
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
