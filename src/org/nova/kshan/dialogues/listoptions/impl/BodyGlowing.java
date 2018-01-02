package org.nova.kshan.dialogues.listoptions.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.nova.kshan.content.bodyglow.BodyGlowColor;
import org.nova.kshan.dialogues.listoptions.ListOptionsDialogue;

/**
 * Body glowing dialogue. Choose which body color to set!
 * 
 * @author K-Shan
 *
 */
public class BodyGlowing extends ListOptionsDialogue {
	
	/**
	 * A list containing all of the colors.
	 */
	ArrayList<BodyGlowColor> colors = new ArrayList<BodyGlowColor>();
	
	@Override
	public void start() {
		if(colors.isEmpty())
			addBodyGlowColors();
		super.start();
	}

	@Override
	public ArrayList<?> getList() {
		return colors;
	}

	@Override
	public String getTitle() {
		return "What body color scheme would you like?";
	}

	@Override
	public void processStage(int s) {
		end();
		player.glow(-1, colors.get(currentIndex).getColors());
		player.sm("Body color set: "+colors.get(currentIndex).toString());
	}
	
	/**
	 * Fills up the colors list with colors from the bodyGlowColors file.
	 */
	private void addBodyGlowColors() {
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("./data/playerdata/bodyGlowColors.txt")));
			String line;
			while((line = r.readLine()) != null) {
				if(line.startsWith("#") || line.equals(""))
					continue;
				String[] tokens = line.split(" = ");
				String colorName = tokens[0];
				String[] colorsAsString = tokens[1].split(", ");
				int[] colorsAsInt = new int[4];
				for(int i = 0; i < 4; i++)
					colorsAsInt[i] = Integer.parseInt(colorsAsString[i]);
				colors.add(new BodyGlowColor(colorName, colorsAsInt));
			}
			r.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
