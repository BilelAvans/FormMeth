package Forming;

public class TransitionRule<T> {

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
	
}
