package org.nova.game.player.content;

import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * This handles gift boxes and gives a reward carefully.
 *
 */
public class GiftBox {
	public static final int KEY = 989;
	
	public static final int Animation = 881;
	/*
	 * Foods
	 */
	
	
	private transient Player player;
	private static String rewardName;
	
	public static void openGift(Player player) {
		if (player.isDueling()) 
			return;
		long currentTime = Misc.currentTimeMillis();
		if (player.getAttackedByDelay() + 10000 > currentTime) {
			player.packets()
					.sendMessage(
							"You can't open the box until 10 seconds after the end of combat.");
			return;
		}
		player.interfaces().sendInterface(1123);
		player.packets().sendIComponentText(1123, 20, "Select an reward that you want to have, it can be skilling items and even an experience lamp.");
	}
	
	public static void scanReward(Player player) {
		   player.addStopDelay(1);
		if (GiftBox.getRewardName().equals("essences")) {
			player.getInventory().addItem(7937, Misc.getRandom(1300));
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
			   
		}
		 if (GiftBox.getRewardName().equals("bowstring")) {
				
		player.getInventory().addItem(1778, Misc.getRandom(1300));
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
			}
		if(GiftBox.getRewardName().equals("bars")) {
			
			player.getInventory().addItem(2364, Misc.getRandom(5));
			player.getInventory().addItem(2362, Misc.getRandom(30));
			player.getInventory().addItem(2360, Misc.getRandom(50));
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
}
		 if (GiftBox.getRewardName().equals("herbs")) {
				
				player.getInventory().addItem(3052, Misc.getRandom(5));
			player.getInventory().addItem(3050, Misc.getRandom(30));
			player.getInventory().addItem(14837, Misc.getRandom(50));
			
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
}
		 if (GiftBox.getRewardName().equals("ores")) {
				
				player.getInventory().addItem(452, Misc.getRandom(5));
			player.getInventory().addItem(450, Misc.getRandom(30));
			player.getInventory().addItem(448, Misc.getRandom(50));
			
			GiftBox.setRewardName("None");
			player.getInventory().refresh();
}	
		if (GiftBox.getRewardName().equals("seeds")) {
			
			player.getInventory().addItem(5096, Misc.getRandom(5));
			player.getInventory().addItem(5097, Misc.getRandom(30));
			player.getInventory().addItem(5098, Misc.getRandom(50));
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
}
		 if (GiftBox.getRewardName().equals("fishes")) {
			
			player.getInventory().addItem(384, Misc.getRandom(500));
			player.getInventory().addItem(390, Misc.getRandom(300));
			
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
	}
		 if (GiftBox.getRewardName().equals("experience")) {
				
				player.getInventory().addItem(22340,1);
			
			player.getInventory().refresh();
			GiftBox.setRewardName("None");
		}
	}
	
	
	public static void handleButtons(Player player, int componentId) {
		switch (componentId) {
		case 68:
			player.closeInterfaces();
			player.addStopDelay(1);
			break;
		case 82:
		player.closeInterfaces();
		setRewardName("essences");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
		break;
		case 86:
			player.closeInterfaces();
			setRewardName("bowstring");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
			break;
		case 89:
			player.closeInterfaces();
			setRewardName("bars");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
	break;
		case 94:
			player.closeInterfaces();
			setRewardName("herbs");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
	break;
		case 98:
			player.closeInterfaces();
			setRewardName("ores");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
	break;
		case 102:
		player.closeInterfaces();
		setRewardName("seeds");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
	break;
		case 106:
		player.closeInterfaces();
		setRewardName("fishes");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
		break;
		case 110:
		player.closeInterfaces();
		setRewardName("experience");
		player.sm("You have selected reward: "+getRewardName()+".");
		scanReward(player);
	break;
		}
	}

	public static String getRewardName() {
		return rewardName;
	}

	public static void setRewardName(String rewardName) {
		GiftBox.rewardName = rewardName;
	}
	
}
