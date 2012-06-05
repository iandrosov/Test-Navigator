package com.acl.sf.model;

import java.util.Date;

public class SFApexTrigger {
	private int count;
	private String Status; 
	private String Name;
	private String NamespacePrefix;
	private Long LengthWithoutComments;
	private Date LastModifiedDate;
	private String StringLastModifiedDate;
	private boolean IsValid;
	private String Id;
	private String CreatedByName;
	private String ApiVersion;
	private boolean Match = true;
	
	public boolean isMatch() {
		return Match;
	}
	public void setMatch(boolean isMatch) {
		Match = isMatch;
	}
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Long getLengthWithoutComments() {
		return LengthWithoutComments;
	}
	public void setLengthWithoutComments(Long lengthWithoutComments) {
		LengthWithoutComments = lengthWithoutComments;
	}
	public Date getLastModifiedDate() {
		return LastModifiedDate;
	}
	public void setLastModifiedDate(Date lastModifiedDate) {
		LastModifiedDate = lastModifiedDate;
	}
	public boolean isIsValid() {
		return IsValid;
	}
	public void setIsValid(boolean isValid) {
		IsValid = isValid;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getCreatedByName() {
		return CreatedByName;
	}
	public void setCreatedByName(String createdByName) {
		CreatedByName = createdByName;
	}
	public String getApiVersion() {
		return ApiVersion;
	}
	public void setApiVersion(String apiVersion) {
		ApiVersion = apiVersion;
	}
	public String getNamespacePrefix() {
		return NamespacePrefix;
	}
	public void setNamespacePrefix(String namespacePrefix) {
		NamespacePrefix = namespacePrefix;
	}
	public String getStringLastModifiedDate() {
		return StringLastModifiedDate;
	}
	public void setStringLastModifiedDate(String stringLastModifiedDate) {
		StringLastModifiedDate = stringLastModifiedDate;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getCount() {
		return count;
	}	

}
