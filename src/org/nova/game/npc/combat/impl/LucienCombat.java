package org.nova.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


public class LucienCombat extends CombatScript {
 private Player player;
	@Override
	public Object[] getKeys() {
		return new Object[] { 14256 };
	}

	@Override
	public int attack(final NPC npc, final Entity target) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		int attackStyle = Misc.getRandom(5);

		if (Misc.getRandom(10) == 0) {
			ArrayList<Entity> possibleTargets = npc.getPossibleTargets();
			final HashMap<String, int[]> tiles = new HashMap<String, int[]>();
			for (Entity t : possibleTargets) {
				if (t instanceof Player) {
					Player p = (Player) t;
					if (!p.getMusicsManager().hasMusic(1008)) {
						p.getMusicsManager().playMusic(581);
						p.getMusicsManager().playMusic(584);
						p.getMusicsManager().playMusic(579);
						p.getMusicsManager().playMusic(1008);
					}
				}
				String key = t.getX() + "_" + t.getY();
				if (!tiles.containsKey(t.getX() + "_" + t.getY())) {
					tiles.put(key, new int[] { t.getX(), t.getY() });
					Game.sendProjectile(npc, new Location(t.getX(), t.getY(),
							npc.getZ()), 1900, 34, 0, 30, 35, 16, 0);
				}
			}
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					ArrayList<Entity> possibleTargets = npc
							.getPossibleTargets();
					for (int[] tile : tiles.values()) {

						Game.sendGraphics(null, new Graphics(1896),
								new Location(tile[0], tile[1], 0));
						for (Entity t : possibleTargets)
							if (t.getX() == tile[0] && t.getY() == tile[1])
								delayHit(
										npc,
										2,
										target,
										getMagicHit(
												npc,
												getRandomMaxHit(npc, defs.getMaxHit(),
														NPCCombatDefinitions.MAGE, target)));
					}
					stop();
				}

			}, 5);
		} else if (Misc.getRandom(10) == 0) {
			npc.setNextGraphics(new Graphics(444));
			npc.heal(500);
		}
		if (attackStyle == 0) { // normal mage move
			npc.setNextAnimation(new Animation(11338));
			delayHit(
					npc,
					2,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MAGE, target)));
			Game.sendProjectile(npc, target, 2963, 34, 16, 40, 35, 16, 0);
		} else if (attackStyle == 1) { // normal mage move
			npc.setNextAnimation(new Animation(11338));
			delayHit(
					npc,
					2,
					target,
					getRangeHit(
							npc,
							getRandomMaxHit(npc, 470,
									NPCCombatDefinitions.RANGE, target)));
			Game.sendProjectile(npc, target, 1904, 34, 16, 30, 35, 16, 0);

			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					target.setNextGraphics(new Graphics(1910));
				}

			}, 2);

		} else if (attackStyle == 2) {
			npc.setNextAnimation(new Animation(11318));
			npc.setNextGraphics(new Graphics(1901));
			Game.sendProjectile(npc, target, 1899, 34, 16, 30, 95, 16, 0);
			delayHit(
					npc,
					4,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MAGE, target)));
		} else if (attackStyle == 3) {
			npc.setNextAnimation(new Animation(11373));
			npc.setNextGraphics(new Graphics(1898));
			target.setNextGraphics(new Graphics(2954));
			delayHit(
					npc,
					4,
					target,
					getMagicHit(
							npc,
							getRandomMaxHit(npc, defs.getMaxHit(),
									NPCCombatDefinitions.MAGE, target)));
		} else if (attackStyle == 4) {
			npc.setNextAnimation(new Animation(11364));
			npc.setNextGraphics(new Graphics(2600));
			npc.setCantInteract(true);
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					for (Entity t : npc.getPossibleTargets()) {
						delayHit(
								npc,
								2,
								target,
								getMagicHit(
										npc,
										getRandomMaxHit(npc, defs.getMaxHit(),
												NPCCombatDefinitions.RANGE, target)));
					}
					npc.getCombat().addCombatDelay(3);
					npc.setCantInteract(false);
					npc.setTarget(target);
				}

			}, 4);
			return 0;
		} else if (attackStyle == 5) {
			npc.setCantInteract(true);
			npc.setNextAnimation(new Animation(11319));
			npc.getCombat().removeTarget();
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					npc.setCantInteract(false);
					npc.setTarget(target);
					int size = npc.getSize();
					int[][] dirs = Misc.getCoordOffsetsNear(size);
					for (int dir = 0; dir < dirs[0].length; dir++) {
						final Location tile = new Location(new Location(
								target.getX() + dirs[0][dir], target.getY()
										+ dirs[1][dir], target.getZ()));
						if (Game.canMoveNPC(tile.getZ(), tile.getX(),
								tile.getY(), size)) { // if found done
							npc.setLocation(tile);
						}
					}
				}
			}, 3);
			return defs.getAttackDelay();
		}

		return defs.getAttackDelay();
	}
}
