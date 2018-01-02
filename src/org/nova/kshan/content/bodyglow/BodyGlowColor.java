package org.nova.kshan.content.bodyglow;

import java.util.Arrays;

/**
 * Represents a single body glow color object.
 * 
 * @author K-Shan
 *
 */
public class BodyGlowColor {

	private String name;
	private int[] colors;
	
	public BodyGlowColor(String name, int[] colors) {
		this.name = name;
		this.colors = colors;
	}

	public String getName() {
		return name;
	}
	
	public int[] getColors() {
		return colors;
	}

	public String getColorsAsString() {
		return Arrays.toString(colors);
	}
	
	@Override
	public String toString() {
		return getName()+" - "+getColorsAsString();
	}

}
