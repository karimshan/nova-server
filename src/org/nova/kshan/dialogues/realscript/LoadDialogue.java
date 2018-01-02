package org.nova.kshan.dialogues.realscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Field;

import org.nova.cache.Cache;
import org.nova.game.player.Player;
import org.nova.utility.loading.playerutils.SFiles;

public class LoadDialogue {
	
	public static void loadDialogues() {
		try {
			final File PATH = new File("./data/scripts/dialogues/");
			File[] files = PATH.listFiles();
			int dialogueCount = 0;
			for(int i = 0; i < files.length; i++) {
				File dialogueFile = new File("./data/scripts/dialogues/"+files[i].getName());
				BufferedReader r = null;
				r = new BufferedReader(new FileReader(dialogueFile));
				String line;
				int lineCount = 0;
				// Lexical analyzer
				while((line = r.readLine()) != null) {
					if(line.startsWith("//"))
						continue;
					if(line.equals(""))
						continue;
					lineCount++;
				}
				System.out.println("Line count: "+lineCount);
				r.close();
				dialogueCount++;
			}
			System.out.println("Loaded "+dialogueCount+" dialogues");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		loadDialogues();
		Cache.load();
		Player testP = SFiles.loadPlayer("lucifer");
		for(Field f : testP.getClass().getDeclaredFields()) {
			f.setAccessible(true);
			if(f.getName().toLowerCase().contains("combatxpboost")) {
				System.out.println(f.getName()+" - "+f.getType()+" - "+f.get(testP));
				f.set(testP, 8472.8);
				System.out.println(f.getName()+" - "+f.getType()+" - "+f.get(testP));
			}
		}
	}

}
