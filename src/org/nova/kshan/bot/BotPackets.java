package org.nova.kshan.bot;

import java.io.Serializable;

import org.nova.game.entity.Entity;
import org.nova.game.item.FloorItem;
import org.nova.game.item.Item;
import org.nova.game.item.ItemsContainer;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.npc.NPC;
import org.nova.game.player.HintIcon;
import org.nova.game.player.Player;
import org.nova.game.player.PublicChatMessage;
import org.nova.game.player.QuickChatMessage;
import org.nova.game.player.Skills;
import org.nova.network.encoders.PacketSender;

/**
 * 
 * @author K-Shan
 *
 */
public class BotPackets extends PacketSender implements Serializable {
	
	/**
	 * @serialField
	 */
	private static final long serialVersionUID = -5377713721030056315L;
	
	private Player player;

	public BotPackets(Player player) {
		super(null, player);
		this.player = player;
	}
	
	public void setPlayer(Player p) {
		player = p;
	}
	
	public Player getPlayer() {
		return player;
	}

	@Override
	public void resetGe(int slot) {
	}

	@Override
	public void setGeSearch(Object[] o) {
	}

	@Override
	public void sendGameBarStages() {
	}

	@Override
	public void sendPrivateGameBarStage() {
	}

	@Override
	public void sendResetMinimapFlag() {
	}

	@Override
	public void closeExchangeSearch() {
	}

	@Override
	public void sendBlackOut(int area) {
	}

	@Override
	public void sendJoinClanChat(String username, String clanName) {
	}

	@Override
	public void setGe(Player player, int slot, int progress, int item,
			int price, int amount, int currentAmount) {
	}

	@Override
	public void sendItems(int key, ItemsContainer<Item> items) {
	}

	@Override
	public void sendItems(int key, boolean keyLessIntegerSize,
			ItemsContainer<Item> items) {
	}

	@Override
	public void sendItems(int key, Item[] items) {
	}

	@Override
	public void sendItems(int key, boolean negativeKey, Item[] items) {
	}

	@Override
	public void setItemSlot(int slot, int item, int amount) {
	}

	@Override
	public void setItemSlot(int slot, int[] item, int[] amount) {
	}

	@Override
	public void resetItemSlot(Player p, int slot) {
	}

	@Override
	public void sendShopLoadMainStock(int shopId) {
	}

	@Override
	public void sendOverlay(int inter) {
	}

	@Override
	public void sendRemoveOverlay() {
	}

	@Override
	public void sendPlayerUnderNPCPriority(boolean priority) {
	}

	@Override
	public void sendHintIcon(HintIcon icon) {
	}

	@Override
	public void sendAccessMask(int set1, int set2, int interfaceId1,
			int childId1, int interfaceId2, int childId2) {
	}

	@Override
	public void sendAccessMask(int set, int inter, int child, int off, int len) {
	}

	@Override
	public void sendItems(int interfaceId, int childId, int type,
			Item[] inventory) {
	}

	@Override
	public void sendItems(int interfaceId, int childId, int type,
			int[] itemArray, int[] itemAmt) {
	}

	@Override
	public void sendCameraShake(int slotId, int b, int c, int d, int e) {
	}

	@Override
	public BotPackets setInterfaceConfig(int interfaceId, int childId,
			boolean set) {
		return this;
	}

	@Override
	public void sendStopCameraShake() {
	}

	@Override
	public void sendIComponentModel(int interfaceId, int componentId,
			int modelId) {
	}

	@Override
	public void sendScrollIComponent(int interfaceId, int componentId, int value) {
	}

	@Override
	public void sendHideIComponent(int interfaceId, int componentId,
			boolean hidden) {
	}

	@Override
	public void sendRemoveGroundItem(FloorItem item) {
	}

	@Override
	public void sendGroundItem(FloorItem item) {
	}

	@Override
	public void sendProjectile(Entity receiver, Location startTile,
			Location endTile, int gfxId, int startHeight, int endHeight,
			int speed, int delay, int curve, int startDistanceOffset,
			int creatorSize) {
	}

	@Override
	public void sendUnlockIComponentOptionSlots(int interfaceId,
			int componentId, int fromSlot, int toSlot, int... optionsSlots) {
	}

	@Override
	public void sendIComponentSettings(int interfaceId, int componentId,
			int fromSlot, int toSlot, int settingsHash) {
	}

	@Override
	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, int width, int height, String... options) {
	}

	@Override
	public void runScript(int id, Object[] params, String types) {
	}

	@Override
	public void sendRunScript(int scriptId, Object... params) {
	}

	@Override
	public void sendGlobalConfig(int id, int value) {
	}

	@Override
	public void sendGlobalConfig1(int id, int value) {
	}

	@Override
	public void sendGlobalConfig2(int id, int value) {
	}

	@Override
	public void sendRunEnergy() {
	}

	@Override
	public void sendIComponentText(int interfaceId, int componentId, String text) {
	}

	@Override
	public void sendIComponentAnimation(int emoteId, int interfaceId,
			int componentId) {
	}

	@Override
	public void sendItemOnIComponent(int interfaceid, int componentId, int id,
			int amount) {
	}

	@Override
	public void sendEntityOnIComponent(boolean isPlayer, int entityId,
			int interfaceId, int componentId) {
	}

	@Override
	public void sendLocation(Location tile) {
	}

	@Override
	public void sendObjectAnimation(GlobalObject object, Animation animation) {
	}

	@Override
	public void sendTileMessage(String message, Location tile, int color) {
	}

	@Override
	public void sendTileMessage(String message, Location tile, int delay,
			int height, int color) {
	}

	@Override
	public void sendSpawnedObject(GlobalObject object) {
	}

	@Override
	public void sendDestroyObject(GlobalObject object) {
	}

	@Override
	public void sendPlayerOnIComponent(int interfaceId, int componentId) {
	}

	@Override
	public void sendNPCOnIComponent(int interfaceId, int componentId, int npcId) {
	}

	@Override
	public void sendRandomOnIComponent(int interfaceId, int componentId, int id) {
	}

	@Override
	public void sendFaceOnIComponent(int interfaceId, int componentId,
			int look1, int look2, int look3) {
	}

	@Override
	public void sendFriendsChatChannel() {
	}

	@Override
	public void sendFriend(String username, String displayName, int world,
			boolean putOnline, boolean warnMessage) {
	}

	@Override
	public void sendIgnore(String name, String display) {
	}

	@Override
	public void sendPrivateMessage(String username, String message) {
	}

	@Override
	public void sendAcceptAidStatus() {
	}

	@Override
	public void receivePrivateMessage(String name, String display, int rights,
			String message) {
	}

	@Override
	public void receivePrivateChatQuickMessage(String name, String display,
			int rights, QuickChatMessage message) {
	}

	@Override
	public void sendPrivateQuickMessageMessage(String username,
			QuickChatMessage message) {
	}

	@Override
	public void receiveFriendChatMessage(String name, String display,
			int rights, String chatName, String message) {
	}

	@Override
	public void receiveFriendChatQuickMessage(String name, String display,
			int rights, String chatName, QuickChatMessage message) {
	}

	@Override
	public void sendUnlockIgnoreList() {
	}

	@Override
	public void sendUnlockFriendList() {
	}

	@Override
	public void sendDynamicMapRegion(boolean wasAtDynamicRegion) {
	}

	@Override
	public void sendMapRegion(boolean sendLswp) {
	}

	@Override
	public void sendCutscene(int id) {
	}

	@Override
	public void sendWindowsPane(int id, int type) {
	}

	@Override
	public void sendPlayerOption(String option, int slot, boolean top) {
	}

	@Override
	public void sendPublicMessage(Player p, PublicChatMessage message) {
	}

	@Override
	public void sendPlayerOption(String option, int slot, boolean top,
			int cursor) {
	}

	@Override
	public void sendLocalPlayersUpdate() {
	}

	@Override
	public void sendLocalNPCsUpdate() {
	}

	@Override
	public void sendGraphics(Graphics graphics, Object target) {
	}

	@Override
	public void sendDelayedGraphics(Graphics graphics, int delay, Location tile) {
	}

	@Override
	public void closeInterface(int windowComponentId) {
	}

	@Override
	public void closeInterface(int windowId, int windowComponentId) {
	}

	@Override
	public void sendInterface(int showId, int windowId, int interfaceId,
			int childId) {
	}

	@Override
	public void sendInterface(boolean nocliped, int windowId,
			int windowComponentId, int interfaceId) {
	}

	@Override
	public void sendSystemUpdate(int delay) {
	}

	@Override
	public void sendUpdateItems(int key, ItemsContainer<Item> items,
			int... slots) {
	}

	@Override
	public void sendUpdateItems(int key, Item[] items, int... slots) {
	}

	@Override
	public void sendUpdateItems(int key, boolean negativeKey, Item[] items,
			int... slots) {
	}

	@Override
	public void sendGlobalString(int id, String string) {
	}

	@Override
	public void sendExecMessage(String command) {
	}

	@Override
	public void sendLogout() {
	}

	@Override
	public void sendMessage(String text) {
	}

	@Override
	public void sendMessage(String text, boolean filter) {
	}

	@Override
	public void sendPanelBoxMessage(String text) {
	}

	@Override
	public void sendTradeRequestMessage(Player p) {
	}

	@Override
	public void sendClanWarsRequestMessage(Player p) {
	}

	@Override
	public void sendDuelRequest(Player p, boolean friendly) {
	}

	@Override
	public void sendMessage(int type, String text, Player p) {
	}

	@Override
	public void sendSound(int id, int delay, int effectType) {
	}

	@Override
	public void sendVoice(int id) {
	}

	@Override
	public void resetSounds() {
	}

	@Override
	public void sendIndex14Sound(int id, int delay) {
	}

	@Override
	public void sendIndex15Sound(int id, int delay) {
	}

	@Override
	public void sendMusicEffect(int id) {
	}

	@Override
	public void sendMusic(int id) {
	}

	@Override
	public void sendMusic(int id, int delay, int volume) {
	}

	@Override
	public void sendSkillLevel(int skill) {
	}

	@Override
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ) {
		sendCameraLook(viewLocalX, viewLocalY, viewZ, -1, -1);
	}

	@Override
	public void sendCameraLook(int viewLocalX, int viewLocalY, int viewZ,
			int speed1, int speed2) {
	}

	@Override
	public void setGe(int slot, int progress, int item, int price, int amount,
			int currentAmount) {
	}

	@Override
	public void sendResetCamera() {
	}

	@Override
	public void sendCameraRotation(int unknown1, int unknown2) {
	}

	@Override
	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ) {
		sendCameraPos(moveLocalX, moveLocalY, moveZ, -1, -1);
	}

	@Override
	public void sendCameraPos(int moveLocalX, int moveLocalY, int moveZ,
			int speed1, int speed2) {
	}

	@Override
	public void sendOpenURL(String url) {
	}

	@Override
	public void sendInputNameScript(String message) {
		sendRunScript(109, new Object[] { message });
	}

	@Override
	public void sendInputIntegerScript(boolean integerEntryOnly, String message) {
		sendRunScript(108, new Object[] { message });
	}

	@Override
	public void sendInputLongTextScript(String message) {
		sendRunScript(110, new Object[] { message });
	}

	@Override
	public void sendItemsLook() {
	}

	@Override
	public void sendInterFlashScript(int interfaceId, int componentId,
			int width, int height, int slot) {
	}

	@Override
	public void sendInterSetItemsOptionsScript(int interfaceId,
			int componentId, int key, boolean negativeKey, int width,
			int height, String... options) {
	}

	@Override
	public void sendClanChatChannel() {
	}

	@Override
	public void sendAccessMask(Player player, int min, int max,
			int interfaceId, int childId, int hash) {
	}

	@Override
	public void sendRunScriptBlank(int scriptId) {

	}

	@Override
	public void sendMinimapFlag(int x, int y) {
	}

	@Override
	public void sendRootInterface(int id, int type) {
	}

	@Override
	public void sendConfigByFile(int fileId, int value) {
	}

	@Override
	public void sendConfig1(int id, int value) {
	}

	@Override
	public void sendConfig2(int id, int value) {
	}

	@Override
	public void sendConfigByFile1(int fileId, int value) {
	}

	@Override
	public void sendConfigByFile2(int fileId, int value) {
	}

	@Override
	public void sendNPCMessage(int border, NPC npc, String message) {
		sendMessage(message);
	}

	@Override
	public void sendSkillLevels() {
		for (int i = 0; i < Skills.SKILL_COUNT; i++)
			sendSkillLevel(i);
	}

	@Override
	public void sendString(String text, int interfaceId, int componentId) {
	}

	@Override
	public void sendChatBoxInterface(int interfaceId) {
		
	}

	@Override
	public void sendPlayerOnInterface(int inter, int button) {
	}

	@Override
	public void sendNPCOnInterface(int npcId, int inter, int button) {
	}

	@Override
	public void animateInterface(int emoteId, int interfaceId, int componentId) {
	}

	@Override
	public void sendCloseInterface() {
	}

	@Override
	public void sendUnlockIComponentOptionSlots(int interfaceId,
			int componentId, int fromSlot, int toSlot, boolean unlockEvent,
			int... optionsSlots) {
	}
}
