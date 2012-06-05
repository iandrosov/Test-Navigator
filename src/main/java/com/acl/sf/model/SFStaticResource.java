package com.acl.sf.model;

import java.util.Date;

public class SFStaticResource {
	private int count;
	private String Name;
	private String NamespacePrefix;
	private Long BodyLength;
	private Date LastModifiedDate;
	private String StringLastModifiedDate;
	private String Id;
	private String CreatedByName;
	private String ContentType;
	private String CacheControl;
	private String Description;
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
	public String getContentType() {
		return ContentType;
	}
	public void setContentType(String contentType) {
		ContentType = contentType;
	}
	public String getCacheControl() {
		return CacheControl;
	}
	public void setCacheControl(String cacheControl) {
		CacheControl = cacheControl;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}

}
