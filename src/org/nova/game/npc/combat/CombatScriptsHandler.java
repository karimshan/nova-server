package org.nova.game.npc.combat;

import java.util.HashMap;

import org.nova.game.entity.Entity;
import org.nova.game.npc.NPC;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

public class CombatScriptsHandler {

	private static final HashMap<Object, CombatScript> cachedCombatScripts = new HashMap<Object, CombatScript>();
	private static final CombatScript DEFAULT_SCRIPT = new Default();

	@SuppressWarnings("rawtypes")
	public static final void init() {
		cachedCombatScripts.clear();
		try {
			Class[] classes = Misc.getClasses("org.nova.game.npc.combat.impl");
			for (Class c : classes) {
				if (c.isAnonymousClass()) // next
					continue;
				Object o = c.newInstance();
				if (!(o instanceof CombatScript))
					continue;
				CombatScript script = (CombatScript) o;
				for (Object key : script.getKeys())
					cachedCombatScripts.put(key, script);
			}
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}

	public static int specialAttack(final NPC npc, final Entity target) {
		CombatScript script = cachedCombatScripts.get(npc.getId());
		if (script == null) {
			script = cachedCombatScripts.get(npc.defs().name);
			if (script == null)
				script = DEFAULT_SCRIPT;
		}
		return script.attack(npc, target);
	}
}
