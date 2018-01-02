package org.nova.kshan.content.skills.construction;

import java.io.Serializable;

/**
 * 
 * @author K-Shan
 *
 */
public class HouseRoom implements Serializable {
	
	private static final long serialVersionUID = 7128842372722410037L;

	private String name;
	
	private int cX;
	private int cY;
	private int levelRequired;
	
	private int cost;
	
	private boolean showingRoof;
	private boolean hasDoors;
	
	public HouseRoom(String name, int cX, int cY, int levelRequired, int cost, 
			boolean showingRoof, boolean hasDoors) {
		this.name = name;
		this.cX = cX;
		this.cY = cY;
		this.levelRequired = levelRequired;
		this.cost = cost;
		this.showingRoof = showingRoof;
		this.hasDoors = hasDoors;
	}
	
	public HouseRoom(String name, int cX, int cY, int levelRequired, int cost) {
		this.name = name;
		this.cX = cX;
		this.cY = cY;
		this.levelRequired = levelRequired;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public int getChunkX() {
		return cX;
	}

	public void setChunkX(int chunkX) {
		this.cX = chunkX;
	}

	public int getChunkY() {
		return cY;
	}

	public void setChunkY(int chunkY) {
		this.cY = chunkY;
	}
	
	public int getLevelRequired() {
		return levelRequired;
	}
	
	public void setLevelRequired(int level) {
		this.levelRequired = level;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public boolean isShowingRoof() {
		return showingRoof;
	}

	public void setShowingRoof(boolean showRoof) {
		this.showingRoof = showRoof;
	}

	public boolean hasDoors() {
		return hasDoors;
	}

	public void setHasDoors(boolean hasDoors) {
		this.hasDoors = hasDoors;
	}
}
