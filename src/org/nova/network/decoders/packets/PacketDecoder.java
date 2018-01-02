package org.nova.network.decoders.packets;

import java.awt.event.KeyEvent;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.game.npc.familiar.Familiar;
import org.nova.game.npc.familiar.Familiar.SpecialAttack;
import org.nova.game.player.CoordsEvent;
import org.nova.game.player.Inventory;
import org.nova.game.player.LogicPacket;
import org.nova.game.player.Player;
import org.nova.game.player.PublicChatMessage;
import org.nova.game.player.QuickChatMessage;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.PlayerCombat;
import org.nova.game.player.actions.PlayerFollow;
import org.nova.game.player.content.DevelopmentMode;
import org.nova.game.player.content.FriendChatsManager;
import org.nova.game.player.content.Magic;
import org.nova.game.player.content.SkillCapeCustomizer;
import org.nova.game.player.content.Trade;
import org.nova.game.player.content.Trade.TradeState;
import org.nova.kshan.bot.Bot;
import org.nova.kshan.bot.BotList;
import org.nova.kshan.content.interfaces.WidgetToolTips;
import org.nova.kshan.content.minigames.duelarena.Duel;
import org.nova.kshan.content.skills.magic.MagicOnGroundPacketHandler;
import org.nova.kshan.content.skills.magic.MagicOnPlayerPacketHandler;
import org.nova.kshan.utilities.Logs;
import org.nova.kshan.utilities.Punishments;
import org.nova.kshan.utilities.StringUtils;
import org.nova.kshan.utilities.TimeUtils;
import org.nova.network.Session;
import org.nova.network.decoders.Decoder;
import org.nova.network.decoders.packets.handlers.ButtonPackets;
import org.nova.network.decoders.packets.handlers.commands.Commands;
import org.nova.network.decoders.packets.handlers.items.InventoryActionHandler;
import org.nova.network.decoders.packets.handlers.items.ItemOnNPC;
import org.nova.network.decoders.packets.handlers.items.ItemOnObject;
import org.nova.network.decoders.packets.handlers.items.ItemOnPlayer;
import org.nova.network.decoders.packets.handlers.npcs.NPCActionPacketHandler;
import org.nova.network.decoders.packets.handlers.objects.ObjectPackets;
import org.nova.network.stream.InputStream;
import org.nova.utility.huffman.Huffman;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

@SuppressWarnings("unused")
public final class PacketDecoder extends Decoder {

	private static final byte[] PACKET_SIZES = new byte[256];

	private final static int ACCEPT_TRADE_CHAT_PACKET = 46;
	private final static int PLAYER_TRADE_OPTION_PACKET = 77;
	private final static int WALKING_PACKET = 12;
	private final static int MINI_WALKING_PACKET = 83;
	private final static int AFK_PACKET = -1;
	public final static int ACTION_BUTTON1_PACKET = 61;
	private final static int ITEM_EXAMINE_PACKET = 27;
	private final static int SWITCH_CLIENT_PACKET = 93;
	private final static int CLAN_FORUM_THREAD_PACKET = 74;
	public final static int ACTION_BUTTON2_PACKET = 64;
	public final static int ACTION_BUTTON3_PACKET = 4;
	public final static int ACTION_BUTTON4_PACKET = 52;
	public final static int ACTION_BUTTON5_PACKET = 81;
	public final static int ACTION_BUTTON6_PACKET = 18;
	public final static int ACTION_BUTTON7_PACKET = 10;
	public final static int MINIMIZE_PACKET = 93;
	public final static int ACTION_BUTTON8_PACKET = 25;
	public final static int ACTION_BUTTON9_PACKET = 91;
	private final static int ENTER_STRING_PACKET = 7;
	public final static int ACTION_BUTTON10_PACKET = 20;
	public final static int RECEIVE_PACKET_COUNT_PACKET = 15;
	private final static int MAGIC_ON_ITEM_PACKET = -1;
	private final static int MOVE_CAMERA_PACKET = 5;
	private final static int MAGIC_ON_GROUND_PACKET = -1;
	private final static int UNIDENTIFIED_PACKET = 85;
	private final static int CLICK_PACKET = 84;
	private final static int AFK_CLIENT_PACKET = 93;
	private final static int MOVE_MOUSE_PACKET = 29;
	private final static int KEY_TYPED_PACKET = 68;
	private final static int CLOSE_INTERFACE_PACKET = 56;
	private final static int COMMANDS_PACKET = 70;
	private final static int ITEM_ON_ITEM_PACKET = 73;
	private final static int IN_OUT_SCREEN_PACKET = 75;
	private final static int SWITCH_DETAIL = 4;
	private final static int DONE_LOADING_REGION = 33;
	private final static int PING_PACKET = 16;
	private final static int SCREEN_PACKET = 87;
	private final static int CHAT_TYPE_PACKET = 23;
	private final static int CHAT_PACKET = 36;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 30;
	private final static int ADD_FRIEND_PACKET = 51;
	private final static int JOIN_FRIEND_CHAT_PACKET = 1;
	private final static int CHANGE_FRIEND_CHAT_PACKET = 41;
	private final static int KICK_FRIEND_CHAT_PACKET = 32;
	private final static int REMOVE_FRIEND_PACKET = 8;
	private final static int SEND_FRIEND_MESSAGE_PACKET = 72;
	private final static int SEND_FRIEND_QUICK_CHAT_PACKET = 79;
	private final static int OBJECT_CLICK1_PACKET = 11;
	private final static int OBJECT_CLICK2_PACKET = 2;
	private final static int OBJECT_CLICK3_PACKET = 76;
	private final static int OBJECT_CLICK5_PACKET = 80;
	private final static int OBJECT_EXAMINE_PACKET = 47;
	private final static int NPC_CLICK1_PACKET = 9;
	private final static int NPC_CLICK2_PACKET = 31;
	private final static int ATTACK_NPC = 66;
	private final static int PLAYER_OPTION_1_PACKET = 14;
	private final static int PLAYER_OPTION_2_PACKET = 53;
	private final static int ITEM_TAKE_PACKET = 24;
	private final static int DIALOGUE_CONTINUE_PACKET = 54;
	private final static int ENTER_INTEGER_PACKET = 3;
	private final static int SHORT_STRING_PACKET = 59;
	private final static int SWITCH_INTERFACE_ITEM_PACKET = 26;
	private final static int INTERFACE_ON_PLAYER = 40;
	private final static int INTERFACE_ON_NPC = 65;
	private final static int ITEM_ON_OBJECT_PACKET = 42;
	private final static int COLOR_ID_PACKET = 22;
	private static final int NPC_CLICK3_PACKET = 34;
	private static final int OBJECT_OPTION4 = 69;
	private static final int INTERFACE_MOUSE_HOVERING_PACKET = 148;
	private static final int ADVANCED_KEY_TYPED_PACKET = 149;

	static {
		loadPacketSizes();
	}

	public static void loadPacketSizes() {
		for (int id = 0; id < 256; id++)
			PACKET_SIZES[id] = -4;
		PACKET_SIZES[64] = 8;
		PACKET_SIZES[18] = 8;
		PACKET_SIZES[25] = 8;
		PACKET_SIZES[41] = -1;
		PACKET_SIZES[14] = 3;
		PACKET_SIZES[46] = 3;
		PACKET_SIZES[87] = 6;
		PACKET_SIZES[47] = 9;
		PACKET_SIZES[57] = 3;
		PACKET_SIZES[67] = 3;
		PACKET_SIZES[91] = 8;
		PACKET_SIZES[24] = 7;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[40] = 11;
		PACKET_SIZES[36] = -1;
		PACKET_SIZES[74] = -1;
		PACKET_SIZES[31] = 3;
		PACKET_SIZES[54] = 6;
		PACKET_SIZES[12] = -1;
		PACKET_SIZES[23] = 1;
		PACKET_SIZES[9] = 3;
		PACKET_SIZES[17] = -1;
		PACKET_SIZES[44] = -1;
		PACKET_SIZES[88] = -1;
		PACKET_SIZES[42] = 17;
		PACKET_SIZES[49] = 3;
		PACKET_SIZES[21] = 15;
		PACKET_SIZES[59] = -1;
		PACKET_SIZES[37] = -1;
		PACKET_SIZES[6] = 8;
		PACKET_SIZES[55] = 7;
		PACKET_SIZES[69] = 9;
		PACKET_SIZES[26] = 16;
		PACKET_SIZES[39] = 12;
		PACKET_SIZES[71] = 4;
		PACKET_SIZES[22] = 2;
		PACKET_SIZES[32] = -1;
		PACKET_SIZES[79] = -1;
		PACKET_SIZES[89] = 4;
		PACKET_SIZES[90] = -1;
		PACKET_SIZES[15] = 4;
		PACKET_SIZES[72] = -2;
		PACKET_SIZES[20] = 8;
		PACKET_SIZES[92] = 3;
		PACKET_SIZES[82] = 3;
		PACKET_SIZES[28] = 3;
		PACKET_SIZES[81] = 8;
		PACKET_SIZES[7] = -1;
		PACKET_SIZES[4] = 8;
		PACKET_SIZES[60] = -1;
		PACKET_SIZES[13] = 2;
		PACKET_SIZES[52] = 8;
		PACKET_SIZES[65] = 11;
		PACKET_SIZES[85] = 2;
		PACKET_SIZES[86] = 7;
		PACKET_SIZES[78] = -1;
		PACKET_SIZES[83] = -1;
		PACKET_SIZES[27] = 7;
		PACKET_SIZES[2] = 9;
		PACKET_SIZES[93] = 1;
		PACKET_SIZES[70] = -1;
		PACKET_SIZES[1] = -1;
		PACKET_SIZES[8] = -1;
		PACKET_SIZES[11] = 9;
		PACKET_SIZES[0] = 9;
		PACKET_SIZES[51] = -1;
		PACKET_SIZES[5] = 4;
		PACKET_SIZES[45] = 7;
		PACKET_SIZES[75] = 4;
		PACKET_SIZES[53] = 3;
		PACKET_SIZES[33] = 0;
		PACKET_SIZES[50] = 3;
		PACKET_SIZES[76] = 9;
		PACKET_SIZES[80] = -1;
		PACKET_SIZES[77] = 3;
		PACKET_SIZES[68] = -1;
		PACKET_SIZES[43] = 3;
		PACKET_SIZES[30] = -1;
		PACKET_SIZES[19] = 3;
		PACKET_SIZES[16] = 0;
		PACKET_SIZES[34] = 3;
		PACKET_SIZES[ENTER_STRING_PACKET] = -1;
		PACKET_SIZES[56] = 0;
		PACKET_SIZES[58] = 2;
		PACKET_SIZES[10] = 8;
		PACKET_SIZES[35] = 7;
		PACKET_SIZES[84] = 6;
		PACKET_SIZES[66] = 3;
		PACKET_SIZES[61] = 8;
		PACKET_SIZES[29] = -1;
		PACKET_SIZES[62] = 3;
		PACKET_SIZES[3] = 4;
		PACKET_SIZES[63] = 4;
		PACKET_SIZES[73] = 16;
		PACKET_SIZES[38] = -1;
		PACKET_SIZES[INTERFACE_MOUSE_HOVERING_PACKET] = 12;
		PACKET_SIZES[ADVANCED_KEY_TYPED_PACKET] = 18;
	}

	private Player player;
	private int chatType;

	public PacketDecoder(Session session, Player player) {
		super(session);
		this.player = player;
	}

	@Override
	public void decode(InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected()
				&& !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if (packetId >= PACKET_SIZES.length) {
				System.out.println("PacketId " + packetId
						+ " has fake packet id.");
				break;
			}
			switch (packetId) {
			case 13:
				int id = stream.readShortl();
				Game.getGrandExchange().buyItem(player, id);
				break;
			}
			int length = PACKET_SIZES[packetId];
			if (length == -1)
				length = stream.readUnsignedByte();
			else if (length == -2)
				length = stream.readUnsignedShort();
			else if (length == -3)
				length = stream.readInt();
			else if (length == -4) {
				length = stream.getRemaining();
				if(packetId != 255)
				System.out.println("Invalid size for PacketId " + packetId
						+ ". Size guessed to be " + length);
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if(packetId != 0)
				System.out.println("PacketId " + packetId
						+ " has fake size. - expected size " + length);
				break;

			}
			int startOffset = stream.getOffset();
			processPackets(packetId, stream, length);
			stream.setOffset(startOffset + length);
		}
	}

	public static void decodeLogicPacket(final Player player, LogicPacket packet) {
		int packetId = packet.getId();
		InputStream stream = new InputStream(packet.getData());

		if (packetId == WALKING_PACKET || packetId == MINI_WALKING_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.cantMove() || !player.getRandomEvent().canWalk())
				return;
			long currentTime = Misc.currentTimeMillis();
			if (player.getStopDelay() > currentTime)
				return;
			if (player.getTrade() == null) {
				player.closeInterfaces();
				if(!player.interfaces().chatboxKeptOpen())
					player.interfaces().closeChatBoxInterface();
				player.interfaces().closeScreenInterface();
				player.interfaces().closeInventoryInterface();
				player.setNextFaceEntity(null);
			}
			if (player.getFreezeDelay() >= currentTime) {
				player.packets().sendMessage(
						"A magical force prevents you from moving.");
				/*if (player.isTransformed()
						&& !SpecialAreaHandler.isInApeToll(player))
					ApeToll.untransformPlayer(player);*/
				if(!player.interfaces().chatboxKeptOpen())
					player.interfaces().closeChatBoxInterface();
				if (player.getRights() == 2 && player.isDeveloperMode()) {
					DevelopmentMode.updatePosition(player);
					return;
				}
				return;
			} else if (packetId == AFK_CLIENT_PACKET) {
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.getSession().getChannel().close();
					}
				}, 300, 300);
			}
			if (packetId == 139) {
				int id = packet.readShort();
				Game.getGrandExchange().buyItem(player, id);
			}
			int length = stream.getLength();
			switch (packetId) {
			case 13:
				int id = stream.readShortl();
				Game.getGrandExchange().buyItem(player, id);
				break;
			}
			if (packetId == MINI_WALKING_PACKET)
				length -= 13;
			int baseX = stream.readUnsignedShortLE128();
			int baseY = stream.readUnsignedShortLE128();
			stream.readByte();
			int steps = (length - 5) / 2;
			if (steps > 25)
				steps = 25;
			if (player.getTrade() != null && steps > 0) {
				Player other = Game.getPlayers().get(
						(Integer) player.getTemporaryAttributtes().get(
								"tradingWith"));
				if (other.getTrade() != null)
					player.getTrade().tradeFailed();
			}
			player.stopAll();
			for (int step = 0; step < steps; step++)
				// dynamic part temporary fix
				if (!player.addWalkSteps(baseX + stream.readUnsignedByte(),
						baseY + stream.readUnsignedByte(), -1,
						!player.isAtDynamicRegion()))
					break;
		} else if (packetId == PLAYER_OPTION_2_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = Game.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getStopDelay() > Misc.currentTimeMillis())
				return;
			player.stopAll(false);
			player.getActionManager().setSkill(new PlayerFollow(p2));
		} else if (packetId == PLAYER_OPTION_1_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;

			boolean unknown = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player p2 = Game.getPlayers().get(playerIndex);
			if (p2 == null || p2.isDead() || p2.hasFinished()
					|| !player.getMapRegionsIds().contains(p2.getRegionId()))
				return;
			if (player.getStopDelay() > Misc.currentTimeMillis()
					|| !player.getControllerManager().canPlayerOption1(p2))
				return;
			if (!player.isCanPvp())
				return;
			if (!player.getControllerManager().canAttack(p2))
				return;
			if (!player.getRandomEvent().canAttackEntity(p2))
				return;
			if (!player.isCanPvp() || !p2.isCanPvp()) {
				player.packets().sendMessage("You can only attack players in a player-vs-player area.");
				return;
			}
			if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
				if (player.getAttackedBy() != p2
						&& player.getAttackedByDelay() > Misc
								.currentTimeMillis()) {
					player.packets().sendMessage("You're already in combat.");
					return;
				}
				if (p2.getAttackedBy() != player
						&& p2.getAttackedByDelay() > Misc.currentTimeMillis()) {
					if (p2.getAttackedBy() instanceof NPC) {
						p2.setAttackedBy(player); // changes enemy to player, player has priority over npc on single areas
					} else {
						player.packets().sendMessage("That player is already in combat.");
						return;
					}
				}
			}
			if(player.inArea("Duel Arena")) {
				Player requesting = (Player) player.getData().getRuntimeData().get("requestingDuel");
				if(requesting == null || !requesting.withinDistance(player, 8))
					requesting = p2;
				if(requesting == null || !requesting.withinDistance(player, 8)) {
					player.sm("That player is not within your general vicinity.");
					return;
				}
				Duel.sendDuelStart(player, requesting);
				return;
			}
			player.stopAll(false);
			player.getActionManager().setSkill(new PlayerCombat(p2));

		} else if (packetId == ATTACK_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.getStopDelay() > Misc.currentTimeMillis())
				return;

			boolean unknown = stream.readByte128() == 1;
			int npcIndex = stream.readUnsignedShort128();
			NPC npc = Game.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId())
					|| !npc.defs().hasAttackOption())
				return;
			if (!player.getControllerManager().canAttack(npc))
				return;
			if (!player.getRandomEvent().canAttackEntity(npc))
				return;
			if (npc instanceof Familiar) {
				Familiar familiar = (Familiar) npc;
				if (familiar == player.getFamiliar()) {
					player.packets().sendMessage(
							"You can't attack your own familiar.");
					return;
				}
				if (!familiar.canAttack(player)) {
					player.packets().sendMessage("You can't attack this npc.");
					return;
				}
			} else if (!npc.isForceMultiAttacked()) {
				if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
					if (player.getAttackedBy() != npc
							&& player.getAttackedByDelay() > Misc
									.currentTimeMillis()) {
						player.packets().sendMessage(
								"You are already in combat.");
						return;
					}
					if (npc.getAttackedBy() != player
							&& npc.getAttackedByDelay() > Misc
									.currentTimeMillis()) {
						player.packets().sendMessage(
								"This npc is already in combat.");
						return;
					}
				}
			}
			//if (player.getRights() > 1)
				//player.sm("Attack NPC: " + npc.getId() + " - " + npc.getName());
			player.getActionManager().setSkill(new PlayerCombat(npc));
		}

		else if (packetId == INTERFACE_ON_PLAYER) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.getStopDelay() > Misc.currentTimeMillis())
				return;
			int playerIndex = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntLE();
			int itemId = stream.readUnsignedShort();
			boolean unknown = stream.read128Byte() == 1;
			int junk2 = stream.readUnsignedShortLE128();
			int interfaceId = interfaceHash >> 16;
			Player usingOn = Game.getPlayers().get(playerIndex);
			if (usingOn == null || usingOn.isDead() || usingOn.hasFinished()
					|| !player.getMapRegionsIds().contains(usingOn.getRegionId()))
				return;
			int buttonId = interfaceHash - (interfaceId << 16);
			if (Misc.getInterfaceDefinitionsSize() <= interfaceId)
				return;
			if(!player.getRandomEvent().canUseInterfaceOnPlayer(usingOn, interfaceId, buttonId, itemId))
				return;
			switch (interfaceId) {
			case 679:
				ItemOnPlayer.process(player, usingOn, itemId);
				break;
			case 430:
				MagicOnPlayerPacketHandler.handlePacket(buttonId, player, usingOn);
				break;
			}
			if (!player.interfaces().containsInterface(interfaceId))
				return;
			if (buttonId == 65535)
				buttonId = -1;
			if (buttonId != -1
					&& Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= buttonId)
				return;
			Player p21 = Game.getPlayers().get(playerIndex);
			if (p21 == null || p21.isDead() || p21.hasFinished()
					|| !player.getMapRegionsIds().contains(p21.getRegionId()))
				return;
			player.stopAll(false);
			switch (interfaceId) {
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && buttonId == 14)
						|| (interfaceId == 662 && buttonId == 65)
						|| (interfaceId == 662 && buttonId == 74)
						|| interfaceId == 747 && buttonId == 17) {
					if ((interfaceId == 662 && buttonId == 74
							|| interfaceId == 747 && buttonId == 23 || interfaceId == 747
							&& buttonId == 17)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (!player.isCanPvp() || !p21.isCanPvp()) {
						player.packets()
								.sendMessage(
										"You can only attack players in a player-vs-player area.");
						return;
					}
					if (!player.getFamiliar().canAttack(p21)) {
						player.packets()
								.sendMessage(
										"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && buttonId == 74
										|| interfaceId == 747
										&& buttonId == 17);
						player.getFamiliar().setTarget(p21);
					}
				}
				break;
			case 193:
				switch (buttonId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, buttonId, 1, false)) {
						player.faceLocation(new Location(p21.getCoordFaceX(p21
								.getSize()), p21.getCoordFaceY(p21.getSize()),
								p21.getZ()));
						if (!player.getControllerManager().canAttack(p21))
							return;
						if (!player.isCanPvp() || !p21.isCanPvp()) {
							player.packets()
									.sendMessage(
											"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p21.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p21
									&& player.getAttackedByDelay() > Misc
											.currentTimeMillis()) {
								player.packets()
										.sendMessage(
												"That "
														+ (player
																.getAttackedBy() instanceof Player ? "player"
																: "npc")
														+ " is already in combat.");
								return;
							}
							if (p21.getAttackedBy() != player
									&& p21.getAttackedByDelay() > Misc
											.currentTimeMillis()) {
								if (p21.getAttackedBy() instanceof NPC) {
									p21.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.packets()
											.sendMessage(
													"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(
								new PlayerCombat(p21));
					}
					break;
				}
			case 192:
				switch (buttonId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 86: // teleblock
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 91: // fire surge
				case 99: // storm of armadyl
				case 36: // bind
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, buttonId, 1, false)) {
						player.faceLocation(new Location(p21.getCoordFaceX(p21
								.getSize()), p21.getCoordFaceY(p21.getSize()),
								p21.getZ()));
						if (!player.getControllerManager().canAttack(p21))
							return;
						if (!player.isCanPvp() || !p21.isCanPvp()) {
							player.packets()
									.sendMessage(
											"You can only attack players in a player-vs-player area.");
							return;
						}
						if (!p21.isAtMultiArea() || !player.isAtMultiArea()) {
							if (player.getAttackedBy() != p21
									&& player.getAttackedByDelay() > Misc
											.currentTimeMillis()) {
								player.packets()
										.sendMessage(
												"That "
														+ (player
																.getAttackedBy() instanceof Player ? "player"
																: "npc")
														+ " is already in combat.");
								return;
							}
							if (p21.getAttackedBy() != player
									&& p21.getAttackedByDelay() > Misc
											.currentTimeMillis()) {
								if (p21.getAttackedBy() instanceof NPC) {
									p21.setAttackedBy(player); // changes enemy
									// to player,
									// player has
									// priority over
									// npc on single
									// areas
								} else {
									player.packets()
											.sendMessage(
													"That player is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setSkill(
								new PlayerCombat(p21));
					}
					break;
				}
				break;
			}
			System.out.println("Spell:" + buttonId);
		} else if (packetId == INTERFACE_ON_NPC) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			if (player.isLocked() || player.getEmotesManager().isDoingEmote())
				return;
//			boolean forceRun = stream.readByte() == 1;
//			int interfaceHash = stream.readInt();
//			int npcIndex = stream.readUnsignedShortLE();
//			int interfaceSlot = stream.readUnsignedShortLE128();
//			int junk2 = stream.readUnsignedShortLE();
//			int interfaceId = interfaceHash >> 16;
//			int buttonId = interfaceHash - (interfaceId << 16);
			int interfaceSlot = 999;
			int npcIndex = stream.readUnsignedShortLE();
			int interfaceHash = stream.readIntLE();
			int itemId = stream.readUnsignedShort();
			boolean forceRun = stream.read128Byte() == 1;
			int junk2 = stream.readUnsignedShortLE128();
			int interfaceId = interfaceHash >> 16;
			int buttonId = interfaceHash - (interfaceId << 16);
			if (Misc.getInterfaceDefinitionsSize() <= interfaceId)
				return;	
			if (buttonId == 65535)
				buttonId = -1;
			System.out.println(forceRun+" "+interfaceHash+" "+npcIndex+" "+interfaceSlot+" "+junk2+" "+interfaceId+" "+buttonId);
			if (buttonId != -1
					&& Misc.getInterfaceDefinitionsComponentsSize(interfaceId) <= buttonId)
				return;
			NPC npc = Game.getNPCs().get(npcIndex);
			if (npc == null || npc.isDead() || npc.hasFinished()
					|| !player.getMapRegionsIds().contains(npc.getRegionId()))
				return;
			player.stopAll();
			if(!player.getRandomEvent().canUseInterfaceOnNPC(npc, interfaceSlot, interfaceId, buttonId))
				return;
			if (forceRun)
				player.setRun(forceRun);
			if (interfaceId != Inventory.INVENTORY_INTERFACE) {
				if (!npc.defs().hasAttackOption()) {
					player.packets().sendMessage("You cannot attack this NPC.");
					return;
				}
			}
			switch (interfaceId) {
			case Inventory.INVENTORY_INTERFACE:
				Item item = player.getInventory().getItem(interfaceSlot);
				
				if (item == null)
					return;
				else if (npc instanceof Familiar) {
					Familiar familiar = (Familiar) npc;
					if (familiar != player.getFamiliar()) {
						player.packets().sendMessage(
								"This is not your familiar!");
						return;
					}
				}
				ItemOnNPC.process(player, npc, item);
				break;
			case 662:
			case 747:
				if (player.getFamiliar() == null)
					return;
				player.resetWalkSteps();
				if ((interfaceId == 747 && buttonId == 15)
						|| (interfaceId == 662 && buttonId == 65)
						|| (interfaceId == 662 && buttonId == 74)
						|| interfaceId == 747 && buttonId == 18
						|| interfaceId == 747 && buttonId == 24) {
					if ((interfaceId == 662 && buttonId == 74 || interfaceId == 747
							&& buttonId == 18)) {
						if (player.getFamiliar().getSpecialAttack() != SpecialAttack.ENTITY)
							return;
					}
					if (npc instanceof Familiar) {
						Familiar familiar = (Familiar) npc;
						if (familiar == player.getFamiliar()) {
							player.packets().sendMessage(
									"You can't attack your own familiar.");
							return;
						}
						if (!player.getFamiliar()
								.canAttack(familiar.getOwner())) {
							player.packets()
									.sendMessage(
											"You can only attack players in a player-vs-player area.");
							return;
						}
					}
					if (!player.getFamiliar().canAttack(npc)) {
						player.packets()
								.sendMessage(
										"You can only use your familiar in a multi-zone area.");
						return;
					} else {
						player.getFamiliar().setSpecial(
								interfaceId == 662 && buttonId == 74
										|| interfaceId == 747
										&& buttonId == 18);
						player.getFamiliar().setTarget(npc);
					}
				}
				break;
			case 193:
				switch (buttonId) {
				case 28:
				case 32:
				case 24:
				case 20:
				case 30:
				case 34:
				case 26:
				case 22:
				case 29:
				case 33:
				case 25:
				case 21:
				case 31:
				case 35:
				case 27:
				case 23:
					if (Magic.checkCombatSpell(player, buttonId, 1, false)) {
						player.faceLocation(new Location(npc.getCoordFaceX(npc
								.getSize()), npc.getCoordFaceY(npc.getSize()),
								npc.getZ()));
						if (!player.getControllerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.packets().sendMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.packets().sendMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Misc
												.currentTimeMillis()) {
									player.packets().sendMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Misc
												.currentTimeMillis()) {
									player.packets().sendMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
			case 192:
				switch (buttonId) {
				case 25: // air strike
				case 28: // water strike
				case 30: // earth strike
				case 32: // fire strike
				case 34: // air bolt
				case 39: // water bolt
				case 42: // earth bolt
				case 45: // fire bolt
				case 49: // air blast
				case 52: // water blast
				case 58: // earth blast
				case 63: // fire blast
				case 70: // air wave
				case 73: // water wave
				case 77: // earth wave
				case 80: // fire wave
				case 84: // air surge
				case 87: // water surge
				case 89: // earth surge
				case 66: // Sara Strike
				case 67: // Guthix Claws
				case 68: // Flame of Zammy
				case 93:
				case 91: // fire surge
				case 99: // storm of Armadyl
				case 36: // bind
				case 55: // snare
				case 81: // entangle
					if (Magic.checkCombatSpell(player, buttonId, 1, false)) {
						player.faceLocation(new Location(npc.getCoordFaceX(npc
								.getSize()), npc.getCoordFaceY(npc.getSize()),
								npc.getZ()));
						if (!player.getControllerManager().canAttack(npc))
							return;
						if (npc instanceof Familiar) {
							Familiar familiar = (Familiar) npc;
							if (familiar == player.getFamiliar()) {
								player.packets().sendMessage(
										"You can't attack your own familiar.");
								return;
							}
							if (!familiar.canAttack(player)) {
								player.packets().sendMessage(
										"You can't attack this npc.");
								return;
							}
						} else if (!npc.isForceMultiAttacked()) {
							if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
								if (player.getAttackedBy() != npc
										&& player.getAttackedByDelay() > Misc
												.currentTimeMillis()) {
									player.packets().sendMessage(
											"You are already in combat.");
									return;
								}
								if (npc.getAttackedBy() != player
										&& npc.getAttackedByDelay() > Misc
												.currentTimeMillis()) {
									player.packets().sendMessage(
											"This npc is already in combat.");
									return;
								}
							}
						}
						player.getActionManager().setAction(
								new PlayerCombat(npc));
					}
					break;
				}
				break;
			case 430:
				Magic.processLunarSpell(player, buttonId, npc);
				break;
			}
			if(Constants.DEVELOPER_MODE)
				System.out.println("Spell button ID: "+buttonId);
		} else if (packetId == NPC_CLICK1_PACKET) {
			NPCActionPacketHandler.handleOption1(player, stream);
		} else if (packetId == OBJECT_CLICK5_PACKET) {
			ObjectPackets.process(player, stream, ObjectPackets.OPTION_5);
		} else if (packetId == NPC_CLICK3_PACKET) {
			NPCActionPacketHandler.handleOption3(player, stream);
		} else if (packetId == NPC_CLICK2_PACKET) {
			NPCActionPacketHandler.handleOption2(player, stream);
		} else if (packetId == OBJECT_CLICK1_PACKET) {
			ObjectPackets.process(player, stream, ObjectPackets.OPTION_1);
		} else if (packetId == OBJECT_CLICK2_PACKET) {
			ObjectPackets.process(player, stream, ObjectPackets.OPTION_2);
		} else if (packetId == OBJECT_CLICK3_PACKET) {
			ObjectPackets.process(player, stream, ObjectPackets.OPTION_3);
		} else if (packetId == ITEM_TAKE_PACKET) {
			if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
					|| player.isDead())
				return;
			long currentTime = Misc.currentTimeMillis();
			if (player.getStopDelay() > currentTime)
				// || player.getFreezeDelay() >= currentTime)
				return;
			final int id = stream.readUnsignedShort128();

			boolean unknown = stream.readByte() == 1;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			boolean forceRun = stream.read128Byte() == 1;
			final Location tile = new Location(x, y, player.getZ());
			final int regionId = tile.getRegionId();
			player.addWalkSteps(tile.getX(), tile.getY(), 1);
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			if (!player.getMapRegionsIds().contains(regionId))
				return;
			final FloorItem item = Game.getRegion(regionId).getGroundItem(id,
					tile, player);
			if (item == null)
				return;
			player.stopAll(false);
			player.setCoordsEvent(new CoordsEvent(tile, new Runnable() {
				@Override
				public void run() {
					final FloorItem item = Game.getRegion(regionId)
							.getGroundItem(id, tile, player);
					if (item == null)
						return;
					Logs.write(player.getUsername()+" picked up an item: ["+
						item.getId()+", "+item.getName()+" - x"+item.getAmount()+"] at location: ["+
							x+", "+y+", "+player.getZ()+"]", "PickUpItems", player, true);
					player.faceLocation(tile);
					if (!player.getRandomEvent().canPickupItem(item))
						return;
					
					Game.removeGroundItem(player, item);
				}
			}, 1, 1));

		} else if (packetId == ITEM_ON_OBJECT_PACKET)
			ItemOnObject.process(player, stream);
	}

	public void processPackets(final int packetId, InputStream stream,
			int length) {

		player.setPacketsDecoderPing(Misc.currentTimeMillis());
		if (packetId == PING_PACKET) {
			// kk we ping :)
		} else if (packetId == MOVE_MOUSE_PACKET) {
			// USELESS PACKET
		} else if (packetId == KEY_TYPED_PACKET) {
			int key = stream.readUnsignedByte();
//			if(player.getKeyStrokes().getKeyStroke() != null)
//				player.getKeyStrokes().getKeyStroke().press(key);
			// No longer using this :-) I made my own packet for key presses
		} else if (packetId == RECEIVE_PACKET_COUNT_PACKET) {
			// interface packets
			stream.readInt();
		} else if (packetId == ITEM_ON_ITEM_PACKET) {
			InventoryActionHandler.handleItemOnItem(player, stream);

		} else if (packetId == PLAYER_TRADE_OPTION_PACKET) {
			long currentTime = System.currentTimeMillis();
			boolean unknown2 = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player other = (Player) Game.getPlayers().get(playerIndex);
			if (player.getTrade() != null || other.getTrade() != null) {
				player.packets().sendMessage("You're already in a trade!");
				return;
			}
			if (player.getLocation().getX() == other.getLocation().getX()
					&& player.getLocation().getY() == other.getLocation()
							.getY()) {
				player.sendMessage("You can't trade in this position.");
				return;
			}
			if ((other.getTemporaryAttributtes().get("receivedTradeRequest") == Boolean.TRUE && (Integer) other
					.getTemporaryAttributtes().get("tradingWith") == player
					.getIndex())
					|| (player.getTemporaryAttributtes().get(
							"receivedTradeRequest") == Boolean.TRUE && (Integer) player
							.getTemporaryAttributtes().get("tradingWith") == other
							.getIndex())) {
				if (player.getLocation().getX() == other.getLocation().getX()
						&& player.getLocation().getY() == other.getLocation()
								.getY()) {
					player.sendMessage("You can't trade in this position.");
					player.getTrade().finish();
					return;
				}
				Trade session = new Trade(player, other);
				player.setTrade(session);
				other.setTrade(session);
				session.start();
			} else {
				if (player.getLocation().getX() == other.getLocation().getX()
						&& player.getLocation().getY() == other.getLocation()
								.getY()) {
					player.sendMessage("You can't trade in this position.");
					return;
				}
				player.packets().sendMessage(
						"Sending " + other.getDisplayName().toLowerCase()
								+ " a trade request...");
				other.packets().sendTradeRequestMessage(player);
				other.getTemporaryAttributtes().put("receivedTradeRequest",
						Boolean.TRUE);
				other.getTemporaryAttributtes().put("tradingWith",
						player.getIndex());
				player.getTemporaryAttributtes().put("tradingWith",
						other.getIndex());
				player.stopAll(false);
			}

		} else if (packetId == ACCEPT_TRADE_CHAT_PACKET) {
			long currentTime = System.currentTimeMillis();
			boolean unknown2 = stream.readByte() == 1;
			int playerIndex = stream.readUnsignedShort();
			Player other = (Player) Game.getPlayers().get(playerIndex);
			if (player.getTrade() != null || other.getTrade() != null) {
				player.packets().sendMessage("You're already in a trade!");
				return;
			}
			if (player.getLocation().getX() == other.getLocation().getX()
					&& player.getLocation().getY() == other.getLocation()
							.getY()) {
				player.sendMessage("You can't trade in this position.");
				return;
			}
			if ((other.getTemporaryAttributtes().get("receivedTradeRequest") == Boolean.TRUE && (Integer) other
					.getTemporaryAttributtes().get("tradingWith") == player
					.getIndex())
					|| (player.getTemporaryAttributtes().get(
							"receivedTradeRequest") == Boolean.TRUE && (Integer) player
							.getTemporaryAttributtes().get("tradingWith") == other
							.getIndex())) {
				if (player.getLocation().getX() == other.getLocation().getX()
						&& player.getLocation().getY() == other.getLocation()
								.getY()) {
					player.sendMessage("You can't trade in this position.");
					other.sendMessage("You can't trade in this position.");
					player.getTrade().endSession();
					other.getTrade().endSession();
					return;
				}
				Trade session = new Trade(player, other);
				player.setTrade(session);
				other.setTrade(session);
				session.start();
			} else {
				if (player.getLocation().getX() == other.getLocation().getX()
						&& player.getLocation().getY() == other.getLocation()
								.getY()) {
					player.sendMessage("You can't trade in this position.");
					other.sendMessage("You can't trade in this position.");
					return;
				}
				player.packets().sendMessage(
						"Sending " + other.getDisplayName().toLowerCase()
								+ " a trade request...");
				other.packets().sendTradeRequestMessage(player);
				other.getTemporaryAttributtes().put("receivedTradeRequest",
						Boolean.TRUE);
				other.getTemporaryAttributtes().put("tradingWith",
						player.getIndex());
				player.getTemporaryAttributtes().put("tradingWith",
						other.getIndex());
				player.stopAll(false);
			}
		} else if (packetId == MAGIC_ON_GROUND_PACKET) {
			MagicOnGroundPacketHandler.handlePacket(player, stream, packetId);
		} else if (packetId == MAGIC_ON_ITEM_PACKET) {

			int junk = stream.readShort();

			int itemSlot = stream.readShortLE();
		}
		if (packetId == AFK_PACKET) {
			player.getSession().getChannel().close();
		} else if (packetId == CLOSE_INTERFACE_PACKET) {
			if (!player.isRunning()) {
				player.run();
				return;
			}
			// player.stopAll();
		} else if (packetId == MOVE_CAMERA_PACKET) {
			// not using it atm
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (packetId == IN_OUT_SCREEN_PACKET) {
			// not using this check because not 100% efficient

			boolean inScreen = stream.readByte() == 1;
		} else if (packetId == SCREEN_PACKET) {
			int displayMode = stream.readUnsignedByte();
			player.setScreenWidth(stream.readUnsignedShort());
			player.setScreenHeight(stream.readUnsignedShort());

			boolean switchScreenMode = stream.readUnsignedByte() == 1;
			if (!player.hasStarted() || player.hasFinished()
					|| displayMode == player.getDisplayMode()
					|| !player.interfaces().containsInterface(742))
				return;
			player.setDisplayMode(displayMode);
			player.interfaces().removeAll();
			player.interfaces().sendInterfaces();
			player.interfaces().sendInterface(742);
		} else if (packetId == CLICK_PACKET) {
			int mouseHash = stream.readShortLE128();
			int mouseButton = mouseHash >> 15;
			int time = mouseHash - (mouseButton << 15); // time
			int positionHash = stream.readIntV1();
			int y = positionHash >> 16; // y;
			int x = positionHash - (y << 16); // x
			boolean clicked;
			// mass click or stupid autoclicker, lets stop lagg
			if (time <= 1 || x < 0 || x > player.getScreenWidth() || y < 0
					|| y > player.getScreenHeight()) {
				 player.getSession().getChannel().close();
				clicked = false;
				return;
			}
			clicked = true;
		} else if (packetId == DIALOGUE_CONTINUE_PACKET) {
			int interfaceHash = stream.readIntV2();

			int junk = stream.readShortLE128();
			int interfaceId = interfaceHash >> 16;

			int buttonId = (interfaceHash & 0xFF);
			if (Misc.getInterfaceDefinitionsSize() <= interfaceId) {
				// hack, or server error or client error
				// player.getSession().getChannel().close();
				return;
			}
			if (!player.isRunning()
					|| !player.interfaces().containsInterface(interfaceId))
				return;
			int componentId = interfaceHash - (interfaceId << 16);
			if (player.getDialogue().getCurrent() != null)
				player.getDialogue().click(interfaceId, componentId);
			else if(player.getDialogueScript().getCurrent() != null)
				player.getDialogueScript().nextStage(interfaceId, componentId);
			else
				player.getMatrixDialogues().continueDialogue(interfaceId,
						componentId);
			System.out.println("Dialogue: "+interfaceId+" Button: "+componentId);
		} else if (packetId == ACTION_BUTTON1_PACKET
				|| packetId == ACTION_BUTTON2_PACKET
				|| packetId == ACTION_BUTTON4_PACKET
				|| packetId == ACTION_BUTTON5_PACKET
				|| packetId == ACTION_BUTTON6_PACKET
				|| packetId == ACTION_BUTTON7_PACKET
				|| packetId == ACTION_BUTTON8_PACKET
				|| packetId == ACTION_BUTTON3_PACKET
				|| packetId == ACTION_BUTTON9_PACKET
				|| packetId == ACTION_BUTTON10_PACKET) {
			ButtonPackets.handleButtons(player, stream, packetId);
		} else if (packetId == SHORT_STRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (player.interfaces().containsInterface(1108))
				player.getFriendsIgnores().setChatPrefix(value);

		} else if (packetId == ENTER_STRING_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			String value = stream.readString();
			if (value.equals(""))
				return;
			if (player.getInputEvent().current() != null) {
				player.getInputEvent().handle((String) value);
				return;
			}
			
		} else if (packetId == ENTER_INTEGER_PACKET) {
			if (!player.isRunning() || player.isDead())
				return;
			int value = stream.readInt();
			if (player.getInputEvent().current() != null) {
				player.getInputEvent().handle((int) value);
				return;
			}
			if ((player.interfaces().containsInterface(762) && player
					.interfaces().containsInterface(763))
					|| player.interfaces().containsInterface(11)) {
				if (value < 0)
					return;
				Integer bank_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bank_item_X_Slot");
				if (bank_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bank_isWithdraw") != null)
					player.getBank().withdrawItem(bank_item_X_Slot, value);
				else
					player.getBank().depositItem(
							bank_item_X_Slot,
							value,
							player.interfaces().containsInterface(11) ? false
									: true);
			}

			else if (player.interfaces().containsInterface(206)
					&& player.interfaces().containsInterface(207)) {
				if (value < 0)
					return;
				Integer pc_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("pc_item_X_Slot");
				if (pc_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("pc_isRemove") != null)
					player.getPriceCheckManager().removeItem(pc_item_X_Slot,
							value);
				else
					player.getPriceCheckManager()
							.addItem(pc_item_X_Slot, value);
			} else if (player.interfaces().containsInterface(671)
					&& player.interfaces().containsInterface(665)) {
				if (player.getFamiliar() == null
						|| player.getFamiliar().getBob() == null)
					return;
				if (value < 0)
					return;
				Integer bob_item_X_Slot = (Integer) player
						.getTemporaryAttributtes().remove("bob_item_X_Slot");
				if (bob_item_X_Slot == null)
					return;
				if (player.getTemporaryAttributtes().remove("bob_isRemove") != null)
					player.getFamiliar().getBob()
							.removeItem(bob_item_X_Slot, value);
				else
					player.getFamiliar().getBob()
							.addItem(bob_item_X_Slot, value);
			} else if (player.getTemporaryAttributtes().get("skillId") != null) {
				int skillId = (Integer) player.getTemporaryAttributtes()
						.remove("skillId");
				if (skillId == Skills.HITPOINTS && value == 1)
					value = 10;
				else if (value < 1)
					value = 1;
				else if (value > 99)
					value = 99;
				player.getSkills().set(skillId, value);
				player.getSkills().setXp(skillId, Skills.getXPForLevel(value));
				player.getAppearance().generateAppearanceData();
				player.getMatrixDialogues().finishDialogue();

			} else if (player.getTemporaryAttributtes().get("finding_player") == Boolean.TRUE) {
				String name = "";
				Player other = Game.getPlayerByDisplayName(name);
				player.interfaces().sendInterface(594);
				player.packets().sendIComponentText(594, 106,
						"You are reporting: " + other.getDisplayName() + "");
			} else if (player.getTemporaryAttributtes().get("view_name") == Boolean.TRUE) {
				String value1 = stream.readString();
				player.getTemporaryAttributtes().remove("view_name");
				Player other = Game.getPlayerByDisplayName(value1);
				if (other == null) {
					player.packets().sendMessage("Couldn't find player.");
					return;
				}
			} else {
				if (player.getTemporaryAttributtes().get("offerX") != null
						&& player.interfaces().containsInterface(335)
						&& player.getTrade().getState() == TradeState.STATE_ONE) {
					player.getTrade().addItem(
							player,
							(Integer) player.getTemporaryAttributtes().get(
									"offerX"), value);
					player.getTemporaryAttributtes().remove("offerX");
				} else {
					if (player.getTemporaryAttributtes().get("removeX") != null
							&& player.interfaces().containsInterface(335)
							&& player.getTrade().getState() == TradeState.STATE_ONE) {
						player.getTrade().removeItem(
								player,
								(Integer) player.getTemporaryAttributtes().get(
										"removeX"), value);
						player.getTemporaryAttributtes().remove("removeX");

					}
				}
			}
		} else if (packetId == SWITCH_INTERFACE_ITEM_PACKET) {
			stream.readUnsignedShort();
			int fromSlot = stream.readUnsignedShortLE();
			stream.readUnsignedShort128();
			int interface1Hash = stream.readIntV1();
			int toSlot = stream.readUnsignedShortLE();
			int interface2Hash = stream.readIntV2();

			int fromInterfaceId = interface1Hash >> 16;
			int fromComponentId = interface1Hash - (fromInterfaceId << 16);

			int toInterfaceId = interface2Hash >> 16;
			int toComponentId = interface2Hash - (toInterfaceId << 16);

			if (Misc.getInterfaceDefinitionsSize() <= fromInterfaceId
					|| Misc.getInterfaceDefinitionsSize() <= toInterfaceId)
				return;
			if (!player.interfaces().containsInterface(fromInterfaceId)
					|| !player.interfaces().containsInterface(toInterfaceId))
				return;
			if (fromComponentId != -1
					&& Misc.getInterfaceDefinitionsComponentsSize(fromInterfaceId) <= fromComponentId)
				return;
			if (toComponentId != -1
					&& Misc.getInterfaceDefinitionsComponentsSize(toInterfaceId) <= toComponentId)
				return;
			if (fromInterfaceId == Inventory.INVENTORY_INTERFACE
					&& fromComponentId == 0
					&& toInterfaceId == Inventory.INVENTORY_INTERFACE
					&& toComponentId == 0) {
				toSlot -= 28;
				if (toSlot < 0
						|| toSlot >= player.getInventory()
								.getItemsContainerSize()
						|| fromSlot >= player.getInventory()
								.getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 763 && fromComponentId == 0
					&& toInterfaceId == 763 && toComponentId == 0) {
				if (toSlot >= player.getInventory().getItemsContainerSize()
						|| fromSlot >= player.getInventory()
								.getItemsContainerSize())
					return;
				player.getInventory().switchItem(fromSlot, toSlot);
			} else if (fromInterfaceId == 762 && toInterfaceId == 762) {
				player.getBank().switchItem(fromSlot, toSlot, fromComponentId,
						toComponentId);
			}
		//	System.out.println("Switch item " + fromInterfaceId + ", "+ fromSlot + ", " + toSlot);
		} else if (packetId == SWITCH_DETAIL) {
			int hash = stream.readInt();
			if (hash != 1057001181) {
				// hack, or server error or client error
				player.getSession().getChannel().close();
				return;
			}
			// done loading region when switch detail or mapregion
		} else if (packetId == DONE_LOADING_REGION) {
			/*
			 * if(!player.clientHasLoadedMapRegion()) { //load objects and items
			 * here player.setClientHasLoadedMapRegion(); }
			 * //player.refreshSpawnedObjects(); //player.refreshSpawnedItems();
			 */
		} else if (packetId == WALKING_PACKET
				|| packetId == MINI_WALKING_PACKET
				|| packetId == ITEM_TAKE_PACKET
				|| packetId == PLAYER_OPTION_2_PACKET
				|| packetId == PLAYER_OPTION_1_PACKET || packetId == ATTACK_NPC
				|| packetId == INTERFACE_ON_PLAYER
				|| packetId == INTERFACE_ON_NPC
				|| packetId == NPC_CLICK1_PACKET
				|| packetId == NPC_CLICK2_PACKET
				|| packetId == OBJECT_CLICK1_PACKET
				|| packetId == SWITCH_INTERFACE_ITEM_PACKET
				|| packetId == OBJECT_CLICK2_PACKET
				|| packetId == OBJECT_CLICK3_PACKET
				|| packetId == ITEM_ON_OBJECT_PACKET)
			player.addLogicPacketToQueue(new LogicPacket(packetId, length,
					stream));
		else if (packetId == OBJECT_EXAMINE_PACKET) {
			ObjectPackets.process(player, stream, ObjectPackets.EXAMINE);
		} else if (packetId == JOIN_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			FriendChatsManager.joinChat(stream.readString(), player);
		} else if (packetId == KICK_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			player.setLastPublicMessage(Misc.currentTimeMillis() + 1000); // avoids
																			// message
																			// appearing
			player.kickPlayerFromFriendsChannel(stream.readString());
		} else if (packetId == CHANGE_FRIEND_CHAT_PACKET) {
			if (!player.hasStarted()
					|| !player.interfaces().containsInterface(1108))
				return;
			player.getFriendsIgnores().changeRank(stream.readString(),
					stream.readUnsignedByteC());
		} else if (packetId == ADD_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().addFriend(stream.readString());
		} else if (packetId == REMOVE_FRIEND_PACKET) {
			if (!player.hasStarted())
				return;
			player.getFriendsIgnores().removeFriend(stream.readString());
		} else if (packetId == SEND_FRIEND_MESSAGE_PACKET) {
			if (!player.hasStarted())
				return;
			if(Punishments.isIPMuted(player.getIP()) || Punishments.isPermMuted(player)) {
				player.sm("You have been permanently muted due to breaking a rule.");
				return;
			}
			if(player.isMuted()) {
				player.sm("You have been temporarily muted due to breaking a rule.");
				player.sm("This mute will remain for a further: "+TimeUtils.formatCountdown(player.getMutedDelay())+".");
				player.sm("To prevent further mutes, please read the rules.");
				return;
			}
			String username = stream.readString();
			Player p2 = Game.getPlayerByDisplayName(username);
			if (p2 == null)
				return;
			String message = Misc.fixChatMessage(Huffman.readEncryptedMessage(150, stream));
			Logs.write(player.getUsername()+" private messaged "+username+" saying: \""+
				message+"\"", "PrivateMessages", player, true);
			Logs.write(username+" received a private message from "+
				player.getUsername()+" saying: \""+message+"\"", "PrivateMessages", p2, true);
			player.getFriendsIgnores().sendMessage(p2, message);
		} else if (packetId == SEND_FRIEND_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			String username = stream.readString();
			int fileId = stream.readUnsignedShort();
			if (fileId > 1163)
				return;
			byte[] data = null;
			if (length > 3 + username.length()) {
				data = new byte[length - (3 + username.length())];
				stream.readBytes(data);
			}
			data = Misc.completeQuickMessage(player, fileId, data);
			Player p2 = Game.getPlayerByDisplayName(username);
			if (p2 == null)
				return;
			player.getFriendsIgnores().sendQuickChatMessage(p2,
					new QuickChatMessage(fileId, data));
		} else if (packetId == PUBLIC_QUICK_CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Misc.currentTimeMillis())
				return;
			player.setLastPublicMessage(Misc.currentTimeMillis() + 300);
			// just tells you which client script created packet

			boolean secondClientScript = stream.readByte() == 1;// script 5059
			// or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Misc.completeQuickMessage(player, fileId, data);
			if (chatType == 0)
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			else if (chatType == 1)
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(
						fileId, data));

		} else if (packetId == CHAT_TYPE_PACKET) {
			chatType = stream.readUnsignedByte();
		} else if (packetId == CHAT_PACKET) {
			if (!player.hasStarted())
				return;
			if (player.getLastPublicMessage() > Misc.currentTimeMillis())
				return;
			// TODO make an auto mute / give warning for spamming lol
			player.setLastPublicMessage(Misc.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			final String message = Huffman.readEncryptedMessage(250, stream);
			if (message == null || message.replaceAll(" ", "").equals(""))
				return;
			if (StringUtils.contains("0hdr2ufufl9ljlzlyla", message)
					|| StringUtils.contains("0hdr", message))
				return;
			if (message.startsWith("::") || message.startsWith(";;")) {

				if (Commands.processCommand(player, message
						.replace("::", "").replace(";;", ""), false, false))
					return;
				return;
			} else if (message.startsWith("item") && player.getRights() > 1) {
				String[] cmd = message.split(" ");
				int itemId = Integer.parseInt(cmd[1]);
				int amount = 1;
				if (cmd.length >= 3)
					amount = Integer.parseInt(cmd[2]);
				player.getInventory().addItem(itemId,
						cmd.length >= 3 ? amount : 1);
				return;
			} else if (!player.isBot())
				for (Bot bot : BotList.getBots())
					if (bot != null && bot.withinDistance(player, 14)) {
						bot.getBotActionHandler().sendResponse(player, message);
					}
			if(StringUtils.contains("follow me", message)) {
				if (!player.hasStarted() || !player.clientHasLoadedMapRegion()
						|| player.isDead())
					return;
				boolean unknown = stream.readByte() == 1;
				int playerIndex = stream.readUnsignedShort();
				for (final Player p2 : Game.getLocalPlayers(player)) {
					if (p2 == null)
						continue;
					if (p2.isBot()) {
						Game.submit(new GameTick(1.5) {
							@Override
							public void run() {
								stop();
								p2.publicChat("Okay, coming!");
								if (p2 == null || p2.isDead() || p2.hasFinished()
										|| !player.getMapRegionsIds().contains(p2.getRegionId()))
									return;
								if (p2.getStopDelay() > Misc.currentTimeMillis())
									return;
								p2.stopAll(false);
								p2.getActionManager().setSkill(new PlayerFollow(player));
							}

						});
					}
				}
			}
			if (StringUtils.contains("train str", message)) {
				for (final Player p : Game.getLocalPlayers(player)) {
					if (p == null)
						continue;
					if (p.isBot()) {
						Game.submit(new GameTick(1.5) {
							@Override
							public void run() {
								stop();
								p.publicChat("You're right, thanks for the suggestion.");
								p.getCombatDefinitions().setAttackStyle(1);
							}

						});
					}
				}
			} else if (StringUtils.contains("train att", message)) {
				for (final Player p : Game.getLocalPlayers(player)) {
					if (p == null)
						continue;
					if (p.isBot()) {
						Game.submit(new GameTick(1.5) {
							@Override
							public void run() {
								stop();
								p.publicChat("Okay, will do!");
								p.getCombatDefinitions().setAttackStyle(0);
							}

						});
					}
				}
			}
			if(Punishments.isIPMuted(player.getIP()) || Punishments.isPermMuted(player)) {
				player.sm("You have been permanently muted due to breaking a rule.");
				return;
			}
			if(player.isMuted()) {
				player.sm("You have been temporarily muted due to breaking a rule.");
				player.sm("This mute will remain for a further: "+TimeUtils.formatCountdown(player.getMutedDelay())+".");
				player.sm("To prevent further mutes, please read the rules.");
				return;
			}
			int effects = (colorEffect << 8) | (moveEffect & 0xff) & ~0x8000;
			if (chatType == 1) {
				player.sendFriendsChannelMessage(Misc.fixChatMessage(message));
				Logs.write("["+player.getCurrentFriendChat().getChannelName()+"] "+
					player.getUsername()+" said: \""+message+"\"", "FriendChatChannels", player, true);
			} else {
				player.sendPublicChatMessage(new PublicChatMessage(Misc
						.fixChatMessage(message), effects));
				Logs.write(player.getUsername()+" said: \""+message+"\"", "ChatLogs", player, true);
			}
		} else if (packetId == COMMANDS_PACKET) {
			if (!player.isRunning())
				return;
			boolean clientCommand = stream.readUnsignedByte() == 1;
			boolean unknown = stream.readUnsignedByte() == 1;
			String command = stream.readString();
			Commands.processCommand(player, command, true,
					clientCommand);
		} else if (packetId == COLOR_ID_PACKET) {
			if (!player.hasStarted())
				return;
			int colorId = stream.readUnsignedShort();
			if (player.getTemporaryAttributtes().get("SkillcapeCustomize") != null)
				SkillCapeCustomizer.handleSkillCapeCustomizerColor(player,
						colorId);
		} else if (packetId == 92) { // NPC Examine Packet
			boolean running = stream.readByte128() == 1;
			int npcIndex = stream.readUnsignedShort128();
			final NPC npc = Game.getNPCs().get(npcIndex);
			NPCActionPacketHandler.handleExamine(player, npc);
		} else if (packetId == 27) { // Item Examine Packet
			final int id = stream.readUnsignedShort128();
			boolean unknown = stream.readByte() == 1;
			int y = stream.readUnsignedShort();
			int x = stream.readUnsignedShortLE();
			final Location tile = new Location(x, y, player.getZ());
			final int regionId = tile.getRegionId();
			final FloorItem item = Game.getRegion(regionId).getGroundItem(id, tile, player);
			if(player.isOwner())
				player.sm(item.toString()+" - "+item.getLocation().toString());
			player.sm(item.getExamine());
		} else if (packetId == OBJECT_OPTION4) { // Object Option 4
			ObjectPackets.process(player, stream, ObjectPackets.OPTION_4);
		} else if(packetId == INTERFACE_MOUSE_HOVERING_PACKET) {
			WidgetToolTips.decode(player, stream);
		} else if(packetId == ADVANCED_KEY_TYPED_PACKET) { // Our own custom packet!
			int keyCode = stream.readInt();
			char keyChar = (char) stream.readInt();
			long whenPressed = stream.readLong();
			String keyText = KeyEvent.getKeyText(keyCode);
			boolean shiftHeld = stream.readByte() == 1;
			boolean ctrlHeld = stream.readByte() == 1;
//			System.out.println("Advanced Key Press: "+keyCode+" - "+keyChar+" - "+
//				whenPressed+" - "+keyText+" - "+shiftHeld+" - "+ctrlHeld);
			if(player.getKeyStrokes().getKeyStroke() != null)
				player.getKeyStrokes().getKeyStroke().press(keyCode, keyChar, shiftHeld);
		
		} else {
			if (packetId != 15 && packetId != 16 && packetId != 93
					&& packetId != 29 && packetId != 68 && packetId != 85
					&& packetId != 255 && packetId != 0)
				Logger.log(this, "Missing packet " + packetId
						+ ", expected size: " + length + ", actual size: "
						+ PACKET_SIZES[packetId]);
		}
	}

	public Player getPlayer() {
		return player;
	}

}
