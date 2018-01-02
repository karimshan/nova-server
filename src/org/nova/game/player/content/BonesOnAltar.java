package org.nova.game.player.content;

import java.util.HashMap;
import java.util.Map;

import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.game.player.actions.Action;

@SuppressWarnings("unused")
public class BonesOnAltar extends Action {
	
	public final String MESSAGE = "The gods are very pleased with your offerings.";
	public final double MULTIPLIER = 2.5;
	
	public enum Bones {
		BONES(new Item(526, 1), 5),
		BIG_BONES(new Item(532, 1), 15),
		BABYDRAGON_BONES(new Item(534, 1), 30),
		WYVERN_BONES(new Item(6812, 1), 50),
		DRAGON_BONES(new Item(536, 1), 72),
		OURG_BONES(new Item(4834, 1), 140),
		FROST_DRAGON_BONES(new Item(18830, 1), 180),
		DAGANNOTH_BONES(new Item(6729, 1), 125);
	
		private static Map<Short, Bones> bones = new HashMap<Short, Bones>();
	
		public static Bones forId(short itemId) {
			return bones.get(itemId);
		}
	
		static {
			for (Bones bone: Bones.values()) {
				bones.put((short) bone.getBone().getId(), bone);
			}
		}
	
		private Item item;
		private int xp;
	
		private Bones(Item item, int xp) {
			this.item = item;
			this.xp = xp;
		}
		
		public Item getBone() {
			return item;
		}
		
		public int getXP() {
			return xp;
		}
	}
		
	private Bones bone;
	private int amount;
	private Item item;
	private GlobalObject object;
	private Animation USING = new Animation(896);
	
	public BonesOnAltar(GlobalObject object, Item item, int amount) {
		this.amount = amount;
		this.item = item;
		this.object = object;
	}
	
	public static Bones isGood(Item item) {
		return Bones.forId((short) item.getId());
	}
	
	public boolean start(Player player) {
		if((this.bone = Bones.forId((short) item.getId())) == null) {
			return false;
		}
		player.faceLocation(new Location(object.getCoordFaceX(
				object.defs().getSizeX(), object.defs().getSizeY(),
				object.getRotation()), object.getCoordFaceY(
				object.defs().getSizeX(), object.defs().getSizeY(),
				object.getRotation()), object.getZ()));
		return true;
	}
	
	public boolean process(Player player) {
		if (!Game.getRegion(object.getRegionId()).containsObject(
						object.getId(), object))
			return false;
		if (!player.getInventory().containsItem(item.getId(), 1)) {
			return false;
		}
		if (!player.getInventory().containsItem(bone.getBone().getId(), 1)) {
			return false;
		}
		return true;
	}
	
	public int processWithDelay(Player player) {
		player.faceLocation(new Location(object.getCoordFaceX(
				object.defs().getSizeX(), object.defs().getSizeY(),
				object.getRotation()), object.getCoordFaceY(
				object.defs().getSizeX(), object.defs().getSizeY(),
				object.getRotation()), object.getZ()));
			player.setNextAnimation(USING);
			player.packets().sendGraphics(new Graphics(624), object);
			player.getInventory().deleteItem(item.getId(), 1);
			player.getSkills().addXp(Skills.PRAYER, bone.getXP() * player.xpModifier);
			player.packets().sendMessage(MESSAGE);
			player.getInventory().refresh();
			return 3;
	}

	public void stop(final Player player) {
		this.setActionDelay(player, 3);
	}
	
}