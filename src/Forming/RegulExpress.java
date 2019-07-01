package Forming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import Parsing.Tuples.ThreeTuple;
import Parsing.Tuples.TwoTuple;

public class RegulExpress {

	enum Operators {
		PLUS, DOT, OR, STAR, STRING
	}

	ArrayList<String> _operatorsInArray = new ArrayList<String>(Arrays.asList(new String[] { "+", ".", "|", "*" }));

	Operators _currentOperator;
	Optional<String> _value;

	public Optional<RegulExpress> _left = Optional.ofNullable(null), _right = Optional.ofNullable(null);
	
	public NDFA _createdNFA = NDFA.Empty();

	private RegulExpress() {
	}

	public RegulExpress(String s) {
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
			System.out.println("LEft: "+ s.substring(0, pos));
			System.out.println(this._currentOperator);
			this._left = Optional.ofNullable(new RegulExpress(s.substring(0, pos)));
			this._left.get().setRight(this);
			
			String right = s.substring(pos + 1, s.length());
			if (right.length() != 0) {
				this._right = Optional.ofNullable(new RegulExpress(s.substring(pos + 1, s.length())));
				this._right.get().setLeft(this);
			} 
			

			this._currentOperator = stringToOperator(op.get());
			System.out.println("Created: "+ _currentOperator);
			
			System.out.println("Done: "+ this.getLeft().get()._value.get());

		} else {
			this._value = Optional.ofNullable(s.replace("(",""));
			this._value = Optional.ofNullable(s.replace(")",""));
			this._currentOperator = Operators.STRING;
			//this.toString();
		}

	}

	public Operators getOperator() {
		return this._currentOperator;
	}
	
	public NDFA genNDFA() {
		 Set<String> signs = new LinkedHashSet<>();
		
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		//nodes.add(new DFANode(0, false));
		RegulExpress ex = this;
		
		
		while (ex.hasRight())
			ex = ex.getRight().get();
		
		while (ex.hasLeft()) {
			// Set to most left
			System.out.println(ex.getOperator());
			ex = ex.getLeft().get();
		}

		System.out.println(ex.getOperator());
		
		while (ex != null) {
			//System.out.println(ex.getOperator());
			// Check for OR
			if (!ex.getRight().isEmpty() && ex.getRight().get().isOperator(Operators.OR))
			{
				ArrayList<DFANode> listLeft = DFA.genNodes(ex._value.get(), nodes.size() - 1);
				ArrayList<DFANode> listRight = DFA.genNodes(ex.getRight().get().getRight().get()._value.get(), nodes.size() + listLeft.size() - 1);
				
				TransitionRule[] rules = new TransitionRule[] { listLeft.get(0).get_transitions().get(0), listRight.get(0).get_transitions().get(0) };
				
				if (nodes.size() > 0)
					nodes.get(nodes.size() - 1).addTransitions(rules);
				
				var right = listRight.get(listRight.size() - 1);
				var left = listLeft.get(listLeft.size() - 2);
				
				listLeft.remove(0);
				listRight.remove(0);
				
				left.addTransitions(new TransitionRule(ex._value.get(), right.get_state()));
				
				nodes.addAll(listLeft);
				nodes.addAll(listRight);
			} else {
				switch (ex.getOperator()) {
				
					case STRING: {
						for (char c: ex._value.get().toCharArray()) {
							signs.add(Character.toString(c));
						}
						
						ArrayList<DFANode> list = DFA.genNodes(ex._value.get(), nodes.size());
						System.out.println("Lise size: "+ list.size());
						
						if (nodes.size() != 0) {
							nodes.get(nodes.size() - 1).addTransitions(list.get(0).get_transitions().get(0));
							list.remove(0);
							list = NDFA.incStatesBy(list, -1);
						}
						
						//list = NDFA.incStatesBy(list, nodes.size());
						nodes.addAll(list);
					}
						break;
					case STAR: {
						String lastString = ex.getLeft().get()._value.get();
						nodes.get(nodes.size() - 1).addTransitions(new TransitionRule(NDFA.EmptySign, nodes.size() - 1 - ex.getLeft().get()._value.get().length()));
						nodes.get(nodes.size() - ex.getLeft().get()._value.get().length() - 1).addTransitions(new TransitionRule(NDFA.EmptySign, nodes.get(nodes.size() - lastString.length() - 1).get_state()));
					} break;
					case PLUS: {
						nodes.get(nodes.size() - 1).addTransitions(new TransitionRule(NDFA.EmptySign, nodes.size() - 1 - ex.getLeft().get()._value.get().length()));
						
//						//signs.add(ex.getLeft().get()._value.get());
//						ArrayList<DFANode> list = DFA.genNodes(ex.getLeft().get()._value.get(), nodes.size());
//						list.get(list.size() - 1).addTransitions(new TransitionRule(NDFA.EmptySign, list.get(0).get_state() - ex.getLeft().get()._value.get().length()));
//						nodes.get(nodes.size() - 1).addTransitions(new TransitionRule(NDFA.EmptySign, list.get(0).get_state() - ex.getLeft().get()._value.get().length()));
						
						
						
						//nodes.addAll(list);
					} break;
			
				}
			}	
			
			if (!ex.hasRight())
				break;
			else 
				ex = ex.getRight().get();
			
		}
		
		nodes.get(nodes.size() - 1).set_isEndSymbol(true);
		
		System.out.println("Nodes seiz: "+ nodes.size());
		
		var alfa = new Alfabet(signs.toArray(String[]::new));
		return new NDFA(alfa, 0, nodes.toArray(DFANode[]::new));
		
	}

	public boolean validateString(String s) {
		
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		DFANode _lastNode = new DFANode(0, false);
		
		var ex = Optional.ofNullable(this);
		int stringPos = 0;

		boolean lastWasTrue;
		Operators currentOperator;

		while (ex.get().hasLeft()) {
			// Set to most left
			ex = ex.get().getLeft();
		}
		
		int counter = 0;
		while (counter < s.length()) {

			String pattern = ex.get()._value.get();
			// Check if matches criteria
			String str = "";
			boolean toStringWithoutErrors = true;
			
			try {
				int length = ex.get()._value.get().length();
				while (length > 0) {
					str += s.toCharArray()[counter];
					length--;
					counter++;
				}
			} catch (Exception e) {
				counter += str.length();
				toStringWithoutErrors = false;
			}


			if ((ex.get().isValid(str) || RegulExpress.consistsOfRepeatingStrings(str, pattern, true)) && toStringWithoutErrors) {
				// Raise counter by amount of characters read
				ex = ex.get().getRight();

				if (!ex.isEmpty()) {
					currentOperator = ex.get().getOperator();
					if (currentOperator == Operators.STRING) {
						// Just go next
					}
					else if (currentOperator == Operators.OR) {
						if (ex.get().isValid(str))
							ex = ex.get().getRight();
						else
							return false;
						// First part is true anyway, ignore until next operator
//						while (!ex.isEmpty() && !ex.get().isOperator()) {
//							ex = ex.get().getRight();
//						}
					} 
					else if (currentOperator == Operators.STAR) {
						// Repeat
						if (!RegulExpress.consistsOfRepeatingStrings(str, pattern, true)) {
							return false;
						} else {

							counter += str.length();
							ex = ex.get().getRight();
						}
						
					} else if (currentOperator == Operators.PLUS) {

						if (!RegulExpress.consistsOfRepeatingStrings(str, pattern, false)) {
							return false;
						} else {
							System.out.println("plus");

							counter += str.length();
							ex = ex.get().getRight();
						}
						

					} else if (currentOperator == Operators.DOT) {

					}
					
				} 
				} else {
					if (ex.get().hasRight()) {
						ex = ex.get().getRight();
						
						if (ex.get().getOperator() == Operators.OR) {
							if (ex.get().isValid(str))
								ex = ex.get().getRight();
							else
								return false;
						} 
					} else {
						return false;
					}
				}
				System.out.println(ex.get().getRight().isEmpty());
				System.out.println("Size: "+ s.length() + " and "+ counter);
				return ex.get().getRight().isEmpty() && counter == s.length();
			} 
			
		return true;
	}

	public static boolean consistsOfRepeatingStrings(String s, String rep, boolean canBeEmpty) {
		if (canBeEmpty && s.length() == 0)
			return true; // True if string is empty

		if (!(s.length() % rep.length() == 0))
			return false; // Size isn't dividable by pattern, so can't be true.

		for (int counter = 0; counter < s.length(); counter += rep.length()) {
			if (!(s.substring(counter, rep.length() + counter).equals(rep)))
				return false;
		}

		return true;
	}

	public boolean isOperator() {
		return this._currentOperator != Operators.STRING;
	}

	public boolean isOperator(Operators op) {

		//System.out.println("Is op: " + (this._currentOperator != op));
		return this._currentOperator == op;
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
		case PLUS:
			return "+";
		case OR:
			return "|";
		case STAR:
			return "*";
		case DOT:
			return ".";
		default:
			return "";
		}

	}

	public Operators stringToOperator(String a) {
		switch (a) {
		case "+":
			return Operators.PLUS;
		case "|":
			return Operators.OR;
		case "*":
			return Operators.STAR;
		case ".":
			return Operators.DOT;
		default:
			return Operators.STRING;
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
		
		switch (this._currentOperator) {
			case STRING:
				return this._value.get().equals(s);
			case OR:
				return _left.get().isValid(s) || _right.get().isValid(s);
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

	@Override
	public String toString() {
		String tempString = this.operatorToString(this._currentOperator);

		if (this.isOperator(Operators.STRING))
			tempString += " " + this._value;

		return tempString;

	}

}
