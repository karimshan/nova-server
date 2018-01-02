package org.nova.kshan.utilities;

/**
 * 
 * @author K-Shan
 *
 */
public class MathUtils {
	
	/**
	 * Performs an action after every x interval
	 * @param runnable
	 * @param integer
	 * @param number
	 * @return 
	 */
	public static boolean forInterval(Runnable runnable, int integer, double value) {
		double multiplier = value / integer;
		double finalNum = ((multiplier * integer) % integer);
		if(finalNum == 0) {
			runnable.run();
			return true;
		} else
			return false;
	}

}
