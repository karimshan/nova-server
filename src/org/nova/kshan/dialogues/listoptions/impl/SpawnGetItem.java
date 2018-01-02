package org.nova.kshan.dialogues.listoptions.impl;

import java.util.ArrayList;

import org.nova.cache.definition.ItemDefinition;
import org.nova.kshan.dialogues.listoptions.ListOptionsDialogue;

/**
 * A list options dialogue that lets you spawn what you search for
 * with the getid command!
 * 
 * @author K-Shan
 *
 */
public class SpawnGetItem extends ListOptionsDialogue {
	
	@Override
	public void start() {
		if(getList() == null)
			end();
		else
			super.start();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<?> getList() {
		return (ArrayList<Integer>) player.getData().getRuntimeData().get("getidlist");
	}

	@Override
	public String getTitle() {
		return "Which item would you like to spawn?";
	}

	@Override
	public void processStage(int s) {
		Integer e = (Integer) getSelectedElement();
		player.getInventory().addItem(e, 1);
		super.start();
	}
	
	@Override
	public <V> String getEachLine(V value) {
		return ItemDefinition.get((Integer) value).name+" - "+value.toString();
	}
	
}
