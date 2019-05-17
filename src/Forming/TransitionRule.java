package Forming;

public class TransitionRule {

	public String _sign;
	
	public String _goTo;
	
	boolean _isEnd;
	
	public TransitionRule(String sign, String goTo, boolean isEnd) {
		this._sign = sign;
		this._goTo = goTo;
		this._isEnd = isEnd;
	}
	
}
