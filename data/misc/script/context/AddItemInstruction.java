package org.nova.kshan.script.context;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.kshan.script.ScriptContext;

public final class AddItemInstruction extends ScriptContext {

	private final int id;
	private final int amount;
	
	public AddItemInstruction() {
		this(-1, -1);
	}
	
	public AddItemInstruction(int id, int amount) {
		super("additem");
		this.id = id;
		this.amount = amount;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int id = 0;
		int amount = 0;
		if (params[0] instanceof Integer) {
			id = (Integer) params[0];
		} 
		if (params[1] instanceof Integer) {
			amount = (Integer) params[1];
		}
		AddItemInstruction context = new AddItemInstruction(id, amount);
		context.parameters = params;
		return context;
	}

	@Override
	public boolean execute(final Object... args) {
		final Player player = (Player) args[0];
		player.getInventory().addItem(new Item(id, amount));
		return true;
	}

}