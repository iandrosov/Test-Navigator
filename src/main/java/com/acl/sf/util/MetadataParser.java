package com.acl.sf.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.acl.io.CloudFileUtil;
import com.acl.sf.model.SFCustomLabel;
import com.acl.sf.model.SFCustomObjectTranslation;
import com.acl.sf.model.SFField;
import com.acl.sf.model.SFFieldTranslation;
import com.acl.sf.model.SFTranslation;
import com.acl.sfdc.SfUtil;

/**
 * Metadata parsing class to handle SF metadata elements
 * List of supported elements:
 * 1.Custom Labels
 * 
 * 
 * @author iandrosov
 *
 */
public class MetadataParser {

	/**
	 * Parse custom labels xml to return list of label data including all available
	 * translation if they exist to be included
	 * 
	 * @param sfdc
	 * @return ArrayList<SFCustomLabel> list of Custom Labels and their translations
	 * @throws Exception
	 */
	public ArrayList<SFCustomLabel> getCustomLabels(SfUtil sfdc) throws Exception {
		ArrayList<SFCustomLabel> lbl = new ArrayList<SFCustomLabel>();
		String meta_lable_file = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/labels/CustomLabels.labels";
		File f = new File(meta_lable_file);
		if (f.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			
			// Get all traslations
			Hashtable<String, ArrayList<SFTranslation>> transMap = getCustomLabelTranslationsMap(sfdc);
			
			// HandleCustomLabels xml parsing
			NodeList nodeCustomLabelsLst = doc.getElementsByTagName("labels"); //$NON-NLS-1$	
			for (int i = 0; i < nodeCustomLabelsLst.getLength(); i++) {
				SFCustomLabel cl = new SFCustomLabel();
				Node fstNode = nodeCustomLabelsLst.item(i);
				
				cl.setCount(i+1);
				cl.setName(MetadataParser.getTagValue("fullName", fstNode));
				cl.setBaseLanguage(MetadataParser.getTagValue("language", fstNode));
				cl.setIsProtected(MetadataParser.getBooleanValue("protected", fstNode));
				cl.setDescription(MetadataParser.getTagValue("shortDescription", fstNode));
				cl.setLabel(MetadataParser.getTagValue("value", fstNode));
				
				// Add translation
				if (transMap != null && transMap.containsKey(cl.getName())){
					cl.setTranslationList(transMap.get(cl.getName()));
				}
				
				lbl.add(cl);
			}
		}
		
		return lbl;
	}
	
	/**
	 * Create translation map from all languages based on LABEL key
	 * Grouping All translations for single label with same name key
	 * 
	 * @param sfdc
	 * @return
	 * @throws Exception
	 */
	public Hashtable<String, ArrayList<SFTranslation>> getCustomLabelTranslationsMap(SfUtil sfdc) throws Exception {
		Hashtable<String, ArrayList<SFTranslation>> trMap = new Hashtable<String, ArrayList<SFTranslation>>();
		String meta_translation_file = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/translations";
		// Read all translation files and build Label map
		File dir = new File(meta_translation_file);
		if (dir.exists() && dir.isDirectory()) {
			File[] flist = dir.listFiles();
			for (int i = 0; i < flist.length; i++) {
				File f = flist[i];
				if (f.exists() && f.isFile() && f.canRead()){
					String name = f.getName(); // This name should be like en_US.translation format
					String lang_code = getLanguageCodeFile(name);
					//System.out.println("CODE - "+lang_code);
					ArrayList<SFTranslation> transList = getCustomLabelTranslations(sfdc, lang_code);
					for (SFTranslation t : transList){
						if (trMap.containsKey(t.getName())){
							// Add to existing map
							ArrayList<SFTranslation> temp_list = trMap.get(t.getName());
							temp_list.add(t);
							trMap.put(t.getName(), temp_list);
						}else{
							// Create new labale map
							ArrayList<SFTranslation> temp_list = new ArrayList<SFTranslation>();
							temp_list.add(t);
							trMap.put(t.getName(), temp_list);							
						}
					}
					
				}
			} // END For
		}
		
		return trMap;
	}
	
	/**
	 * Initialize object translations to use in Object report
	 * @param sfdc
	 * @param objectName
	 * @throws Exception
	 */
	public boolean initObjectTranslation(SfUtil sfdc, String objectName)  throws Exception {
		ArrayList<SFCustomObjectTranslation> trnList = getObjectTranslationList(sfdc, objectName);
		if (trnList != null && trnList.size() > 0){
			for (SFCustomObjectTranslation trn : trnList){
				// Set language code
				this.addLanguage(trn.getLanguage());
				
				ArrayList<SFFieldTranslation> fields = trn.getFields();
				if (fields != null && fields.size() > 0){
					for (SFFieldTranslation f : fields){
						SFTranslation tran = new SFTranslation();
						tran.setName(f.getName());
						tran.setLanguage(f.getLanguage());
						tran.setLabel(f.getLabel());
						this.addFieldTranslation(f.getName(), tran);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	private Hashtable<String, ArrayList<SFTranslation>> htFieldTranslationMap;
	private void addFieldTranslation(String field, SFTranslation tran){
		if (this.htFieldTranslationMap == null)
			this.htFieldTranslationMap = new Hashtable<String, ArrayList<SFTranslation>>();
		
		if (this.htFieldTranslationMap.containsKey(field)){
			ArrayList<SFTranslation> trnList = this.htFieldTranslationMap.get(field);
			trnList.add(tran);
			this.htFieldTranslationMap.put(field,trnList);
		}else{
			ArrayList<SFTranslation> trnList = new ArrayList<SFTranslation>();
			trnList.add(tran);
			this.htFieldTranslationMap.put(field,trnList);			
		}	
	}
	
	public ArrayList<SFTranslation> getFieldTranslation(String field){
		if (this.htFieldTranslationMap != null && this.htFieldTranslationMap.containsKey(field)){
			return this.htFieldTranslationMap.get(field);
		}
		return null;
	}
	
	private ArrayList<String> alLangHeader;
	private void addLanguage(String lng){
		if (this.alLangHeader == null){
			this.alLangHeader = new ArrayList<String>();
		}
		this.alLangHeader.add(lng);
	}
	
	public ArrayList<String> getLanguageList(){
		return this.alLangHeader;
	}
	/**
	 * 
	 * @param sfdc
	 * @param objectName
	 * @return
	 */
	public Hashtable<String, SFCustomObjectTranslation> getObjectTranslationMap(SfUtil sfdc, String objectName)  throws Exception {
		ArrayList<String> transList = getObjectTranslationLanguages(sfdc, objectName);
		if (transList != null && transList.size() > 0){
			Hashtable<String, SFCustomObjectTranslation> map = new Hashtable<String, SFCustomObjectTranslation>();
			for (String lng : transList){
				SFCustomObjectTranslation obj_trans = getCustomObjectTranslations(sfdc, objectName, lng);
				if (obj_trans != null){
					map.put(obj_trans.getLanguage(), obj_trans);
				}
			}
				
			return map;
		}else{
			return null;
		}
	}

	public ArrayList<SFCustomObjectTranslation> getObjectTranslationList(SfUtil sfdc, String objectName)  throws Exception {
		ArrayList<String> transList = getObjectTranslationLanguages(sfdc, objectName);
		if (transList != null && transList.size() > 0){
			ArrayList<SFCustomObjectTranslation> al = new ArrayList<SFCustomObjectTranslation>();
			for (String lng : transList){
				SFCustomObjectTranslation obj_trans = getCustomObjectTranslations(sfdc, objectName, lng);
				if (obj_trans != null){
					al.add(obj_trans);
				}
			}
				
			return al;
		}else{
			return null;
		}
	}
	
	/**
	 * Return Custom Object translation for a given Object and language code ja - japanese en_US - USA English etc.
	 * 
	 * @param sfdc
	 * @param objectName
	 * @param languageCode
	 * @return
	 * @throws Exception
	 */
	public SFCustomObjectTranslation getCustomObjectTranslations(SfUtil sfdc, String objectName, String objectFile) throws Exception {
		SFCustomObjectTranslation tran = new SFCustomObjectTranslation();

		String meta_translation_file = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/objectTranslations/"+objectFile;   //objectName+"-"+languageCode+".objectTranslation";
		File f = new File(meta_translation_file);
		if (f.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			
			// Handle main object name parsing
			String languageCode = getObjectLangugeCode(objectFile);
			tran.setLanguage(languageCode);
			// Add language code to the list for headers
			tran.addLanguageCode(languageCode);
			tran.setObjectName(objectName);
			
			NodeList objLst = doc.getElementsByTagName("caseValues"); //$NON-NLS-1$	
			for (int i = 0; i < objLst.getLength(); i++) {
				Node fstNode = objLst.item(i);
				
				Boolean plural = MetadataParser.getBooleanValue("plural", fstNode);
				if (plural){
					tran.setObjectPluralLabel(MetadataParser.getTagValue("value", fstNode));
				}else{
					tran.setObjectLabel(MetadataParser.getTagValue("value", fstNode));
				}
			}
			
			
			// Handle Custom object fields xml parsing
			NodeList fieldLst = doc.getElementsByTagName("fields"); //$NON-NLS-1$	
			for (int i = 0; i < fieldLst.getLength(); i++) {
				
				Node fstNode = fieldLst.item(i);
				
				SFFieldTranslation fld_tr = new SFFieldTranslation();
				fld_tr.setLanguage(languageCode);
				fld_tr.setName(MetadataParser.getTagValue("name", fstNode));
				fld_tr.setLabel(MetadataParser.getTagValue("label", fstNode));
				fld_tr.setHelpText(MetadataParser.getTagValue("help", fstNode));
				
				//System.out.println("Object "+objectName+ " "+fld_tr.getName()+" "+fld_tr.getLabel()+" "+fld_tr.getLanguage());
				
				// Handle pick list values if found
				/*
				if (fstNode.hasChildNodes()){
				
					NodeList pickListNodeList = fstNode.getChildNodes(); //doc.getElementsByTagName("picklistValues"); //$NON-NLS-1$	
					for (int j = 0; j < pickListNodeList.getLength(); j++) {
						Node pickNode = pickListNodeList.item(j);
						if (pickNode.getNodeType() == Node.ELEMENT_NODE && pickNode.getNodeName().equals("picklistValues")){
							PickListTranslation plt = new PickListTranslation();
							plt.setMasterLabel(MetadataParser.getTagValue("masterLabel", pickNode));
							plt.setTranslation(MetadataParser.getTagValue("translation", pickNode));

							// Add Pick list translations
							fld_tr.addPickListTranslation(plt);
						}
					}
				}
				*/
				//fldLst.add(fld_tr);
				tran.addFieldTranslation(fld_tr);
			}
			
		}
		
		return tran;
	}
		
	/**
	 * Return Custom Label translation for a given language code ja - japanese en_US - USA English etc.
	 * 
	 * @param sfdc
	 * @param languageCode
	 * @return
	 * @throws Exception
	 */
	public ArrayList<SFTranslation> getCustomLabelTranslations(SfUtil sfdc, String languageCode) throws Exception {
		ArrayList<SFTranslation> tran = new ArrayList<SFTranslation>();
		
		String meta_translation_file = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/translations/"+languageCode+".translation";
		File f = new File(meta_translation_file);
		if (f.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			
			// HandleCustomLabels xml parsing
			NodeList nodeCustomLabelsLst = doc.getElementsByTagName("customLabels"); //$NON-NLS-1$	
			for (int i = 0; i < nodeCustomLabelsLst.getLength(); i++) {
				SFTranslation tr = new SFTranslation();
				Node fstNode = nodeCustomLabelsLst.item(i);
				
				tr.setLanguage(languageCode);
				tr.setName(MetadataParser.getTagValue("name", fstNode));
				tr.setLabel(MetadataParser.getTagValue("label", fstNode));
				
				tran.add(tr);
			}
		}
		
		return tran;
	}
	
	/**
	 * Return a map of Object field names found in the Object meta data
	 * This get basic meta data elements from object XML mostly used to get Description field that is not available via Describe
	 * 
	 * @param sfdc
	 * @param objectName
	 * @return
	 */
	public Hashtable<String, SFField> getMetaFieldMap(SfUtil sfdc, String objectName) throws Exception {
		Hashtable<String, SFField> ht = new Hashtable<String, SFField>();
		
		String meta_object_file = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/objects/"+objectName+".object";
		File f = new File(meta_object_file);
		if (f.exists()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(f);
			doc.getDocumentElement().normalize();
			
			// HandleCustomLabels xml parsing
			NodeList nodeFieldLst = doc.getElementsByTagName("fields"); //$NON-NLS-1$	
			for (int i = 0; i < nodeFieldLst.getLength(); i++) {
				Node fstNode = nodeFieldLst.item(i);
				
				SFField fld = new SFField();
				String api_name = MetadataParser.getTagValue("fullName", fstNode); 
				fld.setFullName(api_name);
				fld.setDescription(MetadataParser.getTagValue("description", fstNode));
				fld.setInlineHelpText(MetadataParser.getTagValue("inlineHelpText", fstNode));
				fld.setLabel(MetadataParser.getTagValue("label", fstNode));
				
				ht.put(api_name, fld);
			}
		}
		
		return ht;
	}
	
	/**
	 * Parse XML Node data to get Boolean value
	 * 
	 * @param nm
	 * @param fstNode
	 * @return
	 */
	public static Boolean getBooleanValue(String nm, Node fstNode) {
		String str = getTagValue(nm, fstNode);
		if (str != null && str.equals("true"))
			return true;
		else
			return false;
		
	}
	
	/**
	 * Xml Parsing method to get value from tag on same level
	 * 
	 * @param tag_name
	 * @param fstNode
	 * @return String with value from tag
	 */
	public static String getTagValue(String tag_name, Node fstNode)
	{
		if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
			Element fstElmnt = (Element) fstNode;
			NodeList fstNmElmntLst = fstElmnt.getElementsByTagName(tag_name); //$NON-NLS-1$
			if (fstNmElmntLst != null && fstNmElmntLst.getLength() > 0) {
				Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
				if (fstNmElmnt != null) {
					NodeList fstNm = fstNmElmnt.getChildNodes();
					if (fstNm != null && fstNm.getLength() > 0) {
						String tag_value = ((Node) fstNm.item(0))
								.getNodeValue();
						return tag_value;
					}
				}
			}
		}
		return null;
	}

	/**
	 * parse file name get front part for languge code
	 * 
	 * @param s
	 * @return
	 */
	private String getLanguageCodeFile(String s){
		String[] test = s.split("\\.");
		return test[0];
	}
	
	
	private String getObjectLangugeCode(String s){
		String[] test = s.split("-");
		if (test != null && test.length > 0){
			String[] res = test[1].split("\\.");
			return res[0];
		}else return null;
	}
	
	/**
	 * Search Object translation directory to find the list of languages
	 * available for the given object
	 * 
	 * @param sfdc
	 * @param objectName
	 * @return List of Strings with object translation file names
	 */
	private ArrayList<String> getObjectTranslationLanguages(SfUtil sfdc, String objectName){
		ArrayList<String> al = new ArrayList<String>();
		String meta_translation_dir = CloudFileUtil.getGenZipFileName(sfdc.sf_user) + "/unpackaged/objectTranslations";
		File dir = new File(meta_translation_dir);
		if (dir.exists() && dir.isDirectory()) {
			File[] flist = dir.listFiles();
			for (int i = 0; i < flist.length; i++) {
				File f = flist[i];
				if (f.exists() && f.isFile() && f.canRead()){
					String name = f.getName(); // This name should be Object translation format
					if (name.startsWith(objectName)){
						al.add(name);
					}
				}
			}
		}
		return al;
	}
}
