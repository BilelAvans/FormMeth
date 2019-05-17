package Forming;

public class Alfabet {
	
	private String[] _allSigns;
	
	public Alfabet(String... signs) {
		this._allSigns = signs;
	}
	
	public boolean hasSign(String c) {
		for (String sign: _allSigns) {
			if (sign == c) return true;
		}
		
		return false;
	}
	
	public String[] getAllSigns() {
		return this._allSigns;
	}
}
