package Forming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;

import Parsing.Tuples.ThreeTuple;
import Parsing.Tuples.TwoTuple;

public class RegulExpress {

	enum Operators { PLUS, DOT, OR, STAR, STRING }
	ArrayList<String> _operatorsInArray = new ArrayList<String>(Arrays.asList(new String[]{ "+",".","|","*" }));
	
	Operators _currentOperator;
    Optional<String> _value;
    
	
	public Optional<RegulExpress> _left = Optional.ofNullable(null), _right = Optional.ofNullable(null);
	
	private RegulExpress() { }
	
	public RegulExpress(String s){
		// Split up in groups
		this.setInstances(s);
		
	}
	
	public void setInstances(String s) {
		// Find operator
		Optional<String> op = _operatorsInArray.stream().filter((String o) -> s.contains(o)).map(m -> m).findFirst();
		
		if (!op.isEmpty()) {
			// Find occurence
			int pos = s.indexOf(op.get());
			
			// Found operator, split this stuff.
			this._left = Optional.ofNullable(new RegulExpress(s.substring(0, pos)));
			this._right = Optional.ofNullable(new RegulExpress(s.substring(pos + 1, s.length())));
			//System.out.println(op.get());
			this._currentOperator = stringToOperator(op.get());
		}	else {
			System.out.println("Got val: "+ s);
			this._value = Optional.ofNullable(s);
			this._currentOperator = Operators.STRING;
		}
		
		
	}
	
	public Operators getOperator() {
		return this._currentOperator;
	}
	
	public boolean validateString(String s) {
		var ex = Optional.ofNullable(this);
		int stringPos = 0;
		
		boolean lastWasTrue;
		Operators currentOperator;
		
		while (ex.get().hasLeft()) {
			// Set to most left
			ex = ex.get().getLeft();
		}
		int counter = 0; 
		for (char c = s.toCharArray()[counter]; counter < s.length(); c = s.toCharArray()[counter], counter++) {

			// Check if matches criteria
			String str = "";
			int length = ex.get()._value.get().length();
			while (length > 0) {
				str += s.toCharArray()[counter];
				length--;
				counter++;
			}			
			
			if (ex.get().isValid(str)) {
				// Raise counter by amount of characters read
				lastWasTrue = true;
				
				ex = ex.get().getRight();
				
				if (!ex.isEmpty()) {
					currentOperator = ex.get().getOperator();
					if (currentOperator == Operators.OR) {
						// First part is true anyway, ignore until next operator
						while (!ex.isEmpty() && !ex.get().isOperator()) {
							ex = ex.get().getRight();
						}
					}
					else if (currentOperator == Operators.STAR) {
						// Repeat
					}
					else if (currentOperator == Operators.PLUS) {
						
					}
					else if (currentOperator == Operators.DOT) {
						
					}
					//else if (ex.g)
				}
				else // Right is empty, nothing more todo
					return true;
			} else {
				// Find OR for a chance of success
				while (!ex.isEmpty() && !ex.get().isOperator(Operators.OR)){
					ex = ex.get().getRight();
				}
				
				// Found or?
				
				if (ex.isEmpty())
					return false;

				
				if (ex.get().isValid(str)) {
					if (ex.get().hasRight())
						ex = ex.get().getRight();
					else 
						return true;
					
				} else {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public boolean isOperator() {
		return this._currentOperator != Operators.STRING;
	}
	
	public boolean isOperator(Operators op) {
		return this._currentOperator != op;
	}
	
	public void setLeft(RegulExpress re) {
		this._left = Optional.ofNullable(re);
	}
	
	public void setRight(RegulExpress re) {
		this._right = Optional.ofNullable(re);
	}
	
	public Optional<RegulExpress> getRight() {
		return this._right;
	}
	
	public Optional<RegulExpress> getLeft() {
		return this._left;
	}
	
	public boolean hasLeft() {
		return !this._left.isEmpty();
	}
	
	public boolean hasRight() {
		return !this._right.isEmpty();
	}
	
	public String operatorToString(Operators a) {
		switch (a) {
			case PLUS: return "+"; 
			case OR: return "|"; 
			case STAR: return "*"; 
			case DOT: return "."; 
			default: return "";
		}
		
	}
	
	public Operators stringToOperator(String a) {
		switch (a) {
			case "+": return Operators.PLUS;
			case "|": return Operators.OR;
			case "*": return Operators.STAR;
			case ".": return Operators.DOT;
			default: return Operators.STRING;
		}
	}
	
	public void setOperator(Operators o) {
		this._currentOperator = o;
	}
	
	public RegulExpress OR(RegulExpress e) {
		RegulExpress tempExp = new RegulExpress();
		tempExp.setOperator(Operators.OR);
		tempExp.setLeft(this);
		tempExp.setRight(e);
		
		return tempExp;		
	}
	
	public boolean isValid(String s) {
		
		switch (this._currentOperator)
		{
		
			case STRING: System.out.println("True:?"+ this._value.get() + " and "+ s); return this._value.get().equals(s);
			case OR: return _left.get().isValid(s) || _left.get().isValid(s);
			case DOT:
				break;
			case PLUS:
				break;
			case STAR:
				break;
			default:
				break;
		}
		
		return false;
	}
	
	public NDFA toNDFA() {
		return null;
	}
	
	
}
