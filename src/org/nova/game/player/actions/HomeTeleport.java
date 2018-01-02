package org.nova.game.player.actions;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.utility.misc.Misc;

public class HomeTeleport extends Action {

        protected static final int HOME_ANIMATION = 1722;
        protected static final int HOME_GRAPHIC = 800;
        protected static final int DONE_ANIMATION = 4852; 

        private int currentTime;
        private Location tile;

        @Override
        public boolean start(final Player player) {
                tile = Constants.RESPAWN_PLAYER_LOCATION;
                if (!player.getControllerManager().processMagicTeleport(tile))
                        return false;
                return process(player);
        }

        @Override
        public int processWithDelay(Player player) {
                player.getWalkSteps().clear();
                if (currentTime++ == 0) {
                        player.setNextAnimation(new Animation(HOME_ANIMATION));
                        player.setNextGraphics(new Graphics(HOME_GRAPHIC));
                } else if (currentTime == 17) {
                        Location teleTile = tile;
                        // attemps to randomize tile by 4x4 area
                        for (int trycount = 0; trycount < 10; trycount++) {
                                teleTile = new Location(tile, 2);
                                if (Game.canMoveNPC(tile.getZ(), teleTile.getX(),
                                                teleTile.getY(), player.getSize()))
                                        break;
                                teleTile = tile;
                        }
                        player.setLocation(teleTile);
                        player.setNextAnimation(new Animation(HOME_ANIMATION+1));
                        player.setNextGraphics(new Graphics(HOME_GRAPHIC+1));
                        player.getControllerManager().magicTeleported(Magic.MAGIC_TELEPORT);
                        if (player.getControllerManager().getControler() == null)
                                Magic.teleControlersCheck(player, teleTile);
                        //return 0;
                } else if (currentTime == 21) {
                        player.setNextAnimation(new Animation(-1));
                        player.setNextGraphics(new Graphics(-1));
                        return -1;
                }
                return 0;
        }

        @Override
        public boolean process(Player player) {
                if (player.getAttackedByDelay() + 10000 > Misc.currentTimeMillis()) {
                        player.packets()
                                        .sendMessage(
                                                        "You can't home teleport until 10 seconds after the end of combat.");
                        return false;
                }
                return true;
        }

        @Override
        public void stop(Player player) {
        }

}