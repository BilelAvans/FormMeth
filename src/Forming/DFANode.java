package Forming;

import java.util.AbstractMap.SimpleEntry;
import java.util.Dictionary;
import java.util.TreeMap;


public class DFANode {
	
	private String _state;
	
	private TreeMap<String, String> _transitions = new TreeMap<String, String>();
	
	private boolean _isEndSymbol;
	
	
	public DFANode(String _stateName, boolean isEndSymbol, SimpleEntry<String, String>... transitions) {
		
		this._state = _stateName;
		this._isEndSymbol = isEndSymbol;
		
		for(SimpleEntry<String, String> entry: transitions) {
			_transitions.put(entry.getKey(), entry.getValue());
		}
	}


	public String get_state() {
		return _state;
	}


	public void set_state(String _state) {
		this._state = _state;
	}


	public TreeMap<String, String> get_transitions() {
		return _transitions;
	}


	public void set_transitions(TreeMap<String, String> _transitions) {
		this._transitions = _transitions;
	}


	public boolean is_isEndSymbol() {
		return _isEndSymbol;
	}


	public void set_isEndSymbol(boolean _isEndSymbol) {
		this._isEndSymbol = _isEndSymbol;
	}
	
	
	
	
}
