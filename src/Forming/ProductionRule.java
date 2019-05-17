package Forming;

import java.util.Arrays;

public class ProductionRule {
	
	private String _from;
	private String[] _to;
	
	public ProductionRule(String _from, String... _to) {
		this._from = _from;
		this._to = _to;
	}
	
	
	
	public String getFrom() {
		return _from;
	}

	public String[] getTo() {
		return _to;
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
