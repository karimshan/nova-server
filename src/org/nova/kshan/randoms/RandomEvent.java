package org.nova.kshan.randoms;

import java.util.HashMap;

import org.nova.Constants;
import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.entity.Entity;
import org.nova.game.item.Item;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.ForceTalk;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.Magic;
import org.nova.kshan.dialogues.Dialogue;
import org.nova.kshan.randoms.dependencies.DynamicMap;
import org.nova.kshan.utilities.TimeUtils;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public abstract class RandomEvent {
	
	/**
	 * The Player instance.
	 */
	protected Player player;
	
	/**
	 * Sets the player
	 * @param p
	 */
	public void set(Player p) {
		this.player = p;
	}
	
	/**
	 * Extra data to be used.
	 */
	public Object[] temporaryData;
	
	/**
	 * Returns the player
	 * @return
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Can the player logout at this time or not
	 */
	private boolean canLogout = true;
	
	public abstract void startEvent(); // Starting event only used if need to add stuff after invoking, like a dialogue
	public abstract Integer getNPCId(); // The Id of the npc that teles the player to the event.
	public abstract String[] getNPCMessages(); // The possible messages the npc says when it appears next to player
	public abstract boolean checkLogin(); // What happens when the player logs in?
	public abstract boolean checkLogout(); // What happens when the player logs out?
	public abstract boolean canTeleport(); // Can the player teleport during the event?
	public abstract boolean hasNPCOption1(NPC npc); // Can the player click an npc?
	public abstract boolean hasObjectOption1(GlobalObject obj); // Can the player click an object?
	public abstract boolean canAttackEntity(Entity entity); // Can the player attack an entity? (player or npc)
	public abstract boolean canClickButtons(int interfaceId, int buttonId, int slotId, int packetId); // Can the player click buttons?
	public abstract Dialogue getDialogue(); // The dialogue used when the event starts.
	public abstract Location getEventLocation(); // Where the player is teleported (Event Location)
	public abstract boolean hasHiddenMiniMap(); // Is the minimap hidden when the player is in the event?
	public abstract Integer[] getTabsRemoved(); // Tabs removed during the event
	public abstract boolean isTraditional(); // Is the random event traditional or custom?
	public abstract Integer[] getDynamicMapVariables(); // A custom map that is generated for the user.
	
	/**
	 * Extra npc option (Optional)
	 * @param npc
	 * @return
	 */
	public boolean hasNPCOption2(NPC npc) {
		return true;
	}
	
	/**
	 * Extra npc option (Optional)
	 * @param npc
	 * @return
	 */
	public boolean hasNPCOption3(NPC npc) {
		return true;
	}
	
	/**
	 * Extra object option (Optional)
	 * @param obj
	 * @return
	 */
	public boolean hasObjectOption2(GlobalObject obj) {
		return true;
	}
	
	/**
	 * Extra object option (Optional)
	 * @param obj
	 * @return
	 */
	public boolean hasObjectOption3(GlobalObject obj) {
		return true;
	}
	
	/**
	 * Extra object option
	 * @param object
	 * @return
	 */
	public boolean hasObjectOption4(GlobalObject object) {
		return true;
	}
	
	/**
	 * Extra object option
	 * @param object
	 * @return
	 */
	public boolean hasObjectOption5(GlobalObject object) {
		return true;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean canPickupItem(Item item) {
		return true;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean canWearItem(Item item) {
		return true;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	public boolean canDropItem(Item item) {
		return true;
	}
	
	/**
	 * 
	 * @param npc
	 * @param interfaceSlot
	 * @param interfaceId
	 * @param buttonId
	 * @return
	 */
	public boolean canUseInterfaceOnNPC(NPC npc, int interfaceSlot, int interfaceId, int buttonId) {
		return true;
	}
	
	/**
	 * 
	 * @param usingOn
	 * @param interfaceId
	 * @param buttonId
	 * @param itemId
	 * @return
	 */
	public boolean canUseInterfaceOnPlayer(Player usingOn, int interfaceId, int buttonId, int itemId) {
		return true;
	}
	
	/**
	 * 
	 * @param itemUsed
	 * @param usedWith
	 * @return
	 */
	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		return true;
	}
	
	/**
	 * 
	 * @param npc
	 * @param item
	 * @return
	 */
	public boolean canUseItemOnNPC(NPC npc, Item item) {
		return true;
	}
	
	/**
	 * 
	 * @param item
	 * @param object
	 * @return
	 */
	public boolean canUseItemOnObject(Item item, GlobalObject object) {
		return true;
	}
	
	/**
	 * 
	 * @param usedOn
	 * @param itemId
	 * @return
	 */
	public boolean canUseItemOnPlayer(Player usedOn, int itemId) {
		return true;
	}

	/**
	 * 
	 * @param itemId
	 * @param amount
	 * @return
	 */
	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}

	/**
	 * 
	 * @param nextLocation
	 * @return
	 */
	public boolean canChangeLocation(Location nextLocation) {
		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean canWalk() {
		return true;
	}
	
	/**
	 * 
	 * @param npc
	 * @return
	 */
	public void onNPCDeath(NPC npc) {
		
	}
	
	/**
	 * 
	 * @param entity
	 */
	public void processMovement() {
		
	}
	
	/**
	 * forcestop or not.
	 */
	private boolean forceStop;
	
	/**
	 * sets if it should forceStop or not
	 * @param b
	 */
	public void forceStop(boolean b) {
		forceStop = b;
	}
	
	/**
	 * Returns what to do if the event is force stopped
	 */
	public boolean forceStop() {
		return forceStop;
	}
	
	/**
	 * Again, optional; it's like the world tick which happens every 600 ms
	 */
	public void processEvent() {
		if(System.currentTimeMillis() - getPlayer().timeUntilWrongLocation() >= TimeUtils.getMinutes(15) && !toWrongLocation) 
			// Teleports to wrong location if player is still at event after 15 minutes.
			toWrongLocation = true;
	}
	
	/**
	 * the stage the event is at.
	 */
	protected short interactionStage = 0;
	
	/**
	 * returns if the player gets teled to one of the wrong locations.
	 */
	protected boolean toWrongLocation = false;
	
	/**
	 * Generates a custom map for a player.
	 */
	protected DynamicMap dynamicMap;
	
	/**
	 * Dynamic map dynamic variables (lol)
	 */
	public int baseX;
	public int baseY;
	
	/**
	 * Basic start of every random event.
	 */
	public void invokeFirst() {
		canLogout(false);
		player.stopAll();
		player.setCantMove(true);
		if(getDynamicMapVariables() != null) { // Creates a dynamic map for the player if the variables array isn't empty.
			setDynamicMap(new DynamicMap(getDynamicMapVariables()[0], getDynamicMapVariables()[1], 
				getDynamicMapVariables()[2], getDynamicMapVariables()[3]));
			getDynamicMap().setName(player.getDisplayName()+" - "+getClass().getSimpleName());
			baseX = getDynamicMap().getBaseX();
			baseY = getDynamicMap().getBaseY();
			getDynamicMap().createCopiedMap();
			if(player.getRights() > 1)
				player.sm(getDynamicMap().toString());
		}
		getData().put("originalLocation", new Location(player.getX(), player.getY(), player.getZ()));
		NPC npc = null;
		if(getNPCId() > -1) {
			player.setTimeUntilWrongLocation(System.currentTimeMillis());
			npc = new NPC(getNPCId(), Location.findEmptyLocation(getPlayer(), 1), false);
			npc.setNextGraphics(new Graphics(189)); // Puff of smoke
			npc.setNextFaceEntity(getPlayer());
			npc.setNextForceTalk(new ForceTalk(getNPCMessages()[Misc.random(getNPCMessages().length)]));
		}
		final NPC n = npc != null ? npc : null;
		Magic.telePlayer(player, getEventLocation(), false);
		if(getTabsRemoved() != null && getTabsRemoved().length > 0)
			removeTabs();
		Game.submit(new GameTick(2.6) {
			public void run() {
				stop();
				player.setCantChangeLocation(true);
				if(player.getRandomEvent().getCurrent() == null) {
					player.getRandomEvent().getCurrent().forceStop();
					return;
				}
				if(n != null)
					n.finish();
				if(hasHiddenMiniMap())
					getPlayer().packets().sendBlackOut(2);
				startEvent();
				canLogout(true);
				player.setCantMove(false);
				player.setCantChangeLocation(false);
			}
		});
	}
	
	
	
	/**
	 * Remove tabs for the random Event.
	 */
	protected void removeTabs() {
		for(int i = 0; i < getTabsRemoved().length; i++)
			getPlayer().packets().closeInterface(getTabsRemoved()[i]);
	}
	
	/**
	 * Finds an NPC within the player's proximity.
	 * @param id
	 * @return
	 */
	public NPC findNPC(int id) {
		for (NPC npc : Game.getLocalNPCs(player))
			if (npc.getId() == id && npc != null)
				return npc;
		return null;
	}
	
	/**
	 * Saves player's original location before doing random event.
	 * @return
	 */
	public Location getOriginalLocation() {
		Location loc = (Location) getData().get("originalLocation");
		if(loc == null)
			return Constants.RESPAWN_PLAYER_LOCATION;
		if(toWrongLocation)
			return WRONG_LOCATIONS[Misc.random(WRONG_LOCATIONS.length)];
		else
			return loc;
	}
	
	/**
	 * Returns to remove all tabs
	 * @return
	 */
	protected Integer[] allTabs() {
		return new Integer[] {
			tab(ATTACK_STYLES),
			tab(ACHIEVEMENTS),
			tab(SKILLS),
			tab(QUESTS),
			tab(INVENTORY),
			tab(EQUIPMENT),
			tab(PRAYER),
			tab(MAGIC_BOOK),
			tab(FRIENDS_LIST),
			tab(IGNORES_LIST),
			tab(CLAN_CHAT),
			tab(GAME_SETTINGS),
			tab(EMOTES),
			tab(MUSIC_LIST),
			tab(NOTES)
		};
	}
	
	/**
	 * Removes a few selected tabs (Ex. In the sandwich lady and Estate Agent's randoms.)
	 * @return
	 */
	protected Integer[] selectTabs() {
		return new Integer[] {
			tab(ATTACK_STYLES), 
			tab(QUESTS), 
			tab(ACHIEVEMENTS), 
			tab(EQUIPMENT), 
			tab(PRAYER),
			tab(MAGIC_BOOK), 
			tab(EMOTES),
			tab(NOTES) 
		};
	}
	
	/**
	 * Returns all tab ids.
	 * @param id
	 * @return
	 */
	public Integer tab(int tabId) {
		return getPlayer().interfaces().fullScreen() ? 90 + tabId : 204 + tabId;
	}
	
	/**
	 * Tab ids (Used for above method)
	 */
	protected final byte 
		ATTACK_STYLES = 0,  
		ACHIEVEMENTS = 1, 
		SKILLS = 2, 
		QUESTS = 3,
		INVENTORY = 4,
		EQUIPMENT = 5,
		PRAYER = 6,
		MAGIC_BOOK = 7,
		FRIENDS_LIST = 9,
		IGNORES_LIST = 10,
		CLAN_CHAT = 11,
		GAME_SETTINGS = 12,
		EMOTES = 13,
		MUSIC_LIST = 14,
		NOTES = 15;
	
	/**
	 * An array of wrong locations.
	 */
	public static final Location[] WRONG_LOCATIONS = { 
		// Where to go if player took too long to do event or messed up in event.
        new Location(3074, 3519, 0),
        new Location(3199, 3455, 0),
        new Location(3464, 3469, 0),
        new Location(2424, 3809, 0),
        new Location(2315, 3836, 0),
        new Location(2087, 3939, 0),
	 };
	
	/**
	 * Sends a noContinue npc dialogue, but option to show component after a set amount of time.
	 * @param npcId
	 * @param delay
	 * @param lines
	 */
	protected void npcDialogue(int npcId, double delay, String title, final String[] lines) {
		player.packets().sendChatBoxInterface(checkNPCLines(lines));
		player.packets().animateInterface(Dialogue.NORMAL_TALKING, checkNPCLines(lines), 2);
		player.packets().sendNPCOnInterface(npcId, checkNPCLines(lines), 2);
		player.packets().sendString(title, checkNPCLines(lines), 3);
		for(int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i], checkNPCLines(lines), i + 4);
		player.packets().sendHideIComponent(checkNPCLines(lines), lines.length == 1 ? 5 : 
			lines.length == 2 ? 6 : lines.length == 3 ? 7 : lines.length == 4 ? 8 : 5, true);
		if(delay > 0) {
			Game.submit(new GameTick(delay) {
				public void run() {
					player.packets().sendHideIComponent(checkNPCLines(lines), lines.length == 1 ? 5 : 
						lines.length == 2 ? 6 : lines.length == 3 ? 7 : lines.length == 4 ? 8 : 5, false);
					stop();
				}
			});
		}
	}
	
	/**
	 * Sends a nonclickable chatbox with the player in it
	 * @param lines
	 */
	protected void playerDialogue(final String[] lines, double delay) {
		player.packets().sendChatBoxInterface(checkPlayerLines(lines));
		player.packets().sendPlayerOnInterface(checkPlayerLines(lines), 
				checkPlayerLines(lines) == 64 ? 1 : 2);
		player.packets().animateInterface(Dialogue.NORMAL_TALKING, checkPlayerLines(lines),
				checkPlayerLines(lines) == 64 ? 1 : 2);
		player.packets().sendString(player.getDisplayName(), checkPlayerLines(lines), 3);
		for(int i = 0; i < lines.length; i++)
			player.packets().sendString(lines[i], checkPlayerLines(lines), i + 4);
		player.packets().sendHideIComponent(checkPlayerLines(lines), lines.length == 1 ? 5 : 
			lines.length == 2 ? 6 : lines.length == 3 ? 7 : lines.length == 4 ? 8 : 5, true);
		if(delay > 0) {
			Game.submit(new GameTick(delay) {
				public void run() {
					player.packets().sendHideIComponent(checkPlayerLines(lines), lines.length == 1 ? 5 : 
						lines.length == 2 ? 6 : lines.length == 3 ? 7 : lines.length == 4 ? 8 : 5, false);
					stop();
				}
			});
		}
	}
	
	/**
	 * 
	 * @param lines
	 * @return
	 */
	protected int checkPlayerLines(String[] lines) {
		return lines.length == 1 ? 64 : lines.length == 2 ? 65 : lines.length == 3 ? 66 : 67;
	}

	/**
	 * 
	 * @param lines
	 * @return
	 */
	protected int checkNPCLines(String[] lines) {
		return lines.length == 1 ? 241 : lines.length == 2 ? 242 : lines.length == 3 ? 243 : 244;
	}

	/**
	 * Can the player logout?
	 * 
	 * @return
	 */
	public boolean canLogout() {
		return canLogout;
	}

	/**
	 * Sets whether the player can log out or not.
	 * @param log
	 */
	public void canLogout(boolean log) {
		this.canLogout = log;
	}
	
	/**
	 * Generated map.
	 * @return
	 */
	public DynamicMap getDynamicMap() {
		return dynamicMap;
	}

	/**
	 * 
	 * @param dynamicMap
	 */
	public void setDynamicMap(DynamicMap dynamicMap) {
		this.dynamicMap = dynamicMap;
	}
	
	/**
	 * Returns saved data.
	 * @return
	 */
	public HashMap<String, Object> getData() {
		return player.getRandomEvent().getData();
	}
	
	/**
	 * 
	 */
	public void fullyStop() {
		player.getRandomEvent().fullyStop();
	}
	
	/**
	 * 
	 * @param b
	 */
	public void fullyStop(boolean b) {
		player.getRandomEvent().fullyStop(b);
	}

}
