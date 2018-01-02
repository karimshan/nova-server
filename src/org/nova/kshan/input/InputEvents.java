package org.nova.kshan.input;

import java.util.HashMap;

import org.nova.game.player.Player;
import org.nova.kshan.input.type.InputEvent;
import org.nova.kshan.input.type.integer.IntegerInput;
import org.nova.kshan.input.type.string.StringInput;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class InputEvents {

	private Player player;
	InputEvent script;

	public InputEvents(Player player) {
		this.player = player;
	}

	public void run(Object key, String message, Object... params) {
		script = (InputEvent) (key instanceof InputEvent ? key : getInputEvent(key));
		if (script == null)
			return;
		script.message = message;
		script.data = params;
		script.setPlayer(player);
		script.sendScript(message);
	}

	public InputEvent current() {
		return script;
	}

	public void handle(Object input) {
		try {
			if (script instanceof StringInput)
				((StringInput) script).process((String) input);
			else if (script instanceof IntegerInput)
				((IntegerInput) script).process((int) input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final HashMap<Object, Class<InputEvent>> inputEvents = new HashMap<Object, Class<InputEvent>>();

	@SuppressWarnings({ "unchecked" })
	public static final void addAll() {
		inputEvents.clear();
		try {
			Class<InputEvent>[] classes = Misc
					.getClasses("org.nova.kshan.input.impl");
			for (Class<InputEvent> c : classes) {
				if (c.isAnonymousClass()) // next
					continue;
				inputEvents.put(c.getSimpleName(), c);
			}
		} catch (Throwable e) {
	    Logger.handle(e);
	}
    }

	public static final void reload() {
		inputEvents.clear();
		addAll();
	}

	public static final InputEvent getInputEvent(Object key) {
		Class<InputEvent> scriptClass = inputEvents.get(key);
		if (scriptClass == null)
			return null;
		try {
			return scriptClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
