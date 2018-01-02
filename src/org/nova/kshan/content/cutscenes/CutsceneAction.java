package org.nova.kshan.content.cutscenes;

/**
 * An abstract class that represents a single {@link Cutscene}'s action.
 * 
 * @author K-Shan
 *
 */
public abstract class CutsceneAction {
	
	private int delay;
	
	public abstract void perform();
	
	public CutsceneAction(int delay) {
		this.delay = delay;
	}

	public int getDelay() {
		return delay;
	}

}
