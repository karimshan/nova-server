package org.nova.game.player.content.minigames.clanwars;

import org.nova.game.map.Location;


/**
 * Represents the possible area types.
 * @author Emperor
 *
 */
public enum AreaType {

	/**
	 * The classic area type.
	 */
	CLASSIC_AREA(new Location(2752, 5888, 0), new Location(2815, 6015, 0), 35, 10, -36, -9, 56, 58, -7, -58),
	
	/**
	 * The plateau area type.
	 */
	PLATEAU(new Location(2831, 5888, 0), new Location(2928, 5951, 0), 59, 12, -38, -10, 27, 21, -70, -20),
	
	/**
	 * The forsaken quarry area type.
	 */
	FORSAKEN_QUARRY(new Location(2880, 5504, 0), new Location(2943, 5567, 0), 11, 11, -10, -10, 28, 32, -28, -30),
	
	/**
	 * The blasted forest area type.
	 */
	BLASTED_FOREST(new Location(2880, 5632, 0), new Location(2942, 5695, 0), 12, 9, -12, -9, 13, 29, -13, -28),
	
	/**
	 * The turrets area type.
	 */
	TURRETS(new Location(2689, 5505, 0), new Location(2750, 5630, 0), 42, 7, -40, -5, 2, 3, 0, -1);
	
	/**
	 * The south west tile of the area.
	 */
	private final Location southWestTile;
	
	/**
	 * The north east tile of the area.
	 */
	private final Location northEastTile;
	
	/**
	 * The first team's x spawn offset.
	 */
	private final int firstSpawnOffsetX;
	
	/**
	 * The first team's y spawn offset.
	 */
	private final int firstSpawnOffsetY;
	
	/**
	 * The second team's x spawn offset.
	 */
	private final int secondSpawnOffsetX;
	
	/**
	 * The second team's y spawn offset.
	 */
	private final int secondSpawnOffsetY;
	
	/**
	 * The first team's x death offset.
	 */
	private final int firstDeathOffsetX;
	
	/**
	 * The first team's y death offset.
	 */
	private final int firstDeathOffsetY;
	
	/**
	 * The second team's x death offset.
	 */
	private final int secondDeathOffsetX;
	
	/**
	 * The second team's y death offset.
	 */
	private final int secondDeathOffsetY;
	
	/**
	 * Constructs a new {@code AreaType.java} {@code Object}.
	 * @param southWestTile The south western tile.
	 * @param northEastTile The north east tile.
	 * @param firstSpawnOffsetX The first team's x spawn offset.
	 * @param firstSpawnOffsetY The first team's y spawn offset.
	 * @param secondSpawnOffsetX The second team's x spawn offset.
	 * @param secondSpawnOffsetY The second team's y spawn offset.
	 * @param firstDeathOffsetX The first team's x death offset.
	 * @param firstDeathOffsetY The first team's y death offset.
	 * @param secondDeathOffsetX The second team's x death offset.
	 * @param secondDeathOffsetY The second team's y death offset.
	 */
	private AreaType(Location southWestTile, Location northEastTile, int firstSpawnOffsetX, int firstSpawnOffsetY, int secondSpawnOffsetX, int secondSpawnOffsetY, int firstDeathOffsetX, int firstDeathOffsetY, int secondDeathOffsetX, int secondDeathOffsetY) {
		this.southWestTile = southWestTile;
		this.northEastTile = northEastTile;
		this.firstSpawnOffsetX = firstSpawnOffsetX;
		this.firstSpawnOffsetY = firstSpawnOffsetY;
		this.secondSpawnOffsetX = secondSpawnOffsetX;
		this.secondSpawnOffsetY = secondSpawnOffsetY;
		this.firstDeathOffsetX = firstDeathOffsetX;
		this.firstDeathOffsetY = firstDeathOffsetY;
		this.secondDeathOffsetX = secondDeathOffsetX;
		this.secondDeathOffsetY = secondDeathOffsetY;
	}

	/**
	 * Gets the southWestTile.
	 * @return The southWestTile.
	 */
	public Location getSouthWestTile() {
		return southWestTile;
	}

	/**
	 * Gets the northEastTile.
	 * @return The northEastTile.
	 */
	public Location getNorthEastTile() {
		return northEastTile;
	}

	/**
	 * Gets the firstSpawnOffsetX.
	 * @return The firstSpawnOffsetX.
	 */
	public int getFirstSpawnOffsetX() {
		return firstSpawnOffsetX;
	}

	/**
	 * Gets the firstSpawnOffsetY.
	 * @return The firstSpawnOffsetY.
	 */
	public int getFirstSpawnOffsetY() {
		return firstSpawnOffsetY;
	}

	/**
	 * Gets the secondSpawnOffsetX.
	 * @return The secondSpawnOffsetX.
	 */
	public int getSecondSpawnOffsetX() {
		return secondSpawnOffsetX;
	}

	/**
	 * Gets the secondSpawnOffsetY.
	 * @return The secondSpawnOffsetY.
	 */
	public int getSecondSpawnOffsetY() {
		return secondSpawnOffsetY;
	}

	/**
	 * Gets the firstDeathOffsetX.
	 * @return The firstDeathOffsetX.
	 */
	public int getFirstDeathOffsetX() {
		return firstDeathOffsetX;
	}

	/**
	 * Gets the firstDeathOffsetY.
	 * @return The firstDeathOffsetY.
	 */
	public int getFirstDeathOffsetY() {
		return firstDeathOffsetY;
	}

	/**
	 * Gets the secondDeathOffsetX.
	 * @return The secondDeathOffsetX.
	 */
	public int getSecondDeathOffsetX() {
		return secondDeathOffsetX;
	}

	/**
	 * Gets the secondDeathOffsetY.
	 * @return The secondDeathOffsetY.
	 */
	public int getSecondDeathOffsetY() {
		return secondDeathOffsetY;
	}
}