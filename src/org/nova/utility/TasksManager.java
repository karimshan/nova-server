package org.nova.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nova.game.item.Item;
import org.nova.game.player.Player;

/**
 * Made by: K-Shan
 */
public class TasksManager {

    private static final Map<String, TasksManager> in = new HashMap<String, TasksManager>();

    private String title;
    private String difficulty;
    private String[] messages;

    public static void loadLDTasks() {
        in.clear();
        try {
            File[] files = new File("./data/misc/tasks/Lumbridge&Draynor").listFiles();
            if (files == null || files.length <= 0)
                return;
            for (File file : files) {
                if (file == null || file.isDirectory())
                    continue;
                BufferedReader br = new BufferedReader(new FileReader(file));
                TasksManager ach = new TasksManager();
                String a = br.readLine();
                String[] lines = a.split(" - ");
                ach.title = lines[0];
                ach.difficulty = lines[2];
                String[] items = lines[4].split(", ");
				Item[] rewards = new Item[items.length / 2];
				int count = 0;
				for (int i = 0; i < rewards.length; i++)
					rewards[i] = new Item(Integer.valueOf(items[count++]), Integer.valueOf(items[count++]));
                String line;
                List<String> messages = new ArrayList<String>();
                while ((line = br.readLine()) != null) {
                	if (line.startsWith("//") || line.equals(""))
    					continue;
                    messages.add(line);
                }
                br.close();
                ach.messages = new String[messages.size()];
                for (int i = 0; i < messages.size(); i++)
                    ach.messages[i] = messages.get(i);
                lDTaskNames.add(ach.title);
                lDAreas.add(lines[1]);
                lDPoints.add(Integer.parseInt(lines[3]));
                lDFileNames.add(file.getName().replaceAll(".txt", "").toLowerCase());
                lDRewards.put(lDFileNames.indexOf(file.getName().replaceAll(".txt", "").toLowerCase()), rewards);
                in.put(file.getName().replaceAll(".txt", "").toLowerCase(), ach);
                messages.clear();
                messages = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean display(Player player, String a) {
        TasksManager achieve = in.get(a.toLowerCase());
        if (achieve == null)
            return false;
        if(achieve.difficulty.toLowerCase().equalsIgnoreCase("easy"))
        	achieve.difficulty = "<col=00ff00>Easy</col>";
        else if(achieve.difficulty.toLowerCase().equalsIgnoreCase("medium"))
        	achieve.difficulty = "<col=ffff00>Medium</col>";
        else if(achieve.difficulty.toLowerCase().equalsIgnoreCase("hard"))
        	achieve.difficulty = "<col=c12006>Hard</col>";
        if(achieve.difficulty.toLowerCase().equalsIgnoreCase("elite"))
        	achieve.difficulty = "<col=ff0000>Elite</col>";
        player.tasks().information(achieve.title, achieve.difficulty, achieve.messages);
        return true;
    }
    
    /**
     * All information below is used to make adding tasks easier.
     */
    public static List<String> lDTaskNames = new ArrayList<String>();
    public static List<String> lDFileNames = new ArrayList<String>();
    public static Map<Integer, Item[]> lDRewards = new HashMap<Integer, Item[]>();
    public static List<Integer> lDPoints = new ArrayList<Integer>();
    public static List<String> lDAreas = new ArrayList<String>();

	public static void clearAll() {
		in.clear();
		lDTaskNames.clear();
		lDFileNames.clear();
		lDRewards.clear();
		lDPoints.clear();
		lDAreas.clear();
	}
    
}
