package org.nova.game.npc.others;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

@SuppressWarnings("serial")
public class Jadinko extends NPC {

    public Jadinko(int id, Location tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
	super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
    }

    @Override
    public void sendDeath(Entity source) {
	super.sendDeath(source);
	if (source instanceof Player) {
	    Player player = (Player) source;
	    player.setFavorPoints((getId() == 13820 ? 3 : getId() == 13821 ? 7 : 10) + player.getFavorPoints());
	}
    }
}
