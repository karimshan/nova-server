package org.nova.kshan.content.minigames.duelarena;

import org.nova.game.entity.Entity;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.RandomEvent;

/**
 * 
 * @author K-Shan
 *
 */
public class DuelArena extends RandomEvent {

	@Override
	public void startEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getNPCId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getNPCMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean checkLogin() {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasObjectOption1(GlobalObject obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canAttackEntity(Entity entity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canClickButtons(int interfaceId, int buttonId, int slotId,
			int packetId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Dialogue getDialogue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getEventLocation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasHiddenMiniMap() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer[] getTabsRemoved() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTraditional() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer[] getDynamicMapVariables() {
		// TODO Auto-generated method stub
		return null;
	}

}
