package com.acl.sf.model;

import java.util.Date;

public class SFRecordType {
	private int count;
	private String Id;
	private String Name;
	private String NamespacePrefix;
	private String DeveloperName;
	private String Description;
	private String BusinessProcessId;
	private String SobjectType;
	private Boolean IsActive;
	private String CreatedByName; 
	private Date LastModifiedDate;
	private String StringLastModifiedDate;
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
	public String getNamespacePrefix() {
		return NamespacePrefix;
	}
	public void setNamespacePrefix(String namespacePrefix) {
		NamespacePrefix = namespacePrefix;
	}
	public String getDeveloperName() {
		return DeveloperName;
	}
	public void setDeveloperName(String developerName) {
		DeveloperName = developerName;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getBusinessProcessId() {
		return BusinessProcessId;
	}
	public void setBusinessProcessId(String businessProcessId) {
		BusinessProcessId = businessProcessId;
	}
	public String getSobjectType() {
		return SobjectType;
	}
	public void setSobjectType(String sobjectType) {
		SobjectType = sobjectType;
	}
	public Boolean getIsActive() {
		return IsActive;
	}
	public void setIsActive(Boolean isActive) {
		IsActive = isActive;
	}
	public String getCreatedByName() {
		return CreatedByName;
	}
	public void setCreatedByName(String createdByName) {
		CreatedByName = createdByName;
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
	
	
}
