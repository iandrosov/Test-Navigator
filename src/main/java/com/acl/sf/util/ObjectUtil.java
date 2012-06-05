package com.acl.sf.util;

/**
 * Utility class handle object specific helper methods
 * @author iandrosov
 *
 */

public class ObjectUtil {

	private static String[] standardFieldList = {"LastActivityDate","SystemModstamp","LastModifiedById","LastModifiedDate","CreatedById","CreatedDate","OwnerId","IsDeleted","MasterRecordId"};
	
	/**
	 * Check if Object field is a standard type field such as Id, LastActivityDate, SystemModstamp etc.
	 * These fields often need to be filtered out from documentation and commonly known for every object.
	 * @param fld
	 * @return
	 */
	public static boolean isStandardField(String fld){
		boolean rc = false;
		for (int i = 0; i < standardFieldList.length; i++){
			 if (fld.equals(standardFieldList[i]))
				 return true;
		}
		return rc;
	}

	/**
	 * Check if Object field is a standard type field such as Id, LastActivityDate, SystemModstamp etc.
	 * These fields often need to be filtered out from documentation and commonly known for every object.
	 * @param fld
	 * @return
	 */
	public static boolean isStandardField(String fld, String std){
		boolean rc = false;
		if (std != null && std.equalsIgnoreCase("true")){
			for (int i = 0; i < standardFieldList.length; i++){
			 if (fld.equals(standardFieldList[i]))
				 return true;
			}
		}
		return rc;
	}
	
}
