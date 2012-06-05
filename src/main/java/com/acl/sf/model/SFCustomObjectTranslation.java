package com.acl.sf.model;

import java.util.ArrayList;

public class SFCustomObjectTranslation {
	private String language;
	private String objectName;
	private String objectLabel;
	private String objectPluralLabel;
	private ArrayList<SFFieldTranslation> fields;
	private ArrayList<String> languageList;
	
	public void addFieldTranslation(SFFieldTranslation trn){
		if (this.fields == null)
			this.fields = new ArrayList<SFFieldTranslation>();
		this.fields.add(trn);
	}
	
	public String getObjectName() {
		return objectName;
	}
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getObjectLabel() {
		return objectLabel;
	}
	public void setObjectLabel(String objectLabel) {
		this.objectLabel = objectLabel;
	}
	public String getObjectPluralLabel() {
		return objectPluralLabel;
	}
	public void setObjectPluralLabel(String objectPluralLabel) {
		this.objectPluralLabel = objectPluralLabel;
	}
	public ArrayList<SFFieldTranslation> getFields() {
		return fields;
	}
	public void setFields(ArrayList<SFFieldTranslation> fields) {
		this.fields = fields;
	}

	public ArrayList<String> getLanguageList() {
		return languageList;
	}

	public void setLanguageList(ArrayList<String> languageList) {
		this.languageList = languageList;
	}

	public void addLanguageCode(String code){
		if (this.languageList == null)
			this.languageList = new ArrayList<String>();
		this.languageList.add(code);
	}
}
