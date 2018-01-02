package org.nova.kshan.dialogues.testscript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.nova.cache.Cache;
import org.nova.game.npc.NPC;
import org.nova.kshan.utilities.FileUtils;

/**
 * DialogueScripts class.
 * 
 * @author K-Shan
 *
 */
public class DialogueScripts {
	
	private static final HashMap<String, DialogueScript> DIALOGUES = new HashMap<String, DialogueScript>();

	public static final void load() {
		try {
			final File PATH = new File("./data/scripts/");
			File[] files = PATH.listFiles();
			int dialogueCount = 0;
			for(int i = 0; i < files.length; i++) {
				File dialogueFile = new File("./data/scripts/"+files[i].getName());
				if(dialogueFile.isDirectory())
					continue;
				BufferedReader r = null;
				NPC npc = null;
				DialogueScript newInstance = new DialogueScript();
				int stageCount = 0;
				r = new BufferedReader(new FileReader(dialogueFile));
				String line;
				// Reading options
				while((line = r.readLine()) != null) {
					if(line.startsWith("//"))
						continue;
					if(!line.contains("options="))
						continue;
					if(line.equals(""))
						continue;
					String[] optionTokens = line.split(" : ");
					String title = optionTokens[0].split("options=")[1];
					String[] options = optionTokens[1].split(" - ");
					for(int j = 0; j < options.length; j++)
						if(options[j].contains(" {"))
							options[j] = options[j].replace(" {", "");
					newInstance.cachedDialogues.put(stageCount, new OptionsDialogueScript(title, options));
					stageCount++;
				}
				r.close();
				r = new BufferedReader(new FileReader(dialogueFile));
				// Reading NPC ID
				while((line = r.readLine()) != null) {
					if(line.startsWith("//"))
						continue;
					if(line.equals(""))
						continue;
					if(line.startsWith("NPC="))
						npc = NPC.createBlankNPC(Integer.parseInt(line.split("NPC=")[1]));
				}
				r.close();
				r = new BufferedReader(new FileReader(dialogueFile));
				// Reading npc and player dialogue
				while((line = r.readLine()) != null) {
					if(line.startsWith("//"))
						continue;
					if(!line.contains("player : ") && !line.contains("npc : "))
						continue;
					if(line.equals(""))
						continue;
					DialogueScript toPut = null;
					String[] primaryTokens = line.split(" : ");
					String entity = primaryTokens[0];
					String[] lines = primaryTokens[1].split(" - ");
					if(entity.contains("npc"))
						toPut = new NPCDialogueScript(npc, lines);
					else
						toPut = new PlayerDialogueScript(lines);
					newInstance.cachedDialogues.put(stageCount, toPut);
					stageCount++;
				}
				r.close();
				DIALOGUES.put(files[i].getName().replace(".ds", ""), newInstance);
				dialogueCount++;
			}
			System.out.println("Loaded "+dialogueCount+" dialogues.");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Cache.load();
		load();
		String[] lines = FileUtils.getLines(new File("data/scripts/lumbridge_man.ds"));
		for(int i = 0; i < lines.length; i++) {
			if(lines[i].contains(Instructions.EXECUTE.getType())) {
				String line = lines[i];
				String action = line.substring(line.indexOf("~") + 1);
				System.out.println("Action: "+action);
			}
		}
	}
	
	public static final void reload() {
		DIALOGUES.clear();
		load();
	}

	public static final DialogueScript getDialogue(String name) {
		return DIALOGUES.get(name);
	}

}
