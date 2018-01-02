package org.nova.cache.definition;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.cache.stream.OutputStream;
import org.nova.cache.utility.CacheConstants;
import org.nova.game.player.Equipment;
import org.nova.game.player.Player;
import org.nova.game.player.Skills;
import org.nova.kshan.content.interfaces.InterfaceConstants;
import org.nova.kshan.utilities.ArrayUtils;
import org.nova.network.stream.InputStream;
import org.nova.utility.misc.Misc;

@SuppressWarnings("unused")
public final class ItemDefinition implements Cloneable {

	public static final ConcurrentHashMap<Integer, ItemDefinition> itemDefinitions = 
		new ConcurrentHashMap<Integer, ItemDefinition>(30000);

	private boolean aBool8089;
	public HashMap<Integer, Object> clientScriptData;
	public int equipSlot;
	public int equipType;
	public int femaleEquipModelId1;
	public int femaleEquipModelId2;
	public int femaleEquipModelId3;
	public String[] groundOptions;
	public int id;
	public String[] inventoryOptions;
	public int invModelId;
	public int invModelZoom;
	private HashMap<Integer, Integer> itemRequirements;
	private boolean lended;
	public int lentItemId;
	public int lentModelId;
	private boolean loaded;
	public int maleEquipModelId1;
	public int maleEquipModelId2;
	public int maleEquipModelId3;
	public boolean membersOnly;
	public int modelOffsetX;
	public int modelOffsetY;
	public int modelRotationX;
	public int modelRotationY;
	public String name;
	private boolean noted;
	public int notedItemId;
	public int notedModelId;
	private int opcode116;
	private int opcode117;
	private int opcode13;
	private int opcode14;
	private int opcode151;
	private int opcode157;
	private int opcode170;
	private int opcode244;
	private int opcode27;
	private int opcode66;
	private int opcode82;
	private int opcode9;
	private int opcode18;
	private int opcode134;
	public int[] originalModelColors;
	public int[] originalTextures;
	public int[] modifiedModelColors;
	public int[] modifiedTextures;
	public int stackable;
	public int[] stackAmounts;
	public int[] stackIds;
	public int teamId;
	public byte[] unknownArray1;
	public int[] unknownArray2;
	public byte[] unknownArray3;
	public int[] unknownArray4;
	public int[] unknownArray5;
	public byte[] unknownArray6;
	public int unknownInt1;
	public int unknownInt10;
	public int unknownInt11;
	public int unknownInt12;
	public int unknownInt13;
	public int unknownInt14;
	public int unknownInt15;
	public int unknownInt16;
	public int unknownInt17;
	public int unknownInt18;
	public int unknownInt19;
	public int unknownInt2;
	public int unknownInt20;
	public int unknownInt21;
	public int unknownInt22;
	public int unknownInt23;
	public int unknownInt3;
	public int unknownInt4;
	public int unknownInt5;
	public int unknownInt6;
	public int unknownInt7;
	public int unknownInt8;
	public int unknownInt9;
	
	// Something to do with lent items..
	public int unknownValue1;
	public int unknownValue2;

	public boolean unnoted;
	public int value;

	public ItemDefinition(Cache cache, int id) {
		this(cache, id, true);
	}

	public ItemDefinition(Cache cache, int id, boolean load) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		if (load)
			loadItemDefinitions(cache);
	}
	
	private final void loadItemDefinitions(Cache cache) {
		byte[] data = cache.getIndices()[CacheConstants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			System.out.println("Couldn't load item: " + id + ".");
			return;
		}
		readOpcodeValues(new InputStream(data));
		if (notedModelId != -1 && invModelId <= 0)
			toNote();
		if (lentModelId != -1 && invModelId <= 0)
			toLend(false);
		if (unknownValue1 != -1)
			toLend(true);
		loaded = true;
	}

	public ItemDefinition(int id) {
		this(Cache.INSTANCE, id, true);
	}
	
	public static final ItemDefinition get(Cache cache, int itemId) {
		return get(cache, itemId, true);
	}

	public static final ItemDefinition get(Cache cache, int itemId, boolean load) {
		ItemDefinition def = itemDefinitions.get(Integer.valueOf(itemId));
		if (def == null) {
			def = new ItemDefinition(cache, itemId, load);
			itemDefinitions.put(Integer.valueOf(itemId), def);
		}
		return def;
	}

	public static final ItemDefinition get(int itemId) {
		return get(Cache.INSTANCE, itemId);
	}

	public void changeModelColor(int originalModelColor, int modifiedModelColor) {
		if (this.originalModelColors != null) {
			for (int i = 0; i < this.originalModelColors.length; i++)
				if (this.originalModelColors[i] == originalModelColor) {
					this.modifiedModelColors[i] = modifiedModelColor;
					return;
				}
			int[] newOriginalModelColors = Arrays.copyOf(this.originalModelColors, this.originalModelColors.length + 1);
			int[] newModifiedModelColors = Arrays.copyOf(this.modifiedModelColors, this.modifiedModelColors.length + 1);
			newOriginalModelColors[(newOriginalModelColors.length - 1)] = originalModelColor;
			newModifiedModelColors[(newModifiedModelColors.length - 1)] = modifiedModelColor;
			this.originalModelColors = newOriginalModelColors;
			this.modifiedModelColors = newModifiedModelColors;
		} else {
			this.originalModelColors = new int[] { originalModelColor };
			this.modifiedModelColors = new int[] { modifiedModelColor };
		}
	}

	public void changeModelTexture(int originalTexture, int modifiedTexture) {
		if (this.originalTextures != null) {
			for (int i = 0; i < this.originalTextures.length; i++)
				if (this.originalTextures[i] == originalTexture) {
					this.modifiedTextures[i] = modifiedTexture;
					return;
				}
			int[] newOriginalTextures = Arrays.copyOf(this.originalTextures, this.originalTextures.length + 1);

			int[] newModifiedTextures = Arrays.copyOf(this.modifiedTextures, this.modifiedTextures.length + 1);

			newOriginalTextures[(newOriginalTextures.length - 1)] = originalTexture;
			newModifiedTextures[(newModifiedTextures.length - 1)] = modifiedTexture;
			this.originalTextures = newOriginalTextures;
			this.modifiedTextures = newModifiedTextures;
		} else {
			this.originalTextures = new int[] { originalTexture };
			this.modifiedTextures = new int[] { modifiedTexture };
		}
	}

	@Override
	public ItemDefinition clone() {
		try {
			return (ItemDefinition) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean containsOption(int i, String option) {
		if (inventoryOptions == null || inventoryOptions[i] == null || inventoryOptions.length <= i)
			return false;
		return inventoryOptions[i].equals(option);
	}

	public boolean containsOption(String option) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			if (o == null || !o.equalsIgnoreCase(option))
				continue;
			return true;
		}
		return false;
	}

	public boolean containsOptions(String[] ops) {
		if (inventoryOptions == null)
			return false;
		for (String o : inventoryOptions) {
			for (int i = 0; i < ops.length; i++)
				if (o == null || !o.equalsIgnoreCase(ops[i]))
					continue;
			return true;
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public byte[] encode() {
		OutputStream stream = new OutputStream();

		stream.writeByte(1);
		stream.writeBigSmart(this.invModelId);

		if ((!this.name.equals("null")) && (this.notedModelId == -1)) {
			stream.writeByte(2);
			stream.writeString(this.name);
		}

		if (this.invModelZoom != 2000) {
			stream.writeByte(4);
			stream.writeShort(this.invModelZoom);
		}

		if (this.modelRotationX != 0) {
			stream.writeByte(5);
			stream.writeShort(this.modelRotationX);
		}

		if (this.modelRotationY != 0) {
			stream.writeByte(6);
			stream.writeShort(this.modelRotationY);
		}

		if (this.modelOffsetX != 0) {
			stream.writeByte(7);
			int value = this.modelOffsetX >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}

		if (this.modelOffsetY != 0) {
			stream.writeByte(8);
			int value = this.modelOffsetY >>= 0;
			if (value < 0)
				value += 65536;
			stream.writeShort(value);
		}

		if ((this.stackable >= 1) && (this.notedModelId == -1))
			stream.writeByte(11);

		if ((this.value != 1) && (this.lentModelId == -1)) {
			stream.writeByte(12);
			stream.writeInt(this.value);
		}

		if (this.equipSlot != -1) {
			stream.writeByte(13);
			stream.writeByte(this.equipSlot);
		}

		if (this.equipType != -1) {
			stream.writeByte(14);
			stream.writeByte(this.equipType);
		}

		if ((this.membersOnly) && (this.notedModelId == -1))
			stream.writeByte(16);

		if (this.maleEquipModelId1 != -1) {
			stream.writeByte(23);
			stream.writeBigSmart(this.maleEquipModelId1);
		}

		if (this.maleEquipModelId2 != -1) {
			stream.writeByte(24);
			stream.writeBigSmart(this.maleEquipModelId2);
		}

		if (this.femaleEquipModelId1 != -1) {
			stream.writeByte(25);
			stream.writeBigSmart(this.femaleEquipModelId1);
		}

		if (this.femaleEquipModelId2 != -1) {
			stream.writeByte(26);
			stream.writeBigSmart(this.femaleEquipModelId2);
		}

		for (int index = 0; index < this.groundOptions.length; index++)
			if ((this.groundOptions[index] != null) && ((index != 2) || (!this.groundOptions[index].equals("take")))) {
				stream.writeByte(30 + index);
				stream.writeString(this.groundOptions[index]);
			}
		for (int index = 0; index < this.inventoryOptions.length; index++)
			if ((this.inventoryOptions[index] != null)
					&& ((index != 4) || (!this.inventoryOptions[index].equals("drop")))) {
				stream.writeByte(35 + index);
				stream.writeString(this.inventoryOptions[index]);
			}
		if ((this.originalModelColors != null) && (this.modifiedModelColors != null)) {
			stream.writeByte(40);
			stream.writeByte(this.originalModelColors.length);
			for (int index = 0; index < this.originalModelColors.length; index++) {
				stream.writeShort(this.originalModelColors[index]);
				stream.writeShort(this.modifiedModelColors[index]);
			}
		}

		if ((this.originalTextures != null) && (this.modifiedTextures != null)) {
			stream.writeByte(41);
			stream.writeByte(this.originalTextures.length);
			for (int index = 0; index < this.originalTextures.length; index++) {
				stream.writeShort(this.originalTextures[index]);
				stream.writeShort(this.modifiedTextures[index]);
			}
		}

		if (this.unknownArray1 != null) {
			stream.writeByte(42);
			stream.writeByte(this.unknownArray1.length);
			for (byte element : this.unknownArray1)
				stream.writeByte(element);
		}

		if (this.unnoted)
			stream.writeByte(65);

		if (this.maleEquipModelId3 != -1) {
			stream.writeByte(78);
			stream.writeBigSmart(this.maleEquipModelId3);
		}

		if (this.femaleEquipModelId3 != -1) {
			stream.writeByte(79);
			stream.writeBigSmart(this.femaleEquipModelId3);
		}

		if (this.unknownInt1 != 0) {
			stream.writeByte(90);
			stream.writeBigSmart(this.unknownInt1);
		}
		if (this.unknownInt2 != 0) {
			stream.writeByte(91);
			stream.writeBigSmart(this.unknownInt2);
		}
		if (this.unknownInt3 != 0) {
			stream.writeByte(92);
			stream.writeBigSmart(this.unknownInt3);
		}
		if (this.unknownInt4 != 0) {
			stream.writeByte(93);
			stream.writeBigSmart(this.unknownInt4);
		}
		if (this.unknownInt5 != 0) {
			stream.writeByte(95);
			stream.writeBigSmart(this.unknownInt5);
		}
		if (this.unknownInt6 != 0) {
			stream.writeByte(96);
			stream.writeBigSmart(this.unknownInt6);
		}

		if (this.notedItemId != -1) {
			stream.writeByte(97);
			stream.writeShort(this.notedItemId);
		}

		if (this.notedModelId != -1) {
			stream.writeByte(98);
			stream.writeShort(this.notedModelId);
		}

		if ((this.stackIds != null) && (this.stackAmounts != null))
			for (int index = 0; index < this.stackIds.length; index++)
				if ((this.stackIds[index] != 0) || (this.stackAmounts[index] != 0)) {
					stream.writeByte(100 + index);
					stream.writeShort(this.stackIds[index]);
					stream.writeShort(this.stackAmounts[index]);
				}
		if (this.unknownInt7 != 0) {
			stream.writeByte(110);
			stream.writeShort(this.unknownInt7);
		}
		if (this.unknownInt8 != 0) {
			stream.writeByte(111);
			stream.writeShort(this.unknownInt8);
		}
		if (this.unknownInt9 != 128) {
			stream.writeByte(112);
			stream.writeShort(this.unknownInt9);
		}
		if (this.unknownInt10 != 0) {
			stream.writeByte(113);
			stream.writeByte(this.unknownInt10);
		}
		if (this.unknownInt11 != 0) {
			stream.writeByte(114);
			stream.writeByte(this.unknownInt11);
		}
		if (this.teamId != 0) {
			stream.writeByte(115);
			stream.writeByte(this.teamId);
		}
		if (this.lentItemId != -1) {
			stream.writeByte(121);
			stream.writeShort(this.lentItemId);
		}

		if (this.lentModelId != -1) {
			stream.writeByte(122);
			stream.writeShort(this.lentModelId);
		}
		if ((this.unknownInt12 != 0) && (this.unknownInt13 != 0) && (this.unknownInt14 != 0)) {
			stream.writeByte(125);
			stream.writeByte(this.unknownInt12);
			stream.writeByte(this.unknownInt13);
			stream.writeByte(this.unknownInt14);
		}
		if ((this.unknownInt15 != 0) && (this.unknownInt16 != 0) && (this.unknownInt17 != 0)) {
			stream.writeByte(126);
			stream.writeByte(this.unknownInt15);
			stream.writeByte(this.unknownInt16);
			stream.writeByte(this.unknownInt17);
		}
		if ((this.unknownInt18 != 0) && (this.unknownInt19 != 0)) {
			stream.writeByte(127);
			stream.writeByte(this.unknownInt18);
			stream.writeShort(this.unknownInt19);
		}
		if ((this.unknownInt20 != 0) && (this.unknownInt21 != 0)) {
			stream.writeByte(128);
			stream.writeByte(this.unknownInt20);
			stream.writeShort(this.unknownInt21);
		}
		if ((this.unknownInt20 != 0) && (this.unknownInt21 != 0)) {
			stream.writeByte(129);
			stream.writeByte(this.unknownInt20);
			stream.writeShort(this.unknownInt21);
		}
		if ((this.unknownInt22 != 0) && (this.unknownInt23 != 0)) {
			stream.writeByte(130);
			stream.writeByte(this.unknownInt22);
			stream.writeShort(this.unknownInt23);
		}

		if (this.unknownArray2 != null) {
			stream.writeByte(132);
			stream.writeByte(this.unknownArray2.length);
			for (int element : this.unknownArray2)
				stream.writeShort(element);
		}
		if (this.unknownArray4 != null)
			for (int index = 0; index < this.unknownArray4.length; index++)
				if (this.unknownArray4[index] != -1) {
					stream.writeByte(142 + index);
					stream.writeShort(this.unknownArray4[index]);
				}
		if (this.unknownArray5 != null)
			for (int index = 0; index < this.unknownArray5.length; index++)
				if (this.unknownArray5[index] != -1) {
					stream.writeByte(150 + index);
					stream.writeShort(this.unknownArray5[index]);
				}
		Iterator i$;
		if (this.clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(this.clientScriptData.size());
			for (i$ = this.clientScriptData.keySet().iterator(); i$.hasNext();) {
				int key = ((Integer) i$.next()).intValue();
				Object value = this.clientScriptData.get(Integer.valueOf(key));
				stream.writeByte((value instanceof String) ? 1 : 0);
				stream.write24BitInt(key);
				if ((value instanceof String))
					stream.writeString((String) value);
				else
					stream.writeInt(((Integer) value).intValue());
			}
		}
		stream.writeByte(0);
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(data, 0, data.length);
		return data;
	}

	public int getArchiveId() {
		return id >>> 8;
	}

	public int getAttackSpeed() {
		if (id >= 24455 && id <= 24457)
			return 6;
		if (clientScriptData == null)
			return 4;
		Object attackSpeed = clientScriptData.get(14);
		if (attackSpeed != null && attackSpeed instanceof Integer)
			return (int) attackSpeed;
		return 4;
	}

	public int getCertId() {
		return notedItemId;
	}

	public HashMap<Integer, Object> getClientScriptData() {
		return clientScriptData;
	}

	public HashMap<Integer, Integer> createItemRequirements() {
		if (clientScriptData == null)
			return null;
		HashMap<Integer, Integer> items = new HashMap<Integer, Integer>();
		int requiredId = -1;
		int requiredAmount = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String)
				continue;
			if (key >= 538 && key <= 770) {
				if (key % 2 == 0)
					requiredId = (Integer) value;
				else
					requiredAmount = (Integer) value;
				if (requiredId != -1 && requiredAmount != -1) {
					items.put(requiredAmount, requiredId);
					requiredId = -1;
					requiredAmount = -1;
				}
			}
		}
		return items;
	}

	public int getEquipSlot() {
		return this.equipSlot;
	}

	public int getEquipType() {
		return this.equipType;
	}

	public int getFemaleEquipModelId1() {
		return this.femaleEquipModelId1;
	}

	public int getFemaleEquipModelId2() {
		return this.femaleEquipModelId2;
	}

	public int getFemaleEquipModelId3() {
		return this.femaleEquipModelId3;
	}

	public int getFemaleWornModelId1() {
		return this.femaleEquipModelId1;
	}

	public int getFemaleWornModelId2() {
		return this.femaleEquipModelId2;
	}

	public int getFemaleWornModelId3() {
		return this.femaleEquipModelId3;
	}

	public int getFileId() {
		return 0xff & id;
	}

	public String[] getGroundOptions() {
		return this.groundOptions;
	}

	public int getId() {
		return this.id;
	}

	public String[] getInventoryOptions() {
		return this.inventoryOptions;
	}

	public int getLendedItemId() {
		return this.lentModelId;
	}

	public int getLendId() {
		return lentItemId;
	}

	public int getMaleEquipModelId1() {
		return this.maleEquipModelId1;
	}

	public int getMaleEquipModelId2() {
		return this.maleEquipModelId2;
	}

	public int getMaleEquipModelId3() {
		return this.maleEquipModelId3;
	}

	public int getMaleWornModelId1() {
		return this.maleEquipModelId1;
	}

	public int getMaleWornModelId2() {
		return this.maleEquipModelId2;
	}

	public int getMaleWornModelId3() {
		return this.maleEquipModelId3;
	}

	public int getModelOffset1() {
		return this.modelOffsetX;
	}

	public int getModelOffset2() {
		return this.modelOffsetY;
	}

	public int getModelRotation1() {
		return this.modelRotationX;
	}

	public int getModelRotation2() {
		return this.modelRotationY;
	}

	public int[] getModifiedModelColors() {
		return this.modifiedModelColors;
	}

	public int[] getModifiedTextureColors() {
		return this.modifiedTextures;
	}

	public String getName() {
		return this.name;
	}

	public int getNotedItemId() {
		return this.notedModelId;
	}

	public int[] getOriginalModelColors() {
		return this.originalModelColors;
	}

	public int[] getOriginalTextureColors() {
		return this.originalTextures;
	}

	public int getQuestId() {
		if (clientScriptData == null)
			return -1;
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer)
			return (Integer) questId;
		return -1;
	}

	public int getRenderAnimId() {
		if (clientScriptData == null)
			return 1426;
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer)
			return (Integer) animId;
		return 1426;
	}

	public int getStackable() {
		return this.stackable;
	}

	public int[] getStackAmounts() {
		return this.stackAmounts;
	}

	public int[] getStackIds() {
		return this.stackIds;
	}

	public int getSwitchLendItemId() {
		return this.lentItemId;
	}

	public int getSwitchNoteItemId() {
		return this.notedItemId;
	}

	public int getTeamId() {
		return this.teamId;
	}

	public int getValue() {
		return this.value;
	}

	public int getValue(int itemId) {
		return loadExchangePrices(itemId);
	}

	public HashMap<Integer, Integer> getWearingSkillRequirements() {
		if (clientScriptData == null)
			return null;
		if (itemRequirements == null) {
			HashMap<Integer, Integer> skills = new HashMap<Integer, Integer>();
			for (int i = 0; i < 10; i++) {
				Integer skill = (Integer) clientScriptData.get(749 + (i * 2));
				if (skill != null) {
					Integer level = (Integer) clientScriptData.get(750 + (i * 2));
					if (level != null)
						skills.put(skill, level);
				}
			}
			Integer maxedSkill = (Integer) clientScriptData.get(277);
			if (maxedSkill != null)
				skills.put(maxedSkill, id == 19709 ? 120 : 99);
			itemRequirements = skills;
			if (id == 7462)
				itemRequirements.put(Skills.DEFENCE, 40);
			else if (name.equals("Dragon defender")) {
				itemRequirements.put(Skills.ATTACK, 60);
				itemRequirements.put(Skills.DEFENCE, 60);
			}
			if (name.equals("Completionist cape")) {
				itemRequirements.put(Skills.DEFENCE, 99);
				itemRequirements.put(Skills.ATTACK, 99);
				itemRequirements.put(Skills.STRENGTH, 99);
				itemRequirements.put(Skills.CRAFTING, 99);
				itemRequirements.put(Skills.HERBLORE, 99);
				itemRequirements.put(Skills.THIEVING, 99);
				itemRequirements.put(Skills.HITPOINTS, 99);
				itemRequirements.put(Skills.RANGE, 99);
				itemRequirements.put(Skills.PRAYER, 99);
				itemRequirements.put(Skills.MAGIC, 99);
				itemRequirements.put(Skills.COOKING, 99);
				itemRequirements.put(Skills.WOODCUTTING, 99);
				itemRequirements.put(Skills.FLETCHING, 99);
				itemRequirements.put(Skills.FIREMAKING, 99);
				itemRequirements.put(Skills.SMITHING, 99);
				itemRequirements.put(Skills.MINING, 99);
				itemRequirements.put(Skills.AGILITY, 99);
			}
		}
		return itemRequirements;
	}

	public boolean hasModifiedColors() {
		return originalModelColors != null;
	}

	public boolean hasModifiedTextureColors() {
		return originalTextures != null;
	}

	public boolean hasSpecialBar() {
		if (clientScriptData == null)
			return false;
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer)
			return (Integer) specialBar == 1;
		return false;
	}

	public boolean isDestroyItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("destroy"))
				return true;
		}
		return false;
	}

	public boolean isLended() {
		return lended;
	}

	public boolean isMembersOnly() {
		return this.membersOnly;
	}

	public boolean isNoted() {
		return noted;
	}

	public boolean isStackable() {
		return stackable == 1;
	}

	public boolean isUnnoted() {
		return this.unnoted;
	}

	public boolean isWearItem() {
		if (inventoryOptions == null)
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip"))
				return true;
		}
		return false;
	}

	public boolean isWearItem(boolean male) {
		if (inventoryOptions == null)
			return false;
		if (Equipment.getItemSlot(id) != Equipment.SLOT_RING && Equipment.getItemSlot(id) != Equipment.SLOT_ARROWS
				&& Equipment.getItemSlot(id) != Equipment.SLOT_AURA
				&& (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1))
			return false;
		for (String option : inventoryOptions) {
			if (option == null)
				continue;
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip"))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the param def matches the class
	 * 
	 * @param def
	 * @return
	 */
	public boolean matches(ItemDefinition def) {
		try {
			Class<ItemDefinition> c = ItemDefinition.class;
			for (int i = 0; i < c.getDeclaredFields().length; i++) {
				Object o = c.getDeclaredFields()[i].get(def);
				Object original = this.getClass().getDeclaredFields()[i].get(this);
				if ((o == null && original != null) || (o != null && original == null))
					return false;
				if (o != null && original != null && !o.equals(original))
					return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0)
				break;
			readValues(stream, opcode);
		}
	}

	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1)
			invModelId = stream.readBigSmart();
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 4)
			invModelZoom = stream.readUnsignedShort();
		else if (opcode == 5)
			modelRotationX = stream.readUnsignedShort();
		else if (opcode == 6)
			modelRotationY = stream.readUnsignedShort();
		else if (opcode == 7) {
			modelOffsetX = stream.readUnsignedShort();
			if (modelOffsetX > 32767)
				modelOffsetX -= 65536;
			modelOffsetX <<= 0;
		} else if (opcode == 8) {
			modelOffsetY = stream.readUnsignedShort();
			if (modelOffsetY > 32767)
				modelOffsetY -= 65536;
			modelOffsetY <<= 0;
		} else if (opcode == 11)
			stackable = 1;
		else if (opcode == 12)
			value = stream.readInt();
		else if (opcode == 16)
			membersOnly = true;
		else if (opcode == 18) // added
			opcode18 = stream.readUnsignedShort();
		else if (opcode == 23)
			maleEquipModelId1 = stream.readBigSmart();
		else if (opcode == 24)
			maleEquipModelId2 = stream.readBigSmart();
		else if (opcode == 25)
			femaleEquipModelId1 = stream.readBigSmart();
		else if (opcode == 26)
			femaleEquipModelId2 = stream.readBigSmart();
		else if (opcode >= 30 && opcode < 35)
			groundOptions[opcode - 30] = stream.readString();
		else if (opcode >= 35 && opcode < 40)
			inventoryOptions[opcode - 35] = stream.readString();
		else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = stream.readUnsignedShort();
				modifiedModelColors[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextures = new int[length];
			modifiedTextures = new int[length];
			for (int index = 0; index < length; index++) {
				originalTextures[index] = stream.readUnsignedShort();
				modifiedTextures[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++)
				unknownArray1[index] = (byte) stream.readByte();
		} else if (opcode == 65)
			unnoted = true;
		else if (opcode == 78)
			maleEquipModelId3 = stream.readBigSmart();
		else if (opcode == 79)
			femaleEquipModelId3 = stream.readBigSmart();
		else if (opcode == 90)
			unknownInt1 = stream.readBigSmart();
		else if (opcode == 91)
			unknownInt2 = stream.readBigSmart();
		else if (opcode == 92)
			unknownInt3 = stream.readBigSmart();
		else if (opcode == 93)
			unknownInt4 = stream.readBigSmart();
		else if (opcode == 95)
			unknownInt5 = stream.readUnsignedShort();
		else if (opcode == 96)
			unknownInt6 = stream.readUnsignedByte();
		else if (opcode == 97)
			notedItemId = stream.readUnsignedShort();
		else if (opcode == 98)
			notedModelId = stream.readUnsignedShort();
		else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110)
			unknownInt7 = stream.readUnsignedShort();
		else if (opcode == 111)
			unknownInt8 = stream.readUnsignedShort();
		else if (opcode == 112)
			unknownInt9 = stream.readUnsignedShort();
		else if (opcode == 113)
			unknownInt10 = stream.readByte();
		else if (opcode == 114)
			unknownInt11 = stream.readByte() * 5;
		else if (opcode == 115)
			teamId = stream.readUnsignedByte();
		else if (opcode == 121)
			lentItemId = stream.readUnsignedShort();
		else if (opcode == 122)
			lentModelId = stream.readUnsignedShort();
		else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 0;
			unknownInt13 = stream.readByte() << 0;
			unknownInt14 = stream.readByte() << 0;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 0;
			unknownInt16 = stream.readByte() << 0;
			unknownInt17 = stream.readByte() << 0;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 117)
			opcode117 = stream.readUnsignedByte();
		else if (opcode == 82)
			opcode82 = stream.readUnsignedByte();
		else if (opcode == 13)
			opcode13 = stream.readUnsignedByte();
		else if (opcode == 14)
			opcode14 = stream.readUnsignedByte();
		else if (opcode == 9)
			opcode9 = stream.readUnsignedByte();
		else if (opcode == 27)
			opcode27 = stream.readUnsignedByte();
		else if (opcode == 66)
			opcode66 = stream.readUnsignedByte();
		else if (opcode == 116)
			opcode116 = stream.readUnsignedByte();
		else if (opcode == 157)
			opcode157 = stream.readUnsignedByte();
		else if (opcode == 244)
			opcode244 = stream.readUnsignedByte();
		else if (opcode == 170)
			opcode170 = stream.readUnsignedByte();
		else if (opcode == 151)
			opcode151 = stream.readUnsignedByte();// 14 66 116 157 244 170 151 9 27
		else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++)
				unknownArray2[index] = stream.readUnsignedShort();
		} else if (opcode == 134)
			opcode134 = stream.readUnsignedByte();
		else if (opcode == 139)
			unknownValue2 = stream.readUnsignedShort();
		else if (opcode == 140)
			unknownValue1 = stream.readUnsignedShort();
		else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null)
				clientScriptData = new HashMap<Integer, Object>(length);
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				clientScriptData.put(key, value);
			}
		} else
			System.out.println("MISSING OPCODE " + opcode + " FOR ITEM " + id);
	}

	public void resetModelColors() {
		this.originalModelColors = null;
		this.modifiedModelColors = null;
	}

	public void resetTextures() {
		this.originalTextures = null;
		this.modifiedTextures = null;
	}

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}

	private void setDefaultsVariableValues() {
		name = "null";
		maleEquipModelId1 = -1;
		maleEquipModelId2 = -1;
		femaleEquipModelId1 = -1;
		femaleEquipModelId2 = -1;
		invModelZoom = 2000;
		lentItemId = -1;
		lentModelId = -1;
		notedItemId = -1;
		notedModelId = -1;
		unknownInt9 = 128;
		value = 1;
		maleEquipModelId3 = -1;
		femaleEquipModelId3 = -1;
		unknownValue1 = -1;
		unknownValue2 = -1;

	}

	public void setEquipSlot(int equipSlot) {
		this.equipSlot = equipSlot;
	}

	public void setEquipType(int equipType) {
		this.equipType = equipType;
	}

	public void setFemaleEquipModelId1(int femaleEquipModelId1) {
		this.femaleEquipModelId1 = femaleEquipModelId1;
	}

	public void setFemaleEquipModelId2(int femaleEquipModelId2) {
		this.femaleEquipModelId2 = femaleEquipModelId2;
	}

	public void setFemaleEquipModelId3(int femaleEquipModelId3) {
		this.femaleEquipModelId3 = femaleEquipModelId3;
	}

	public void setGroundOptions(String[] groundOptions) {
		this.groundOptions = groundOptions;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setInventoryOptions(String[] inventoryOptions) {
		this.inventoryOptions = inventoryOptions;
	}

	public void setLendedItemId(int lendedItemId) {
		this.lentModelId = lendedItemId;
	}

	public void setMaleEquipModelId1(int maleEquipModelId1) {
		this.maleEquipModelId1 = maleEquipModelId1;
	}

	public void setMaleEquipModelId2(int maleEquipModelId2) {
		this.maleEquipModelId2 = maleEquipModelId2;
	}

	public void setMaleEquipModelId3(int maleEquipModelId3) {
		this.maleEquipModelId3 = maleEquipModelId3;
	}

	public void setMembersOnly(boolean membersOnly) {
		this.membersOnly = membersOnly;
	}

	public void setModelOffset1(int modelOffset1) {
		this.modelOffsetX = modelOffset1;
	}

	public void setModelOffset2(int modelOffset2) {
		this.modelOffsetY = modelOffset2;
	}

	public void setModelRotation1(int modelRotation1) {
		this.modelRotationX = modelRotation1;
	}

	public void setModelRotation2(int modelRotation2) {
		this.modelRotationY = modelRotation2;
	}

	public void setModifiedModelColors(int[] modifiedModelColors) {
		this.modifiedModelColors = modifiedModelColors;
	}

	public void setModifiedTextureColors(int[] modifiedTextureColors) {
		this.modifiedTextures = modifiedTextureColors;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNotedItemId(int notedItemId) {
		this.notedModelId = notedItemId;
	}

	public void setOriginalModelColors(int[] originalModelColors) {
		this.originalModelColors = originalModelColors;
	}

	public void setOriginalTextureColors(int[] originalTextureColors) {
		this.originalTextures = originalTextureColors;
	}

	public void setStackable(int stackable) {
		this.stackable = stackable;
	}

	public void setStackAmounts(int[] stackAmounts) {
		this.stackAmounts = stackAmounts;
	}

	public void setStackIds(int[] stackIds) {
		this.stackIds = stackIds;
	}

	public void setSwitchLendItemId(int switchLendItemId) {
		this.lentItemId = switchLendItemId;
	}

	public void setSwitchNoteItemId(int switchNoteItemId) {
		this.notedItemId = switchNoteItemId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public void setUnknownInt9(int parseInt) {
		this.unknownInt9 = parseInt;
	}

	public void setUnnoted(boolean unnoted) {
		this.unnoted = unnoted;
	}

	public void setUnNoted(boolean selected) {
		this.unnoted = selected;
	}

	public void setValue(int value) {
		this.value = value;
	}

	private void toLend(boolean lendBind) {
		ItemDefinition realItem = get(lendBind ? unknownValue2 : lentItemId);
		if(!lendBind)
			lentModelId = 13009;
		originalModelColors = realItem.originalModelColors;
		maleEquipModelId3 = realItem.maleEquipModelId3;
		femaleEquipModelId3 = realItem.femaleEquipModelId3;
		teamId = realItem.teamId;
		value = 0;
		membersOnly = realItem.membersOnly;
		name = realItem.name;
		inventoryOptions = new String[5];
		groundOptions = realItem.groundOptions;
		if (realItem.inventoryOptions != null)
			for (int optionIndex = 0; optionIndex < 4; optionIndex++)
				inventoryOptions[optionIndex] = realItem.inventoryOptions[optionIndex];
		if(lendBind)
			inventoryOptions[0] = null;
		inventoryOptions[4] = "Discard";
		maleEquipModelId1 = realItem.maleEquipModelId1;
		maleEquipModelId2 = realItem.maleEquipModelId2;
		femaleEquipModelId1 = realItem.femaleEquipModelId1;
		femaleEquipModelId2 = realItem.femaleEquipModelId2;
		clientScriptData = realItem.clientScriptData;
		lended = true;
	}
	
	private void toNote() {
		ItemDefinition realItem = get(notedItemId);
		notedModelId = 799;
		membersOnly = realItem.membersOnly;
		this.value = realItem.value;
		name = realItem.name;
		stackable = 1;
		unnoted = false;
		noted = true;
	}
	
	public static void getItemFromName(final Player player, String name) {
		int count = 0;
		boolean loaded = false;
		if (!loaded)
			for (int i = 0; i < Misc.getItemsSize(); i++) {
				get(i);
				loaded = true;
			}
		List<String> results = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		for (ItemDefinition definition : itemDefinitions.values()) {
			name = name.toLowerCase();
			String output = definition.name.toLowerCase();
			int itemId = definition.getId();
			ItemDefinition defs = ItemDefinition.get(itemId);
			if (output.contains(name)) {
				count++;
				String result = "" + count + " - <col=000000><shad=FFC919>" + defs.name
						+ "</col></shad> <col=000000><shad=FF0000>[" + definition.id + "</col>]";
				if (count > 300) {
					player.packets()
							.sendMessage("Found " + count + "+ item results for \"" + name + "\". Only 300 listed.");
					break;
				}
				results.add(result);
				ids.add(itemId);
			}
		}
		if (results.size() == 0) {
			player.sm("0 item results found for: \"" + name + "\"");
			return;
		}
		InterfaceConstants.sendQuestLines(player, results.size() + " results found for: \"" + name + "\"",
				ArrayUtils.asStringArray(results));
		player.sm("<col=78040B>" + results.size() + " item results found for: \"" + name + "\"");
		player.getData().getRuntimeData().put("getidlist", ids);
	}

	public static int loadExchangePrices(int itemId) {
		try (BufferedReader buf = new BufferedReader(new FileReader("./data/items/prices.txt"))) {
			String line;
			while ((line = buf.readLine()) != null) {

				String[] regex = line.split(",");
				if (line.startsWith(String.valueOf("ItemID: " + itemId))) {
					int itemValue = Integer.valueOf(regex[1].replace(" Value: ", ""));
					return itemValue;
				}
			}
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 1;
	}

	@Override
	public String toString() {
		return this.id + " - " + this.name;
	}

	public void write(Cache store) {
		store.getIndices()[19].putFile(getArchiveId(), getFileId(), encode());
	}
	
	public static void main(String[] args) {
		Cache.load();
		int[] ids = { 1, 13447, 50, 29, 1939, 2147, 2745, 4932, 8321, 9, 1983, 20135, 6570, 6585, 10344 };
		Class<ItemDefinition> c = ItemDefinition.class;
		for(Field f : c.getDeclaredFields()) {
			System.out.print("["+f.getName()+"]: ");
			for(int id : ids) {
				ItemDefinition def = ItemDefinition.get(id);
				try {
					Object o = f.get(def);
					if(o instanceof int[]) {
						int[] intArray = (int[]) o;
						System.out.print(Arrays.toString(intArray)+""+(id == ids[ids.length - 1] ? "" : ", "));
					} else if(o instanceof String[]) {
						String[] array = (String[]) o;
						System.out.print(Arrays.toString(array)+""+(id == ids[ids.length - 1] ? "" : ", "));
					} else if(o instanceof int[][]) {
						int[][] array = (int[][]) o;
						for(int i = 0; i < array.length; i++)
							System.out.print(Arrays.toString(array[i])+""+(id == ids[ids.length - 1] ? "" : ", "));
					} else if(o instanceof byte[]) {
						byte[] array = (byte[]) o;
						System.out.print(Arrays.toString(array)+""+(id == ids[ids.length - 1] ? "" : ", "));
					} else if(o instanceof HashMap) {
						@SuppressWarnings("unchecked")
						HashMap<Integer, Object> map = (HashMap<Integer, Object>) o;
						ArrayList<Integer> keys = new ArrayList<Integer>();
						ArrayList<Object> values = new ArrayList<Object>();
						for(Object k : map.values())
							values.add(k);
						for(Integer k : map.keySet())
							keys.add(k);
						for(int i = 0; i < values.size(); i++)
							System.out.print("["+keys.get(i)+", "+values.get(i)+""+(id == ids[ids.length - 1] ? "]" : ", "));
					} else {
						System.out.print(f.get(def)+""+(id == ids[ids.length - 1] ? "" : ", "));
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}

}