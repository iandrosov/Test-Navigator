package com.acl.sf.model;

import java.util.ArrayList;

public class SFFieldTranslation extends SFTranslation{
	private String desc;
	private String helpText;
	private ArrayList<PickListTranslation> pickList;

	public void addPickListTranslation(PickListTranslation trn){
		if (this.pickList == null)
			this.pickList = new ArrayList<PickListTranslation>();
		this.pickList.add(trn);
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public ArrayList<PickListTranslation> getPickList() {
		return pickList;
	}
	public void setPickList(ArrayList<PickListTranslation> pickList) {
		this.pickList = pickList;
	}
	
}
