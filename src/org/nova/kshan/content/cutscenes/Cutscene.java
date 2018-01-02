package org.nova.kshan.content.cutscenes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class Cutscene {
	
	protected Player player;
	protected Object[] data;
	protected int delay;
	protected int stage;
	private List<CutsceneAction> actions = new ArrayList<CutsceneAction>();
	private List<Object> storedData = new ArrayList<Object>();
	
	public abstract double getTickDelay(); // The rate at which each action tick occurs at
	public abstract void processActions(); // Processes every action
	public abstract boolean hasHiddenMinimap(); // Returns if this cutscene has a hidden minimap
	public abstract boolean hasScreenZoom(); // Only works in re-sizable mode
	public abstract boolean teleportBack(); // Returns whether or not to teleport back to previous location;
	
	/**
	 * Handles any extra actions.
	 */
	public void extraActions() {
		
	}
	
	/**
	 * Processes each tick
	 * @return
	 */
	public boolean processPulse() {
		if(delay > 0) {
			delay--;
			return true;
		}
		while(true) {
			if (stage == actions.size()) {
				stopCutscene();
				return false;
			}
			CutsceneAction action = actions.get(stage++);
			action.perform();
			extraActions(); // Additional actions after the cached actions.
			if(action.getDelay() == -1)
				continue;
			this.delay = action.getDelay();
			return true;
		}
	}
	
	/**
	 * What happens when the cutscene has started.
	 */
	public void startCutscene() {
		if(hasHiddenMinimap())
			player.packets().sendBlackOut(2);
		if(hasScreenZoom())
			player.packets().sendConfig(1241, 1);
		player.lock();
		player.setCantMove(true);
		player.setCantChangeLocation(true);
		player.stopAll(true, false);
		processActions();
	}
	
	/**
	 * What happens after the cutscene has ended.
	 */
	public void stopCutscene() {
		if(teleportBack())
			player.setLocation((Location) getData().get("previousLocation"));
		player.packets().sendConfig(1241, 0); // No more zoomed in screen
		player.packets().sendBlackOut(0); // Gets rid of hidden minimap
		player.packets().sendResetCamera();
		player.unlock();
		player.setCantMove(false);
		player.setCantChangeLocation(false);
		for(Object object : storedData) {
			if(object instanceof NPC)
				((NPC) object).finish();
		}
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public HashMap<String, Object> getData() {
		return player.getCutscenes().getData();
	}
	
	public List<Object> getStoredData() {
		return storedData;
	}
	
	////////////////////////////////// Start of Cutscene Actions \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	
	public CutsceneAction delay(int delay) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				// Nothing here because it's simply a delay
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction message(String message, int delay) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				player.sm(message);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction graphics(int id, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.graphics(id);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction animate(int id, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.animate(id);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction walk(int[] coords, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.addWalkSteps(use.getX() + coords[0], use.getY() + coords[1]); 
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction move(int x, int y, int z, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.setLocation(new Location(x, y, z));
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction forceTalk(String message, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.forceTalk(message);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction faceLocation(Location location, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.faceLocation(location);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction faceDirection(String direction, int delay, Entity... entity) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				Entity use = entity.length < 1 ? player : entity[0];
				use.changeDirection(direction);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction cam(Location location, int offsetX, int offsetY, int height, int speed, boolean look, int delay) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				if(look)
					player.camLook(location.getX() + offsetX, location.getY() + offsetY, height, speed);
				else
					player.camPos(location.getX() + offsetX, location.getY() + offsetY, height, speed);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction cam(int offsetX, int offsetY, int height, int speed, boolean look, int delay) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				if(look)
					player.camLook(player.getX() + offsetX, player.getY() + offsetY, height, speed);
				else
					player.camPos(player.getX() + offsetX, player.getY() + offsetY, height, speed);
			}
			
		};
		actions.add(action);
		return action;
	}
	
	public CutsceneAction createNPC(int npcId, int[] offsets, boolean canWalk, 
			String faceDirection, String customName, int delay) {
		CutsceneAction action = new CutsceneAction(delay) {
			
			@Override
			public void perform() {
				NPC npc = new NPC(npcId, new Location(player.getX() + offsets[0], player.getY() + offsets[1], 
					player.getZ()), canWalk, false, faceDirection, customName);
				npc.spawnNPC();
				storedData.add(npc);
			}
			
		};
		actions.add(action);
		return action;
	}
	
}
