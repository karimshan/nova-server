package org.nova.kshan.content.interfaces;

import java.io.Serializable;
import java.text.NumberFormat;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.TasksManager;

/**
 * 
 * @author K-Shan
 *
 */
public class Tasks implements Serializable {
	
	private static final long serialVersionUID = 8882955872715229649L;
	
	private transient Player player;
	
	public void setPlayer(Player player) {
		this.player = player;
	}

	public Tasks() {

	}
	
	private transient String interfaceState = null;
	private transient int tempButtonId = -1;
	private int points;
	
	public int points() {
		return points;
	}
	
	public void addPoints(int amount) {
		points += amount;
	}
	
	public void removePoints(int amount) {
		points -= amount;
	}
	
	public void setPoints(int amount) {
		this.points = amount;
	}
	
	public String interfaceState() {
		return interfaceState;
	}
	
	public void mainInterface() {
		player.interfaces().sendInterface(761);
		player.packets().sendString("Tasks", 761, 6);
		player.packets().sendString("Click on an area to view its tasks.", 761, 9);
		player.packets().sendString("Click the 'next' button to find a page filled with more areas.", 761, 23);
		for(int i = 10; i <= 21; i++)
			player.packets().sendString("", 761, i);
		player.packets().sendString("<col=ffffff>1). Lumbridge/Draynor", 761, 10);
		interfaceState = "Main";
	}
	
	public void lumbridgeDraynorTasks() {
		player.interfaces().sendInterface(761);
		player.packets().sendString("Lumbridge/Draynor: ("+ldTasksCompleted()+"/10)", 761, 6);
		player.packets().sendString("Click on the task you wish to see details about.", 761, 9);
		player.packets().sendString("Click the 'next' button to find a page filled with more tasks.", 761, 23);
		for(int i = 10; i <= 21; i++)
			player.packets().sendString("", 761, i);
		for(int i = 0; i < TasksManager.lDTaskNames.size(); i++)
			player.packets().sendString(ldTasks()[i] ? 
				"<col=000000><shad=00ff00><str>"+(i + 1)+"). "+TasksManager.lDTaskNames.get(i) : ""
					+ "<col=000000><shad=ff0000>"+(i + 1)+"). "+TasksManager.lDTaskNames.get(i), 761, 10 + i);
		player.packets().sendString("<col=ffffff>Next..", 761, 21);
		interfaceState = "Lumb&DrayInter";
	}
	
	public void buttons(int interfaceId, int buttonId) {
		switch(interfaceState) {
		
			case "Main":
				if(interfaceId == 761)
					if(buttonId == 10)
						lumbridgeDraynorTasks();
				break;
				
			case "Lumb&DrayInter":
				if(interfaceId == 761) {
					for(int i = 0; i < TasksManager.lDTaskNames.size(); i++) {
						if(i > 11)
							i = 11;
						if(buttonId == 10 + i && buttonId != 21)
							TasksManager.display(player, TasksManager.lDFileNames.get(i));
						if(ldTasks()[i])
							player.packets().sendString("YOU HAVE COMPLETED THIS TASK.", 35, 4);
						interfaceState = "returnBack";
					}
				}
				break;
				
			case "returnBack":
				if(interfaceId == 35)
					if(buttonId == 10)
						lumbridgeDraynorTasks();
				break;
		}
	}
	
	public void information(String title, String difficulty, String... messages) {
		player.interfaces().sendInterface(35);
		player.packets().sendString(title, 35, 8);
		player.packets().sendString("This is a"+(difficulty.toLowerCase().contains("easy") ? 
				"n "+difficulty : difficulty.toLowerCase().contains("elite") ? 
				"n "+difficulty : " "+difficulty)+" task, which will require "
				+ ""+(difficulty.toLowerCase().contains("easy") ? "minimal" : 
				difficulty.toLowerCase().contains("medium") ? "some" : 
				difficulty.toLowerCase().contains("hard") ? "a lot of" : "an extreme amount of")+" effort.", 35, 7);
		player.packets().sendString("How to complete this task:", 35, 1);
		player.packets().sendString("Requirements:", 35, 2);
		player.packets().sendString("Rewards:", 35, 3);
		for(int i = 0; i < messages.length; i++)
			player.packets().sendString(messages[i], 35, 4 + i);
	}
	
	public void taskCompletion(String fileName) {
		player.packets().sendOverlay(1073);
		player.packets().sendString("Task Complete!", 1073, 10);
		int index = indexFromName(fileName);
		String taskName = TasksManager.lDTaskNames.get(index);
		int points = TasksManager.lDPoints.get(index);
		String area = TasksManager.lDAreas.get(index);
		Item[] item = TasksManager.lDRewards.get(index);
		player.packets().sendString(taskName, 1073, 11);
		player.packets().sendMessage("You have completed the task: <col=c12006>"+taskName+"</col>! You are rewarded "+points+" point(s) and");
		player.packets().sendMessage(NumberFormat.getInstance().format(item[0].getAmount())+" coins have been added to your bank for completing this task.");
		player.getBank().addItem(item[0].getId(), item[0].getAmount(), true);
		if(item.length > 1) {
			StringBuilder message = new StringBuilder();
			for(int i = 1; i < item.length; i++) {
				if(i == item.length - 1 && i != 1)
					message.append("and x"+item[i].getAmount()+" "+item[i].getName()+""
						+ ""+(!item[i].getName().endsWith("s") ? "(s)" : "")+".");
				else if(item.length - 1 == 1)
					message.append("x"+item[i].getAmount()+" "+item[i].getName()+""
						+ ""+(!item[i].getName().endsWith("s") ? "(s)" : "")+".");
				else
					message.append("x"+item[i].getAmount()+" "+item[i].getName()+""
						+ ""+(!item[i].getName().endsWith("s") ? "(s)" : "")+", ");
				player.getBank().addItem(item[i].getId(), item[i].getAmount(), true);
			}
			player.packets().sendMessage("You also receive: <col=0000ff>"+message.toString()+"</col>");
			player.sm("Those item(s) have been put in your bank account.");
		}
		addPoints(points);
		if(area.equals("Lumb&Dray")) {
			addLDTasksCompleted();
			setCompleted(ldTasks(), (byte) index);
		}
		addTotalTasksCompleted();
		Game.submit(new GameTick(1.0) {
			int tick = 0;
			@Override
			public void run() {
				tick++;
				if(tick == 3) {
					player.packets().sendString("You now have:", 1073, 10);
					player.packets().sendString(points()+" task point(s).", 1073, 11);
				} else if(tick == 6) {
					stop();
					player.packets().sendRemoveOverlay();
				}
			}
		});
	}
	
	public int indexFromName(String fileName) {
		fileName = fileName.toLowerCase();
		for(String s : TasksManager.lDFileNames)
			if(s.toLowerCase().contains(fileName))
				return TasksManager.lDFileNames.indexOf((String) s.toLowerCase());
		return -1;
	}
	
	/**
	 * Start of Lumbridge and draynor task data
	 */
	boolean[] ldTasks = new boolean[125];
	private int ldTasksCompleted;
	
	public int ldTasksCompleted() {
		return ldTasksCompleted;
	}
	
	public void addLDTasksCompleted() {
		ldTasksCompleted++;
	}
	
	public boolean[] ldTasks() {
		return ldTasks;
	}

	/**
	 * End of Lumbridge and Draynor task data.
	 */
	
	private int totalTasksCompleted;
	
	public int totalTasksCompleted() {
		return totalTasksCompleted;
	}
	
	public void addTotalTasksCompleted() {
		totalTasksCompleted++;
	}
	
	
	public void setCompleted(boolean[] area, byte index) {
		area[index] = true;
	}
		
	public boolean hasFinished(boolean[] area, byte index) {
		return area[index];
	}
	
	public void tempButtonId(int id) {
		this.tempButtonId = id;
	}
	
	public int tempButtonId() {
		return tempButtonId;
	}
	
}
