package org.nova.kshan.content.skills.magic;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.game.player.content.MagicAbility;
import org.nova.network.stream.InputStream;
/**
 * 
 * @author Fuzen Seth
 * @since 9.12.2013
 * @category Repesents  player using magic spell to a ground item.
 */
public class MagicOnGroundPacketHandler {
	/**
	 * Tele grab spell id.
	 */
	public static final int TELE_GRAB = 44;
	/**
	 * The regular spellbook.
	 */
	public static final int REGULAR_SPELLBOOK = 190;
	/**
	 * The ancient spellbook.
	 */
	public static final int ANCIENT_SPELLBOOK = -1;
	/**
	 * The lunar spellbook.
	 */
	public static final int LUNAR_SPELLBOOK = -1;
	/**
	 * We handle the magic on ground packet.
	 * @param player
	 * @param stream
	 * @param packetId
	 */
	public static final void handlePacket(Player player, InputStream stream, int packetId) {
		int inventoryInter = stream.readInt() >> 16;
		int itemId = stream.readShort();
		int junk = stream.readShort();
		int itemSlot = stream.readShortLE();
		int interfaceSet = stream.readIntV1();
		int spellId = interfaceSet & 0xFFF;
		int magicInter = interfaceSet >> 16;
		Item item = new Item(itemId);
		
		switch (inventoryInter) {
		case REGULAR_SPELLBOOK:
			switch (spellId) {
			/**
			 * Teleportation grab
			 * Takes a item from ground by using magic.
			 */
			case TELE_GRAB:
				MagicAbility.preformTeleGrab(player, item);
				break;
			}
			break;
			
			default:
				if (player.isDeveloperMode())
					System.out.println("Item:" + itemId + "slot:" + itemSlot + "spell:"
							+ spellId + "i:" + interfaceSet + "l:" + magicInter + "x:"
							+ junk + "k:" + inventoryInter);
		}
	}
}
