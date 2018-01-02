package org.nova.network.decoders.packets.handlers.objects;

import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.map.GlobalObject;
import org.nova.game.player.Player;
import org.nova.game.player.content.Pickables;
import org.nova.game.player.content.itemactions.Flax;

/**
 * 
 * @author Karimshan Nawaz
 *
 */
public class ObjectOption3 {
	
	/**
	 * 
	 * @param player
	 * @param object
	 */
	public static void process(final Player player, final GlobalObject object) {
		int id = object.getId();
		ObjectDefinition objectDef = object.defs();
		if(!player.getRandomEvent().hasObjectOption3(object))
			return;
		if(!player.getControllerManager().processObjectClick3(object))
			return;
		if(id == 2646) {
			Flax.pickFlax(player, object);
			return;
		}
		if(Pickables.pick(player, object))
			return;
		switch(objectDef.name.toLowerCase()) {
			case "bank booth":
				if (objectDef.containsOption(2, "Collect"))
					player.interfaces().sendInterface(109);
				break;
			case "gate":
			case "metal door":
				if(object.getType() == 0
					&& objectDef.containsOption(2, "Open"))
					Passages.passGate(player, object);
				break;
			case "door":
				if(object.getType() == 0
					&& objectDef.containsOption(2, "Open"))
					Passages.passDoor(player, object);
				break;
			case "ladder":
				if (id == 21512)
					return;
	
				Passages.climbLadder(player, object, 3);
				break;
			case "staircase":
				Passages.navigateStaircase(player, object, 3);
				break;
			default:
				break;
		}
	}

}
