package Forming;

public class TransitionRule {

	// Sign from our alfabet.
	private String _sign;
	// Where will it lead
	private String _goTo;
	
	public TransitionRule(String sign, String goTo) {
		this._sign = sign;
		this._goTo = goTo;
	}
	
	
	
	public String getSign() {
		return _sign;
	}



	public String getGoTo() {
		return _goTo;
	}


	@Override
	public String toString() {
		return "";
//		return _isEnd ? _sign + " -> " + _sign + _goTo + " | "+ _sign : 
//						_sign + " -> " + _sign + _goTo;
	}
	
}
