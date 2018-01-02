package org.nova.game.player;

import java.io.Serializable;
import java.util.Arrays;

import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.definition.NPCDefinition;
import org.nova.cache.loaders.ClientScriptMap;
import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.network.stream.OutputStream;
import org.nova.utility.misc.Misc;

public class Appearance implements Serializable {

	private static final long serialVersionUID = 7655608569741626586L;

	private transient int renderEmote;
	private int title;
	private int[] currentLook;
	private byte[] colour;
	private boolean male;
	private boolean glowingRed;
	private transient byte[] appeareanceData;
	private transient byte[] md5AppeareanceDataHash;
	private transient short transformedNpcId;
	private transient boolean hidePlayer;

	private transient Player player;

	public Appearance() {
		male = true;
		renderEmote = -1;
		title = -1;
		resetAppearence();
	}

	public void setPlayer(Player player) {
		this.player = player;
		transformedNpcId = -1;
		renderEmote = -1;
		if (currentLook == null)
			resetAppearence();
	}

	public void transformIntoNPC(int id) {
		transformedNpcId = (short) id;
		generateAppearanceData();
	}

	public void switchHidden() {
		hidePlayer = !hidePlayer;
		generateAppearanceData();
	}

	public boolean isHidden() {
		return hidePlayer;
	}

	public void generateAppearanceData() {
		OutputStream stream = new OutputStream();
		int flag = 0;
		if (glowingRed && player.getBodyGlow() == null)
			player.glow(90, new int[] { 0, 0, 0, 255 });
		if (!male)
			flag |= 0x1;
		if (transformedNpcId >= 0
				&& NPCDefinition.get(transformedNpcId).unknownBoolean7)
			flag |= 0x2;
		stream.writeByte(flag);
		stream.writeByte(title);
		if(title == 26)
			writeTitleStream(stream, player.hasTitle() ? player.getTitle() : "", false);
		else
			writeTitleStream(stream, ClientScriptMap.getMap(male ? 1093 : 3872).getStringValue(title), false);
		stream.writeByte(player.hasSkull() ? player.getSkullId() : -1);
		stream.writeByte(player.getCustomHeadIcon() > -1 ? player
				.getCustomHeadIcon() : player.getPrayer().getPrayerHeadIcon());
		stream.writeByte(hidePlayer ? 1 : 0);
		if (transformedNpcId >= 0) {
			stream.writeShort(-1);
			stream.writeShort(transformedNpcId);
			stream.writeByte(0);
		} else {
			for (int index = 0; index < 4; index++) {
				Item item = player.getEquipment().getItems().get(index);
				if (item == null)
					stream.writeByte(0);
				else
					stream.writeShort(32768 + item.getEquipId());
			}
			Item item = player.getEquipment().getItems()
					.get(Equipment.SLOT_CHEST);
			stream.writeShort(item == null ? 0x100 + currentLook[2]
					: 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_SHIELD);
			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_CHEST);
			if (item == null || !Equipment.isFullBody(item)) // ||
																// !Equipment.hideArms(item)
																// <-- this line
																// b4
				stream.writeShort(0x100 + currentLook[3]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_LEGS);
			stream.writeShort(item == null ? 0x100 + currentLook[5]
					: 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_HAT);
			if ((item == null || (!Equipment.isFullMask(item) && !Equipment
					.isFullHat(item))))
				stream.writeShort(0x100 + currentLook[0]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_HANDS);
			stream.writeShort(item == null ? 0x100 + currentLook[4]
					: 32768 + item.getEquipId());
			item = player.getEquipment().getItems().get(Equipment.SLOT_FEET);
			stream.writeShort(item == null ? 0x100 + currentLook[6]
					: 32768 + item.getEquipId());
			// tits for female, bear for male
			item = player.getEquipment().getItems()
					.get(male ? Equipment.SLOT_HAT : Equipment.SLOT_CHEST);
			if (item == null || (male && Equipment.showBear(item)))
				stream.writeShort(0x100 + currentLook[1]);
			else
				stream.writeByte(0);
			item = player.getEquipment().getItems().get(Equipment.SLOT_AURA);
			if (item == null)
				stream.writeByte(0);
			else
				stream.writeShort(16384 + item.getId());
			int pos = stream.getOffset();
			stream.writeShort(0);
			int hash = 0;
			int slotFlag = -1;
			for (int slotId = 0; slotId < player.getEquipment().getItems()
					.getSize(); slotId++) {
				if (Equipment.DISABLED_SLOTS[slotId] != 0)
					continue;
				slotFlag++;
				if (slotId == Equipment.SLOT_HAT) {
					int hatId = player.getEquipment().getHatId();
					if (hatId == 20768 || hatId == 20770 || hatId == 20772) {
						ItemDefinition defs = ItemDefinition.get(hatId - 1);
						if ((hatId == 20768
								&& Arrays.equals(
										player.getMaxedCapeCustomized(),
										defs.originalModelColors) || ((hatId == 20770 || hatId == 20772) && Arrays
								.equals(player.getCompletionistCapeCustomized(),
										defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] hat = hatId == 20768 ? player
								.getMaxedCapeCustomized() : player
								.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(hat[i]);
					}
				} else if (slotId == Equipment.SLOT_CAPE) {
					int capeId = player.getEquipment().getCapeId();
					if (capeId == 20767 || capeId == 20769 || capeId == 20771) {
						ItemDefinition defs = ItemDefinition.get(capeId);
						if ((capeId == 20767
								&& Arrays.equals(
										player.getMaxedCapeCustomized(),
										defs.originalModelColors) || ((capeId == 20769 || capeId == 20771) && Arrays
								.equals(player.getCompletionistCapeCustomized(),
										defs.originalModelColors))))
							continue;
						hash |= 1 << slotFlag;
						stream.writeByte(0x4); // modify 4 model colors
						int[] cape = capeId == 20767 ? player
								.getMaxedCapeCustomized() : player
								.getCompletionistCapeCustomized();
						int slots = 0 | 1 << 4 | 2 << 8 | 3 << 12;
						stream.writeShort(slots);
						for (int i = 0; i < 4; i++)
							stream.writeShort(cape[i]);
					}
				} else if (slotId == Equipment.SLOT_AURA) {
					int auraId = player.getEquipment().getAuraId();
					boolean isActivated = player.getAuraManager().isActivated();
					if (auraId == -1 || !isActivated && auraId < 22440)
						continue;
					ItemDefinition auraDefs = ItemDefinition.get(auraId);
					if (auraDefs.getMaleWornModelId1() == -1
							|| auraDefs.getFemaleWornModelId1() == -1)
						continue;
					hash |= 1 << slotFlag;
					stream.writeByte(0x1); // modify model ids
					int modelId = player.getAuraManager().getAuraModelId();
					if(auraDefs.name.toLowerCase().contains("aura"))
						modelId = auraDefs.getMaleWornModelId1();
					stream.writeBigSmart(modelId); // male modelid1
					stream.writeBigSmart(modelId); // female modelid1
					if (auraDefs.getMaleWornModelId2() != -1
							|| auraDefs.getFemaleWornModelId2() != -1) {
						int modelId2 = player.getAuraManager()
								.getAuraModelId2();
						stream.writeBigSmart(modelId2);
						stream.writeBigSmart(modelId2);
					}
				}
			}
			int pos2 = stream.getOffset();
			stream.setOffset(pos);
			stream.writeShort(hash);
			stream.setOffset(pos2);
		}

		for (int index = 0; index < colour.length; index++)
			// colour length 10
			stream.writeByte(colour[index]);
		if (player.render() > 0)
			stream.writeShort(player.render());
		else
			stream.writeShort(getRenderEmote());
		stream.writeString(player.getDisplayName());
		boolean pvpArea = Game.pvpArea(player);
		stream.writeByte(pvpArea ? player.getSkills().getCombatLevel() : player
				.getSkills().getCombatLevelWithSummoning());
		stream.writeByte(pvpArea ? player.getSkills()
				.getCombatLevelWithSummoning() : 0);
		stream.writeByte(-1); // higher level acc name appears in front :P
		stream.writeByte(transformedNpcId >= 0 ? 1 : 0); // to end here else id
															// need to send more
															// data
		if (transformedNpcId >= 0) {
			NPCDefinition defs = NPCDefinition.get(transformedNpcId);
			stream.writeShort(defs.unknownInt8);
			stream.writeShort(defs.unknownInt9);
			stream.writeShort(defs.unknownInt10);
			stream.writeShort(defs.unknownInt11);
			stream.writeByte(defs.unknownInt12);
		}

		// done separated for safe because of synchronization
		byte[] appeareanceData = new byte[stream.getOffset()];
		System.arraycopy(stream.getBuffer(), 0, appeareanceData, 0,
				appeareanceData.length);
		byte[] md5Hash = Misc.encryptUsingMD5(appeareanceData);
		this.appeareanceData = appeareanceData;
		md5AppeareanceDataHash = md5Hash;
	}
	
	public void writeTitleStream(OutputStream stream, String title, boolean inFrontOfName) {
		stream.writeString(title);
		stream.writeByte(inFrontOfName ? 1 : 0);
	}

	public int getSize() {
		if (transformedNpcId >= 0)
			return NPCDefinition.get(transformedNpcId).size;
		return 1;
	}

	public void setRenderEmote(int id) {
		this.renderEmote = id;
		generateAppearanceData();
	}

	public int getBootColor() {
		return colour[5];
	}

	public int getRenderEmote() {
		if (renderEmote >= 0)
			return renderEmote;
		if (transformedNpcId >= 0)
			return NPCDefinition.get(transformedNpcId).renderEmote;
		return player.getEquipment().getWeaponRenderEmote();
	}

	public void setColor(int index, int color) {
		this.colour[index] = (byte) color;
	}

	public void resetAppearence() {
		currentLook = new int[7];
		colour = new byte[10];
		male();
	}

	public void male() {
		currentLook[0] = 5; // Hair
		currentLook[1] = 16; // Beard
		currentLook[2] = 461; // Torso
		currentLook[3] = 26; // Arms
		currentLook[4] = 34; // Bracelets
		currentLook[5] = 638; // Legs
		currentLook[6] = 42; // Shoes~

		colour[2] = 9;
		colour[1] = 17;
		colour[0] = 12;
		male = true;
	}

	public void female() {
		currentLook[0] = 272; // Hair
		currentLook[1] = 57; // Beard
		currentLook[2] = 587; // Torso
		currentLook[3] = 65; // Arms
		currentLook[4] = 68; // Bracelets
		currentLook[5] = 492; // Legs
		currentLook[6] = 80; // Shoes

		colour[2] = 19;
		colour[1] = 16;
		colour[0] = 6;
		male = false;
	}

	public byte[] getAppeareanceData() {
		return appeareanceData;
	}

	public byte[] getMD5AppeareanceDataHash() {
		return md5AppeareanceDataHash;
	}

	public boolean isMale() {
		return male;
	}

	public void setLook(int i, int i2) {
		currentLook[i] = i2;
	}

	public void setMale(boolean male) {
		this.male = male;
	}

	public void setHairStyle(int i) {
		currentLook[0] = i;
	}

	public void setTopStyle(int i) {
		currentLook[2] = i;
	}

	public int getTopStyle() {
		return currentLook[2];
	}

	public void setArmsStyle(int i) {
		currentLook[3] = i;
	}

	public void setWristsStyle(int i) {
		currentLook[4] = i;
	}

	public void setLegsStyle(int i) {
		currentLook[5] = i;
	}

	public int getHairStyle() {
		return currentLook[0];
	}

	public void setBeardStyle(int i) {
		currentLook[1] = i;
	}

	public int getBeardStyle() {
		return currentLook[1];
	}

	public void setFacialHair(int i) {
		currentLook[1] = i;
	}

	public int getFacialHair() {
		return currentLook[1];
	}

	public void setSkinColor(int color) {
		colour[4] = (byte) color;
	}

	public int getSkinColor() {
		return colour[4];
	}

	public void setHairColor(int color) {
		colour[0] = (byte) color;
	}

	public void setTopColor(int color) {
		colour[1] = (byte) color;
	}

	public void setLegsColor(int color) {
		colour[2] = (byte) color;
	}

	public int getHairColor() {
		return colour[0];
	}

	public int getTitle() {
		return title;
	}

	public void setTitle(int title) {
		this.title = title;
		generateAppearanceData();
	}

	public void setBootsColor(int color) {
		colour[3] = (byte) color;
	}

	public void setBootsStyle(int i) {
		currentLook[6] = i;
	}

	public boolean isGlowingRed() {
		return glowingRed;
	}

	public void setGlowingRed(boolean glowingRed) {
		this.glowingRed = glowingRed;
		generateAppearanceData();
	}
}