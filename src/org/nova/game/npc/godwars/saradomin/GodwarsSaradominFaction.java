package org.nova.game.npc.godwars.saradomin;

import java.util.ArrayList;
import java.util.List;

import org.nova.game.Game;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


@SuppressWarnings("serial")
public class GodwarsSaradominFaction extends NPC {

	public GodwarsSaradominFaction(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>();
		for (int regionId : getMapRegionsIds()) {
			List<Integer> playerIndexes = Game.getRegion(regionId)
					.getPlayerIndexes();
			if (playerIndexes != null) {
				for (int npcIndex : playerIndexes) {
					Player player = Game.getPlayers().get(npcIndex);
					if (player == null
							|| player.isDead()
							|| player.hasFinished()
							|| !player.isRunning()
							|| !player
									.withinDistance(
											this,
											getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
													: getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
															: 8)
							|| ((!isAtMultiArea() || !player.isAtMultiArea())
									&& player.getAttackedBy() != this && player
									.getAttackedByDelay() > Misc
									.currentTimeMillis())
							|| !clipedProjectile(player, false))
						continue;
					possibleTarget.add(player);
				}
			}
			List<Integer> npcsIndexes = Game.getRegion(regionId)
					.getNPCsIndexes();
			if (npcsIndexes != null) {
				for (int npcIndex : npcsIndexes) {
					NPC npc = Game.getNPCs().get(npcIndex);
					if (npc == null
							|| npc == this
							|| npc instanceof GodwarsSaradominFaction
							|| npc.isDead()
							|| npc.hasFinished()
							|| !npc.withinDistance(
									this,
									getCombatDefinitions().getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
											: getCombatDefinitions()
													.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 16
													: 8)
							|| !npc.defs().hasAttackOption()
							|| ((!isAtMultiArea() || !npc.isAtMultiArea())
									&& npc.getAttackedBy() != this && npc
									.getAttackedByDelay() > Misc
									.currentTimeMillis())
							|| !clipedProjectile(npc, false))
						continue;
					possibleTarget.add(npc);
				}
			}
		}
		return possibleTarget;
	}

}
