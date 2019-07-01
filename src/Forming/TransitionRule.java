package Forming;

import java.io.Serializable;
import java.util.ArrayList;

public class TransitionRule<T> implements Serializable {

	// Sign from our alfabet.
	private String _sign;
	// Where will it lead
	private T _goTo;
	
	public TransitionRule(String sign, T goTo) {
		this._sign = sign;
		this._goTo = goTo;
	}
	
	public TransitionRule(Character sign, T goTo) {
		this._sign = Character.toString(sign);
		this._goTo = goTo;
	}
	
	public String getSign() {
		return _sign;
	}



	public T getGoTo() {
		return _goTo;
	}
	
	public boolean hasSign(String sign) {
		return _sign == sign;
	}

	public boolean hasGoTo(T goTo) {
		return _goTo == goTo;
	}

	public String toProductionRuleString(String from, ArrayList<T> endSigns) {
		return endSigns.contains(_goTo) ? from + " -> " + _sign + _goTo + " | "+ _sign + " " : 
											from + " -> " + _sign + _goTo + " ";
	}
	
	@Override
	public boolean equals(Object ob) {
		try {
			TransitionRule otherRule = (TransitionRule) ob;
			
			return otherRule.getGoTo().equals(this.getGoTo()) && otherRule.getSign().equals(this.getSign());
		} catch (ClassCastException e) {
			
		}
		
		return false;
		
	}


	@SuppressWarnings("unchecked")
	public void setGoTo(T i) {
		this._goTo = (T)i;
		
		System.out.println("New go to: "+ i +" and "+ this._goTo);
		
	}
	
}
