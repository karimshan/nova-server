package org.nova.game.player;

public final class HintIcon {
	
	public static final class HintDirections {
		public static final byte CENTER = 2;
		public static final byte WEST = 3;
		public static byte EAST = 4;
		public static final byte SOUTH = 5;
		public static final byte NORTH = 6;
	}
	
	public transient static final byte DOOR_HEIGHT = 125;

	private int coordX;
	private int coordY;
	private int distanceShown;
	private int distanceFromFloor;
	private int targetType;
	private int targetIndex;
	private int arrowType;
	private boolean followOnFeet;
	private int index;

	public HintIcon() {
		this.setIndex(7);
	}

	public HintIcon(int targetType, boolean modelId, int index) {
		this.setTargetType(targetType);
		this.setFollowOnFeet(modelId);
		this.setIndex(index);
	}

	public HintIcon(int targetIndex, int targetType, int arrowType, boolean modelId, int index) {
		this.setTargetType(targetType);
		this.setTargetIndex(targetIndex);
		this.setArrowType(arrowType);
		this.setFollowOnFeet(modelId);
		this.setIndex(index);
	}

	public HintIcon(int coordX, int coordY, int distanceShown, int distanceFromFloor, int targetType, int arrowType, boolean modelId, int index) {
		this.setCoordX(coordX);
		this.setCoordY(coordY);
		this.setDistanceShown(distanceShown);
		this.setDistanceFromFloor(distanceFromFloor);
		this.setTargetType(targetType);
		this.setArrowType(arrowType);
		this.setFollowOnFeet(modelId);
		this.setIndex(index);
	}

	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetIndex(int targetIndex) {
		this.targetIndex = targetIndex;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public void setArrowType(int arrowType) {
		this.arrowType = arrowType;
	}

	public int getArrowType() {
		return arrowType;
	}

	public void setFollowOnFeet(boolean modelId) {
		this.followOnFeet = modelId;
	}

	public boolean getFollowOnFeet() {
		return followOnFeet;
	}

	public void setIndex(int modelPart) {
		this.index = modelPart;
	}

	public int getIndex() {
		return index;
	}

	public void setCoordX(int coordX) {
		this.coordX = coordX;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordY(int coordY) {
		this.coordY = coordY;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setDistanceShown(int plane) {
		this.distanceShown = plane;
	}

	public int getDistanceShown() {
		return distanceShown;
	}

	public void setDistanceFromFloor(int distanceFromFloor) {
		this.distanceFromFloor = distanceFromFloor;
	}

	public int getDistanceFromFloor() {
		return distanceFromFloor;
	}
}
