package Forming;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import Parsing.IMethodAsString;

public class DFA implements IMethodAsString {
	
	private Alfabet _alfabet;
	
	private	ArrayList<DFANode> _nodes = new ArrayList<DFANode>();

	private final String _startState;
	
	// Create from Grammar
	private DFA(Grammar grammar) {
		_startState = "";
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
	
	public DFA(Alfabet alfa, DFANode nodes, final String startState) {
		_alfabet = alfa;
		_nodes = (ArrayList<DFANode>)Arrays.asList(nodes);
		_startState = startState;
		
	}
	
	public boolean startAvailable() {
		if (!_startState.isEmpty()) {
			Optional<DFANode> node = _nodes.stream().filter((DFANode n) -> n.get_state().equals(_startState)).findAny();
			
			return (!node.isEmpty());
		}
		
		return false;
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
	
	
}
