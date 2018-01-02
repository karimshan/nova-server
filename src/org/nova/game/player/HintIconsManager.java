package org.nova.game.player;

import org.nova.game.entity.Entity;

public class HintIconsManager {

	private Player player;
	private HintIcon[] loadedIcons;

	public HintIconsManager(Player p) {
		this.player = p;
		loadedIcons = new HintIcon[7];
	}

	public int addHintIcon(Entity target, int arrowType, boolean followOnFeet, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(target.getIndex(), target instanceof Player ? 10 : 1, arrowType, followOnFeet, index);
			player.packets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}

	public int addHintIcon(int coordX, int coordY, int distanceShown, int distanceFromFloor, int direction, int arrowType, boolean followOnFeet, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			if (direction < 2 || direction > 6)
				direction = 2;
			HintIcon icon = new HintIcon(coordX, coordY, distanceShown, distanceFromFloor, direction, arrowType, followOnFeet, index);
			player.packets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}
	
	public int sendHint(int coordX, int coordY, int height, int direction) {
		int index = 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(coordX, coordY, 25, height, direction, 0, false, index);
			player.packets().sendHintIcon(icon);
		}
		return index;
	}
	
	public int sendHint(int coordX, int coordY, int height, int distanceShown, int direction) {
		int index = 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(coordX, coordY, distanceShown, height, direction, 0, false, index);
			player.packets().sendHintIcon(icon);
		}
		return index;
	}
	
	public int doorHint(int coordX, int coordY, byte direction) {
		int index = 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(coordX, coordY, 50, HintIcon.DOOR_HEIGHT, direction, 0, false, index);
			player.packets().sendHintIcon(icon);
		}
		return index;
	}

	public int addHintIcon(boolean modelId, boolean saveIcon) {
		int index = saveIcon ? getFreeIndex() : 7;
		if (index != -1) {
			HintIcon icon = new HintIcon(8, modelId, index);
			player.packets().sendHintIcon(icon);
			if (saveIcon)
				loadedIcons[index] = icon;
		}
		return index;
	}

	public void removeUnsavedHintIcon() {
		//player.packets().clearHintIcon(new HintIcon());
	}

	public boolean reloadHintIcon(int index) {
		if (index >= loadedIcons.length)
			return false;
		if (loadedIcons[index] == null)
			return false;
		player.packets().sendHintIcon(loadedIcons[index]);
		return true;
	}

	public boolean removeHintIcon(int index) {
		if (index == 7) {
			removeUnsavedHintIcon();
			return true;
		}
		if (index >= loadedIcons.length)
			return false;
		if (loadedIcons[index] == null)
			return false;
		loadedIcons[index].setTargetType(0);
		//player.packets().clearHintIcon(loadedIcons[index]);
		loadedIcons[index] = null;
		return true;
	}

	public void removeAll() {
		for (int index = 0; index < loadedIcons.length; index++) {
			if (loadedIcons[index] != null) {
				loadedIcons[index].setTargetType(0);
				//player.packets().clearHintIcon(loadedIcons[index]);
				loadedIcons[index] = null;
			}
		}
	}

	public boolean isEmpty() {
		for (int index = 0; index < loadedIcons.length; index++)
			if (loadedIcons[index] != null)
				return false;
		return true;
	}

	private int getFreeIndex() {
		for (int index = 0; index < loadedIcons.length; index++)
			if (loadedIcons[index] == null)
				return index;
		return -1;
	}

	public void remove() {
		sendHint(0,0,0,0);
	}
}