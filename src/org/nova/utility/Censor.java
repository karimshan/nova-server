package org.nova.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.nova.utility.misc.Misc;

/**
 * Made by: K-Shan
 */
public class Censor {
	
	private final static List<String> censored = new ArrayList<String>();
	
	public static void initialize() {
		censored.clear();
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/misc/censor.txt")));
			while (true) {
				String line = r.readLine();
				if (line == null)
					break;
				if (line.startsWith("//") || line.equals(""))
					continue;
				censored.add(line);
			}	
			//System.err.println("[Censor] Loaded "+censored.size()+" words.");
			r.close();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}

	public static String filter(String message) {
		message = message.toLowerCase();
		for (String word : censored()) {
		    if (message.contains(word)) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < word.length(); i++)
			    sb.append("*");
			message = message.replace(word, sb.toString());
		    }
		}
		return Misc.fixChatMessage(message);
	}
	
	public static final List<String> censored() {
		return censored;
	}

}
