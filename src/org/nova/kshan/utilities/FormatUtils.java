package org.nova.kshan.utilities;

/**
 * Made by: K-Shan
 */
public class FormatUtils {

	/**
	 * Formats a name for examine
	 */
	public static String formatForExamine(String name, boolean use) {
		String def = "";
		char[] vowels = { 'a', 'e', 'i', 'o', 'u' };
		for(char v : vowels)
			if(name.toLowerCase().charAt(0) != v)
				def = new StringBuilder().append(""+(use ? "It's " : "")+"a "+name+".").toString();
		for(char v : vowels)
			if(name.toLowerCase().charAt(0) == v)
				def = new StringBuilder().append(""+(use ? "It's " : "")+"an "+name+".").toString();
		if(name.charAt(name.length() - 1) == 's')
			def = new StringBuilder().append(""+(use ? "They're " : "")+""+name+".").toString();
		return def;
	}

	/**
	 * 
	 * @param name
	 * @return
	 */
	public static String formatForExamine(String name) {
		return formatForExamine(name, true);
	}
}
