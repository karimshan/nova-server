package org.nova.game.engine;

/**
 * Used for registering any game ticks.
 * 
 * @author K-Shan <infamous575@yahoo.com> <skype>k-shan904</skype>
 * 
 */
public abstract class GameTick implements Runnable {
	
	protected String tickName;

	/**
	 * How many seconds the event should wait between each cycle.
	 * 
	 * @delay
	 */
	protected double delay;

	/**
	 * if the task has been stopped
	 * 
	 * @param stop
	 */
	protected boolean isStopped;

	/**
	 * Create an event with the specified tick time.
	 * 
	 * @param tick
	 */
	public GameTick(double delay) {
		this.delay = delay;
	}

	public GameTick(String string, double d) {
		this.tickName = string;
		this.delay = d;
	}

	/**
	 * Gets the current delay.
	 * 
	 * @param delay
	 * @return
	 */
	public double getDelay() {
		return delay;
	}

	/**
	 * Sets the Task delay
	 * 
	 * @param d
	 */
	public void setDelay(double d) {
		this.delay = d;
	}

	/**
	 * Checks if the task has stopped
	 * 
	 * @param stop
	 * @return
	 */
	public boolean isStopped() {
		return isStopped;
	}

	/**
	 * Stops current Task.
	 */
	public void stop() {
		whenStopped();
		isStopped = true;
		//System.out.println("THE CURRENT GAME TICK HAS BEEN STOPPED.");
	}
	
	/**
	 * What to do after the game tick gets stopped.
	 * @return
	 */
	public void whenStopped() {
		
	}
	
	/**
	 * The name of the game tick
	 * @return
	 */
	public String getName() {
		return tickName;
	}

	/**
	 * Schedules task.
	 * 
	 * @param task
	 * @param run
	 */
	public void executeTask() {
		if(isStopped())
			return;
		try {
			run();
		} catch (Throwable t) {
			t.printStackTrace();
			stop();
		}
	}

	/**
	 * Abstract void run.
	 * 
	 * @param run
	 */
	public abstract void run();
}