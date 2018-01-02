package org.nova.kshan.content.skills.slayer;

import java.io.Serializable;

import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class SlayerTask implements Serializable {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -3885979679549716755L;
	
	/**
	 * The Player
	 */
	private Player player;

	/**
	 * The Slayer Monsters.
	 */
	public static final int[] SLAYER_MASTERS = {9085, 8462, 8275, 1598, 7780, 8274, 1597, 8273};
	
	/**
	 * Slayer masters data
	 */
	public enum Master {
		
		 KURADAL(9085, new Object[][] 
                 { { "General Graardor", 75, 5, 15, 350.0 },
                 { "Kree'arra", 75, 5, 15, 355.0 },
                 { "Cow", 1, 25, 125, 25.0 },
                 { "K'ril Tsutsaroth", 75, 5, 15, 275.0 },
                 { "Gargoyle", 75, 5, 15, 255.0 },
                 { "Dark beast", 75, 5, 15, 285.0 },
                 { "Corporal Beast", 75, 5, 15, 310.0 } }),

         SPRIA(8462, new Object[][]
                 { { "Banshee", 15, 40, 85, 85.0 },
   				{ "Bat", 1, 40, 85, 35.0 },
   				{ "Bear", 1, 40, 85, 50.0 },
   				{ "Cave crawler", 10, 40, 85, 122.0 },
   				{ "Cave slime", 17, 40, 85, 25.0 },
   				{ "Cockatrice", 25, 40, 85, 37.0 },
   				{ "Cyclopse", 1, 30, 60, 105.0 },
   				{ "Flesh crawler", 1, 40, 70, 25.0 },
                 { "Rock Crab", 1, 25, 125, 25.0 },
                 { "Crawling hand", 1, 50, 125, 55.0 } }),
 
                 DURADEL(8275, new Object[][] {
          				{ "Aberrant spectre", 60, 130, 200, 90.0 },
          				{ "Abyssal demon", 85, 130, 200, 150.0 },
          				{ "Aquanite", 78, 130, 200, 125.0 },
          				{ "Black demon", 1, 130, 200, 157.0 },
          				{ "Black dragon", 1, 40, 80, 199.4 },
          				{ "Bloodveld", 50, 130, 200, 134.0 },
          				{ "Dagannoth", 1, 130, 200, 331.0 },
          				{ "Dark beast", 90, 130, 200, 220.0 },
          				{ "Desert strykewyrm", 77, 90, 140, 120.0 },
          				{ "Dust devil", 65, 130, 200, 105.0 },
          				{ "Ganodermic creature", 95, 50, 100, 540.0 },
          				{ "Gargoyle", 75, 130, 200, 105.0 },
          				{ "Greater demon", 1, 130, 200, 87.0 },
          				{ "Grifalopine", 88, 80, 120, 333.0 },
          				{ "Hellhound", 1, 130, 200, 116.0 },
          				{ "Iron dragon", 1, 40, 80, 173.2 },
          				{ "Steel dragon", 1, 40, 80, 220.4 },
          				{ "Nechryael", 80, 120, 200, 280.0 }
          				}),
          		CHAELDAR(1598, new Object[][] {
          				{ "Aberrant spectre", 60, 110, 170, 50.0 },
          				{ "Banshee", 15, 110, 170, 93.5 },
          				{ "Basilisk", 40, 110, 170, 57.0 },
          				{ "Bloodveld", 50, 110, 170, 196.0 },
          				{ "Blue dragon", 1, 110, 170, 157.0 },
          				{ "Brine rat", 47, 110, 170, 45.0 },
          				{ "Bronze dragon", 1, 30, 60, 130.5 },
          				{ "Cave crawler", 10, 110, 170, 60.6 },
          				{ "Cave horror", 58, 110, 170, 57.6 },
          				{ "Crawling hand", 5, 110, 170, 100.0 },
          				{ "Dagannoth", 1, 110, 170, 255.0 },
          				{ "Dust devil", 65, 110, 170, 77.2 },
          				{ "Elves", 1, 60, 90, 89.4 },
          				{ "Fire giant", 1, 110, 170, 103.6 },
          				{ "Gargoyle", 75, 110, 170, 103.5 },
          				{ "Grifolapine", 88, 62, 63, 293.4 },
          				{ "Grifolaroo", 82, 62, 63, 340.5 },
          				{ "Grotworm", 1, 72, 109, 73.6 },
          				{ "Jungle strykewyrm", 73, 80, 110, 185.6 },
          				{ "Infernal mage", 45, 110, 170, 50.0 },
          				{ "Jelly", 52, 110, 170, 87.0 },
          				{ "Jungle horror", 1, 110, 170, 41.5 },
          				{ "Kalphite", 1, 110, 170, 267.8 },
          				{ "Lesser demon", 1, 110, 170, 90.5 },
          				{ "Shadow warrior", 1, 110, 170, 67.0 },
          				{ "Troll", 1, 110, 170, 150.0 },
          				{ "Turoth", 55, 110, 170, 210.0 },
          				{ "Vyrewatch", 31, 89, 106, 196.0 },
          				{ "Warped tortiose", 56, 110, 170, 87.0 }
          				}),
          		SUMONA(7780, new Object[][] {
          				{ "Aberrant spectre", 60, 110, 170, 50.0 },
          				{ "Banshee", 15, 110, 170, 93.5 },
          				{ "Basilisk", 40, 110, 170, 57.0 },
          				{ "Bloodveld", 50, 110, 170, 196.0 },
          				{ "Blue dragon", 1, 110, 170, 157.0 },
          				{ "Brine rat", 47, 110, 170, 45.0 },
          				{ "Bronze dragon", 1, 30, 60, 130.5 },
          				{ "Cave crawler", 10, 110, 170, 60.6 },
          				{ "Cave horror", 58, 110, 170, 57.6 },
          				{ "Crawling hand", 5, 110, 170, 100.0 },
          				{ "Dagannoth", 1, 110, 170, 255.0 },
          				{ "Dust devil", 65, 110, 170, 77.2 },
          				{ "Elves", 1, 60, 90, 89.4 },
          				{ "Fire giant", 1, 110, 170, 103.6 },
          				{ "Gargoyle", 75, 110, 170, 103.5 },
          				{ "Grifolapine", 88, 62, 63, 293.4 },
          				{ "Grifolaroo", 82, 62, 63, 340.5 },
          				{ "Grotworm", 1, 72, 109, 73.6 },
          				{ "Jungle strykewyrm", 73, 80, 110, 185.6 },
          				{ "Infernal mage", 45, 110, 170, 50.0 },
          				{ "Jelly", 52, 110, 170, 87.0 },
          				{ "Jungle horror", 1, 110, 170, 41.5 },
          				{ "Kalphite", 1, 110, 170, 267.8 },
          				{ "Lesser demon", 1, 110, 170, 90.5 },
          				{ "Shadow warrior", 1, 110, 170, 67.0 },
          				{ "Troll", 1, 110, 170, 150.0 },
          				{ "Turoth", 55, 110, 170, 210.0 },
          				{ "Vyrewatch", 31, 89, 106, 196.0 },
          				{ "Warped tortiose", 56, 110, 170, 87.0 }
          				}),
          		MAZCHNA(8274, new Object[][] {
          				{ "Banshee", 15, 40, 85, 85.0 },
          				{ "Bat", 1, 40, 85, 35.0 },
          				{ "Bear", 1, 40, 85, 50.0 },
          				{ "Cave crawler", 10, 40, 85, 122.0 },
          				{ "Cave slime", 17, 40, 85, 25.0 },
          				{ "Cockatrice", 25, 40, 85, 37.0 },
          				{ "Cyclopse", 1, 30, 60, 105.0 },
          				{ "Flesh crawler", 1, 40, 70, 25.0 },
          				{ "Ghoul", 1, 40, 70, 50.0 },
          				{ "Ghost", 1, 40, 70, 157.0 },
          				{ "Hill giant", 1, 40, 70, 35.0 },
          				{ "Hobgoblin", 1, 40, 70, 88.0 },
          				{ "Ice warrior", 1, 40, 70, 59.0 },
          				{ "Kalphite", 32, 40, 70, 255.5 },
          				{ "Pyrefiend", 30, 40, 70, 48.0 },
          				{ "Rock slug", 20, 40, 70, 27.0 },
          				{ "Scorpion", 1, 40, 70, 88.0 },
          				{ "Vampyre", 1, 40, 70, 60.0 },
          				{ "Wolf", 1, 40, 70, 100.0 },
          				{ "Zombie", 1, 40, 70, 102.0 } 
          				}),
          		VANNAKA(1597, new Object[][] {
          				{ "Aberrant spectre", 60, 60, 120, 90.0 },
          				{ "Ankou", 1, 60, 120, 80.0 },
          				{ "Banshee", 15, 60, 120, 85.0 },
          				{ "Basilisk", 40, 60, 120, 75.0 },
          				{ "Bloodveld", 50, 60, 120, 200.0 },
          				{ "Brine rat", 47, 60, 120, 50.0 },
          				{ "Cockatrice", 25, 60, 120, 37.0 },
          				{ "Crocodile", 1, 30, 60, 72.0 },
          				{ "Cyclops", 1, 60, 120, 100.0 },
          				{ "Dust devil", 65, 60, 120, 105.0 },
          				{ "Earth warrior", 1, 30, 60, 54.0 },
          				{ "Ghoul", 1, 60, 120, 50.0 },
          				{ "Green dragon", 1, 30, 60, 75.0 },
          				{ "Grotworm", 1, 68, 69, 90.0 },
          				{ "Harpie bug swarm", 33, 60, 120, 25.0 },
          				{ "Hill giant", 1, 60, 120, 35.0 },
          				{ "Ice giant", 1, 60, 120, 70.0 },
          				{ "Infernal mage", 60, 120, 60.0 },
          				{ "Jelly", 52, 60, 120, 50.0 },
          				{ "Jungle horror", 1, 60, 120, 50.0 },
          				{ "Killerwatt", 37, 60, 120, 51.0 },
          				{ "Lesser demon", 1, 60, 120, 79.0 },
          				{ "Mogre", 32, 60, 120, 48.0 },
          				{ "Moss giant", 1, 60, 120, 120.0 },
          				{ "Ogre", 1, 60, 120, 60.0 },
          				{ "Pyrefiend", 30, 60, 120, 45.0 },
          				{ "Shade", 1, 60, 120, 110.0 },
          				{ "Shadow warrior", 1, 60, 120, 67.0 },
          				{ "Turoth", 55, 60, 120, 60.0 },
          				{ "Vampyre", 1, 60, 120, 60.0 },
          				{ "Werewolf", 1, 60, 120, 120.0 }
          				}),
          		TURAEL(8273, new Object[][] {
          				{ "Banshee", 15, 40, 85, 85.0 },
          				{ "Bat", 1, 40, 85, 35.0 },
          				{ "Bear", 1, 40, 85, 50.0 },
          				{ "Cave crawler", 10, 40, 85, 122.0 },
          				{ "Cave slime", 17, 40, 85, 25.0 },
          				{ "Cockatrice", 25, 40, 85, 37.0 },
          				{ "Cyclopse", 1, 30, 60, 105.0 },
          				{ "Flesh crawler", 1, 40, 70, 25.0 },
          				{ "Ghoul", 1, 40, 70, 50.0 },
          				{ "Ghost", 1, 40, 70, 157.0 },
          				{ "Hill giant", 1, 40, 70, 35.0 },
          				{ "Hobgoblin", 1, 40, 70, 88.0 },
          				{ "Ice warrior", 1, 40, 70, 59.0 },
          				{ "Kalphite", 32, 40, 70, 255.5 },
          				{ "Pyrefiend", 30, 40, 70, 48.0 },
          				{ "Rock slug", 20, 40, 70, 27.0 },
          				{ "Scorpion", 1, 40, 70, 88.0 },
          				{ "Vampyre", 1, 40, 70, 60.0 },
          				{ "Wolf", 1, 40, 70, 100.0 },
          				{ "Zombie", 1, 40, 70, 102.0 } 
          				});

		private int id;
		private Object[][] data;

		private Master(int id, Object[][] data) {
			this.id = id;
			this.data = data;
		}
		
		public static Master forId(int id) {
			for (Master master : Master.values()) {
				if (master.id == id) {
					return master;
				}
			}
			return null;
		}

		public int getId() {
			return id;
		}
		
	}
	
	public Player player() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public SlayerTask() {
		
	}

	private Master master;
	private int taskId;
	private int taskAmount;
	private int amountKilled;
	private String currentMaster;
	private boolean completed = false;

	public SlayerTask(Master master, int taskId, int taskAmount) {
		this.master = master;
		this.taskId = taskId;
		this.taskAmount = taskAmount;
		currentMaster(Misc.formatPlayerNameForDisplay(master.toString().toLowerCase()));
		completed(false);
	}

	public String monsterName() {
		return (String) master.data[taskId][0];
	}

	public static SlayerTask assignRandomTask(Player player, Master master) {
		SlayerTask task = null;
		while (true) {
			int random = Misc.random(master.data.length - 1);
			int requiredLevel = (Integer) master.data[random][1];
			if (player.getSkills().getLevel(Skills.SLAYER) < requiredLevel) {
				continue;
			}
			int minimum = (Integer) master.data[random][2];
			int maximum = (Integer) master.data[random][3];
			if (task == null) {
				task = new SlayerTask(master, random, Misc.random(minimum,
						maximum));
			task.currentMaster(Misc.formatPlayerNameForDisplay(master.toString().toLowerCase()));
		
			}
			break;
		}
		player.sm("You've been assigned a slayer task: "+task.taskTotal()+" "+task.monsterName().toLowerCase()+"s.");
		return task;
	}
	
	public int levelRequired(String name) {
		int i = 0;
		for(Master m : Master.values()) {
			i++;
			if(monsterName().equalsIgnoreCase(name))
				return (int) m.data[i][1];
		}
		return 1;
	}

	public int taskId() {
		return taskId;
	}

	public int taskTotal() {
		return taskAmount;
	}

	public double xpAmount() {
		return (double) master.data[taskId][4];
	}

	public Master master() {
		return master;
	}
	
	public int amountKilled() {
		return amountKilled;
	}

	public void amountKilled(int amountKilled) {
		this.amountKilled = amountKilled;
	}
	
	public void currentMaster(String current) {
		this.currentMaster = current;
	}
	
	public String currentMaster() {
		return currentMaster;	
	}
	
	public int amountLeft() {
		return taskTotal() - amountKilled();
	}

	public static void sendDeath(Player player, NPC n) {
		player.slayerTask().amountKilled(player.slayerTask().amountKilled() + 1);
//		player.packets().sendMessage("Excellent, you slay the "+n.defs().name.toLowerCase()+"! "
//				+ "Only "+player.slayerTask().amountLeft()+" left to kill.");
		if (player.slayerTask().amountLeft() < 1) {
			player.getSkills().addXp(Skills.SLAYER, player.slayerTask().xpAmount() * 2.1);
			player.slayerTask().completed(true);
			if (player.getEquipment().getRingId() == 13281) {
				player.setSlayerPoints(player.slayerPoints() + Misc.getRandom(40));
				player.packets().sendMessage("You've finished your slayer task. Speak to "+player.slayerTask().currentMaster()+" in "+player.slayerTask().currentMasterLocation()+" for another task.");
			} else {
				player.setSlayerPoints(player.slayerPoints() + Misc.getRandom(25));
				player.packets().sendMessage("You've finished your slayer task. Speak to "+player.slayerTask().currentMaster()+" in "+player.slayerTask().currentMasterLocation()+" for another task.");
			}
		} else {
			player.getSkills().addXp(Skills.SLAYER, player.slayerTask().xpAmount());
		}
	}

	public String currentMasterLocation() {
		String name = currentMaster();
		return name != null && name.equalsIgnoreCase("Spria") ? "Taverly" : 
			name != null && name.equalsIgnoreCase("Mazchna") ? "Canifis" :
			name != null && name.equalsIgnoreCase("Turael") ? "Burthope" :
			name != null && name.equalsIgnoreCase("Vannaka") ? "Edgeville Dungeon" : 
			name != null && name.equalsIgnoreCase("Chaeldar") ? "Zanaris" : 
			name != null && name.equalsIgnoreCase("Sumona") ? "Pollnivneach" : 
			name != null && name.equalsIgnoreCase("Duradel") ? "Shilo Village" : "Ancient cavern"; // ancient cavern is kuradal
	}

	public static SlayerTask assignBeginnerTask(Player player) {
		SlayerTask s = new SlayerTask();
		player.slayerTask(s);
		s.setPlayer(player);
		SlayerTask task = new SlayerTask(Master.SPRIA, 8, 25);
		player.sm("You've been assigned a slayer task: "+task.taskTotal()+" "+task.monsterName().toLowerCase()+"s.");
		return task;
	}

	public boolean completed() {
		return completed;
	}
	
	public void completed(boolean b) {
		this.completed = b;
	}
}
