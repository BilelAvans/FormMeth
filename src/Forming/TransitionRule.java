package Forming;

import java.io.Serializable;

public class TransitionRule<T> implements Serializable {

	// Sign from our alfabet.
	private String _sign;
	// Where will it lead
	private T _goTo;
	
	public TransitionRule(String sign, T goTo) {
		this._sign = sign;
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

	@Override
	public String toString() {
		return "";
//		return _isEnd ? _sign + " -> " + _sign + _goTo + " | "+ _sign : 
//						_sign + " -> " + _sign + _goTo;
	}



	@SuppressWarnings("unchecked")
	public void setGoTo(int i) {
		if (this._goTo instanceof Integer)
			this._goTo = (T) (Object)((int)this._goTo + i);
		else if (this._goTo instanceof String)
			this._goTo = (T) ((Object)((String)this._goTo) + String.valueOf(i));
		
	}
	
}
