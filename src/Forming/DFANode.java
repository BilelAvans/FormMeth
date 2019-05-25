package Forming;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.TreeMap;


public class DFANode {
	
	private String _state;
	
	private ArrayList<TransitionRule> _transitions = new ArrayList<>();
	
	private boolean _isEndSymbol;
	
	public DFANode(String _stateName, boolean isEndSymbol, TransitionRule... transitions) {
		
		this._state = _stateName;
		this._isEndSymbol = isEndSymbol;
		
		addTransitions(transitions);
	}


	public String get_state() {
		return _state;
	}


	public void set_state(String _state) {
		this._state = _state;
	}


	public ArrayList<TransitionRule> get_transitions() {
		return _transitions;
	}
	
	public void addTransitions(TransitionRule... transitions) {
		for(TransitionRule transition: transitions) {
			_transitions.add(transition);
		}
	}


	public void set_transitions(ArrayList<TransitionRule> _transitions) {
		this._transitions = _transitions;
	}


	public boolean get_isEndSymbol() {
		return _isEndSymbol;
	}


	public void set_isEndSymbol(boolean isEndSymbol) {
		this._isEndSymbol = isEndSymbol;
	}
	
	@Override
	public boolean equals(Object ob) {
		if (ob instanceof DFANode)
			return ((DFANode)ob)._state.equals(this._state);
		
		return false;
	}
	
	
	
	
}
