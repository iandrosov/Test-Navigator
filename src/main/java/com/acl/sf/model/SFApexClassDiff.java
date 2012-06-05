package com.acl.sf.model;

public class SFApexClassDiff {
	private int count;
	private String Name1;
	private Long LengthWithoutComments1;
	private String Name2;
	private Long LengthWithoutComments2;
	private boolean Match = true;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isMatch() {
		return Match;
	}
	public void setMatch(boolean isMatch) {
		Match = isMatch;
	}
	public String getName1() {
		return Name1;
	}
	public void setName1(String name1) {
		Name1 = name1;
	}
	public Long getLengthWithoutComments1() {
		return LengthWithoutComments1;
	}
	public void setLengthWithoutComments1(Long lengthWithoutComments1) {
		LengthWithoutComments1 = lengthWithoutComments1;
	}
	public String getName2() {
		return Name2;
	}
	public void setName2(String name2) {
		Name2 = name2;
	}
	public Long getLengthWithoutComments2() {
		return LengthWithoutComments2;
	}
	public void setLengthWithoutComments2(Long lengthWithoutComments2) {
		LengthWithoutComments2 = lengthWithoutComments2;
	}

}
