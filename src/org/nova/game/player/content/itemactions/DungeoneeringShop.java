package org.nova.game.player.content.itemactions;

import java.util.HashMap;
import java.util.Map;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;

public class DungeoneeringShop {
	
	public enum DungeonReward {
		BONECRUSHER(18337, 0, 21, 34000),
		HERBICIDE(19675, 5, 21, 34000),
		SCROLL_OF_LIFE(18336, 15, 25, 10000),
		SCROLL_OF_CLEANSING(19890, 40, 49, 20000),
		SCROLL_OF_EFFICIENCY(19670, 105, 55, 20000),
		SCROLL_OF_AUGURY(18344, 150, 77, 153000),
		SCROLL_OF_RIGOUR(18839, 145, 74, 140000),
		SCROLL_OF_RENEWAL(18343, 125, 65, 107000),
		MERCENARY_GLOVES(18347, 140, 73, 48500),
		TOME_OF_FROST(18346, 80, 48, 43000),
		ARCANE_PULSE_NECKLACE(18333, 20, 30, 6500),
		GRAVITE_SHORTBOW(18373, 70, 45, 40000),
		GRAVITE_LONGSWORD(18367, 55, 45, 40000),
		GRAVITE_RAPIER(18365, 50, 45, 40000),
		GRAVITE_STAFF(18371, 65, 45, 40000),
		GRAVITE_2H(18369, 60, 45, 40000),
		ARCANE_BLAST_NECKLACE(18334, 90, 50, 15500),
		RING_OF_VIGOUR(19669, 120, 62, 50000),
		ARCANE_STREAM_NECKLACE(18335, 130, 70, 30500),
		CHAOTIC_RAPIER(18349, 155, 80, 200000),
		CHAOTIC_LONGSWORD(18351, 160, 80, 200000),
		CHAOTIC_MAUL(18353, 165, 80, 200000),
		CHAOTIC_STAFF(18355, 170, 80, 200000),
		CHAOTIC_CROSSBOW(18357, 175, 80, 200000),
		CHAOTIC_KITESHIELD(18359, 180, 80, 200000),
		EAGLE_EYE_KITESHIELD(18361, 185, 80, 200000),
		FARSEER_KITESHIELD(18363, 190, 80, 200000),
		SNEAKERPEEPER(19894, 195, 80, 85000),
		TWISTEDNECKLACE(19886, 25, 30, 8500),
		DRAGONTOOTHNECKLACE(19887, 115, 60, 17000),
		DEMONHORNNECKLACE(19888, 200, 90, 35000);

		private static Map<Integer, DungeonReward> monsters = new HashMap<Integer, DungeonReward>();

		public static DungeonReward forId(int id) {
			return monsters.get(id);
		}

		static {
			for (DungeonReward monster : DungeonReward.values())
				monsters.put(monster.slotId, monster);
		}

		private int id;
		private int req;
		private int cost;
		private int slotId;
		private String name;

		private DungeonReward(int id, int slotId, int req, int cost) {
			this.id = id;
			this.req = req;
			this.cost = cost;
			this.slotId = slotId;
			this.name = ItemDefinition.get(id).getName();
		}

		public int getId() {
			return id;
		}
		
		public String getName() {
			return name;
		}
		
		public int getCost() {
			return cost;
		}
		
		public int getSlotId() {
			return slotId;
		}

		public int getRequirement() {
			return req;
		}
	}
	
	public static void openRewardsShop(Player player) {
		player.interfaces().sendInterface(940);
		player.packets().sendIComponentSettings(940, 2, 0, 205, 1278);
		refresh(player);
	}
	
	public static void refresh(Player player) {
		player.packets().sendIComponentText(940, 31, ""+player.getDungeoneeringPoints());
	}
	
	public static void handleButtons(Player player, int componentId, int slotId, int packetId) {
		if (componentId == 64 && packetId == 14) {
			if (player.getTemporaryAttributtes().get("dungReward") != null) {
				DungeonReward reward = (DungeonReward) player.getTemporaryAttributtes().get("dungReward");
				if (reward != null) {
					if (player.getSkills().getLevelFromXP(Skills.DUNGEONEERING) < reward.getRequirement()) {
						player.packets().sendMessage("You need "+reward.getRequirement()+" dungeoneering to buy this reward.");
						return;
					}
					if (player.getDungeoneeringPoints() < reward.getCost()) {
						player.packets().sendMessage("You need "+reward.getCost()+" dungeoneering tokens to buy this reward.");
						return;
					}
					player.getMatrixDialogues().startDialogue("DungRewardConfirm", reward);
				} else {
					player.packets().sendMessage("You must choose a reward before trying to buy something.");
				}
			}
			return;
		}
		if (componentId == 2) {
			DungeonReward reward = DungeonReward.forId(slotId);
			if (reward == null) {
				player.packets().sendMessage("That reward is not added yet. "+slotId);
				return;
			} else {
				player.getTemporaryAttributtes().put("dungReward", reward);
				player.packets().sendMessage(reward.getName()+" requires "+ reward.getRequirement() +" dungeoneering and costs "+reward.getCost()+" dungeoneering tokens.");
			}
		}
	}

}