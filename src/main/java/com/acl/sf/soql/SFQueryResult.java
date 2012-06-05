package com.acl.sf.soql;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.acl.sf.export.XLSDocumentManager;


import flexjson.JSONSerializer;

public class SFQueryResult {
	
	private ArrayList<String> columnHeaderList = new ArrayList<String>();
	private ArrayList<SFDisplayRow> rowData = new ArrayList<SFDisplayRow>();
	private int resultCount;
	private int displayResultCount;
	private String queryTime;
	public ArrayList<String> getColumnHeaderList() {
		return columnHeaderList;
	}
	public void setColumnHeaderList(ArrayList<String> columnHeaderList) {
		this.columnHeaderList = columnHeaderList;
	}
	public ArrayList<SFDisplayRow> getRowData() {
		return rowData;
	}
	public void setRowData(ArrayList<SFDisplayRow> rowData) {
		this.rowData = rowData;
	}
	public int getResultCount() {
		return resultCount;
	}
	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	public String getQueryTime() {
		return queryTime;
	}
	public void setQueryTime(String queryTime) {
		this.queryTime = queryTime;
	}
	public int getDisplayResultCount() {
		return displayResultCount;
	}
	public void setDisplayResultCount(int displayResultCount) {
		this.displayResultCount = displayResultCount;
	}
	public String toJSON(){
		
		return new JSONSerializer().include("columnHeaderList","rowData.columnList").serialize( this );
		
	}
	
	/**
	 * Write out query result in CSV format for export
	 * @param out
	 * @throws IOException
	 */
	public void toCSVStream(OutputStream out) throws IOException{
		StringBuffer sb = new StringBuffer();
		// Setup header row
		String header = "";
		int cnt = 0;
		for (String s : this.columnHeaderList){
			if (cnt > 0)
				header += ",";
			header += "\""+s+"\"";
			cnt++;
		}
		header += "\n";
		sb.append(header);
		// Setup data rows
		for (SFDisplayRow row : this.rowData){
			String str = row.toCSV(this.columnHeaderList.size()) + "\n";
			sb.append(str);
		}
		// Output data to stream
		if (out != null){
			byte[] b = sb.toString().getBytes();
			out.write(b);
		}
	}
	
	/**
	 * Write out new MS XLS file to given stream for download
	 * 
	 * @param out
	 */
	public void toXLSStream(OutputStream out) throws IOException{
		Workbook wb = new HSSFWorkbook();
		Map<String, CellStyle> styles = XLSDocumentManager.createStyles(wb);
		// Create new Sheet
		//CreationHelper createHelper = wb.getCreationHelper();
		String sheet_name = "Query result";
		Sheet sheet = wb.createSheet(sheet_name);
		// Create header row with header columns
		Row hrow = sheet.createRow((short)0);
		int cnt = 1; 
		hrow.createCell(0).setCellValue("No.");
		hrow.getCell(0).setCellStyle(styles.get("header")); //$NON-NLS-1$
		for (String s : this.columnHeaderList){
			hrow.createCell(cnt).setCellValue(s);
			hrow.getCell(cnt).setCellStyle(styles.get("header")); //$NON-NLS-1$
			cnt++;
		}
		// Create data rows
		int row_cnt = 1;
		for (SFDisplayRow row : this.rowData){
			 // Create a row and put some cells in it. Rows are 0 based.
			 Row xls_row = sheet.createRow((short)row_cnt);
			 row.toXLSRow(xls_row, row_cnt);
			 row_cnt++;
		}
		

		 // Control column width
		 sheet.setDefaultColumnWidth(50);
		 sheet.setColumnWidth(0, 1500);
		 sheet.setColumnWidth(2, 2000);
		 sheet.setColumnWidth(3, 2000);
		 sheet.setColumnWidth(4, 2000);	  
		
		wb.write(out);
		out.flush();
		out.close();		

	}
}
