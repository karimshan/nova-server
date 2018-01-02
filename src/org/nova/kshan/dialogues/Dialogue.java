package org.nova.kshan.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.Player;

/*
 * Edited by: K-Shan
 */
public abstract class Dialogue {

	protected Player player;
	protected int stage = 0;

	public Object[] data;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public abstract void start();

	public abstract void process(int interfaceId, int buttonId);

	public abstract void finish();

	protected final void end() {
		player.getDialogue().finishDialogue();
	}

	public static final int REALLY_SAD = 9760, SAD = 9765, DEPRESSED = 9770,
			WORRIED = 9775, SCARED = 9780, MEAN_FACE = 9785,
			MEAN_HEAD_BANG = 9790, EVIL = 9795, WHAT_THE_CRAP = 9800,
			CALM = 9805, CALM_TALK = 9810, TOUGH = 9815, SNOBBY = 9820,
			SNOBBY_HEAD_MOVE = 9825, CONFUSED = 9830, DRUNK_HAPPY_TIRED = 9835,
			TALKING_ALOT = 9845, HAPPY_TALKING = 9850, BAD_ASS = 9855,
			THINKING = 9860, COOL_YES = 9864, LAUGH_EXCITED = 9851,
			SECRELTY_TALKING = 9838, CRYING = 9765, SHY = 9770, MAD = 9785,
			NORMAL_TALKING = 9827, VERY_ANGRY = 9790;

	protected final String TITLE = "Select an Option";
	
	protected final short TWO_OPTIONS = 236;
	protected final short THREE_OPTIONS = 231;
	protected final short FOUR_OPTIONS = 237;
	protected final short FIVE_OPTIONS = 238;

	protected final byte PLAYER = 0;
	protected final byte NPC = 1;

	protected final byte OPTION1 = 1, OPTION2 = 2, OPTION3 = 3,
			OPTION4 = 4, OPTION5 = 5, OPTION1_OTHER = 2, OPTION2_OTHER = 3,
			OPTION3_OTHER = 4;

	public int checkOptions(String[] options) {
		if (options.length <= 1 || options.length >= 6)
			return 236;
		return options.length == 2 ? 236
				: options.length == 3 ? 231
						: options.length == 4 ? 237 : 238;
	}
	
	public void checkTitleSwords(int optionsFilled) {
		if (checkOptions(optionsFilled) == 236) {
			player.packets().sendHideIComponent(236, 5, true);
			player.packets().sendHideIComponent(236, 6, false);
		} else if (checkOptions(optionsFilled) == 231) {
			player.packets().sendHideIComponent(231, 5, true);
			player.packets().sendHideIComponent(231, 6, true);
			player.packets().sendHideIComponent(231, 10, false);
		} else if (checkOptions(optionsFilled) == 237) {
			player.packets().sendHideIComponent(237, 5, true);
			player.packets().sendHideIComponent(237, 6, true);
			player.packets().sendHideIComponent(237, 8, false);
		} else if (checkOptions(optionsFilled) == 238) {
			player.packets().sendHideIComponent(238, 6, true);
			player.packets().sendHideIComponent(238, 7, true);
			player.packets().sendHideIComponent(238, 9, false);
		}
	}

	public int checkOptions(int options) {
		if (options <= 1 || options >= 6)
			return 236;
		return options == 2 ? 236 : options == 3 ? 231 : options == 4 ? 237 : 238;
	}

	public void sendOptions(String title, String... options) {
		player.packets().sendChatBoxInterface(checkOptions(options));
		player.packets().sendString(title, checkOptions(options),
				checkOptions(options) == 231 ? 1 : 0);
		for (int i = 0; i < options.length; i++)
			player.packets().sendString(options[i], checkOptions(options),
					checkOptions(options) == 231 ? i + 2 : i + 1);
		checkTitleSwords(options.length);
	}

	public static int checkInfoLines(String[] lines, boolean noContinue) {
		if (noContinue)
			return lines.length == 1 ? 215
					: lines.length == 2 ? 216
							: lines.length == 3 ? 217
									: lines.length == 4 ? 218
											: 219;
		else
			return lines.length == 1 ? 210
					: lines.length == 2 ? 211
							: lines.length == 3 ? 212
									: lines.length == 4 ? 213
											: 214;
	}

	public void sendLines(String[] lines, boolean noContinue) {
		player.packets()
				.sendChatBoxInterface(checkInfoLines(lines, noContinue));
		for (int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i],
					checkInfoLines(lines, noContinue), i + 1);
	}

	public void sendLines(boolean noContinue, String... lines) {
		player.packets()
				.sendChatBoxInterface(checkInfoLines(lines, noContinue));
		for (int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i],
					checkInfoLines(lines, noContinue), i + 1);
	}

	public void sendLines(String... lines) {
		player.packets().sendChatBoxInterface(checkInfoLines(lines, false));
		for (int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i], checkInfoLines(lines, false),
					i + 1);
	}
	
	public static void sendLines(Player player, String... lines) {
		player.packets().sendChatBoxInterface(checkInfoLines(lines, false));
		for (int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i], checkInfoLines(lines, false),
					i + 1);
	}

	public int checkNPCLines(String[] lines, boolean noContinue) {
		if (noContinue)
			return lines.length == 1 ? 245
					: lines.length == 2 ? 246
							: lines.length == 3 ? 247
									: 248;
		else
			return lines.length == 1 ? 241
					: lines.length == 2 ? 242
							: lines.length == 3 ? 243 : 244;
	}

	public int checkPlayerLines(String[] lines, boolean noContinue) {
		if (noContinue)
			return lines.length == 1 ? 68
					: lines.length == 2 ? 69
							: lines.length == 3 ? 70
									: 71;
		else
			return lines.length == 1 ? 64
					: lines.length == 2 ? 65
							: lines.length == 3 ? 66
									: 67;
	}

	public void sendEntityDialogue(byte entity, int animId, int id,
			String title, boolean noContinue, String... lines) {
		if (entity == PLAYER) {
			player.packets().sendChatBoxInterface(
					checkPlayerLines(lines, noContinue));
			if (checkPlayerLines(lines, noContinue) == 68) {
				player.packets().sendPlayerOnInterface(
						checkPlayerLines(lines, noContinue), 1);
				player.packets().animateInterface(animId,
						checkPlayerLines(lines, noContinue), 1);
			} else {
				player.packets().sendPlayerOnInterface(
						checkPlayerLines(lines, noContinue), 2);
				player.packets().animateInterface(animId,
						checkPlayerLines(lines, noContinue), 2);
			}
			player.packets().sendString(title,
					checkPlayerLines(lines, noContinue), 3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i],
						checkPlayerLines(lines, noContinue), i + 4);
		} else {
			player.packets().sendChatBoxInterface(
					checkNPCLines(lines, noContinue));
			player.packets().animateInterface(animId,
					checkNPCLines(lines, noContinue), 2);
			player.packets().sendNPCOnInterface(id,
					checkNPCLines(lines, noContinue), 2);
			player.packets().sendString(title,
					checkNPCLines(lines, noContinue), 3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i],
						checkNPCLines(lines, noContinue), i + 4);
		}
	}

	public void sendEntityDialogue(byte entity, int animId, int id,
			String title, String... lines) {
		if (entity == PLAYER) {
			player.packets().sendChatBoxInterface(
					checkPlayerLines(lines, false));
			if (checkPlayerLines(lines, false) == 68) {
				player.packets().sendPlayerOnInterface(
						checkPlayerLines(lines, false), 1);
				player.packets().animateInterface(animId,
						checkPlayerLines(lines, false), 1);
			} else {
				player.packets().sendPlayerOnInterface(
						checkPlayerLines(lines, false), 2);
				player.packets().animateInterface(animId,
						checkPlayerLines(lines, false), 2);
			}
			player.packets().sendString(title, checkPlayerLines(lines, false),
					3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i],
						checkPlayerLines(lines, false), i + 4);
		} else {
			player.packets().sendChatBoxInterface(checkNPCLines(lines, false));
			player.packets().animateInterface(animId,
					checkNPCLines(lines, false), 2);
			player.packets().sendNPCOnInterface(id,
					checkNPCLines(lines, false), 2);
			player.packets().sendString(title, checkNPCLines(lines, false), 3);
			for (int i = 0; i < lines.length; i++)
				player.packets().sendString(lines[i],
						checkNPCLines(lines, false), i + 4);
		}
	}

	public int checkNPCLines(int length) {
		return length == 1 ? 241 : length == 2 ? 242
				: length == 3 ? 243 : 244;
	}

	public void sendPlayerDialogue(boolean noContinue, String... lines) {
		sendEntityDialogue(PLAYER, HAPPY_TALKING, -1, player.getDisplayName(),
				noContinue, lines);
	}

	public void sendNPCDialogue(int id, String name, boolean noContinue,
			String... lines) {
		sendEntityDialogue(NPC, HAPPY_TALKING, id, name, noContinue, lines);
	}

	public void player(String... lines) {
		sendEntityDialogue(PLAYER, HAPPY_TALKING, -1, player.getDisplayName(),
				false, lines);
	}
	
	public void player(int nextStage, String... lines) {
		sendPlayerDialogue(nextStage, lines);
	}
	
	public void sendPlayerDialogue(int nextStage, String... lines) {
		sendEntityDialogue(PLAYER, HAPPY_TALKING, -1, player.getDisplayName(),
				false, lines);
		this.stage = nextStage;
	}

	public void npc(int id, String... lines) {
		sendEntityDialogue(NPC, HAPPY_TALKING, id, NPCDefinition.get(id)
				.getName().equals(null) ? "Fix the name for npc: " + id
				: NPCDefinition.get(id).getName(), false, lines);
	}
	
	public void npc(int nextStage, int id, String... lines) {
		sendNPCDialogue(nextStage, id, lines);
	}
	
	public void sendNPCDialogue(int nextStage, int id, String... lines) {
		sendEntityDialogue(NPC, HAPPY_TALKING, id, NPCDefinition.get(id)
				.getName().equals(null) ? "Fix the name for npc: " + id
				: NPCDefinition.get(id).getName(), false, lines);
		this.stage = nextStage;
	}

	public void sendItems(int[] items, String line) {
		player.packets().sendChatBoxInterface(131);
		player.packets().sendItemOnIComponent(131, 0, items[0], items[1]);
		player.packets().sendItemOnIComponent(131, 2, items[2], items[3]);
		player.packets().sendString(line, 131, 1);
	}

	public void sendItemDialogue(Item item, String line) {
		player.packets().sendChatBoxInterface(519);
		player.packets().sendString(line, 519, 1);
		player.packets().sendItemOnIComponent(519, 0, item.getId(),
				item.getAmount());
	}

	public Dialogue(Player player) {
		this.player = player;
	}

	public Dialogue() {

	}

}