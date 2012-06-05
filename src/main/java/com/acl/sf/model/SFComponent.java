package com.acl.sf.model;

import java.util.Date;

public class SFComponent {
	private int count;
	private String NamespacePrefix;
	private String Name;
	private String MasterLabel;
	private Date LastModifiedDate;
	private String StringLastModifiedDate;
	private String Id;
	private String Description;
	private String CreatedByName; 
	private String ControllerType;
	private String ControllerKey;
	private String ApiVersion;
	public String getNamespacePrefix() {
		return NamespacePrefix;
	}
	public void setNamespacePrefix(String namespacePrefix) {
		NamespacePrefix = namespacePrefix;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMasterLabel() {
		return MasterLabel;
	}
	public void setMasterLabel(String masterLabel) {
		MasterLabel = masterLabel;
	}
	public Date getLastModifiedDate() {
		return LastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		LastModifiedDate = lastModifiedDate;
	}
	public String getStringLastModifiedDate() {
		return StringLastModifiedDate;
	}
	public void setStringLastModifiedDate(String stringLastModifiedDate) {
		StringLastModifiedDate = stringLastModifiedDate;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getCreatedByName() {
		return CreatedByName;
	}
	public void setCreatedByName(String createdByName) {
		CreatedByName = createdByName;
	}
	public String getControllerType() {
		return ControllerType;
	}
	public void setControllerType(String controllerType) {
		ControllerType = controllerType;
	}
	public String getControllerKey() {
		return ControllerKey;
	}
	public void setControllerKey(String controllerKey) {
		ControllerKey = controllerKey;
	}
	public String getApiVersion() {
		return ApiVersion;
	}
	public void setApiVersion(String apiVersion) {
		ApiVersion = apiVersion;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
}
