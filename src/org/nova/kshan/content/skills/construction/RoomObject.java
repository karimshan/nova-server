package org.nova.kshan.content.skills.construction;

import org.nova.game.item.Item;

/**
 * 
 * @author K-Shan
 *
 */
public class RoomObject {
	
	private String name;
	private int itemId;
	private byte levelRequired;
	private double xp;
	private int[] objectIds;
	private Item[] itemsRequired;
	
	public RoomObject(String name, int itemId, int levelRequired, double xp, int[] objectIds, Item[] itemsRequired) {
		this.name = name;
		this.itemId = itemId;
		this.levelRequired = (byte) levelRequired;
		this.xp = xp;
		this.objectIds = objectIds;
		this.itemsRequired = itemsRequired;
	}
	
	public RoomObject(String name, int itemId, int levelRequired, double xp, int objectId, Item[] itemsRequired) {
		this.name = name;
		this.itemId = itemId;
		this.levelRequired = (byte) levelRequired;
		this.xp = xp;
		this.objectIds = new int[] { objectId };
		this.itemsRequired = itemsRequired;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public byte getLevelRequired() {
		return levelRequired;
	}

	public void setLevelRequired(byte levelRequired) {
		this.levelRequired = levelRequired;
	}

	public double getXp() {
		return xp;
	}

	public void setXp(double xp) {
		this.xp = xp;
	}

	public int[] getObjectIds() {
		return objectIds;
	}

	public void setObjectIds(int[] objectIds) {
		this.objectIds = objectIds;
	}

	public Item[] getItemsRequired() {
		return itemsRequired;
	}

	public void setItemsRequired(Item[] itemsRequired) {
		this.itemsRequired = itemsRequired;
	}

}
