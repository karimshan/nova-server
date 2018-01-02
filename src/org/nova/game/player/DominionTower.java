package org.nova.game.player;

import java.io.Serializable;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.Game;
import org.nova.game.engine.cores.CoresManager;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.map.RegionBuilder;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.npc.NPC;
import org.nova.utility.loading.playerutils.DominionTowerRank;
import org.nova.utility.misc.Misc;


public final class DominionTower implements Serializable {

	public static final int CLIMBER = 0, ENDURANCE = 1, MAX_FACTOR = 10000000;
	private static final long serialVersionUID = -5230255619877910203L;

	private transient Player player;
	private transient int[] mapBaseCoords;

	private int nextBossIndex;
	private int progress;
	private int dominionFactor;
	private long totalScore;
	private boolean talkedWithFace;
	private int killedBossesCount;
	private int maxFloorEndurance;
	private int maxFloorClimber;

	public void setPlayer(Player player) {
		this.player = player;
	}

	public DominionTower() {
		nextBossIndex = -1;
	}

	public boolean hasRequiriments() {
		return player.getSkills().getCombatLevelWithSummoning() >= 110;
	}

	public void openSpectate() {
		player.interfaces().sendInterface(1157);
	}

	public void growFace() {
		player.packets().sendSound(7913, 0, 2);
		player.getMatrixDialogues()
				.startDialogue(
						"SimpleMessage",
						"The face on the wall groans and cowls at you. Perhaps you should",
						"talk to it first.");
	}

	public void openModes() {
		if (!hasRequiriments()) {
			player.getMatrixDialogues().startDialogue("DTSpectateReq");
			return;
		}
		if (!talkedWithFace) {
			growFace();
			return;
		}
		if (progress == 256) {
			player.getMatrixDialogues()
					.startDialogue(
							"SimpleMessage",
							"You have some dominion factor which you must exchange before",
							"starting another match.");
			player.packets()
					.sendMessage(
							"You can't go back into the arena, you must go to the next floor on entrance.");
			return;
		}
		player.interfaces().sendInterface(1164);
		player.packets().sendIComponentText(
				1164,
				27,
				progress == 0 ? "Ready for a new match" : "Floor progress: "
						+ progress);
	}

	public void handleButtons(int interfaceId, int componentId) {
		if (interfaceId == 1164) {
			if (componentId == 26)
				openClimberMode();
			else if (componentId == 28)
				openEnduranceMode();
			else if (componentId == 29)
				openSpecialMode();
			else if (componentId == 30)
				openFreeStyleMode();
			else if (componentId == 31)
				openSpectate();
		} else if (interfaceId == 1163) {
			if (componentId == 89)
				player.closeInterfaces();
		} else if (interfaceId == 1168) {
			if (componentId == 254)
				player.closeInterfaces();
		} else if (interfaceId == 1170) {
			if (componentId == 85)
				player.closeInterfaces();
		} else if (interfaceId == 1173) {
			if (componentId == 54)
				player.closeInterfaces();
			else if (componentId == 55)
				startEnduranceMode();
		}
	}

	private static final int[] MUSICS = { 1015, 1022, 1018, 1016, 1021 };

	public static final class Boss {

		private String name;
		private String text;
		private int[] ids;
		private boolean forceMulti;
		private Item item;

		public Boss(String name, String text, int... ids) {
			this(name, text, false, null, ids);
		}

		public Boss(String name, String text, boolean forceMulti, Item item,
				int... ids) {
			this.name = name;
			this.text = text;
			this.forceMulti = forceMulti;
			this.ids = ids;
			this.item = item;
		}

		public boolean isForceMulti() {
			return forceMulti;
		}

		public String getName() {
			return name;
		}
	}

	private static final Boss[] BOSSES = { new Boss("Elvarg", "Grrrr", 14548),
			new Boss("Corporeal Beast", null, true, null, 8133),
			new Boss("Delrith", "Grrrr", false, new Item(2402, 1), 14578),
			new Boss("Evil Chicken", "Bwak bwak bwak", 3375),
			new Boss("The Black Knight Titan", "Kill kill kill!", 14436),
			new Boss("Bouncer", "Grrr", 14483) };

	private void startEnduranceMode() {
		if (progress == 256) {
			player.getMatrixDialogues()
					.startDialogue(
							"SimpleMessage",
							"You have some dominion factor which you must exchange before",
							"starting another match.");
			player.packets()
					.sendMessage(
							"You can't go back into the arena, you must go to the next floor on entrance.");
			return;
		}
		createArena(ENDURANCE);
	}

	public void createArena(final int mode) {
		player.closeInterfaces();
		player.setInfiniteStopDelay();
		CoresManager.slowExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					boolean needDestroy = mapBaseCoords != null;
					final int[] oldMapBaseCoords = mapBaseCoords;
					mapBaseCoords = RegionBuilder.findEmptyMap(8, 8);
					RegionBuilder.copyAllPlanesMap(456, 768, mapBaseCoords[0],
							mapBaseCoords[1], 8);
					teleportToArena(mode);
					if (needDestroy) {
						WorldTasksManager.schedule(new WorldTask() {
							@Override
							public void run() {
								CoresManager.slowExecutor
										.execute(new Runnable() {
											@Override
											public void run() {
												try {
													RegionBuilder
															.destroyMap(
																	oldMapBaseCoords[0],
																	oldMapBaseCoords[1],
																	8, 8);
												} catch (Exception e) {
													e.printStackTrace();
												} catch (Error e) {
													e.printStackTrace();
												}
											}
										});
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void teleportToArena(int mode) {
		player.faceLocation(new Location(player.getX() + 1, player
				.getY(), 0));
		player.getControllerManager().startController("DTControler", mode);
		player.resetStopDelay();
		player.setLocation(new Location(getBaseX() + 10, getBaseY() + 29,
				2));
		player.getMusicsManager().playMusic(
				MUSICS[Misc.getRandom(MUSICS.length - 1)]);
	}

	public String getStartFightText(int message) {
		switch (message) {
		case 0:
			return "Kick my ass!";
		case 1:
			return "Please don't hit my face";
		case 2:
			return "Argh!";
		default:
			return "Bring it on!";
		}
	}

	public void startFight(final NPC[] bosses) {
		for (NPC boss : bosses) {
			boss.setCantInteract(true);
			boss.faceLocation(new Location(boss.getX() - 1, boss
					.getY(), 0));
		}
		player.setInfiniteStopDelay();
		player.setLocation(new Location(getBaseX() + 25, getBaseY() + 32,
				2));
		player.faceLocation(new Location(player.getX() + 1, player
				.getY(), 0));
		WorldTasksManager.schedule(new WorldTask() {

			private int count;

			@Override
			public void run() {
				if (count == 0) {
					player.interfaces()
							.sendTab(
									player.interfaces()
											.isFullScreen() ? 9 : 8, 1172);
					player.packets().sendHideIComponent(1172, 2, true);
					player.packets().sendHideIComponent(1172, 7, true);
					player.packets().sendIComponentText(1172, 4,
							player.getDisplayName());
					player.packets().sendConfig(1241, 1);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 25),
							Game.getCutsceneY(player, getBaseY() + 38), 1800);
					player.packets().sendCameraLook(
							Game.getCutsceneX(player, getBaseX() + 25),
							Game.getCutsceneY(player, getBaseY() + 29), 800);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 32),
							Game.getCutsceneY(player, getBaseY() + 38), 1800, 6, 6);
				} else if (count == 1) {
					player.setNextForceTalk(new ForceTalk(
							getStartFightText(Misc.getRandom(1))));
				} else if (count == 3) {
					player.packets().sendHideIComponent(1172, 2, false);
					player.packets().sendHideIComponent(1172, 5, true);
					player.packets().sendIComponentText(1172, 6,
							BOSSES[nextBossIndex].name);
					player.packets().sendConfig(1241, 0);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 37), 1800);
					player.packets().sendCameraLook(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 28), 800);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 42),
							Game.getCutsceneY(player, getBaseY() + 37), 1800, 6, 6);
				} else if (count == 4) {
					if (BOSSES[nextBossIndex].text != null)
						bosses[0].setNextForceTalk(new ForceTalk(
								BOSSES[nextBossIndex].text));
				} else if (count == 6) {
					player.packets()
							.closeInterface(
									player.interfaces()
											.isFullScreen() ? 9 : 8);
					player.interfaces().sendInterface(1172);
					player.packets().sendHideIComponent(1172, 2, true);
					player.packets().sendHideIComponent(1172, 5, true);
					player.packets().sendIComponentText(1172, 8, "Fight!");
					player.packets().sendHideIComponent(1172, 10, true);
					player.getControllerManager().sendInterfaces();
					player.packets().sendCameraLook(
							Game.getCutsceneX(player, getBaseX() + 32),
							Game.getCutsceneY(player, getBaseY() + 36), 0);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 32),
							Game.getCutsceneY(player, getBaseY() + 16), 5000);
					player.packets().sendVoice(7882);
				} else if (count == 8) {
					if (BOSSES[nextBossIndex].item != null)
						Game.addGroundItem(BOSSES[nextBossIndex].item,
								new Location(getBaseX() + 26, getBaseY() + 33,
										2));
					player.closeInterfaces();
					player.packets().sendResetCamera();
					for (NPC boss : bosses) {
						boss.setCantInteract(false);
						boss.setTarget(player);
					}
					player.resetStopDelay();
					stop();
				}
				count++;
			}

		}, 0, 1);
	}

	public void removeItem() {
		if (BOSSES[nextBossIndex].item != null) {
			player.getInventory().deleteItem(
					BOSSES[nextBossIndex].item.getId(),
					BOSSES[nextBossIndex].item.getAmount());
			player.getEquipment().deleteItem(
					BOSSES[nextBossIndex].item.getId(),
					BOSSES[nextBossIndex].item.getAmount());
			player.getAppearance().generateAppearanceData();
		}
	}

	public void loss(final int mode) {
		/*
		 * if(mapBaseCoords == null) { //died on logout
		 * player.setNextLocation(new Location(3744, 6425, 0));
		 * player.getControlerManager().removeControlerWithoutCheck(); return; }
		 */
		removeItem();
		nextBossIndex = -1;
		player.setInfiniteStopDelay();
		player.setLocation(new Location(getBaseX() + 35, getBaseY() + 31,
				2));
		player.faceLocation(new Location(player.getX() + 1, player
				.getY(), 0));

		WorldTasksManager.schedule(new WorldTask() {
			int count;

			@Override
			public void run() {
				if (count == 0) {
					player.setNextAnimation(new Animation(836));
					player.packets()
							.closeInterface(
									player.interfaces()
											.isFullScreen() ? 10 : 8);
					player.interfaces().sendInterface(1172);
					player.packets().sendHideIComponent(1172, 2, true);
					player.packets().sendHideIComponent(1172, 5, true);
					player.packets().sendIComponentText(1172, 8,
							"Unlucky, you lost!");
					player.packets().sendIComponentText(
							1172,
							10,
							"You leave with a dominion factor of: "
									+ dominionFactor);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 37), 2500);
					player.packets().sendCameraLook(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 28), 800);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 42),
							Game.getCutsceneY(player, getBaseY() + 37), 2500, 6, 6);
					player.packets().sendVoice(7874);
				} else if (count == 4) {
					player.setForceMultiArea(false);
					player.reset();
					player.setNextAnimation(new Animation(-1));
					player.closeInterfaces();
					player.packets().sendResetCamera();
					player.resetStopDelay();
					destroyArena(false, mode);
					stop();
				}
				count++;
			}
		}, 0, 1);
	}

	public void win(int mode) {
		removeItem();
		int factor = getBossesTotalLevel() * (mode == CLIMBER ? 100 : 10);
		progress++;
		if (mode == CLIMBER) {
			if (progress > maxFloorClimber)
				maxFloorClimber = progress;
		} else if (mode == ENDURANCE) {
			if (progress > maxFloorEndurance)
				maxFloorEndurance = progress;
		}

		killedBossesCount++;
		dominionFactor += factor;
		totalScore += factor;
		if (dominionFactor > MAX_FACTOR) {
			dominionFactor = MAX_FACTOR;
			player.packets()
					.sendMessage(
							"You've reached the maximum Dominion Factor you can get so you should spend it!");
		}
		DominionTowerRank.checkRank(player, mode, BOSSES[nextBossIndex].name);
		nextBossIndex = -1;
		player.setInfiniteStopDelay();
		player.setLocation(new Location(getBaseX() + 35, getBaseY() + 31,
				2));
		player.faceLocation(new Location(player.getX() + 1, player
				.getY(), 0));

		WorldTasksManager.schedule(new WorldTask() {

			private int count;

			@Override
			public void run() {
				if (count == 0) {
					player.packets()
							.closeInterface(
									player.interfaces()
											.isFullScreen() ? 10 : 8);
					player.interfaces().sendInterface(1172);
					player.packets().sendHideIComponent(1172, 2, true);
					player.packets().sendHideIComponent(1172, 5, true);
					player.packets().sendIComponentText(1172, 8,
							"Yeah! You won!");
					player.packets().sendIComponentText(
							1172,
							10,
							"You now have a dominion factor of: "
									+ dominionFactor);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 37), 2500);
					player.packets().sendCameraLook(
							Game.getCutsceneX(player, getBaseX() + 35),
							Game.getCutsceneY(player, getBaseY() + 28), 800);
					player.packets().sendCameraPos(
							Game.getCutsceneX(player, getBaseX() + 42),
							Game.getCutsceneY(player, getBaseY() + 37), 2500, 6, 6);
					player.packets().sendVoice(7897);
				} else if (count == 4) {
					player.reset();
					player.closeInterfaces();
					player.packets().sendResetCamera();
					player.resetStopDelay();
					stop();
				}
				count++;
			}
		}, 0, 1);

	}

	/*
	 * 4928 15936
	 */
	/*
	 * 4960, 15968
	 */

	public void destroyArena(final boolean logout, int mode) {
		Location tile = new Location(3744, 6425, 0);
		if (logout)
			player.setCoords(tile);
		else {
			player.getControllerManager().removeControllerWithoutCheck();
			player.setInfiniteStopDelay();
			player.setLocation(tile);
			if (mode == ENDURANCE)
				progress = 0;
		}
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				CoresManager.slowExecutor.execute(new Runnable() {
					@Override
					public void run() {
						try {
							RegionBuilder.destroyMap(mapBaseCoords[0],
									mapBaseCoords[1], 8, 8);
							if (!logout) {
								mapBaseCoords = null;
								player.resetStopDelay();
							}
						} catch (Exception e) {
							e.printStackTrace();
						} catch (Error e) {
							e.printStackTrace();
						}
					}
				});
			}
		}, 1);
	}

	public NPC[] createBosses() {
		NPC[] bosses = new NPC[BOSSES[nextBossIndex].ids.length];
		for (int i = 0; i < BOSSES[nextBossIndex].ids.length; i++)
			bosses[i] = Game
					.spawnNPC(BOSSES[nextBossIndex].ids[i], new Location(
							getBaseX() + 37 + (i * 2), getBaseY() + 31, 2), -1,
							true, true);
		return bosses;
	}

	/*
	 * 4928 15936
	 */
	/*
	 * 4961, 15968
	 */

	public int getBaseX() {
		return mapBaseCoords[0] << 3;
	}

	public int getBaseY() {
		return mapBaseCoords[1] << 3;
	}

	public void selectBoss() {
		if (nextBossIndex < 0 || nextBossIndex >= BOSSES.length)
			nextBossIndex = Misc.getRandom(BOSSES.length - 1);
	}

	public void openClimberMode() {
		player.packets().sendMessage(
				"Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1163);
		// selectBoss();
		// player.getPackets().sendIComponentText(1163, 32, "0"); // you leave
		// with
	}

	public void openEnduranceMode() {
		selectBoss();
		player.interfaces().sendScreenInterface(96, 1173);
		player.packets().sendIComponentText(1173, 20,
				BOSSES[nextBossIndex].name); // current
		player.packets().sendIComponentText(1173, 33,
				String.valueOf(progress + 1)); // current
		player.packets().sendIComponentText(1173, 47, "None. Good luck :o."); // current
		player.packets().sendIComponentText(1173, 24,
				String.valueOf(dominionFactor)); // current
		player.packets().sendIComponentText(
				1173,
				26,
				dominionFactor == MAX_FACTOR ? "" : String
						.valueOf(getBossesTotalLevel() * 10)); // on win
		player.packets().sendIComponentText(1173, 28,
				String.valueOf(dominionFactor)); // on death
	}

	public int getBossesTotalLevel() {
		int level = 0;
		for (int id : BOSSES[nextBossIndex].ids)
			level = +NPCDefinition.get(id).combatLevel;
		return level;
	}

	public void openSpecialMode() {
		player.packets().sendMessage(
				"Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1170);
	}

	public void openFreeStyleMode() {
		player.packets().sendMessage(
				"Only endurance mode is currently working.");
		// player.getInterfaceManager().sendScreenInterface(96, 1168);
	}

	public void talkToFace() {
		talkToFace(false);
	}

	public void talkToFace(boolean fromDialogue) {
		if (!hasRequiriments()) {
			player.getMatrixDialogues().startDialogue("SimpleMessage",
					"You need at least level 110 combat to use this tower.");
			return;
		}
		if (!talkedWithFace)
			player.getMatrixDialogues().startDialogue("StrangeFace");
		else {
			if (!fromDialogue)
				player.packets().sendVoice(7893);
			player.interfaces().sendInterface(1160);
		}
	}

	public void openRewards() {
		if (!talkedWithFace) {
			talkToFace();
			return;
		}
		player.packets().sendVoice(7893);
		player.interfaces().sendInterface(1156);
	}

	public void openRewardsChest() {
		if (!talkedWithFace) {
			growFace();
			return;
		}
		progress = 0;
		dominionFactor = 0;
		player.interfaces().sendInterface(1171);
	}

	public void openBankChest() {
		if (!talkedWithFace) {
			growFace();
			return;
		}
		player.getBank().openBank();
	}

	public void viewScoreBoard() {
		DominionTowerRank.showRanks(player);
	}

	public boolean isTalkedWithFace() {
		return talkedWithFace;
	}

	public void setTalkedWithFace(boolean talkedWithFace) {
		this.talkedWithFace = talkedWithFace;
	}

	public int getProgress() {
		return progress;
	}

	public long getTotalScore() {
		return totalScore;
	}

	public int getDominionFactor() {
		return dominionFactor;
	}

	public Boss getNextBoss() {
		return BOSSES[nextBossIndex];
	}

	public int getMaxFloorClimber() {
		return maxFloorClimber;
	}

	public int getMaxFloorEndurance() {
		return maxFloorEndurance;
	}

	public int getKilledBossesCount() {
		return killedBossesCount;
	}

}
