package org.nova.game.player.content.cities;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.masks.Animation;
import org.nova.game.player.Player;
import org.nova.game.player.content.quests.CamelotKnight;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * Looping - Most gayest shit of planet earth.
 */
public class BarbarianOutpost {
	
	public static void processDiveJump(final Player player) {
	player.teleportPlayer(2512, 3520, 0);
	player.setCamelotknightStage(4);
	CamelotKnight.ShowBind(player);
	player.getAppearance().setRenderEmote(2034);
		player.setTeleport(true); 
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.addWalkSteps(2512, 3509, -1, false);
			switch (loop) {
			
			case 0:
				player.unlock();
				player.lock();
				player.interfaces().closeChatBoxInterface();
				player.unlock();
				player.lock();
				break;
			case 1:
				player.interfaces().sendInterface(119);
				player.sm("You are currently jumping....");
				break;
			case 3:
				player.setNextAnimation(new Animation(1115));
				break;
			case 4:
				player.sm("You have found yourself underwater, the jump hurts you in legs.");
				player.setNextAnimation(new Animation(1113));
				player.teleportPlayer(2963, 9479, 0);
				player.getAppearance().setRenderEmote(-1);
				break;
			case 5:
				player.sm("You have dived succesfully to correct location.");
				player.teleportPlayer(2963, 9479,1);
				player.closeInterfaces();
				player.unlock();
				Underwater.constructUnderwaterEffects(player);
				stop();
				break;

				
				}
				loop++;
				}
			
			}, 0, 1);
	}
	
	
}
