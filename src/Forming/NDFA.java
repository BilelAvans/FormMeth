package Forming;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import Parsing.ArrayUtils;
import Parsing.IMethodAsString;

public class NDFA implements IMethodAsString, Serializable {

	protected Alfabet _alfabet;

	protected List<DFANode> _nodes = new ArrayList<DFANode>();

	protected final int _startState;

	
	public static final String EmptySign = "ε";
	protected String matchString = "";
	
	// For display and saving purpouses
	protected String _name;

	// Create from Grammar
	@SuppressWarnings("unchecked")
	protected NDFA(Grammar grammar) {

		this._alfabet = grammar.get_alfabet();
		this._startState = Integer.parseInt(grammar.get_startSymbol());

		for (ProductionRule pr : grammar.get_rules()) {

			ArrayList<TransitionRule<Integer>> trRules = new ArrayList<TransitionRule<Integer>>();

			for (TransitionRule<String> tr : pr.getTransitions()) {
				trRules.add(new TransitionRule<Integer>(tr.getSign(), Integer.valueOf(tr.getGoTo())));
			}

			_nodes.add(new DFANode((char) pr.getFrom().toCharArray()[0], pr.isEndState(),
					trRules.toArray(new TransitionRule[0])));

		}
	}

	public NDFA(Alfabet alfa, int startState, DFANode... nodes) {
		_alfabet = alfa;
		_nodes = Arrays.asList(nodes);
		_startState = startState;

	}

	public boolean startAvailable() {
		// Check if node with "S" exists
		return !(_nodes.stream().filter((DFANode n) -> n.get_state() == this._startState).findAny().isEmpty());

	}



	// Returns non-empty String if false
	public String everyNodeHasEverySymbol() {
		String errorString = "";
		// Iterate our alfabet to check if all nodes have a path with all alfabet signs.
		for (DFANode node : _nodes) {
			for (final String sign : _alfabet.getAllSigns()) {
				// Find transition with sign
				Optional<TransitionRule<Integer>> transition = node.get_transitions().stream()
						.filter((TransitionRule<Integer> rule) -> rule.hasSign(sign)).findAny();
				// Return false if it's not available
				if (transition.isEmpty()) {
					errorString += ("\n Symbol " + sign + " not found in node: " + node.get_state());
				}
			}
		}

		return errorString;
	}

	public String onlyExistingSymbolsAdded() {
		return "";
	}

	public boolean isValidString(String content) {
		if (!this.get_alfabet().canFormString(content))
			return false;
		
		
		int counter = 0;

		// Follow the path, get Start
		Optional<DFANode> node = _nodes.stream().filter((DFANode n) -> n.get_state() == this._startState).findAny();
		
		TransitionRule<String> lastEpsilonAfrit = null;

		while (!node.isEmpty()) {

			// First check if we're done
			if (content.length() == counter)
				return node.get().get_isEndSymbol();

			// Get symbol to traverse
			final char c = content.charAt(counter);

			// Where to go? Read from TransitionRule
			Optional<TransitionRule<Integer>> rule = node.get().getTransitionRuleBySymbol(Character.toString(c));

			if (rule.isEmpty()) {
				// Can only use epsilon if it's an NDFA
				if (this instanceof NDFA) {
					// Check for epsilon if no characters are found, meaby we can work this out
					Optional<TransitionRule<Integer>> epsilonRule = node.get().getTransitionRuleBySymbol(this.EmptySign);
					
					if (!epsilonRule.isEmpty()) {
						// Found epsilon!!!
						// Decrement count by 1, because we're still using the same character.
						counter--;
						node = _nodes.stream().filter((DFANode n) -> n.get_state() == epsilonRule.get().getGoTo()).findAny();
					} else {
						return false;
					}
				}

			} else {

				// next in line
				node = _nodes.stream().filter((DFANode n) -> n.get_state() == rule.get().getGoTo()).findAny();
			}
				
			// Increment count to get next char in next loop iteration.
			counter++;
		}

		return false;
	}

	@Override
	public String getMethodAsGraphVizString() {

		StringBuilder builder = new StringBuilder();
		builder.append("digraph { \n");

		String endNodesString = "node [shape = doublecircle]; ";
		String otherStr = "";

		for (DFANode node : _nodes) {
			if (node.get_isEndSymbol())
				endNodesString += String.valueOf(node.get_state()) + " ";

			for (TransitionRule<Integer> entry : node.get_transitions()) {
				otherStr += (node.get_state() + " -> " + entry.getGoTo() + " [ label=" + "\"" + entry.getSign() + "\""
						+ ", weigth=\"0.6\"]; \n");
			}
		}

		//endNodesString += (";\n");
		builder.append(endNodesString);
		builder.append("node [shape = circle]; \n");
		builder.append(otherStr);

		builder.append("}");

		return builder.toString();
	}

	@Override
	public String getMethodName() {
		return this._name == null ? "test" : this._name;
	}

	public ArrayList<DFANode> getEndStates() {
		// Get all points with isEndSymbol == true
		List<DFANode> nodes = _nodes.stream().filter(node -> node.get_isEndSymbol()).collect(Collectors.toList());

		if (nodes.size() > 0) {
			return (ArrayList<DFANode>) nodes;
		}
		return new ArrayList<DFANode>();
	}
	
	public ArrayList<DFANode> getStartStates() {
		// Get all points with isEndSymbol == true
		List<DFANode> nodes = _nodes.stream().filter(node -> !node.get_isEndSymbol()).collect(Collectors.toList());

		if (nodes.size() > 0) {
			return (ArrayList<DFANode>) nodes;
		}
		return new ArrayList<DFANode>();
	}

	public String getMatchString() {
		return matchString;
	}

	public void setMatchString(String matchString) {
		this.matchString = matchString;
		this.setMethodName(matchString);
	}


	
	protected NDFA() {
		this._startState = 0;
		this._alfabet = new Alfabet();
	}
	
	
	public static NDFA Empty() {
		return new NDFA();
	}

	@SuppressWarnings("unchecked")
	public static DFA fromGraphVizStringToDFA(String string, String dfaName) {

		String[] splitString = string.split("\n");

		// Add every new occuring character to a unique set
		Set<String> alfabetCharacters = new LinkedHashSet<String>();
		// Create all our nodes
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();

		for (int counter = 1; counter < splitString.length - 2; counter++) {
			String stringPart = splitString[counter];
			// C -> S [ label="a", weigth="0.6"];
			Optional<DFANode> node = nodes.stream()
					.filter(nd -> String.valueOf(nd.get_state()).equals(stringPart.charAt(0))).findAny();

			if (node.isEmpty()) {
				node = Optional.of((new DFANode(Integer.valueOf(stringPart.charAt(0)), true)));
				nodes.add(node.get());
			}
			// Add transition label to alfabet
			alfabetCharacters.add(stringPart.split("\"")[1]);

			node.get().addTransitions(new TransitionRule<Integer>(stringPart.split("\"")[1], Integer.valueOf(stringPart.split("->")[1])));
			nodes.add(node.get());
		}

		DFA dfa = new DFA(new Alfabet(alfabetCharacters.toArray(String[]::new)), 0, nodes.toArray(DFANode[]::new));
		dfa.setMethodName(dfaName);
		return dfa;
	}
	
	public void set_alfabet(Alfabet a) {
		this._alfabet = a;
	}

	public NDFA addDFA(DFA other) {
//		if (!other._alfabet.equals(this._alfabet))
//			return null;

		Alfabet alfabet = new Alfabet(ArrayUtils.combine(this._alfabet.getAllSigns(), other.get_alfabet().getAllSigns()));
        
		DFA otherDFA = new Cloon<DFA>(other).get_ob();
		otherDFA.set_alfabet(alfabet);
		otherDFA.incStatesBy(this.get_nodes().size());
		this._alfabet = alfabet;
		// new array of size * size elements
		ArrayList<DFANode> nodes = new ArrayList<>(otherDFA.get_nodes().size() * this.get_nodes().size());
		ArrayList<TransitionRule<Integer>> rules = new ArrayList<>();

		for (int x = 0; x < otherDFA.get_nodes().size(); x++) {
			for (int y = 0 + otherDFA.get_nodes().size(); y < this._nodes.size() + otherDFA.get_nodes().size(); y++) {
				DFANode node = new DFANode(x * this._nodes.size() + y);
				
				
			nodes.add(node);
			// DFANode node = new DFANode();
				for (String a : alfabet.getAllSigns()) {
					Optional<TransitionRule<Integer>> ruleA = this.getNodeByState(x).get().getTransitionRuleBySymbol(a);
					Optional<TransitionRule<Integer>> ruleB = otherDFA.getNodeByState(y).get().getTransitionRuleBySymbol(a);
					// Both rules exist
					if (!ruleA.isEmpty() && !ruleB.isEmpty()) {
						var newRule = new TransitionRule<Integer>(a, ruleA.get().getGoTo() * this._nodes.size() + ruleB.get().getGoTo());
						node.addTransitions(newRule);
					}
				
					
				}
			}

		}
	

		DFA dfa = new DFA(alfabet, 0, nodes.toArray(new DFANode[nodes.size()]));
		return dfa;
	}

	protected Alfabet get_alfabet() {
		// TODO Auto-generated method stub
		return this._alfabet;
	}

	public void incStatesBy(int size) {
		for (DFANode node: this._nodes) {
			node.set_state(node.get_state() + size);
			for (TransitionRule<Integer> tr: node.get_transitions()) {
				tr.setGoTo(tr.getGoTo() + size);
			}
		}
	}

	protected Optional<DFANode> getNodeByState(Integer state) {
		return this._nodes.stream().filter(r -> r.get_state() == state).findAny();
	}

	public List<DFANode> get_nodes() {
		return _nodes;
	}

	public NDFA Denial() {
		NDFA revDFA = new Cloon<NDFA>(this).get_ob();
		
		for (DFANode node : revDFA.get_nodes()) {
			node.set_isEndSymbol(!node.get_isEndSymbol());
		}

		return revDFA;

	}
	
	public void setMethodName(String _name) {
		this._name = _name;
	}
	
	public DFA toDFA() {
		return (DFA)this;
	}
	
	public Grammar toGrammar() {
		
		Grammar grammar = new Grammar(this._alfabet, "S");
		
		for (DFANode node: this._nodes) {
			String nodeName = intToCharStartingFromAWithZeroAsStart(node.get_state());
			ProductionRule pr = new ProductionRule(nodeName, nodeName.equals("S"));
				
				for(TransitionRule<Integer> tr: node.get_transitions()) {
					String s = intToCharStartingFromAWithZeroAsStart(tr.getGoTo());
					
					pr.addTransitionRule(new TransitionRule<String>(tr.getSign(), s));
				}
			grammar.addProductionRule(pr);				
		}

		
		return grammar;
	}
	
	private String intToCharStartingFromAWithZeroAsStart(int count) {
		if (count == 0)
			return "S";
		else if (count < 0)
			return "ERROR";
		
		String newChar = Character.toString((('A') - 1 + count));
		
		return newChar;
	}
	
	
	

//	public DFA reverse() {
//		DFA dfa = new Cloon<DFA>(this).get_ob();
//		
//		for (DFANode node: dfa.get_nodes()) {
//			
//			if (node.get_state().equals(this._startState))
//				node.set_isEndSymbol(true);
//			else if (node.get_isEndSymbol()){
//				this._startStates = node.get_state();
//			}
//			
//			// Switch TransitionRules
//			for (TransitionRule tR: node.get_transitions()) {
//				Optional<DFANode> otherNode = this._nodes.stream().filter(r -> r.get_state().equals());
//				
//				if (!otherNode.isEmpty()) {
//					
//				}
//			}
//			
//		}
//		
//		return dfa;
//	}


//	
//	public static void splitInGroups(int startState, Map<Integer, ArrayList<DFANode>> groupStates, int currentNumber){
//		Set<String> uniqueStuff = new LinkedHashSet<>();
//		
//		// Start states
//		for (Map.Entry<Integer, ArrayList<DFANode>> entry : groupStates.entrySet()) {
//			for (DFANode node: entry.getValue()) {
//				for (TransitionRule tR: node.get_transitions()) {
//					uniqueStuff.add(node.get_state()+tR.getGoTo());	
//				}							
//			}
//		}
//		// Only 1 unique entry, our list can't be minimized further
//		if (uniqueStuff.size() == 1) {
//			return;
//		}
//		else {
//			// Create new states
//			int newNumberOfGroups = uniqueStuff.size();
//			for (int counter = currentNumber; counter < newNumberOfGroups + currentNumber; counter++) {
//				groupStates.put(counter, new ArrayList<DFANode>());
//			
//			}
//			
//			// Transfer states and assign new state name
//			for (Map.Entry<Integer, ArrayList<DFANode>> entry : groupStates.entrySet()) {
//				for (DFANode node: entry.getValue()) {
//					for (TransitionRule tR: node.get_transitions()) {
//						String newState = 'A' + currentNumber + uniqueStuff.stream().collect(Collectors.toList()).indexOf(new DFANode(node.get_state()+tR.getGoTo()));
//						DFANode newNode = new DFANode()
//					}							
//				}
//			}
//			
//			
//			// Remove old states
//			for (int newNumber = newNumberOfGroups + currentNumber;; currentNumber < newNumber; currentNumber++) {
//				groupStates.remove(currentNumber);
//			}
//			currentNumber = 
//			
//		}
//		
//		
//	}

}
