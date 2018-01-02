package org.nova.game.npc.others;

import java.util.List;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.OwnedObjectManager;
import org.nova.game.player.OwnedObjectManager.ConvertEvent;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Hunter.HunterNPC;


@SuppressWarnings("serial")
public class HuntNPC extends NPC {

	public HuntNPC(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		List<GlobalObject> objects = Game.getRegion(getRegionId())
				.getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (GlobalObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object,
							new GlobalObject(info.getTransformObjectId(), 10, 0,
									this.getX(), this.getY(), this.getZ()),
							new ConvertEvent() {
								@Override
								public boolean canConvert(Player player) {
									if (player == null)
										return false;
									return player.getSkills().getLevel(
											Skills.HUNTER) >= info.getLevel();
								}
							})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}
}
