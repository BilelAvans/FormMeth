package Forming;

public class Alfabet {
	
	private char[] _allSigns;
	
	public Alfabet(char... signs) {
		this._allSigns = signs;
	}
	
	public boolean hasSign(char c) {
		for (char sign: _allSigns) {
			if (sign == c) return true;
		}
		
		return false;
	}
	
	public char[] getAllSigns() {
		return this._allSigns;
	}
}
