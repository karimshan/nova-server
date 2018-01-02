package org.nova.game.player.content;

import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.content.minigames.clanwars.FfaZone;
import org.nova.game.player.content.minigames.clanwars.RequestController;

/**
 * 
 * @author Fuzen Seth
 * @information Holds all world-wide teleportations.
 * @since 21.6.2014
 */
public class WorldTeleport {
	
	/**
	 * The singleton.
	 */
	private static final WorldTeleport singleton = new WorldTeleport();
	/**
	 * Holds the worldtile.
	 */
	private Location nextTile;
	/**
	 * Destination.
	 */
	private String destination;
	/**
	 * Does the teleport
	 * @param player
	 */
	public void locateTeleportDestination(Player player) {
		switch (getDestination()) {
		case "crabs":
			setNextTile(new Location(2418, 3847, 0));
			//player.getControlerManager().startControler("CrabControler", player);
			break;
		case "west_greens":
			player.getControllerManager().startController("Wilderness", player);
			setNextTile(new Location(2989, 3611, 0));
			break;
		case "east_greens":
			player.getControllerManager().startController("Wilderness", player);
			setNextTile(new Location(3363,3684,0));
			break;
		case "revenants":
			player.getControllerManager().startController("Wilderness", player);
			setNextTile(new Location(3071,3638,0));
			break;
		case "castlewars_ogres":
			setNextTile(new Location(2491,3086,0));
			break;
		case "grand_tree":
			setNextTile(new Location(2466,3496,1));
			break;
		case "baxtorian_falls":
			setNextTile(new Location(2520, 3495, 0));
			break;
		case "ardougne":
			setNextTile(new Location(2665,3304,0));
			break;
		case "karamja_dungeon":
			setNextTile(new Location(2854,3166,0));
			break;
		case "edge_hills":
			setNextTile(new Location(3114,9848,0));
			break;
			case "moss_giants_varrock":
				setNextTile(new Location(3168,9893,0));
				break;
			case "rock_cavern":
				setNextTile(new Location(3014,9829,0));
				break;
		case "clan_wars":
			setNextTile(new Location(2984,9677,0));
			 if (RequestController.inWarRequest(player))
			    player.getControllerManager().startController("clan_wars_request");
			else if (FfaZone.inArea(player))
			    player.getControllerManager().startController("clan_wars_ffa");
			break;
		case "varrock_dungeon":
			setNextTile(new Location(3237,9859,0));
			break;
		case "relleka_slay_dung":
			setNextTile(new Location(2804,10003,0));
			break;
		case "ancient_cavern":
			setNextTile(new Location(1768,5366,1));
			break;
		case "jadinko_lair":
			setNextTile(new Location(3025,9226,0));
			break;
		case "living_rock_caverns":
			setNextTile(new Location(3016,9832,0));
			break;
		case "dwarven_mine":
			setNextTile(new Location(3015,3447,0));
			break;
		case "bandit_camp":
			setNextTile(new Location(3173,2981,0));
			break;
		case "kalphite_hive":
			setNextTile(new Location(3485,9510,2));
			break;
		case "mole_hole":
			setNextTile(new Location(34855,9510,2));
			break;
		case "taverley_dungeon":
			setNextTile(new Location(2883,9799,0));
			break;
		case "brimhaven_dungeon":
			setNextTile(new Location(2744,3148,0));
			break;	
			case "monastery_monks":
				setNextTile(new Location(3053,3468,0));
				break;
		case "asgarnia_dung":
			setNextTile(new Location(3011,3153,0));
			break;
		case "wizard_tower":
			setNextTile(new Location(3114,3171,0));
			break;
		case "canifis":
			setNextTile(new Location(2883,9799,0));
			break;
		case "slayer_tower":
			setNextTile(new Location(3429,3539,0));
			break;
		case "artisan":
			setNextTile(new Location(3033,3339,0));
			break;
		case "duel_arena":
			setNextTile(new Location(3315,3234,0));
			break;
		case "castlewars":
			setNextTile(new Location(2442,3088,0));
			break;
		case "relleka_crabs":
			setNextTile(new Location(2687,3715,0));
			break;
		}
		TeleportManager.wiseOldManTeleport(player, 0,0, getNextTile());
	}
	
	public static WorldTeleport getSingleton() {
		return singleton;
	}
	
	public String getDestination() {
		return destination;
	}
	
	public void setDestination(Player player, String destination) {
		this.destination = destination;
	locateTeleportDestination(player);
	player.interfaces().closeChatBoxInterface();
	}

	public Location getNextTile() {
		return nextTile;
	}

	public void setNextTile(Location nextTile) {
		this.nextTile = nextTile;
	}
	
}
