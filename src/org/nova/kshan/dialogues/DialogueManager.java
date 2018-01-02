package org.nova.kshan.dialogues;

import java.util.HashMap;

import org.nova.game.player.Player;
import org.nova.kshan.content.skills.construction.ChangeHouseOptions;
import org.nova.kshan.content.skills.construction.EstateAgentDialogue;
import org.nova.kshan.content.skills.construction.ExpelGuests;
import org.nova.kshan.content.skills.construction.HousePortal;
import org.nova.kshan.dialogues.listoptions.impl.LocationCrystal;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

public class DialogueManager {

	private Player player;
	Dialogue dialogue;

	public DialogueManager(Player player) {
		this.player = player;
	}

	public void start(Object key, final Object... parameters) {
		if (dialogue != null)
			dialogue.finish();
		dialogue = (Dialogue) (key instanceof Dialogue ? key : getDialogue(key));
		if (dialogue == null)
			return;
		dialogue.data = parameters;
		dialogue.setPlayer(player);
		dialogue.start();
	}
	
	public void open(Dialogue dialogue, Object... params) {
		start(dialogue, params);
	}

	public void click(int interfaceId, int componentId) {
		try {
			if (dialogue == null) {
				player.interfaces().closeChatBoxInterface();
				return;
			}
			dialogue.process(interfaceId, componentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishDialogue() {
		if (dialogue == null)
			return;
		dialogue.finish();
		dialogue = null;
		if (player.interfaces().containsChatBoxInter())
			player.interfaces().closeChatBoxInterface();
	}

	private static final HashMap<Object, Class<Dialogue>> dialogues = new HashMap<Object, Class<Dialogue>>();

	@SuppressWarnings({ "unchecked" })
	public static final void addAll() {
		dialogues.clear();
		try {
			Class<Dialogue>[] classes = Misc
					.getClasses("org.nova.kshan.dialogues.impl");
			for (Class<Dialogue> c : classes) {
				if (c.isAnonymousClass())
					continue;
				dialogues.put(c.getSimpleName(), c);
			}
			addOthers();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	private static void addOthers() throws ClassNotFoundException {
		addClass(ChangeHouseOptions.class);
		addClass(HousePortal.class);
		addClass(EstateAgentDialogue.class);
		addClass(ExpelGuests.class);
		addClass(LocationCrystal.class);
	}
	
	@SuppressWarnings("unchecked")
	private static void addClass(Class<?> c) throws ClassNotFoundException {
		dialogues.put(c.getSimpleName(), (Class<Dialogue>) Class.forName(c.getCanonicalName()));
	}

	public static final void reload() {
		dialogues.clear();
		addAll();
	}

	public static final Dialogue getDialogue(Object key) {
		Class<Dialogue> classD = dialogues.get((String) key);
		if (classD == null)
			return null;
		try {
			return classD.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Dialogue getCurrent() {
		return dialogue;
	}

	/**
	 * Sends a quick info message in the chat box as a dialogue.
	 * 
	 * @param lines
	 */
	public void sendMsg(String... lines) {
		player.getDialogue().start("InfoText", (Object) lines);
		
	}

}
