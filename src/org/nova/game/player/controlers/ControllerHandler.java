package org.nova.game.player.controlers;

import java.util.HashMap;

import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

public class ControllerHandler {

	private static final HashMap<Object, Class<Controller>> handledControlers = new HashMap<Object, Class<Controller>>();

	@SuppressWarnings({ "unchecked" })
	public static final void init() {
		handledControlers.clear();
		try {
			Class<Controller>[] classes = Misc
					.getClasses("org.nova.game.player.controlers");
			for (Class<Controller> c : classes) {
				if (c.isAnonymousClass()) // next
					continue;
				handledControlers.put(c.getSimpleName(), c);
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static final void reload() {
		handledControlers.clear();
		init();
	}

	public static final Controller getControler(Object key) {
		if (key instanceof Controller)
			return (Controller) key;
		Class<Controller> classC = handledControlers.get((String) key);
		if (classC == null) {
			return null;
		}
		try {
			return classC.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
