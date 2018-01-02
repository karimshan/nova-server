package org.nova.kshan.content.skills.construction;

import java.io.Serializable;

/**
 * Represents a Global House Object.
 * 
 * @author K-Shan
 *
 */
public class HouseObject implements Serializable {
	
	private static final long serialVersionUID = -5221007205662234707L;
	
	private int id;
	private int rotation;
	private int type;
	private int offsetX, offsetY, z;
	
	public HouseObject(int id, int rotation, int type, int offsetX, int offsetY, int z) {
		this.id = id;
		this.rotation = rotation;
		this.type = type;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.z = z;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
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

	public boolean matchesCoords(HouseObject ho) {
		return ho.getOffsetX() == getOffsetX() && ho.getOffsetY() == getOffsetY() && ho.getZ() == getZ();
	}

}
