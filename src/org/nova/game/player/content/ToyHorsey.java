package org.nova.game.player.content;

import java.util.Random;

import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceTalk;
import org.nova.game.player.Player;

public class ToyHorsey {

	private static String speech [][] = 
		{{"Come-on Dobbin, we can win the race!"}, 
		{"Hi-ho Silver, and away!"},
		{"Neaahhhyyy!"}};


	/**
	 * 
	 * @param player
	 */
	public static void play(Player player) {
		int rand = RandomSpeech();
		player.setNextAnimation(new Animation(918));
		player.setNextForceTalk(new ForceTalk(speech[rand][0]));
	}
	
	/**
	 * 
	 * @return
	 */
	public static int RandomSpeech() {
		int random = 0;
		Random rand = new Random();
		random = rand.nextInt(speech.length);
		return random;
	}
	
}
