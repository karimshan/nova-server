package org.nova.game.player.dialogues;

import java.util.HashMap;

import org.nova.kshan.dialogues.impl.NexEntrance;

public final class MatrixDialogueHandler {

	private static final HashMap<Object, Class<MatrixDialogue>> handledDialogues = new HashMap<Object, Class<MatrixDialogue>>();

	@SuppressWarnings("unchecked")
	public static final void init() {
		handledDialogues.clear();
		try {
			handledDialogues.put("ThessaliaD", (Class<MatrixDialogue>) Class.forName(ThessaliaD.class.getCanonicalName()));
				handledDialogues.put("LanderD", (Class<MatrixDialogue>) Class.forName(LanderD.class.getCanonicalName()));
			handledDialogues.put("ClanWarsViewing", (Class<MatrixDialogue>) Class.forName(ClanWarsViewing.class.getCanonicalName()));
			handledDialogues.put("WiseOldMan", (Class<MatrixDialogue>) Class.forName(WiseOldMan.class.getCanonicalName()));
				handledDialogues.put("DungRewardConfirm", (Class<MatrixDialogue>) Class.forName(DungRewardConfirm.class.getCanonicalName()));
			handledDialogues.put("DragonfireFinish", (Class<MatrixDialogue>) Class.forName(DragonfireFinish.class.getCanonicalName()));
			handledDialogues.put("PrayerD", (Class<MatrixDialogue>) Class.forName(PrayerD.class.getCanonicalName()));
			handledDialogues.put("SpinningD", (Class<MatrixDialogue>) Class.forName(SpinningD.class.getCanonicalName()));
			handledDialogues.put("Mand", (Class<MatrixDialogue>) Class.forName(Mand.class.getCanonicalName()));
			handledDialogues.put("Lumdo", (Class<MatrixDialogue>) Class.forName(Lumdo.class.getCanonicalName()));
			handledDialogues.put("DeanVellioD", (Class<MatrixDialogue>) Class.forName(DeanVellioD.class.getCanonicalName()));
			handledDialogues.put("RottenPotatoD", (Class<MatrixDialogue>) Class.forName(RottenPotatoD.class.getCanonicalName()));
			handledDialogues.put("PartyRoomLever", (Class<MatrixDialogue>) Class.forName(PartyRoomLever.class.getCanonicalName()));
				/**
			 * Vellio's dialogues.
			 * 
			 */
			handledDialogues.put("VellioLow", (Class<MatrixDialogue>) Class.forName(VellioLow.class.getCanonicalName()));
			handledDialogues.put("VellioMid", (Class<MatrixDialogue>) Class.forName(VellioMid.class.getCanonicalName()));
			handledDialogues.put("VellioHard", (Class<MatrixDialogue>) Class.forName(VellioHard.class.getCanonicalName()));
			
			handledDialogues.put("DwarfD", (Class<MatrixDialogue>) Class.forName(DwarfD.class.getCanonicalName()));
			handledDialogues.put("DragonfireShieldD", (Class<MatrixDialogue>) Class.forName(DragonfireShieldD.class.getCanonicalName()));
			handledDialogues.put("Bartender", (Class<MatrixDialogue>) Class.forName(Bartender.class.getCanonicalName()));
			handledDialogues.put("ThakD", (Class<MatrixDialogue>) Class.forName(ThakD.class.getCanonicalName()));
			handledDialogues.put("AvalaniD", (Class<MatrixDialogue>) Class.forName(AvalaniD.class.getCanonicalName()));
			handledDialogues.put("Maverick", (Class<MatrixDialogue>) Class.forName(Maverick.class.getCanonicalName()));
			handledDialogues.put("Dotmatrix", (Class<MatrixDialogue>) Class.forName(Dotmatrix.class.getCanonicalName()));
			handledDialogues.put("ProfessorHenry", (Class<MatrixDialogue>) Class.forName(ProfessorHenry.class.getCanonicalName()));
			handledDialogues.put("MosolRei", (Class<MatrixDialogue>) Class.forName(MosolRei.class.getCanonicalName()));
			handledDialogues.put("Jack", (Class<MatrixDialogue>) Class.forName(Jack.class.getCanonicalName()));
			handledDialogues.put("WaydarQuickD", (Class<MatrixDialogue>) Class.forName(WaydarQuickD.class.getCanonicalName()));
			handledDialogues.put("SpiritTree", (Class<MatrixDialogue>) Class.forName(SpiritTree.class.getCanonicalName()));
			handledDialogues.put("ArtisanDia", (Class<MatrixDialogue>) Class.forName(ArtisanDia.class.getCanonicalName()));
			handledDialogues.put("HealingMonk", (Class<MatrixDialogue>) Class.forName(HealingMonk.class.getCanonicalName()));
			handledDialogues.put("Daero", (Class<MatrixDialogue>) Class.forName(Daero.class.getCanonicalName()));
			handledDialogues.put("JackFrost", (Class<MatrixDialogue>) Class.forName(JackFrost.class.getCanonicalName()));
			handledDialogues.put("DecantingBobD", (Class<MatrixDialogue>) Class.forName(DecantingBobD.class.getCanonicalName()));
			handledDialogues.put("Advisor", (Class<MatrixDialogue>) Class.forName(Advisor.class.getCanonicalName()));
			handledDialogues.put("Musicians", (Class<MatrixDialogue>) Class.forName(Musicians.class.getCanonicalName()));
			handledDialogues.put("Kolodion", (Class<MatrixDialogue>) Class.forName(Kolodion.class.getCanonicalName()));
			handledDialogues.put("CuratorD", (Class<MatrixDialogue>) Class.forName(CuratorD.class.getCanonicalName()));
					Class<MatrixDialogue> value79 = (Class<MatrixDialogue>) Class
					.forName(Varnis.class.getCanonicalName());
			handledDialogues.put("Varnis", value79);

			handledDialogues.put("XPBook", (Class<MatrixDialogue>) Class
					.forName(XPBook.class.getCanonicalName()));
			handledDialogues.put("MrEx", (Class<MatrixDialogue>) Class
					.forName(MrEx.class.getCanonicalName()));
			
			handledDialogues.put("FlowerPickup", (Class<MatrixDialogue>) Class.forName(FlowerPickup.class.getCanonicalName()));
			handledDialogues.put("Ronald", (Class<MatrixDialogue>) Class.forName(Ronald.class.getCanonicalName()));
			handledDialogues.put("RonaldTWO", (Class<MatrixDialogue>) Class.forName(RonaldTWO.class.getCanonicalName()));
			handledDialogues.put("TicketDwarf", (Class<MatrixDialogue>) Class.forName(TicketDwarf.class.getCanonicalName()));
			handledDialogues.put("Escotada", (Class<MatrixDialogue>) Class.forName(Escotada.class.getCanonicalName()));
				
			handledDialogues.put("Sandy", (Class<MatrixDialogue>) Class.forName(Sandy.class.getCanonicalName()));
			handledDialogues.put("ResourcesManagerD", (Class<MatrixDialogue>) Class.forName(ResourcesManagerD.class.getCanonicalName()));
			handledDialogues.put("FrogD", (Class<MatrixDialogue>) Class.forName(FrogD.class.getCanonicalName()));
			handledDialogues.put("KingFrogD", (Class<MatrixDialogue>) Class.forName(KingFrogD.class.getCanonicalName()));
			

			handledDialogues.put("Diango", (Class<MatrixDialogue>) Class.forName(Diango.class.getCanonicalName()));
			handledDialogues.put("KingBolren", (Class<MatrixDialogue>) Class.forName(KingBolren.class.getCanonicalName()));
			handledDialogues.put("KingBolrenTwo", (Class<MatrixDialogue>) Class.forName(KingBolrenTwo.class.getCanonicalName()));
			handledDialogues.put("KingPercival", (Class<MatrixDialogue>) Class.forName(KingPercival.class.getCanonicalName()));
			handledDialogues.put("KingPercivalTwo", (Class<MatrixDialogue>) Class.forName(KingPercivalTwo.class.getCanonicalName()));

			handledDialogues.put("KingBolrenThird", (Class<MatrixDialogue>) Class.forName(KingBolrenThird.class.getCanonicalName()));

			handledDialogues.put("MariusGiste", (Class<MatrixDialogue>) Class.forName(MariusGiste.class.getCanonicalName()));

			handledDialogues.put("AuraSelect", (Class<MatrixDialogue>) Class.forName(AuraSelect.class.getCanonicalName()));
			
			handledDialogues.put("Repair", (Class<MatrixDialogue>) Class.forName(Repair.class.getCanonicalName()));

		

			handledDialogues.put("Selene", (Class<MatrixDialogue>) Class.forName(Selene.class.getCanonicalName()));

			 
				  Class<MatrixDialogue> value1756 = (Class<MatrixDialogue>) Class
							.forName(Azzandra.class.getCanonicalName());
					handledDialogues.put("Azzandra", value1756);

					   Class<MatrixDialogue> value1111 = (Class<MatrixDialogue>) Class
								.forName(GameTeles.class.getCanonicalName());
						handledDialogues.put("Game", value1111);
					

					   Class<MatrixDialogue> value1866 = (Class<MatrixDialogue>) Class
								.forName(Arthur.class.getCanonicalName());
						handledDialogues.put("Arthur", value1866);
						

						   Class<MatrixDialogue> value1412 = (Class<MatrixDialogue>) Class
									.forName(WildyHats.class.getCanonicalName());
							handledDialogues.put("WildyHats", value1412);
							
						
						
						  Class<MatrixDialogue> value1826 = (Class<MatrixDialogue>) Class
									.forName(ArthurTwo.class.getCanonicalName());
							handledDialogues.put("ArthurTwo", value1826);
							
							
							  Class<MatrixDialogue> value333 = (Class<MatrixDialogue>) Class
										.forName(Elof.class.getCanonicalName());
								handledDialogues.put("Elof", value333);
								
							
							  Class<MatrixDialogue> value1026 = (Class<MatrixDialogue>) Class
										.forName(Costumes.class.getCanonicalName());
								handledDialogues.put("Costumes", value1026);
							
							
						  Class<MatrixDialogue> value1181 = (Class<MatrixDialogue>) Class
									.forName(Sigli.class.getCanonicalName());
							handledDialogues.put("Sigli", value1181);
							
							 Class<MatrixDialogue> value1171 = (Class<MatrixDialogue>) Class
										.forName(SigliTwo.class.getCanonicalName());
								handledDialogues.put("SigliTwo", value1171);
								

                    
				   Class<MatrixDialogue> value1859 = (Class<MatrixDialogue>) Class
							.forName(Acantha.class.getCanonicalName());
					handledDialogues.put("Acantha", value1859);
					

					   Class<MatrixDialogue> value1119 = (Class<MatrixDialogue>) Class
								.forName(SandwichLady.class.getCanonicalName());
						handledDialogues.put("SandwichLady", value1119);
						
						  Class<MatrixDialogue> value1112 = (Class<MatrixDialogue>) Class
									.forName(DungeoneeringTutor.class.getCanonicalName());
							handledDialogues.put("DungeoneeringTutor", value1112);
							
				
				Class<MatrixDialogue> value667 = (Class<MatrixDialogue>) Class
						.forName(Rewardsman.class.getCanonicalName());
				handledDialogues.put("Rewardsman", value667);
				
				Class<MatrixDialogue> value99 = (Class<MatrixDialogue>) Class
						.forName(Ned.class.getCanonicalName());
				handledDialogues.put("Ned", value99);
			

				Class<MatrixDialogue> value946 = (Class<MatrixDialogue>) Class
						.forName(BookReldo.class.getCanonicalName());
				handledDialogues.put("BookReldo", value946);
				
				Class<MatrixDialogue> value966 = (Class<MatrixDialogue>) Class
						.forName(Reldo.class.getCanonicalName());
				handledDialogues.put("Reldo", value966);
				Class<MatrixDialogue> value967 = (Class<MatrixDialogue>) Class
						.forName(SirOwen.class.getCanonicalName());
				handledDialogues.put("SirOwen", value967);
			
				
				
				
				Class<MatrixDialogue> value165 = (Class<MatrixDialogue>) Class
						.forName(Islands.class.getCanonicalName());
				handledDialogues.put("Islands", value165);
			
				
				Class<MatrixDialogue> value184 = (Class<MatrixDialogue>) Class
						.forName(Slayer.class.getCanonicalName());
				handledDialogues.put("Slayer", value184);
			

				Class<MatrixDialogue> value711 = (Class<MatrixDialogue>) Class
						.forName(Bosses.class.getCanonicalName());
				handledDialogues.put("Bosses", value711);
				
				Class<MatrixDialogue> value712 = (Class<MatrixDialogue>) Class
						.forName(Cities.class.getCanonicalName());
				handledDialogues.put("Cities", value712);
	
				
				Class<MatrixDialogue> value322 = (Class<MatrixDialogue>) Class
						.forName(Cities.class.getCanonicalName());
				handledDialogues.put("Cities", value322);
	
			
				
				Class<MatrixDialogue> value622 = (Class<MatrixDialogue>) Class
						.forName(Dungeons.class.getCanonicalName());
				handledDialogues.put("Dungeons", value622);
			

				Class<MatrixDialogue> value1711 = (Class<MatrixDialogue>) Class
						.forName(Forests.class.getCanonicalName());
				handledDialogues.put("Forests", value1711);
			
				
				Class<MatrixDialogue> value188 = (Class<MatrixDialogue>) Class
						.forName(Meteora.class.getCanonicalName());
				handledDialogues.put("Meteora", value188);


				
				Class<MatrixDialogue> value19112 = (Class<MatrixDialogue>) Class
						.forName(MeteoraFinish.class.getCanonicalName());
				handledDialogues.put("MeteoraFinish", value19112);
			
				
				Class<MatrixDialogue> value161 = (Class<MatrixDialogue>) Class
						.forName(Acantha.class.getCanonicalName());
				handledDialogues.put("Acantha", value161);

				
				
				Class<MatrixDialogue> value811 = (Class<MatrixDialogue>) Class
						.forName(Osman.class.getCanonicalName());
				handledDialogues.put("Osman", value811);
			
				  Class<MatrixDialogue> value102 = (Class<MatrixDialogue>) Class
							.forName(Switcher.class.getCanonicalName());
					handledDialogues.put("Switcher", value102);
					
					
				  Class<MatrixDialogue> value101 = (Class<MatrixDialogue>) Class
							.forName(NedShip.class.getCanonicalName());
					handledDialogues.put("NedShip", value101);
				
				
				
				Class<MatrixDialogue> value100 = (Class<MatrixDialogue>) Class
							.forName(Frank.class.getCanonicalName());
					handledDialogues.put("Frank", value100);
				
					
			Class<MatrixDialogue> value1 = (Class<MatrixDialogue>) Class
					.forName(LevelUp.class.getCanonicalName());
			handledDialogues.put("LevelUp", value1);

			Class<MatrixDialogue> value57 = (Class<MatrixDialogue>) Class
					.forName(DungLad.class.getCanonicalName());
			handledDialogues.put("DungLad", value57);
			
			Class<MatrixDialogue> value61 = (Class<MatrixDialogue>) Class
					.forName(Crate.class.getCanonicalName());
			handledDialogues.put("Crate", value61);
			Class<MatrixDialogue> value62 = (Class<MatrixDialogue>) Class
					.forName(Pool.class.getCanonicalName());
			handledDialogues.put("Pool", value62);
		
			
			Class<MatrixDialogue> value721 = (Class<MatrixDialogue>) Class
					.forName(Minigames.class.getCanonicalName());
			handledDialogues.put("Minigames", value721);
			
			
			Class<MatrixDialogue> value600 = (Class<MatrixDialogue>) Class
					.forName(FirstVant.class.getCanonicalName());
			handledDialogues.put("FirstVant", value600);
			
			Class<MatrixDialogue> value601 = (Class<MatrixDialogue>) Class
					.forName(OculusOrb.class.getCanonicalName());
			handledDialogues.put("OculusOrb", value601);
			
			Class<MatrixDialogue> value621 = (Class<MatrixDialogue>) Class
					.forName(Aggie.class.getCanonicalName());
			handledDialogues.put("Aggie", value621);
		
			
			Class<MatrixDialogue> value2 = (Class<MatrixDialogue>) Class
					.forName(ZarosAltar.class.getCanonicalName());
			handledDialogues.put("ZarosAltar", value2);
			Class<MatrixDialogue> value3 = (Class<MatrixDialogue>) Class
					.forName(ClimbNoEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbNoEmoteStairs", value3);
			Class<MatrixDialogue> value4 = (Class<MatrixDialogue>) Class
					.forName(Banker.class.getCanonicalName());
			handledDialogues.put("Banker", value4);
			Class<MatrixDialogue> value5 = (Class<MatrixDialogue>) Class
					.forName(DestroyItemOption.class.getCanonicalName());
			handledDialogues.put("DestroyItemOption", value5);
			Class<MatrixDialogue> value6 = (Class<MatrixDialogue>) Class
					.forName(FremennikShipmaster.class.getCanonicalName());
			handledDialogues.put("FremennikShipmaster", value6);
			Class<MatrixDialogue> value7 = (Class<MatrixDialogue>) Class
					.forName(DungeonExit.class.getCanonicalName());
			handledDialogues.put("DungeonExit", value7);
			Class<MatrixDialogue> value8 = (Class<MatrixDialogue>) Class
					.forName(NexEntrance.class.getCanonicalName());
			handledDialogues.put("NexEntrance", value8);
			Class<MatrixDialogue> value9 = (Class<MatrixDialogue>) Class
					.forName(MagicPortal.class.getCanonicalName());
			handledDialogues.put("MagicPortal", value9);
			Class<MatrixDialogue> value10 = (Class<MatrixDialogue>) Class
					.forName(LunarAltar.class.getCanonicalName());
			handledDialogues.put("LunarAltar", value10);
			Class<MatrixDialogue> value11 = (Class<MatrixDialogue>) Class
					.forName(AncientAltar.class.getCanonicalName());
			handledDialogues.put("AncientAltar", value11);
			// TODO 12 and 13
			Class<MatrixDialogue> value12 = (Class<MatrixDialogue>) Class
					.forName(FletchingD.class.getCanonicalName());
			handledDialogues.put("FletchingD", value12);
			Class<MatrixDialogue> value14 = (Class<MatrixDialogue>) Class
					.forName(RuneScapeGuide.class.getCanonicalName());
			handledDialogues.put("RuneScapeGuide", value14);
			Class<MatrixDialogue> value15 = (Class<MatrixDialogue>) Class
					.forName(SurvivalExpert.class.getCanonicalName());
			handledDialogues.put("SurvivalExpert", value15);
			Class<MatrixDialogue> value16 = (Class<MatrixDialogue>) Class
					.forName(SimpleMessage.class.getCanonicalName());
			handledDialogues.put("SimpleMessage", value16);
			Class<MatrixDialogue> value17 = (Class<MatrixDialogue>) Class
					.forName(ItemMessage.class.getCanonicalName());
			handledDialogues.put("ItemMessage", value17);
			Class<MatrixDialogue> value18 = (Class<MatrixDialogue>) Class
					.forName(ClimbEmoteStairs.class.getCanonicalName());
			handledDialogues.put("ClimbEmoteStairs", value18);
			Class<MatrixDialogue> value20 = (Class<MatrixDialogue>) Class
					.forName(GemCuttingD.class.getCanonicalName());
			handledDialogues.put("GemCuttingD", value20);
			Class<MatrixDialogue> value21 = (Class<MatrixDialogue>) Class
					.forName(CookingD.class.getCanonicalName());
			handledDialogues.put("CookingD", value21);
			Class<MatrixDialogue> value22 = (Class<MatrixDialogue>) Class
					.forName(HerbloreD.class.getCanonicalName());
			handledDialogues.put("HerbloreD", value22);
			Class<MatrixDialogue> value23 = (Class<MatrixDialogue>) Class
					.forName(BarrowsD.class.getCanonicalName());
			handledDialogues.put("BarrowsD", value23);
			Class<MatrixDialogue> value24 = (Class<MatrixDialogue>) Class
					.forName(SmeltingD.class.getCanonicalName());
			handledDialogues.put("SmeltingD", value24);
			Class<MatrixDialogue> value25 = (Class<MatrixDialogue>) Class
					.forName(LeatherCraftingD.class.getCanonicalName());
			handledDialogues.put("LeatherCraftingD", value25);
			
			Class<MatrixDialogue> value27 = (Class<MatrixDialogue>) Class
					.forName(ForfeitDialouge.class.getCanonicalName());
			handledDialogues.put("ForfeitDialouge", value27);
			Class<MatrixDialogue> value28 = (Class<MatrixDialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value28);
			Class<MatrixDialogue> value29 = (Class<MatrixDialogue>) Class
					.forName(WildernessDitch.class.getCanonicalName());
			handledDialogues.put("WildernessDitch", value29);
			Class<MatrixDialogue> value30 = (Class<MatrixDialogue>) Class
					.forName(SimpleNPCMessage.class.getCanonicalName());
			handledDialogues.put("SimpleNPCMessage", value30);
			Class<MatrixDialogue> value31 = (Class<MatrixDialogue>) Class
					.forName(Transportation.class.getCanonicalName());
			handledDialogues.put("Transportation", value31);
			Class<MatrixDialogue> value32 = (Class<MatrixDialogue>) Class
					.forName(DTSpectateReq.class.getCanonicalName());
			handledDialogues.put("DTSpectateReq", value32);
			Class<MatrixDialogue> value33 = (Class<MatrixDialogue>) Class
					.forName(StrangeFace.class.getCanonicalName());
			handledDialogues.put("StrangeFace", value33);
			Class<MatrixDialogue> value34 = (Class<MatrixDialogue>) Class
					.forName(AncientEffigiesD.class.getCanonicalName());
			handledDialogues.put("AncientEffigiesD", value34);
			Class<MatrixDialogue> value35 = (Class<MatrixDialogue>) Class
					.forName(DTClaimRewards.class.getCanonicalName());
			handledDialogues.put("DTClaimRewards", value35);
			Class<MatrixDialogue> value36 = (Class<MatrixDialogue>) Class
					.forName(SetSkills.class.getCanonicalName());
			handledDialogues.put("SetSkills", value36);
			Class<MatrixDialogue> value37 = (Class<MatrixDialogue>) Class
					.forName(DismissD.class.getCanonicalName());
			handledDialogues.put("DismissD", value37);
			Class<MatrixDialogue> value39 = (Class<MatrixDialogue>) Class
					.forName(MakeOverMage.class.getCanonicalName());
			handledDialogues.put("MakeOverMage", value39);
			Class<MatrixDialogue> value40 = (Class<MatrixDialogue>) Class
					.forName(KaramjaTrip.class.getCanonicalName());
			handledDialogues.put("KaramjaTrip", value40);
			Class<MatrixDialogue> value41 = (Class<MatrixDialogue>) Class
					.forName(OzanD.class.getCanonicalName());
			handledDialogues.put("OzanD", value41);
			Class<MatrixDialogue> value43 = (Class<MatrixDialogue>) Class
					.forName(Lucien.class.getCanonicalName());
			handledDialogues.put("Lucien", value43);
			Class<MatrixDialogue> value44 = (Class<MatrixDialogue>) Class
					.forName(TeleportMinigame.class.getCanonicalName());
			handledDialogues.put("TeleportMinigame", value44);
			Class<MatrixDialogue> value45 = (Class<MatrixDialogue>) Class
					.forName(TeleportBosses.class.getCanonicalName());
			handledDialogues.put("TeleportBosses", value45);
			Class<MatrixDialogue> value46 = (Class<MatrixDialogue>) Class
					.forName(TeleportTraining.class.getCanonicalName());
			handledDialogues.put("TeleportTraining", value46);
            Class<MatrixDialogue> value51 = (Class<MatrixDialogue>) Class.forName(JadEnter.class.getCanonicalName());
            handledDialogues.put("JadEnter", value51);
            Class<MatrixDialogue> value52 = (Class<MatrixDialogue>) Class.forName(BorkEnter.class.getCanonicalName());
            handledDialogues.put("BorkEnter", value52);
			Class<MatrixDialogue> value48 = (Class<MatrixDialogue>) Class
					.forName(Veliaf.class.getCanonicalName());
			handledDialogues.put("Veliaf", value48);
			Class<MatrixDialogue> value49 = (Class<MatrixDialogue>) Class
					.forName(Max.class.getCanonicalName());
			handledDialogues.put("Max", value49);
			Class<MatrixDialogue> value54 = (Class<MatrixDialogue>) Class
					.forName(RoyalGuard.class.getCanonicalName());
			handledDialogues.put("RoyalGuard", value54);
			
			Class<MatrixDialogue> value55 = (Class<MatrixDialogue>) Class
					.forName(XPRate.class.getCanonicalName());
			handledDialogues.put("XPRate", value55);
			
			Class<MatrixDialogue> value654 = (Class<MatrixDialogue>) Class
					.forName(TaskD.class.getCanonicalName());
			handledDialogues.put("TaskD", value654);
			

			Class<MatrixDialogue> value354 = (Class<MatrixDialogue>) Class
					.forName(TeleportCrystalD.class.getCanonicalName());
			handledDialogues.put("TeleportCrystalD", value354);
			
			
                        Class<MatrixDialogue> value50 = (Class<MatrixDialogue>) Class
					.forName(GrimReaper.class.getCanonicalName());
			handledDialogues.put("GrimReaper", value50);
			   Class<MatrixDialogue> value53 = (Class<MatrixDialogue>) Class
						.forName(Vote.class.getCanonicalName());
				handledDialogues.put("Vote", value53);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static final void reload() {
		handledDialogues.clear();
		init();
	}

	public static final MatrixDialogue getDialogue(Object key) {
		if (key instanceof MatrixDialogue)
			return (MatrixDialogue) key;
		Class<MatrixDialogue> classD = handledDialogues.get(key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	private MatrixDialogueHandler() {

	}
}
