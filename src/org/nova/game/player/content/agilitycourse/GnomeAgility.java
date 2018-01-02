package org.nova.game.player.content.agilitycourse;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.ForceMovement;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Agility;

public class GnomeAgility {

	// gnome course

	public static void walkGnomeLog(final Player player) {
		if (player.getX() != 2474 || player.getY() != 3436)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(2474, 3429, -1, false);
		player.packets().sendMessage(
				"You walk carefully across the slippery log...", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					setGnomeStage(player, 0);
					player.getSkills().addXp(Skills.AGILITY, 20);
					player.packets().sendMessage(
							"... and make it safely to the other side.", true);
					stop();
				}
			}
		}, 0, 6);
	}

	public static void climbGnomeObstacleNet(final Player player) {
		if (player.getY() != 3426)
			return;
		player.packets().sendMessage("You climb the netting.", true);
		player.useStairs(828, new Location(player.getX(), 3423, 1), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 0)
					setGnomeStage(player, 1);
				player.getSkills().addXp(Skills.AGILITY, 20);
			}
		}, 1);
	}

	public static void climbUpGnomeTreeBranch(final Player player) {
		player.packets().sendMessage("You climb the tree...", true);
		player.useStairs(828, new Location(2473, 3420, 2), 1, 2,
				"... to the plantaform above.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 1)
					setGnomeStage(player, 2);
				player.getSkills().addXp(Skills.AGILITY, 15);
			}
		}, 1);
	}
	
	public static void climbUpGnomeTreeBranch2(final Player player) {
		if(!Agility.hasLevel(player, 80)) {
			return;
		} else {
		player.packets().sendMessage("You climb the tree...", true);
		player.useStairs(828, new Location(2472, 3419, 3), 1, 2,
				"... to the plantaform above.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 2)
					setGnomeStage(player, 3);
				player.getSkills().addXp(Skills.AGILITY, 15);
			}
		}, 1);
		}
	}
		
	
	public static void RunGnomeBoard2(final Player player, final GlobalObject object) {
		if (player.getX() != 2477 || player.getY() != 3418
				|| player.getZ() != 3)
			return;
		final boolean running = player.getRun();
		 player.lock(4);
		 player.setNextAnimation(new Animation(2922));
		 final Location toTile = new Location(2484, 3418, object.getZ());
		 player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.EAST));
		player.getSkills().addXp(Skills.AGILITY, 22);
		player.packets().sendMessage("You skilfully run across the Board", true);
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setLocation(toTile);
				setGnomeStage(player, 4);
			}	
			 
		 }, 1);
	}

	public static void walkBackGnomeRope(final Player player) {
		if (player.getX() != 2483 || player.getY() != 3420
				|| player.getZ() != 2)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2477, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 20);
					player.packets().sendMessage(
							"You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void walkGnomeRope(final Player player) {
		if (player.getX() != 2477 || player.getY() != 3420
				|| player.getZ() != 2)
			return;
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(7);
		player.addWalkSteps(2483, 3420, -1, false);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(155);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					if (getGnomeStage(player) == 2)
						setGnomeStage(player, 3);
					player.getSkills().addXp(Skills.AGILITY, 20);
					player.packets().sendMessage(
							"You passed the obstacle succesfully.", true);
					stop();
				}
			}
		}, 0, 5);
	}

	public static void climbDownGnomeTreeBranch(final Player player) {
		player.useStairs(828, new Location(2487, 3421, 0), 1, 2,
				"You climbed the tree branch succesfully.");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 3)
					setGnomeStage(player, 4);
				player.getSkills().addXp(Skills.AGILITY, 15);
			}
		}, 1);
	}

	public static void climbGnomeObstacleNet2(final Player player) {
		if (player.getY() != 3425)
			return;
		player.packets().sendMessage("You climb the netting.", true);
		player.useStairs(828, new Location(player.getX(),
				player.getY() == 3425 ? 3428 : 3425, 0), 1, 2);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				if (getGnomeStage(player) == 4)
					setGnomeStage(player, 5);
				player.getSkills().addXp(Skills.AGILITY, 27);
			}
		}, 1);
	}

	public static void enterGnomePipe(final Player player, int objectX,
			int objectY) {
		final boolean running = player.getRun();
		player.setRunHidden(false);
		player.lock(8);
		player.addWalkSteps(objectX, objectY == 3431 ? 3437 : 3430, -1, false);
		player.packets().sendMessage(
				"You pulled yourself through the pipes.", true);
		WorldTasksManager.schedule(new WorldTask() {
			boolean secondloop;

			@Override
			public void run() {
				if (!secondloop) {
					secondloop = true;
					player.getAppearance().setRenderEmote(295);
				} else {
					player.getAppearance().setRenderEmote(-1);
					player.setRunHidden(running);
					player.getSkills().addXp(Skills.AGILITY, 7);
					if (getGnomeStage(player) == 5) {
						removeGnomeStage(player);
						player.getSkills().addXp(Skills.AGILITY, 90);
						//player.increaseAgility();

					}
					stop();
				}
			}
		}, 0, 6);
	}
	
	public static void RunGnomeBoard(final Player player, final GlobalObject object) {
	    if (player.getX() != 2477 || player.getY() != 3418
	            || player.getZ() != 3)
	        return;
	    final boolean running = player.getRun();
	     player.lock(4);
	     player.setNextAnimation(new Animation(2922));
	     final Location toTile = new Location(2484, 3418, object.getZ());
	     player.setNextForceMovement(new ForceMovement(player, 1, toTile, 3, ForceMovement.EAST));
	    player.getSkills().addXp(Skills.AGILITY, 22);
	    player.packets().sendMessage("You skilfully run across the Board", true);
	     WorldTasksManager.schedule(new WorldTask() {
	 
	        @Override
	        public void run() {
	            player.setLocation(toTile);
	            player.getSkills().addXp(Skills.AGILITY, 60);
	            setGnomeStage(player, 4);
	        }   
	          
	     }, 1);
	}
	
	public static void PreSwing(final Player player, final GlobalObject object) {
	    if (player.getX() != 2486 || player.getY() != 3418
	            || player.getZ() != 3)
	    player.lock(3);
	    player.setNextAnimation(new Animation(11784));
	    final Location toTile = new Location(player.getX(), 3421, object.getZ());
	    player.setNextForceMovement(new ForceMovement(player, 0, toTile, 2, ForceMovement.NORTH));
	     WorldTasksManager.schedule(new WorldTask() {
	         int stage;
	        @Override
	        public void run() {
	            if(stage == 1) {
	                player.setLocation(toTile);
	                player.setNextAnimation(new Animation(11785));
	                Swing(player,object);
	                stop();
	                }
	                stage++;
	            }
	          
	     }, 0, 1);
	}
	public static void Swing(final Player player, final GlobalObject object) {
	    if(!Agility.hasLevel(player, 80))
	        return;
	    player.lock(4);
	    final Location toTile = new Location(player.getX(), 3425, object.getZ());
	    player.setNextForceMovement(new ForceMovement(player, 0, toTile, 1, ForceMovement.NORTH));
	     WorldTasksManager.schedule(new WorldTask() {
	         int stage;
	        @Override
	        public void run() {
	            if (stage == 0) {
	                player.setNextAnimation(new Animation(11789));
	                player.setLocation(toTile);
	            } else if (stage == 1) {
	                Swing1(player,object);
	                player.getSkills().addXp(Skills.AGILITY, 70);
	                stop();
	                }
	                stage++;
	            }
	          
	     }, 0, 1);
	}
	public static void Swing1(final Player player, final GlobalObject object) {
	    if(!Agility.hasLevel(player, 80))
	        return;
	    player.lock(4);
	    final Location NextTile = new Location(player.getX(), 3429, object.getZ());
	    player.setNextForceMovement(new ForceMovement(player, 2, NextTile, 3, ForceMovement.NORTH));
	     WorldTasksManager.schedule(new WorldTask() {
	         int stage;
	        @Override
	        public void run() {
	         if (stage == 3) {              
	                player.setLocation(NextTile);
	                Swing2(player,object);
	                stop();
	                }
	                stage++;
	            }
	          
	     }, 0, 1);
	}
	public static void Swing2(final Player player, final GlobalObject object) {
	    if(!Agility.hasLevel(player, 80))
	        return;
	    player.lock(3);
	    final Location LastTile = new Location(player.getX(), 3432, object.getZ());
	    player.setNextForceMovement(new ForceMovement(player, 0, LastTile, 1, ForceMovement.NORTH));
	     WorldTasksManager.schedule(new WorldTask() {
	         int stage;
	        @Override
	        public void run() {
	         if (stage == 2) {              
	                player.setLocation(LastTile);
	                stop();
	                }
	                stage++;
	            }
	          
	     }, 0, 1);
	}
	
	public static void JumpDown(final Player player, GlobalObject object) {
	    if(!Agility.hasLevel(player, 80))
	        return;
	    player.lock(1);
	    final Location toTile = new Location(2485, 3436, 0);
	     WorldTasksManager.schedule(new WorldTask() {
	          
	        boolean secondLoop;
	        @Override
	        public void run() {
	             
	            if(!secondLoop) {
	                player.setNextForceMovement(new ForceMovement(player, 0, toTile, 5, ForceMovement.NORTH));
	                player.setNextAnimation(new Animation(2923));
	                secondLoop = true;
	            }else{
	            	player.setNextForceMovement(new ForceMovement(player, 0, toTile, 3, ForceMovement.NORTH));
	                player.setNextAnimation(new Animation(2924));
	                player.setLocation(toTile);
	                player.getSkills().addXp(Skills.AGILITY, 90);
	                stop();
	            }
	             
	        }
	          
	     }, 1, 2);
	}

	public static void removeGnomeStage(Player player) {
		player.getTemporaryAttributtes().remove("GnomeCourse");
	}

	public static void setGnomeStage(Player player, int stage) {
		player.getTemporaryAttributtes().put("GnomeCourse", stage);
	}

	public static int getGnomeStage(Player player) {
		Integer stage = (Integer) player.getTemporaryAttributtes().get(
				"GnomeCourse");
		if (stage == null)
			return -1;
		return stage;
	}
}
