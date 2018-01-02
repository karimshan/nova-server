package org.nova.game.npc.combat.impl;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.npc.NPC;
import org.nova.game.npc.combat.CombatScript;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.player.Player;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class Assassin extends CombatScript {
	
	private transient boolean transformed;

	@Override
	public Object[] getKeys() {
		return new Object[] { "Assassin" };
	}
	
	@Override
	public int attack(final NPC npc, final Entity target) {
		final Player p = (Player) target;
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();
		defs.setCanAttackThroughPrayer(true);
		defs.setAttackDelay(5);
		if(npc.getId() == 14675) {
			int damage = getRandomMaxHit(npc, defs.getMaxHit(), defs.getAttackStyle(), target);
			int random = Misc.random(10);
			if(random == 0) {
				npc.setNextAnimation(new Animation(1074));
				npc.setNextGraphics(new Graphics(381));
				Game.sendProjectile(npc, target, 380, 20, 16, 55, 35, 16, 0);
				int newDamage = getRandomMaxHit(npc, 900, 1, target); // Lmao, he could 1 hit you ;)
				int newDamage2 = getRandomMaxHit(npc, 900, 1, target);
				if(p.usingProtectRange())
					defs.setRangeDamageModifier(.50); // Merlyn's special attack. does 50% damage even through prayer.
				Hit firstHit = getRangeHit(npc, newDamage);
				Hit secondHit = getRangeHit(npc, newDamage2);
				firstHit.setCriticalMark();
				secondHit.setCriticalMark();
				delayHit(npc, 2, target, firstHit);
				if(target instanceof Player)
					delayHit(npc, 2, target, secondHit);
				npc.forceTalk("<col=000000>FEEL THE POWER OF THE ASSASSIN");
				p.sm("Brace yourself for the assassin's ranged attack!");
			} else if(random == 4 || random == 7) {
				npc.setNextAnimation(new Animation(1074));
				npc.setNextGraphics(new Graphics(1993));
				Game.sendProjectile(npc, target, 380, 20, 16, 55, 35, 16, 0);
				int newDamage = getRandomMaxHit(npc, 400, 1, target);
				int newDamage2 = getRandomMaxHit(npc, 400, 1, target);
				if(p.usingProtectRange())
					defs.setRangeDamageModifier(.64); // Merlyn's special attack. does 64% damage even through prayer.
				Hit firstHit = getRangeHit(npc, newDamage);
				Hit secondHit = getRangeHit(npc, newDamage2);
				firstHit.setCriticalMark();
				secondHit.setCriticalMark();
				delayHit(npc, 2, target, firstHit);
				if(target instanceof Player)
					delayHit(npc, 2, target, secondHit);
				p.graphics(383);
				p.getPrayer().drainPrayer(Misc.random(10, 151));
				npc.forceTalk("Pathetic fool, you can't defeat me!");
				p.sm("The assassin drained your prayer.");
			} else {
				if(p.usingProtectRange())
					defs.setRangeDamageModifier(.195); // Now merlyn can hit through prayer, but only 1/5 of the original damage
				delayHit(npc, 2, target, defs.getAttackStyle() == NPCCombatDefinitions.RANGE ? getRangeHit(npc, damage) : getMagicHit(npc, damage));
				if (defs.getAttackProjectile() != -1)
					Game.sendProjectile(npc, target, defs.getAttackProjectile(), 41, 16, 41, 35, 16, 0);
				if (defs.getAttackGfx() != -1)
					npc.setNextGraphics(new Graphics(defs.getAttackGfx()));
				npc.setNextAnimation(new Animation(defs.getAttackEmote()));
			}
			if(!transformed && npc.getHitpoints() <= (npc.getMaxHitpoints() * .50)) {
				npc.setCantInteract(true);
				npc.setAttackedBy(null);
				p.setAttackedBy(null);
				p.resetReceivedDamage();
				npc.resetReceivedDamage();
				p.resetCombat();
				npc.resetCombat();
				npc.reset();
				p.stopAll();
				transformed = true;
				npc.animate(843);
				npc.forceTalk("I will not be defeated!");
				p.sm("Watch out, the assassin is about to gain a new ability!");
				Game.submit(new GameTick(1) {
					int ticks;
					NPC newNPC;
					Location toSpawn;
					public void run() {
						ticks++;
						if(ticks == 3) {
							npc.resetMasks();
						} else if(ticks == 4) {
							toSpawn = new Location(npc.getX(), npc.getY(), p.getZ());
							npc.finish();
						} else if(ticks == 5) {
							newNPC = new NPC(14676, toSpawn, false);
							newNPC.forceTalk("Gaaaarrrhhhhhh!");
							newNPC.animate(2563);
							newNPC.graphics(365);
							p.sm("<col=b00002>Quick, turn on your magic prayers!");
							transformed = false;
						} else if(ticks == 11) {
							stop();
							newNPC.setTarget(p);
							p.getActionManager().setAction(new PlayerCombat(newNPC));
						}
					}
				});
				
			}
		} else if(npc.getId() == 14676) {
			int random = Misc.random(10);
			if(random == 0) {
				npc.setNextAnimation(new Animation(1979));
				npc.setNextGraphics(new Graphics(2795));
				Game.sendProjectile(npc, target, 2704, 20, 16, 55, 35, 16, 0);
				Hit firstHit = getMagicHit(npc, getRandomMaxHit(npc, 884, 2, target));
				Hit secondHit = getMagicHit(npc, getRandomMaxHit(npc, 865, 2, target));
				firstHit.setCriticalMark();
				secondHit.setCriticalMark();
				Game.submit(new GameTick(.6) {
					public void run() {
						stop();
						if(!target.isDead())
							target.graphics(2754);
					}
				});
				if(p.usingProtectMagic())
					defs.setMagicDamageModifier(.50);
				delayHit(npc, 2, target, firstHit);
				delayHit(npc, 2, target, secondHit);
				npc.forceTalk("<col=00005c>FEEL THE POWER OF THE ASSASSIN");
				p.sm("Brace yourself for the assassin's magic attack!");
			} else if(random == 4 || random == 7) {
				npc.setLocation(new Location(p.getX() + 1 + Misc.random(4), p.getY() - 1 + Misc.random(5), p.getZ()));
				Game.submit(new GameTick(1.0) {
					public void run() {
						stop();
						npc.setNextAnimation(new Animation(1979));
						p.packets().sendSound(171, 0, 1);
						Game.sendProjectile(npc, target, 368, 60, 32, 50, 50, 0, 0);
						Hit firstHit = getMagicHit(npc, getRandomMaxHit(npc, 484, 2, target));
						firstHit.setCriticalMark();
						if(p.usingProtectMagic())
							defs.setMagicDamageModifier(.64);
						int delay = Misc.getDistance(npc, target) > 3 ? 4 : 2;
						Game.submit(new GameTick(delay) {
							public void run() {
								stop();
								if(!target.isDead()) {
									long currentTime = Misc.currentTimeMillis();
									if (target.getSize() >= 2
											|| target.getFreezeDelay() >= currentTime
											|| target.getFrozenBlockedDelay() >= currentTime) {
										target.graphics(1677);
									} else {
										target.graphics(369);
										target.addFreezeDelay(20000);
									}
								}
							}
						});
						delayHit(npc, delay, target, firstHit);
						npc.forceTalk("Prepare to be frozen in an icy hell!");
						p.sm("<col=0000ff>The assassin's ice attack has wounded you.");
					}
				});
			} else {
				npc.setNextAnimation(new Animation(1060));
				npc.graphics(2756);
				Game.submit(new GameTick(1.06) {
					public void run() {
						stop();
						Game.sendProjectile(npc, target, 396, 30, 20, 36, 35, 16, 0);
					}
				});
				if(p.usingProtectMagic())
					defs.setMagicDamageModifier(.20);
				delayHit(npc, 4, target, getMagicHit(npc, getRandomMaxHit(npc, 484, 2, target)));
				Game.submit(new GameTick(2.5) {
					public void run() {
						stop();
						target.graphics(373);
					}
				});
			}
			if(!transformed && npc.getHitpoints() <= (npc.getMaxHitpoints() * .46)) {
				npc.setCantInteract(true);
				npc.setAttackedBy(null);
				p.setAttackedBy(null);
				p.resetReceivedDamage();
				npc.resetReceivedDamage();
				p.resetCombat();
				npc.resetCombat();
				npc.reset();
				p.stopAll();
				transformed = true;
				npc.animate(843);
				npc.forceTalk("I will not be defeated!");
				p.sm("Watch out, merlyn is about to gain a new ability!");
				Game.submit(new GameTick(1) {
					int ticks;
					NPC newNPC;
					Location toSpawn;
					public void run() {
						ticks++;
						if(ticks == 3) {
							npc.resetMasks();
						} else if(ticks == 4) {
							toSpawn = new Location(npc.getX(), npc.getY(), p.getZ());
							npc.finish();
						} else if(ticks == 5) {
							newNPC = new NPC(14677, toSpawn, false);
							newNPC.forceTalk("Gaaaaaaaaaarrrrrrrrhhhhhhhhhh!");
							newNPC.animate(2563);
							newNPC.graphics(365);
							p.sm("<col=b00002>Quick, turn on your melee prayers!");
							p.sm("<col=ff0000>THIS IS THE ASSASSIN'S FINAL AND ULTIMATE FORM!!!");
							transformed = false;
						} else if(ticks == 11) {
							stop();
							newNPC.setTarget(p);
							p.getActionManager().setAction(new PlayerCombat(newNPC));
						}
					}
				});
				
			}
		} else if(npc.getId() == 14677) {
			defs.setAttackDelay(4);
			int random = Misc.random(10);
			if(random == 0) {
				npc.animate(2820);
				npc.graphics(306);
				npc.forceTalk("<col=95200E>FEEL THE POWER OF THE ASSASSIN");
				int wepDam = getRandomMaxHit(npc, 990, 0, target);
				int wepDam2 = getRandomMaxHit(npc, 990, 0, target);
				double multiplier = 0.59 + Math.random();
				wepDam *= multiplier;
				Hit h = getMeleeHit(npc, wepDam);
				h.setCriticalMark();
				if(p.usingProtectMelee())
					defs.setMeleeDamageModifier(.43);
				delayHit(npc, 0, target, h);
				target.graphics(2751);
				final Hit second = getMeleeHit(npc, wepDam2);
				second.setCriticalMark();
				Game.submit(new GameTick(1.05) {
					public void run() {
						if(target.isDead() || target.hasFinished())
							stop();
						npc.setNextAnimation(new Animation(12009));
						delayHit(npc, 0, target, second);
						stop();
					}
				});
				p.sm("Brace yourself for the assassin's ultimate attack!");
			} else if(random == 4 || random == 7 || random == 9) {
				npc.setNextAnimation(new Animation(14788));
				target.graphics(2795);
				if(p.usingProtectMelee())
					defs.setMagicDamageModifier(.29);
				Hit hit = getMagicHit(npc, getRandomMaxHit(npc, 894, 2, target));
				hit.setCriticalMark();
				delayHit(npc, 0, target, hit);
				npc.forceTalk("Fool! I cannot be defeated!!");
				p.sm("<col=993ffc>The assassin projected a bolt of lightning from his blades!");
			} else {
				npc.setNextAnimation(new Animation(12009));
				if(p.usingProtectMelee())
					defs.setMeleeDamageModifier(.20);
				delayHit(npc, 0, target, getMeleeHit(npc, getRandomMaxHit(npc, 694, 2, target)));
				delayHit(npc, 1, target, getMeleeHit(npc, getRandomMaxHit(npc, 694, 2, target)));
			}
		}
		return defs.getAttackDelay();
	}
}
