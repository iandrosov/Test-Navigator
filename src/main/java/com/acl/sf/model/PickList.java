package com.acl.sf.model;

import java.util.ArrayList;

public class PickList {
	public String fullName;
	public String value;
	public Boolean is_default;
	
	// Translations for field labels
	private ArrayList<PickListTranslation> translationList;
	
	public void addTranslation(PickListTranslation trn){
		if (this.translationList == null)
			this.translationList = new ArrayList<PickListTranslation>();
		this.translationList.add(trn);
	}
	
	
	public ArrayList<PickListTranslation> getTranslationList() {
		return translationList;
	}


	public void setTranslationList(ArrayList<PickListTranslation> translationList) {
		this.translationList = translationList;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getIs_default() {
		return is_default;
	}
	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}
	public String getDisplayName(){
		String nm = this.fullName;
		if (!this.fullName.equals(this.value)){
			nm = this.value +" - "+this.fullName;
		}
		return nm;	
	}
	
}
