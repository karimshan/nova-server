package org.nova.game.player.dialogues;

import org.nova.game.map.Location;
import org.nova.game.masks.Graphics;
public class XPBook extends MatrixDialogue {

	public XPBook() {
	}

	@Override
	public void start() {
		stage = 1;
		sendDialogue(SEND_5_OPTIONS, "Select a Option", "Attack experience",
				"Strength Experience", "Defence Experience",
				"Ranged Experience", "Magic Experience");

	}

	@Override
	public void run(int interfaceId, int componentId) {
		if (stage == 1) {
			if (componentId == 1) {
				player.getSkills().addXp(0, 450);
				player.sm("You have choosed attack experience.");
				player.interfaces().closeChatBoxInterface();
				player.getInventory().deleteItem(22340, 1);
				player.getInventory().refresh();
			} else if (componentId == 2) {
				player.getSkills().addXp(2, 450);
				player.sm("You have choosed strength experience.");
				player.interfaces().closeChatBoxInterface();
				player.getInventory().deleteItem(22340, 1);
				player.getInventory().refresh();
			} else if (componentId == 3) {
				player.getSkills().addXp(1, 450);
				player.sm("You have choosed defence experience.");
				player.interfaces().closeChatBoxInterface();
				player.getInventory().deleteItem(22340, 1);
				player.getInventory().refresh();

			} else if (componentId == 4) {
				player.getSkills().addXp(4, 450);
				player.sm("You have choosed ranged experience.");
				player.interfaces().closeChatBoxInterface();
				player.getInventory().deleteItem(22340, 1);
				player.getInventory().refresh();

			} else if (componentId == 5) {
				player.getSkills().addXp(6, 450);
				player.getInventory().deleteItem(22340, 1);
				player.getInventory().refresh();
				player.sm("You have choosed magic experience.");
				player.interfaces().closeChatBoxInterface();
			}
		}

	}

	private void teleportPlayer(int x, int y, int z) {
		player.setNextGraphics(new Graphics(111));
		player.setLocation(new Location(x, y, z));
		player.stopAll();

	}

	@Override
	public void finish() {
	}

}
