package Forming;

import java.io.Serializable;

public class Alfabet implements Serializable {
	
	private String[] _allSigns;
	
	public Alfabet(String... signs) {
		this._allSigns = signs;
	}
	
	public boolean hasSign(String c) {
		for (String sign: _allSigns) {
			if (sign.equals(c)) return true;
		}
		
		return false;
	}
	
	public String[] getAllSigns() {
		return this._allSigns;
	}

	public void setAllSigns(String[] allSigns) {
		this._allSigns = allSigns;
		
	}
	
	@Override
	public boolean equals(Object ob) {
		Alfabet other = (Alfabet)ob;
		
		return this._allSigns.equals(other.getAllSigns());
	}
}
