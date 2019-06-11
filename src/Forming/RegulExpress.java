package Forming;

import java.util.Optional;

public class RegulExpress {

	
	public static enum Operators { OR, PLUS, STAR, DOT, String };
	
	private RegulExpress _right;
	private RegulExpress _left;
	
	private Operators _op;
	private String _expressionPart;
	
	public RegulExpress() {
		this._op = Operators.String;
	}
	
	
	public RegulExpress(String s) {
		this._expressionPart = s;
		this._op = Operators.String;
	}
	
	public static RegulExpress Generate(String s) {
		int counter = 0;
		
		RegulExpress last = new RegulExpress(s);
		
		for (char[] strArray = s.toCharArray(); counter < s.length(); counter++) {
			
		}
		
		
	}
	
	public RegulExpress performOperation(Operators o, RegulExpress s) {
		switch (o) {
			case OR: return OR(s);
			case STAR: return STAR(s);
			case PLUS: return PLUS(s);
			case DOT: return DOT(s);
			default: return null;
		}
	}
		
	private RegulExpress OR(RegulExpress regul) {
		RegulExpress expr = new RegulExpress();
		expr.set_left(this);
		this.set_op(Operators.OR);
		this.set_right(regul);
		
		return expr;
	}
	
	private RegulExpress PLUS(RegulExpress regul) {
		RegulExpress expr = new RegulExpress();
		expr.set_left(this);
		this.set_op(Operators.PLUS);
		
		return expr;
	}
	
	private RegulExpress STAR(RegulExpress regul) {
		RegulExpress expr = new RegulExpress();
		expr.set_left(this);
		this.set_op(Operators.STAR);
		
		return expr;
	}
	
	private RegulExpress DOT(RegulExpress regul) {
		RegulExpress expr = new RegulExpress();
		expr.set_left(this);
		expr.set_right(regul);
		this.set_op(Operators.DOT);
		
		return expr;
	}
	
	public RegulExpress get_right() {
		return _right;
	}


	public void set_right(RegulExpress _right) {
		this._right = _right;
	}


	public RegulExpress get_left() {
		return _left;
	}


	public void set_left(RegulExpress _left) {
		this._left = _left;
	}


	public Operators get_op() {
		return _op;
	}


	public void set_op(Operators _op) {
		this._op = _op;
	}


	private static Operators getOperator(String s) {
		if (s.length() != 1)
			return null;
		
		
		char operator = s.toCharArray()[0];
		
		switch (operator) {
			case '|': return Operators.OR;
			case '*': return Operators.STAR;
			case '+': return Operators.PLUS;
			case '.': return Operators.DOT;
			default: return null;
		}
	}
}
