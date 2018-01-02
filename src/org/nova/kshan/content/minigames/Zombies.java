package org.nova.kshan.content.minigames;

import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;

public class Zombies extends RandomEvent {

	@Override
	public void startEvent() {
		player.getDialogue().sendMsg("The time has come for you to die.");
	}

	@Override
	public Integer getNPCId() {
		return -1;
	}

	@Override
	public String[] getNPCMessages() {
		return null;
	}

	@Override
	public boolean checkLogin() {
		player.sm("BRUH ZOMBIES BOUT TO ATTACK YOU, RUN!!!!");
		return false;
	}

	@Override
	public boolean checkLogout() {
		return false;
	}

	@Override
	public boolean canTeleport() {
		fullyStop();
		return true;
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
		return true;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId) {
		return true;
	}

	@Override
	public Dialogue getDialogue() {
		return null;
	}

	@Override
	public Location getEventLocation() {
		return getDynamicMap().getCenter();
	}

	@Override
	public boolean hasHiddenMiniMap() {
		return false;
	}

	@Override
	public Integer[] getTabsRemoved() {
		return null;
	}

	@Override
	public boolean isTraditional() {
		return true;
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		return new Integer[] { 205, 710, 5, 4 };
	}

}
