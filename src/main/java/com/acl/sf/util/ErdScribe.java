package com.acl.sf.util;

import java.util.ArrayList;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acl.sfdc.SfUtil;
import com.sforce.soap.partner.ChildRelationship;
import com.sforce.soap.partner.DescribeSObjectResult;

public class ErdScribe {
	private static final Logger logger = LoggerFactory.getLogger(ObjectScribe.class);
	
	private ArrayList<String> xmlDataNode;
	private String sobjectNameStringList;
	private Boolean isIncludeStandardObjects = false;
	
	 public ArrayList<String> getXMLDataItems(SfUtil sfdc) throws Exception {
		 logger.info("Call ErdScribe: getXMLDataItems" );
	        this.xmlDataNode = new ArrayList<String>();
	        String start_xml = "<Graph>";
	        this.xmlDataNode.add(start_xml);
	        String root = "<Node id=\"1\" name=\"Force.com\" desc=\"Force.com\" nodeColor=\"0x333333\" nodeSize=\"32\" nodeClass=\"earth\" nodeIcon=\"center\" x=\"10\" y=\"10\" />";
	        this.xmlDataNode.add(root);
	        
	        Hashtable<String,String> ht_object = new Hashtable<String,String>();
	        //Doc_MetaInfo m = new Doc_MetaInfo() ;
	        //List<String> names = m.getObjectNameList() ;
	        //names.sort(); 
	        //Map<String, String> objectNLMap = m.objectNameLableMap;
	        Integer y_pos = 10;
	        //Integer i = 0;
	        // parse ; string to get all object names
	        ArrayList<String> tempArray = new ArrayList<String>();
	        if (sobjectNameStringList != null){
	        	String[] nameArray = sobjectNameStringList.split(";");
	        
	        	for(Integer i = 0 ; i < nameArray.length ; i++ ){
	        		if (nameArray[i] != null && nameArray[i].length() > 0){
	        			String tmp = cleanObjectName(nameArray[i]);
	        			tempArray.add(tmp);
	        			ht_object.put(tmp, tmp);
	        		}
	        	}
	        }
	        //tempArray.sort();
	        for(Integer i = 0 ; i < tempArray.size() ; i++ ){	
	        	String obj_name = tempArray.get(i);
	        	String obj_display = obj_name;

	        	String xml_data = "<Node id=\""+obj_name+"\" name=\""+obj_display+"\" desc=\""+obj_name+"\" nodeColor=\"0x8F8FFF\" nodeSize=\"12\" nodeClass=\"tree\" nodeIcon=\"2\" x=\"10\" y=\""+y_pos+"\" />";
				this.xmlDataNode.add(xml_data);
	 			//String xml_root_link = '<Edge fromID="1" toID="'+obj_name+'" edgeLabel="(Default)" flow="80" color="0xb22222" edgeClass="sun" edgeIcon="Good" />';
	 			
	 			String xml_root_link = "<Edge fromID=\"1\" toID=\""+obj_name+"\" edgeLabel=\"(Default)\" flow=\"80\" color=\"0x657383\" edgeClass=\"sun\" edgeIcon=\"Good\" />";
	 			this.xmlDataNode.add(xml_root_link);
	 				           
	            // Build related node links
	            String root_object_nameId = obj_name;

            
	            // Map<String, Schema.SObjectType> gd = Schema.getGlobalDescribe(); 
	            //DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
	            //Schema.SObjectType obj = gd.get(obj_name);
	            DescribeSObjectResult obj = sfdc.getConnection().describeSObject(obj_name);
	            if (obj != null){
	            	//Schema.DescribeSObjectResult R = obj.getDescribe();	            	
	           	 	//List<Schema.Childrelationship> scrList = R.getChildRelationships();
	           	 	ChildRelationship[] cr_arr = obj.getChildRelationships();
	            	//for (Schema.Childrelationship scr : scrList){
	            	for (int j = 0; j < cr_arr.length; j++){	
		            	//Schema.SObjectType sot = scr.getChildSObject();
		            	String child_name = cr_arr[j].getChildSObject();
		            	//Schema.DescribeSObjectResult dsor = sot.getDescribe();
		            	String related_object_nameId = child_name; //dsor.getName();
		            	if (includeObject(related_object_nameId, isIncludeStandardObjects) && ht_object.containsKey(child_name)){
							//String relation_name = scr.getRelationshipName();
							String relation_name = cr_arr[j].getRelationshipName();
							//if (scr.isCascadeDelete())
							if (cr_arr[j].isCascadeDelete())
								relation_name = "MasterDetail";
							else
								relation_name = "Lookup";
			            	String xml_data_child_node = "<Node id=\""+related_object_nameId+"\" name=\""+related_object_nameId+"\" desc=\""+related_object_nameId+"\" nodeColor=\"0x4E8975\" nodeSize=\"12\" nodeClass=\"tree\" nodeIcon=\"2\" x=\"10\" y=\""+y_pos+"\" />";
		        			this.xmlDataNode.add(xml_data_child_node);
		            		String xml_link = "<Edge fromID=\""+root_object_nameId+"\" toID=\""+related_object_nameId+"\" edgeLabel=\""+relation_name+"\" flow=\"80\" color=\"0x2B60DE\" edgeClass=\"sun\" edgeIcon=\"Good\" />";
		            		this.xmlDataNode.add(xml_link);
		            		y_pos = y_pos+5;
		            	}
	            	}
	            } 
	        }   
       
	        String end_xml = "</Graph>";
	        this.xmlDataNode.add(end_xml);
	        return this.xmlDataNode;
	    }
	    
	 private String cleanObjectName(String on){
		 String obj_display = on;
		 if (on != null){
			 String obj_name = on.trim();
			 obj_display = obj_name;
	        	if (obj_name.endsWith(";"))
	        		obj_display = obj_name.substring(0, obj_name.length()-1);
	        	if (obj_name.startsWith(";"))
	        		obj_display = obj_name.substring(1, obj_name.length());	

		 }
		 return obj_display;
	 }
	    public String getXmldata(){
	    	String xml_str = "";
	    	for (String str : this.xmlDataNode)
	    		xml_str += str;
	    	return xml_str;	
	    }
	    public Boolean includeObject(String obj_name, Boolean flag){
	    	if (!flag){
	    		if (obj_name.equals("Attachment") || obj_name.equals("Note") || obj_name.equals("ProcessInstance") 
	    			|| obj_name.equals("ProcessInstanceHistory") || obj_name.equals("NoteAndAttachment")
	    			|| obj_name.equals("Event") || obj_name.equals("Task") || obj_name.equals("OpenActivity")
	    			|| obj_name.equals("ActivityHistory") || obj_name.endsWith("__Share") || obj_name.endsWith("__History"))   		
	    		return false;
	    	}
	    	return true;
	    }

		public String getSobjectNameStringList() {
			return sobjectNameStringList;
		}

		public void setSobjectNameStringList(String sobjectNameStringList) {
			this.sobjectNameStringList = sobjectNameStringList;
		}

		public Boolean getIsIncludeStandardObjects() {
			return isIncludeStandardObjects;
		}

		public void setIsIncludeStandardObjects(Boolean isIncludeStandardObjects) {
			this.isIncludeStandardObjects = isIncludeStandardObjects;
		}
	
}
