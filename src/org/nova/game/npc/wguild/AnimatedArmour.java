package org.nova.game.npc.wguild;

import org.nova.game.entity.Entity;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;

public class AnimatedArmour extends NPC {

public Player playerCon;

public void setPlayerCon(Player con) {
playerCon = con;
}

public Player getPlayerCon() {
return playerCon;
}

public AnimatedArmour(int id, Location tile, int mapAreaNameHash,
boolean canBeAttackFromOutOfArea, boolean spawned) {
super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
}

@Override
public void sendDeath(Entity source) {
Player other = (Player) source;
super.sendDeath(source);
other.getTemporaryAttributtes().put("killednpc", getTokensForNpcId());
}

public int getTokensForNpcId() {
switch(this.getId()) {
case 4278:
return 20;
case 4279:
return 25;
case 4280:
return 30;
case 4282:
return 35;
case 4283:
return 40;
case 4284:
return 100;
}
return 0;
}

}