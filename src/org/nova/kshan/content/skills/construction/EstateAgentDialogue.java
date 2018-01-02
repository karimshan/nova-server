package org.nova.kshan.content.skills.construction;

import org.nova.kshan.dialogues.Dialogue;

/**
 * Handles the estate agent dialogue for purchasing a house. Different than the estate agent random event.
 * 
 * @author K-Shan
 *
 */
public class EstateAgentDialogue extends Dialogue {
	
	int npcId;

	@Override
	public void start() {
		npcId = (int) data[0];
		npc(npcId, "Hello. Welcome to the Nova Housing Agency!", "What can I do for you?");
	}

	@Override
	public void process(int interfaceId, int b) {
		if(!player.getHouse().hasPurchasedHouse()) {
			if(stage == 0) {
				sendOptions(TITLE, "How can I get a house?", "Tell me about houses");
				stage = 1;
			} else if(stage == 1) {
				if(b == 1) {
					player("How can I get a house?");
					stage = 2;
				} else {
					player("Tell me about houses!");
					stage = 8;
				}
			} else if(stage == 2) {
				npc(npcId, "I can sell you a starting house in Rimmington for", 
					"1000 coins. As you increase your construction skill you", 
					"will be able to have your house moved to other areas", "and redecorated in other styles.");
				stage = 3;
			} else if(stage == 3) {
				npc(npcId, "Do you want to buy a starter house?");
				stage = 4;
			} else if(stage == 4) {
				sendOptions(TITLE, "Yes please!", "No thanks");
				stage = 5;
			} else if(stage == 5) {
				if(b == 1) {
					player("Yes please!");
					stage = 6;
				} else {
					end();
					npc(npcId, "Let me know if I can be of any more help.");
				}
			} else if(stage == 6) {
				if(player.getInventory().containsItem(995, 1000)) {
					npc(npcId, "Thank you. Go through the Rimmington house portal",
						"and you will find your house ready for you to start", "building in it.");
					stage = 7;
				} else {
					end();
					npc(npcId, "You need 1000 coins to purchase a house.");
				}
			} else if(stage == 7) {
				player.getInventory().deleteItem(995, 1000);
				player.getHouse().setPurchasedHouse(true);
				end();
				npc(npcId, "This book will help you to start building your house.");
				player.getInventory().addItem(8463, 1);
			} else if(stage == 8) {
				npc(npcId, "It all came out of the wizards' experiments. They found a", 
					"way to fold space, so that they could pack many acres of", 
					"land into an area only a foot across.");
				stage = 9;
			} else if(stage == 9) {
				npc(npcId, "They created several folded-space regions across", 
					"Nova. Each one contains hundereds of small plots", "where people can build houses.");
				stage = 10;
			} else if(stage == 10) {
				player("Ah, so that's how everyone can have a house without", 
					"them cluttering up the world!");
				stage = 11;
			} else if(stage == 11) {
				npc(npcId, "Quite. The wizards didn't want to get bogged down in the", 
					"business side of things so they hired me to sell the houses.");
				stage = 12;
			} else if(stage == 12) {
				end();
				npc(npcId, 
					"There are various other people across Nova who can",
					"help you furnish your house. You should start by buying",
					"planks from the sawmill operator in Varrock.");
			}
		} else {
			if(stage == 0) {
				sendOptions(TITLE, "Can you move my house please?", "Can you redecorate my house please?", 
					"Could I have a Construction guidebook?", "Tell me about houses", "Can you tell me about the Construction skillcape?");
				stage = 1;
			} else if(stage == 1) {
				if(b == OPTION1) {
					player("Can you move my house please?");
					stage = 2;
				} else if(b == OPTION2) {
					player("Can you redecorate my house please?");
					stage = 7;
				} else if(b == OPTION3) {
					player("Could I have a Construction guidebook?");
					stage = 9;
				} else if(b == OPTION4) {
					player("Tell me about houses!");
					stage = 10;
				} else {
					player("Can you tell me about the Construction skillcape?");
					stage = 15;
				}
			} else if(stage == 2) {
				npc(npcId, "Certainly. Where would you like it moved to?");
				stage = 3;
			} else if(stage == 3) {
				sendOptions(TITLE, "Rimmington (5,000)", "Taverly (5,000)", "Pollnivneach (7,500)",
					"Rellekka (10,000)", "More...");
				stage = 4;
			} else if(stage == 4) {
				if(b == OPTION1) {
					player.getTemporaryAttributtes().put("houseMoving", "Rimmington");
					player.getTemporaryAttributtes().put("houseMovingCost", 5000);
					player("To Rimmington please!");
					stage = 5;
				} else if(b == OPTION2) {
					player.getTemporaryAttributtes().put("houseMoving", "Taverly");
					player.getTemporaryAttributtes().put("houseMovingCost", 5000);
					player("To Taverly please!");
					stage = 5;
				} else if(b == OPTION3) {
					player.getTemporaryAttributtes().put("houseMoving", "Pollniveach");
					player.getTemporaryAttributtes().put("houseMovingCost", 7500);
					player("To Pollniveach please!");
					stage = 5;
				} else if(b == OPTION4) {
					player.getTemporaryAttributtes().put("houseMoving", "Rellekka");
					player.getTemporaryAttributtes().put("houseMovingCost", 10000);
					player("To Rellekka please!");
					stage = 5;
				} else {
					sendOptions(TITLE, "Brimhaven (15,000)", "Yanille (25,000)", "Previous...");
					stage = 6;
				}
			} else if(stage == 5) {
				end();
				String location = (String) player.getTemporaryAttributtes().get("houseMoving");
				int cost = (int) player.getTemporaryAttributtes().get("houseMovingCost");
				if(player.getHouse().getHouseLocationName().equals(location)) {
					npc(npcId, "Your house is already there!");
					return;
				}
				if(!player.getInventory().containsItem(995, cost)) {
					npc(npcId, "Hmph. Come back when you have "+cost+" coins.");
					return;
				}
				player.getInventory().deleteItem(995, cost);
				player.getHouse().setHouseLocationName(location);
				npc(npcId, "Your house has been moved to "+player.getHouse().getHouseLocationName()+".");
				player.getTemporaryAttributtes().remove("houseMoving");
				player.getTemporaryAttributtes().remove("houseMovingCost");
			} else if(stage == 6) {
				if(b == OPTION1_OTHER) {
					player.getTemporaryAttributtes().put("houseMoving", "Brimhaven");
					player.getTemporaryAttributtes().put("houseMovingCost", 15000);
					player("To Brimhaven please!");
					stage = 5;
				} else if(b == OPTION2_OTHER) {
					player.getTemporaryAttributtes().put("houseMoving", "Yanille");
					player.getTemporaryAttributtes().put("houseMovingCost", 25000);
					player("To Yanille please!");
					stage = 5;
				} else {
					sendOptions(TITLE, "Rimmington (5,000)", "Taverly (5,000)", "Pollnivneach (7,500)",
						"Rellekka (10,000)", "More...");
					stage = 4;
				}
			} else if(stage == 7) {
				npc(npcId, "Certainly. My magic can rebuild the house in a completely", 
					"new style! What style would you like?");
				stage = 8;
			} else if(stage == 8) {
				sendOptions(TITLE, "Basic wood (5,000)", "Basic stone (5,000)", "Whitewashed stone (7,500)",
					"Fremennik-style wood (10,000)", "More...");
				stage = 17;
			} else if(stage == 17) {
				if(b == OPTION1) {
					player.getTemporaryAttributtes().put("houseStyle", "Basic wood");
					player.getTemporaryAttributtes().put("houseStylingCost", 5000);
					player("Basic wood please!");
					stage = 18;
				} else if(b == OPTION2) {
					player.getTemporaryAttributtes().put("houseStyle", "Basic stone");
					player.getTemporaryAttributtes().put("houseStylingCost", 5000);
					player("Basic stone please!");
					stage = 18;
				} else if(b == OPTION3) {
					player.getTemporaryAttributtes().put("houseStyle", "Whitewashed stone");
					player.getTemporaryAttributtes().put("houseStylingCost", 7500);
					player("Whitewashed stone please!");
					stage = 18;
				} else if(b == OPTION4) {
					player.getTemporaryAttributtes().put("houseStyle", "Fremennik-style wood");
					player.getTemporaryAttributtes().put("houseStylingCost", 10000);
					player("Fremennik-style wood please!");
					stage = 18;
				} else {
					sendOptions(TITLE, "Tropical wood (15,000)", "Fancy stone (25,000)",
						"Zenevivia's dark stone (Free)", "Previous...");
					stage = 19;
				}
			} else if(stage == 18) {
				end();
				String style = (String) player.getTemporaryAttributtes().get("houseStyle");
				int cost = (int) player.getTemporaryAttributtes().get("houseStylingCost");
				if(player.getHouse().getStyleName().equals(style)) {
					npc(npcId, "Your house is already in that style!");
					return;
				}
				if(!player.getInventory().containsItem(995, cost)) {
					npc(npcId, "Hmph. Come back when you have "+cost+" coins.");
					return;
				}
				player.getHouse().setUsingAlternativeStyle(false);
				switch(style) {
					case "Basic wood":
						player.getHouse().setStyle(0);
						break;
					case "Basic stone":
						player.getHouse().setStyle(1);
						break;
					case "Whitewashed stone":
						player.getHouse().setStyle(2);
						break;
					case "Fremennik-style wood":
						player.getHouse().setStyle(3);
						break;
					case "Tropical wood":
						player.getHouse().setStyle(0);
						player.getHouse().setUsingAlternativeStyle(true);
						break;
					case "Fancy stone":
						player.getHouse().setStyle(1);
						player.getHouse().setUsingAlternativeStyle(true);
						break;
					case "Zenevivia's dark stone":
						player.getHouse().setStyle(2);
						player.getHouse().setUsingAlternativeStyle(true);
						break;
				}
				player.getInventory().deleteItem(995, cost);
				npc(npcId, "Your house has been redecorated.");
				player.getTemporaryAttributtes().remove("houseStyle");
				player.getTemporaryAttributtes().remove("houseStylingCost");
			} else if(stage == 19) {
				if(b == OPTION1) {
					player.getTemporaryAttributtes().put("houseStyle", "Tropical wood");
					player.getTemporaryAttributtes().put("houseStylingCost", 15000);
					player("Tropical wood please!");
					stage = 18;
				} else if(b == OPTION2) {
					player.getTemporaryAttributtes().put("houseStyle", "Fancy stone");
					player.getTemporaryAttributtes().put("houseStylingCost", 25000);
					player("Fancy stone please!");
					stage = 18;
				} else if(b == OPTION3) {
					player.getTemporaryAttributtes().put("houseStyle", "Zenevivia's dark stone");
					player.getTemporaryAttributtes().put("houseStylingCost", 0);
					player("Zenevivia's dark stone please!");
					stage = 18;
				} else {
					sendOptions(TITLE, "Basic wood (5,000)", "Basic stone (5,000)", "Whitewashed stone (7,500)",
						"Fremennik-style wood (10,000)", "More...");
					stage = 17;
				}
			}
			else if(stage == 9) {
				end();
				if(player.getInventory().containsItem(8463, 1))
					npc(npcId, "You've already got one!");
				else {
					npc(npcId, "Certainly.");
					player.getInventory().addItem(8463, 1);
				}
			} else if(stage == 10) {
				npc(npcId, "It all came out of the wizards' experiments. They found a", 
					"way to fold space, so that they could pack many acres of", 
					"land into an area only a foot across.");
				stage = 11;
			} else if(stage == 11) {
				npc(npcId, "They created several folded-space regions across", 
					"Nova. Each one contains hundereds of small plots", "where people can build houses.");
				stage = 12;
			} else if(stage == 12) {
				player("Ah, so that's how everyone can have a house without", 
						"them cluttering up the world!");
				stage = 13;
			} else if(stage == 13) {
				npc(npcId, "Quite. The wizards didn't want to get bogged down in the", 
					"business side of things so they hired me to sell the houses.");
				stage = 14;
			} else if(stage == 14) {
				end();
				npc(npcId, 
					"There are various other people across Nova who can",
					"help you furnish your house. You should start by buying",
					"planks from the sawmill operator in Varrock.");
			} else if(stage == 15) {
				npc(npcId, "As you may know, skillcapes are only available to masters", 
					"in a skill. I have spent my entire life building houses and", 
					"now I spend my time selling them! As a sign of my abilities", 
					"I wear this Skillcape of Construction. If you ever have");
				stage = 16;
			} else if(stage == 16) {
				end();
				npc(npcId, "enough skill to build a demonic throne, come and talk to", 
					"me and I'll sell you a skillcape like mine.");
			}
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		
	}

}
