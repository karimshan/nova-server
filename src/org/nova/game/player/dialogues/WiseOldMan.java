package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.player.content.WorldTeleport;

public class WiseOldMan extends MatrixDialogue {

	private int npcId;

	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_2_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"Good day, "+player.getDisplayName()+" I can take you anywhere", "around the world of Nova."}, IS_NPC, npcId, 9785);
	
	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == -1) {
			stage = 1;
		}

		switch (stage) {
		case 1:
			sendEntityDialogue(SEND_1_TEXT_CHAT,
					new String[] { player.getDisplayName(), "Sounds excellent! where could you take me?" },
					IS_PLAYER, player.getIndex(), 9785);
					stage = 2;
					
			break;
		case 2:
			sendDialogue(SEND_5_OPTIONS, "Select a Option",
					"View training locations.", "View minigame locations.", "View city locations.", "View Slayer locations.",
					"Next page");
			stage = 3;
			break;
		case 3:
			switch (componentId) {
			case 1:
				sendDialogue(SEND_5_OPTIONS, "Training locations",
						"Jatiszo Rock crabs", "Castlewars ogres", "Relleka rock crabs", "Baxtorian falls",  "Next page");
			stage = 6;
				break;
			case 2:
				sendDialogue(SEND_5_OPTIONS, "Minigame locations",
						"Clan wars", "Duel arena", "Castlewars", "Artisan's Workshop", "Nevermind.");
				stage = 4;
				break;
			case 3:
				sendDialogue(SEND_5_OPTIONS, "City locations",
						"Varrock", "Lumbridge", "Neitiznot", "Jatiszo", "Next page");
				break;
			case 4:
				sendDialogue(SEND_5_OPTIONS, "Slayer locations",
						"Fremmennik Slayer dungeon", "Ancient cavern", "Brimhaven Slayer dungeon", "");
				break;
			case 5:
				sendDialogue(SEND_4_OPTIONS, "Select a Option",
						"View skilling locations.", "View membership locations.", "Wilderness locations.", "Nevermind, I don't want to teleport.");
				break;
			}
			break;
		case 4:
			switch (componentId) {
			case 1:
				WorldTeleport.getSingleton().setDestination(player, "clan_wars");
				break;
			case 2:
				WorldTeleport.getSingleton().setDestination(player, "duel_arena");
				break;
			case 3:
				WorldTeleport.getSingleton().setDestination(player, "castlewars");
				break;
			case 4:
				WorldTeleport.getSingleton().setDestination(player, "artisan");
				break;
			case 5:
				player.interfaces().closeChatBoxInterface();
				break;
			}
			break;
		case 5:
			switch (componentId) {
			case 1:
				//SKILLING LOCATIONS
				sendDialogue(SEND_5_OPTIONS, "Slayer locations",
						"Fremmennik Slayer dungeon", "Ancient cavern", "Brimhaven Slayer dungeon", "");
				break;
			case 2:
				//member locations
				sendDialogue(SEND_5_OPTIONS, "Slayer locations",
						"Fremmennik Slayer dungeon", "Ancient cavern", "Brimhaven Slayer dungeon", "");
				break;
			case 3:	
				sendDialogue(SEND_5_OPTIONS, "Mage bank",
					"East dragons", "West dragons", "Revenants cave", "Wildywyrm", "Never mind, I don't want to teleport.");
				break;
			case 4:
				WorldTeleport.getSingleton().setDestination(player, "artisan");
				break;
			case 5:
				player.interfaces().closeChatBoxInterface();
				break;
			}
			break;
			/**
			 * Training locations, page 1
			 */
		case 6:
			switch (componentId) {
			case 1:
				WorldTeleport.getSingleton().setDestination(player, "crabs");
				break;
			case 2:
				WorldTeleport.getSingleton().setDestination(player, "castlewars_ogres");
				break;
			case 3:	
				WorldTeleport.getSingleton().setDestination(player, "relleka_crabs");
				break;
			case 4:
				WorldTeleport.getSingleton().setDestination(player, "baxtorial_falls");
				break;
			case 5:
				sendDialogue(SEND_5_OPTIONS, "Select an option",
						"Bandit camp", "Varrock moss giants & Red spiders", "Living rock cavern", "Kalphite hive", "Next page");
				stage = 7;
				break;
			}
			break;
			/**
			 * Training locations, page 2
			 */
		case 7:
			switch (componentId) {
			case 1:
				WorldTeleport.getSingleton().setDestination(player, "bandit_camp");
				break;
			case 2:
				WorldTeleport.getSingleton().setDestination(player, "moss_giants_varrock");
				break;
			case 3:	
				WorldTeleport.getSingleton().setDestination(player, "rock_caverns");
				break;
			case 4:
				WorldTeleport.getSingleton().setDestination(player, "kalphite_hive");
				break;
			case 5:
				sendDialogue(SEND_5_OPTIONS, "Select an option",
						"Combat training camp ogres", "Monastery monks", "Al-kharid warriors", "Karamja dungeon", "Next page");
				stage = 8;
			}
				break;
				/**
				 * Training locations, page 3
				 */
			case 8:
				switch (componentId) {
				case 1:
					WorldTeleport.getSingleton().setDestination(player, "combatarea_ogres");
					break;
				case 2:
					WorldTeleport.getSingleton().setDestination(player, "monastery_monks");
					break;
				case 3:	
					WorldTeleport.getSingleton().setDestination(player, "kharidwarriors");
					break;
				case 4:
					WorldTeleport.getSingleton().setDestination(player, "karamja_dungeon");
					break;
				case 5:
					sendDialogue(SEND_5_OPTIONS, "Select an option",
							"Chaos tunnels", "Morytania ghouls", "Armoured zombies", "White knight's castle", "Next page");
					stage = 9;
					break;
			}
			break;
			/**
			 * Training teleports, page 4
			 */
			case 9:
				switch (componentId) {
				case 1:
					WorldTeleport.getSingleton().setDestination(player, "chaos_tunnels");
					break;
				case 2:
					WorldTeleport.getSingleton().setDestination(player, "ghouls_morytania");
					break;
				case 3:	
					WorldTeleport.getSingleton().setDestination(player, "zombiearmour");
					break;
				case 4:
					WorldTeleport.getSingleton().setDestination(player, "wknightcastle");
					break;
				case 5:
					sendDialogue(SEND_5_OPTIONS, "Select a Option",
							"Jadinko lair", "Edgeville hill giants", "Troll mountain trolls", "Jogres & Tribesman (Jungle)",
							"Next page");
					stage = 10;
					break;
			}
			break;
			/**
			 * Training teleports, page 5
			 */
			case 10:
				switch (componentId) {
				case 1:
					WorldTeleport.getSingleton().setDestination(player, "jadinko_lair");
					break;
				case 2:
					WorldTeleport.getSingleton().setDestination(player, "edge_hills");
					break;
				case 3:	
					WorldTeleport.getSingleton().setDestination(player, "trollmountain");
					break;
				case 4:
					WorldTeleport.getSingleton().setDestination(player, "jungle");
					break;
				case 5:
					sendDialogue(SEND_5_OPTIONS, "Select a Option",
							"Crandor fire giants", "Ancient cavern", "Ape Atoll monkey guards", "Ape Atoll dungeon",
							"Take me back to the main page.");
					stage = 11;
					break;
			}
			break;
			/**
			 * Training teleports, page 6
			 */
			case 11:
				switch (componentId) {
				case 1:
					WorldTeleport.getSingleton().setDestination(player, "crandorfiregiants");
					break;
				case 2:
					WorldTeleport.getSingleton().setDestination(player, "ancient_cavern");
					break;
				case 3:	
					WorldTeleport.getSingleton().setDestination(player, "monkey_guards");
					break;
				case 4:
					WorldTeleport.getSingleton().setDestination(player, "atolldungeon");
					break;
				case 5:
					sendDialogue(SEND_5_OPTIONS, "Select a Option",
							"View training locations.", "View minigame locations.", "View city locations.", "View Slayer locations.",
							"Next page");
					stage = 3;
					break;
			}
			break;
		}
	}

	@Override
	public void finish() {

	}
}