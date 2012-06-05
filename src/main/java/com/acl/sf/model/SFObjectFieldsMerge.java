package com.acl.sf.model;

public class SFObjectFieldsMerge {
	private SFObjectFlds source_obj;
	private SFObjectFlds target_obj;

	public SFObjectFieldsMerge(){
		this.source_obj = new SFObjectFlds();
		this.target_obj = new SFObjectFlds();
	}
	
	public SFObjectFlds getSource_obj() {
		return source_obj;
	}
	public void setSource_obj(SFObjectFlds source_obj) {
		this.source_obj = source_obj;
	}
	public SFObjectFlds getTarget_obj() {
		return target_obj;
	}
	public void setTarget_obj(SFObjectFlds target_obj) {
		this.target_obj = target_obj;
	}
	
	
}
