package org.nova.kshan.content.bodyglow;

import org.nova.utility.misc.Misc;

/**
 * This class is used for the mask in player updating.
 * *Do not get this confused with BodyGlowColor*
 * 
 * @author K-Shan
 *
 */
public class BodyGlow {
	
	private int duration;
	private int[] colors;
	
	public BodyGlow(int duration, int[] colors) {
		this.setDuration(duration);
		this.colors = colors;
	}
	
	public static BodyGlow generate(int duration) {
		return new BodyGlow(duration, new int[] { 
			Misc.random(254), Misc.random(254), Misc.random(254), Misc.random(254) });
	}
	
	public static BodyGlow generate() {
		return new BodyGlow(-1, new int[] { 
			Misc.random(254), Misc.random(254), Misc.random(254), Misc.random(254) });
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int[] getColors() {
		return colors;
	}
	
	public void setColor(int index, byte color) {
		this.colors[index] = color;
	}

}
