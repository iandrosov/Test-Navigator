package com.acl.sf.export;

import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.acl.sfdc.SfUtil;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;
import com.sforce.ws.ConnectionException;

import com.acl.sf.util.*;
import com.acl.sf.model.*;

public class XLSDocumentManager extends BaseManager {
	private HSSFWorkbook _templateWorkbook;
	private Hashtable<String,String> ht_object_map = new Hashtable<String,String>();

	public Boolean isValid = false;

	public XLSDocumentManager(){
		this.isValid = true;
	}

	public XLSDocumentManager(Locale loc){
		try{
			this.isValid = true;
			this.setLocale(loc); // Set Locale in BaseManager class
		}catch(Exception e){
			this.isValid = false;
		}
	}
	
	public XLSDocumentManager(String template_file){
		try{
			this._templateWorkbook = new HSSFWorkbook(new FileInputStream(template_file));
			this.isValid = true;
		}catch(Exception e){
			//e.printStackTrace();
			this.isValid = false;
		}
	}

	public XLSDocumentManager(String template_file, Locale loc){
		try{
			this._templateWorkbook = new HSSFWorkbook(new FileInputStream(template_file));
			this.isValid = true;
			this.setLocale(loc); // Set Locale in BaseManager class
		}catch(Exception e){
			//e.printStackTrace();
			this.isValid = false;
		}
	}

	public void writeObjectCompareReportWorkbook(ObjectCompare oc, OutputStream ouputStream, String src, String target ) throws Exception {
		Workbook wb = createObjectComapreWorkbook(oc, src, target);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	
	public void writeObjectReportWorkbook(SfUtil sfdc, ArrayList<String> object_type_list, ArrayList<String> object_name_list, OutputStream ouputStream, String std) throws Exception {
		Workbook wb = createObjectWorkbook(sfdc, object_type_list, object_name_list, std);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	
	public void writeApexClassReportWorkbook(ArrayList<SFApexClass> classList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createApexClassReportWorkbook(classList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	
	public void writeApexTriggerReportWorkbook(ArrayList<SFApexTrigger> triggerList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createApexTriggerReportWorkbook(triggerList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();		
	}
	
	public void writeApexComponentReportWorkbook(ArrayList<SFComponent> componentList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createApexComponentReportWorkbook(componentList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();		
	}
	
	public void writeProfileReportWorkbook(ArrayList<SFProfile> profileList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createProfileReportWorkbook(profileList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();		
	}

	//
	public void writeApexPageReportWorkbook(ArrayList<SFApexPage> pageList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createApexPageReportWorkbook(pageList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	public void writeCustomLabelReportWorkbook(ArrayList<SFCustomLabel> labelList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createCustomLabelReportWorkbook(labelList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	public void writeRecordTypeReportWorkbook(ArrayList<SFRecordType> recordList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createRecordTypeReportWorkbook(recordList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	public void writeStaticResourceReportWorkbook(ArrayList<SFStaticResource> resourceList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createStaticResourceReportWorkbook(resourceList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
	public void writeDocumentReportWorkbook(ArrayList<SFDocument> docList, OutputStream ouputStream ) throws Exception {
		Workbook wb = createDocumentReportWorkbook(docList);
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	
	/**
	 * Build a work book with Object field compared results as report
	 * used to export MS Excel file
	 * 
	 * @param oc
	 * @return
	 * @throws Exception
	 */
	public Workbook createObjectComapreWorkbook(ObjectCompare oc, String src, String target) throws Exception{
		Workbook wb = new HSSFWorkbook();
		createObjectCompareDetailSheet(wb, oc, src, target);
		return wb;
	}
	
	/**
	 * Build new XLS Workbook with list of sheets and summary table of all objects
	 * 
	 * @param sfdc - SfUtil object provides connection to SFDC
	 * @param object_type_list - list of Object API Names as strings
	 * @param object_name_list - list of Object Label names as string
	 * @param std - String state if Custom/Standard object type	
	 */
	public Workbook createObjectWorkbook(SfUtil sfdc, ArrayList<String> object_type_list, ArrayList<String> object_name_list, String std) throws Exception{
		  // Create workbook
		  Workbook wb = new HSSFWorkbook();
		  ArrayList<SFObject> objList = this.getMetadataList(sfdc, object_type_list, std);
		  if (objList != null && objList.size() > 0){
			  this.createObjectListSheetFromObject(wb,objList);
		  }else{
			  this.createObjectListSheet(wb,object_type_list);
		  }
		  for (int i = 0; i < object_type_list.size(); i++) {
			  SFObject obj = objList.get(i);
			  if (obj != null){
				  this.createObjectDetailSheetEX(sfdc, wb, obj, (String)object_type_list.get(i), i+1, (String)object_name_list.get(i), std);
			  }else{
				  this.createObjectDetailSheet(sfdc, wb, (String)object_type_list.get(i), i+1, (String)object_name_list.get(i), std);				  
			  }
		  }
		  return wb;
	}
	
	/**
	 * Build new XLS Workbook report for Apex classes list from List of APEX class objects
	 * 
	 * @param classList
	 * @return wb - workbook object
	 */
	public Workbook createApexClassReportWorkbook(ArrayList<SFApexClass> classList){
		Workbook wb = new HSSFWorkbook();
		this.createApexClassListSheet(wb, classList);
		return wb;
	}

	
	public Workbook createApexPageReportWorkbook(ArrayList<SFApexPage> pageList){
		Workbook wb = new HSSFWorkbook();
		this.createApexPageListSheet(wb, pageList);
		return wb;
	}
	
	/**
	 * Build new XLS Workbook for APEX Trigger report from list of trigger objects
	 * 
	 * @param triggerList
	 * @return wb - workbook object
	 */
	public Workbook createApexTriggerReportWorkbook(ArrayList<SFApexTrigger> triggerList){
		Workbook wb = new HSSFWorkbook();
		this.createApextriggerListSheet(wb, triggerList);
		return wb;
	}

	/**
	 * Build new XLS Workbook for Visual Force Components
	 * 
	 * @param componentList
	 * @return wb - workbook object
	 */
	public Workbook createApexComponentReportWorkbook(ArrayList<SFComponent> componentList){
		Workbook wb = new HSSFWorkbook();
		this.createApexComponentListSheet(wb, componentList);
		return wb;
	}
	
	/**
	 * Build new XLS Workbook for listing all Profiles and their permission flags
	 * 
	 * @param profileList
	 * @return wb - workbook object
	 */
	public Workbook createProfileReportWorkbook(ArrayList<SFProfile> profileList){
		Workbook wb = new HSSFWorkbook();
		this.createProfileListSheet(wb, profileList);
		return wb;
	}

	/**
	 * Build new XLS Workbook listing custom Labels and translations
	 * 
	 * @param labelList
	 * @return
	 */
	public Workbook createCustomLabelReportWorkbook(ArrayList<SFCustomLabel> labelList){
		Workbook wb = new HSSFWorkbook();
		this.createCustomLabelListSheet(wb, labelList);
		return wb;
	}

	/**
	 * Build new XLS Workbook listing Record Types
	 * 
	 * @param recordList
	 * @return
	 */
	public Workbook createRecordTypeReportWorkbook(ArrayList<SFRecordType> recordList){
		Workbook wb = new HSSFWorkbook();
		this.createRecordTypeListSheet(wb, recordList);
		return wb;
	}

	/**
	 * Build new XLS Workbook listing Static Resources
	 * 
	 * @param resourceList
	 * @return
	 */
	public Workbook createStaticResourceReportWorkbook(ArrayList<SFStaticResource> resourceList){
		Workbook wb = new HSSFWorkbook();
		this.createStaticResourceListSheet(wb, resourceList);
		return wb;
	}

	/**
	 * Build new XLS Workbook listing Document
	 * 
	 * @param docList
	 * @return
	 */
	public Workbook createDocumentReportWorkbook(ArrayList<SFDocument> docList){
		Workbook wb = new HSSFWorkbook();
		this.createDocumentListSheet(wb, docList);
		return wb;
	}
	
	/** 
	 * Create a new sheet for a workbook
	 * 
	 * @param wb
	 * @param sname
	 * @param sheet_idx
	 * @return Sheet - object
	 */
	public Sheet createSheetFromTemplate(Workbook wb, String sname, int sheet_idx){

	    // Create new Sheet
		String s = getSheetName(wb, sname, sheet_idx);
		Sheet sheet = wb.createSheet(s);
				
		Sheet temp_sheet = _templateWorkbook.getSheetAt(sheet_idx);
		sheet.setDefaultColumnWidth(temp_sheet.getDefaultColumnWidth());
		sheet.setDefaultRowHeight(temp_sheet.getDefaultRowHeight());
		sheet.setDefaultRowHeightInPoints(temp_sheet.getDefaultRowHeightInPoints());
		
		Iterator<Row> rowIter = temp_sheet.rowIterator();
		//Row hrow = temp_sheet.getRow(temp_sheet.getFirstRowNum());
		int rcnt = 0;
		int col = 0;
		while (rowIter.hasNext() && rcnt < 8){
			Row hrow = (Row)rowIter.next(); 
			Row newRow = sheet.createRow(rcnt);
			Iterator<Cell> cellIter = hrow.cellIterator();
			//if (rcnt == 0)
			col = 0;
			while (cellIter.hasNext()) {
				Cell celObj = (Cell)cellIter.next();
				
				if (celObj.getCellType() == Cell.CELL_TYPE_NUMERIC || celObj.getCellType() == Cell.CELL_TYPE_FORMULA){
					newRow.createCell(col).setCellValue(celObj.getNumericCellValue());
					newRow.getCell(col).setCellType(celObj.getCellType());
				}else if (celObj.getCellType() == Cell.CELL_TYPE_BOOLEAN){
					newRow.createCell(col).setCellValue(celObj.getBooleanCellValue());
					newRow.getCell(col).setCellType(celObj.getCellType());
				//else if (celObj.getCellType() == Cell.CELL_TYPE_FORMULA)
				//	newRow.createCell(col).setCellValue(celObj.getCellFormula());
				}else if (celObj.getCellType() == Cell.CELL_TYPE_STRING){
					RichTextString str = celObj.getRichStringCellValue();
					newRow.createCell(col).setCellValue(str);
					newRow.getCell(col).setCellType(celObj.getCellType());
					
					//if (str.toString().trim().length() > 0)
					//	newRow.getCell(col).setCellStyle(cell_style_map.get("main_header"));
					
				}else if (celObj.getCellType() == Cell.CELL_TYPE_BLANK){
					newRow.createCell(col).setCellValue("");
					newRow.getCell(col).setCellType(celObj.getCellType());
					//newRow.getCell(col).setCellStyle(cell_style_map.get("cell_normal"));
				}else{
					newRow.createCell(col).setCellValue("");
					//newRow.getCell(col).setCellStyle(cell_style_map.get("cell_normal"));
				}
				
				
				//newRow.getCell(col).cloneStyleFrom(celObj.getCellStyle());
				newRow.getCell(col).setCellStyle(copyCellStyle(wb, _templateWorkbook, celObj.getCellStyle()));
				
				col++;
			}
			rcnt++;
		}
		// Set Column width
		for (int i = 0; i < col; i++)		
			sheet.setColumnWidth(i, temp_sheet.getColumnWidth(i));
		
		return sheet;
	}
	
	public Row createRowFromTemplate(Sheet sheet, int row_idx){
		Row row = sheet.createRow((short)row_idx);
		return row;
	}
	
	private CellStyle copyCellStyle(Workbook wb, Workbook old_wb, CellStyle oldStyle){
		CellStyle style = wb.createCellStyle();
	     style.setBorderRight(oldStyle.getBorderRight());
	     style.setRightBorderColor(oldStyle.getRightBorderColor());
	     style.setBorderBottom(oldStyle.getBorderBottom());
	     style.setBottomBorderColor(oldStyle.getBottomBorderColor());
	     style.setBorderLeft(oldStyle.getBorderLeft());
	     style.setLeftBorderColor(oldStyle.getLeftBorderColor());
	     style.setBorderTop(oldStyle.getBorderTop());
	     style.setTopBorderColor(oldStyle.getTopBorderColor());
	     style.setAlignment(oldStyle.getAlignment());
	     style.setFillForegroundColor(oldStyle.getFillForegroundColor());
	     style.setFillPattern(oldStyle.getFillPattern());
		
		Font font1 = wb.createFont();
		Font copyFont = old_wb.getFontAt(oldStyle.getFontIndex());
		font1.setFontName(copyFont.getFontName());
		font1.setColor(copyFont.getColor());
	    font1.setBoldweight(copyFont.getBoldweight());
	    font1.setCharSet(copyFont.getCharSet());
	    font1.setFontHeight(copyFont.getFontHeight());
	    font1.setFontHeightInPoints(copyFont.getFontHeightInPoints());
	    font1.setItalic(copyFont.getItalic());
	    font1.setStrikeout(copyFont.getStrikeout());
	    font1.setTypeOffset(copyFont.getTypeOffset());
	    font1.setUnderline(copyFont.getUnderline());
	    style.setFont(font1);	
	    
		return style;
	}
	
	/**
	 * Create a unique Sheet name based on pattern [<MAX SHEET INDEX + 1> <Template sheet name> <geven name>]
	 * MAX SHEET INDEX indicates existing sheet number on the workbook. If template available the name from 
	 * template sheet is used to create a new name.
	 * 
	 * @param wb - new Workbook object
	 * @param sname - name of sheet
	 * @return - String name for new sheet
	 */
	public String getSheetName(Workbook wb, String sname, int idx){
		String name = Integer.toString( wb.getNumberOfSheets() + 1 );		
		if (this.isValid){
			Sheet sh = _templateWorkbook.getSheetAt(idx);
			if (sh != null && sname != null && sname.length() > 0)
				name += " " + sh.getSheetName() + " - " + sname;
			else if (sh != null)
				name += " " + sh.getSheetName();
		}else{
			name += " " + sname;
		}
		return name;
	}
	
	/**
	 * Create Sheet formatted for Custom labels. Use template style if available.
	 * If no template provided default styles will be created.
	 * 
	 * @param wb - new workbook
	 * @param sheet_name - name for new sheet
	 * @return Sheet object new created with all headers, sizes, fonts  and styles
	 */
	public Sheet createCustomLabelSheet(Workbook wb, String sheet_name) {
		Sheet sheet = wb.createSheet(getSheetName(wb, sheet_name, 0));
	    if (this.isValid){
			Sheet temp_sheet = _templateWorkbook.getSheetAt(0);
			sheet.setDefaultColumnWidth(temp_sheet.getDefaultColumnWidth());
			sheet.setDefaultRowHeight(temp_sheet.getDefaultRowHeight());
			sheet.setDefaultRowHeightInPoints(temp_sheet.getDefaultRowHeightInPoints());
			
			Iterator<Row> rowIter = temp_sheet.rowIterator();
			int rcnt = 0;
			int col = 0;
			while (rowIter.hasNext() && rcnt < 8){
				Row hrow = (Row)rowIter.next(); 
				Row newRow = sheet.createRow(rcnt);
				Iterator<Cell> cellIter = hrow.cellIterator();
				col = 0;
				while (cellIter.hasNext()) {
					Cell celObj = (Cell)cellIter.next();
					
					if (celObj.getCellType() == Cell.CELL_TYPE_NUMERIC || celObj.getCellType() == Cell.CELL_TYPE_FORMULA){
						newRow.createCell(col).setCellValue(celObj.getNumericCellValue());
						newRow.getCell(col).setCellType(celObj.getCellType());
					}else if (celObj.getCellType() == Cell.CELL_TYPE_BOOLEAN){
						newRow.createCell(col).setCellValue(celObj.getBooleanCellValue());
						newRow.getCell(col).setCellType(celObj.getCellType());
					//else if (celObj.getCellType() == Cell.CELL_TYPE_FORMULA)
					//	newRow.createCell(col).setCellValue(celObj.getCellFormula());
					}else if (celObj.getCellType() == Cell.CELL_TYPE_STRING){
						RichTextString str = celObj.getRichStringCellValue();
						newRow.createCell(col).setCellValue(str);
						newRow.getCell(col).setCellType(celObj.getCellType());
						
					}else if (celObj.getCellType() == Cell.CELL_TYPE_BLANK){
						newRow.createCell(col).setCellValue("");
						newRow.getCell(col).setCellType(celObj.getCellType());
					}else{
						newRow.createCell(col).setCellValue("");
					}
					
					newRow.getCell(col).setCellStyle(copyCellStyle(wb, _templateWorkbook, celObj.getCellStyle()));
					
					col++;
				}
				rcnt++;
			}
			// Set Column width
			for (int i = 0; i < col; i++)		
				sheet.setColumnWidth(i, temp_sheet.getColumnWidth(i));
	    	
	    }else{
			// Create new Sheet
			CreationHelper createHelper = wb.getCreationHelper();			

			Map<String, CellStyle> styles = createStyles(wb);
			// Create header row
			Row hrow = sheet.createRow((short) 0);
			hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No"));
			hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
			hrow.createCell(1).setCellValue(createHelper.createRichTextString("Full Name"));
			hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
			hrow.createCell(2).setCellValue(createHelper.createRichTextString("Value"));
			hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
			hrow.createCell(3).setCellValue("Description");
			hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
			hrow.createCell(4).setCellValue("Language");
			hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
			hrow.createCell(5).setCellValue("Protected");
			hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$

			// Control column width
			sheet.setDefaultColumnWidth(50);
			sheet.setColumnWidth(0, 1500);
	    }
		return sheet;
	}
	
	
	/**
	 * create a library of cell styles to use in formatting workbook sheets
	 */
	public static Map<String, CellStyle> createStyles(Workbook wb){
	     Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
	     DataFormat df = wb.createDataFormat();

	     CellStyle style;
	     Font headerFont = wb.createFont();
	     headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_CENTER);
	     style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     style.setFont(headerFont);
	     styles.put("header", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     styles.put("cell_color", style); //$NON-NLS-1$
	     
	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_CENTER);
	     style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     style.setFont(headerFont);
	     style.setDataFormat(df.getFormat("d-mmm")); //$NON-NLS-1$
	     styles.put("header_date", style); //$NON-NLS-1$

	     Font font1 = wb.createFont();
	     font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_LEFT);
	     style.setFont(font1);
	     styles.put("cell_b", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_CENTER);
	     style.setFont(font1);
	     styles.put("cell_b_centered", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     style.setFont(font1);
	     style.setDataFormat(df.getFormat("d-mmm")); //$NON-NLS-1$
	     styles.put("cell_b_date", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     style.setFont(font1);
	     style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     style.setDataFormat(df.getFormat("d-mmm")); //$NON-NLS-1$
	     styles.put("cell_g", style); //$NON-NLS-1$

	     Font font2 = wb.createFont();
	     font2.setColor(IndexedColors.BLUE.getIndex());
	     font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_LEFT);
	     style.setFont(font2);
	     styles.put("cell_bb", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     style.setFont(font1);
	     style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     style.setDataFormat(df.getFormat("d-mmm")); //$NON-NLS-1$
	     styles.put("cell_bg", style); //$NON-NLS-1$

	     Font font3 = wb.createFont();
	     font3.setFontHeightInPoints((short)14);
	     font3.setColor(IndexedColors.DARK_BLUE.getIndex());
	     font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_LEFT);
	     style.setFont(font3);
	     style.setWrapText(true);
	     styles.put("cell_h", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_LEFT);
	     style.setWrapText(true);
	     styles.put("cell_normal", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_CENTER);
	     style.setWrapText(true);
	     styles.put("cell_normal_centered", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_RIGHT);
	     style.setWrapText(true);
	     style.setDataFormat(df.getFormat("d-mmm")); //$NON-NLS-1$
	     styles.put("cell_normal_date", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setAlignment(CellStyle.ALIGN_LEFT);
	     style.setIndention((short)1);
	     style.setWrapText(true);
	     styles.put("cell_indented", style); //$NON-NLS-1$

	     style = createBorderedStyle(wb);
	     style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
	     style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	     styles.put("cell_blue", style); //$NON-NLS-1$

	     return styles;
	 }

	 public static CellStyle createBorderedStyle(Workbook wb){
	     CellStyle style = wb.createCellStyle();
	     style.setBorderRight(CellStyle.BORDER_THIN);
	     style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	     style.setBorderBottom(CellStyle.BORDER_THIN);
	     style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	     style.setBorderLeft(CellStyle.BORDER_THIN);
	     style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	     style.setBorderTop(CellStyle.BORDER_THIN);
	     style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	     return style;
	 }

	 /**
	  * Method to add a new sheet to wokbook with compared results
	  * 
	  * @param wb
	  * @param oc
	  */
	 private void createObjectCompareDetailSheet(Workbook wb, ObjectCompare oc, String src, String target)
	 {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 String sheet_name = this.getMessageSource("label.title.apex.class.list", "Object Compare Result");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow_0 = sheet.createRow((short)0);
		 // Create Source Object Field headers
		 hrow_0.createCell(0).setCellValue(this.getMessageSource("label.object.name","Object Name:"));
		 hrow_0.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_0.createCell(1).setCellValue(oc.getSobjectName() + " - " + oc.getSobjectLabel());
		 hrow_0.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 Row hrow_1 = sheet.createRow((short)1);
		 hrow_1.createCell(0).setCellValue(this.getMessageSource("label.source.user","Source Org User:"));
		 hrow_1.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(1).setCellValue(src);
		 hrow_1.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(2).setCellValue("");
		 hrow_1.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(3).setCellValue("");
		 hrow_1.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(4).setCellValue("");
		 hrow_1.getCell(4).setCellStyle(styles.get("header"));	 //$NON-NLS-1$
		 hrow_1.createCell(5).setCellValue("");
		 hrow_1.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(6).setCellValue("");
		 hrow_1.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
	
		 hrow_1.createCell(7).setCellValue(this.getMessageSource("label.target.user","Target Org User:"));
		 hrow_1.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow_1.createCell(8).setCellValue(target);
		 hrow_1.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 Row hrow = sheet.createRow((short)2);
		 // Create Source Object Field headers
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.api.name","API name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.type","Type"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.label","Label"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.related","Related"));
		 hrow.getCell(4).setCellStyle(styles.get("header"));	 //$NON-NLS-1$

		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.size","Size"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 
		 // Create target Object Field headers
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.api.name","API name"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.type","Type"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(10).setCellValue(this.getMessageSource("label.column.label","Label"));
		 hrow.getCell(10).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(11).setCellValue(this.getMessageSource("label.column.related","Related"));
		 hrow.getCell(11).setCellStyle(styles.get("header"));	 //$NON-NLS-1$

		 hrow.createCell(12).setCellValue(this.getMessageSource("label.column.size","Size"));
		 hrow.getCell(12).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 hrow.createCell(13).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(13).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 
		 // Get results	
		 ArrayList<SFObjectFieldsMerge> diplay_list = oc.getMergeResult();
		 for (int i = 0; i < diplay_list.size(); i++){

			 SFObjectFieldsMerge obj_info = (SFObjectFieldsMerge)diplay_list.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+3);

			 
			 
			 
			 row.createCell(0).setCellValue(obj_info.getSource_obj().getCounter());
			 
			 if (!obj_info.getSource_obj().isMatch()){
				 // Accent field name cell to show different fields
				 row.createCell(1).setCellValue(obj_info.getSource_obj().getFldName());
				 row.getCell(1).setCellStyle(styles.get("cell_color")); //$NON-NLS-1$
			 }else{
				 row.createCell(1).setCellValue(obj_info.getSource_obj().getFldName()); 
			 }
			 
			 row.createCell(2).setCellValue(obj_info.getSource_obj().getTypeName());
			 row.createCell(3).setCellValue(obj_info.getSource_obj().getLableName());
			 row.createCell(4).setCellValue(obj_info.getSource_obj().getRelationName());
			 row.createCell(5).setCellValue(obj_info.getSource_obj().getLength());
			 row.createCell(6).setCellValue(obj_info.getSource_obj().getHelpText());
			 row.createCell(7).setCellValue(obj_info.getTarget_obj().getCounter());

			 if (!obj_info.getTarget_obj().isMatch()){
				 // Accent field name cell to show different fields
				 row.createCell(8).setCellValue(obj_info.getTarget_obj().getFldName());
				 row.getCell(8).setCellStyle(styles.get("cell_color")); //$NON-NLS-1$
			 }else{
				 row.createCell(8).setCellValue(obj_info.getTarget_obj().getFldName()); 
			 }
			 
			 row.createCell(9).setCellValue(obj_info.getTarget_obj().getTypeName());			 
			 row.createCell(10).setCellValue(obj_info.getTarget_obj().getLableName());
			 row.createCell(11).setCellValue(obj_info.getTarget_obj().getRelationName());
			 row.createCell(12).setCellValue(obj_info.getTarget_obj().getLength());
			 row.createCell(13).setCellValue(obj_info.getTarget_obj().getHelpText());

		 }
		 
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 3000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(6, 3000);
		 sheet.setColumnWidth(7, 1500);
		 sheet.setColumnWidth(9, 3000);
		 sheet.setColumnWidth(10, 3000);
		 sheet.setColumnWidth(11, 3000);
		 sheet.setColumnWidth(12, 3000);
		 
	 }
	 
	 /**
	  * Method to add a new Sheet to workbook present report style
	  * display list of Apex Classes
	  * 
	  * @param wb
	  * @param classList
	  */
	 private void createApexClassListSheet(Workbook wb, ArrayList<SFApexClass> classList)
	 {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.apex.class.list", "Apex Class List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no", "No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name", "Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.status", "Status"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.length", "Length"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.valid", "Valid"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.id", "ID"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.api.version", "Version"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.namespace.prefix", "Prefix"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.created.by", "CreatedBy"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.date.change", "Date Changed"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < classList.size(); i++){
			 SFApexClass apex_class_info = (SFApexClass)classList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(apex_class_info.getName());
			 row.createCell(2).setCellValue(apex_class_info.getStatus());
			 row.createCell(3).setCellValue(apex_class_info.getLengthWithoutComments());
			 row.createCell(4).setCellValue(apex_class_info.isIsValid());
			 row.createCell(5).setCellValue(apex_class_info.getId());
			 row.createCell(6).setCellValue(apex_class_info.getApiVersion());
			 row.createCell(7).setCellValue(apex_class_info.getNamespacePrefix());
			 row.createCell(8).setCellValue(apex_class_info.getCreatedByName());
			 row.createCell(9).setCellValue(apex_class_info.getStringLastModifiedDate());
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }

	 /**
	  * Display Custom Label
	  * 
	  * @param wb
	  * @param labelList
	  */
	 private void createCustomLabelListSheet(Workbook wb, ArrayList<SFCustomLabel> labelList) {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.custom.label", "Custom Label List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no", "No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name", "Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.desc", "Desc"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.base.language", "Language"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.label", "Label"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("lable.column.protected", "Protected"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 // Add language translation headers if any
		 ArrayList<String> headerList = XLSDocumentManager.getLabelHeader(labelList);
		 int idx = 6;
		 for (String s : headerList){
			 hrow.createCell(idx).setCellValue(s);
			 hrow.getCell(idx).setCellStyle(styles.get("header")); //$NON-NLS-1$
			 idx++;
		 }
		 
		 for (int i = 0; i < labelList.size(); i++){
			 SFCustomLabel apex_custom_label_info = (SFCustomLabel)labelList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);
			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(apex_custom_label_info.getName());
			 row.createCell(2).setCellValue(apex_custom_label_info.getDescription());
			 row.createCell(3).setCellValue(apex_custom_label_info.getBaseLanguage());
			 row.createCell(4).setCellValue(apex_custom_label_info.getLabel());
			 row.createCell(5).setCellValue(apex_custom_label_info.getIsProtected());

			 ArrayList<SFTranslation> trans = apex_custom_label_info.getTranslationList();
			 if (trans != null && trans.size() > 0){
				 for (int j = 0; j < trans.size(); j++){
					 SFTranslation sft = trans.get(j);
					 row.createCell(j+6).setCellValue(sft.getLabel());
				 }
			 }
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(1, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }
	
	 /**
	  * Using CustomLabel array get language translation headers
	  * 
	  * @param al_custom_label
	  * @return
	  */
	public static ArrayList<String> getLabelHeader(ArrayList<SFCustomLabel> al_custom_label){
			ArrayList<String> al = new ArrayList<String>();
			for (SFCustomLabel l : al_custom_label){
				if (l.getTranslationList() != null && l.getTranslationList().size() > 0){
					for (SFTranslation t : l.getTranslationList()){
						al.add(t.getLanguage());
					}
					// End llop got all labels headers from 1st element
					return al;
				}
			}
			return al;
	}
	 
	 private void createRecordTypeListSheet(Workbook wb, ArrayList<SFRecordType> labelList){
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.record.type", "Record Type List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no", "No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name", "Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.id", "ID"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.desc", "Desc"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.developer.name", "Dev Name"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.business.process","Buis. Proccess"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.sobject.type", "Object Type"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.namespace.prefix", "Prefix"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.created.by", "CreatedBy"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.date.change", "Date Changed"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < labelList.size(); i++){
			 SFRecordType record_type_info = (SFRecordType)labelList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(record_type_info.getName());
			 row.createCell(2).setCellValue(record_type_info.getId());		 
			 row.createCell(3).setCellValue(record_type_info.getDescription());
			 row.createCell(4).setCellValue(record_type_info.getDeveloperName());
			 row.createCell(5).setCellValue(record_type_info.getBusinessProcessId());
			 row.createCell(6).setCellValue(record_type_info.getSobjectType());
			 row.createCell(7).setCellValue(record_type_info.getNamespacePrefix());
			 row.createCell(8).setCellValue(record_type_info.getCreatedByName());
			 row.createCell(9).setCellValue(record_type_info.getStringLastModifiedDate());
		 }
		 
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }

	 private void createStaticResourceListSheet(Workbook wb, ArrayList<SFStaticResource> resourceList){
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.static.resource", "Static Resource List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no", "No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name", "Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.id", "ID"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.desc", "Desc"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.length", "Length"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.content.type", "Content Type"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.cache.control", "Cache Control"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.namespace.prefix", "Prefix"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.created.by", "CreatedBy"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.date.change","Date Changed"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < resourceList.size(); i++){
			 SFStaticResource _info = (SFStaticResource)resourceList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(_info.getName());
			 row.createCell(2).setCellValue(_info.getId());
			 row.createCell(3).setCellValue(_info.getDescription());
			 row.createCell(4).setCellValue(_info.getBodyLength());
			 row.createCell(5).setCellValue(_info.getContentType());
			 row.createCell(6).setCellValue(_info.getCacheControl());
			 row.createCell(7).setCellValue(_info.getNamespacePrefix());
			 row.createCell(8).setCellValue(_info.getCreatedByName());
			 row.createCell(9).setCellValue(_info.getStringLastModifiedDate());
		 }
		 
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }

	 private void createDocumentListSheet(Workbook wb, ArrayList<SFDocument> docList){
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.docments", "Document List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no", "No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name","Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.id","ID"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.developer.name","Dev Name"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.content.type","Content Type"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.length","Length"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("lable.column.folder.id","Folder ID"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("lable.column.url", "URL"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.namespace.prefix","Prefix"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(10).setCellValue(this.getMessageSource("label.column.created.by","CreatedBy"));
		 hrow.getCell(10).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(11).setCellValue(this.getMessageSource("label.column.date.change","Date Change"));
		 hrow.getCell(11).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < docList.size(); i++){
			 SFDocument _info = (SFDocument)docList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(_info.getName());
			 row.createCell(2).setCellValue(_info.getId());
			 row.createCell(3).setCellValue(_info.getDescription());
			 row.createCell(4).setCellValue(_info.getDeveloperName());
			 row.createCell(5).setCellValue(_info.getContentType());
			 row.createCell(6).setCellValue(_info.getBodyLength());
			 row.createCell(7).setCellValue(_info.getFolderId());
			 row.createCell(8).setCellValue(_info.getURL());
			 row.createCell(9).setCellValue(_info.getNamespacePrefix());
			 row.createCell(10).setCellValue(_info.getCreatedByName());
			 row.createCell(11).setCellValue(_info.getStringLastModifiedDate());
		 }
		 
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }

	 /**
	  * Display Visual Force Pages
	  * 
	  * @param wb
	  * @param pageList
	  */
	 private void createApexPageListSheet(Workbook wb, ArrayList<SFApexPage> pageList){
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.apex.page.list", "Apex Page List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name","Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.id","ID"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.length","Length"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.content.type","Content Type"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.cache.control","Cache Control"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.api.version","API Version"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.namespace.prefix","Prefix"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.created.by","CreatedBy"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(10).setCellValue(this.getMessageSource("label.column.date.change","Date Change"));
		 hrow.getCell(10).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < pageList.size(); i++){
			 SFApexPage apex_page_info = (SFApexPage)pageList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(apex_page_info.getName());
			 row.createCell(2).setCellValue(apex_page_info.getId());
			 row.createCell(3).setCellValue(apex_page_info.getDescription());
			 
			 row.createCell(4).setCellValue(apex_page_info.getLength());
			 row.createCell(5).setCellValue(apex_page_info.getContentType());
			 row.createCell(6).setCellValue(apex_page_info.getCacheControll());
			 
			 row.createCell(7).setCellValue(apex_page_info.getApiVersion());
			 row.createCell(8).setCellValue(apex_page_info.getNamespacePrefix());
			 row.createCell(9).setCellValue(apex_page_info.getCreatedByName());
			 row.createCell(10).setCellValue(apex_page_info.getStringLastModifiedDate());
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 3000);
		 sheet.setColumnWidth(4, 2000);	  
	 }

	 private void createApextriggerListSheet(Workbook wb, ArrayList<SFApexTrigger> triggerList)
	 {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 String sheet_name = this.getMessageSource("label.title.trigger.list", "Apex Trigger List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name","Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.status","Status"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.length","Length"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.valid","Valid"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.id","ID"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.api.version","API Version"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.namespace.prefix","Prefix"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.created.by","CreatedBy"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.date.change","Date Change"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$

		 for (int i = 0; i < triggerList.size(); i++){
			 SFApexTrigger apex_trigger_info = (SFApexTrigger)triggerList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(apex_trigger_info.getName());
			 row.createCell(2).setCellValue(apex_trigger_info.getStatus());
			 row.createCell(3).setCellValue(apex_trigger_info.getLengthWithoutComments());
			 row.createCell(4).setCellValue(apex_trigger_info.isIsValid());
			 row.createCell(5).setCellValue(apex_trigger_info.getId());
			 row.createCell(6).setCellValue(apex_trigger_info.getApiVersion());
			 row.createCell(7).setCellValue(apex_trigger_info.getNamespacePrefix());
			 row.createCell(8).setCellValue(apex_trigger_info.getCreatedByName());
			 row.createCell(9).setCellValue(apex_trigger_info.getStringLastModifiedDate());
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 3500);
			  
	 }

	 private void createApexComponentListSheet(Workbook wb, ArrayList<SFComponent> componentList)
	 {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 String sheet_name = this.getMessageSource("label.title.component.list","Apex Component List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name","Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.master.label","Master Label"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.controller.key","Controller Key"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.controller.type","Controller Type"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.api.version","API Version"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.namespace.prefix","Prefix"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.created.by","CreatedBy"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.date.change","Date Changed"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 for (int i = 0; i < componentList.size(); i++){
			 SFComponent apex_class_info = (SFComponent)componentList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(apex_class_info.getName());
			 row.createCell(2).setCellValue(apex_class_info.getDescription());
			 row.createCell(3).setCellValue(apex_class_info.getMasterLabel());
			 row.createCell(4).setCellValue(apex_class_info.getControllerKey());
			 row.createCell(5).setCellValue(apex_class_info.getControllerType());
			 row.createCell(6).setCellValue(apex_class_info.getApiVersion());
			 row.createCell(7).setCellValue(apex_class_info.getNamespacePrefix());
			 row.createCell(8).setCellValue(apex_class_info.getCreatedByName());
			 row.createCell(9).setCellValue(apex_class_info.getStringLastModifiedDate());
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 3500);
			  
	 }

	 private void createProfileListSheet(Workbook wb, ArrayList<SFProfile> profileList)
	 {
		 Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		 // Create new Sheet
		 //CreationHelper createHelper = wb.getCreationHelper();
		 String sheet_name = this.getMessageSource("label.title.profile.list","Profile List");
		 Sheet sheet = wb.createSheet(sheet_name);
		 // Create header row with header columns
		 Row hrow = sheet.createRow((short)0);
		 hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No."));
		 hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(1).setCellValue(this.getMessageSource("label.column.name","Name"));
		 hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(2).setCellValue(this.getMessageSource("label.column.desc","Desc"));
		 hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$	
		 hrow.createCell(3).setCellValue(this.getMessageSource("label.column.user.type","User Type"));
		 hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(4).setCellValue(this.getMessageSource("label.column.id","ID"));
		 hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 // Setup all Profile permissions cells/columns
		 hrow.createCell(5).setCellValue(this.getMessageSource("label.column.author.apex","Author Apex"));
		 hrow.getCell(5).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(6).setCellValue(this.getMessageSource("label.column.api.enabled","API Enabled"));
		 hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(7).setCellValue(this.getMessageSource("label.column.view.setup","View Setup"));
		 hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(8).setCellValue(this.getMessageSource("label.column.data.categories","Data Categories"));
		 hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(9).setCellValue(this.getMessageSource("label.column.all.data","All Data"));
		 hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(10).setCellValue(this.getMessageSource("label.column.team.reasign.wizard","Reassign Wizard"));
		 hrow.getCell(10).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(11).setCellValue(this.getMessageSource("label.column.transfer.any.lead","Transfer Any Lead"));
		 hrow.getCell(11).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(12).setCellValue(this.getMessageSource("label.column.any.entity","Any Entity"));
		 hrow.getCell(12).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(13).setCellValue(this.getMessageSource("label.column.any.case","Any Case"));
		 hrow.getCell(13).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(14).setCellValue(this.getMessageSource("label.column.solution.import","Solution Import"));
		 hrow.getCell(14).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(15).setCellValue(this.getMessageSource("label.column.send.sit.request","Send Request"));
		 hrow.getCell(15).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(16).setCellValue(this.getMessageSource("label.column.schedule.reports","Schedule Reports"));
		 hrow.getCell(16).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(17).setCellValue(this.getMessageSource("label.column.run.reports","Run Reports"));
		 hrow.getCell(17).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(18).setCellValue(this.getMessageSource("label.column.publish.multiforce","Publish Multiforce"));
		 hrow.getCell(18).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(19).setCellValue(this.getMessageSource("label.column.password.no.expire","Password No Expire"));
		 hrow.getCell(19).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(20).setCellValue(this.getMessageSource("label.column.modify.all.data","Modify All Data"));
		 hrow.getCell(20).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(21).setCellValue(this.getMessageSource("label.column.manage.users","Manage Users"));
		 hrow.getCell(21).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(22).setCellValue(this.getMessageSource("label.column.manage.solutions","Manage Solutions"));
		 hrow.getCell(22).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(23).setCellValue(this.getMessageSource("label.column.self.service","Selef Service"));
		 hrow.getCell(23).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(24).setCellValue(this.getMessageSource("label.column.remote.access","Remote Access"));
		 hrow.getCell(24).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(25).setCellValue(this.getMessageSource("label.column.mobile","Mobile"));
		 hrow.getCell(25).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(26).setCellValue(this.getMessageSource("label.column.leads","Leads"));
		 hrow.getCell(26).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(27).setCellValue(this.getMessageSource("label.column.email.client.config","Email Client Config"));
		 hrow.getCell(27).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(28).setCellValue(this.getMessageSource("label.column.dynamic.dashboards","Dynamic Dashboards"));
		 hrow.getCell(28).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(29).setCellValue(this.getMessageSource("label.column.data.integrations","Data Integrations"));
		 hrow.getCell(29).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(30).setCellValue(this.getMessageSource("label.column.data.categories","Data Categories"));
		 hrow.getCell(30).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(31).setCellValue(this.getMessageSource("label.column.dashboards","Dashboards"));
		 hrow.getCell(31).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(32).setCellValue(this.getMessageSource("label.column.custom.report.types","Report Types"));
		 hrow.getCell(32).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(33).setCellValue(this.getMessageSource("label.column.css.users","CSS Users"));
		 hrow.getCell(33).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(34).setCellValue(this.getMessageSource("label.column.categories","Categories"));
		 hrow.getCell(34).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(35).setCellValue(this.getMessageSource("label.column.cases","Cases"));
		 hrow.getCell(35).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(36).setCellValue(this.getMessageSource("label.column.call.centers","Call Centers"));
		 hrow.getCell(36).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(37).setCellValue(this.getMessageSource("label.column.business.hour.holidays","Business Hour Holidays"));
		 hrow.getCell(37).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(38).setCellValue(this.getMessageSource("label.column.analytic.snapshots","Analytic Snapshots"));
		 hrow.getCell(38).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(39).setCellValue(this.getMessageSource("label.column.install.multiforce","Multiforce"));
		 hrow.getCell(39).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(40).setCellValue(this.getMessageSource("label.column.import.lead","Import Lead"));
		 hrow.getCell(40).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(41).setCellValue(this.getMessageSource("label.column.enable.notifications","Enable Notifications"));
		 hrow.getCell(41).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(42).setCellValue(this.getMessageSource("label.column.edit.task", "Edit Task"));
		 hrow.getCell(42).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(43).setCellValue(this.getMessageSource("label.column.edit.report","Edit Report"));
		 hrow.getCell(43).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(44).setCellValue(this.getMessageSource("label.column.edit.readonly.fields","Read only Fields"));
		 hrow.getCell(44).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(45).setCellValue(this.getMessageSource("label.column.edit.public.documents","Edit Public Documents"));
		 hrow.getCell(45).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(46).setCellValue(this.getMessageSource("label.column.edit.item.unit.price","Edit Unit Price"));
		 hrow.getCell(46).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(47).setCellValue(this.getMessageSource("label.column.edit.event","Edit Event"));
		 hrow.getCell(47).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(48).setCellValue(this.getMessageSource("label.column.edit.case.comments","Edit Case Comments"));
		 hrow.getCell(48).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(49).setCellValue(this.getMessageSource("label.column.customize.app","Customize App"));
		 hrow.getCell(49).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(50).setCellValue(this.getMessageSource("label.column.create.multiforce","Create Multiforce"));
		 hrow.getCell(50).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(51).setCellValue(this.getMessageSource("label.column.convert.leads","Convert Leads"));
		 hrow.getCell(51).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(52).setCellValue(this.getMessageSource("label.column.use.new.dashboard","Use New Dashboard"));
		 hrow.getCell(52).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 hrow.createCell(53).setCellValue(this.getMessageSource("label.column.bulk.api.hard.delete","API Hard Delete"));
		 hrow.getCell(53).setCellStyle(styles.get("header")); //$NON-NLS-1$
		 
		 for (int i = 0; i < profileList.size(); i++){
			 SFProfile profile_info = (SFProfile)profileList.get(i);

			 // Create a row and put some cells in it. Rows are 0 based.
			 Row row = sheet.createRow((short)i+1);

			 row.createCell(0).setCellValue(i);
			 row.createCell(1).setCellValue(profile_info.getName());
			 row.createCell(2).setCellValue(profile_info.getDescription());
			 row.createCell(3).setCellValue(profile_info.getUserType());
			 row.createCell(4).setCellValue(profile_info.getId());
			 // Set properties permissions			 
			 row.createCell(5).setCellValue(getBooleanFlag(profile_info.isPermissionsAuthorApex()));
			 row.createCell(6).setCellValue(getBooleanFlag(profile_info.isPermissionsApiEnabled()));
			 row.createCell(7).setCellValue(getBooleanFlag(profile_info.isPermissionsViewSetup()));
			 row.createCell(8).setCellValue(getBooleanFlag(profile_info.isPermissionsManageDataCategories()));
			 row.createCell(9).setCellValue(getBooleanFlag(profile_info.isPermissionsViewAllData()));
			 row.createCell(10).setCellValue(getBooleanFlag(profile_info.isPermissionsUseTeamReassignWizards()));
			 row.createCell(11).setCellValue(getBooleanFlag(profile_info.isPermissionsTransferAnyLead()));
			 row.createCell(12).setCellValue(getBooleanFlag(profile_info.isPermissionsTransferAnyEntity()));
			 row.createCell(13).setCellValue(getBooleanFlag(profile_info.isPermissionsTransferAnyCase()));
			 row.createCell(14).setCellValue(getBooleanFlag(profile_info.isPermissionsSolutionImport()));
			 row.createCell(15).setCellValue(getBooleanFlag(profile_info.isPermissionsSendSitRequests()));
			 row.createCell(16).setCellValue(getBooleanFlag(profile_info.isPermissionsScheduleReports()));
			 row.createCell(17).setCellValue(getBooleanFlag(profile_info.isPermissionsRunReports()));
			 row.createCell(18).setCellValue(getBooleanFlag(profile_info.isPermissionsPublishMultiforce()));
			 row.createCell(19).setCellValue(getBooleanFlag(profile_info.isPermissionsPasswordNeverExpires()));
			 row.createCell(20).setCellValue(getBooleanFlag(profile_info.isPermissionsModifyAllData()));
			 row.createCell(21).setCellValue(getBooleanFlag(profile_info.isPermissionsManageUsers()));
			 row.createCell(22).setCellValue(getBooleanFlag(profile_info.isPermissionsManageSolutions()));
			 row.createCell(23).setCellValue(getBooleanFlag(profile_info.isPermissionsManageSelfService()));
			 row.createCell(24).setCellValue(getBooleanFlag(profile_info.isPermissionsManageRemoteAccess()));
			 row.createCell(25).setCellValue(getBooleanFlag(profile_info.isPermissionsManageMobile()));
			 row.createCell(26).setCellValue(getBooleanFlag(profile_info.isPermissionsManageLeads()));
			 row.createCell(27).setCellValue(getBooleanFlag(profile_info.isPermissionsManageEmailClientConfig()));
			 row.createCell(28).setCellValue(getBooleanFlag(profile_info.isPermissionsManageDynamicDashboards()));
			 row.createCell(20).setCellValue(getBooleanFlag(profile_info.isPermissionsManageDataIntegrations()));
			 row.createCell(30).setCellValue(getBooleanFlag(profile_info.isPermissionsManageDataCategories()));
			 row.createCell(31).setCellValue(getBooleanFlag(profile_info.isPermissionsManageDashboards()));
			 row.createCell(32).setCellValue(getBooleanFlag(profile_info.isPermissionsManageCustomReportTypes()));
			 row.createCell(33).setCellValue(getBooleanFlag(profile_info.isPermissionsManageCssUsers()));
			 row.createCell(34).setCellValue(getBooleanFlag(profile_info.isPermissionsManageCategories()));
			 row.createCell(35).setCellValue(getBooleanFlag(profile_info.isPermissionsManageCases()));
			 row.createCell(36).setCellValue(getBooleanFlag(profile_info.isPermissionsManageCallCenters()));
			 row.createCell(37).setCellValue(getBooleanFlag(profile_info.isPermissionsManageBusinessHourHolidays()));
			 row.createCell(38).setCellValue(getBooleanFlag(profile_info.isPermissionsManageAnalyticSnapshots()));
			 row.createCell(39).setCellValue(getBooleanFlag(profile_info.isPermissionsInstallMultiforce()));
			 row.createCell(40).setCellValue(getBooleanFlag(profile_info.isPermissionsImportLeads()));
			 row.createCell(41).setCellValue(getBooleanFlag(profile_info.isPermissionsEnableNotifications()));
			 row.createCell(42).setCellValue(getBooleanFlag(profile_info.isPermissionsEditTask()));
			 row.createCell(43).setCellValue(getBooleanFlag(profile_info.isPermissionsEditReports()));
			 row.createCell(44).setCellValue(getBooleanFlag(profile_info.isPermissionsEditReadonlyFields()));
			 row.createCell(45).setCellValue(getBooleanFlag(profile_info.isPermissionsEditPublicDocuments()));
			 row.createCell(46).setCellValue(getBooleanFlag(profile_info.isPermissionsEditOppLineItemUnitPrice()));
			 row.createCell(47).setCellValue(getBooleanFlag(profile_info.isPermissionsEditEvent()));
			 row.createCell(48).setCellValue(getBooleanFlag(profile_info.isPermissionsEditCaseComments()));
			 row.createCell(49).setCellValue(getBooleanFlag(profile_info.isPermissionsCustomizeApplication()));
			 row.createCell(50).setCellValue(getBooleanFlag(profile_info.isPermissionsCreateMultiforce()));
			 row.createCell(51).setCellValue(getBooleanFlag(profile_info.isPermissionsConvertLeads()));
			 row.createCell(52).setCellValue(getBooleanFlag(profile_info.isPermissionsCanUseNewDashboardBuilder()));
			 row.createCell(53).setCellValue(getBooleanFlag(profile_info.isPermissionsBulkApiHardDelete()));
		 }
		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(3, 4000);
		 sheet.setColumnWidth(4, 5000);
		 for (int i = 5; i < 53; i++)
			  sheet.setColumnWidth(i, 2000);	  
		 
		 
	 }
	 
	 private String getBooleanFlag(boolean bl){
		 if (bl)
			 return this.getMessageSource("label.true.flag", "Y");
		 return this.getMessageSource("label.false.flag","N");
	 }
	 

		/**
		 * Build page listing all objects
		 * @param wb
		 * @param object_list
		 */
		public void createObjectListSheet(Workbook wb, ArrayList<String> object_list)
		{
			  Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
			  // Create new Sheet
			  CreationHelper createHelper = wb.getCreationHelper();
			  String sheet_name = this.getMessageSource("label.title.object.list","Object List");
			  Sheet sheet = wb.createSheet(sheet_name);
			  // Create header row
			  Row hrow = sheet.createRow((short)0);
			  hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No"));
			  hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
			  hrow.createCell(1).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.label","Label")));
			  hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
			  hrow.createCell(2).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.api.name","API Name")));
			  hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$

			  for (int i = 0; i < object_list.size(); i++){
				  String object_name = (String)object_list.get(i);

				  // Create a row and put some cells in it. Rows are 0 based.
				  Row row = sheet.createRow((short)i+1);

				  // Get label for objects run Object Schema only if type =  CustomObject
				  String obj_label = (String)ht_object_map.get(object_name);
				  if (obj_label == null)
					  obj_label = ""; //$NON-NLS-1$

				  row.createCell(0).setCellValue(i+1);
				  row.createCell(1).setCellValue(obj_label);//createHelper.createRichTextString(obj_label));
				  row.createCell(2).setCellValue(object_name); //createHelper.createRichTextString(object_name));				  
			  }
			  // Control column width
			  sheet.setDefaultColumnWidth(50);
			  sheet.setColumnWidth(0, 1500);
			  sheet.setColumnWidth(3, 2500);
			  
		}

		/**
		 * Build page listing all objects
		 * @param wb
		 * @param object_list
		 */
		public void createObjectListSheetFromObject(Workbook wb, ArrayList<SFObject> object_list)
		{
			  Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
			  // Create new Sheet
			  CreationHelper createHelper = wb.getCreationHelper();
			  String sheet_name = this.getMessageSource("label.title.object.list","Object List");
			  Sheet sheet = wb.createSheet(sheet_name);
			  // Create header row
			  Row hrow = sheet.createRow((short)0);
			  hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No"));
			  hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
			  hrow.createCell(1).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.label","Label")));
			  hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$
			  hrow.createCell(2).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.api.name","API Name")));
			  hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$

			  for (int i = 0; i < object_list.size(); i++){
				  SFObject obj = object_list.get(i);
				  
				  String object_name = obj.getApi_name();

				  // Create a row and put some cells in it. Rows are 0 based.
				  Row row = sheet.createRow((short)i+1);

				  // Get label for objects run Object Schema only if type =  CustomObject
				  String obj_label = obj.getLabel();
				  if (obj_label == null)
					  obj_label = ""; //$NON-NLS-1$

				  row.createCell(0).setCellValue(i+1);
				  row.createCell(1).setCellValue(obj_label);//createHelper.createRichTextString(obj_label));
				  row.createCell(2).setCellValue(object_name); //createHelper.createRichTextString(object_name));				  
			  }
			  // Control column width
			  sheet.setDefaultColumnWidth(50);
			  sheet.setColumnWidth(0, 1500);
			  sheet.setColumnWidth(3, 2500);
			  
		}
		
		private ArrayList<SFObject> getMetadataList(SfUtil sfdc, ArrayList<String> object_type_list, String std) throws ConnectionException, Exception {
			ArrayList<SFObject> objList =  new ArrayList<SFObject>();
			for (String onm : object_type_list){
				ObjectScribe os = new ObjectScribe();
				SFObject sobj = os.getObjectMetadata(sfdc, onm, std);
				objList.add(sobj);
			}
			return objList;
		}

		private void createObjectDetailSheet(SfUtil sfdc, Workbook wb, String sObjectType, int idx, String object_full_name, String std) throws ConnectionException, Exception
		{
			if (sfdc != null) // && sfdc.isPing_status())
			{
				ObjectScribe os = new ObjectScribe();
				SFObject sobj = os.getObjectMetadata(sfdc, sObjectType, std);
				createObjectDetailSheetEX(sfdc, wb, sobj, sObjectType, idx, object_full_name, std);
			}
		}
		
		/**
		 * Create detailed spreadsheet for each Object with fields,data types, size, relationship 
		 * types and Pick list values
		 * 
		 * @param wb - Reference to Excel WorkBook
		 * @param sObjectType - String name of the object type Account, Contact, MailTest__c etc.
		 * @param idx - Index of current work sheet to act as parent
		 * @throws ConnectionException - connection to salesforce 
		 */
		private void createObjectDetailSheetEX(SfUtil sfdc, Workbook wb, SFObject sobj, String sObjectType, int idx, String object_full_name, String std) throws ConnectionException
		{
			if (sfdc != null) // && sfdc.isPing_status())
			{
				// Create new object sheet
				Sheet sheet = null;
				String sheet_name = "";
				if (sobj.isCustom) {
					sheet_name = Integer.toString(idx) + " " + this.getMessageSource("label.DocumentCreator_Custom_Object","Custom Object") + "-" + sObjectType+" - "+object_full_name;
				} else {
					sheet_name = Integer.toString(idx) + " " + this.getMessageSource("label.DocumentCreator_Standard_Object","Standard Object") + "-" + sObjectType+" - "+object_full_name;
				}

				if (wb.getSheet(sheet_name) == null)
					sheet = wb.createSheet(sheet_name);
				
				Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				// Create Object Header
				  Row hrow0 = sheet.createRow((short)0);
				  hrow0.createCell(0).setCellValue(this.getMessageSource("label.column.api.name","API name"));
				  hrow0.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  hrow0.createCell(1).setCellValue(sObjectType);

				  Row hrow1 = sheet.createRow((short)1);
				  hrow1.createCell(0).setCellValue(this.getMessageSource("label.column.label","Label"));
				  hrow1.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  hrow1.createCell(1).setCellValue(sobj.getLabel());
				  
				  //if (this.ht_object_map != null && this.ht_object_map.containsKey(sObjectType))
				  //	  hrow1.createCell(1).setCellValue((String)this.ht_object_map.get(sObjectType));
				  
				  Row hrow = sheet.createRow((short)2);
				  
				  hrow.createCell(0).setCellValue(this.getMessageSource("label.column.no","No"));
				  hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  
				  hrow.createCell(1).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.api.name","API name")));
				  hrow.getCell(1).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(2).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.type","Type")));
				  hrow.getCell(2).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(3).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.pick.val","Pick Values")));
				  hrow.getCell(3).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  			  
				  hrow.createCell(4).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.label","Label")));
				  hrow.getCell(4).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(5).setCellValue(this.getMessageSource("label.column.related","Related"));
				  hrow.getCell(5).setCellStyle(styles.get("header"));	 //$NON-NLS-1$

				  hrow.createCell(6).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.size","Size")));
				  hrow.getCell(6).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(7).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_Decimal","Decimal")));
				  hrow.getCell(7).setCellStyle(styles.get("header")); //$NON-NLS-1$
 
				  hrow.createCell(8).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_ByteSize","Byte Size")));
				  hrow.getCell(8).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(9).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_Autonumber","Autonumber")));
				  hrow.getCell(9).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  
				  hrow.createCell(10).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_CalcFormula","Formula")));
				  hrow.getCell(10).setCellStyle(styles.get("header")); //$NON-NLS-1$

				  hrow.createCell(11).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_IsUnique","Unique")));
				  hrow.getCell(11).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  //hrow.getCell(10).setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
				  
				  hrow.createCell(12).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_IsLookup","Lookup")));
				  hrow.getCell(12).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  //hrow.getCell(11).setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
				  
				  hrow.createCell(13).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.DocumentCreator_IsExternalId","External Id")));
				  hrow.getCell(13).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  //hrow.getCell(12).setCellType(HSSFCell.CELL_TYPE_BOOLEAN);

				  hrow.createCell(14).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.inline.help","Inline Help")));
				  hrow.getCell(14).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  
				  hrow.createCell(15).setCellValue(createHelper.createRichTextString(this.getMessageSource("label.column.desc","Desc")));
				  hrow.getCell(15).setCellStyle(styles.get("header")); //$NON-NLS-1$
				  
				  // Add translation headers for labels
				  ArrayList<String> optList = sobj.getOptionHeaders();
				  if (optList != null && optList.size() > 0){
					  for (int i = 0; i < optList.size(); i++){
						  hrow.createCell(i+16).setCellValue(this.getMessageSource("label.column.label","Label") + " - " + createHelper.createRichTextString(optList.get(i)));
						  hrow.getCell(i+16).setCellStyle(styles.get("header")); //$NON-NLS-1$
					  }
				  }
				  
				Field[] fld_arr = sobj.fld_arr;
				// Init field map
				sobj.initFieldMap();
				// Record each field as row
				int row_num = 2;
				for (int i = 0; i < fld_arr.length; i++){
					Field fld = fld_arr[i];
					int row_counter = i+1;
					row_num++;
					Row row = sheet.createRow((short)row_num);
					row.createCell(0).setCellValue(row_counter);				
					row.createCell(1).setCellValue(fld.getName());
					row.createCell(2).setCellValue(fld.getType().name());
					
					// Handle picklists
					if (fld.isDependentPicklist())
						row.createCell(3).setCellValue("Dependent");
					if (fld.isRestrictedPicklist())
						row.createCell(3).setCellValue("Restricted");
					//System.out.println("Export fld: "+fld.getName()+" type: "+fld.getType().name());
					if (fld.getType().name().equalsIgnoreCase("picklist") || fld.getType().name().equalsIgnoreCase("multipicklist"))
						row_num = addPicklistValues(row_num, row_counter, fld, sheet);
					
					row.createCell(4).setCellValue(fld.getLabel());
					if (fld.getRelationshipName() != null){
						String relate_order = "[Child]";
						if (fld.getRelationshipOrder() == 0)
							relate_order = "[Parent]";							
						row.createCell(5).setCellValue(fld.getRelationshipName()+" "+relate_order);
					}else{
						row.createCell(5).setCellValue(fld.getRelationshipName());
					}
					// Handle double type size vs String
					if (fld.getType().name().equalsIgnoreCase("_double")){
						row.createCell(6).setCellValue(fld.getPrecision());
					}else{
						row.createCell(6).setCellValue(fld.getLength());
					}
					row.createCell(7).setCellValue(fld.getScale());					
					row.createCell(8).setCellValue(fld.getByteLength());
					row.createCell(9).setCellValue(getFlagValue(fld.isAutoNumber()));
					row.createCell(10).setCellValue(fld.getCalculatedFormula());
					row.createCell(11).setCellValue(getFlagValue(fld.isUnique()));
					row.createCell(12).setCellValue(getFlagValue(fld.isIdLookup()));
					row.createCell(13).setCellValue(getFlagValue(fld.isExternalId()));
					row.createCell(14).setCellValue(fld.getInlineHelpText());
					
					// Add cells for translations
					SFField sf_field = sobj.getFieldMap(fld.getName());
					if (sf_field != null){ //&& sf_fieldList.size() > i){
						// Add description here if exists
						row.createCell(15).setCellValue(sf_field.getDescription());
						
						ArrayList<SFTranslation> tranList = sf_field.getTranslationList();
						if(tranList != null && tranList.size() > 0){
							for(int t = 0; t < tranList.size(); t++){
								SFTranslation trn = tranList.get(t);
								row.createCell(t+16).setCellValue(trn.getLabel());
							}
						}
					}
				}
				sheet.setDefaultColumnWidth(50);
				sheet.setColumnWidth(0, 1500);
				sheet.setColumnWidth(2, 3000);
				sheet.setColumnWidth(3, 3000);
				sheet.setColumnWidth(6, 3000);
				sheet.setColumnWidth(7, 3000);
				sheet.setColumnWidth(8, 3000);
				// skip FORMULA
				sheet.setColumnWidth(10, 3000);
				sheet.setColumnWidth(11, 3000);
				sheet.setColumnWidth(12, 3000);
				sheet.setColumnWidth(13, 3000);
				sheet.setColumnWidth(14, 3000);
				sheet.setColumnWidth(15, 3000);
			}	
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
		private int addPicklistValues(int row_num, int row_counter, Field fld, Sheet sheet)
		{
			int cnt = row_num;
			PicklistEntry[] ple_arr = fld.getPicklistValues();
			for (int i = 0; i < ple_arr.length; i++){
				cnt++;
				
				Row row = sheet.createRow((short)cnt);
				String pick_row_num = Integer.toString(row_counter) + "." + Integer.toString(i+1); 
				row.createCell(0).setCellValue(pick_row_num);
				row.createCell(1).setCellValue("");
				row.createCell(2).setCellValue("");	
				if (ple_arr[i].getValue().equals(ple_arr[i].getLabel())){
					row.createCell(3).setCellValue(ple_arr[i].getValue()); //(ple_arr[i].getLabel()+"-"+ple_arr[i].getValue());
				}else{
					row.createCell(3).setCellValue(ple_arr[i].getValue()+"-"+ple_arr[i].getLabel());
				}
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");
				row.createCell(6).setCellValue("");
				row.createCell(7).setCellValue("");
				row.createCell(8).setCellValue("");
				row.createCell(9).setCellValue("");
				row.createCell(10).setCellValue("");
				row.createCell(11).setCellValue("");
				row.createCell(12).setCellValue("");
			}
			return cnt;
		}

		private String getFlagValue(Boolean flag){
			if (flag){
				return this.getMessageSource("label.deploy.flag","Y");
			}
			return "";
		}

		/*
		private String getRelationshipLabel(SFField fld) {
			if (fld != null && (fld.relationshipName != null || fld.referenceTo != null || fld.relationshipLabel != null)){
				String str = fld.referenceTo + " [Name:"+fld.relationshipName + " " +fld.relationshipLabel + "]";
				if (fld.relationshipOrder == 1)
					str += " (Child)";
				else
					str += " (Parent)";
				return str;
			}
			return null;
		}
		*/
	 
	 
}
