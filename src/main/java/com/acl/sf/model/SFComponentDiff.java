package com.acl.sf.model;

public class SFComponentDiff {
	private int count;
	private String Name1;
	private String Name2;
	private boolean Match = true;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		Name1 = name1;
	}
	public String getName2() {
		return Name2;
	}
	public void setName2(String name2) {
		Name2 = name2;
	}
	public boolean isMatch() {
		return Match;
	}
	public void setMatch(boolean match) {
		Match = match;
	}

	
}
