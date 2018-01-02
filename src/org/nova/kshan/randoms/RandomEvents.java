package org.nova.kshan.randoms;

import java.util.HashMap;

import org.nova.game.player.content.minigames.duel.DuelArena;
import org.nova.kshan.content.minigames.Zombies;
import org.nova.kshan.content.skills.construction.EnterHouse;
import org.nova.utility.misc.Misc;

/**
 * Represents the random events
 * 
 * @author Karimshan Nawaz
 *
 */
public class RandomEvents {
	
	public static final HashMap<Object, Class<RandomEvent>> randomEvents = new HashMap<Object, Class<RandomEvent>>();

	@SuppressWarnings({ "unchecked" })
	public static final void addAll() {
		randomEvents.clear();
		try {
			Class<RandomEvent>[] classes = Misc.getClasses("org.nova.kshan.randoms.impl");
			for (Class<RandomEvent> c : classes) {
				if (c.isAnonymousClass())
					continue;
				randomEvents.put(c.getSimpleName(), c);
			}
			addOthers();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void addOthers() throws ClassNotFoundException {
		addClass(EnterHouse.class);
		addClass(DuelArena.class);
		addClass(Zombies.class);
	}
	
	@SuppressWarnings("unchecked")
	private static void addClass(Class<?> c) throws ClassNotFoundException {
		randomEvents.put(c.getSimpleName(), (Class<RandomEvent>) Class.forName(c.getCanonicalName()));
	}

	public static final void reload() {
		randomEvents.clear();
		addAll();
	}

	public static final RandomEvent getRandomEvent(Object o) {
		Class<RandomEvent> classC = randomEvents.get(o);
		if (classC == null)
			return null;
		try {
			return classC.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

}
