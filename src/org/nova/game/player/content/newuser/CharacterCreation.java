package org.nova.game.player.content.newuser;

import java.io.Serializable;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.controlers.Tutorial;

/** 			New player's tutorial.
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 * 
 */
public class CharacterCreation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1505340498106711741L;

	private Player player;
	
	
	private String tutorialName;
	
	
	public CharacterCreation(Player player) {
		this.player = player;
	}

	public static void startIntroduction(Player player) {
		player.closeInterfaces();
		player.interfaces().closeChatBoxInterface();
		
	}
	public static void addsStarter(Player player) {	
		Tutorial.addStarter(player);
		player.setTeleport(false);
		player.setInTutorial(false);
		player.starter = 1;
}
	public void startTutorial() {
	player.setTeleport(true);
	player.setInTutorial(true);
	setTutorialName("Avalani");
	player.sendMessage("Speak with "+ tutorialName +" for more instructions.");
	player.xpModifier = 50;
	player.setLocation(new Location(2700, 5211, 3));
	player.addStopDelay(1);
	}
	
	public static void addGems(Player player) {
		player.getInventory().reset();
		player.getInventory().addItem(1625, 5);
		player.getInventory().addItem(1755, 1);
		player.getCharacterCreation().sendTutorialMessage("What you need to do is that you must cut the gems. Once you cut them speak to Maverick again.");

	}
	
	public static void addAvalaniSupplies(Player player) {
		player.getInventory().addItem(317, 5); //Shrimps
		player.getInventory().addItem(1511, 3); //Logs
		player.getInventory().addItem(590, 1); //Tinderbox
		player.getCharacterCreation().sendTutorialMessage("Avalani has given you some logs and tinderbox. Make a fire and cook your shrimps, once this is done speak with her again for more instructions.");
	}
	
	public static void processClimbAction(final Player player) {
		if (player.getX() != 2707 || player.getY() != 5192
				|| player.getZ() != 3)
		 if (player.getX() != 2708 || player.getY() != 5193 || player.getZ() != 3)
			return;
		final boolean running = player.isRunning();
		player.setRunHidden(false);
		player.setRun(false);
		player.addStopDelay(12);
		player.addWalkSteps(2718, 5192, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.setRun(false);
					player.getAppearance().setRenderEmote(2199);
				} else {
					player.setRunHidden(running);
					player.setRun(false);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.packets().sendMessage(
							"You passed the ladder succesfully.", true);
					player.getAppearance().setRenderEmote(-1);
					stop();
				}
			}
		}, 0, 12);
	}
	public static void processCableAction(final Player player) {
		if (player.getX() != 2720 || player.getY() != 5194
				|| player.getZ() != 3)
		 if (player.getX() != 2720 || player.getY() != 5195 || player.getZ() != 3)
			 if (player.getX() != 2721 || player.getY() != 5195 || player.getZ() != 3)
			return;
		final boolean running = player.isRunning();
		player.setRunHidden(false);
		player.setRun(false);
		player.addStopDelay(15);
		player.addWalkSteps(2720, 5205, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.setRun(false);
					player.getAppearance().setRenderEmote(1957);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.setRun(false);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.packets().sendMessage(
							"You successfully cross to the other side of the rope.", true);
					stop();
				}
			}
		}, 0, 10);
	}
	public static void processTunnelAction(final Player player) {
		if (player.getX() != 2721 || player.getY() != 5205
				|| player.getZ() != 3)
			return;
		final boolean running = player.isRunning();
		player.setRunHidden(false);
		player.setRun(false);
		player.addStopDelay(4);
		player.addWalkSteps(2721, 5208, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.setRun(false);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.setRun(false);
					player.getSkills().addXp(Skills.AGILITY, 7);
					player.packets().sendMessage(
							"You pass through the dark tunnel.", true);

					player.closeInterfaces();
					stop();
				}
			}
		}, 0, 4);
	}
	/**
	 * Starter objects - handling.
	 * @param player
	 * @param objectId
	 * @return
	 */
	public static boolean handleObjects(Player player, int objectId) {
	
	switch (objectId) {
	case 22564:
		
		return true;
	case 22569:
		
		return true;
	case 22557:
		
		return true;
	case 22664:
		player.sendMessage("Speak with the 'Avalani' to get your instructions.");
		return true;
	case 22552:
		player.sendMessage("Speak with the druid 'Dotmatrix' to continue.");
		return true;
	}
		return false;
	}
	
	public String getTutorialName() {
		return tutorialName;
	}
	
	public void sendTutorialMessage(String Msg) {
		player.interfaces().closeChatBoxInterface(); 
		player.packets().sendMessage("<col=ff0033>Tutorial: "+Msg+"");
	}
	
	public void setTutorialName(String tutorialName) {
		this.tutorialName = tutorialName;
	}
	
}
