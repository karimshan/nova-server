package org.nova.kshan.content.cutscenes;

import java.util.HashMap;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class Cutscenes {
	
	private transient Player player;
	private transient Cutscene cutscene;
	private transient HashMap<String, Object> savedData;

	public Cutscenes(Player player) {
		this.player = player;
	}

	public void startCutscene(Object key, final Object... data) {
		if (cutscene != null)
			cutscene.stopCutscene();
		cutscene = (Cutscene) (key instanceof Cutscene ? key : getCutscene(key));
		if (cutscene == null)
			return;
		cutscene.data = data;
		cutscene.setPlayer(player);
		savedData = new HashMap<String, Object>();
		getData().put("previousLocation", new Location(player));
		Game.submit(player, getCutscenesTick());
		cutscene.startCutscene();
	}
	
	public void processPulse() {
		if (cutscene == null)
			return;
		if(cutscene.processPulse())
			return;
		cutscene = null;
	}
	
	public void stopCutscene() {
		if (cutscene == null)
			return;
		cutscene.stopCutscene();
		cutscene = null;
	}

	private static final HashMap<Object, Class<Cutscene>> cutscenes = new HashMap<Object, Class<Cutscene>>();

	@SuppressWarnings({ "unchecked" })
	public static final void addAll() {
		cutscenes.clear();
		try {
			Class<Cutscene>[] classes = Misc.getClasses("org.nova.kshan.content.cutscenes.impl");
			for (Class<Cutscene> c : classes) {
				if (c.isAnonymousClass())
					continue;
				cutscenes.put(c.getSimpleName(), c);
			}
			addOthers();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	private static void addOthers() throws ClassNotFoundException {
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void addClass(Class<?> c) throws ClassNotFoundException {
		cutscenes.put(c.getSimpleName(), (Class<Cutscene>) Class.forName(c.getCanonicalName()));
	}

	public static final void reload() {
		cutscenes.clear();
		addAll();
	}

	public static final Cutscene getCutscene(Object key) {
		Class<Cutscene> c = cutscenes.get((String) key);
		if (c == null)
			return null;
		try {
			return c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Cutscene getCutscene() {
		return cutscene;
	}

	public HashMap<String, Object> getData() {
		return savedData;
	}
	
	public GameTick getCutscenesTick() {
		return new GameTick(getCutscene().getTickDelay()) {

			@Override
			public void run() {
				if(getCutscene() != null)
					processPulse();
				else
					stop();
			}
			
		};
	}
	
}
