package org.nova.game.npc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.Directions;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.masks.Hit;
import org.nova.game.masks.Hit.HitLook;
import org.nova.game.npc.combat.NPCCombat;
import org.nova.game.npc.combat.NPCCombatDefinitions;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.player.Player;
import org.nova.game.player.controlers.Wilderness;
import org.nova.kshan.content.NPCDeath;
import org.nova.kshan.utilities.FormatUtils;
import org.nova.utility.loading.map.MapAreas;
import org.nova.utility.loading.npcs.NCDLoader;
import org.nova.utility.loading.npcs.NPCBonuses;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.loading.npcs.NPCExamines;
import org.nova.utility.loading.npcs.NPCHoverMessages;
import org.nova.utility.misc.Misc;

public class NPC extends Entity implements Serializable {

	private static final long serialVersionUID = 113719508416206773L;
	
	private int id;
	private Location respawnTile;
	private int mapAreaNameHash;
	private boolean canBeAttackFromOutOfArea;
	private boolean randomwalk;
	private int[] bonuses; // 0 stab, 1 slash, 2 crush,3 mage, 4 range, 5 stab def, blahblah till 9
	private boolean spawned;
	private transient NPCCombat combat;
	private Location forceWalk;

	private long lastAttackedByTarget;
	private boolean cantInteract;
	private int capDamage;
	private int lureDelay;
	private boolean cantFollowUnderCombat;
	private boolean forceAgressive;
	private int forceTargetDistance;
	private boolean forceFollowClose;
	private boolean forceMultiAttacked;

	// npc masks
	private transient Transformation nextTransformation;
	//name changing masks
	private String name;
	private transient boolean changedName;
	private int combatLevel;
	private transient boolean changedCombatLevel;

	public NPC(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea) {
		this(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	public boolean isFamiliar() {
		return this instanceof Familiar;
	}

	/*
	 * creates and adds npc
	 */
	public NPC(int id, Location tile, int mapAreaNameHash,
			boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new Location(tile);
		this.mapAreaNameHash = mapAreaNameHash;
		this.canBeAttackFromOutOfArea = canBeAttackFromOutOfArea;
		this.spawned = spawned;
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk((defs().walkMask & 0x2) != 0
				|| forceRandomWalk(id));
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		Game.addNPC(this);
		Game.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}
	
	private transient boolean toBeSpawned;
	private transient String faceDir;
	private transient String storedCustomName;
	
	public NPC(int id, Location l, boolean canWalk, boolean spawn, String faceDir, String customName) { 
		// Only creates the instance
		super(l);
		this.id = id;
		this.respawnTile = new Location(x, y, z);
		this.mapAreaNameHash = -1;
		this.setToBeSpawned(spawn);
		this.canBeAttackFromOutOfArea = true;
		this.spawned = false;
		if(faceDir != null)
			this.setFaceDir(faceDir);
		else
			setFaceDir("NORTH");
		if(customName != null) {
			this.setStoredCustomName(customName);
			setName(customName);
		}
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk(canWalk);
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
	}
	
	public void spawnNPC() { // Spawns it
		setFinished(false);
		setLastRegionId(0);
		initEntity();
		Game.addNPC(this);
		Game.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
		changeDirection(getFaceDir());
	}
	
	public NPC(int id, Location tile, boolean canWalk) {
		super(tile);
		this.id = id;
		this.respawnTile = new Location(tile);
		this.mapAreaNameHash = -1;
		this.canBeAttackFromOutOfArea = true;
		this.spawned = true;
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk(canWalk);
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		Game.addNPC(this);
		Game.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}
	
	public NPC(int id, Location tile, boolean canWalk, boolean spawned) {
		super(tile);
		this.id = id;
		this.respawnTile = new Location(tile);
		this.mapAreaNameHash = -1;
		this.canBeAttackFromOutOfArea = true;
		this.spawned = spawned;
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk(canWalk);
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
		// npc is inited on creating instance
		initEntity();
		Game.addNPC(this);
		Game.updateEntityRegion(this);
		// npc is started on creating instance
		loadMapRegions();
		checkMultiArea();
	}

	/**
	 * This is a fake NPC object.
	 * @param id
	 */
	public NPC(int id) {
		super(null);
		this.id = id;
		this.respawnTile = new Location(x, y, z);
		this.mapAreaNameHash = -1;
		this.canBeAttackFromOutOfArea = true;
		this.spawned = false;
		combatLevel = -1;
		setHitpoints(getMaxHitpoints());
		setDirection(getRespawnDirection());
		setRandomWalk(false);
		bonuses = NPCBonuses.getBonuses(id);
		combat = new NPCCombat(this);
		capDamage = -1;
		lureDelay = 12000;
	}

	@Override
	public boolean needMasksUpdate() {
		return super.needMasksUpdate() || nextTransformation != null || changedCombatLevel || changedName;
	}

	public void transformIntoNPC(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
	}

	public void setNPC(int id) {
		this.id = id;
		bonuses = NPCBonuses.getBonuses(id);
	}

	@Override
	public void resetMasks() {
		super.resetMasks();
		nextTransformation = null;
		changedCombatLevel = false;
		changedName = false;
	}

	public int getMapAreaNameHash() {
		return mapAreaNameHash;
	}

	public boolean canBeAttackFromOutOfArea() {
		return canBeAttackFromOutOfArea;
	}

	public NPCDefinition defs() {
		return NPCDefinition.get(id);
	}

	public NPCCombatDefinitions getCombatDefinitions() {
		return NCDLoader.getNPCCombatDefinitions(id);
	}

	@Override
	public int getMaxHitpoints() {
		return getCombatDefinitions().getHitpoints();
	}

	public int getId() {
		return id;
	}

	public void processNPC() {
		if (isDead())
			return;
		if (!combat.process()) { // if not under combat
			if (!isForceWalking()) {// combat still processed for attack delay
									// go down
				// random walk
				if (!cantInteract) {
					if (!checkAgressivity()) {
						if (getFreezeDelay() < Misc.currentTimeMillis()) {
							if (((hasRandomWalk()) && Game.getRotation(
									getZ(), getX(), getY()) == 0) // temporary
																		// fix
									&& Math.random() * 1000.0 < 100.0) {
								int moveX = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								int moveY = (int) Math
										.round(Math.random() * 10.0 - 5.0);
								resetWalkSteps();
								if (getMapAreaNameHash() != -1) {
									if (!MapAreas.isAtArea(getMapAreaNameHash(), this)) {
										forceWalkRespawnTile();
										return;
									}
									addWalkSteps(getX() + moveX, getY() + moveY, 5);
								}else 
									addWalkSteps(respawnTile.getX() + moveX, respawnTile.getY() + moveY, 5);
							}
						}
					}
				}
			}
		}
		
		
			
			
			
							//Renamers
							if(id == 519)
									setName("Herb Store");
							if(id == 576)
									setName("Skilling Shop");
							if(id == 544)
   									setName("SkillCape Shop");
							if(id == 6537)
    								setName("Special Item Store");
							if(id == 524)
    								setName("Loyalty Shop");
							if(id == 551)
   									setName("Magic Shop");
							if(id == 530)
									setName("Lamp Exchange");
							if(id == 4555)
    								setName("F.O.G Shop");
							if(id == 15582)
    								setName("Prestige Master");
							if(id == 546)
   									setName("Magic Supplies");
							if(id == 530)
									setName("Lamp Exchange");
							if(id == 524)
									setName("Mage Gear");
							if(id == 400)
									setName("QBD Exchange");
							if(id == 346)
   									setName("Range Armour");
							if(id == 1694)
   									setName("Range Weapons");
							if(id == 211)
    								setName("Melee Armour");
							if(id == 6971)
    								setName("Summoning Supplies");
							if(id == 537)
   									setName("Pouch Shop");
							if(id == 2830)
									setName("Melee Wepons");
							if(id == 454)
    								setName("Food n Pots");	
							if(id == 5580)
                                    setName("Poanizer");
							if(id == 11254)
									setName("Lewis");
							if(id == 1039)
									setName("Male Clothes Designer");
							if(id == 4518)
									setName("Female Clothes Designer");
							if(id == 523)
									setName("Sell to Me");
							if(id == 3067)
									setName("Steel Kiwi");
							if(id == 6136)
									setName("Poanizer's first 'Custom' dialogue");		
							if(id == 219)
									setName("Fishing Supplies");
							if(id == 210)
									setName("Pure Accessories");
									
									
									
									
									
									
		if (isForceWalking()) {
			if (getFreezeDelay() < Misc.currentTimeMillis()) {
				setRandomWalk(false);                         
                             if(id == 519)
                                    setRandomWalk(false);
                             if(id == 550)
                                    setRandomWalk(false);
                             if(id == 546)
                                    setRandomWalk(false);
							if(id == 6136)
                                    setRandomWalk(true); //Notices Crier	
                             if(id == 549)
                                    setRandomWalk(false);
                             if(id == 683)
                                    setRandomWalk(false);
                             if(id == 2676)
                                    setRandomWalk(false);
                             if(id == 948)
                                    setRandomWalk(false);
	           if(id == 15581)
                                    setRandomWalk(true);
	           if(id == 2676)
                                    setRandomWalk(false);
                             if(id == 948)
                                    setRandomWalk(false);
                             if(id == 445)
                                    setRandomWalk(false);
                             if(id == 3299)
                                    setRandomWalk(false);
                             if(id == 2732)
                                    setRandomWalk(false);
                             if(id == 4906)
                                    setRandomWalk(false);
                             if(id == 3706)
                                    setRandomWalk(false);
//Home
                             if(id == 521)
                                    setRandomWalk(false);
                             if(id == 531)
                                    setRandomWalk(false);
                             if(id == 544)
                                    setRandomWalk(false);
                             if(id == 540)
                                    setRandomWalk(false);
                             if(id == 551)
                                    setRandomWalk(false);
                             if(id == 576)
                                    setRandomWalk(false);
                             if(id == 4555)
                                    setRandomWalk(false);
                             if(id == 598)
                                    setRandomWalk(false);
                             if(id == 2676)
                                    setRandomWalk(false);
							if(id == 7571)
                                    setRandomWalk(false);
							if(id == 14256)
                                    setRandomWalk(true);
							if(id == 546)
                                    setRandomWalk(false);
							if(id == 5867)
                                    setRandomWalk(false);
							
							if(id == 2025)
								setRandomWalk(false);
							if(id == 2026)
								setRandomWalk(false);
							if(id == 2027)
								setRandomWalk(false);		
							if(id == 2028)
								setRandomWalk(false);
							if(id == 2029)
								setRandomWalk(false);
							if(id == 2030)
								setRandomWalk(false);
									
									
				if (getX() != forceWalk.getX() || getY() != forceWalk.getY()) {
					if (!hasWalkSteps())
						addWalkSteps(forceWalk.getX(), forceWalk.getY(),
								getSize(), true);
					if (!hasWalkSteps()) { // failing finding route
						setLocation(new Location(forceWalk)); // force
																	// tele
																	// to
																	// the
																	// forcewalk
																	// place
						forceWalk = null; // so ofc reached forcewalk place
					}
				} else
					// walked till forcewalk place
					forceWalk = null;
			}
		}
	}

	@Override
	public void processEntity() {
		super.processEntity();
		processNPC();
	}

	public int getRespawnDirection() {
		NPCDefinition definitions = defs();
		if (definitions.unknownInt3 << 32 != 0 && definitions.respawnDirection > 0
				&& definitions.respawnDirection <= 8)
			return (4 + definitions.respawnDirection) << 11;
		return 0;
	}

	/*
	 * forces npc to random walk even if cache says no, used because of fake
	 * cache information
	 */
	private static boolean forceRandomWalk(int npcId) {
		switch (npcId) {
		case 11226:
			return true;
		default:
			return false;
			/*
			 * default: return NPCDefinitions.getNPCDefinitions(npcId).name
			 * .equals("Icy Bones");
			 */
		}
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		Entity source = hit.getSource();
		if (source == null)
			return;
		if(source instanceof Player) {
			final Player p = (Player) source;
			if(p.isOwner())
				setCapDamage(-1);
		}
		if (capDamage != -1 && hit.getDamage() > capDamage)
			hit.setDamage(capDamage);
		if (hit.getLook() != HitLook.MELEE_DAMAGE
				&& hit.getLook() != HitLook.RANGE_DAMAGE
				&& hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (source instanceof Player) {
			final Player p2 = (Player) source;
			if(p2.isOwner())
				setCapDamage(-1);
			if (p2.getPrayer().hasPrayersOn()) {
				if (p2.getPrayer().usingPrayer(1, 18)) {
					final NPC target = this;
					if (hit.getDamage() > 0)
						Game.sendProjectile(p2, this, 2263, 11, 11, 20, 5, 0,
								0);
					p2.heal(hit.getDamage() / 5);
					p2.getPrayer().drainPrayer(hit.getDamage() / 5);
					WorldTasksManager.schedule(new WorldTask() {
						@Override
						public void run() {
							setNextGraphics(new Graphics(2264));
							if (hit.getDamage() > 0)
								Game.sendProjectile(target, p2, 2263, 11, 11,
										20, 5, 0, 0);
						}
					}, 1);
				}
				if (hit.getDamage() == 0)
					return;
				if (!p2.getPrayer().isBoostedLeech()) {
					if (hit.getLook() == HitLook.MELEE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 19)) {
							p2.getPrayer().setBoostedLeech(true);
							return;
						} else if (p2.getPrayer().usingPrayer(1, 1)) { // sap
																		// att
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(0)) {
									p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your sap curse has no effect.",
													true);
								} else {
									p2.getPrayer().increaseLeechBonus(0);
									p2.packets()
											.sendMessage(
													"Your curse drains Attack from the enemy, boosting your Attack.",
													true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2214));
								p2.getPrayer().setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2215, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2216));
									}
								}, 1);
								return;
							}
						} else {
							if (p2.getPrayer().usingPrayer(1, 10)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(3)) {
										p2.packets()
												.sendMessage(
														"Your opponent has been weakened so much that your leech curse has no effect.",
														true);
									} else {
										p2.getPrayer().increaseLeechBonus(3);
										p2.packets()
												.sendMessage(
														"Your curse drains Attack from the enemy, boosting your Attack.",
														true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2231, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2232));
										}
									}, 1);
									return;
								}
							}
							if (p2.getPrayer().usingPrayer(1, 14)) {
								if (Misc.getRandom(7) == 0) {
									if (p2.getPrayer().reachedMax(7)) {
										p2.packets()
												.sendMessage(
														"Your opponent has been weakened so much that your leech curse has no effect.",
														true);
									} else {
										p2.getPrayer().increaseLeechBonus(7);
										p2.packets()
												.sendMessage(
														"Your curse drains Strength from the enemy, boosting your Strength.",
														true);
									}
									p2.setNextAnimation(new Animation(12575));
									p2.getPrayer().setBoostedLeech(true);
									Game.sendProjectile(p2, this, 2248, 35,
											35, 20, 5, 0, 0);
									WorldTasksManager.schedule(new WorldTask() {
										@Override
										public void run() {
											setNextGraphics(new Graphics(2250));
										}
									}, 1);
									return;
								}
							}

						}
					}
					if (hit.getLook() == HitLook.RANGE_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 2)) { // sap range
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(1)) {
									p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your sap curse has no effect.",
													true);
								} else {
									p2.getPrayer().increaseLeechBonus(1);
									p2.packets()
											.sendMessage(
													"Your curse drains Range from the enemy, boosting your Range.",
													true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2217));
								p2.getPrayer().setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2218, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2219));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 11)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(4)) {
									p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
								} else {
									p2.getPrayer().increaseLeechBonus(4);
									p2.packets()
											.sendMessage(
													"Your curse drains Range from the enemy, boosting your Range.",
													true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2236, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2238));
									}
								});
								return;
							}
						}
					}
					if (hit.getLook() == HitLook.MAGIC_DAMAGE) {
						if (p2.getPrayer().usingPrayer(1, 3)) { // sap mage
							if (Misc.getRandom(4) == 0) {
								if (p2.getPrayer().reachedMax(2)) {
									p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your sap curse has no effect.",
													true);
								} else {
									p2.getPrayer().increaseLeechBonus(2);
									p2.packets()
											.sendMessage(
													"Your curse drains Magic from the enemy, boosting your Magic.",
													true);
								}
								p2.setNextAnimation(new Animation(12569));
								p2.setNextGraphics(new Graphics(2220));
								p2.getPrayer().setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2221, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2222));
									}
								}, 1);
								return;
							}
						} else if (p2.getPrayer().usingPrayer(1, 12)) {
							if (Misc.getRandom(7) == 0) {
								if (p2.getPrayer().reachedMax(5)) {
									p2.packets()
											.sendMessage(
													"Your opponent has been weakened so much that your leech curse has no effect.",
													true);
								} else {
									p2.getPrayer().increaseLeechBonus(5);
									p2.packets()
											.sendMessage(
													"Your curse drains Magic from the enemy, boosting your Magic.",
													true);
								}
								p2.setNextAnimation(new Animation(12575));
								p2.getPrayer().setBoostedLeech(true);
								Game.sendProjectile(p2, this, 2240, 35, 35,
										20, 5, 0, 0);
								WorldTasksManager.schedule(new WorldTask() {
									@Override
									public void run() {
										setNextGraphics(new Graphics(2242));
									}
								}, 1);
								return;
							}
						}
					}

					// overall

					if (p2.getPrayer().usingPrayer(1, 13)) { // leech defence
						if (Misc.getRandom(10) == 0) {
							if (p2.getPrayer().reachedMax(6)) {
								p2.packets()
										.sendMessage(
												"Your opponent has been weakened so much that your leech curse has no effect.",
												true);
							} else {
								p2.getPrayer().increaseLeechBonus(6);
								p2.packets()
										.sendMessage(
												"Your curse drains Defence from the enemy, boosting your Defence.",
												true);
							}
							p2.setNextAnimation(new Animation(12575));
							p2.getPrayer().setBoostedLeech(true);
							Game.sendProjectile(p2, this, 2244, 35, 35, 20, 5,
									0, 0);
							WorldTasksManager.schedule(new WorldTask() {
								@Override
								public void run() {
									setNextGraphics(new Graphics(2246));
								}
							}, 1);
							return;
						}
					}
				}
			}
		}

	}

	@Override
	public void reset() {
		super.reset();
		setDirection(getRespawnDirection());
		combat.reset();
		bonuses = NPCBonuses.getBonuses(id); // back to real bonuses
		forceWalk = null;
	}

	@Override
	public void finish() {
		if (hasFinished())
			return;
		setFinished(true);
		Game.updateEntityRegion(this);
		Game.removeNPC(this);
	}

	public void setRespawnTask() {
		if(!hasFinished()) {
			reset();
			setCoords(respawnTile);
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, getCombatDefinitions().getRespawnDelay() * 600,
				TimeUnit.MILLISECONDS);
	}

	public void deserialize() {
		if (combat == null)
			combat = new NPCCombat(this);
		spawn();
	}

	public void spawn() {
		setFinished(false);
		Game.addNPC(this);
		setLastRegionId(0);
		Game.updateEntityRegion(this);
		loadMapRegions();
		checkMultiArea();
	}

	public NPCCombat getCombat() {
		return combat;
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		int id = this.getId();
		Player killer = this.getMostDamageReceivedSourcePlayer();
		int coordFaceX = this.getCoordFaceX(this.getSize());
		int coordFaceY = this.getCoordFaceY(this.getSize());
		resetWalkSteps();
		combat.removeTarget();
		setNextAnimation(null);
		if (!isDead())
			setHitpoints(0);
		final int deathDelay = defs.getDeathDelay();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= deathDelay) {
					if(source instanceof Player) {
						((Player) source).getRandomEvent().onNPCDeath(NPC.this);
						NPCDeath.onDeath(NPC.this, ((Player) source));
					}
					drop(id, coordFaceX, coordFaceY, killer);
					reset();
					setLocation(respawnTile);
					finish();
					if (!spawned)
						setRespawnTask();
					if (source.getAttackedBy() == NPC.this) {
						source.setAttackedByDelay(0);
						source.setAttackedBy(null);
						source.setFindTargetDelay(0);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	public void drop(int id, int coordFaceX, int coordFaceY, Player killer) {
		try {
			if(killer == null || coordFaceX < 0 || coordFaceY < 0 || id < 0)
				return;
			Drop[] drops = NPCDrops.getDrops(id);
			if (drops == null)
				return;
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100) {
					Game.addGroundItem(new Item(drop.getItemId(), drop.getMinAmount()
						+ Misc.getRandom(drop.getExtraAmount())), new Location(
							coordFaceX, coordFaceY, killer.getZ()), killer,
								false, 180, true);
				} else {
					if ((Misc.getRandomDouble(99) + 1) <= drop.getRate() * 1.5)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0) {
				Drop drop = possibleDrops[Misc.getRandom(possibleDropsCount - 1)];
				Game.addGroundItem(new Item(drop.getItemId(), drop.getMinAmount()
					+ Misc.getRandom(drop.getExtraAmount())), new Location(
						coordFaceX, coordFaceY, killer.getZ()), killer,
							false, 180, true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void drop(NPC npc) {
		try {
			Drop[] drops = NPCDrops.getDrops(npc.getId());
			if (drops == null)
				return;
			Player killer = npc.getMostDamageReceivedSourcePlayer();
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					npc.sendDrop(killer, npc, drop);
				else {
					if ((Misc.getRandomDouble(99) + 1) <= drop.getRate() * 1.5)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
				npc.sendDrop(killer, npc, possibleDrops[Misc.getRandom(possibleDropsCount - 1)]);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendDrop(Player player, NPC npc, Drop drop) {
		int size = getSize();
		Game.addGroundItem(new Item(drop.getItemId(), drop.getMinAmount()
				+ Misc.getRandom(drop.getExtraAmount())), new Location(
				npc.getCoordFaceX(size), npc.getCoordFaceY(size), npc.getZ()), player,
				false, 180, true);
	}
	
	@Override
	public int getSize() {
		return defs().size;
	}

	public int getMaxHit() {
		return getCombatDefinitions().getMaxHit();
	}

	public int[] getBonuses() {
		return bonuses;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return getCombatDefinitions().canAttackThroughPrayer() ? getCombatDefinitions().getMagicDamageModifier() : 0;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return getCombatDefinitions().canAttackThroughPrayer() ? getCombatDefinitions().getRangeDamageModifier() : 0;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return getCombatDefinitions().canAttackThroughPrayer() ? getCombatDefinitions().getMeleeDamageModifier() : 0;
	}

	public Location getRespawnTile() {
		return respawnTile;
	}

	public boolean isUnderCombat() {
		return combat.underCombat();
	}

	@Override
	public void setAttackedBy(Entity target) {
		super.setAttackedBy(target);
		if (target == combat.getTarget()
				&& !(combat.getTarget() instanceof Familiar))
			lastAttackedByTarget = Misc.currentTimeMillis();
	}

	public boolean canBeAttackedByAutoRelatie() {
		return Misc.currentTimeMillis() - lastAttackedByTarget > lureDelay;
	}

	public boolean isForceWalking() {
		return forceWalk != null;
	}

	public void setTarget(Entity entity) {
		if (isForceWalking()) // if force walk not gonna get target
			return;
		combat.setTarget(entity);
		lastAttackedByTarget = Misc.currentTimeMillis();
	}

	public void removeTarget() {
		if (combat.getTarget() == null)
			return;
		combat.removeTarget();
	}

	public void forceWalkRespawnTile() {
		setForceWalk(respawnTile);
	}

	public void setForceWalk(Location tile) {
		resetWalkSteps();
		forceWalk = tile;
	}

	public boolean hasForceWalk() {
		return forceWalk != null;
	}

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
											forceTargetDistance > 0 ? forceTargetDistance
													: (getCombatDefinitions()
															.getAttackStyle() == NPCCombatDefinitions.MELEE ? 4
															: getCombatDefinitions()
																	.getAttackStyle() == NPCCombatDefinitions.SPECIAL ? 64
																	: 8))
							|| (!forceMultiAttacked
									&& (!isAtMultiArea() || !player
											.isAtMultiArea())
									&& player.getAttackedBy() != this && (player
									.getAttackedByDelay() > System
									.currentTimeMillis() || player
									.getFindTargetDelay() > System
									.currentTimeMillis()))
							|| !clipedProjectile(player, false)
							|| (!forceAgressive && !Wilderness.isAtWild(this) && player
									.getSkills().getCombatLevelWithSummoning() >= defs().combatLevel * 2))
						continue;
					possibleTarget.add(player);
				}
			}
		}
		return possibleTarget;
	}

	public boolean checkAgressivity() {
		// if(!(Wilderness.isAtWild(this) &&
		// getDefinitions().hasAttackOption())) {
		if (!forceAgressive) {
			NPCCombatDefinitions defs = getCombatDefinitions();
			if (defs.getAgressivenessType() == NPCCombatDefinitions.PASSIVE)
				return false;
		}
		// }
		ArrayList<Entity> possibleTarget = getPossibleTargets();
		if (!possibleTarget.isEmpty()) {
			Entity target = possibleTarget.get(Misc.getRandom(possibleTarget
					.size() - 1));
			setTarget(target);
			target.setAttackedBy(target);
			target.setFindTargetDelay(Misc.currentTimeMillis() + 10000);
			return true;
		}
		return false;
	}

	public boolean isCantInteract() {
		return cantInteract;
	}

	public void setCantInteract(boolean cantInteract) {
		this.cantInteract = cantInteract;
		if (cantInteract)
			combat.reset();
	}

	public int getCapDamage() {
		return capDamage;
	}

	public void setCapDamage(int capDamage) {
		this.capDamage = capDamage;
	}

	public int getLureDelay() {
		return lureDelay;
	}

	public void setLureDelay(int lureDelay) {
		this.lureDelay = lureDelay;
	}

	public boolean isCantFollowUnderCombat() {
		return cantFollowUnderCombat;
	}

	public void setCantFollowUnderCombat(boolean canFollowUnderCombat) {
		this.cantFollowUnderCombat = canFollowUnderCombat;
	}

	public Transformation getNextTransformation() {
		return nextTransformation;
	}

	public boolean isForceAgressive() {
		return forceAgressive;
	}

	public void setForceAgressive(boolean forceAgressive) {
		this.forceAgressive = forceAgressive;
	}

	public int getForceTargetDistance() {
		return forceTargetDistance;
	}

	public void setForceTargetDistance(int forceTargetDistance) {
		this.forceTargetDistance = forceTargetDistance;
	}

	public boolean isForceFollowClose() {
		return forceFollowClose;
	}

	public void setForceFollowClose(boolean forceFollowClose) {
		this.forceFollowClose = forceFollowClose;
	}

	public boolean isForceMultiAttacked() {
		return forceMultiAttacked;
	}

	public void setForceMultiAttacked(boolean forceMultiAttacked) {
		this.forceMultiAttacked = forceMultiAttacked;
	}

	public boolean hasRandomWalk() {
		return randomwalk;
	}

	public void setRandomWalk(boolean forceRandomWalk) {
		this.randomwalk = forceRandomWalk;
	}

	public String getCustomName() {
		return name;
	}
	
	public void setName(String string) {
		this.name = defs().name.equals(string) ? null : string;
		changedName = true;
	}
	
	public int getCustomCombatLevel() {
		return combatLevel;
	}
	
	public int getCombatLevel() {
		return combatLevel >= 0 ? combatLevel : defs().combatLevel;
	}
	
	public String getName() {
		return name != null ? name : defs().name;
	}
	
	public void setCombatLevel(int level) {
		combatLevel  = defs().combatLevel == level ? -1 : level;
		changedCombatLevel = true;
	}
	
	public boolean hasChangedName() {
		return changedName;
	}
	
	public boolean hasChangedCombatLevel() {
		return changedCombatLevel;
	}
	
	public Location getMiddleLocation() {
		int size = getSize();
		return new Location(getCoordFaceX(size),getCoordFaceY(size), getZ());
	}

	public void setNextNPCTransformation(int id) {
		setNPC(id);
		nextTransformation = new Transformation(id);
		if (getCustomCombatLevel() != -1)
			changedCombatLevel = true;
		if (getCustomName() != null)
			changedName = true;
	}
	
	public String getExamine() {
		String examine = NPCExamines.getExamine(getId());
		return examine == null ? FormatUtils.formatForExamine(getName()) : examine;
	}

	public boolean isToBeSpawned() {
		return toBeSpawned;
	}

	public void setToBeSpawned(boolean toBeSpawned) {
		this.toBeSpawned = toBeSpawned;
	}

	public String getFaceDir() {
		return faceDir;
	}

	public void setFaceDir(String faceDir) {
		this.faceDir = faceDir;
	}

	public String getStoredCustomName() {
		return storedCustomName;
	}

	public void setStoredCustomName(String storedCustomName) {
		this.storedCustomName = storedCustomName;
	}
	
	@Override
	public String toString() {
		return id+" - "+getName();
	}

	public void faceDirection(Directions west) {
		faceLocation(new Location(getX() + west.getOffsetX(), getY() + west.getOffsetY(), getZ()));
	}

	public static NPC createBlankNPC(int npcId) {
		return new NPC(npcId, new Location(0, 0, 0), false, false, null, null);	
	}

	
	public String getHoverMessage() {
		if(NPCHoverMessages.getMessage(id) == null)
			return null;
		else
			return NPCHoverMessages.getMessage(id);
	}
	
	public boolean hasValidHoverMessage() {
		if(getHoverMessage() != null)
			if(!getHoverMessage().equals("") 
				&& NPCHoverMessages.getMessage(id).equalsIgnoreCase(getHoverMessage()))
				return true;
		return false;
	}
	
}
