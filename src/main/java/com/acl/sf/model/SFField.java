package com.acl.sf.model;

import java.util.ArrayList;

/**
 * @author iandrosov
 *
 */
public class SFField {
	private int counter = 0;
	private String fullName;
	private String label;
	private String type;
	private String displayFormat; // Used Only for Name field
	private String description;
	private String inlineHelpText;
	private int length;
	private int byteLength;
	private int digits;
	private int scale;
	
	// References
	private String referenceTo;
	private String relationshipLabel;
	private String relationshipName;
	private int relationshipOrder;
	
	private Boolean isNameField = false;	
	private ArrayList<PickList> pickList; 
	
	// Additional meta data elements
	private String formula;
	private Boolean isAutoNumber = false;
	private Boolean isUnique = false;
	private Boolean isIdLookup = false;
	private Boolean isExternalId = false;
	private String typeName; 
	private int precision = 0;
	
	// Translations for field labels
	private ArrayList<SFTranslation> translationList;
	
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


	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDisplayFormat() {
		return displayFormat;
	}
	public void setDisplayFormat(String displayFormat) {
		this.displayFormat = displayFormat;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getInlineHelpText() {
		return inlineHelpText;
	}
	public void setInlineHelpText(String inlineHelpText) {
		this.inlineHelpText = inlineHelpText;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getReferenceTo() {
		return referenceTo;
	}
	public void setReferenceTo(String referenceTo) {
		this.referenceTo = referenceTo;
	}
	public String getRelationshipLabel() {
		return relationshipLabel;
	}
	public void setRelationshipLabel(String relationshipLabel) {
		this.relationshipLabel = relationshipLabel;
	}
	public String getRelationshipName() {
		return relationshipName;
	}
	public void setRelationshipName(String relationshipName) {
		this.relationshipName = relationshipName;
	}
	public int getRelationshipOrder() {
		return relationshipOrder;
	}
	public void setRelationshipOrder(int relationshipOrder) {
		this.relationshipOrder = relationshipOrder;
	}
	public Boolean getIsNameField() {
		return isNameField;
	}
	public void setIsNameField(Boolean isNameField) {
		this.isNameField = isNameField;
	}
	public ArrayList<PickList> getPickList() {
		return pickList;
	}
	public void setPickList(ArrayList<PickList> pickList) {
		this.pickList = pickList;
	}
	public String getFormula() {
		return formula;
	}
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public Boolean getIsAutoNumber() {
		return isAutoNumber;
	}
	public void setIsAutoNumber(Boolean isAutoNumber) {
		this.isAutoNumber = isAutoNumber;
	}
	public Boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(Boolean isUnique) {
		this.isUnique = isUnique;
	}
	public Boolean getIsIdLookup() {
		return isIdLookup;
	}
	public void setIsIdLookup(Boolean isIdLookup) {
		this.isIdLookup = isIdLookup;
	}
	public Boolean getIsExternalId() {
		return isExternalId;
	}
	public void setIsExternalId(Boolean isExternalId) {
		this.isExternalId = isExternalId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}


	public int getByteLength() {
		return byteLength;
	}


	public void setByteLength(int byteLength) {
		this.byteLength = byteLength;
	}


	public int getDigits() {
		return digits;
	}


	public void setDigits(int digits) {
		this.digits = digits;
	}


	public int getScale() {
		return scale;
	}


	public void setScale(int scale) {
		this.scale = scale;
	}
	
}
