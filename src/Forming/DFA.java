package Forming;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import Parsing.IMethodAsString;

public class DFA implements IMethodAsString {
	
	private Alfabet _alfabet;
	
	private	List<DFANode> _nodes = new ArrayList<DFANode>();

	private final String _startState;
	
	private String matchString = "";
	
	// Create from Grammar
	private DFA(Grammar grammar) {
		
		this._alfabet = grammar.get_alfabet();
		this._startState = grammar.get_startSymbol();

		for (ProductionRule tr: grammar.get_rules()) {
			
			_nodes.add(new DFANode(tr.getFrom(), tr.isEndState(), (TransitionRule[])tr.getTransitions().toArray()));

		}
	}
	
	// Test case
	public DFA(Alfabet alfa) {
		_alfabet = alfa;
		
		_startState = "S";
		
		_nodes.add(new DFANode("S", false, 	new TransitionRule("a", "A"),
											new TransitionRule("b", "A")));
		_nodes.add(new DFANode("A", true, 	new TransitionRule("a", "A"),
											new TransitionRule("b", "A")));
	}
	
	public DFA(Alfabet alfa, final String startState, DFANode... nodes) {
		_alfabet = alfa;
		_nodes = Arrays.asList(nodes);
		_startState = startState;
		
		this.matchString = "abcdef";
		
		addMissingAlfabetCharacters();
		
		
	}
	
	public boolean startAvailable() {
		if (!_startState.isEmpty()) {
			Optional<DFANode> node = _nodes.stream().filter((DFANode n) -> n.get_state().equals(_startState)).findAny();
			
			return (!node.isEmpty());
		}
		
		return false;
	}
	
	private void addMissingAlfabetCharacters() {
		
		int counter = 0;
		
		String currentPath = "";
		
		this._nodes = this._nodes.stream().sorted((n1, n2) -> n1.compareTo(n2)).collect(Collectors.toList());
		
		for (DFANode node: this._nodes) {
			for (String c: this._alfabet.getAllSigns()) {
				System.out.println(c);
				if (counter == 0) {
					Optional<TransitionRule> rule = node.get_transitions().stream().filter(tr -> tr.getSign().equals(c)).findAny();
					if (rule.isEmpty()) {
						node.addTransitions(new TransitionRule(c, "S"));
					} else {
						currentPath += rule.get().getSign();
					}
				} else {
					Optional<TransitionRule> rule = node.get_transitions().stream().filter(tr -> tr.getSign().equals(c)).findAny();
					if (rule.isEmpty()) {
						// Where must we go then?
						String firstString = currentPath + c;
						TransitionRule newRule = null;

						for (int count = 0; count < counter; count++) {
							// Match the strings
							// Look for the biggest amount of chars in which the back of our new string compares to the front of the other. (Does this even make sense?)
							String secondString = this.matchString.substring(0, count+1);
							System.out.println("length - counter = "+ (firstString.length() - counter));
							System.out.println(firstString.length() + 1);
							String thirdString  = firstString.substring(firstString.length() - counter, firstString.length());
							if (thirdString.equals(secondString)) {
								newRule = new TransitionRule(c, Character.toString((char)'A'+counter-1));
							}
							
						}
						
						if (newRule == null)
							newRule = new TransitionRule(c, "S");
						
						node.addTransitions(newRule);
						
					} else {
						currentPath += rule.get().getSign();
					}
				}
				
			}
			counter++;
		}
		
//		for (int counter = matchString.length(); counter > 0; counter-- ) {
//			String str = new String(matchString, startingIndex, lastIndex + 1 - startingIndex);
//			
//			
//			
//			if ()
//		}
		
		
	}
	
	// Returns non-empty String if false
	public String everyNodeHasEverySymbol() {
		String errorString = "";
		// Iterate our alfabet to check if all nodes have a path with all alfabet signs.
		for (DFANode node: _nodes) {
			for (final String sign: _alfabet.getAllSigns()) {
				// Find transition with sign
				Optional<TransitionRule> transition = node.get_transitions().stream().filter((TransitionRule rule) -> rule.hasSign(sign)).findAny();
				// Return false if it's not available
				if (transition.isEmpty()) {
					errorString += ("\n Symbol "+ sign +" not found in node: "+ node.get_state());
				}
			}
		}
		
		return errorString;
	}
	
	public String onlyExistingSymbolsAdded() {
		return "";
	}
	
	public boolean isValidString(String content) {
		int counter = 0;
		
		// Follow the path, get Start
		Optional<DFANode> node = _nodes.stream().filter((DFANode n) -> n.get_state() == this._startState).findAny();
		
		while (!node.isEmpty()) {
			
			// First check if we're done
			if (content.length() == counter)
				return node.get().get_isEndSymbol();

			 
			// Get symbol to traverse
			final char c = content.charAt(counter);
			
			// Where to go? Read from TransitionRule
			Optional<TransitionRule> rule = node.get().get_transitions().stream().filter((TransitionRule tr) -> tr.getSign().equals(Character.toString(c))).findAny();

			if (rule.isEmpty())
				return false;
			
			// next in line
			node = _nodes.stream().filter((DFANode n) -> n.get_state() == rule.get().getGoTo()).findAny();
						
			// Increment count to get next char in next loop iteration.
			counter++;
		}		
		
		return false;
	}
	
	@Override
	public String getMethodAsGraphVizString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("digraph { ");
		
		for (DFANode node: _nodes) {
			for (TransitionRule entry: node.get_transitions()) {
				builder.append(node.get_state() +" -> "+ entry.getGoTo() +" [ label="+ "\""+ entry.getSign() +"\""  +", weigth=\"0.6\"];");
			}
		}
		
		builder.append("}");
		
		return builder.toString();
	}
	
	@Override
	public String getMethodName() {
		return "DFA.gv";
	}
	
	public ArrayList<DFANode> getEndStates() {
		// Get all points with isEndSymbol == true
		List<DFANode> nodes = _nodes.stream().filter(node -> node.get_isEndSymbol()).collect(Collectors.toList());
		
		if (nodes.size() > 0) {
			return (ArrayList<DFANode>)nodes;
		}
		return new ArrayList<DFANode>();
	}
	
	public static DFA GenerateDFA(String matchString) {
		if (matchString.length() > 25)
			return null; // Can't handle letters above Z yet. Also need to reformat it for numbers later (q0, q1, q2, ...)
		
		
		// Add every new occuring character to a unique set
		Set<String> alfabetCharacters = new LinkedHashSet<String>();
		// Create all our nodes
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		// Start node
		DFANode lastDFA = new DFANode("S", false);
		int counter = 0;
		for(char[] c = matchString.toCharArray(); counter < c.length; counter++) {
			// Add every new character to our alfabet
			alfabetCharacters.add(Character.toString(c[counter]));
			
			if (counter + 1 != c.length)
				lastDFA.addTransitions(new TransitionRule(Character.toString(c[counter]), Character.toString(65+counter)));
			// Add node to list before create a new one
			nodes.add(lastDFA);
			// Create a node starting with symbol "A", end char is always end symbol
			lastDFA = new DFANode(Character.toString(65+counter), counter + 1 == c.length);
		}
		
		return new DFA(new Alfabet(alfabetCharacters.toArray(String[]::new)), "S", nodes.toArray(DFANode[]::new));
	}
	
	
}
