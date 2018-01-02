package org.nova.kshan.dialogues.testscript;

import java.util.HashMap;
import java.util.Map;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.Player;
import org.nova.kshan.dialogues.Dialogue;

/**
 * 
 * @author K-Shan
 *
 */
public class DialogueScript {
	
	protected transient Player player;
	protected Map<Integer, DialogueScript> cachedDialogues = new HashMap<Integer, DialogueScript>();
	protected String[] lines;
	protected int stage;
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void open() {
		stage = 0;
		DialogueScript initial = cachedDialogues.get(stage);
		if(initial instanceof NPCDialogueScript)
			npc(((NPCDialogueScript) initial).getNPC().getId(), initial.getLines());
		else if(initial instanceof PlayerDialogueScript)
			player(initial.getLines());
		else if(initial instanceof OptionsDialogueScript)
			options(((OptionsDialogueScript) initial).getTitle(), initial.getLines());
		stage++;
	}
	
	public void nextStage(int interfaceId, int buttonId) {
		if(stage == cachedDialogues.size()) {
			terminate();
			return;
		}
		DialogueScript nextDialogue = cachedDialogues.get(stage);
		if(nextDialogue instanceof NPCDialogueScript)
			npc(((NPCDialogueScript) nextDialogue).getNPC().getId(), nextDialogue.getLines());
		else if(nextDialogue instanceof PlayerDialogueScript)
			player(nextDialogue.getLines());
		else if(nextDialogue instanceof OptionsDialogueScript)
			options(((OptionsDialogueScript) nextDialogue).getTitle(), nextDialogue.getLines());
		stage++;
	}
	
	public void terminate() {
		player.getDialogueScript().terminate();
	}
	
	public void player(String... lines) {
		sendEntityDialogue((byte) 0, Dialogue.HAPPY_TALKING, -1, player.getDisplayName(), lines);
	}
	
	public void npc(int id, String... lines) {
		sendEntityDialogue((byte) 1, Dialogue.HAPPY_TALKING, id, NPCDefinition.get(id).getName().equals(null) 
			? "Fix the name for npc: " + id : NPCDefinition.get(id).getName(), lines);
	}
	
	public void sendEntityDialogue(byte entity, int animId, int id,
			String title, String... lines) {
		if (entity == 0) {
			player.packets().sendChatBoxInterface(checkPlayerLines(lines));
			if(checkPlayerLines(lines) == 68) {
				player.packets().sendPlayerOnInterface(checkPlayerLines(lines), 1);
				player.packets().animateInterface(animId, checkPlayerLines(lines), 1);
			} else {
				player.packets().sendPlayerOnInterface(checkPlayerLines(lines), 2);
				player.packets().animateInterface(animId, checkPlayerLines(lines), 2);
			}
			player.packets().sendString(title, checkPlayerLines(lines), 3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i], checkPlayerLines(lines), i + 4);
		} else {
			player.packets().sendChatBoxInterface(checkNPCLines(lines));
			player.packets().animateInterface(animId, checkNPCLines(lines), 2);
			player.packets().sendNPCOnInterface(id, checkNPCLines(lines), 2);
			player.packets().sendString(title, checkNPCLines(lines), 3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i], checkNPCLines(lines), i + 4);
		}
	}

	public void options(String title, String... options) {
		int length = options.length;
		player.packets().sendChatBoxInterface(checkOptions(length));
		player.packets().sendString(title, checkOptions(length), checkOptions(length) == 231 ? 1 : 0);
		for (int i = 0; i < options.length; i++)
			player.packets().sendString(options[i], checkOptions(length), checkOptions(length) == 231 ? i + 2 : i + 1);
		checkTitleSwords(options.length);
	}
	
	public void checkTitleSwords(int optionsFilled) {
		switch(checkOptions(optionsFilled)) {
			case 236:
				player.packets().sendHideIComponent(236, 5, true);
				player.packets().sendHideIComponent(236, 6, false);
				break;
			case 231:
				player.packets().sendHideIComponent(231, 5, true);
				player.packets().sendHideIComponent(231, 6, true);
				player.packets().sendHideIComponent(231, 10, false);
				break;
			case 237:
				player.packets().sendHideIComponent(237, 5, true);
				player.packets().sendHideIComponent(237, 6, true);
				player.packets().sendHideIComponent(237, 8, false);
				break;
			case 238:
				player.packets().sendHideIComponent(238, 6, true);
				player.packets().sendHideIComponent(238, 7, true);
				player.packets().sendHideIComponent(238, 9, false);
				break;
		}
	}
	
	public int checkOptions(int options) {
		if (options <= 1 || options >= 6)
			return 236;
		return options == 2 ? 236 : options == 3 ? 231 : options == 4 ? 237 : 238;
	}
	
	public int checkNPCLines(String[] lines) {
		return lines.length == 1 ? 241 : lines.length == 2 ? 242 : lines.length == 3 ? 243 : 244;
	}

	public int checkPlayerLines(String[] lines) {
		return lines.length == 1 ? 64 : lines.length == 2 ? 65 : lines.length == 3 ? 66 : 67;
	}

	public int getDialogueCount() {
		return cachedDialogues.size();
	}
	
	public String[] getLines() {
		return lines;
	}
	
}
