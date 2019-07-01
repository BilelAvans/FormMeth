package Forming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import Parsing.MoreFunctions.TwoParamFunction;

public class DFA extends NDFA {

	public DFA(Alfabet alfa, int startState, DFANode... nodes) {
		super(alfa, startState, nodes);
		alfa.removeEpsilon();
		// TODO Auto-generated constructor stub
	}
	
	public static enum DFAGenerationOptions { STARTS_WITH, ENDS_WITH, CONTAINS, FULLSTRING };

	public static DFA GenerateDFA(DFAGenerationOptions options, String matchString) {
		// Create all our nodes
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		var alfabet = Alfabet.fromString(matchString);
		
		int counter = 0;
//		if (options == DFAGenerationOptions.ENDS_WITH || options == DFAGenerationOptions.CONTAINS) {
//			DFANode firstNode = new DFANode(counter, false);
//			for (String s: alfabet.getAllSigns()) {
//				firstNode.addTransitions(new TransitionRule<Integer>(s, counter));
//			}
//			nodes.add(firstNode);
//		}
			
		
		// Add every new occuring character to a unique set
		Set<String> alfabetCharacters = new LinkedHashSet<String>();

		// Start node
		DFANode lastDFA = new DFANode(counter, false);
		
		counter++;
		for (char[] c = matchString.toCharArray(); counter <= c.length; counter++) {
			// Add every new character to our alfabet
			alfabetCharacters.add(Character.toString(c[counter - 1]));
			
			if (!(Character.toString(c[counter - 1]).equals(NDFA.EmptySign)))
				lastDFA.addTransitions(new TransitionRule<Integer>(Character.toString(c[counter - 1]), counter));

			// Add node to list before create a new one
			nodes.add(lastDFA);
			// Create a node starting with symbol "A", end char is always end symbol
			lastDFA = new DFANode(counter, counter == c.length);

		}

		nodes.add(lastDFA);
		
//		if (options == DFAGenerationOptions.STARTS_WITH) {
//			DFANode lastNode = new DFANode(counter, true);
//			for (String s: alfabet.getAllSigns()) {
//				lastNode.addTransitions(new TransitionRule<Integer>(s, counter));
//			}
//			nodes.add(lastNode);
//		}

		DFA dfa = new DFA(alfabet, 0, nodes.toArray(DFANode[]::new));
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
		this._alfabet.removeEpsilon();
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

						for (int count = 0; count <= counter; count++) {
							// Match the strings
							// Look for the biggest amount of chars in which the back of our new string
							// compares to the front of the other. (Does this even make sense?)
							String secondString = this.matchString.substring(0, count);
							String thirdString = firstString.substring(firstString.length() - count, firstString.length());

							System.out.println("Comp "+ secondString +" to "+ thirdString);
							
							if (thirdString.equals(secondString)) {
								newRule = new TransitionRule<Integer>(c, count);
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
	
	public static ArrayList<DFANode> genNodes(String a, int incBy) {
		int counter = incBy;
		ArrayList<DFANode> nodes = new ArrayList<DFANode>();
		
		DFANode node = new DFANode(counter, false);
		
		for (char c: a.toCharArray()) {
			System.out.println("State: "+ (counter + 1));
			node.addTransitions(new TransitionRule(c, counter + 1));		
			nodes.add(node);
			node = new DFANode(counter + 1, false);
			counter++;
		}
		
		nodes.add(node);
		
		nodes.get(nodes.size() - 1).set_transitions(new ArrayList<TransitionRule<Integer>>());
		
		return nodes;
	}
	
	public DFA minimize() {
		DFA dfa = new Cloon<DFA>(this).get_ob();

		ArrayList<ArrayList<DFANode>> allNodes = new ArrayList<>();
		allNodes.add(dfa.getStartStates());
		allNodes.add(dfa.getEndStates());
		
		// Function to check in which arraylist a specific node is
		// Needed to caluclate block name/number
		TwoParamFunction<ArrayList<ArrayList<DFANode>>, DFANode, Integer> groupOf = (list, node) -> {
			for (int counter = 0; counter < list.size(); counter++) {
				if (list.get(counter).contains(node))
					return counter;
			}
			// -1 = error code.
			return -1;
		};		
		
		// Loop until we cant minimalize anymore
		for (boolean blocksComplete = false; !blocksComplete; ) {
			blocksComplete = true;
			
			ArrayList<ArrayList<DFANode>> newNodes = new ArrayList<>();
		
			for (ArrayList<DFANode> nodesList: allNodes) {
				//ArrayList<String> groups = new ArrayList<String>();
				HashMap<DFANode, String> map = new HashMap<>();
				
				for (DFANode node: nodesList) {
					String paths = "";
					
					for(String s: dfa.get_alfabet().getAllSigns()) {
						// Find out where it all leads
						if (!paths.isEmpty()) {
							paths += ",";
						} 
						
						// Get goto
						Optional<TransitionRule<Integer>> tr = node.getTransitionRuleBySymbol(s);
						
						if (!tr.isEmpty())
							paths += String.valueOf(groupOf.apply(allNodes, new DFANode(tr.get().getGoTo())));
						else
							return null;
					}
					
					map.put(node, paths);
					
				}
				// Check a set from all groups and check if we have more than 1 (if so, we need to rearrange)
				Set<String> uniqueGroups = new HashSet<String>(map.values());
				boolean isCorrect = uniqueGroups.size() == 1;
				// All blocks must be complete, so we need this operation
				blocksComplete &= isCorrect;
				
				// Create new groups
				// Create a group from a unique set values
				for (String s: uniqueGroups) {
					// Get all nodes of a group and add them to a new one
					ArrayList<DFANode> nodesToAdd = new ArrayList<DFANode>(map.entrySet().stream().filter(entry -> entry.getValue().equals(s)).map(kv -> kv.getKey()).collect(Collectors.toList()));
					//RemoveNodes(allNodes, nodesToAdd);	
					newNodes.add(nodesToAdd);
					
				}
			}
			
			for (ArrayList<DFANode> n: newNodes) {				
				for (var node: n) {					
					for (var trans: node.get_transitions()) {
						int newGoTo = groupOf.apply(newNodes, new DFANode(trans.getGoTo()));
						trans.setGoTo(newGoTo);
					}
					
					//RemoveNodes(newNodes, n);
				}
			}
			
			allNodes = newNodes;
			
		}
		
		// Complete, let's reform our nodes
		var nodes = new ArrayList<DFANode>();
		
		int counter = 0;
		
		for (ArrayList<DFANode> group: allNodes) {
			// Every group becomes 1 node
			ArrayList<TransitionRule<Integer>> rules = group.get(0).get_transitions();
			
			if (!rules.isEmpty())
			{
				DFANode node = new DFANode(counter, group.get(0).get_isEndSymbol(), rules.toArray(TransitionRule[]::new));
				nodes.add(node);
				counter++;
			}
			
		}
		return new DFA(this._alfabet, 0, nodes.toArray(DFANode[]::new));
	}
	

	private ArrayList<DFANode> JoinNodes(ArrayList<ArrayList<DFANode>> n1){
		ArrayList<DFANode> nodes = new ArrayList<>();
		
		for (ArrayList<DFANode> list: n1) {
			for (DFANode node: list) {
				nodes.add(node);
			}
		}
		
		return nodes;
	}
	
	private ArrayList<DFANode> RemoveNodes(ArrayList<ArrayList<DFANode>> n1, ArrayList<DFANode> nodesToAdd){
		ArrayList<DFANode> nodes = new ArrayList<>();
		
		for (ArrayList<DFANode> list: n1) {
			Iterator<DFANode> nodeiterator = list.iterator();
			
			while (nodeiterator.hasNext()) {
				DFANode node = nodeiterator.next();
				// Remove if node is found
				for (DFANode no: nodesToAdd) {
					if (node.equals(no))
						nodeiterator.remove();
				}

			}
		}
		
		return nodes;
	}
	
	
	
}
