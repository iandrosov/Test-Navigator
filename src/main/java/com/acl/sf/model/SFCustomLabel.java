package com.acl.sf.model;

import java.util.ArrayList;

public class SFCustomLabel {
	private int count;
	private String Name;
	private String Label;
	private String Description;
	private String BaseLanguage;
	private Boolean isProtected;
	private ArrayList<SFTranslation> translationList;
	
	public void addTranslation(SFTranslation trn){
		if (this.translationList == null)
			this.translationList = new ArrayList<SFTranslation>();
		this.translationList.add(trn);
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getLabel() {
		return Label;
	}
	public void setLabel(String label) {
		Label = label;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getBaseLanguage() {
		return BaseLanguage;
	}
	public void setBaseLanguage(String baseLanguage) {
		BaseLanguage = baseLanguage;
	}
	public Boolean getIsProtected() {
		return isProtected;
	}
	public void setIsProtected(Boolean isProtected) {
		this.isProtected = isProtected;
	}
	public ArrayList<SFTranslation> getTranslationList() {
		return translationList;
	}
	public void setTranslationList(ArrayList<SFTranslation> translationList) {
		this.translationList = translationList;
	}
	
	
}
