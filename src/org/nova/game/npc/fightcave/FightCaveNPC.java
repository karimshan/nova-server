package org.nova.game.npc.fightcave;

import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.utility.misc.Misc;

public class FightCaveNPC extends NPC
{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FightCaveNPC(int id, Location tile)
    {
        super(id, tile, Misc.getNameHash("FightCaves"), false, true);
    }
}