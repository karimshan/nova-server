package org.nova.network.decoders.packets.handlers.npcs;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.Constants;
import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.masks.Animation;
import org.nova.game.npc.Drop;
import org.nova.game.npc.NPC;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.player.CoordsEvent;
import org.nova.game.player.Player;
import org.nova.game.player.actions.CowMilking;
import org.nova.game.player.actions.Fishing;
import org.nova.game.player.actions.Fishing.FishingSpots;
import org.nova.game.player.actions.MusicanRest;
import org.nova.game.player.actions.PickPocketAction;
import org.nova.game.player.actions.PickPocketableNPC;
import org.nova.game.player.content.DeanVellio;
import org.nova.game.player.content.PlayerLook;
import org.nova.game.player.content.SheepShearing;
import org.nova.game.player.content.exchange.GrandExchange;
import org.nova.game.player.content.itemactions.Decant;
import org.nova.game.player.content.itemactions.DungeoneeringShop;
import org.nova.game.player.content.newuser.CharacterCreation;
import org.nova.game.player.content.playertask.PlayerTaskShop;
import org.nova.game.player.content.quests.UzerRuins;
import org.nova.game.player.content.quests.Wanted;
import org.nova.game.player.controlers.PuroPuro;
import org.nova.game.player.dialogues.FremennikShipmaster;
import org.nova.kshan.content.skills.slayer.SlayerTask;
import org.nova.kshan.dialogues.impl.CosmicEntity;
import org.nova.kshan.input.type.integer.IntegerInput;
import org.nova.network.stream.InputStream;
import org.nova.utility.ShopsHandler;
import org.nova.utility.loading.npcs.NPCDrops;
import org.nova.utility.misc.Misc;

public class NPCActionPacketHandler {
	
	/**
	 * When you examine an NPC, a drop predictor pops up.
	 * @param player
	 * @param npc
	 */
	public static void handleExamine(Player player, NPC npc) {
		if(player.isOwner())
			player.sm(npc.toString()+" - "+npc.getLocation().toString());
		player.sm(npc.getExamine());
		if(npc.defs().hasAttackOption()) {
			if(NPCDrops.getDrops(npc.getId()) != null) {
				Map<Integer, Integer> map = new HashMap<>();
				int npcId = npc.getId();
				player.getInputEvent().run(new IntegerInput() {

					@Override
					public void process(int input) {
						if(input < 0 || input > 100000) {
							player.getDialogue().sendMsg("Invalid input.");
							return;
						}
						int kills = input;
						for (int i = 0; i < kills; i++) {
							Drop[] drops = NPCDrops.getDrops(npcId);
							if (drops == null) {
								return;
							}
							Drop[] possibleDrops = new Drop[drops.length];
							int possibleDropsCount = 0;
							for (Drop drop : drops) {
								if (drop.getRate() == 100) {
									int previousValue = map.get(drop.getItemId()) != null ? map
											.get(drop.getItemId()) : 0;
									map.put(drop.getItemId(),
											drop.getMinAmount()
													+ Misc.getRandom(drop.getExtraAmount())
													+ previousValue);
								} else {
									if (Misc.getRandomDouble(100) <= drop.getRate()) {
										possibleDrops[possibleDropsCount++] = drop;
									}
								}
							}
							if (possibleDropsCount > 0) {
								Drop drop2 = possibleDrops[Misc
										.getRandom(possibleDropsCount - 1)];
								int previousValue = map.get(drop2.getItemId()) != null ? map
										.get(drop2.getItemId()) : 0;
								map.put(drop2.getItemId(),
										drop2.getMinAmount()
												+ Misc.getRandom(drop2.getExtraAmount())
												+ previousValue);
							}
						}
						Item[] items = new Item[map.size()];
						int index = 0;
						for (Entry<Integer, Integer> entry : map.entrySet()) {
							int formatted = entry.getValue() < 0 || entry.getValue() > Integer.MAX_VALUE ? 
								Integer.MAX_VALUE : entry.getValue();
							items[index] = new Item(entry.getKey(), formatted);
							index++;
						}
						player.interfaces().sendInterface(860);
						player.packets().sendHideIComponent(860, 20, true);
						player.packets().sendHideIComponent(860, 21, true);
						player.packets().sendHideIComponent(860, 24, true);
						player.packets().sendHideIComponent(860, 25, true);
						player.packets().sendHideIComponent(860, 26, true);
						player.packets().sendHideIComponent(860, 28, true);
						player.packets().sendString("Drop predictor", 860, 18);
						player.packets().sendString("Showing a list of probable drops if you killed \""+
								npc.getName()+"\" "+NumberFormat.getInstance().format(input)+" time(s).", 860, 19);
						player.packets().sendItems(90, false, items);
						player.packets().sendInterSetItemsOptionsScript(860, 23, 90, 9, 120, "Examine");
						player.packets().sendUnlockIComponentOptionSlots(860, 23, 0, 600, 0);
					}

					@Override
					public void whileTyping(int key, char keyChar, boolean shiftHeld) {
						
					}
					
				}, "Enter the number of kills and view the drop likelihood for \""+
					npc.getName()+"\" (Max is 100,000)");
			}
		}
	}

	public static void handleOption1(final Player player, InputStream stream) {
		@SuppressWarnings("unused")
		boolean unknown = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = Game.getNPCs().get(npcIndex);
		if (!(player.getStopDelay() > 1))
			player.faceEntity(npc);
		player.face(npc);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		if (!(player.getStopDelay() > 1))
			player.face(npc);
		player.stopAll(false);
		if (npc.getName().contains("Sandwich")
				&& player.getRandomEvent().hasNPCOption1(npc))
			return;
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!(player.getStopDelay() > 1))
					player.face(npc);
				FishingSpots spot = FishingSpots.forId(npc.getId() | 1 << 24);
				if (spot != null) {
					player.getActionManager().setSkill(new Fishing(spot, npc));
					return;
				}
				npc.face(player);
				player.face(npc);
				if (!player.getRandomEvent().hasNPCOption1(npc))
					return;
				if (!player.getControllerManager().processNPCClick1(npc))
					return;
				
				if(npc.getId() >= 14691 && npc.getId() <= 14702) { // Pet IDs; add more here anytime
					if(player.getPet() != null && npc.getName().equalsIgnoreCase(player.getPet().getName())) {
						if(npc == player.getPet()) {
							for(int i = 0; i < Misc.getItemsSize(); i++) {
								String name = ItemDefinition.get(i).getName();
								String nName = npc.getName();
								if(name.equalsIgnoreCase(nName)) {
									if(player.getInventory().hasFreeSlots()) {
										player.getInventory().addItem(i, 1);
									} else {
										player.sm("You need more free inventory slots to pick up "+nName+".");
										return;
									}
									if(player.getPet() != null)
										player.getPet().finish();
									player.setPet(null);
									player.sm("You pick up "+nName+".");
								}
							}
						} else {
							player.sm("This is not your pet.");
							return;
						}
					} else {
						player.sm("This is not your pet.");
					}
				}
				
				switch (npc.defs().name.toString().toLowerCase()) {

				case "estate agent":
					player.getDialogue().start("EstateAgentDialogue",
							npc.getId());
					break;

				case "student":
					player.getMatrixDialogues().startDialogue(
							"SimpleNPCMessage", npc.getId(),
							"I'm studying, go away!");
					if (!(player.getStopDelay() > 1))
						player.faceEntity(npc);
					return;

				case "wilfred":
					player.getDialogue().start("WilfredD", npc);
					return;

				case "ozan":
					player.getDialogue().start("OzanD", npc);
					return;

				case "lumbridge sage":
					player.getDialogue().start("LumbridgeSageD", npc);
					return;

				case "surgeon general tafani":
					player.getDialogue().start("SurgeonGeneralTafani", npc);
					return;

				case "eniola":
					player.getMatrixDialogues().startDialogue("Banker",
							npc.getId());
					break;

				case "ethereal guide":
					if (player.getStarterStage() < 2)
						player.getDialogue().start("StarterGuide", npc);
					else
						player.sm("The guide seems busy.");
					return;

				case "cow":
					player.getMatrixDialogues().startDialogue(
							"SimpleNPCMessage", npc.getId(), "Moo moo!");
					break;
				case "mandrith":
					player.getMatrixDialogues().startDialogue("Mand",
							npc.getId());
					break;
				}
				if (npc.defs().name.contains("Raccoon")
						|| npc.defs().name.contains("Fox")) {
					if (!(player.getStopDelay() > 1))

						player.faceEntity(npc);
					player.sendMessage("You cannot pick up the "
							+ npc.getName() + ".");
				}
				if (npc.getId() == 9703) {
					player.getMatrixDialogues().startDialogue("SpiritD",
							npc.getId());
					player.faceEntity(npc);
					return;
				}

				if (npc.defs().name.contains("Musician")) {
					player.getMatrixDialogues().startDialogue("Musicians",
							npc.getId());
				}
				if (npc.defs().name.contains("Spirit")) {
					player.faceEntity(npc);
					player.getDialogue().start("SpiritTree", npc);
				}

				// if (npc.defs().hasOption("examine"))
				// player.sm(npc.getExamine());

				if (npc.getName().toLowerCase().contains("sheep"))
					SheepShearing.shearAttempt(player, npc);
				if (npc.getId() == 1419 || npc.getId() == 2240
						|| npc.getId() == 2241 || npc.getId() == 2593
						|| npc.getId() == 15529 || npc.getId() == 15530) {
					if (!(player.getStopDelay() > 1))
						player.faceEntity(npc);
					if (!player.withinDistance(npc, 2))
						return;
					if (!npc.getName().toLowerCase().contains("fishing spot")) {
						npc.faceEntity(player);
					}
					player.getMatrixDialogues().startDialogue("Banker",
							npc.getId());
				}
				if (npc.getName().equals("Trader Crewmember")) {
					player.getShipcharter().openShipCharter();
					player.sendMessage("Select a destination where you wish to go.");
				}

				if (npc.getName().toLowerCase().equals("monk")) {
					player.getMatrixDialogues().startDialogue("HealingMonk",
							npc.getId());
				} else if (npc.getId() == 6070)
					PuroPuro.openPuroInterface(player);
				/**
				 * We grab NPC by the name.
				 */
				switch (npc.getName().toLowerCase().toString()) {
				case "bartender":
					player.getMatrixDialogues().startDialogue("Bartender",
							npc.getId());
					break;
				case "dairy cow":
					if (player.getInventory().containsItem(1925, 1)) {
						player.getInventory().deleteItem(1925, 1);
						player.setNextAnimation(new Animation(2292));
						player.sendMessage("You take some milk from the "
								+ npc.getName().toLowerCase() + ".");
						player.getInventory().addItem(1927, 1);
					}
					break;
				}

				/**
				 * We grab NPC by ID.
				 */
				switch (npc.getId()) {
				case 520:
					player.sm("Right-click trade the shopkeeper to view his inventory.");
					break;
				case 14688:
					player.getDialogue().start(new CosmicEntity(), npc);
					break;
				case 2253:
					player.getMatrixDialogues().startDialogue("WiseOldMan",
							npc.getId());
					break;
				case DeanVellio.VELLIO:
					player.getMatrixDialogues().startDialogue("DeanVellioD",
							npc.getId());
					break;
				case 2208:
					player.getMatrixDialogues().startDialogue("Lumdo",
							npc.getId());
					break;
				case 5883:
					player.getMatrixDialogues().startDialogue("DwarfD",
							npc.getId());
					break;
				case 1409:
					player.getMatrixDialogues().startDialogue("WaydarQuickD",
							npc.getId());
					break;
				case 1821:
					player.getMatrixDialogues().startDialogue("Daero",
							npc.getId());
					break;
				case 13482:
					player.getMatrixDialogues().startDialogue("SpiritD",
							npc.getId());
					break;
				case 6524:
					player.getMatrixDialogues().startDialogue("DecantingBobD",
							npc.getId());
					break;
				case 5886:
					player.getMatrixDialogues().startDialogue("TicketDwarf",
							npc.getId());
					break;
				case 646:
					player.getMatrixDialogues().startDialogue("CuratorD",
							npc.getId());
					break;
				case 8517:
					player.getMatrixDialogues().startDialogue("JackFrost",
							npc.getId());
					break;
				case 3636:

					player.getMatrixDialogues().startDialogue("SpiritTree",
							npc.getId());
					break;
				case 8786:
					if (player.isCompletedWanted()) {
						player.sendMessage("There is no need to speak with Thak anymore. He is too busy.");
						return;
					} else if (player.getInventory().containsItem(1917, 1)
							&& player.getWantedStage() == 6) {
						Wanted.sendReward(player);
						return;
					}
					player.getMatrixDialogues().startDialogue("ThakD",
							npc.getId());
					break;
				case 8801:
					player.getMatrixDialogues().startDialogue("Bartender",
							npc.getId());
					break;
				case 1841: // Ronald
					if (player.getWantedStage() == 3) {
						player.getMatrixDialogues()
								.startDialogue("SimpleNPCMessage", 1841,
										"Theres no time to talk, find a route to Keldagrim!");
					}
					if (player.isCompletedWanted()) {
						player.getMatrixDialogues()
								.startDialogue("SimpleNPCMessage", 1841,
										"I'm proud of you, feel free to enter Keldagrim.");
						return;
					}
					if (player.getInventory().containsItem(1898, 1)
							|| player.getInventory().containsItem(1897, 1)
							&& player.getWantedStage() == 1) {
						player.getMatrixDialogues().startDialogue("RonaldTWO",
								npc.getId());
					} else {
						player.getMatrixDialogues().startDialogue("Ronald",
								npc.getId());
					}
					break;

				}
				if (npc.getId() == 5933) {
					player.getMatrixDialogues().startDialogue("MariusGiste",
							npc.getId());
				}
				if (npc.getId() == 8629) {
					player.getMatrixDialogues().startDialogue("SandwichLady",
							npc.getId());
				}
				if (npc.getId() == 4517) {
					if (player.isCamelotKnight()) {
						player.getMatrixDialogues().startDialogue("Selene",
								npc.getId());
					} else {
						player.sm("Theres nothing to discuss with Selene.");
					}
				}
				if (npc.getId() == 1281) {
					if (player.isCamelotKnight())
						return;
					if (player.getCamelotknightStage() == 1
							&& !player.isCamelotKnight()) {
						player.getMatrixDialogues().startDialogue("Sigli",
								npc.getId());
					} else if (player.getInventory().containsItem(2140, 5)) {
						player.getMatrixDialogues().startDialogue("SigliTwo",
								npc.getId());
					} else {
						player.sm("Sigli has asked me to bring him 5 cooked chickens, maybe I should move to cooking.");
					}
				}
				if (npc.getId() == 251) {
					if (player.isCamelotKnight())
						return;
					if (player.getInventory().containsItem(769, 1)) {
						player.getMatrixDialogues().startDialogue("ArthurTwo",
								npc.getId());

					} else if (!player.isCamelotKnight()) {
						player.getMatrixDialogues().startDialogue("Arthur",
								npc.getId());
					}
				}
				if (npc.getId() == 9047) {
					if (UzerRuins.isFoundbook() == true
							&& !player.isCompletedRuinsofUzer()) {
						player.getMatrixDialogues().startDialogue("Azzandra",
								npc.getId());
					} else {
						player.sm("There's nothing interesting you to say.");
					}
				}
				if (npc.getId() == 456) {
					if (!player.isCompletedHoarfrost()) {
						player.sm("You have to complete Battle Quest: Hoarfrost Hollow to use his services.");
					} else {
						player.getMatrixDialogues().startDialogue("Switcher",
								npc.getId());
					}
				}
				if (npc.getId() == 13623) {
					if (player.isCompletedHoarfrostDepths() == true) {
						player.sm("There's nothing interesting to talk with.");
					} else if (player.getHoarfrostdepthsStage() == 1) {
						player.getMatrixDialogues().startDialogue("Meteora",
								npc.getId());
					} else if (player.getInventory().containsItem(600, 1)) {
						player.getMatrixDialogues().startDialogue(
								"MeteoraFinish", npc.getId());
					}
				}

				if (npc.getId() == 2660) {
					if (player.isCompletedRuinsofUzer())
						return;
					if (UzerRuins.isFoundbook()) {
						player.sm("You have nothing to do here, I should move in to the Varrock museum.");
					} else if (player.getInventory().containsItem(757, 1)) {
						player.getMatrixDialogues().startDialogue("BookReldo",
								npc.getId());
					} else {
						player.getMatrixDialogues().startDialogue("Reldo",
								npc.getId());
					}
				}
				if (npc.getId() == 970) {

					player.getMatrixDialogues().startDialogue("Diango",
							npc.getId());
				}
				if (npc.getId() == 918)
					player.getMatrixDialogues().startDialogue("Ned",
							npc.getId());
				if (npc.getId() == 7864) {
					if (player.isCompletedRuinsofUzer())
						return;
					player.getMatrixDialogues().startDialogue("SirOwen",
							npc.getId());
				}
				if (npc.getId() == 375) {
					if (player.getInventory().containsItem(9083, 1)) {
						player.sm("You have already Seal of Passage. Try to find Ned at Port Sarim ships.");
					} else {
						player.getMatrixDialogues().startDialogue("Frank",
								npc.getId());
					}
				}

				if (npc.getId() == 6653) {

					player.getMatrixDialogues().startDialogue("TaskD",
							npc.getId());
				}
				if (npc.getId() == 11294) {
					if (player.getInventory().containsItem(5507, 1)) {
						UzerRuins.sendReward(player);
					} else {
						player.getMatrixDialogues().startDialogue("Rewardsman",
								npc.getId());
					}
				}
				if (npc.getId() == 1569)
					player.getMatrixDialogues().startDialogue("Veliaf",
							npc.getId());
				// if (npc.getId() == 741)
				// player.getDialogueManager().startDialogue("Acantha",
				// npc.getId());
				if (npc.getId() == 6970)
					player.getMatrixDialogues().startDialogue("Pikkupstix",
							npc.getId());
				if (npc.getId() == 4475) {
					if (player.isCompletedHoarfrost()) {
						return;
					} else {
						player.getMatrixDialogues().startDialogue("NedShip",
								npc.getId());
					}
				}
				if (npc.getId() == 12379)
					player.getMatrixDialogues().startDialogue("GrimReaper",
							npc.getId());
				if (npc.getId() == 3709)
					player.getMatrixDialogues().startDialogue("MrEx",
							npc.getId());
				if (npc.getId() == 8443)
					player.getMatrixDialogues().startDialogue("Lucien",
							npc.getId());
				if (npc.getId() == 5282)
					player.getMatrixDialogues().startDialogue("Osman",
							npc.getId());
				if (npc.getId() == 798)
					player.getMatrixDialogues().startDialogue("XPRate",
							npc.getId());
				if (npc.getId() == 3373)
					player.getMatrixDialogues().startDialogue("Max",
							npc.getId());
				else if (npc.getId() == 9707)
					player.getMatrixDialogues().startDialogue(
							"FremennikShipmaster", npc.getId(), true);
				else if (npc.getId() == 9708)
					player.getMatrixDialogues().startDialogue(
							"FremennikShipmaster", npc.getId(), false);
				else if (npc.getId() == 15513)
					player.getMatrixDialogues().startDialogue("RoyalGuard",
							npc.getId());
				else if (npc.getId() == 8031)
					player.getMatrixDialogues().startDialogue("Acantha",
							npc.getId());

				else if (npc.getId() == 2676)
					player.getMatrixDialogues().startDialogue("MakeOverMage",
							npc.getId(), 0);
				if (npc.defs().name.contains("Banker")
						|| npc.defs().name.contains("banker")) {
					if (!(player.getStopDelay() > 1))
						player.faceEntity(npc);

					if (!player.withinDistance(npc, 2))
						return;
					if (!npc.getName().toLowerCase().contains("fishing spot")) {
						npc.faceEntity(player);
					}
					player.getMatrixDialogues().startDialogue("Banker",
							npc.getId());
					return;
				}
				if (npc.getName().equals("Egil")
						|| npc.getName().equals("Abel")
						|| npc.getName().equals("Suak"))
					player.getMatrixDialogues().startDialogue("ArtisanDia",
							npc.getId());
				if (npc.getId() == 3670) {
					player.getMatrixDialogues().startDialogue("Escotada",
							npc.getId());
				}
				if (npc.getId() == 9712) {
					if (!player.getInventory().containsItem(15707, 1)) {
						player.getMatrixDialogues().startDialogue(
								"DungeoneeringTutor", npc.getId());
					} else {
						player.sm("You have already Ring of Kinship.");
					}
				}
				if (npc.getId() == 933) {
					player.getMatrixDialogues().startDialogue("Aggie",
							npc.getId());
				}
				for (int slayerMasters : SlayerTask.SLAYER_MASTERS)
					if (npc.getId() == slayerMasters)
						player.getDialogue().start("SlayerMaster", npc.getId());

				switch (npc.getId()) {
				case 13827:
					if (!npc.getName().toLowerCase().contains("fishing spot")) {
						npc.faceEntity(player);
					}
					if (!(player.getStopDelay() > 1))
						player.faceEntity(npc);
					if (player.getInventory().containsItem(317, 1)) {
						player.getMatrixDialogues()
								.startDialogue("SimpleMessage",
										"You still have raw shrimps in your inventory, you must cook them all.");
						return;
					}
					
					if (player.getInventory().containsItem(315, 1)) {
						player.getInventory().reset();
						player.getMatrixDialogues().startDialogue(
								"AvalaniFinish", npc.getId());
					} else {
						player.getMatrixDialogues().startDialogue("AvalaniD",
								npc.getId());
					}
					break;
				case 500:
					player.getMatrixDialogues().startDialogue("MosolRei",
							npc.getId());
					break;
				case 230:
					player.getMatrixDialogues().startDialogue("Jack",
							npc.getId());
					break;
				case 13823:
					player.getMatrixDialogues().startDialogue("Dotmatrix",
							npc.getId());
					break;
				case 13824:// Maverick
					
					if (player.getInventory().containsItem(1609, 5)
							&& player.isSpeakedwithMaverick()) {
						player.getMatrixDialogues().startDialogue(
								"MaverickFinish", npc.getId());
						return;
					}
					if (player.isSpeakedwithMaverick()) {
						if (!player.getInventory().containsItem(1625, 5)
								|| !player.getInventory().containsItem(1755, 1)) {
							CharacterCreation.addGems(player);
						}
					}

					break;
				case 3112:
					if (!player.isCompletedThrone()) {
						player.sm("There is currently nothing to talk about with Sandy.");
						return;
					}

					player.getMatrixDialogues().startDialogue("Sandy",
							npc.getId());
					break;
				case 13172:
					if (player.isCompletedThrone()) {
						player.getMatrixDialogues().startDialogue(
								"ResourcesManagerD", npc.getId());
					} else {
						player.sm("You must have completed quest Throne of Miscellania to use Leela's services.");
					}
					break;
				case 7143:
					if (player.isCompletedExams()) {
						player.getMatrixDialogues()
								.startDialogue("SimpleMessage",
										"You have already completed player safety exam.");
						return;
					}
					if (player.getSkills().getCombatLevelWithSummoning() >= 49) {
						player.getMatrixDialogues().startDialogue(
								"ProfessorHenry", npc.getId());
					} else {
						player.sendMessage("You must have combat level of 50 to start safety exam programme.");
					}
					break;
				case 284:
					player.getMatrixDialogues().startDialogue("Repair",
							npc.getId());
					break;
				case 211:
					if (player.isCompletedThrone()) {
						player.sm("Theres nothing interesting to talk about.");
						return;
					}
					if (player.getInventory().containsItem(3710, 1)
							&& player.getMiscellaniaThrone() == 4) {
						player.getMatrixDialogues().startDialogue(
								"KingPercivalTwo", npc.getId());
					}
					if (player.getMiscellaniaThrone() == 1) {
						player.getMatrixDialogues().startDialogue(
								"KingPercival", npc.getId());
					}
					break;

				case 905:
					player.getMatrixDialogues().startDialogue("Kolodion",
							npc.getId());
					break;

				case 469:
					if (player.isCompletedThrone()) {
						player.sm("Theres nothing to talk about right now.");
						return;
					}
					if (player.getMiscellaniaThrone() == 4
							&& !player.getInventory().containsItem(3710, 1)) {
						player.sm("King noticed that you have lost the contract, you have now new one.");
						player.getInventory().addItem(3710, 1);
					}
					if (player.getMiscellaniaThrone() == 2) {
						player.getMatrixDialogues().startDialogue(
								"KingBolrenTwo", npc.getId());
					}
					if (player.getMiscellaniaThrone() == 3) {

						player.getMatrixDialogues().startDialogue(
								"KingBolrenThird", npc.getId());
					} else if (player.getMiscellaniaThrone() == 0) {
						player.getMatrixDialogues().startDialogue("KingBolren",
								npc.getId());
					}
					break;
				// default:
				// player.dialogue().start("AllNPCsD", npc.getId());
				// break;
				}

				System.out.println("NPC Option 1: " + npc.getId() + ", "
						+ npc.getX() + ", " + npc.getY() + ", " + npc.getZ());

			}
		}, npc.getSize()));
	}

	public static void handleOption2(final Player player, InputStream stream) {
		@SuppressWarnings("unused")
		boolean unknown = stream.readByte128() == 1;
		int npcIndex = stream.readUnsignedShort128();
		final NPC npc = Game.getNPCs().get(npcIndex);
		player.setNextFaceEntity(npc);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);

		switch (npc.getId()) {
		case 520:
			ShopsHandler.openShop(player, 1);
			break;
		case 961:
			if (player.getHitpoints() == player.getMaxHitpoints())
				player.sm("You are not in need of healing");
			else {
				if (player.getPoison().isPoisoned()) {
					player.sm("The surgeon heals you and cures your poison.");
					player.getPoison().reset();
					player.heal(player.getMaxHitpoints());
				} else {
					player.sm("The surgeon heals you to a healthy state.");
					player.heal(player.getMaxHitpoints());
				}
			}
			break;
		case 1409:
			player.getMatrixDialogues().startDialogue("WaydarQuickD",
					npc.getId());
			break;
		case 6362:
			player.getBank().openBank();
			break;
		}
		if (npc.defs().name.contains("Banker")
				|| npc.defs().name.contains("banker")) {
			if (!(player.getStopDelay() > 1))
				player.faceEntity(npc);
			if (!player.withinDistance(npc, 2))
				return;
			if (!npc.getName().toLowerCase().contains("fishing spot")) {
				npc.faceEntity(player);
			}
			player.getBank().openBank();
			return;
		}

		// if (npc.defs().hasOption("examine"))
		// player.sm

		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!(player.getStopDelay() > 1))
					player.faceEntity(npc);
				if (!player.getRandomEvent().hasNPCOption2(npc))
					return;
				FishingSpots spot = FishingSpots.forId(npc.getId() | (2 << 24));
				if (npc.getId() == 11294)
					player.getMatrixDialogues().startDialogue(
							"SimpleNPCMessage", 11294,
							"You have currently " + player.questpoints + ".");
				if (spot != null) {
					player.getActionManager().setSkill(new Fishing(spot, npc));
					return;
				}
				PickPocketableNPC pocket = PickPocketableNPC.get(npc.getId());
				if (pocket != null) {
					player.getActionManager().setSkill(
							new PickPocketAction(npc, pocket));
					return;
				}
				if (npc.defs().name.contains("Musician")) {
					long currentTime = Misc.currentTimeMillis();
					if (player.getEmotesManager().getNextEmoteEnd() >= currentTime) {
						player.packets().sendMessage(
								"You can't rest while perfoming an emote.");
						return;
					}
					if (player.getStopDelay() >= currentTime) {
						player.packets().sendMessage(
								"You can't rest while perfoming an action.");
						return;
					}
					player.stopAll();
					player.getActionManager().setSkill(new MusicanRest());

				}
				if (npc instanceof Familiar) {
					if (npc.defs().hasOption("store")) {
						if (player.getFamiliar() != npc) {
							player.packets().sendMessage(
									"That isn't your familiar.");
							return;
						}
						player.getFamiliar().store();
					} else if (npc.defs().hasOption("cure")) {
						if (player.getFamiliar() != npc) {
							player.packets().sendMessage(
									"That isn't your familiar.");
							return;
						}
						if (npc.defs().name.contains("Musician")) {
							player.sm("The musician's music relaxes you.");
						}
						if (npc.getName().equals("Dairy cow"))
							player.getActionManager().setAction(
									new CowMilking());
						if (npc.getId() == 11294) {
							player.sm(npc.getName());
						}

						if (!player.getPoison().isPoisoned()) {
							player.packets().sendMessage(
									"Your arent poisoned or diseased.");
							return;
						} else {
							player.getFamiliar().drainSpecial(2);
							player.addPoisonImmune(120);
						}
					}
					return;
				}
				if (npc.defs().hasPickupOption() || npc.defs().hasTakeOption()) {
					if (!player.withinDistance(npc, 2)) {
						return;
					}
					if (!(player.getStopDelay() > 1))
						player.faceEntity(npc);
					if (player.getPetFollow() != player.getIndex()) {
						player.sendMessage("This isn't your pet!");
						return;
					}
					if (player.getPetId() == 0) {
						return;
					}
					player.getRealPet().dissmissPet(false);
					return;
				}
				if (!npc.getName().toLowerCase().contains("fishing spot")) {
					npc.faceEntity(player);
				}
				if (!player.getControllerManager().processNPCClick2(npc))
					return;
				if (npc.getId() == 1419 || npc.getId() == 2240
						|| npc.getId() == 2241 || npc.getId() == 2593
						|| npc.getId() == 15529 || npc.getId() == 15530) {
					GrandExchange.openGE(player);
				}
				if (npc.getId() == 6653) {
					PlayerTaskShop.openStore(player);
				}
				if (npc.getId() == 8461)
					player.getMatrixDialogues().startDialogue("Turael",
							npc.getId());
				if (npc.getId() == 9707)
					FremennikShipmaster.sail(player, true);
				else if (npc.getId() == 9708)
					FremennikShipmaster.sail(player, false);
				else if (npc.getId() == 13455)
					player.getBank().openBank();
				else if (npc.getId() == 9711)
					DungeoneeringShop.openRewardsShop(player);
				else if (npc.getId() == 519)
					ShopsHandler.openShop(player, 1);
				else if (npc.getId() == DeanVellio.VELLIO)
					ShopsHandler.openShop(player, 22);
				else if (npc.getId() == 520)
					ShopsHandler.openShop(player, 99);
				if (npc.getId() == 14057) {
					player.getMatrixDialogues().startDialogue("Velio",
							npc.getId());
					// player.sm("Starting Velio Dialogue");
				}
				if (npc.getName().equals("Monk")) {
					player.getMatrixDialogues().startDialogue("HealingMonk",
							npc.getId());
				}
				if (npc.getId() == 14078) {
					player.getMatrixDialogues().startDialogue("Varnis",
							npc.getId());
					// player.sm("Starting Varnis Dialogue");
				} else if (npc.getId() == 521)
					ShopsHandler.openShop(player, 3);
				else if (npc.getId() == 522)
					ShopsHandler.openShop(player, 4);
				else if (npc.getId() == 523)
					ShopsHandler.openShop(player, 5);
				else if (npc.getId() == 524)
					ShopsHandler.openShop(player, 6);
				else if (npc.getId() == 525)
					ShopsHandler.openShop(player, 7);
				else if (npc.getId() == 526)
					ShopsHandler.openShop(player, 8);
				else if (npc.getId() == 527)
					ShopsHandler.openShop(player, 9);
				else if (npc.getId() == 528)
					ShopsHandler.openShop(player, 10);
				else if (npc.getId() == 529)
					ShopsHandler.openShop(player, 11);
				else if (npc.getId() == 530)
					ShopsHandler.openShop(player, 12);
				else if (npc.getId() == 531)
					ShopsHandler.openShop(player, 13);
				else if (npc.getId() == 534)
					ShopsHandler.openShop(player, 14);
				else if (npc.getId() == 535)
					ShopsHandler.openShop(player, 15);
				else if (npc.getId() == 551)
					ShopsHandler.openShop(player, 16);
				else if (npc.getId() == 552)
					ShopsHandler.openShop(player, 17);
				else if (npc.getId() == 554)
					ShopsHandler.openShop(player, 18);
				else if (npc.getId() == 555)
					ShopsHandler.openShop(player, 19);
				else if (npc.getId() == 561)
					ShopsHandler.openShop(player, 20);
				else if (npc.getId() == 1699)
					ShopsHandler.openShop(player, 21);
				else if (npc.getId() == 1917)
					ShopsHandler.openShop(player, 22);
				else if (npc.getId() == 11678)
					ShopsHandler.openShop(player, 23);
				else if (npc.getId() == 11679)
					ShopsHandler.openShop(player, 24);
				else if (npc.getId() == 536)
					ShopsHandler.openShop(player, 25);
				else if (npc.getId() == 537)
					ShopsHandler.openShop(player, 26);
				else if (npc.getId() == 538)
					ShopsHandler.openShop(player, 27);
				else if (npc.getId() == 556)
					ShopsHandler.openShop(player, 28);
				else if (npc.getId() == 540)
					ShopsHandler.openShop(player, 29);
				else if (npc.getId() == 541)
					ShopsHandler.openShop(player, 30);
				else if (npc.getId() == 542)
					ShopsHandler.openShop(player, 31);
				else if (npc.getId() == 545)
					ShopsHandler.openShop(player, 33);
				else if (npc.getId() == 873)
					ShopsHandler.openShop(player, 34);
				else if (npc.getId() == 6893)
					ShopsHandler.openShop(player, 36);
				else if (npc.getId() == 2676)
					PlayerLook.openMageMakeOver(player);
				else {
					System.out.println("cliked 2 at npc id : " + npc.getId()
							+ ", " + npc.getX() + ", " + npc.getY() + ", "
							+ npc.getZ());
				}
			}
		}, npc.getSize()));
	}

	public static void handleOption3(final Player player, InputStream stream) {
		int npcIndex = stream.readUnsignedShort128();
		boolean forceRun = stream.read128Byte() == 1;
		final NPC npc = Game.getNPCs().get(npcIndex);
		player.setNextFaceEntity(npc);
		if (npc == null || npc.isCantInteract() || npc.isDead()
				|| npc.hasFinished()
				|| !player.getMapRegionsIds().contains(npc.getRegionId()))
			return;
		player.stopAll(false);
		if (forceRun)
			player.setMusicianResting(forceRun);
		player.setCoordsEvent(new CoordsEvent(npc, new Runnable() {
			@Override
			public void run() {
				npc.resetWalkSteps();
				if (!player.getRandomEvent().hasNPCOption3(npc))
					return;
				if (!player.getControllerManager().processNPCClick3(npc))
					return;
				if (!(player.getStopDelay() > 1))
					player.faceEntity(npc);
				if (npc.getId() >= 8837 && npc.getId() <= 8839) {
					return;
				}
				if (npc.defs().hasOption("examine")) {
					player.interfaces().sendInterface(109);
				}
				if (npc.getId() == 11294) {
					player.sm(npc.getName());
				}
				if (npc.getName().equals("Banker")) {
					player.interfaces().sendInterface(109);
				}
				// if (npc.defs().hasOption("examine"))
				// Examines.getExamine(npc.getId());
				if (npc.getId() == 11294)
					player.getMatrixDialogues().startDialogue(
							"SimpleNPCMessage", 11294,
							"You have currently " + player.questpoints + ".");
				if (npc.getName().equals("Trader Crewmember")) {
					player.getShipcharter().openShipCharter();
					player.sendMessage("Select a destination where you wish to go.");
				}
				switch (npc.getId()) {
				case 6524:
					Decant decant = new Decant(player);
					decant.sendDecantingTask();
					break;

				}
				if (npc.getId() == 13727) {
					player.packets().sendMessage("Title cleared.");
					player.getAppearance().setTitle(0);
					player.getDisplayName();
					player.getAppearance().generateAppearanceData();
				}
				if (npc.getId() == 3374) {
					ShopsHandler.openShop(player, 18);
				}
				if (!npc.getName().toLowerCase().contains("fishing spot")) {
					npc.faceEntity(player);
				}
				if (npc.getId() == 548) {
				}
				if (npc.getId() == 5532) {
				}
			}
		}, npc.getSize()));
		if (Constants.DEVELOPER_MODE)
			System.out.println("cliked 3 at npc id : " + npc.getId() + ", "
					+ npc.getX() + ", " + npc.getY() + ", " + npc.getZ());
	}

}