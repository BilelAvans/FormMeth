package Parsing;

import Forming.DFA;
import Forming.NDFA;

public class DFACommandParser {

	public static DFA ParseMultiString(String str) {
		if (str.length() == 0)
			return (DFA)DFA.Empty();
		
		DFA currentDFA = DFA.GenerateDFA(str);
		
		if (str.split(" ").length == 1)
			return currentDFA;
		
		
		else {
			String[] splitString = str.split("\\\\s+");
			
			System.out.println(str);
			
			for (int counter = 1; counter < splitString.length; counter += 2) {
				DFA newDFA = DFA.GenerateDFA(splitString[counter+1]);
				System.out.println(splitString[counter+1]);
				
				if (splitString[counter].equals("&&"))
					currentDFA.addDFA(newDFA);
				
			}
		}
		
		return currentDFA;
	}
}
