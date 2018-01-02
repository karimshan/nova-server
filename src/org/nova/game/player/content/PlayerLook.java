package org.nova.game.player.content;

import org.nova.cache.loaders.ClientScriptMap;
import org.nova.cache.loaders.GeneralRequirementMap;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
/**
 * @author Dragonkk
 * @author Fuzen Seth
 *
 */
public final class PlayerLook {
	/**
	 * 
	 * @author Fuzen Seth
	 *
	 */

	@SuppressWarnings("unused")
	private InterfaceState state = InterfaceState.MAIN;
	private CustomizeCategory customIndex = CustomizeCategory.SKIN;
	private int designIndex = -1;
	private int secondaryDesignIndex = -1;

	public static enum CustomizeCategory {
		SKIN,
		HAIR,
		TORSO,
		LEGS,
		SHOES,
		FACIAL_HAIR,
		ARMS,
		BRACELETS;
		
		public static CustomizeCategory getCustomIndex(int index) {
			switch (index) {
			case 0:
				return CustomizeCategory.HAIR;
			case 1:
				return CustomizeCategory.FACIAL_HAIR;
			case 2:
				return CustomizeCategory.TORSO;
			case 3:
				return CustomizeCategory.ARMS;
			case 4:
				return CustomizeCategory.BRACELETS;
			case 5:
				return CustomizeCategory.LEGS;
			case 6:
				return CustomizeCategory.SHOES;
			
			}
			return null;
		}
	}

	public static enum InterfaceState {
		MAIN,
		CUSTOMIZATION;
	}
	

    @SuppressWarnings("deprecation")
	public static void openCharacterCustomizing(Player player) {
/**	player.getInterfaceManager().sendScreenInterface(303, 1028);
	player.getPackets().sendUnlockIComponentOptionSlots(1028, 65, 0, 11, 0);
	player.getPackets().sendUnlockIComponentOptionSlots(1028, 128, 0, 50, 0);
	player.getPackets().sendUnlockIComponentOptionSlots(1028, 132, 0, 250, 0);
	player.getPackets().sendGlobalConfig(543, player.getAppearence().isMale() ? 0 : 1);
 TODO */
    }
    public static void handleCharacterCustomizingButtons(Player player, int buttonId, int slotId) {
    	if (buttonId == 117) { // confirm
    	   player.closeInterfaces();
    	    player.getTemporaryAttributtes().remove("SelectWearDesignD");
    	    player.getTemporaryAttributtes().remove("ViewWearDesign");
    	    player.getTemporaryAttributtes().remove("ViewWearDesignD");
    	    player.getAppearance().generateAppearanceData();
    	} else if (buttonId >= 68 && buttonId <= 74) {
    	    player.getTemporaryAttributtes().put("ViewWearDesign", (buttonId - 68));
    	    player.getTemporaryAttributtes().put("ViewWearDesignD", 0);
    	    setDesign(player, buttonId - 68, 0);
    	} else if (buttonId >= 103 && buttonId <= 105) {
    	    Integer index = (Integer) player.getTemporaryAttributtes().get("ViewWearDesign");
    	    if (index == null)
    		return;
    	    player.getTemporaryAttributtes().put("ViewWearDesignD", (buttonId - 103));
    	    setDesign(player, index, buttonId - 103);
    	} else if (buttonId == 62 || buttonId == 63) {
    	    setGender(player, buttonId == 62);
    	} else if (buttonId == 65) {
    	    setSkin(player, slotId);
    	} else if (buttonId >= 116 && buttonId <= 121) {
    	    player.getTemporaryAttributtes().put("SelectWearDesignD", (buttonId - 116));
    	} else if (buttonId == 128) {
    	    Integer index = (Integer) player.getTemporaryAttributtes().get("SelectWearDesignD");
    	    if (index == null || index == 1) {
    		boolean male = player.getAppearance().isMale();
    		int map1 = ClientScriptMap.getMap(male ? 3304 : 3302).getIntValue(slotId);
    		if (map1 == 0)
    		    return;
    		GeneralRequirementMap map = GeneralRequirementMap.getMap(map1);
    		player.getAppearance().setHairStyle(map.getIntValue(788));
    		if (!male)
    		    player.getAppearance().setBeardStyle(player.getAppearance().getHairStyle());
    	    } else if (index == 2) {
    		player.getAppearance().setTopStyle(ClientScriptMap.getMap(player.getAppearance().isMale() ? 3287 : 1591).getIntValue(slotId));
    		player.getAppearance().setArmsStyle(player.getAppearance().isMale() ? 26 : 65); // default
    		player.getAppearance().setWristsStyle(player.getAppearance().isMale() ? 34 : 68); // default
    		player.getAppearance().generateAppearanceData();
    	    } else if (index == 3)
    		player.getAppearance().setLegsStyle(ClientScriptMap.getMap(player.getAppearance().isMale() ? 3289 : 1607).getIntValue(slotId));
    	    else if (index == 4)
    		player.getAppearance().setBootsStyle(ClientScriptMap.getMap(player.getAppearance().isMale() ? 1136 : 1137).getIntValue(slotId));
    	    else if (player.getAppearance().isMale())
    		player.getAppearance().setBeardStyle(ClientScriptMap.getMap(3307).getIntValue(slotId));
    	} else if (buttonId == 132) {
    	    Integer index = (Integer) player.getTemporaryAttributtes().get("SelectWearDesignD");
    	    if (index == null || index == 0)
    		setSkin(player, slotId);
    	    else {
    		if (index == 1 || index == 5)
    		    player.getAppearance().setHairColor(ClientScriptMap.getMap(2345).getIntValue(slotId));
    		else if (index == 2)
    		    player.getAppearance().setTopColor(ClientScriptMap.getMap(3283).getIntValue(slotId));
    		else if (index == 3)
    		    player.getAppearance().setLegsColor(ClientScriptMap.getMap(3283).getIntValue(slotId));
    		else
    		    player.getAppearance().setBootsColor(ClientScriptMap.getMap(3297).getIntValue(slotId));
    	    }
    	}
        }
    public static void setGender(Player player, boolean male) {
	if (male == player.getAppearance().isMale())
	    return;
	if (!male)
	    player.getAppearance().female();
	else
	    player.getAppearance().male();
	Integer index1 = (Integer) player.getTemporaryAttributtes().get("ViewWearDesign");
	Integer index2 = (Integer) player.getTemporaryAttributtes().get("ViewWearDesignD");
	setDesign(player, index1 != null ? index1 : 0, index2 != null ? index2 : 0);
	player.getAppearance().generateAppearanceData();
	 }

	public static void setDesign(Player player, int index1, int index2) {
    	int map1 = ClientScriptMap.getMap(3278).getIntValue(index1);
    	if (map1 == 0)
    	    return;
    	boolean male = player.getAppearance().isMale();
    	int map2Id = GeneralRequirementMap.getMap(map1).getIntValue((male ? 1169 : 1175) + index2);
    	if (map2Id == 0)
    	    return;
    	GeneralRequirementMap map = GeneralRequirementMap.getMap(map2Id);
    	for (int i = 1182; i <= 1186; i++) {
    	    int value = map.getIntValue(i);
    	    if (value == -1)
    		continue;
    	    player.getAppearance().setLook(i - 1180, value);
    	}
    	for (int i = 1187; i <= 1190; i++) {
    	    int value = map.getIntValue(i);
    	    if (value == -1)
    		continue;
    	    player.getAppearance().setColor(i - 1186, value);
    	}
    	if (!player.getAppearance().isMale())
    	    player.getAppearance().setBeardStyle(player.getAppearance().getHairStyle());

        }

	public static void setSkin(Player player, int index) {
    	player.getAppearance().setSkinColor(ClientScriptMap.getMap(748).getIntValue(index));
        }

	public static void handleMageMakeOverButtons(Player player, int buttonId) {
		if (buttonId == 14 || buttonId == 16 || buttonId == 15 ||buttonId == 17)
			player.getTemporaryAttributtes().put("MageMakeOverGender",
					buttonId == 14 || buttonId == 16);
		if (buttonId == 11) {
			player.packets().sendHideIComponent(309, 11, true);
		
		}
		else if (buttonId >= 20 && buttonId <= 31) {

			int skin;
			if (buttonId == 31)
				skin = 11;
			else if (buttonId == 30)
				skin = 10;
			else if (buttonId == 20)
				skin = 9;
			else if (buttonId == 21)
				skin = 8;
			else if (buttonId == 22)
				skin = 7;
			else if (buttonId == 29)
				skin = 6;
			else if (buttonId == 28)
				skin = 5;
			else if (buttonId == 27)
				skin = 4;
			else if (buttonId == 26)
				skin = 3;
			else if (buttonId == 25)
				skin = 2;
			else if (buttonId == 24)
				skin = 1;
			else
				skin = 0;
			player.getTemporaryAttributtes().put("MageMakeOverSkin", skin);
		} else if (buttonId == 33) {
			Boolean male = (Boolean) player.getTemporaryAttributtes().remove(
					"MageMakeOverGender");
			Integer skin = (Integer) player.getTemporaryAttributtes().remove(
					"MageMakeOverSkin");
			player.closeInterfaces();
			PlayerLook.openHairdresserSalon(player);
			if (male == null || skin == null)
				return;
				player.getAppearance().setSkinColor(skin);
				player.getAppearance().generateAppearanceData();
			}
		
	}

	public static void handleHairdresserSalonButtons(Player player, int buttonId,
			int slotId) {// Hair and color match button count so just loop and
							// do ++, but cant find button ids
		if (buttonId == 6)
			player.getTemporaryAttributtes().put("hairSaloon", true);
		else if (buttonId == 7)
			player.getTemporaryAttributtes().put("hairSaloon", false);
		else if (buttonId == 18) {
				openThessaliasMakeOver(player);
				
		} else if (buttonId == 10) {
			Boolean hairSalon =  (Boolean) player.getTemporaryAttributtes().get("hairSaloon");
			if(hairSalon != null && hairSalon) 
				player.getAppearance().setHairStyle((int) ClientScriptMap.getMap(player.getAppearance().isMale() ? 2339 : 2342).getKeyForValue(slotId/2));
			else if (player.getAppearance().isMale())
				player.getAppearance().setBeardStyle(ClientScriptMap.getMap(703).getIntValue(slotId/2));
		} else if (buttonId == 16) 
			player.getAppearance().setHairColor(ClientScriptMap.getMap(2345).getIntValue(slotId/2));
	}
	
	public static void openMageMakeOver(Player player) {
		player.interfaces().sendInterface(900);
		player.packets().sendIComponentText(900, 33, "Confirm");
		player.packets().sendConfigByFile(6098,
				player.getAppearance().isMale() ? 0 : 1);
		player.packets().sendConfigByFile(6099,
				player.getAppearance().getSkinColor());
		player.getTemporaryAttributtes().put("MageMakeOverGender",
				player.getAppearance().isMale());
		player.getTemporaryAttributtes().put("MageMakeOverSkin",
				player.getAppearance().getSkinColor());
	}

	
	public static void handleThessaliasMakeOverButtons(Player player, int buttonId, int slotId) {
		if (buttonId == 6)
			player.getTemporaryAttributtes().put("ThessaliasMakeOver", 0);
		else if (buttonId == 7) {
			if(ClientScriptMap.getMap(player.getAppearance().isMale() ? 690 : 1591).getKeyForValue(player.getAppearance().getTopStyle()) >= 32) {
				player.getTemporaryAttributtes().put("ThessaliasMakeOver", 1);
			}else
				player.packets().sendMessage("You can't select different arms to go with that top.");
		}else if (buttonId == 8) {
			if(ClientScriptMap.getMap(player.getAppearance().isMale() ? 690 : 1591).getKeyForValue(player.getAppearance().getTopStyle()) >= 32) {
				player.getTemporaryAttributtes().put("ThessaliasMakeOver", 2);
			}else
				player.packets().sendMessage("You can't select different wrists to go with that top.");
		}else if (buttonId == 9)
			player.getTemporaryAttributtes().put("ThessaliasMakeOver", 3);
		
		 else if (buttonId == 12) { //set part
			Integer stage = (Integer) player.getTemporaryAttributtes().get("ThessaliasMakeOver");
			if(stage == null || stage == 0) {
				player.getAppearance().setTopStyle((int) ClientScriptMap.getMap(player.getAppearance().isMale() ? 690 : 1591).getIntValue(slotId/2));
				if(!player.getAppearance().isMale())
					player.getAppearance().setBeardStyle(player.getAppearance().getTopStyle());
				player.getAppearance().setArmsStyle(player.getAppearance().isMale() ? 26 : 65); //default
				player.getAppearance().setWristsStyle(player.getAppearance().isMale() ? 34 : 68); //default
			}else if (stage == 1) //arms
				player.getAppearance().setArmsStyle((int) ClientScriptMap.getMap(player.getAppearance().isMale() ? 711 : 693).getIntValue(slotId/2));
			else if (stage == 2) //wrists
				player.getAppearance().setWristsStyle((int) ClientScriptMap.getMap(751).getIntValue(slotId/2));
			else
				player.getAppearance().setLegsStyle((int) ClientScriptMap.getMap(player.getAppearance().isMale() ? 1586 : 1607).getIntValue(slotId/2));
			
		} else if (buttonId == 17) {//color
			Integer stage = (Integer) player.getTemporaryAttributtes().get("ThessaliasMakeOver");
			if(stage == null || stage == 0 || stage == 1) 
				player.getAppearance().setTopColor(ClientScriptMap.getMap(3282).getIntValue(slotId/2));
			else if (stage == 3)
				player.getAppearance().setLegsColor(ClientScriptMap.getMap(3284).getIntValue(slotId/2));
		}
		else if (buttonId == 19) { //confirm
				player.closeInterfaces();
				//player.getDialogueManager().startDialogue("ThessaliaD", 548);
				if(player.getStarterStage() == 0)
					player.getDialogue().start("ChooseGameMode");
			//player.closeInterfaces();
		}
	}
	
	
	public static void openThessaliasMakeOver(final Player player) {
		player.setNextAnimation(new Animation(11623));
		player.interfaces().sendInterface(729);
		player.packets().sendIComponentText(729, 21, "Finish");
		player.getTemporaryAttributtes().put("ThessaliasMakeOver", 0); 
		player.packets().sendUnlockIComponentOptionSlots(729, 12, 0, 100, 0); 
		player.packets().sendUnlockIComponentOptionSlots(729, 17, 0, ClientScriptMap.getMap(3282).getSize()*2, 0); 
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
			//	player.getDialogueManager().startDialogue("SimpleNPCMessage", 548, "A marvellous choise. You look splendid!");
				player.setNextAnimation(new Animation(-1));
				player.getAppearance().getAppeareanceData();
				player.getTemporaryAttributtes().remove("ThessaliasMakeOver");
			}
			
		});
	}
	
	
	public static void openHairdresserSalon(final Player player) {
		if(player.getEquipment().getHatId() != -1) {
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 598, "I'm afraid I can't see your head at the moment. Please remove your headgear first.");
			return;
		}
		if(player.getEquipment().getWeaponId() != -1 || player.getEquipment().getShieldId() != -1) {
			player.getMatrixDialogues().startDialogue("SimpleNPCMessage", 598, "I don't feel comfortable cutting hair when you are wielding something. Please remove what you are holding first.");
			return;
		}
			player.packets().sendHideIComponent(309, 4, true);
		player.packets().sendString("Continue..", 309, 20);
		player.setNextAnimation(new Animation(11623));
		player.interfaces().sendInterface(309);
		player.packets().sendUnlockIComponentOptionSlots(309, 10, 0, ClientScriptMap.getMap(player.getAppearance().isMale() ? 2339 : 2342).getSize()*2, 0);
		player.packets().sendUnlockIComponentOptionSlots(309, 16, 0, ClientScriptMap.getMap(2345).getSize()*2, 0); 
		player.getTemporaryAttributtes().put("hairSaloon", true);

		player.packets().sendIComponentText(309, 3, "Please select a hairstyle for your character.");
		player.setCloseInterfacesEvent(new Runnable() {

			@Override
			public void run() {
				//player.getDialogueManager().startDialogue("SimpleNPCMessage", 598, "An excellent choise, "+(player.getAppearence().isMale() ? "sir" : "lady")+".");
				player.setNextAnimation(new Animation(-1));
				player.getAppearance().getAppeareanceData();
				player.getTemporaryAttributtes().remove("hairSaloon");
			}
			
		});
	}

	private PlayerLook() {

	}

}