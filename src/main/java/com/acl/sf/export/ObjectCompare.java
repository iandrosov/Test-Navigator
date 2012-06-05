package com.acl.sf.export;

import java.util.ArrayList;
import java.util.Hashtable;

import com.sforce.soap.partner.Field;

import com.acl.sfdc.SfCreds;
import com.acl.sfdc.SfUtil;
import com.acl.sf.model.*;
import com.acl.sf.util.*;

public class ObjectCompare {

	private String sobjectName;
	private String sobjectLabel;
    private ArrayList<SFObjectFlds> fldList;
    private ArrayList<SFObjectFlds> fldListRemote;  
	private Hashtable<String, SFObjectFlds> ht_map_source;
	private Hashtable<String, SFObjectFlds> ht_map_target;
    private String acl_user;
    
	public ObjectCompare(){
		this.fldList = new ArrayList<SFObjectFlds>();
		this.fldListRemote = new ArrayList<SFObjectFlds>();
		this.ht_map_source = new Hashtable<String, SFObjectFlds>();
		this.ht_map_target = new Hashtable<String, SFObjectFlds>();
	}
	
	public boolean doCompareResult(String object_name, String src, String trg) throws Exception{
		// Get SOurce ORG information
		SfUtil sfdc_source = new SfUtil();
		sfdc_source.login_wsc(src);
		this.acl_user = sfdc_source.getAcl_user();
		this.fldList = this.getObjectInfo(sfdc_source, object_name, true);
		sfdc_source.logout();
		// Get Target compare org information
		SfUtil sfdc_target = new SfUtil();
		sfdc_target.login_wsc(trg);
		this.fldListRemote = this.getObjectInfo(sfdc_target, object_name, false);
		sfdc_target.logout();
		// Set compare fields
		this.setComparedFields();
		return true;
	}

	public boolean doCompareResult(String object_name, String src, String trg, ArrayList<SfCreds> crdList) throws Exception{
		// Get SOurce ORG information
		SfUtil sfdc_source = new SfUtil(crdList);
		sfdc_source.login_wsc(src);
		this.acl_user = sfdc_source.getAcl_user();
		this.fldList = this.getObjectInfo(sfdc_source, object_name, true);
		sfdc_source.logout();
		// Get Target compare org information
		SfUtil sfdc_target = new SfUtil(crdList);
		sfdc_target.login_wsc(trg);
		this.fldListRemote = this.getObjectInfo(sfdc_target, object_name, false);
		sfdc_target.logout();
		// Set compare fields
		this.setComparedFields();
		return true;
	}
	
	private void setComparedFields() {
		for (SFObjectFlds objFld : fldList){
			if (ht_map_target != null && objFld != null){
				if (ht_map_target.containsKey(objFld.getFldName()))
					objFld.setMatch(true);
			}
		}
		for (SFObjectFlds objFld_trg : fldListRemote){
			if (ht_map_source != null && objFld_trg != null){
				if (ht_map_source.containsKey(objFld_trg.getFldName()))
					objFld_trg.setMatch(true);
			}
		}
	}
	
    private ArrayList<SFObjectFlds> getObjectInfo(SfUtil sfdc, String object_name, boolean source)throws Exception{
    	ArrayList<SFObjectFlds> ll = new ArrayList<SFObjectFlds>();
    		ObjectScribe os = new ObjectScribe();
    		SFObject obj = os.getObjectMetadata(sfdc,object_name);
    		if (obj != null){
    			this.sobjectLabel = obj.getLabel();
    			this.sobjectName = obj.getApi_name();
    			ArrayList<String> nameList = new ArrayList<String>();
    			for (int i = 0; i < obj.fld_arr.length; i++){
    				Field fld = obj.fld_arr[i];
    				if (!ObjectUtil.isStandardField(fld.getName())){
    					SFObjectFlds cust_fld = new SFObjectFlds();
    					cust_fld.setFldName(fld.getName());
    					cust_fld.setLableName(fld.getLabel());
    					cust_fld.setHelpText(fld.getInlineHelpText());
    					cust_fld.setLength(fld.getLength());
    					cust_fld.setTypeName(fld.getType().name());
    					cust_fld.setCounter(i+1);
    				
    					if (source)
    						this.ht_map_source.put(fld.getName(), cust_fld);
    					else
    						this.ht_map_target.put(fld.getName(), cust_fld);
        		 
    					nameList.add(fld.getName());
    				}
    			}
    			// Sort names here
    			// nameList.sort();
    			for(String fld_name : nameList){
    				SFObjectFlds sf_fld;
    				if (source){
    					sf_fld = (SFObjectFlds)this.ht_map_source.get(fld_name);
    				}else{
    					sf_fld = (SFObjectFlds)this.ht_map_target.get(fld_name);
    				}
    				ll.add(sf_fld);
    			}
    		}
        	return ll;
    }

    /**
     * Create Merged list of results combined object has source and target
     * @return
     */
    public ArrayList<SFObjectFieldsMerge> getMergeResult() {
    	ArrayList<SFObjectFieldsMerge> lst = new ArrayList<SFObjectFieldsMerge>();
    	int merge_size = 0;
    	if (fldList != null && fldList.size() > 0)
    		merge_size = fldList.size();
    	
    	if (fldListRemote != null && fldListRemote.size() > 0 && fldListRemote.size() > merge_size)
    		merge_size = fldListRemote.size();
    	
    	for (int i = 0; i < merge_size; i++){
    		SFObjectFieldsMerge fm = new SFObjectFieldsMerge();
    		if (fldList != null && fldList.size() > i){
    			fm.setSource_obj(fldList.get(i));
    		}
    		if (fldListRemote != null && fldListRemote.size() > i){
    			fm.setTarget_obj(fldListRemote.get(i));
    		}
    		lst.add(fm);	
    	}
    	
    	return lst;
    }
    
	public ArrayList<SFObjectFlds> getFldList() {
		return fldList;
	}

	public void setFldList(ArrayList<SFObjectFlds> fldList) {
		this.fldList = fldList;
	}

	public ArrayList<SFObjectFlds> getFldListRemote() {
		return fldListRemote;
	}

	public void setFldListRemote(ArrayList<SFObjectFlds> fldListRemote) {
		this.fldListRemote = fldListRemote;
	}

	public String getSobjectName() {
		return sobjectName;
	}

	public void setSobjectName(String sobjectName) {
		this.sobjectName = sobjectName;
	}

	public String getSobjectLabel() {
		return sobjectLabel;
	}

	public void setSobjectLabel(String sobjectLabel) {
		this.sobjectLabel = sobjectLabel;
	}

	public String getAcl_user() {
		return acl_user;
	}

	public void setAcl_user(String acl_user) {
		this.acl_user = acl_user;
	}
	
	
}
