package com.acl.sf.model;

public class SFProfileDiff {
	private int count;
	private String UserType1;
	private String Name1;
	private String UserType2;
	private String Name2;
	private boolean Match = true;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getUserType1() {
		return UserType1;
	}
	public void setUserType1(String userType1) {
		UserType1 = userType1;
	}
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		Name1 = name1;
	}
	public String getUserType2() {
		return UserType2;
	}
	public void setUserType2(String userType2) {
		UserType2 = userType2;
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
