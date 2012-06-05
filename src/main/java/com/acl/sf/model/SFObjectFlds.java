package com.acl.sf.model;

public class SFObjectFlds {

    private String fldName;
    private String typeName;
    private String lableName;
    private String relationName;
    private Integer length;
    private String helpText;
    private boolean match;
    private int counter;
    
    public SFObjectFlds(){
    	fldName = "";
        typeName = "";
        lableName = "";
        relationName = "";
        length = 0;
        helpText = "";
        match = false;
        counter = 0;
    }
    
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public String getFldName() {
		return fldName;
	}
	public void setFldName(String fldName) {
		this.fldName = fldName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getLableName() {
		return lableName;
	}
	public void setLableName(String lableName) {
		this.lableName = lableName;
	}
	public String getRelationName() {
		return relationName;
	}
	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public boolean isMatch() {
		return match;
	}
	public void setMatch(boolean match) {
		this.match = match;
	}
    
    

}
