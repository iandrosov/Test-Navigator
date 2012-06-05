package com.acl.sf.util;

import java.io.FileWriter;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Hashtable;



import com.acl.sf.model.PickList;
import com.acl.sf.util.ObjectUtil;
import com.acl.sf.model.SFField;
import com.acl.sf.model.SFObjectStats;
import com.acl.sf.model.SFTranslation;
import com.acl.sf.model.SFObject;
import com.acl.sfdc.SfUtil;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.soap.partner.PicklistEntry;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.ws.ConnectionException;

public class ObjectScribe extends BaseManager {
	//private static final Logger logger = LoggerFactory.getLogger(ObjectScribe.class);
	
	/**
	 * Return string list of object names and their labels as text combined
	 * @param sfdc - Utility object presenting SFDC Connection or session
	 * @return List of object names found in the org
	 * @throws Exception
	 */
	public ArrayList<String> getObjectStringList(SfUtil sfdc) throws Exception {
		// Call process methods here				
		DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
		DescribeGlobalSObjectResult[] objList = dgr.getSobjects();
		ArrayList<String> objectList = new ArrayList<String>();
		for (int i = 0; i < objList.length; i++){
			String sobjectName = objList[i].getName();
			if (includeObject(sobjectName))
				objectList.add(sobjectName + " - " + objList[i].getLabel());	
		}
		return objectList;
	}

	/**
	 * Return string list of object API names as text
	 * @param sfdc - Utility object presenting SFDC Connection or session
	 * @return List of object names found in the org
	 * @throws Exception
	 */
	public ArrayList<String> getObjectAPIStringList(SfUtil sfdc) throws Exception {
		// Call process methods here				
		DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
		DescribeGlobalSObjectResult[] objList = dgr.getSobjects();
		ArrayList<String> objectList = new ArrayList<String>();
		for (int i = 0; i < objList.length; i++){
			String sobjectName = objList[i].getName();
			if (includeObject(sobjectName))
				objectList.add(sobjectName);	
		}
		return objectList;
	}
	
	/**
	 * Return list of SFObject objects result from describe call.
	 * SObjects mapped to SFObject
	 * @param sfdc
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SFObject> getObjectList(SfUtil sfdc) throws Exception {
		// Call process methods here				
		DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
		DescribeGlobalSObjectResult[] objList = dgr.getSobjects();
		ArrayList<SFObject> objectList = new ArrayList<SFObject>();
		for (int i = 0; i < objList.length; i++){
			String sobjectName = objList[i].getName();
			SFObject obj = new SFObject();
			obj.api_name = objList[i].getName();
			obj.label = objList[i].getLabel();
			obj.isCustom = objList[i].isCustom();
			if (includeObject(sobjectName))
				objectList.add(obj);	
		}
		return objectList;
	}
	
	/**
	 * Get list of objects and their fields in XML format
	 * 
	 * @param sfdc
	 * @return String XML presenting Objects
	 * @throws Exception
	 */
	public String getObjectListXML(SfUtil sfdc) throws Exception {
		String xml = "";//readXMLData(sfdc.sf_user);
		String t_xml = "<?xml version='1.0' encoding='iso-8859-1'?>\n<tree id=\"0\">\n";
		if (xml == null || (xml != null && xml.length() == 0)){
			xml = "<list>";
			DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
			DescribeGlobalSObjectResult[] objList = dgr.getSobjects();
			for (int i = 0; i < objList.length; i++){
				String type_name = objList[i].getName();
				xml += "	<object title=\""+type_name+"-"+objList[i].getLabel()+"\" code=\""+type_name+"\">";
				
				if (i == 0){
					t_xml +="	<item text=\""+type_name+"-"+objList[i].getLabel()+"\" id=\""+type_name+"\" im0=\"database.png\" im1=\"database_gear.png\" im2=\"database.png\" call=\"1\" select=\"1\">\n";
					
				}else{	
					// Test node
					t_xml +="	<item text=\""+type_name+"-"+objList[i].getLabel()+"\" id=\""+type_name+"\" im0=\"database.png\" im1=\"database_gear.png\" im2=\"database.png\">\n";
				}
				// Handle Object fileds here
				SFObject sfo = this.getObjectMetadata(sfdc,type_name);
				xml += "		<fields title=\"Fields\">";
				
				// Test node
				t_xml += "		<item text=\"Fields\" id=\"fld_"+ Integer.toString(i) +"\" im0=\"database_table.png\" im1=\"table_gear.png\" im2=\"database_table.png\">\n";
				
				for (int j = 0; j < sfo.fld_arr.length; j++){
					xml += "		<field label=\""+sfo.fld_arr[j].getLabel()+"\" name=\""+sfo.fld_arr[j].getName()+"\" sobj=\""+type_name+"\"/>";
				
					// Test node
					t_xml += "			<item text=\""+sfo.fld_arr[j].getLabel()+"\" id=\""+ sfo.fld_arr[j].getName() +"\" im0=\"table.png\" im1=\"table.png\" im2=\"table.png\"/>\n";
				}
				xml += "		</fields>\n	</object>";
				
				// test node
				t_xml += "		</item>\n";
				t_xml += "	</item>\n";
			}		
			xml += "</list>";
			
			// Save file for chache
			saveXMLData(xml, sfdc.sf_user);
		}
		t_xml += "</tree>";
		FileWriter fw1 = new FileWriter("/Users/iandrosov/test_"+getFileName(sfdc.sf_user));
		fw1.write(t_xml);
		fw1.flush();
		fw1.close();

		return xml;
	}
	
	/**
	 * Get tree list of objects and their API names in XML format.
	 * The list does not include fields, they will be added dynamically as ajax requests
	 * 
	 * @param sfdc
	 * @return String XML presenting Objects
	 * @throws Exception
	 */
	public String getObjectTreeXML(SfUtil sfdc) throws Exception {
		
		String t_xml = "<?xml version='1.0' encoding='UTF-8'?>\n<tree id=\"0\">\n";
			DescribeGlobalResult dgr = sfdc.getConnection().describeGlobal();
			DescribeGlobalSObjectResult[] objList = dgr.getSobjects();
			for (int i = 0; i < objList.length; i++){
				String type_name = objList[i].getName();
				//String enc_label = java.net.URLEncoder.encode(objList[i].getLabel(), "UTF8");
				String enc_label = forXML(objList[i].getLabel());
				if (i == 0){
					t_xml +="	<item text=\""+type_name+"-"+enc_label+"\" id=\""+type_name+"\" im0=\"database.png\" im1=\"database_gear.png\" im2=\"database.png\" call=\"1\" select=\"1\" child=\"1\">\n";					
				}else{	
					t_xml +="	<item text=\""+type_name+"-"+enc_label+"\" id=\""+type_name+"\" im0=\"database.png\" im1=\"database_gear.png\" im2=\"database.png\" child=\"1\">\n";
				}
				
				t_xml += "		<item text=\"Fields\" id=\"fldNode_"+ type_name +"\" im0=\"database_table.png\" im1=\"table_gear.png\" im2=\"database_table.png\" child=\"1\">\n";

				// Add Dummy Field node to enable tree exapansion event for ajax request
				//t_xml += "			<item text=\"Field Name\" id=\""+ Integer.toString(i) +"\" im0=\"table.png\" im1=\"table.png\" im2=\"table.png\"/>\n";
				/** Hold on to 
				SFObject sfo = sfdc.getObjectMetadata(type_name);

				for (int j = 0; j < sfo.fld_arr.length; j++){
					t_xml += "			<item text=\""+sfo.fld_arr[j].getLabel()+"\" id=\""+ sfo.fld_arr[j].getName() +"\" im0=\"table.png\" im1=\"table.png\" im2=\"table.png\"/>\n";
				}
				**/
				// test node
				t_xml += "		</item>\n";
				t_xml += "	</item>\n";
			}		
			
		
		t_xml += "</tree>";

		return t_xml;
	}
	
	/**
	 * Get tree list of fields for single object and their API names in XML format.
	 * Fields will be added dynamically as part of ajax requests to parent node.
	 * The partial xml need to have tree as root node.
	 * 
	 * @param sfdc
	 * @param 
	 * @return String XML presenting Objects
	 * @throws Exception
	 */
	public String getFieldTreeXML(SfUtil sfdc, String object_type) throws Exception {
		String t_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		
		t_xml += "<tree id=\"fldNode_"+ object_type +"\">\n"; //   <item text=\"Fields\" id=\"fldNode_"+ object_type +"\" im0=\"database_table.png\" im1=\"table_gear.png\" im2=\"database_table.png\" child=\"1\">\n";

		// Handle Object fileds here
		SFObject sfo = this.getObjectMetadata(sfdc,object_type);
		if (sfo != null){
			for (int j = 0; j < sfo.fld_arr.length; j++){
				//String enc_label = java.net.URLEncoder.encode(sfo.fld_arr[j].getLabel(), "UTF8");
				String enc_label = sfo.fld_arr[j].getName() +"-"+ forXML(sfo.fld_arr[j].getLabel());
				t_xml += "	<item text=\""+enc_label+"\" id=\"fld_"+ object_type + "." + sfo.fld_arr[j].getName() +"\" im0=\"table.png\" im1=\"table.png\" im2=\"table.png\"/>\n";
			}
		}
		
		t_xml += "</tree>";
		
		//FileWriter fw1 = new FileWriter("/Users/iandrosov/test_"+getFileName(sfdc.sf_user));
		//fw1.write(t_xml);
		//fw1.flush();
		//fw1.close();
		
		return t_xml;
		
	}
	
	/**
	   Escape characters for text appearing as XML data, between tags.
	   
	   <P>The following characters are replaced with corresponding character entities :
	   <table border='1' cellpadding='3' cellspacing='0'>
	   <tr><th> Character </th><th> Encoding </th></tr>
	   <tr><td> < </td><td> &lt; </td></tr>
	   <tr><td> > </td><td> &gt; </td></tr>
	   <tr><td> & </td><td> &amp; </td></tr>
	   <tr><td> " </td><td> &quot;</td></tr>
	   <tr><td> ' </td><td> &#039;</td></tr>
	   </table>
	   
	   <P>Note that JSTL's {@code <c:out>} escapes the exact same set of 
	   characters as this method. <span class='highlight'>That is, {@code <c:out>}
	    is good for escaping to produce valid XML, but not for producing safe 
	    HTML.</span>
	  */
	  private String forXML(String aText){
	    final StringBuilder result = new StringBuilder();
	    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
	    char character =  iterator.current();
	    while (character != CharacterIterator.DONE ){
	      if (character == '<') {
	        result.append("&lt;");
	      }
	      else if (character == '>') {
	        result.append("&gt;");
	      }
	      else if (character == '\"') {
	        result.append("&quot;");
	      }
	      else if (character == '\'') {
	        result.append("&#039;");
	      }
	      else if (character == '&') {
	         result.append("&amp;");
	      }
	      else {
	        //the char is not a special one
	        //add it to the result as is
	        result.append(character);
	      }
	      character = iterator.next();
	    }
	    return result.toString();
	  }
	private String getFileName(String org){
		String s = "xml_data_";
		String tmp = org.replaceAll("@", "_");
		return s+tmp+".xml";
	}
	
	private void saveXMLData(String xml, String org) throws Exception{
		FileWriter fw = new FileWriter(getFileName(org));
		fw.write(xml);
		fw.flush();
		fw.close();
		
	}
	/*
	private String readXMLData(String org) throws Exception{
		String filePath = getFileName(org);
		File ff = new File(filePath);
		if (!ff.exists())
			return null;
		
		byte[] buffer = new byte[(int) new File(filePath).length()];
	    BufferedInputStream f = null;
	    try {
	        f = new BufferedInputStream(new FileInputStream(filePath));
	        f.read(buffer);
	    } finally {
	        if (f != null) try { f.close(); } catch (IOException ignored) { }
	    }
	    return new String(buffer);		
	}
	*/
	/**
	 * return String list of Apex class names
	 * 
	 * @param sfdc
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> getClassList(SfUtil sfdc) throws Exception {
		//logger.info("Start Apex Class list." );
		ArrayList<String> classList = new ArrayList<String>();
		String queryString = "Select a.Status, a.NamespacePrefix, a.Name, a.LengthWithoutComments, a.LastModifiedDate, a.IsValid, a.Id, a.CreatedById, a.CreatedBy.Name, a.CreatedBy.FirstName, a.CreatedBy.LastName, a.CreatedBy.Username, a.ApiVersion From ApexClass a ORDER BY a.Name";
		QueryResult qr = sfdc.getConnection().query(queryString);
		if (qr != null){
			SObject[] sobjList = qr.getRecords();
			for (int i = 0; i < sobjList.length; i++){
				SObject sobj = sobjList[i];
				String str = sobj.getField("Name").toString();
				classList.add(str);
			}
		}
		return classList;
	}
	
	/**
	 * Filter Query SOQL string and convert SELECT * into fields full query
	 * 
	 * @param sfdc
	 * @param qry
	 * @return
	 */
	public String filterQueryString(SfUtil sfdc, String qry) throws Exception{
		String result_query = qry;
		if (qry != null && qry.length() > 10 && sfdc != null && sfdc.isPing_status()){
			int start = 7;
			int end = qry.indexOf("FROM");
			String test = qry.substring(start, end);
			if (test.indexOf("*") != -1){ // Process query
				int idx = qry.indexOf("FROM");
				String tmp = qry.substring(idx+5, qry.length());
				int obj_end_idx = tmp.trim().indexOf(" ");
				String obj_name = tmp.substring(0, obj_end_idx);
	
				String fld = "";
				SFObject sfo = this.getObjectMetadata(sfdc,obj_name);
				if (sfo != null){
					for (int j = 0; j < sfo.fld_arr.length; j++){
						if (j > 0)
						    fld += ",";
						fld += sfo.fld_arr[j].getName();
					}
				}
				
				result_query = "SELECT "+fld+" FROM "+tmp;
			}
		}
		return result_query;
	}
	
	/**
	 * Filter standard unchangable objects form the list like Attachment, Notes, History object Share object etc.
	 * these object added by SFDC automatically and cannot be modified or customized.
	 * 
	 * @param obj_name - String API name of an object 
	 * @return - Boolean true/false is object included into display list or filtered out
	 */
	public Boolean includeObject(String obj_name){
    		if (obj_name.equals("Attachment") || obj_name.equals("Note") || obj_name.equals("ProcessInstance") 
    			|| obj_name.equals("ProcessInstanceHistory") || obj_name.equals("NoteAndAttachment")
    			|| obj_name.equals("Event") || obj_name.equals("Task") || obj_name.equals("OpenActivity")
    			|| obj_name.equals("ActivityHistory") || obj_name.endsWith("__Share") || obj_name.endsWith("__History")
    			|| obj_name.endsWith("History") || obj_name.endsWith("Share") || obj_name.endsWith("Feed")){   		
    			return false;
    		}
    	return true;
    }	
	
	/**
	 * Return Object metadata field list, types, picklists, translations.
	 * 
	 * @param sObjectType
	 * @param std
	 * @return
	 * @throws ConnectionException
	 */
	public SFObject getObjectMetadata(SfUtil sfdc, String sObjectType, String std) throws ConnectionException, Exception {
		if (sfdc != null && sfdc.connection != null && sObjectType != null) // && sfdc.isPing_status())
		{
			MetadataParser mp = new MetadataParser();
			Hashtable<String, SFField> fldMap = mp.getMetaFieldMap(sfdc, sObjectType);
			
			DescribeSObjectResult dsor = sfdc.connection.describeSObject(sObjectType);
			Field[] fld_arr = dsor.getFields();
			SFObject obj = new SFObject();
			obj.setLabel( dsor.getLabel() );
			obj.setApi_name(dsor.getName());
			obj.setFld_arr(fld_arr);
			obj.setFields(fldarray2fieldlist(fld_arr, std, fldMap));
			obj.setIsCustom(dsor.isCustom());
			obj.setFieldStats(getObjectStats(fld_arr));

			// Add translation data if available for object
			if (mp.initObjectTranslation(sfdc, sObjectType)){

				obj.setOptionHeaders(mp.getLanguageList());
				
				// Build field translation map
				ArrayList<SFField> fldList = obj.getFields();
				for (SFField f : fldList){
					ArrayList<SFTranslation> trnList = mp.getFieldTranslation(f.getFullName());
					if (trnList != null && trnList.size() > 0){
						f.setTranslationList(trnList);
					}else{
						if (mp.getLanguageList() != null){
							trnList = new ArrayList<SFTranslation>();
							for (int i = 0; i < mp.getLanguageList().size(); i++){
								SFTranslation t = new SFTranslation();
								trnList.add(t);
							}
							f.setTranslationList(trnList);
						}
					}
				}
				obj.setFields(fldList);
				
			}
			
			return obj;
		}		
		return null;
	}
	
	public SFObject getObjectMetadata(SfUtil sfdc,String sObjectType) throws ConnectionException {
		if (sfdc != null && sfdc.connection != null && sObjectType != null) // && sfdc.isPing_status())
		{
			DescribeSObjectResult dsor = sfdc.connection.describeSObject(sObjectType);
			Field[] fld_arr = dsor.getFields();
			SFObject obj = new SFObject();
			obj.setLabel( dsor.getLabel() );
			obj.setApi_name(dsor.getName());
			obj.setFld_arr(fld_arr);
			obj.setFields(fldarray2fieldlist(fld_arr));
			obj.setIsCustom(dsor.isCustom());
			obj.setFieldStats(getObjectStats(fld_arr));
			return obj;
		}		
		return null;
	}

	private SFObjectStats getObjectStats(Field[] fld_arr){
		SFObjectStats stat = new SFObjectStats ();
		stat.totalFields = fld_arr.length;
		for (int i = 0; i < fld_arr.length; i++){
			 if (fld_arr[i].isCalculated())
				 stat.totalFormulaFields++;
			 else{				 
				 if(fld_arr[i].getType() == FieldType.picklist)stat.totalPicklistFields++;
				 if(fld_arr[i].getType() == FieldType._boolean)stat.totalBooleanFields++;
				 if(fld_arr[i].getType() == FieldType.multipicklist)stat.totalPicklistFields++;
				 if(fld_arr[i].getType() == FieldType.string)stat.totalStringFields++;
				 if(fld_arr[i].getType() == FieldType.date)stat.totalDateFields++;
				 if(fld_arr[i].getType() == FieldType.reference)stat.totalReferenceFields++;
				 if(fld_arr[i].getType() == FieldType.datetime)stat.totalDateTimeFields++;
				 if(fld_arr[i].getType() == FieldType._double)stat.totalNumberFields++;
				 if(fld_arr[i].getType() == FieldType.email)stat.totalEmailFields++;
				 if(fld_arr[i].getType() == FieldType._int)stat.totalNumberFields++;
				 if(fld_arr[i].getType() == FieldType.base64)stat.totalBASE64Fields++;
				 if(fld_arr[i].getType() == FieldType.id)stat.totalIDFields++;		 
				 if(fld_arr[i].getType() == FieldType.currency)stat.totalCurrencyFields++;
				 if(fld_arr[i].getType() == FieldType.textarea)stat.totalTextAreaFields++;
				 if(fld_arr[i].getType() == FieldType.url)stat.totalURLFields++;
				 if(fld_arr[i].getType() == FieldType.phone)stat.totalPhoneFields++;
				 if(fld_arr[i].getType() == FieldType.percent)stat.totalPercentFields++;
				 if(fld_arr[i].getType() == FieldType.combobox)stat.totalComboboxFields++;
				 if(fld_arr[i].getType() == FieldType.time)stat.totalTimeFields++;		 
			 }
		}
		return stat;
	}
	private ArrayList<SFField> fldarray2fieldlist(Field[] fld_arr) {
		ArrayList<SFField> fld_lst = new ArrayList<SFField>();
		int count = 1;
		for (int i = 0; i < fld_arr.length; i++){
			if (!ObjectUtil.isStandardField(fld_arr[i].getName())){
				SFField fld = mapField(fld_arr[i]);
				fld.setCounter(count);

				// Map all fields
				fld.setDescription(fld_arr[i].getInlineHelpText()); // Set description to value of inline help because SF do not provide description API
				fld.setInlineHelpText(fld_arr[i].getInlineHelpText());
				fld.setIsAutoNumber(fld_arr[i].getAutoNumber());
				
				fld_lst.add(fld);
				count++;
			}
		}
		return fld_lst;
	}
	
	private ArrayList<SFField> fldarray2fieldlist(Field[] fld_arr, String std, Hashtable<String, SFField> ext_map) {
		ArrayList<SFField> fld_lst = new ArrayList<SFField>();
		int count = 1;
		for (int i = 0; i < fld_arr.length; i++){
			if (!ObjectUtil.isStandardField(fld_arr[i].getName(), std)){
				SFField fld = mapField(fld_arr[i]);
				fld.setCounter(count);

				// Map all extended fields
				if (ext_map != null && ext_map.containsKey(fld_arr[i].getName())){
					SFField ext_fld = ext_map.get(fld_arr[i].getName());
					if (ext_fld != null){
						fld.setDescription(ext_fld.getDescription());
						if (ext_fld.getTranslationList() != null){
							fld.setTranslationList(ext_fld.getTranslationList());
						}
					}
				}else{
					fld.setDescription(fld_arr[i].getInlineHelpText()); // Set description to value of inline help because SF do not provide description API
				}
				fld_lst.add(fld);
				count++;
			}
		}
		return fld_lst;
	}
	
	private SFField mapField(Field f){
		SFField fld = new SFField();
		fld.setFullName(f.getName());
		fld.setLabel(f.getLabel());
		fld.setTypeName(f.getType().name());
		fld.setLength(f.getLength());
		fld.setRelationshipName(getRelationshipName(f));
		fld.setInlineHelpText(f.getInlineHelpText());
		fld.setIsAutoNumber(f.getAutoNumber());
		fld.setByteLength(f.getByteLength());
		fld.setDigits(f.getDigits());
		fld.setScale(f.getScale());
		fld.setPrecision(f.getPrecision());
		
		// System.out.println(fld.getTypeName()+" - "+Integer.toString(f.getLength())+" "+Integer.toString(f.getByteLength())+" "+Integer.toString(f.getScale())+" "+Integer.toString(f.getPrecision())+" "+Integer.toString(f.getDigits()));
		
		if (fld.getTypeName().equalsIgnoreCase("picklist") || fld.getTypeName().equalsIgnoreCase("multipicklist"))
			fld.setPickList(addPicklistValues(f));
		
		return fld;
	}
	private String getRelationshipName(Field f){
		String nm = "";
		if (f != null){
			if (f.getRelationshipName() != null){
				String relate_order = "[Child]";
				if (f.getRelationshipOrder() == 0)
					relate_order = "[Parent]";							
				nm = f.getRelationshipName()+" "+relate_order;
			}else{
				nm = f.getRelationshipName();
			}						
		}
		return nm;
	}
	
	/**
	 * Default Row for picklist values map picklist to default columns
	 * 
	 * @param row_num
	 * @param row_counter
	 * @param fld
	 * @param sheet
	 * @return
	 */
	private ArrayList<PickList> addPicklistValues(Field fld)
	{
		ArrayList<PickList> al = new ArrayList<PickList>();
		PicklistEntry[] ple_arr = fld.getPicklistValues();
		for (int i = 0; i < ple_arr.length; i++){
			PickList pl = new PickList();
			pl.fullName = ple_arr[i].getLabel();
			pl.value = ple_arr[i].getValue();
			al.add(pl); //(ple_arr[i].getLabel()+"-"+ple_arr[i].getValue());
		}
		return al;
	}
	
}
