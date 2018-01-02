package org.nova.cache.definition;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.nova.cache.Cache;
import org.nova.cache.stream.InputStream;
import org.nova.cache.stream.OutputStream;
import org.nova.game.player.Player;
import org.nova.kshan.content.interfaces.InterfaceConstants;
import org.nova.kshan.utilities.ArrayUtils;
import org.nova.utility.misc.Misc;

/**
 * Reads the values of a game object
 * (Cache editor loading)
 */
@SuppressWarnings("unused")
public class ObjectDefinition implements Cloneable {

	private static final ConcurrentHashMap<Integer, ObjectDefinition> DEFINITIONS = 
		new ConcurrentHashMap<Integer, ObjectDefinition>();
	
	public static void getObjectFromName(final Player player, String name) {
		int count = 0;
		boolean loaded = false;
		if (!loaded) {
			for (int i = 0; i < Misc.getObjectsSize(); i++) {
				get(i);
				loaded = true;
			}
		}
		List<String> results = new ArrayList<String>();
		for (ObjectDefinition definition : DEFINITIONS.values()) {
			name = name.toLowerCase();
			String output = definition.name.toLowerCase();
			int objectId = definition.id;
			ObjectDefinition defs = ObjectDefinition.get(objectId);
			if (output.contains(name)) {
				count++;
				String result = "" + count + " - <col=000000><shad=FFC919>"
						+ defs.name
						+ "</col></shad> <col=000000><shad=FF0000>["
						+ definition.id + "</col>]";
				if (count > 300) {
					player.packets().sendMessage(
							"Found " + count + "+ object results for \"" + name
									+ "\". Only 300 listed.");
					break;
				}
				results.add(result);
			}
		}
		if(results.size() == 0) {
			player.sm("0 object results found for: \""+name+"\"");
			return;
		}
		InterfaceConstants.sendQuestLines(player, results.size()+" results found for: \""+name+"\"", 
			ArrayUtils.asStringArray(results));
		player.sm("<col=78040B>"+results.size()+" object results found for: \""+name+"\"");
	}

	public int[] originalColors;
	int[] toObjectIds;
	int[] anIntArray3833 = null;
	private int anInt3834;
	int anInt3835;
	private byte aByte3837;
	int anInt3838 = -1;
	boolean aBoolean3839;
	private int anInt3840;
	public int height;
	int anInt3844;
	boolean aBoolean3845;
	private byte aByte3847;
	private byte aByte3849;
	int anInt3850;
	int anInt3851;
	public boolean secondBool;
	public boolean aBoolean3853;
	int anInt3855;
	public boolean notClipped;
	int anInt3857;
	private byte[] aByteArray3858;
	int[] anIntArray3859;
	int anInt3860;
	public String[] options;
	int configFileId;
	public int[] modifiedColors;
	int anInt3865;
	boolean aBoolean3866;
	boolean aBoolean3867;
	public boolean projectileClipped;
	private int[] anIntArray3869;
	boolean aBoolean3870;
	public int sizeY;
	boolean aBoolean3872;
	boolean aBoolean3873;
	public int thirdInt;
	private int anInt3875;
	public int objectAnimation;
	private int anInt3877;
	private int anInt3878;
	public int clipType;
	private int anInt3881;
	private int anInt3882;
	private int anInt3883;
	private int anInt3889;
	public int sizeX;
	public boolean aBoolean3891;
	int anInt3892;
	public int clickable;
	boolean aBoolean3894;
	boolean aBoolean3895;
	int anInt3896;
	int configId;
	public byte[] terrainTypes;
	int anInt3900;
	public String name;
	public int length;
	int anInt3904;
	int anInt3905;
	boolean aBoolean3906;
	int[] anIntArray3908;
	private byte aByte3912;
	public int cursor;
	private byte aByte3914;
	private int anInt3915;
	public int[][] modelIds;
	public int width;
	private boolean loaded;
	private short[] aShortArray3919;
	private short[] aShortArray3920;
	int anInt3921;
	private HashMap<Integer, Object> parameters;
	boolean aBoolean3923;
	boolean aBoolean3924;
	int anInt3925;
	public int id;
	private boolean aBool6886;

	private int opcode170;
	private int opcode171;
	private int opcode173_1;
	private int opcode173_2;
	private boolean opcode177;
	private int opcode178;
	private int opcode186;
	private boolean opcode189;
	private int[] opcode190_195;
	private int opcode196;
	private int opcode197;
	private int opcode201_1;
	private int opcode201_2;
	private int opcode201_3;
	private int opcode201_4;
	private int opcode201_5;
	private int opcode201_6;

	public String getFirstOption() {
		if ((this.options == null) || (this.options.length < 1)) {
			return "";
		}
		return this.options[0];
	}

	public String getSecondOption() {
		if ((this.options == null) || (this.options.length < 2)) {
			return "";
		}
		return this.options[1];
	}

	public String getOption(int option) {
		if ((this.options == null) || (this.options.length < option)
				|| (option == 0)) {
			return "";
		}
		return this.options[(option - 1)];
	}

	public String getThirdOption() {
		if ((this.options == null) || (this.options.length < 3)) {
			return "";
		}
		return this.options[2];
	}

	public boolean containsOption(int i, String option) {
		if ((this.options == null) || (this.options[i] == null)
				|| (this.options.length <= i)) {
			return false;
		}
		return this.options[i].equals(option);
	}

	public boolean containsOption(String o) {
		if (this.options == null) {
			return false;
		}
		for (String option : this.options) {
			if (option != null) {
				if (option.equalsIgnoreCase(o)) {
					return true;
				}
			}
		}
		return false;
	}

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1 || opcode == 5) {
			boolean aBoolean1162 = false;
			if (opcode == 5 && aBoolean1162)
				skipReadModelIds(stream);
			int firstDimensionLength = stream.readUnsignedByte();
			modelIds = new int[firstDimensionLength][];
			setTerrainTypes(new byte[firstDimensionLength]);
			for (int firstIndex = 0; firstIndex < firstDimensionLength; firstIndex++) {
				getTerrainTypes()[firstIndex] = (byte) stream.readByte();
				int idsLength = stream.readUnsignedByte();
				modelIds[firstIndex] = new int[idsLength];
				for (int modelIndex = 0; idsLength > modelIndex; modelIndex++) {
					modelIds[firstIndex][modelIndex] = stream.readBigSmart();
				}
			}
			if (opcode == 5 && !aBoolean1162)
				skipReadModelIds(stream);
		} 
		else if (opcode == 2)
			name = stream.readString();
		else if (opcode == 14)
			sizeX = stream.readUnsignedByte();
		else if (opcode == 15)
			sizeY = stream.readUnsignedByte();
		else if (opcode == 17) {
			projectileClipped = false;
			clipType = 0;
		} else if (opcode == 18) {
			projectileClipped = true;
			notClipped = false;
			clipType = 2;
		} else if (opcode == 19)
			clickable = stream.readUnsignedByte();
		else if (opcode == 21)
			aByte3912 = (byte) 1;
		else if (opcode == 22)
			aBoolean3867 = true;
		else if (opcode == 23)
			thirdInt = 1;
		else if (opcode == 24)
			objectAnimation = stream.readBigSmart();
		else if (opcode == 27)
			clipType = 1;
		else if (opcode == 28)
			anInt3892 = (stream.readUnsignedByte() << 2);
		else if (opcode == 29)
			anInt3878 = stream.readByte();
		else if (opcode == 39)
			anInt3840 = (stream.readByte() * 5);
		else if (opcode >= 30 && opcode < 35)
			options[-30 + opcode] = (stream.readString());
		else if (opcode == 40) {
			int i_53_ = (stream.readUnsignedByte());
			originalColors = new int[i_53_];
			modifiedColors = new int[i_53_];
			for (int i_54_ = 0; i_53_ > i_54_; i_54_++) {
				originalColors[i_54_] = stream.readUnsignedShort();
				modifiedColors[i_54_] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int i_71_ = (stream.readUnsignedByte());
			aShortArray3920 = new short[i_71_];
			aShortArray3919 = new short[i_71_];
			for (int i_72_ = 0; i_71_ > i_72_; i_72_++) {
				aShortArray3920[i_72_] = (short) (stream.readUnsignedShort());
				aShortArray3919[i_72_] = (short) (stream.readUnsignedShort());
			}
		} else if (opcode == 42) {
			int i_69_ = (stream.readUnsignedByte());
			aByteArray3858 = (new byte[i_69_]);
			for (int i_70_ = 0; i_70_ < i_69_; i_70_++)
				aByteArray3858[i_70_] = (byte) (stream.readByte());
		} else if (opcode == 62)
			aBoolean3839 = true;
		else if (opcode == 64)
			aBoolean3872 = false;
		else if (opcode == 65)
			length = stream.readUnsignedShort();
		else if (opcode == 66)
			height = stream.readUnsignedShort();
		else if (opcode == 67)
			width = stream.readUnsignedShort();
		else if (opcode == 69)
			anInt3925 = stream.readUnsignedByte();
		else if (opcode == 70)
			anInt3883 = stream.readShort() << 2;
		else if (opcode == 71)
			anInt3889 = stream.readShort() << 2;
		else if (opcode == 72)
			anInt3915 = stream.readShort() << 2;
		else if (opcode == 73)
			secondBool = true;
		else if (opcode == 74)
			notClipped = true;
		else if (opcode == 75)
			anInt3855 = stream.readUnsignedByte();
		else if (opcode == 77 || opcode == 92) {
			configFileId = stream.readUnsignedShort();
			if (configFileId == 65535)
				configFileId = -1;
			configId = stream.readUnsignedShort();
			if (configId == 65535)
				configId = -1;
			int i_66_ = -1;
			if (opcode == 92) {
				i_66_ = stream.readBigSmart();
			}
			int i_67_ = stream.readUnsignedByte();
			toObjectIds = new int[i_67_ - -2];
			for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
				toObjectIds[i_68_] = stream.readBigSmart();
			}
			toObjectIds[i_67_ + 1] = i_66_;
			
		} else if (opcode == 78) {
			anInt3860 = stream.readUnsignedShort();
			anInt3904 = stream.readUnsignedByte();
		} else if (opcode == 79) {
			anInt3900 = stream.readUnsignedShort();
			anInt3905 = stream.readUnsignedShort();
			anInt3904 = stream.readUnsignedByte();
			int i_64_ = stream.readUnsignedByte();
			anIntArray3859 = new int[i_64_];
			for (int i_65_ = 0; i_65_ < i_64_; i_65_++)
				anIntArray3859[i_65_] = stream.readUnsignedShort();
		} else if (opcode == 81) {
			aByte3912 = (byte) 2;
			anInt3882 = 256 * stream.readUnsignedByte();
		} else if (opcode == 82)
			aBoolean3891 = true;
		else if (opcode == 88)
			aBoolean3853 = false;
		else if (opcode == 89)
			aBoolean3895 = false;
		else if (opcode == 90)
			aBoolean3870 = true;
		else if (opcode == 91)
			aBoolean3873 = true;
		else if (opcode == 93) {
			aByte3912 = (byte) 3;
			anInt3882 = stream.readUnsignedShort();
		} else if (opcode == 94)
			aByte3912 = (byte) 4;
		else if (opcode == 95) {
			aByte3912 = (byte) 5;
			anInt3882 = stream.readShort();
		} else if (opcode == 96)
			aBoolean3924 = true;
		else if (opcode == 97)
			aBoolean3866 = true;
		else if (opcode == 98)
			aBoolean3923 = true;
		else if (opcode == 99) {
			anInt3857 = stream.readUnsignedByte();
			anInt3835 = stream.readUnsignedShort();
		} else if (opcode == 100) {
			anInt3844 = stream.readUnsignedByte();
			cursor = stream.readUnsignedShort();
		} else if (opcode == 101)
			anInt3850 = stream.readUnsignedByte();
		else if (opcode == 102)
			anInt3838 = stream.readUnsignedShort();
		else if (opcode == 103)
			thirdInt = 0;
		else if (opcode == 104)
			anInt3865 = stream.readUnsignedByte();
		else if (opcode == 105)
			aBoolean3906 = true;
		else if (opcode == 106) {
			int i_55_ = stream.readUnsignedByte();
			anIntArray3869 = new int[i_55_];
			anIntArray3833 = new int[i_55_];
			for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
				anIntArray3833[i_56_] = stream.readBigSmart();
				int i_57_ = stream.readUnsignedByte();
				anIntArray3869[i_56_] = i_57_;
				anInt3881 += i_57_;
			}
		} else if (opcode == 107)
			anInt3851 = stream.readUnsignedShort();
		else if (opcode >= 150 && opcode < 155)
			options[opcode + -150] = stream.readString();
		else if (opcode == 160) {
			int i_62_ = stream.readUnsignedByte();
			anIntArray3908 = new int[i_62_];
			for (int i_63_ = 0; i_62_ > i_63_; i_63_++)
				anIntArray3908[i_63_] = stream.readUnsignedShort();
		} else if (opcode == 162) {
			aByte3912 = (byte) 3;
			anInt3882 = stream.readInt();
		} else if (opcode == 163) {
			aByte3847 = (byte) stream.readByte();
			aByte3849 = (byte) stream.readByte();
			aByte3837 = (byte) stream.readByte();
			aByte3914 = (byte) stream.readByte();
		} else if (opcode == 164)
			anInt3834 = stream.readShort();
		else if (opcode == 165)
			anInt3875 = stream.readShort();
		else if (opcode == 166)
			anInt3877 = stream.readShort();
		else if (opcode == 167)
			anInt3921 = stream.readUnsignedShort();
		else if (opcode == 168)
			aBoolean3894 = true;
		else if (opcode == 169) {
			aBoolean3845 = true;
		} else if (opcode == 170) {
			opcode170 = stream.readUnsignedSmart();
		} else if (opcode == 171) {
			opcode171 = stream.readUnsignedSmart();
		} else if (opcode == 173) {
			opcode173_1 = stream.readUnsignedShort();
			opcode173_2 = stream.readUnsignedShort();
		} else if (opcode == 177) {
			opcode177 = true;
		} else if (opcode == 178) {
			opcode178 = stream.readUnsignedByte();
		} else if (opcode == 186) {
			opcode186 = stream.readUnsignedByte();
		} else if (opcode == 189) {
			opcode189 = true;
		} else if (opcode >= 190 && opcode < 196) {
			int length = stream.readUnsignedByte();
			opcode190_195 = new int[length];
			for (int index = 0; length > index; index++)
				opcode190_195[index] = (short) stream.readUnsignedShort();
		} else if (opcode == 196) {
			opcode196 = stream.readUnsignedByte();
		} else if (opcode == 197) {
			opcode197 = stream.readUnsignedByte();
		} else if (opcode == 200) {
			aBool6886 = true;
		} else if (opcode == 201) {
			opcode201_1 = stream.readUnsignedByte();
			opcode201_2 = stream.readUnsignedByte();
			opcode201_3 = stream.readUnsignedByte();
			opcode201_4 = stream.readUnsignedByte();
			opcode201_5 = stream.readUnsignedByte();
			opcode201_6 = stream.readUnsignedByte();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (parameters == null)
				parameters = new HashMap<Integer, Object>(length);
			for (int i_60_ = 0; i_60_ < length; i_60_++) {
				boolean bool = stream.readUnsignedByte() == 1;
				int i_61_ = stream.read24BitInt();
				if (!bool)
					parameters.put(i_61_, stream.readInt());
				else
					parameters.put(i_61_, stream.readString());
			}
		}

	}

	/**
	 * Returns the data of the object in bytes.
	 * @return
	 */
	public byte[] getData() {
		OutputStream stream = new OutputStream();
		// opcode 1
		if(modelIds != null) {
			stream.writeByte(1);
			stream.writeByte(modelIds.length);
			for(int i = 0; i < modelIds.length; i++) {
				stream.writeByte(getTerrainTypes()[i]);
				stream.writeByte(modelIds[i].length);
				for(int i2 = 0; i2 < modelIds[i].length; i2++) {
					stream.writeBigSmart(modelIds[i][i2]);
				}
			}
		}
		// opcode 2
		if(!name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(name);
		}
		// opcode 14
		if(sizeX != 1) {
			stream.writeByte(14);
			stream.writeByte(sizeX);
		}
		// opcode 15
		if(sizeY != 1) {
			stream.writeByte(15);
			stream.writeByte(sizeY);
		}
		// opcode 17
		if(clipType == 0 && !projectileClipped) {
			stream.writeByte(17);
		}
		// opcode 18
		if(projectileClipped) {
			stream.writeByte(18);
		}
		// opcode 19
		{
			stream.writeByte(19);
			stream.writeByte(clickable);
		}
		// opcode 21
		if(aByte3912 == 1) {
			stream.writeByte(21);
		}
		// opcode 22
		if(aBoolean3867) {
			stream.writeByte(22);
		}
		// opcode 23
		if(thirdInt == 1) {
			stream.writeByte(23);
		}
		// opcode 24
		if(objectAnimation != -1) {
			stream.writeByte(24);
			stream.writeBigSmart(objectAnimation);
		}
		// opcode 27
		if(clipType == 1 || clipType == 2) {
			stream.writeByte(27);
		}
		// opcode 29
		if(anInt3878 != -1) {
			stream.writeByte(29);
			stream.writeByte(anInt3878);
		}
		// opcode 39
		if(anInt3840 != -1) {
			stream.writeByte(39);
			stream.writeByte(anInt3840);
		}
		// opcode 30-35
		for(int index = 0; index < options.length; index++) {
			if (options[index] != null) {
				stream.writeByte(30 + index);
				stream.writeString(options[index]);
			}
		}
		// opcode 40
		if((originalColors != null) && (modifiedColors != null)) {
			stream.writeByte(40);
			stream.writeByte(originalColors.length);
			for (int index = 0; index < originalColors.length; index++) {
				stream.writeShort(originalColors[index]);
				stream.writeShort(modifiedColors[index]);
			}
		}
		// opcode 65
		if (length != 128) {
			stream.writeByte(65);
			stream.writeShort(length);
		}
		// opcode 66
		if(height != 128) {
			stream.writeByte(66);
			stream.writeShort(height);
		}
		// opcode 67
		if(width != 128) {
			stream.writeByte(67);
			stream.writeShort(width);
		}
		// opcode 75
		if(anInt3855 != -1) {
			stream.writeByte(75);
			stream.writeByte(anInt3855);
		}
		// opcode 99
		{
			stream.writeByte(99);
			stream.writeByte(anInt3857);
			stream.writeShort(anInt3835);
		}
		// opcode 100
		{
			stream.writeByte(100);
			stream.writeByte(anInt3844);
			stream.writeShort(cursor);
		}
		// opcode 103
		if(thirdInt == 0) {
			stream.writeByte(103);
		}
		stream.writeByte(0);
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(data, 0, data.length);
		return data;
	}

	private void skipReadModelIds(InputStream stream) {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			int length2 = stream.readUnsignedByte();
			for (int i = 0; i < length2; i++)
				stream.readBigSmart();
		}
	}

	private void readValueLoop(InputStream stream) {
		for (;;) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			try {
				readValues(stream, opcode);
			} catch(Exception e) {
				System.out.println("Exception occurred at object: "+id);
				e.printStackTrace();
			}
		}
	}

	public void setDefaultVariables() {
		this.anInt3835 = -1;
		this.anInt3860 = -1;
		this.configFileId = -1;
		this.aBoolean3866 = false;
		this.anInt3851 = -1;
		this.anInt3865 = 255;
		this.aBoolean3845 = false;
		this.aBoolean3867 = false;
		this.anInt3850 = 0;
		this.anInt3844 = -1;
		this.anInt3881 = 0;
		this.anInt3857 = -1;
		this.aBoolean3872 = true;
		this.anInt3882 = -1;
		this.anInt3834 = 0;
		this.options = new String[5];
		this.anInt3875 = 0;
		this.aBoolean3839 = false;
		this.anIntArray3869 = null;
		this.sizeY = 1;
		this.thirdInt = -1;
		this.anInt3883 = 0;
		this.aBoolean3895 = true;
		this.anInt3840 = 0;
		this.aBoolean3870 = false;
		this.anInt3889 = 0;
		this.aBoolean3853 = true;
		this.secondBool = false;
		this.clipType = 2;
		this.projectileClipped = true;
		this.notClipped = false;
		this.anInt3855 = -1;
		this.anInt3878 = 0;
		this.anInt3904 = 0;
		this.sizeX = 1;
		this.objectAnimation = -1;
		this.aBoolean3891 = false;
		this.anInt3905 = 0;
		this.name = "null";
		this.cursor = -1;
		this.aBoolean3906 = false;
		this.aBoolean3873 = false;
		this.aByte3914 = 0;
		this.anInt3915 = 0;
		this.anInt3900 = 0;
		this.clickable = -1;
		this.aBoolean3894 = false;
		this.aByte3912 = 0;
		this.anInt3921 = 0;
		this.length = 128;
		this.configId = -1;
		this.anInt3877 = 0;
		this.anInt3925 = 0;
		this.anInt3892 = 64;
		this.aBoolean3923 = false;
		this.aBoolean3924 = false;
		this.height = 128;
		this.width = 128;
	}

	final void method3287() {
		if (this.clickable == -1) {
			this.clickable = 0;
			if ((this.getTerrainTypes() != null)
					&& (this.getTerrainTypes().length == 1)
					&& (this.getTerrainTypes()[0] == 10)) {
				this.clickable = 1;
			}
			for (int i_13_ = 0; i_13_ < 5; i_13_++) {
				if (this.options[i_13_] != null) {
					this.clickable = 1;
					break;
				}
			}
		}
		if (this.anInt3855 == -1) {
			this.anInt3855 = (this.clipType != 0 ? 1 : 0);
		}
	}

	private static int getArchiveId(int i_0_) {
		return i_0_ >>> -1135990488;
	}

	public int getArchiveId() {
		return this.id >>> -1135990488;
	}

	public int getFileId() {
		return 0xFF & this.id;
	}
	
	public static ObjectDefinition get(int id) {
		return getDef(id, Cache.INSTANCE);
	}

	public static ObjectDefinition getDef(int id, Cache store) {
		ObjectDefinition def = DEFINITIONS.get(Integer.valueOf(id));
		if (def == null) {
			def = new ObjectDefinition(store, id);
			def.id = id;
			byte[] data = store.getIndices()[16].getFile(getArchiveId(id),
					id & 0xFF);
			if (data != null) {
				def.readValueLoop(new InputStream(data));
			}
			def.method3287();
			if ((def.name != null)
					&& ((def.name.equalsIgnoreCase("bank booth")) || (def.name
							.equalsIgnoreCase("counter")))) {
				def.notClipped = false;
				def.projectileClipped = true;
				if (def.clipType == 0) {
					def.clipType = 1;
				}
			}
			if (def.notClipped) {
				def.projectileClipped = false;
				def.clipType = 0;
			}
			DEFINITIONS.put(Integer.valueOf(id), def);
		}
		return def;
	}

	private void loadObjectDefinition(Cache cache) {
		byte[] data = cache.getIndices()[16].getFile(this.id >>> -1135990488,
				this.id & 0xFF);
		if (data == null) {
			System.out.println("FAILED LOADING OBJECT: " + this.id);
			return;
		}
		try {
			readOpcodeValues(new InputStream(data));
		} catch (RuntimeException e) {
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

	public static ObjectDefinition getObjectDefinition(Cache cache, int objectId) {
		return getObjectDefinition(cache, objectId, true);
	}

	public static ObjectDefinition getObjectDefinition(Cache cache,
			int objectId, boolean load) {
		return new ObjectDefinition(cache, objectId, load);
	}
	
	public ObjectDefinition(Cache cache, int id) {
		setDefaultVariables();
	}

	public ObjectDefinition(Cache cache, int id, boolean load) {
		this.id = id;
		setDefaults();
		setDefaultVariables();
		if(load)
			loadObjectDefinition(cache);
	}
	
	private void setDefaults() {
		name = "null";
		sizeX = 1;
		sizeY = 1;
		projectileClipped = true;
		clipType = 2;
		objectAnimation = -1;
		setTerrainTypes(new byte[] { 10 });
		cursor = 0;
		length = 128;
		width = 128;
		height = 128;
		setClickable(true);
		options = new String[] { null, null, null, null, null };
	}

	public int getClipType() {
		return this.clipType;
	}
	
	public String[] getOptions() {
		return options;
	}

	public boolean isProjectileClipped() {
		return this.projectileClipped;
	}

	public int getSizeX() {
		return this.sizeX;
	}

	public int getSizeY() {
		return this.sizeY;
	}

	public static void clearObjectDefinitions() {
		DEFINITIONS.clear();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void write(Cache store) {
		store.getIndices()[16].putFile(getArchiveId(), getFileId(), getData());
	}

	@Override
	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		return this.id + " - " + this.name;
	}

	public void setProjectileClipped(boolean clip) {
		projectileClipped = clip;
		clipType = clip ? 2 : 0;
		notClipped = clip;
	}
	
	public void setClickable(boolean clickable) {
		this.clickable = clickable ? 1 : 0;
	}
	
	public boolean isClickable() {
		return clickable == 1;
	}

	public byte[] getTerrainTypes() {
		return terrainTypes;
	}

	public void setTerrainTypes(byte[] terrainType) {
		this.terrainTypes = terrainType;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getLength() {
		return length;
	}
	
	public static void compareDefs() throws Exception {
		Cache.load();
		int[] ids = { 3644 };
		Class<ObjectDefinition> c = ObjectDefinition.class;
		for(Field f : c.getDeclaredFields()) {
			f.setAccessible(true);
			System.out.print("["+f.getName()+"]: ");
			for(int id : ids) {
				ObjectDefinition def = ObjectDefinition.get(id);
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
	
	public static void main(String[] args) {
		//Cache.load();
		try {
			/*ObjectDefinition def1 = ObjectDefinition.get(3644);
			ObjectDefinition def2 = ObjectDefinition.get(64088);
			Field[] fields = def1.getClass().getDeclaredFields();
			for(Field f : fields) {
				f.setAccessible(true);
				System.out.println("Field in "+def1.id+" and "+def2.id+": "+f.getName()+" - "+f.get(def1)+" ----- "+f.get(def2));
			}*/
			compareDefs();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
