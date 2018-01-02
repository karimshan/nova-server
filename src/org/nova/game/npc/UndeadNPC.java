package org.nova.game.npc;
/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class UndeadNPC {
	private static final Object [][][] UNDEAD_NPCS= {getUNDEAD_NPC()};

	 private static Object[][] UNDEAD_NPC = { {1631}, {234} };

	public static Object[][] getUNDEAD_NPC() {
		return UNDEAD_NPC;
	}

	public static void setUNDEAD_NPC(Object[][] uNDEAD_NPC) {
		UNDEAD_NPC = uNDEAD_NPC;
	}

	public static Object[][][] getUndeadNpcs() {
		return UNDEAD_NPCS;
	}
}
