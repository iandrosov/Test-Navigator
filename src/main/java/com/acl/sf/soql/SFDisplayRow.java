package com.acl.sf.soql;

import java.util.ArrayList;
import org.apache.poi.ss.usermodel.Row;

public class SFDisplayRow {

	private int rowCount;
	private ArrayList<String> columnList;

	public ArrayList<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(ArrayList<String> columnList) {
		this.columnList = columnList;
	}
	
	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public void add(String s) {
		if (columnList == null)
			columnList = new ArrayList<String>();
		columnList.add(s);
	}
	
	public void print(){
		if (columnList != null){
			String str = "";
			for (int i = 0; i < columnList.size(); i++){
				str += columnList.get(i)+"; "; 
			}
			System.out.println(str);
		}
	}
	
	public String toCSV(int fld){
		String result = "";
		int cnt = 0;
		for (String s : this.columnList){
			if (cnt > 0)
				result += ",";
			result += "\""+s+"\"";
			cnt++;
		}
		if (this.columnList != null && this.columnList.size() > 0 && fld > this.columnList.size()){
			String csv_pad = padCSV(fld - this.columnList.size());
			result += csv_pad;
		}
		return result;
	}
	
	/**
	 * Pad CSV fields
	 * @param c
	 * @return
	 */
	private String padCSV(int c){
		String s = "";
		for (int i = 0; i < c; i++){
			s +=",\"\"";
		}
		return s;
	}
	
	/**
	 * Create XLS cells for a row including row counter
	 * @param xls_row
	 * @param row_count
	 */
	public void toXLSRow(Row xls_row, int row_count) {
		 // Create a row and put some cells in it. Rows are 0 based.
		int cnt = 0;
		xls_row.createCell(cnt).setCellValue(row_count);
		for (String s : this.columnList){
			cnt++;
			xls_row.createCell(cnt).setCellValue(s);
		}		
	}
}
