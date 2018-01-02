package org.nova.game.player.content.agilitycourse;

import org.nova.game.Game;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Agility;

public class BarbarianOutpostAgility {

	
	
	public static void enterObstaclePipe(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		 player.lock(4);
		 player.setNextAnimation(new Animation(10580));
		 final Location toTile = new Location(object.getX(), player.getY() >= 3561 ?  3558 : 3561, object.getZ());
		 player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, player.getY() >= 3561 ? ForceMovement.SOUTH : ForceMovement.NORTH));
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				player.getSkills().addXp(Skills.AGILITY, 1/20);
			}	
			 
		 }, 1);
	}
	
	public static void runUpWall(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.lock(10);
		final Location toTile = new Location(2538, 3545, 2);
		 WorldTasksManager.schedule(new WorldTask() {
			 
			boolean secondLoop;
			@Override
			public void run() {
				
				if(!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 7, toTile, 8, ForceMovement.NORTH));
					player.setNextAnimation(new Animation(10492));
					secondLoop = true;
				}else{
					player.setNextAnimation(new Animation(10493));
					player.setLocation(toTile);
					player.getSkills().addXp(Skills.AGILITY, 15);
					stop();
				}
				
			}
			 
		 }, 1, 6);
	}
	
	public static void climbUpWall(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.useStairs(10023, new Location(2536, 3546, 3), 2, 3);
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextAnimation(new Animation(11794));
				player.getSkills().addXp(Skills.AGILITY, 15);
			}
			 
		 }, 1);
	}
	
	public static void fireSpringDevice(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.lock(5);
		player.addWalkSteps(2533, 3547, -1, false);
		final Location toTile = new Location(2532, 3553, 3);
		 WorldTasksManager.schedule(new WorldTask() {
			 
			 boolean secondLoop;
			 
			@Override
			public void run() {
				if(!secondLoop) {
					player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.NORTH));
					player.setNextAnimation(new Animation(4189));
					Game.sendObjectAnimation(player, object, new Animation(11819));
					
					secondLoop = true;
				}else{
					player.setLocation(toTile);
					player.getSkills().addXp(Skills.AGILITY, 15);
					stop();
				}
			}
			 
		 }, 1, 1);
	}
	
	public static void crossBalanceBeam(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.lock(4);
		final Location toTile = new Location(2536, 3553, 3);
		player.addWalkSteps(2536, 3553, -1, false);
		player.setNextAnimation(new Animation(16079));
		player.getAppearance().setRenderEmote(330);
		 WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setLocation(toTile);
				player.getSkills().addXp(Skills.AGILITY, 15);
				player.setNextAnimation(new Animation(-1));
				stop();
			}
			 
		 }, 2);
	}
	
	
	public static void jumpOverGap(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.lock(1);
		player.setNextAnimation(new Animation(2586));
		player.getAppearance().setRenderEmote(-1);
		 WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.setLocation(new Location(2538, 3553, 2));
				player.setNextAnimation(new Animation(2588));
				player.getSkills().addXp(Skills.AGILITY, 15);
				stop();
			}
			 
		 }, 0);
	}
	
	public static void slideDownRoof(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 90))
			return;
		player.lock(6);
		player.setNextAnimation(new Animation(11792));
		final Location toTile = new Location(2544, player.getY(), 0);
		player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.EAST));
		 WorldTasksManager.schedule(new WorldTask() {
			 int stage;
			@Override
			public void run() {
				if(stage == 0) {
					player.setLocation(new Location(2541, player.getY(), 1));
					player.setNextAnimation(new Animation(11790));
					stage = 1;
				}else if (stage == 1) {
					stage = 2;
				}else if (stage == 2) {
					player.setNextAnimation(new Animation(11791));
					stage = 3;
				}else if (stage == 3) {
					player.setLocation(toTile);
					player.setNextAnimation(new Animation(2588));
					player.getSkills().addXp(Skills.AGILITY, 15);
					 if (getStage(player) == 1) {
						removeStage(player);
						player.getSkills().addXp(Skills.AGILITY, 615);
					}
					stop();
				}
			}
			 
		 }, 0, 0);
	}
	
	
	public static void swingOnRopeSwing(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		 player.lock(4);
		 player.setNextAnimation(new Animation(751));
		 Game.sendObjectAnimation(player, object, new Animation(497));
		 final Location toTile = new Location(object.getX(), 3549, object.getZ());

			player.addWalkSteps(object.getX(), 3549, -1, false);
		 player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.SOUTH));
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.packets().sendMessage("You skilfully swing across.", true);
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				setStage(player, 0);
			}	
			 
		 }, 1);
	}
	
	public static void walkAcrossLogBalance(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		if(player.getY() != object.getY()) {
			player.addWalkSteps(2551, 3546, -1, false);
			player.lock(2);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					walkAcrossLogBalanceEnd(player, object);
				}
			}, 1);
		}else
			walkAcrossLogBalanceEnd(player, object);
	}
	
	private static void walkAcrossLogBalanceEnd(final Player player, GlobalObject object) {
		 player.packets().sendMessage("You walk carefully across the slippery log...", true);
		 player.lock(17);
		 player.setNextAnimation(new Animation(9908));
		 final Location toTile = new Location(2541, object.getY(), object.getZ());
			player.addWalkSteps(2541, object.getY(),-1, false);
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				player.getSkills().addXp(Skills.AGILITY, 13);
				 player.packets().sendMessage("... and make it safely to the other side.", true);
				 if (getStage(player) == 0)
						setStage(player, 1);
			}	
			 
		 }, 15);
	}
	
	public static void walkAcrossBalancingLedge(final Player player, final GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		 player.packets().sendMessage("You put your food on the ledge and try to edge across...", true);
		 player.lock(5);
		 player.setNextAnimation(new Animation(753));
		 player.getAppearance().setRenderEmote(157);
		 final Location toTile = new Location(2532, object.getY(), object.getZ());
		 player.setRun(true);
		 player.addWalkSteps(toTile.getX(), toTile.getY(), -1, false);
		 WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				 player.setNextAnimation(new Animation(759));
				 player.getAppearance().setRenderEmote(-1);
				player.getSkills().addXp(Skills.AGILITY, 22);
				player.packets().sendMessage("You skilfully edge across the gap.", true);
				if (getStage(player) == 2)
					setStage(player, 3);
			}	 
		 }, 3);
	}
	
	
	public static void climbObstacleNet(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 35) || player.getY() < 3545 || player.getY() > 3547) 
			return;
		player.packets().sendMessage("You climb the netting...", true);
		player.getSkills().addXp(Skills.AGILITY, 8.2);
		player.useStairs(828, new Location(object.getX()-1, player.getY(), 1), 1, 2);
		if (getStage(player) == 1)
			setStage(player, 2);
	}
	
	public static void climbOverCrumblingWall(final Player player, GlobalObject object) {
		if(!Agility.hasLevel(player, 35))
			return;
		if(player.getX() >= object.getX()) {
			 player.packets().sendMessage("You cannot climb that from this side.");
			 return;
		}
		player.packets().sendMessage("You climb the low wall...", true);
		 player.lock(3);
		 player.setNextAnimation(new Animation(4853));
		 final Location toTile = new Location(object.getX()+1, object.getY(), object.getZ());

			player.addWalkSteps(object.getX()+1, object.getY(), -1, false);
		 //player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.EAST));
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				player.getSkills().addXp(Skills.AGILITY, 13.7);
				int stage = getStage(player);
				if (stage == 3)
					setStage(player, 4);
				else if (stage == 4) {
					removeStage(player);
					player.getSkills().addXp(Skills.AGILITY, 46.2);
					player.barbLap++;
				}
			}	
			 
		 }, 1);
	}
	
	public static void removeStage(Player player) {
		player.getTemporaryAttributtes().remove("BarbarianOutpostCourse");
	}

	public static void setStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("BarbarianOutpostCourse", stage);
	}

	public static int getStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get(
				"BarbarianOutpostCourse");
		if (stage == null)
			return -1;
		return stage;
	}
}
