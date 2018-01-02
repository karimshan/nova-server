package org.nova.kshan.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author K-Shan
 *
 */
public class StringUtils {
	
	/**
	 * Returns true if the stringToFind is in findingFrom.
	 * @param stringToFind
	 * @param findingFrom
	 * @return
	 */
	public static boolean contains(String stringToFind, String findingFrom) {
		  if(stringToFind.equals(""))
		    return true;
		  if(findingFrom == null || stringToFind == null || findingFrom .equals(""))
		    return false; 
		  Pattern p = Pattern.compile(stringToFind, Pattern.CASE_INSENSITIVE + Pattern.LITERAL);
		  Matcher m = p.matcher(findingFrom);
		  return m.find();
	}
	
	/**
	 * Color Hex codes
	 */
	public static final String[][] COLORS = { 
		{ "RED", "FF0000" }, { "GREEN", "00FF00" }, { "BLUE", "0000FF" }, 
		{ "BLACK", "000000" }, { "ORANGE", "FF9900" }, { "PURPLE", "4B0082" },
		{ "LOYALTY", "C35617" }, { "WHITE", "FFFFFF" }, { "LOYALTY PURPLE", "9932CC" },
		{ "SWAMP BLUE", "99992" }, { "CYAN", "00FFFF" }, { "MAROON", "C00006" }};
	
	public static String getColorFromHex(String hex) {
		hex = hex.toUpperCase();
		for (String[] color : COLORS) {
			if (hex.equals(color[1]))
				return color[0];
		}
		return null;
	}

	public static String getHexFromColor(String color) {
		color = color.toUpperCase();
		for (String[] col : COLORS) {
			if (col.equals(col[0]))
				return col[1];
		}
		return null;
	}

	/**
	 * If the two objects contain the same value, and have the same size,
	 * then it returns true
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static boolean objectsMatch(Object a1, Object a2) {
		if((a1 == null && a2 != null) || (a2 == null && a1 != null))
			return false;
		if(a1 instanceof int[] && a2 instanceof int[]) {
			int[] arr1 = (int[]) a1;
			int[] arr2 = (int[]) a2;
			if(arr1.length != arr2.length)
				return false;
			for(int i = 0; i < arr1.length; i++) {
				if(arr1[i] != arr2[i]) {
					return false;
				}
			}
		}
		if(a1 instanceof String[] && a2 instanceof String[]) {
			String[] arr1 = (String[]) a1;
			String[] arr2 = (String[]) a2;
			if(arr1.length != arr2.length)
				return false;
			for(int i = 0; i < arr1.length; i++) {
				if((arr1[i] != null && arr2[i] == null) || (arr1[i] == null && arr2[i] != null)) {
					return false;
				} else if((arr1[i] != null && arr2[i] != null) && !arr1[i].equals(arr2[i]))
					return false;
			}
		}
		return true;
	}

}
