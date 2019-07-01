package Forming;

import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Alfabet implements Serializable {

	private String[] _allSigns;

	public Alfabet(String... signs) {
		this._allSigns = signs;
	}

	public boolean hasSign(String c) {
		for (String sign : _allSigns) {
			if (sign.equals(c))
				return true;
		}

		return false;
	}

	public String[] getAllSigns() {
		return this._allSigns;
	}

	public void setAllSigns(String[] allSigns) {
		this._allSigns = allSigns;

	}

	void removeEpsilon() {
		ArrayList<String> signs = new ArrayList<String>();

		if (!Arrays.asList(_allSigns).contains(NDFA.EmptySign))
			return;

		for (int counter = 0; counter < this._allSigns.length; counter++) {
			if (!(_allSigns[counter] == NDFA.EmptySign)) {
				signs.add(this._allSigns[counter]);
			}
		}

		this._allSigns = signs.stream().toArray(String[]::new);
	}

	public static Alfabet fromString(String str) {
		Set<String> set = new HashSet<String>();
		
		for (char c: str.toCharArray())
			set.add(Character.toString(c));
		
		return new Alfabet(set.toArray(String[]::new));
	}

	@Override
	public boolean equals(Object ob) {
		Alfabet other = (Alfabet) ob;

		return this._allSigns.equals(other.getAllSigns());
	}

	public boolean canFormString(String content) {
		var list = Arrays.asList(this._allSigns);

		for (int counter = 0; counter < content.length(); counter++) {
			if (!list.contains(Character.toString(content.charAt(counter))))
				return false;
		}

		return true;
	}
	
}
