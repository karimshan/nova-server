package org.nova.cache.definition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.cache.stream.InputStream;
import org.nova.cache.stream.OutputStream;
import org.nova.game.player.Player;
import org.nova.kshan.content.interfaces.InterfaceConstants;
import org.nova.kshan.utilities.ArrayUtils;
import org.nova.utility.misc.Misc;

public final class NPCDefinition implements Cloneable {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static final ConcurrentHashMap<Integer, NPCDefinition> npcDefinitions = new ConcurrentHashMap();
	public int id;
	public HashMap<Integer, Object> clientScripts;
	public int unknownInt13;
	public int unknownInt6;
	public int unknownInt15;
	public byte respawnDirection;
	public int size = 1;
	public int[][] unknownArray3;
	public boolean unknownBoolean2;
	public int unknownInt9;
	public int unknownInt4;
	public int[] unknownArray2;
	public int unknownInt7;
	public int renderEmote;
	public boolean unknownBoolean5 = false;
	public int unknownInt20;
	public byte unknownByte1;
	public boolean unknownBoolean3;
	public int unknownInt3;
	public byte unknownByte2;
	public boolean unknownBoolean6;
	public boolean unknownBoolean4;
	public int[] originalModelColors;
	public int combatLevel;
	public byte[] unknownArray1;
	public short unknownShort1;
	public boolean unknownBoolean1;
	public int height;
	public String name;
	public int[] modifiedTextures;
	public byte walkMask;
	public int[] modelIds;
	public int unknownInt1;
	public int unknownInt21;
	public int unknownInt11;
	public int unknownInt17;
	public int unknownInt14;
	public int unknownInt12;
	public int unknownInt8;
	public int headIcon;
	public int unknownInt19;
	public int[] originalTextures;
	public int[][] anIntArrayArray882;
	public int unknownInt10;
	public int[] unknownArray4;
	public int unknownInt5;
	public int unknownInt16;
	public boolean visibleOnMap;
	public int[] chatHeads;
	public short unknownShort2;
	public String[] options;
	public int[] modifiedModelColors;
	public int unknownInt2;
	public int width;
	public int unknownInt18;
	public boolean unknownBoolean7;
	public int[] unknownArray5;
	public int opcode179_1;
	public int opcode179_2;
	public int opcode179_3;
	public int opcode179_4;
	public int opcode179_5;
	public int opcode179_6;
	public int opcode168;
	public int opcode165;
	public int opcode164_2;
	public int opcode164_1;
	public int opcode163;
	public int opcode128;
	
	public static final NPCDefinition get(int id) {
		NPCDefinition def = npcDefinitions.get(Integer.valueOf(id));
		if (def == null) {
			def = new NPCDefinition(id);
			def.method694();
			byte[] data = Cache.INSTANCE.getIndices()[18].getFile(id >>> 134238215,
					id & 0x7F);
			if (data != null) {
				def.readValueLoop(new InputStream(data));
			}
			npcDefinitions.put(Integer.valueOf(id), def);
		}
		return def;
	}
	
	public static final NPCDefinition get(Cache cache, int id) {
		NPCDefinition def = npcDefinitions.get(Integer.valueOf(id));
		if (def == null) {
			def = new NPCDefinition(id);
			def.method694();
			byte[] data = cache.getIndices()[18].getFile(id >>> 134238215,
					id & 0x7F);
			if (data != null) {
				def.readValueLoop(new InputStream(data));
			}
			npcDefinitions.put(Integer.valueOf(id), def);
		}
		return def;
	}
	
	public static void getNPCFromName(final Player player, String name) {
		int count = 0;
		boolean loaded = false;
		if (!loaded) {
			for (int i = 0; i < Misc.getNPCsSize(); i++) {
				get(i);
				loaded = true;
			}
		}
		List<String> results = new ArrayList<String>();
		for (NPCDefinition definition : npcDefinitions.values()) {
			name = name.toLowerCase();
			String output = definition.name.toLowerCase();
			int npcId = definition.getId();
			NPCDefinition defs = NPCDefinition.get(npcId);
			if (output.contains(name)) {
				count++;
				String result = "" + count + " - <col=000000><shad=FFC919>"
						+ defs.name
						+ "</col></shad> <col=000000><shad=FF0000>["
						+ definition.id + "</col>]";
				if (count > 300) {
					player.packets().sendMessage(
							"Found " + count + "+ NPC results for \"" + name
									+ "\". Only 300 listed.");
					break;
				}
				results.add(result);
			}
		}
		if(results.size() == 0) {
			player.sm("0 NPC results found for: \""+name+"\"");
			return;
		}
		InterfaceConstants.sendQuestLines(player, results.size()+" results found for: \""+name+"\"", 
			ArrayUtils.asStringArray(results));
		player.sm("<col=78040B>"+results.size()+" NPC results found for: \""+name+"\"");
	}

	public static NPCDefinition getNPCDefinition(Cache cache, int npcId) {
		return getNPCDefinition(cache, npcId, true);
	}

	public static NPCDefinition getNPCDefinition(Cache cache, int npcId,
			boolean load) {
		return new NPCDefinition(cache, npcId, load);
	}

	public NPCDefinition(Cache cache, int id, boolean load) {
		this.id = id;
		setDefaultVariableValues();
		setDefaultOptions();
		if (load)
			loadNPCDefinition(cache);
		//System.out.println("NPC ID: "+id);
	}

	@Override
	public NPCDefinition clone() {
		try {
			return (NPCDefinition) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setDefaultOptions() {
		this.options = new String[] { null, null, null, null, null };
	}

	private void setDefaultVariableValues() {
		this.name = "null";
		this.combatLevel = 0;
		this.visibleOnMap = true;
		this.renderEmote = -1;
		this.respawnDirection = 7;
		this.size = 1;
		this.unknownInt9 = -1;
		this.unknownInt4 = -1;
		this.unknownInt15 = -1;
		this.unknownInt7 = -1;
		this.unknownInt3 = 32;
		this.unknownInt6 = -1;
		this.unknownInt1 = 0;
		this.walkMask = 0;
		this.unknownInt20 = 255;
		this.unknownInt11 = -1;
		this.unknownBoolean3 = true;
		this.unknownShort1 = 0;
		this.unknownInt8 = -1;
		this.unknownByte1 = -96;
		this.unknownInt12 = 0;
		this.unknownInt17 = 42;
		this.unknownBoolean4 = true;
		this.unknownInt21 = -1;
		this.unknownInt14 = -1;
		this.unknownInt13 = -1;
		this.height = 128;
		this.headIcon = -1;
		this.unknownBoolean6 = false;
		this.unknownInt5 = -1;
		this.unknownByte2 = -16;
		this.unknownBoolean1 = false;
		this.unknownInt16 = -1;
		this.unknownInt10 = -1;
		this.unknownBoolean2 = true;
		this.unknownInt19 = -1;
		this.width = 128;
		this.unknownShort2 = 0;
		this.unknownInt2 = 0;
		this.unknownInt18 = -1;
	}

	private void loadNPCDefinition(Cache cache) {
		byte[] data = cache.getIndices()[18].getFile(getArchiveId(),
				getFileId());
		if (data == null) {
			return;
		}
		try {
			readOpcodeValues(new InputStream(data));
		} catch (RuntimeException e) {
			System.out.println(id);
			e.printStackTrace();
		}
	}

	private void readOpcodeValues(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	public int getArchiveId() {
		return this.id >>> 134238215;
	}

	public int getFileId() {
		return 0x7F & this.id;
	}

	public void changeDefinition(Cache store) {
		changeDefinition(store, encode());
	}
	
	public void changeDefinition(Cache cache, byte[] data) {
		cache.getIndices()[18].putFile(getArchiveId(), getFileId(), data);
	}

	public void method694() {
		if (this.modelIds == null) {
			this.modelIds = new int[0];
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			int i = stream.readUnsignedByte();
			this.modelIds = new int[i];
			for (int i_66_ = 0; i_66_ < i; i_66_++) {
				this.modelIds[i_66_] = stream.readBigSmart();
				if ((this.modelIds[i_66_] ^ 0xFFFFFFFF) == -65536) {
					this.modelIds[i_66_] = -1;
				}
			}
		} else if (opcode == 2) {
			this.name = stream.readString();
		} else if (opcode == 12) {
			this.size = stream.readUnsignedByte();
		} else if ((opcode >= 30) && (opcode < 36)) {
			this.options[(opcode - 30)] = stream.readString();
			if (this.options[(opcode - 30)].equalsIgnoreCase("Hidden")) {
				this.options[(opcode - 30)] = null;
			}
		} else if (opcode == 40) {
			int i = stream.readUnsignedByte();
			this.originalModelColors = new int[i];
			this.modifiedModelColors = new int[i];
			for (int i_65_ = 0; (i ^ 0xFFFFFFFF) < (i_65_ ^ 0xFFFFFFFF); i_65_++) {
				this.originalModelColors[i_65_] = stream.readUnsignedShort();
				this.modifiedModelColors[i_65_] = stream.readUnsignedShort();
			}
		}
		if (opcode == 41) {
			int i = stream.readUnsignedByte();
			this.originalTextures = new int[i];
			this.modifiedTextures = new int[i];
			for (int i_54_ = 0; (i_54_ ^ 0xFFFFFFFF) > (i ^ 0xFFFFFFFF); i_54_++) {
				this.originalTextures[i_54_] = stream.readUnsignedShort();
				this.modifiedTextures[i_54_] = stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i = stream.readUnsignedByte();
			this.unknownArray1 = new byte[i];
			for (int i_55_ = 0; i > i_55_; i_55_++) {
				this.unknownArray1[i_55_] = ((byte) stream.readByte());
			}
		} else if (opcode == 60) {
			int i = stream.readUnsignedByte();
			this.chatHeads = new int[i];
			for (int i_64_ = 0; (i_64_ ^ 0xFFFFFFFF) > (i ^ 0xFFFFFFFF); i_64_++) {
				this.chatHeads[i_64_] = stream.readBigSmart();
			}
		} else if (opcode == 93) {
			this.visibleOnMap = false;
		} else if (opcode == 95) {
			this.combatLevel = stream.readUnsignedShort();
		} else if (opcode == 97) {
			this.height = stream.readUnsignedShort();
		} else if (opcode == 98) {
			this.width = stream.readUnsignedShort();
		} else if (opcode == 99) {
			this.unknownBoolean1 = true;
		} else if (opcode == 100) {
			this.unknownInt1 = stream.readByte();
		} else if (opcode == 101) {
			this.unknownInt2 = (stream.readByte() * 5);
		} else if (opcode == 102) {
			this.headIcon = stream.readUnsignedShort();
		} else if (opcode == 103) {
			this.unknownInt3 = stream.readUnsignedShort();
		} else if ((opcode == 106) || (opcode == 118)) {
			this.unknownInt4 = stream.readUnsignedShort();
			if (this.unknownInt4 == 65535) {
				this.unknownInt4 = -1;
			}
			this.unknownInt5 = stream.readUnsignedShort();
			if (this.unknownInt5 == 65535) {
				this.unknownInt5 = -1;
			}
			int i = -1;
			if (opcode == 118) {
				i = stream.readUnsignedShort();
				if ((i ^ 0xFFFFFFFF) == -65536) {
					i = -1;
				}
			}
			int i_56_ = stream.readUnsignedByte();
			this.unknownArray2 = new int[2 + i_56_];
			for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
				this.unknownArray2[i_57_] = stream.readUnsignedShort();
				if (this.unknownArray2[i_57_] == 65535) {
					this.unknownArray2[i_57_] = -1;
				}
			}
			this.unknownArray2[(i_56_ - -1)] = i;
		} else if (opcode == 107) {
			this.unknownBoolean2 = false;
		} else if (opcode == 109) {
			this.unknownBoolean3 = false;
		} else if (opcode == 111) {
			this.unknownBoolean4 = false;
		} else if (opcode == 113) {
			this.unknownShort1 = ((short) stream.readUnsignedShort());
			this.unknownShort2 = ((short) stream.readUnsignedShort());
		} else if (opcode == 114) {
			this.unknownByte1 = ((byte) stream.readByte());
			this.unknownByte2 = ((byte) stream.readByte());
		} else if (opcode == 119) {
			this.walkMask = ((byte) stream.readByte());
		} else if (opcode == 121) {
			this.unknownArray3 = new int[this.modelIds.length][];
			int i = stream.readUnsignedByte();
			for (int i_62_ = 0; (i_62_ ^ 0xFFFFFFFF) > (i ^ 0xFFFFFFFF); i_62_++) {
				int i_63_ = stream.readUnsignedByte();
				int[] is = this.unknownArray3[i_63_] = new int[3];
				is[0] = stream.readByte();
				is[1] = stream.readByte();
				is[2] = stream.readByte();
			}
		} else if (opcode == 122) {
			this.unknownInt6 = stream.readBigSmart();
		} else if (opcode == 123) {
			this.unknownInt7 = stream.readUnsignedShort();
		} else if (opcode == 125) {
			this.respawnDirection = ((byte) stream.readByte());
		} else if (opcode == 127) {
			this.renderEmote = stream.readUnsignedShort();
		} else if (opcode == 128) {
			opcode128 = stream.readUnsignedByte();
		} else if (opcode == 134) {
			this.unknownInt8 = stream.readUnsignedShort();
			if (this.unknownInt8 == 65535) {
				this.unknownInt8 = -1;
			}
			this.unknownInt9 = stream.readUnsignedShort();
			if (this.unknownInt9 == 65535) {
				this.unknownInt9 = -1;
			}
			this.unknownInt10 = stream.readUnsignedShort();
			if ((this.unknownInt10 ^ 0xFFFFFFFF) == -65536) {
				this.unknownInt10 = -1;
			}
			this.unknownInt11 = stream.readUnsignedShort();
			if ((this.unknownInt11 ^ 0xFFFFFFFF) == -65536) {
				this.unknownInt11 = -1;
			}
			this.unknownInt12 = stream.readUnsignedByte();
		} else if (opcode == 135) {
			this.unknownInt13 = stream.readUnsignedByte();
			this.unknownInt14 = stream.readUnsignedShort();
		} else if (opcode == 136) {
			this.unknownInt15 = stream.readUnsignedByte();
			this.unknownInt16 = stream.readUnsignedShort();
		} else if (opcode == 137) {
			this.unknownInt17 = stream.readUnsignedShort();
		} else if (opcode == 138) {
			this.unknownInt18 = stream.readBigSmart();
		} else if (opcode == 139) {
			this.unknownInt19 = stream.readBigSmart();
		} else if (opcode == 140) {
			this.unknownInt20 = stream.readUnsignedByte();
		} else if (opcode == 141) {
			this.unknownBoolean5 = true;
		} else if (opcode == 142) {
			this.unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 143) {
			this.unknownBoolean6 = true;
		} else if ((opcode >= 150) && (opcode < 155)) {
			this.options[(opcode - 150)] = stream.readString();
			if (this.options[(opcode - 150)].equalsIgnoreCase("Hidden")) {
				this.options[(opcode - 150)] = null;
			}
		} else {
			if (opcode == 155) {
				int aByte821 = stream.readByte();
				int aByte824 = stream.readByte();
				int aByte843 = stream.readByte();
				int aByte855 = stream.readByte();
			} else {
				byte aByte833;
				if (opcode == 158) {
					aByte833 = 1;
				} else {
					if (opcode == 159) {
						aByte833 = 0;
					} else if (opcode == 160) {
						int i = stream.readUnsignedByte();
						this.unknownArray4 = new int[i];
						for (int i_58_ = 0; i > i_58_; i_58_++) {
							this.unknownArray4[i_58_] = stream
									.readUnsignedShort();
						}
					} else if (opcode == 162) {
						this.unknownBoolean7 = true;
					} else {
						if (opcode == 163) {
							opcode163 = stream.readUnsignedByte();
						} else {
							if (opcode == 164) {
								opcode164_1 = stream.readUnsignedShort();
								opcode164_2 = stream.readUnsignedShort();
							} else {
								if (opcode == 165) {
									opcode165 = stream.readUnsignedByte();
								} else {
									if (opcode == 168) {
										opcode168 = stream.readUnsignedByte();
									} else if ((opcode >= 170)
											&& (opcode < 176)) {
										if (null == this.unknownArray5) {
											this.unknownArray5 = new int[6];
											Arrays.fill(this.unknownArray5, -1);
										}
										int i_44_ = (short) stream
												.readUnsignedShort();
										if (i_44_ == 65535) {
											i_44_ = -1;
										}
										this.unknownArray5[(opcode - 170)] = i_44_;
									} else if (opcode == 179) {
										opcode179_1 = stream.readUnsignedByte();
										opcode179_2 = stream.readUnsignedByte();
										opcode179_3 = stream.readUnsignedByte();
										opcode179_4 = stream.readUnsignedByte();
										opcode179_5 = stream.readUnsignedByte();
										opcode179_6 = stream.readUnsignedByte();
									} else if (opcode == 249) {
										int i = stream.readUnsignedByte();
										if (this.clientScripts == null)
											this.clientScripts = new HashMap(i);
										for (int i_60_ = 0; i > i_60_; i_60_++) {
											boolean stringInstance = stream.readUnsignedByte() == 1;
											int key = stream.read24BitInt();
											Object value;
											if (stringInstance)
												value = stream.readString();
											else
												value = Integer.valueOf(stream.readInt());
											clientScripts.put(Integer.valueOf(key), value);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public byte[] encode() {
		OutputStream stream = new OutputStream();
		if(this.modelIds != null) {
			stream.writeByte(1);
			stream.writeByte(this.modelIds.length);
			for (int index = 0; index < this.modelIds.length; index++) {
				stream.writeBigSmart(this.modelIds[index]);
			}
		}
		if (!this.name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(this.name);
		}
		if (this.size != 1) {
			stream.writeByte(12);
			stream.writeByte(this.size);
		}
		for (int index = 0; index < this.options.length; index++) {
			if ((this.options[index] != null) && (!this.options[index].equals("Hidden"))) {
				stream.writeByte(30 + index);
				stream.writeString(this.options[index]);
			}
		}
		if ((this.originalModelColors != null)
				&& (this.modifiedModelColors != null)) {
			stream.writeByte(40);
			stream.writeByte(this.originalModelColors.length);
			for (int index = 0; index < this.originalModelColors.length; index++) {
				stream.writeShort(this.originalModelColors[index]);
				stream.writeShort(this.modifiedModelColors[index]);
			}
		}
		if ((this.originalTextures != null)
				&& (this.modifiedTextures != null)) {
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
			for (int index = 0; index < this.unknownArray1.length; index++) {
				stream.writeByte(this.unknownArray1[index]);
			}
		}
		if (this.chatHeads != null) {
			stream.writeByte(60);
			stream.writeByte(this.chatHeads.length);
			for (int index = 0; index < this.chatHeads.length; index++) {
				stream.writeBigSmart(this.chatHeads[index]);
			}
		}
		if (!isVisibleOnMap()) {
			stream.writeByte(93);
		}
		if (this.combatLevel != -1) {
			stream.writeByte(95);
			stream.writeShort(this.combatLevel);
		}
		if (this.height != 128) {
			stream.writeByte(97);
			stream.writeShort(this.height);
		}
		if (this.width != 128) {
			stream.writeByte(98);
			stream.writeShort(this.width);
		}
		if (this.unknownBoolean1) {
			stream.writeByte(99);
		}
		if (this.unknownInt1 != 0) {
			stream.writeByte(100);
			stream.writeByte(this.unknownInt1);
		}
		if (this.unknownInt2 != 0) {
			stream.writeByte(101);
			stream.writeByte(this.unknownInt2 / 5);
		}
		if (this.headIcon != -1) {
			stream.writeByte(102);
			stream.writeShort(this.headIcon);
		}
		if (this.walkMask != 0) {
			stream.writeByte(119);
			stream.writeByte(this.walkMask);
		}
		if (this.unknownInt6 != -1) {
			stream.writeByte(122);
			stream.writeBigSmart(this.unknownInt6);
		}
		if (this.respawnDirection != 7) {
			stream.writeByte(125);
			stream.writeByte(this.respawnDirection);
		}
		if (this.renderEmote != -1) {
			stream.writeByte(127);
			stream.writeShort(this.renderEmote);
		}
		if (this.unknownInt17 != 42) {
			stream.writeByte(137);
			stream.writeShort(this.unknownInt17);
		}
		for (int index = 0; index < this.options.length; index++) {
			if ((this.options[index] != null) && (!this.options[index].equals("Hidden"))) {
				stream.writeByte(150 + index);
				stream.writeString(this.options[index]);
			}
		}
		Iterator i$;
		if (this.clientScripts != null) {
			stream.writeByte(249);
			stream.writeByte(this.clientScripts.size());
			for (i$ = this.clientScripts.keySet().iterator(); i$.hasNext();) {
				int key = ((Integer) i$.next()).intValue();
				Object value = this.clientScripts.get(Integer.valueOf(key));
				stream.writeByte((value instanceof String) ? 1 : 0);
				stream.write24BitInt(key);
				if ((value instanceof String)) {
					stream.writeString((String) value);
				} else {
					stream.writeInt(((Integer) value).intValue());
				}
			}
		}
		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(data, 0, data.length);
		return data;
	}

	public static final void clearNPCDefinitions() {
		npcDefinitions.clear();
	}

	public NPCDefinition(int id) {
		this.id = id;
		this.unknownInt9 = -1;
		this.unknownInt4 = -1;
		this.unknownInt15 = -1;
		this.unknownInt7 = -1;
		this.unknownInt3 = 32;
		this.combatLevel = -1;
		this.unknownInt6 = -1;
		this.name = "null";
		this.unknownInt1 = 0;
		this.walkMask = 0;
		this.unknownInt20 = 255;
		this.unknownInt11 = -1;
		this.unknownBoolean3 = true;
		this.unknownShort1 = 0;
		this.unknownInt8 = -1;
		this.unknownByte1 = -96;
		this.unknownInt12 = 0;
		this.unknownInt17 = -1;
		this.renderEmote = -1;
		this.respawnDirection = 7;
		this.unknownBoolean4 = true;
		this.unknownInt21 = -1;
		this.unknownInt14 = -1;
		this.unknownInt13 = -1;
		this.height = 128;
		this.headIcon = -1;
		this.unknownBoolean6 = false;
		this.unknownInt5 = -1;
		this.unknownByte2 = -16;
		this.unknownBoolean1 = false;
		this.visibleOnMap = true;
		this.unknownInt16 = -1;
		this.unknownInt10 = -1;
		this.unknownBoolean2 = true;
		this.unknownInt19 = -1;
		this.width = 128;
		this.unknownShort2 = 0;
		this.options = new String[5];
		this.unknownInt2 = 0;
		this.unknownInt18 = -1;
	}

	public NPCDefinition(Cache s, int npcId) {
		this(s, npcId, true);
	}

	@Override
	public String toString() {
		return this.id + " - " + this.name;
	}
	
	public void resetModelColors() {
		this.originalModelColors = null;
		this.modifiedModelColors = null;
	}

	public void resetTextures() {
		this.originalTextures = null;
		this.modifiedTextures = null;
	}

	public void changeModelColor(int originalModelColor, int modifiedModelColor) {
		if (this.originalModelColors != null) {
			for (int i = 0; i < this.originalModelColors.length; i++) {
				if (this.originalModelColors[i] == originalModelColor) {
					this.modifiedModelColors[i] = modifiedModelColor;
					return;
				}
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
			for (int i = 0; i < this.originalTextures.length; i++) {
				if (this.originalTextures[i] == originalTexture) {
					this.modifiedTextures[i] = modifiedTexture;
					return;
				}
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

	public boolean hasAttackOption() {
		for (String option : options) {
			if (option != null && option.toLowerCase().contains("attack"))
				return true;
		}
		return false;
	}

	public boolean hasMarkOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("mark"))
				return true;
		}
		return false;
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op))
				return true;
		}
		return false;
	}

	public boolean hasPickupOption() {
		String as[];
		int j = (as = options).length;
		for (int i = 0; i < j; i++) {
			String option = as[i];
			if (option != null && option.equalsIgnoreCase("pick-up"))
				return true;
		}

		return false;
	}

	public boolean hasTakeOption() {
		String as[];
		int j = (as = options).length;
		for (int i = 0; i < j; i++) {
			String option = as[i];
			if (option != null && option.equalsIgnoreCase("take"))
				return true;
		}

		return false;
	}

	public byte getRespawnDirection() {
		return this.respawnDirection;
	}

	public void setRespawnDirection(byte respawnDirection) {
		this.respawnDirection = respawnDirection;
	}

	public int getSize() {
		return this.size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRenderEmote() {
		return this.renderEmote;
	}

	public void setRenderEmote(int renderEmote) {
		this.renderEmote = renderEmote;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean isVisibleOnMap) {
		this.visibleOnMap = isVisibleOnMap;
	}

	public String[] getOptions() {
		return this.options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public int getCombatLevel() {
		return this.combatLevel;
	}

	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns true if the param def matches the class
	 * 
	 * @param def
	 * @return
	 */
	public boolean matches(NPCDefinition def) {
		try {
			Class<NPCDefinition> c = NPCDefinition.class;
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
	
	public static void compareDefs() throws Exception {
		Cache.load();
		int[] ids = { 1, 13447, 50, 29, 1939, 2147, 2745, 4932, 8321, 9, 1983, 3637 };
		Class<NPCDefinition> c = NPCDefinition.class;
		for(Field f : c.getDeclaredFields()) {
			f.setAccessible(true);
			System.out.print("["+f.getName()+"]: ");
			for(int id : ids) {
				NPCDefinition def = NPCDefinition.getNPCDefinition(Cache.INSTANCE, id);
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

	/**
	 * Returns the tooltip
	 * @return
	 */
	public String getToolTip() {
		if(clientScripts == null)
			return "";
		Object toolTip = clientScripts.get(1);
		if (toolTip != null)
			return ""+toolTip;
		return "";
	}

	
	/*public static void main(String[] args) {
		try {
			compareDefs();
			for(int i = 0; i < Misc.getNPCsSize(); i++) {
				//NPCDefinition def = NPCDefinition.get(i);
				//def.write(Cache.INSTANCE);
			}
			//compareDefs();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}*/
	
}
