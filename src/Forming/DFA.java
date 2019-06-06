package Forming;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DFA extends NDFA {

	public DFA(Alfabet alfa, int startState, DFANode... nodes) {
		super(alfa, startState, nodes);
		// TODO Auto-generated constructor stub
	}

	public static DFA GenerateDFA(String matchString) {
		if (matchString.length() > 25)
			return null; // Can't handle letters above Z yet. Also need to reformat it for numbers later
							// (q0, q1, q2, ...)

		// Add every new occuring character to a unique set
		Set<String> alfabetCharacters = new LinkedHashSet<String>();
		// Create all our nodes
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		// Start node
		DFANode lastDFA = new DFANode(0, false);
		int counter = 1;
		for (char[] c = matchString.toCharArray(); counter <= c.length; counter++) {
			System.out.println(c[counter - 1]);
			// Add every new character to our alfabet
			alfabetCharacters.add(Character.toString(c[counter - 1]));

			lastDFA.addTransitions(new TransitionRule<Integer>(Character.toString(c[counter - 1]), counter));

			// Add node to list before create a new one
			nodes.add(lastDFA);
			// Create a node starting with symbol "A", end char is always end symbol
			lastDFA = new DFANode(counter, counter == c.length);

		}

		nodes.add(lastDFA);

		DFA dfa = new DFA(new Alfabet(alfabetCharacters.toArray(String[]::new)), 0, nodes.toArray(DFANode[]::new));
		dfa.setMatchString(matchString);
		dfa.addMissingAlfabetCharacters();

		return dfa;
	}
	
	private DFANode getDestinationNodeByPath(String pathString) {
		int pathRunner = 0;
		
		Optional<DFANode> node = this.getNodeByState(0);
		
		while (!node.isEmpty() && pathRunner < pathString.length()) {
			Optional<TransitionRule<Integer>> rule = node.get().getTransitionRuleBySymbol(Character.toString(pathString.charAt(pathRunner)));
			
			if (!rule.isEmpty())
				node = this.getNodeByState(rule.get().getGoTo());
			else
				node = Optional.of(null);	
			
		}
		
		if (node.isEmpty())
			node = this.getNodeByState(0);
		
		return node.get();
	}

	@SuppressWarnings("unchecked")
	protected void addMissingAlfabetCharacters() {

		int counter = 0;
		
		this._nodes = this._nodes.stream().sorted((n1, n2) -> n1.compareTo(n2)).collect(Collectors.toList());
		String currentPath = "";
		for (DFANode node : this._nodes) {
			
			for (String c : this._alfabet.getAllSigns()) {
				if (counter == 0) {
					Optional<TransitionRule<Integer>> rule = node.getTransitionRuleBySymbol(c);

					if (rule.isEmpty()) {
						node.addTransitions(new TransitionRule<Integer>(c, 0));
					} else {
						currentPath += rule.get().getSign();
					}
				} else {
					Optional<TransitionRule<Integer>> rule = node.getTransitionRuleBySymbol(c);
					if (rule.isEmpty()) {
						// Where must we go then?
						String firstString = currentPath + c;
						TransitionRule<Integer> newRule = null;

						for (int count = 0; count < counter; count++) {
							// Match the strings
							// Look for the biggest amount of chars in which the back of our new string
							// compares to the front of the other. (Does this even make sense?)
							String secondString = this.matchString.substring(0, count);
							String thirdString = firstString.substring(firstString.length() - count,
									firstString.length());

							System.out.println("Comparing: " + secondString + " to: " + thirdString +" in node: "+ node.get_state());
							System.out.println("Comparing: " + secondString + " to: " + thirdString +" in node: "+ node.get_state());

							if (thirdString.equals(secondString)) {
								newRule = new TransitionRule<Integer>(c, count % _nodes.size());
							}
						}

						/*
						 * if (node.get_isEndSymbol()) // newRule = new TransitionRule<Integer>(c,
						 * counter); // else
						 */ if (newRule == null)
							newRule = new TransitionRule<Integer>(c, 0);

						node.addTransitions(newRule);

					} else {
						currentPath += rule.get().getSign();
					}
				}
				
			}
			counter++;
		}

	}

}
