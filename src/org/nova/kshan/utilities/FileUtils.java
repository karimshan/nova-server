package org.nova.kshan.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

/**
 * 
 * @author K-Shan
 *
 */
public class FileUtils {
	
	/**
	 * Returns the lines in the specified file.
	 * @param file
	 * @return
	 */
	public static String[] getLines(File file) {
		String[] excludedSymbols = { "#", "//" };
		return getLines(file, excludedSymbols, true);
	}
	
	/**
	 * Returns the lines in the specified file.
	 * @param file
	 * @param excludeBlankLines
	 * @return
	 */
	public static String[] getLines(File file, boolean excludeBlankLines) {
		String[] excludedSymbols = { "#", "//" };
		return getLines(file, excludedSymbols, excludeBlankLines);
	}
	
	/**
	 * Returns the lines in the specified file.
	 * @param file
	 * @param excludedSymbols
	 * @param excludeBlankLines
	 * @return
	 */
	public static String[] getLines(File file, String[] excludedSymbols, boolean excludeBlankLines) {
		try {
			int lineCount = 0;
			BufferedReader r = null;
			r = new BufferedReader(new FileReader(file));
			String line;
			while((line = r.readLine()) != null) {
				for(String s : excludedSymbols)
					if(line.startsWith(s))
						continue;
				if(excludeBlankLines)
					if(line.equals(""))
						continue;
				lineCount++;
			}	
			r.close();
			r = new BufferedReader(new FileReader(file));
			String[] lines = new String[lineCount];
			int index = 0;
			while((line = r.readLine()) != null) {
				for(String s : excludedSymbols)
					if(line.startsWith(s))
						continue;
				if(excludeBlankLines)
					if(line.equals(""))
						continue;
				lines[index] = line;
				index++;
			}	
			return lines;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Prints out the fields of the specified class object.
	 * @param def
	 */
	public static void printFields(Object classObject) {
		Field[] fields = classObject.getClass().getDeclaredFields();
		for(Field f : fields) {
			f.setAccessible(true);
			try {
				System.out.println("["+classObject.getClass().getSimpleName()+"] Field: "+f.getName()+" - "+f.get(classObject));
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Returns the fields of the specified class object.
	 * @param def
	 */
	public static Field[] getFields(Object classObject) {
		return classObject.getClass().getDeclaredFields();
	}
	
}
