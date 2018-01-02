package org.nova.kshan.script.context;

import org.nova.game.entity.Entity;
import org.nova.kshan.script.ScriptContext;

public final class CheckRandomCondition extends ScriptContext {

	private final int value;
	
	public CheckRandomCondition() {
		this(0);
	}
	
	public CheckRandomCondition(int value) {
		super("israndom");
		this.value = value;
	}

	@Override
	public ScriptContext parse(Object... params) {
		int value = 0;
		if (params[0] instanceof Integer) {
			value = (Integer) params[0];
		}
		CheckRandomCondition context = new CheckRandomCondition(value);
		context.parameters = params;
		return context;
	}

	@Override
	public boolean execute(Object... args) {
		System.out.println("Random attributte: "+((Entity) args[0]).getTemporaryAttributtes().get("random"));
		return ((Entity) args[0]).getTemporaryAttributtes().get("random") == Integer.valueOf(value);
	}
	
	public int getValue() {
		return value;
	}

}