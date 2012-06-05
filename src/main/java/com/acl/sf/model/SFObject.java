package com.acl.sf.model;

import java.util.ArrayList;
import java.util.Hashtable;

import com.sforce.soap.partner.Field;


public class SFObject {
	public String api_name;
	public String label;
	public String file;
	public String sharingModel;
	public ArrayList<SFField> fields;	
	
	public ArrayList<String> optionHeaders;
	
	public Boolean isCustom = false;
	// Field array from describe object
	public Field[] fld_arr;
	
	// Statistics data
	public SFObjectStats fieldStats;
	
	// Translations for Object labels
	private ArrayList<SFTranslation> translationList;
	
	private Hashtable<String,SFField> fieldMap;
	
	public void initFieldMap(){
		if (this.fieldMap == null){
			this.fieldMap = new Hashtable<String,SFField>();
		}
		if(this.fields != null && this.fields.size() > 0){
			this.fieldMap.clear();
			for (SFField f : this.fields){
				this.fieldMap.put(f.getFullName(), f);
			}
		}
	}
	public SFField getFieldMap(String fld){
		if (this.fieldMap != null && this.fieldMap.containsKey(fld)){
			return this.fieldMap.get(fld);
		}
		return null;	
	}
	
	public void addTranslation(SFTranslation trn){
		if (this.translationList == null)
			this.translationList = new ArrayList<SFTranslation>();
		this.translationList.add(trn);
	}
	
	
	public ArrayList<SFTranslation> getTranslationList() {
		return translationList;
	}


	public void setTranslationList(ArrayList<SFTranslation> translationList) {
		this.translationList = translationList;
	}
	
	public ArrayList<String> getOptionHeaders() {
		return optionHeaders;
	}
	public void setOptionHeaders(ArrayList<String> optionHeaders) {
		this.optionHeaders = optionHeaders;
	}
	public String getApi_name() {
		return api_name;
	}
	public void setApi_name(String api_name) {
		this.api_name = api_name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	public String getSharingModel() {
		return sharingModel;
	}
	public void setSharingModel(String sharingModel) {
		this.sharingModel = sharingModel;
	}
	public ArrayList<SFField> getFields() {
		return fields;
	}
	public void setFields(ArrayList<SFField> fields) {
		this.fields = fields;
	}
	public Boolean getIsCustom() {
		return isCustom;
	}
	public void setIsCustom(Boolean isCustom) {
		this.isCustom = isCustom;
	}
	public SFObjectStats getFieldStats() {
		return fieldStats;
	}
	public void setFieldStats(SFObjectStats fieldStats) {
		this.fieldStats = fieldStats;
	}


	public Field[] getFld_arr() {
		return fld_arr;
	}


	public void setFld_arr(Field[] fld_arr) {
		this.fld_arr = fld_arr;
	}
	
	
}
