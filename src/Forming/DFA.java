package Forming;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Map;

import Parsing.IMethodAsString;

public class DFA implements IMethodAsString {
	
	private Alfabet _alfabet;
	
	ArrayList<DFANode> _nodes = new ArrayList<DFANode>();

	// Create from Grammar
	public DFA(Grammar grammar) {
		
	}
	
	public DFA() {
		_nodes.add(new DFANode("a", false, new SimpleEntry<String, String>("a", "b"), new SimpleEntry<String, String>("a", "c")));
	}
	
	@Override
	public String getMethodAsGraphVizString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("digraph { ");
		
		for (DFANode node: _nodes) {
			
			for (Map.Entry<String, String> entry: node.get_transitions().entrySet()) {
				builder.append(node.get_state() +" -> "+ entry.getValue() +" [ label="+ "\""+ entry.getKey() +"\""  +", weigth=\"0.6\"];");
			}
		}
		
		builder.append("}");
		
		return builder.toString();
	}

	@Override
	public String getMethodName() {
		return "DFA.gv";
	}
	
	
}
