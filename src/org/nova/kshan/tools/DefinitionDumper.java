package org.nova.kshan.tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.cache.definition.ObjectDefinition;
import org.nova.cache.loaders.RenderDefinition;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class DefinitionDumper {
	
//	public static void main(String[] args) {
//		Cache.init();
//		dumpNPCs();
//	}
	
	public static final void dumpItems() {
		try {
			FileWriter fstream = new FileWriter("data/text/defs/ItemList.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			for(int i = 0; i <= Misc.getItemsSize(); i++) {
				ItemDefinition def = ItemDefinition.get(i);
				out.write(def.getId()+" - "+def.getName());
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static final void dumpNPCs() {
		int npcId = 0;
		try {
			FileWriter fstream = new FileWriter("data/misc/defs/NPCList.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			for(int i = 0; i < Misc.getNPCsSize(); i++) {
				NPCDefinition def = NPCDefinition.get(i);
				int render = def.renderEmote;
				int standingEmote = render == -1 ? -1 : RenderDefinition.get(render).standingEmoteId;
				int[] models = def.modelIds == null ? new int[] { -1 } : def.modelIds;
				out.write(def.id+" - "+def.name+" - Render: "+render+" (Standing emote: "+standingEmote+") - Model Ids: "+Arrays.toString(models)+" - Combat Level: "+def.combatLevel);
				Logger.log("Server", ""+def.getId()+" "+def.name);
				out.newLine();
				npcId = i;
			}
			out.close();
		} catch (Exception e) {
			System.out.println(npcId+" threw an error.");
			e.printStackTrace();
		}
	}
	
	public static final void dumpObjects() {
		try {
			FileWriter fstream = new FileWriter("data/text/defs/ObjectList.txt", true);
			BufferedWriter out = new BufferedWriter(fstream);
			for(int i = 0; i <= Misc.getObjectsSize(); i++) {
				ObjectDefinition def = ObjectDefinition.get(i);
				out.write(def.id+" - "+def.name+" - Clipped: "+def.projectileClipped+" - Animation: "+def.objectAnimation);
				out.newLine();
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
