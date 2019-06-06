package Forming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;


public class DFANode implements Comparable<DFANode>, Serializable {
	
	private int _state;
	
	private ArrayList<TransitionRule<Integer>> _transitions = new ArrayList<>();
	
	private boolean _isEndSymbol;
	
	public DFANode(int state, ArrayList<TransitionRule<Integer>> tr, boolean _isEndSymbol) {
		this._state = state;
		
	}
	
	@SafeVarargs
	public DFANode(int _stateName, boolean isEndSymbol, TransitionRule<Integer>... transitions) {
		
		this._state = _stateName;
		this._isEndSymbol = isEndSymbol;
		
		addTransitions(transitions);
	}
	
	


	public DFANode(int _state) {
		super();
		this._state = _state;
	}

	public int get_state() { 
		return _state;
	}


	public void set_state(int _state) {
		this._state = _state;
	}
	
	public Optional<TransitionRule<Integer>> getTransitionRuleBySymbol(String symbol) {
		return this.get_transitions().stream().filter(tr -> tr.getSign().equals(symbol)).findAny();
	}


	public ArrayList<TransitionRule<Integer>> get_transitions() {
		return _transitions;
	}
	
	@SuppressWarnings("unchecked")
	public void addTransitions(TransitionRule<Integer>... transitions) {
		for(TransitionRule<Integer> transition: transitions) {
			_transitions.add(transition);
		}
	}


	public void set_transitions(ArrayList<TransitionRule<Integer>> _transitions) {
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
		if (ob instanceof DFANode) {
			return ((DFANode)ob)._state == this._state;
		}
			
		return false;
	}
	
	


	@Override
	public int compareTo(DFANode node) {		
		
		return Integer.valueOf((this._state)).compareTo(Integer.valueOf(node.get_state()));
		
	}
	
	
	
	
	
	
}
