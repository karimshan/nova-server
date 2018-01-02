package org.nova.game.player.controlers;

import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.player.Player;

public class GodWars extends Controller {

	@Override
	public void start() {
		if (!player.sentInter) {
			player.interfaces().sendTab(player.interfaces().isFullScreen() ? 1 : 10, 601);
			player.sentInter = true;
		}
		setArguments(new Object[] { 0, 0, 0, 0, 0, 0 });
		sendInterfaces();
	}

	public GodWars() {
		
	}

	@Override
	public boolean logout() {
		return false; // so doesnt remove script
	}

	@Override
	public boolean login() {
		sendInterfaces();
		return false; // so doesnt remove script
	}

	public static boolean isInsideBandosRoom(Player player) {
		return (player.getX() >= 2863 && player.getY() <= 5370 && player.getX() <= 2878 && player.getY() >= 5350);
	}

	public static boolean isInsideArmadylRoom(Player player) {
		return (player.getX() == 2835 && player.getY() == 5295);
	}

	public static boolean isInsideSaradominRoom(Player player) {
		return (player.getX() >= 2913 && player.getY() <= 5256 && player.getX() <= 2933 && player.getY() >= 5241);
	}

	public static boolean isInsideZamorakRoom(Player player) {
		return (player.getX() >= 2917 && player.getY() <= 5332 && player.getX() <= 2936 && player.getY() >= 5318);
	}

	public static boolean isOnOtherSideBridge(Player player) {
		return (player.getX() >= 2884 && player.getY() <= 5348 && player.getX() <= 2886 && player.getY() >= 5344);
	}

	public static boolean isFirstWall(Player player) {
		return (player.getX() >= 2908 && player.getY() <= 5303 && player.getX() <= 2917 && player.getY() >= 5294);
	}

	public static boolean isDown(Player player) {
		return (player.getX() >= 2915 && player.getY() <= 5300 && player.getX() <= 2916 && player.getY() >= 5296);
	}

	public static boolean isSecondWall(Player player) {
		return (player.getX() >= 2918 && player.getY() <= 5281 && player.getX() <= 2922 && player.getY() >= 5274);
	}

	public static boolean isDown2(Player player) {
		return (player.getX() >= 2916 && player.getY() <= 5276 && player.getX() <= 2921 && player.getY() >= 5273);
	}

	public static boolean isOnNorthSidePiller(Player player) {
		return (player.getX() == 2872 && player.getY() == 5280);
	}

	public static boolean isOnSouthSidePiller(Player player) {
		return (player.getX() == 2872 && player.getY() == 5272);
	}

	@Override
	public boolean processObjectClick1(final GlobalObject object) {
		if (object.getId() == 26341) {
			player.setLocation(new Location(882, 5311, 0));
			player.packets().sendMessage("You descend down the hole.");
		}
		if (object.getId() == 26293) {
			player.packets().sendMessage("You climb up the rope...");
			player.setLocation(new Location(2918, 3742, 2));
		}
		if (object.getId() == 26384) {
			if (player.BandosKills < 40 && player.getX() >= 2851) {
				player.packets().sendMessage("You need to kill at least 40 Bandos minions to enter.");
				return false;
			}
			if (player.BandosKills >= 40) {
				if (player.getX() == 2851 && player.getY() == 5334 || player.getX() == 2852 && player.getY() == 5334) {
					player.setLocation(new Location(2850, 5334, 2));
					player.packets().sendMessage("You enter through the door and your killcount reduces by 40.");
					player.BandosKills = 0;
					refresh(player);
				}
			}
			if (player.getX() == 2850 || player.getX() == 2851) {
				player.setLocation(new Location(2851, 5334, 2));
				player.packets().sendMessage("You enter through the door.");
			}
		}
		if (object.getId() == 75463) {
			if (player.ArmadylKills < 40 && player.getY() <= 5272) {
				player.packets().sendMessage("You need to kill at least 40 Armadyl minions to cross.");
				return false;
			}
			if (player.ArmadylKills >= 40) {
				if (isOnNorthSidePiller(player)) {
					player.packets().sendMessage("You move to the other side and your killcount has reduced by 40.");
					player.setLocation(new Location(2872, 5272, 2));
					player.ArmadylKills -= 40;
					refresh(player);
				}
			}
			if (!isOnSouthSidePiller(player)) {
				player.packets().sendMessage("You move to the other side.");
				player.setLocation(new Location(2872, 5280, 2));
			}
		}
		if (object.getId() == 26439) {
			if (player.ZamorakKills < 40 && player.getY() <= 5338) {
				player.packets().sendMessage("You need to kill at least 40 Zamorak minions to cross.");
				return false;
			}
			if (player.ZamorakKills >= 40) {
				if (!isOnOtherSideBridge(player)) {
					player.setLocation(new Location(2886, 5344, 2));
					player.packets().sendMessage("You cross the bridge and your killcount has reduced by 40.");
					player.ZamorakKills = 0;
					refresh(player);
				}
			}
			if (isOnOtherSideBridge(player)) {
				player.setLocation(new Location(2886, 5338, 0));
				player.packets().sendMessage("You cross the bridge.");
			}
		}
		if (object.getId() == 75462) {
			if (isFirstWall(player)) {
				if (player.SaradominKills < 40 && player.getX() == 2911) {
					player.packets().sendMessage("You need to kill at least 40 Saradomin minions to climb this.");
					return false;
				}
				if (player.SaradominKills >= 40) {
					if (!isDown(player)) {
						player.setLocation(new Location(2915, 5298, 0));
						player.packets().sendMessage("You jump down and your killcount has reduced by 40.");
						player.SaradominKills -= 40;
						refresh(player);
					}
				}
				if (isDown(player)) {
					player.setLocation(new Location(2911, 5299, 2));
					player.packets().sendMessage("You climb up.");
				}
			}
			if (isSecondWall(player)) {
				if (isDown2(player)) {
					player.setLocation(new Location(2920, 5279, 2));
					player.packets().sendMessage("You climb up.");
				} else if (!isDown2(player)) {
					player.setLocation(new Location(2919, 5275, 0));
					player.packets().sendMessage("You jump down.");
				}
			}
		}
		if (object.getId() == 26445) {
			player.setLocation(new Location(2920, 5274, 0));
			player.packets().sendMessage("You jump off.");
		}
		if (object.getId() == 26427) {
			if (isInsideSaradominRoom(player)) {
				player.setLocation(new Location(2923, 5257, 0));
				player.packets().sendMessage("You exit the room.");
			} else {
				player.setLocation(new Location(2923, 5256, 0));
				player.packets().sendMessage("You enter the room.");
			}
		}
		if (object.getId() == 26428) {
			if (isInsideZamorakRoom(player)) {
				player.packets().sendMessage("You exit the room.");
				player.setLocation(new Location(2925, 5333, 2));
			} else {
				player.packets().sendMessage("You enter the room.");
				player.setLocation(new Location(2925, 5332, 2));
			}
		}
		if (object.getId() == 26425) {
			if (!isInsideBandosRoom(player)) {
				player.packets().sendMessage("You enter the room.");
				player.setLocation(new Location(2863, 5357, 2));
			} else {
				player.packets().sendMessage("You exit the room.");
				player.setLocation(new Location(2862, 5357, 2));
			}
		}
		if (object.getId() == 26426) {
			if (!isInsideArmadylRoom(player)) {
				player.packets().sendMessage("You enter the room.");
				player.setLocation(new Location(2835, 5295, 2));
			} else {
				player.packets().sendMessage("You exit the room.");
				player.setLocation(new Location(2835, 5294, 2));
			}
		}
		if (object.getId() == 57225) {
			player.getMatrixDialogues().startDialogue("NexEntrance");
			return true;
		}
		return false;
	}

	@Override
	public void sendInterfaces() {
		player.interfaces().sendTab(player.interfaces().isFullScreen() ? 34 : 8, getInterface());
		player.packets().sendIComponentText(601, 8, "" + player.ArmadylKills);
		player.packets().sendIComponentText(601, 9, "" + player.BandosKills);
		player.packets().sendIComponentText(601, 10, "" + player.SaradominKills);
		player.packets().sendIComponentText(601, 11, "" + player.ZamorakKills);
	}

	public int getInterface() {
		switch ((Integer) getArguments()[0]) {
		case 1: // zamorak area
			return 599;
		case 2:// zamorak boss area
			return 598;
		default:
			return 601;
		}
	}

	@Override
	public boolean sendDeath() {
		remove();
		removeControler();
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		remove();
		removeControler();
	}

	public void remove() {
		player.packets().closeInterface(player.interfaces().isFullScreen() ? 34 : 8);
	}

	public static void refresh(Player player) {
		player.packets().sendIComponentText(601, 8, "" + player.ArmadylKills);
		player.packets().sendIComponentText(601, 9, "" + player.BandosKills);
		player.packets().sendIComponentText(601, 10, "" + player.SaradominKills);
		player.packets().sendIComponentText(601, 11, "" + player.ZamorakKills);
	}

}