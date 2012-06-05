package com.acl.sf.model;

import java.util.Date;

public class SFDocument {
	private int count;
	private String DeveloperName; 
	private String Name;
	private String NamespacePrefix;
	private Long BodyLength;
	private Date LastModifiedDate;
	private String StringLastModifiedDate;
	private boolean IsPublic;
	private String Id;
	private String CreatedByName;
	private String FolderId;
	private String ContentType;
	private String Type;
	private String Description;
	private String URL;
	private String Keywords;
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getDeveloperName() {
		return DeveloperName;
	}
	public void setDeveloperName(String developerName) {
		DeveloperName = developerName;
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
	public Long getBodyLength() {
		return BodyLength;
	}
	public void setBodyLength(Long bodyLength) {
		BodyLength = bodyLength;
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
	public boolean isIsPublic() {
		return IsPublic;
	}
	public void setIsPublic(boolean isPublic) {
		IsPublic = isPublic;
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
	public String getFolderId() {
		return FolderId;
	}
	public void setFolderId(String folderId) {
		FolderId = folderId;
	}
	public String getContentType() {
		return ContentType;
	}
	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getKeywords() {
		return Keywords;
	}
	public void setKeywords(String keywords) {
		Keywords = keywords;
	}
	
	
}
