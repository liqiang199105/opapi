package com.litb.opapi.util;

public class StringUtil {

	public static boolean isEmpty(String str){
		if(null==str || "".equals(str)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static boolean equals(String str1, String str2){
		if(null!=str1){
			return str1.equals(str2);
		}
		return null==str2;
	}
}
