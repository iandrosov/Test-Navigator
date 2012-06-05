package com.acl.sf.soql;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.sforce.ws.bind.XmlObject;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;

public class SoqlQueryTool {

	private PartnerConnection connection = null;

	public SoqlQueryTool(PartnerConnection conn){
		this.connection = conn;
	}
	public PartnerConnection getConnection() {
		return connection;
	}

	public void setConnection(PartnerConnection connection) {
		this.connection = connection;
	}

	/**
	 * Run SOQL Query on Partner connection and parse generic result
	 * of SObjects handle fields based on defined query and XmlObjects returned by service call
	 *  
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public SFQueryResult queryRecords(String queryString) throws Exception {
		ArrayList<String> columnHeaderList = new ArrayList<String>();
		ArrayList<SFDisplayRow> rowData = new ArrayList<SFDisplayRow>();
		// Get current time
		long start = System.currentTimeMillis();
		QueryResult qr = this.getConnection().query(queryString);
		System.out.println("Query Executed: "+queryString);
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		int row_size = 0;
		if (qr != null) {
			// Get the size of result
			if (qr.getSize() > 0) {
				row_size = qr.getSize();
			}
			
			columnHeaderList = getFieldNamesFromQuery(queryString);
			SObject[] sobjList = qr.getRecords();
			for (int i = 0; i < sobjList.length; i++){
				SObject sobj = sobjList[i];
				
				SFDisplayRow row = new SFDisplayRow();
				row.setRowCount(i+1);
				Hashtable<String,String> ht_row_data = new Hashtable<String,String>();
				Hashtable<String,String> ht_dup = new Hashtable<String,String>();
				Iterator<XmlObject> xobj = sobj.getChildren();
				while (xobj.hasNext()){
					XmlObject o = xobj.next();
					QName qn = o.getName();
					String type_name = qn.getLocalPart();
					//System.out.println("LocalPart: "+type_name+" QN: "+qn.toString());
					if (o.getValue() != null && !type_name.equals("type") && !ht_dup.containsKey(type_name)){
						//row.add(o.getValue().toString());
						ht_dup.put(qn.getLocalPart(), qn.getLocalPart());
						ht_row_data.put(type_name.toLowerCase(), o.getValue().toString());
					}
					
					
					String sub_type_name = "";
					
					// Get sub query element by relationship Level - 1
					if (type_name != null && o.getValue() == null){	
						XmlObject sub_xobj = (XmlObject)sobj.getField(type_name);
						if (sub_xobj != null){
							Hashtable<String,String> ht_sub_dup = new Hashtable<String,String>();
							Iterator<XmlObject> sub_qry = sub_xobj.getChildren();	
							while (sub_qry.hasNext()){
								XmlObject o1 = sub_qry.next();
								QName qn1 = o1.getName();
								sub_type_name = qn1.getLocalPart();
								//System.out.println("SUB LocalPart: "+type_name+" QN: "+qn1.toString());
								if (o1.getValue() != null && !sub_type_name.equals("type") && !ht_sub_dup.containsKey(sub_type_name)){
									//row.add(o1.getValue().toString());
									ht_sub_dup.put(qn1.getLocalPart(), qn1.getLocalPart());
									String sub_key = type_name+"."+sub_type_name;
									ht_row_data.put(sub_key.toLowerCase(), o1.getValue().toString());
									//System.out.println("SUB Inner LocalPart: "+type_name+" QN: "+qn1.toString()+" Data: "+o1.getValue().toString());
								}
								
								// Get sub query element by relationship Level - 2
								String sub_type_name2 = "";
								if (sub_type_name != null && o1.getValue() == null){
									XmlObject sub_xobj2 = (XmlObject)sub_xobj.getField(sub_type_name);
									if (sub_xobj2 != null){
										//Hashtable<String,String> ht_sub2_dup = new Hashtable<String,String>();
										Iterator<XmlObject> sub_qry2 = sub_xobj2.getChildren();
										while (sub_qry2.hasNext()){
											XmlObject o2 = sub_qry2.next();
											QName qn2 = o2.getName();
											sub_type_name2 = qn2.getLocalPart();
											//System.out.println("SUB2 LocalPart: "+sub_type_name2+" QN: "+qn2.toString());
											if (o2.getValue() != null && !sub_type_name2.equals("type")) { // && !ht_sub2_dup.containsKey(sub_type_name2)){
												//ht_sub2_dup.put(qn2.getLocalPart(), qn2.getLocalPart());
												String sub_key2 = type_name+"."+sub_type_name+"."+sub_type_name2;
												ht_row_data.put(sub_key2.toLowerCase(), o2.getValue().toString());
												//System.out.println("SUB2 Inner LocalPart: "+sub_type_name2+" QN2: "+qn2.toString()+" Data2: "+o2.getValue().toString());
											}
											
											// Get sub query element by relationship Level - 3
											String sub_type_name3 = "";
											if (sub_type_name2 != null && o2.getValue() == null){
												XmlObject sub_xobj3 = (XmlObject)sub_xobj2.getField(sub_type_name2);
												if (sub_xobj3 != null){
													//Hashtable<String,String> ht_sub3_dup = new Hashtable<String,String>();
													Iterator<XmlObject> sub_qry3 = sub_xobj3.getChildren();
													while (sub_qry3.hasNext()){
														XmlObject o3 = sub_qry3.next();
														QName qn3 = o3.getName();
														sub_type_name3 = qn3.getLocalPart();
														//System.out.println("SUB3 LocalPart: "+sub_type_name3+" QN: "+qn3.toString());
														if (o3.getValue() != null && !sub_type_name3.equals("type")) { // && !ht_sub3_dup.containsKey(sub_type_name3)){
															//ht_sub3_dup.put(qn3.getLocalPart(), qn3.getLocalPart());
															String sub_key3 = type_name+"."+sub_type_name+"."+sub_type_name2+"."+sub_type_name3;
															ht_row_data.put(sub_key3.toLowerCase(), o3.getValue().toString());
															//System.out.println("SUB3 Inner LocalPart: "+sub_type_name3+" QN3: "+qn2.toString()+" Data3: "+o3.getValue().toString());
														}
														
														// Get sub query element by relationship Level - 4
														String sub_type_name4 = "";
														if (sub_type_name3 != null && o3.getValue() == null){
															XmlObject sub_xobj4 = (XmlObject)sub_xobj3.getField(sub_type_name3);
															if (sub_xobj4 != null){
																//Hashtable<String,String> ht_sub4_dup = new Hashtable<String,String>();
																Iterator<XmlObject> sub_qry4 = sub_xobj4.getChildren();
																while (sub_qry4.hasNext()){
																	XmlObject o4 = sub_qry4.next();
																	QName qn4 = o4.getName();
																	sub_type_name4 = qn4.getLocalPart();
																	//System.out.println("SUB4 LocalPart: "+sub_type_name4+" QN4: "+qn4.toString());
																	if (o4.getValue() != null && !sub_type_name4.equals("type")) { // && !ht_sub4_dup.containsKey(sub_type_name4)){
																		//ht_sub4_dup.put(qn4.getLocalPart(), qn4.getLocalPart());
																		String sub_key4 = type_name+"."+sub_type_name+"."+sub_type_name2+"."+sub_type_name3+"."+sub_type_name4;
																		ht_row_data.put(sub_key4.toLowerCase(), o4.getValue().toString());
																	}
																	
																	// Get sub query element by relationship Level - 5
																	String sub_type_name5 = "";
																	if (sub_type_name4 != null && o4.getValue() == null){
																		XmlObject sub_xobj5 = (XmlObject)sub_xobj4.getField(sub_type_name4);
																		if (sub_xobj4 != null){
																			Iterator<XmlObject> sub_qry5 = sub_xobj5.getChildren();
																			while (sub_qry5.hasNext()){
																				XmlObject o5 = sub_qry5.next();
																				QName qn5 = o5.getName();
																				sub_type_name5 = qn5.getLocalPart();
																				if (o5.getValue() != null && !sub_type_name5.equals("type")) {
																					String sub_key5 = type_name+"."+sub_type_name+"."+sub_type_name2+"."+sub_type_name3+"."+sub_type_name4+"."+sub_type_name5;
																					ht_row_data.put(sub_key5.toLowerCase(), o5.getValue().toString());
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}							
					}
				}
				
				// Create row data form parsed
				for (String hdr : columnHeaderList){
					String key = hdr.toLowerCase();
					if (ht_row_data.containsKey(key)){
						row.add((String)ht_row_data.get(key));
					}else{
						row.add("");
					}
				}
				
				rowData.add(row);
				ht_row_data.clear();
			}
			// Print stuff out
			/*
			String str = "";
			str = "";
			for (String s : columnHeaderList)
				str += s + "     ";
			System.out.println(str);
			for (SFDisplayRow dr : rowData)
				dr.print();
				*/
		}
		SFQueryResult sfqr = new SFQueryResult();
		sfqr.setColumnHeaderList(columnHeaderList);
		sfqr.setRowData(rowData);
		if (rowData != null)
			sfqr.setDisplayResultCount(rowData.size());
		sfqr.setResultCount(row_size);
		sfqr.setQueryTime(Float.toString(elapsedTimeSec));
		return sfqr;
	}

	/**
	 * Run SOQL Query on Partner connection and parse generic result
	 * of SObjects handle fields based on defined query and XmlObjects returned by service call
	 *  
	 * @param queryString
	 * @return
	 * @throws Exception
	 */
	public SFQueryResult queryAllRecords(String queryString) throws Exception {
		ArrayList<String> columnHeaderList = new ArrayList<String>();
		ArrayList<SFDisplayRow> rowData = new ArrayList<SFDisplayRow>();
		// Get current time
		long start = System.currentTimeMillis();
		QueryResult qr = this.getConnection().queryAll(queryString);
		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		int row_size = 0;
		if (qr != null){
			
			// Get the size of result
			if (qr.getSize() > 0) {
				row_size = qr.getSize();
			}
			
			columnHeaderList = getFieldNamesFromQuery(queryString);
			SObject[] sobjList = qr.getRecords();
			for (int i = 0; i < sobjList.length; i++){
				SObject sobj = sobjList[i];
				
				SFDisplayRow row = new SFDisplayRow();
				row.setRowCount(i+1);
				
				Hashtable<String,String> ht_dup = new Hashtable<String,String>();
				Iterator<XmlObject> xobj = sobj.getChildren();
				while (xobj.hasNext()){
					XmlObject o = xobj.next();
					QName qn = o.getName();
					
					if (o.getValue() != null && !qn.getLocalPart().equals("type") && !ht_dup.containsKey(qn.getLocalPart())){
						row.add(o.getValue().toString());
						ht_dup.put(qn.getLocalPart(), qn.getLocalPart());
						//if (i == 0)
						//	columnHeaderList.add(qn.getLocalPart());
					}
					
					String type_name = qn.getLocalPart();
					// Get sub query element by relationship
					if (type_name != null && o.getValue() == null){	
						XmlObject sub_xobj = (XmlObject)sobj.getField(type_name);
						if (sub_xobj != null){
							Hashtable<String,String> ht_sub_dup = new Hashtable<String,String>();
							Iterator<XmlObject> sub_qry = sub_xobj.getChildren();	
							while (sub_qry.hasNext()){
								XmlObject o1 = sub_qry.next();
								QName qn1 = o1.getName();

								if (o1.getValue() != null && !qn1.getLocalPart().equals("type") && !ht_sub_dup.containsKey(qn1.getLocalPart())){
									row.add(o1.getValue().toString());
									ht_sub_dup.put(qn1.getLocalPart(), qn1.getLocalPart());
									//if (i == 0)
									//	columnHeaderList.add(type_name+"."+qn1.getLocalPart());

								}
							}
						}							
					}
				}
				
				rowData.add(row);
				
			}
			// Print stuff out
			/*
			String str = "";
			str = "";
			for (String s : columnHeaderList)
				str += s + "     ";
			System.out.println(str);
			for (SFDisplayRow dr : rowData)
				dr.print();
				*/
		}
		SFQueryResult sfqr = new SFQueryResult();
		sfqr.setColumnHeaderList(columnHeaderList);
		sfqr.setRowData(rowData);
		if (rowData != null)
			sfqr.setDisplayResultCount(rowData.size());
		sfqr.setResultCount(row_size);
		sfqr.setQueryTime(Float.toString(elapsedTimeSec));
		return sfqr;
	}
	

	/**
	 * Delete all records that qualify for specific query
	 * 
	 * @param queryString
	 * @return String message/error notifying operation result
	 * @throws Exception
	 */
	public String deleteRecords(String queryString) throws Exception {
		ArrayList<String> ls_id = new ArrayList<String>();
		String res = "";
		// Get current time
		long start = System.currentTimeMillis();
		System.out.println("QRY: "+queryString);
		QueryResult qr = this.getConnection().query(queryString);
		
		boolean done = false;
		if (qr != null && qr.getSize() > 0) {
			while (! done) {
				   SObject[] records = qr.getRecords();
				   for ( int i = 0; i < records.length; ++i ) {
					   SObject obj = (SObject) records[i];					   
					   ls_id.add( obj.getId() );
				   }
				   if (qr.isDone()) {
					   done = true;
				   } else {
					   qr = connection.queryMore(qr.getQueryLocator());
				   }
			}
			
			// Complete query
			int cnt = 0;
			ArrayList<String> temp = new ArrayList<String>();
			for (int j = 0; j < ls_id.size(); j++) {
				cnt++;
				if (cnt == 200){
					cnt = 0;
					this.getConnection().delete(list2array(temp));
					temp.clear();
				}
				temp.add(ls_id.get(j));
			}
			if (temp.size() != 0){
				this.getConnection().delete(list2array(temp));
			}
	     } else {
	       System.out.println("No records found.");
	       res = "No records found.";
	     }		

		// Get elapsed time in milliseconds
		long elapsedTimeMillis = System.currentTimeMillis()-start;
		// Get elapsed time in seconds
		float elapsedTimeSec = elapsedTimeMillis/1000F;
		System.out.println("Delete completed in "+ Float.toString(elapsedTimeSec) );
		return res;
	}
	
	private String[] list2array(ArrayList<String> lst){
		String[] sa = new String[lst.size()];
		for (int i = 0; i < lst.size(); i++){
			sa[i] = (String)lst.get(i);
		}
		return sa;
	}

	private ArrayList<String> getFieldNamesFromQuery(String qry){
		String temp = qry.toLowerCase();
		int end = temp.indexOf("from");
		String s = qry.substring(6, end);
		String[] ss = s.split(",");
		ArrayList<String> al = new ArrayList<String>();
		if (ss != null){
			for (int i = 0; i < ss.length; i++)
				al.add(ss[i].trim());
		}
		return al;
	}
	
}
