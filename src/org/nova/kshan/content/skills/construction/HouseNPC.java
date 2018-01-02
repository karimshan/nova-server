package org.nova.kshan.content.skills.construction;

import java.io.Serializable;

/**
 * Represents a Global House NPC.
 * 
 * @author K-Shan
 *
 */
public class HouseNPC implements Serializable {
	
	private static final long serialVersionUID = 6847455968962687094L;
	
	private int id;
	private int offsetX, offsetY, z;
	private boolean randomWalk;
	private String spawnDir, customName;
	
	public HouseNPC(int id, int offsetX, int offsetY, int z, boolean randomWalk, String spawnDir, String customName) {
		this.setId(id);
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.z = z;
		this.setRandomWalk(randomWalk);
		this.setSpawnDirection(spawnDir);
		this.setCustomName(customName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public boolean hasRandomWalk() {
		return randomWalk;
	}

	public void setRandomWalk(boolean randomWalk) {
		this.randomWalk = randomWalk;
	}

	public String getSpawnDirection() {
		return spawnDir;
	}

	public void setSpawnDirection(String spawnDir) {
		this.spawnDir = spawnDir;
	}

	public String getCustomName() {
		return customName;
	}

	public void setCustomName(String customName) {
		this.customName = customName;
	}

	public boolean matchesCoords(HouseNPC ho) {
		return ho.getOffsetX() == getOffsetX() && ho.getOffsetY() == getOffsetY() && ho.getZ() == getZ();
	}

}
