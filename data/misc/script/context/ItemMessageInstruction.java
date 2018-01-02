package org.nova.kshan.script.context;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.kshan.script.ScriptContext;

public class ItemMessageInstruction extends ScriptContext {

	private final int id;
	private final String message;

	public ItemMessageInstruction() {
		this(-1, null);
	}
	
	public ItemMessageInstruction(int id, final String message) {
		super("itemmessage");
		super.setInstant(false);
		this.id = id;
		this.message = message;
	}

	@Override
	public boolean execute(Object... args) {
		Player player = (Player) args[0];
		player.getDialogue().getCurrent().sendItemDialogue(new Item(id, 1), message);
		//player.getDialogue().getCurrent().setStage(); TODO
		return true;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int id = (Integer) params[0];
		String message = (String) params[1];
		ItemMessageInstruction context = new ItemMessageInstruction(id, message);
		context.parameters = params;
		return context;
	}
}