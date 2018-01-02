package org.nova.kshan.randoms.impl;

import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;

/**
 * Good ol' Sergeant Damien
 * @author karim
 *
 */
public class DrillDemon extends RandomEvent {
	
	// The models of the exercises to send on the chat interface
	private final int PUSH_UPS = 9789, JOG_IN_PLACE = 9790, SIT_UPS = 9791, STAR_JUMPS = 9792;
	
	// The IDs of the exercise mat signs
	private final int SITUPS_SIGN = 10072, PUSHUPS_SIGN = 10073, STARJUMPS_SIGN = 10074, JOG_SIGN = 10075;
	
	// The interface id when the player is asked to go on a specific mat
	private final int ALERT_INTERFACE = 26;

	@Override
	public void startEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getNPCId() {
		return 2790;
	}

	@Override
	public String[] getNPCMessages() {
		return new String[] { 
			"You've been slacking private, and I will not have it!",
			"Private "+player.getDisplayName()+"! Get yourself to the parade ground ASAP!",
			"It's time for some drills private!",
			"Do you think you can be the best?"
		};
	}

	@Override
	public boolean checkLogin() {
		// Event Cancelled
		return true;
	}

	@Override
	public boolean checkLogout() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canTeleport() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasNPCOption1(NPC npc) {
		return true;
	}

	@Override
	public boolean hasObjectOption1(GlobalObject obj) {
		return true;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId) {
		return true;
	}

	@Override
	public Dialogue getDialogue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getEventLocation() {
		return new Location(baseX + 27, baseY + 20, 0);
	}

	@Override
	public boolean hasHiddenMiniMap() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer[] getTabsRemoved() {
		return new Integer[] {
				tab(ATTACK_STYLES), 
				tab(QUESTS), 
				tab(ACHIEVEMENTS),
				tab(SKILLS),
				tab(EQUIPMENT),
				tab(INVENTORY),
				tab(PRAYER),
				tab(MAGIC_BOOK), 
				tab(EMOTES),
				tab(NOTES),
				tab(GAME_SETTINGS)
		};
	}

	@Override
	public boolean isTraditional() {
		return true;
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		return new Integer[] { 392, 600, 8, 8 };
	}

}
