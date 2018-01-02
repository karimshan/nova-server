package org.nova.cache.loaders;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

import org.nova.cache.Cache;
import org.nova.cache.stream.OutputStream;
import org.nova.cache.utility.CacheUtils;
import org.nova.kshan.utilities.ArrayUtils;
import org.nova.network.stream.InputStream;
import org.nova.utility.misc.Misc;

/**
 * Widget Component Definition (WC)
 * Refactored, decoded and encoded--Mostly
 * 
 * @author Shan
 *
 */
public class WCDefinition {

	private static WidgetSettings GLOBAL_SETTINGS = new WidgetSettings(0, -1);
	private static WCDefinition[][] widgets;
	
	static {
		 widgets = new WCDefinition[10000][]; // Probably never going to go past 10000 interfaces lmao
	}

	public static WCDefinition[] getComponents(int id) {
		if (id >= widgets.length)
			return null;
		if (widgets[id] == null) {
			widgets[id] = new WCDefinition[Misc
					.getInterfaceDefinitionsComponentsSize(id)];
			for (int i = 0; i < widgets[id].length; i++) {
				byte[] data = Cache.INSTANCE.getIndices()[3].getFile(id, i);
				if (data != null) {
					WCDefinition defs = widgets[id][i] = new WCDefinition();
					defs.hash = i + (id << 16);
					if (data[0] != -1) {
						// throw some dumbass illegalexception
					}
					defs.decode(new InputStream(data));
				}
			}
		}
		return widgets[id];
	}
	
	public static WCDefinition createWidget(int id) {
		if(id == -1)
			id = Misc.getInterfaceDefinitionsSize();
		if(widgets[id] == null) {
			widgets[id] = new WCDefinition[1];
			WCDefinition def = new WCDefinition();
			def.setDefaultValues();
			def.hash = 0 + (id << 16);
			widgets[id][0] = def;
		}
		return widgets[id][0];
	}
	
	public static WCDefinition addComponent(int id) {
		WCDefinition def = new WCDefinition();
		def.setDefaultValues();
		def.hash = (widgets[id].length) + (id << 16);
		WCDefinition.widgets[id] = (WCDefinition[]) ArrayUtils.addSpecialElement(widgets[id], def);
		return def;
	}
	
	public void destroyComponent(int widgetId, int index) {
		if(widgets[getWidgetId()] == null || widgets[getWidgetId()][index] == null) {
			System.out.println("The specified widget index is either corrupted or does not exist.");
			return;
		}
		if(index == 0 && widgets[getWidgetId()].length == 1) {
			System.out.println("You cannot delete the only component this widget has.");
			return;
		}
		widgets[widgetId] = (WCDefinition[]) ArrayUtils.destroySpecialElement(widgets[getWidgetId()], index);
	}

	public static WCDefinition getComponent(int id,
			int component) {
		WCDefinition[] inter = getComponents(id);
		if (inter == null || component >= inter.length)
			return null;
		return inter[component];
	}

	static final int method925(int i) {
		return (i & 0x3fda8) >> 11;
	}
	
	protected boolean aBoolean4707;
	protected boolean textHasShadow;
	protected boolean lineFlip;
	protected boolean aBoolean4723;
	protected boolean aBoolean4727;
	protected boolean aBoolean4730;
	protected boolean spriteFlippedW;
	protected boolean spriteB2;
	protected boolean spriteFlippedH;
	protected boolean boxIsFilled;
	protected boolean spriteB3;
	protected boolean aBoolean4802;
	protected boolean aBoolean4819;
	protected boolean textDropFontColors;
	protected boolean aBoolean4858;
	protected boolean spriteB1;
	protected boolean aBoolean4865;
	protected byte horizontalPositionMode;
	protected byte verticalResizeMode;
	protected byte horizontalResizeMode;
	protected byte verticalPositionMode;
	protected byte[] aByteArray4733;
	protected byte[] aByteArray4806;
	public Hashtable<Long, Object> aHashTable4823;
	protected int anInt4679;
	protected int anInt4681;
	protected int anInt4682;
	protected int anInt4683;
	protected int anInt4687;
	protected int containerMaxScrollH;
	protected int width;
	protected int anInt4694 = -1;
	protected int anInt4695;
	protected int textLineHeight;
	protected int anInt4698;
	protected int anInt4700;
	protected int anInt4703;
	protected int anInt4708;
	protected int anInt4709 = 0;
	protected int anInt4714;
	protected int anInt4718;
	protected int anInt4719;
	protected int height;
	protected int anInt4724;
	protected int spriteI1;
	protected int anInt4734;
	protected int containerMaxScrollW;
	protected int spriteBorderThickness;
	protected int anInt4746;
	protected int anInt4747;
	protected int anInt4748;
	protected int lineThickWidth;
	protected int componentColor;
	protected int componentAlpha;
	protected int textFontId;
	protected int anInt4760;
	protected int anInt4761;
	protected int anInt4762;
	protected int anInt4764;
	protected int textMaxLines;
	protected int anInt4773;
	protected int anInt4780;
	protected int anInt4783;
	protected int anInt4787;
	protected int anInt4792;
	protected int anInt4794;
	protected int anInt4795;
	protected int spriteShadowColor;
	protected int anInt4797;
	protected int anInt4800;
	protected int anInt4801;
	protected int anInt4809;
	protected int anInt4810;
	protected int anInt4811;
	protected int anInt4813;
	protected int anInt4814;
	protected int anInt4815;
	protected int startY;
	protected int anInt4817;
	protected int spriteId;
	protected int anInt4821;
	protected int anInt4824;
	protected int textVerticalPositionMode;
	protected int anInt4826;
	protected int anInt4831;
	protected int textHorizontalPositionMode;
	protected int anInt4839;
	protected int anInt4842;
	protected int anInt4848;
	protected int anInt4849;
	protected int startX;
	protected int anInt4860;
	protected int modelId;
	protected int[] anIntArray4705;
	protected int[] anIntArray4772;
	protected int[] anIntArray4789;
	protected int[] anIntArray4805;
	protected int[] anIntArray4812;
	protected int[] anIntArray4829;
	protected int[] anIntArray4833;
	protected int[] anIntArray4838;
	protected int[] anIntArray4863;
	protected Object[] anObjectArray4680;
	protected Object[] anObjectArray4688;
	protected Object[] anObjectArray4701;
	protected Object[] anObjectArray4706;
	protected Object[] anObjectArray4711;
	protected Object[] anObjectArray4712;
	protected Object[] anObjectArray4740;
	protected Object[] anObjectArray4742;
	protected Object[] anObjectArray4745;
	protected Object[] anObjectArray4751;
	protected Object[] anObjectArray4753;
	protected Object[] anObjectArray4756;
	public Object[] cs2HoverArray;
	protected Object[] anObjectArray4768;
	protected Object[] cs2CloseWidget;
	protected Object[] anObjectArray4771;
	protected Object[] anObjectArray4774;
	protected Object[] anObjectArray4775;
	protected Object[] anObjectArray4777;
	protected Object[] anObjectArray4778;
	protected Object[] anObjectArray4788;
	protected Object[] anObjectArray4798;
	protected Object[] anObjectArray4799;
	protected Object[] anObjectArray4803;
	protected Object[] anObjectArray4807;
	protected Object[] cs2IdleComponentHover;
	protected Object[] anObjectArray4828;
	protected Object[] cs2ComponentHover;
	protected Object[] anObjectArray4846;
	protected Object[] anObjectArray4852;
	protected Object[] anObjectArray4854;
	protected Object[] anObjectArray4856;
	protected Object[] anObjectArray4857;
	protected Object[] anObjectArray4862;
	protected WidgetSettings widgetSettings;
	public String aString4765;
	public String componentName;
	protected String specialClick;
	protected String aString4786;
	public String text;
	protected String[] clickOptions;
	protected boolean hasScripts;
	protected boolean hidden;
	
	protected int hash;
	protected int modelType;
	protected int parentId;
	public int type;
	
	public int newInt;
	private int hiddenByte;
	private int boolean4858Byte;
	private int spriteB1B2Byte;
	private int spriteFlippedWByte;
	private int spriteFlippedHByte;
	private int spriteB3Byte;
	private int unknownByte7;
	private boolean unknownBoolean1;
	private int textDropFontColorsByte;
	private int textHasShadowByte;
	private int boxIsFilledByte;
	private int lineFlipByte;
	private int componentClickType;
	private int unknownByte12;
	private byte unknownByte15;
	private byte unknownByte16;
	private int unknownByte17;
	private int unknownByte18;
	private int indexHolder;
	private int unknownByte19;
	private int widgetSettingArg;
	private int unknownByte20;
	private int unknown24BitInteger2;
	private int unknownInt1;
	private int unknownByte21;
	private int unknown24BitInteger3;
	private String unknownJagString1;
	
	public void setDefaultValues() {
		hash = 0 + (getWidgetId() << 16);
		this.componentName = "newComponent";
		this.setType("container");
		this.setHidden(false);
		this.startX = 0;
		this.startY = 0;
		this.width = 0;
		this.height = 0;
		this.horizontalPositionMode = 0;
		this.verticalPositionMode = 0;
		this.horizontalResizeMode = 0;
		this.verticalResizeMode = 0;
		this.specialClick = null;
		this.clickOptions = new String[1];
		this.componentColor = 0;
		this.componentAlpha = 0;
		this.setBoxFilled(false);
		this.setSpriteB1B2(false);
		this.setSpriteB3(true);
		this.newInt = 3;
		this.anObjectArray4680 = null;
		this.anObjectArray4688 = null;
		this.anObjectArray4701 = null;
		this.anObjectArray4706 = null;
		this.anObjectArray4711 = null;
		this.anObjectArray4712 = null;
		this.anObjectArray4740 = null;
		this.anObjectArray4742 = null;
		this.anObjectArray4745 = null;
		this.anObjectArray4751 = null;
		this.anObjectArray4753 = null;
		this.anObjectArray4756 = null;
		this.cs2HoverArray = null;
		this.anObjectArray4768 = null;
		this.cs2CloseWidget = null;
		this.anObjectArray4771 = null;
		this.anObjectArray4774 = null;
		this.anObjectArray4775 = null;
		this.anObjectArray4777 = null;
		this.anObjectArray4778 = null;
		this.anObjectArray4788 = null;
		this.anObjectArray4798 = null;
		this.anObjectArray4799 = null;
		this.anObjectArray4803 = null;
		this.anObjectArray4807 = null;
		this.cs2IdleComponentHover = null;
		this.anObjectArray4828 = null;
		this.cs2ComponentHover = null;
		this.anObjectArray4846 = null;
		this.anObjectArray4852 = null;
		this.anObjectArray4854 = null;
		this.anObjectArray4856 = null;
		this.anObjectArray4857 = null;
		this.anObjectArray4862 = null;
		this.anIntArray4705 = null;
		this.anIntArray4772 = null;
		this.anIntArray4789 = null;
		this.anIntArray4805 = null;
		this.anIntArray4812 = null;
		this.anIntArray4829 = null;
		this.anIntArray4833 = null;
		this.anIntArray4838 = null;
		this.anIntArray4863 = null;
	}

	public WCDefinition() {
		anInt4698 = -1;
		aBoolean4730 = false;
		anInt4747 = 0;
		aBoolean4707 = false;
		anInt4682 = 0;
		lineThickWidth = 1;
		spriteI1 = 0;
		textFontId = -1;
		aBoolean4727 = false;
		boxIsFilled = false;
		width = 0;
		textMaxLines = 0;
		horizontalResizeMode = (byte) 0;
		anInt4679 = 0;
		anInt4761 = -1;
		componentName = "";
		verticalResizeMode = (byte) 0;
		anInt4780 = 0;
		spriteB3 = true;
		componentAlpha = 0;
		componentColor = 0;
		anInt4687 = -1;
		anInt4783 = -1;
		anInt4773 = -1;
		anInt4795 = 0;
		spriteShadowColor = 0;
		anInt4681 = 0;
		anInt4714 = 2;
		aBoolean4802 = false;
		hasScripts = false;
		spriteBorderThickness = 0;
		aString4786 = "";
		anInt4700 = 1;
		textLineHeight = 0;
		aBoolean4819 = false;
		hash = -1;
		anInt4821 = 0;
		anInt4815 = 0;
		anInt4718 = -1;
		spriteId = -1;
		lineFlip = false;
		horizontalPositionMode = (byte) 0;
		anInt4817 = 0;
		anInt4708 = 0;
		anInt4810 = 0;
		anInt4787 = 100;
		spriteB2 = false;
		containerMaxScrollH = 0;
		textHasShadow = false;
		textVerticalPositionMode = 0;
		anInt4719 = 0;
		anInt4734 = 0;
		text = "";
		textDropFontColors = true;
		anInt4762 = 0;
		anInt4792 = 1;
		containerMaxScrollW = 0;
		anInt4831 = 0;
		anInt4800 = 0;
		anInt4748 = -1;
		anInt4809 = 0;
		textHorizontalPositionMode = 0;
		anInt4695 = 0;
		anInt4813 = 0;
		modelType = 1;
		widgetSettings = GLOBAL_SETTINGS;
		anInt4814 = 0;
		anInt4811 = 0;
		height = 0;
		anInt4849 = 0;
		anInt4683 = -1;
		parentId = -1;
		anInt4801 = 0;
		anInt4824 = -1;
		anInt4703 = -1;
		aBoolean4858 = false;
		hidden = false;
		startX = 0;
		spriteB1 = false;
		anInt4848 = -1;
		anInt4797 = 0;
		anInt4860 = 0;
		aBoolean4723 = false;
		verticalPositionMode = (byte) 0;
		startY = 0;
		anInt4842 = 0;
		anInt4839 = -1;
		newInt = -1;
		hiddenByte = 0;
		boolean4858Byte = 0;
		spriteB1B2Byte = 0;
		spriteFlippedWByte = 0;
		spriteFlippedHByte = 0;
		spriteB3Byte = 1;
		unknownByte7 = 0;
		unknownBoolean1 = false;
		textDropFontColorsByte = 0;
		textHasShadowByte = 0;
		boxIsFilledByte = 0;
		lineFlipByte = 0;
		componentClickType = 0;
		unknownByte12 = 0;
		unknownByte15 = 0;
		unknownByte16 = 0;
		unknownByte17 = 0;
		unknownByte18 = 0;
		indexHolder = 0;
		unknownByte19 = 0;
		widgetSettingArg = 0;
		unknownByte20 = 0;
		unknown24BitInteger2 = 0;
		unknownInt1 = 0;
		unknownByte21 = 0;
		unknown24BitInteger3 = 0;
		unknownJagString1 = "";
		aString4765 = "";
		modelId = -1;
		aHashTable4823 = new Hashtable<Long, Object>();
	}
	
	public byte[] getData() {
		OutputStream stream = new OutputStream();
		
		// Start encoding here
		stream.writeByte(newInt);
		stream.writeByte(type);
		if((type & 0x80 ^ 0xffffffff) != -1) {
			stream.writeString(aString4765); // For some reason everything fucks up if this isn't here
			System.out.println("Wrote aString4765");
		}
		stream.writeShort(anInt4814);
		stream.writeShort(startX);
		stream.writeShort(startY);
		stream.writeShort(width);
		stream.writeShort(height);
		stream.writeByte(horizontalResizeMode);
		stream.writeByte(verticalResizeMode);
		stream.writeByte(horizontalPositionMode);
		stream.writeByte(verticalPositionMode);
		stream.writeShort(parentId);
		stream.writeByte(hiddenByte); // hidden
		
		// for type 0 (Container)
		if(type == 0) {
			stream.writeShort(containerMaxScrollW);
			stream.writeShort(containerMaxScrollH);
			if((newInt ^ 0xffffffff) > -1)
				stream.writeByte(boolean4858Byte);
		}
		
		// for type 5 (Sprite)
		if(type == 5) {
			stream.writeInt(spriteId);
			stream.writeShort(spriteI1);
			stream.writeByte(spriteB1B2Byte); // spriteB1
			stream.writeByte(componentAlpha);
			stream.writeByte(spriteBorderThickness);
			stream.writeInt(spriteShadowColor);
			stream.writeByte(spriteFlippedWByte); // spriteFlippedW
			stream.writeByte(spriteFlippedHByte); // spriteFlippedH
			stream.writeInt(componentColor);
			if(newInt >= 3)
				stream.writeByte(spriteB3Byte); // spriteB3
		}
		
		// For type 6 (Models)
		if(type == 6) {
			if(isNewWidget())
				stream.writeBigSmart(modelId);
			else
				stream.writeShort(modelId);
			stream.writeByte(unknownByte7);
			if(unknownBoolean1) {
				stream.writeShort(anInt4709);
				stream.writeShort(anInt4797);
				stream.writeShort(anInt4815);
				stream.writeShort(anInt4821);
				stream.writeShort(anInt4682);
				stream.writeShort(anInt4787);
			} else if(aBoolean4865) {
				stream.writeShort(anInt4709);
				stream.writeShort(anInt4797);
				stream.writeShort(anInt4842);
				stream.writeShort(anInt4815);
				stream.writeShort(anInt4821);
				stream.writeShort(anInt4682);
				stream.writeShort(anInt4787);
			}
			if(isNewWidget())
				stream.writeBigSmart(anInt4773);
			else
				stream.writeShort(anInt4773);
			if(horizontalResizeMode != 0)
				stream.writeShort(anInt4800);
			if((verticalResizeMode ^ 0xffffffff) != -1)
				stream.writeShort(anInt4849);
		}
		
		// For type 4 (Text)
		if(type == 4) {
			if(isNewWidget())
				stream.writeBigSmart(textFontId);
			else
				stream.writeShort(textFontId);
			if ((newInt ^ 0xffffffff) <= -3)
				stream.writeByte(textDropFontColorsByte); // textDropFontColors
			stream.writeString(text);
			stream.writeByte(textLineHeight);
			stream.writeByte(textHorizontalPositionMode);
			stream.writeByte(textVerticalPositionMode);
			stream.writeByte(textHasShadowByte); // textHasShadow
			stream.writeInt(componentColor);
			stream.writeByte(componentAlpha);
			if((newInt ^ 0xffffffff) <= -1)
				stream.writeByte(textMaxLines);
		}
		
		// For type 3 (Box)
		if(type == 3) {
			stream.writeInt(componentColor);
			stream.writeByte(boxIsFilledByte); // boxIsFilled
			stream.writeByte(componentAlpha);
		}
		
		// For type 9 (Line)
		if(type == 9) {
			stream.writeByte(lineThickWidth);
			stream.writeInt(componentColor);
			stream.writeByte(lineFlipByte); // lineFlip
		}
		
		// No idea what this part of the component is
		stream.write24BitInt(componentClickType); // Nvm now I do
		stream.writeByte(unknownByte12);
		if((unknownByte12 ^ 0xffffffff) != -1) {
			stream.writeByte(unknownByte15);
			stream.writeByte(unknownByte16);
		}
		
		// The rest of the encoding
		if(componentName.equals(null) || componentName == null || componentName.equals("null"))
			stream.writeString("");
		else
			stream.writeString(componentName);
		
		if(clickOptions == null)
			clickOptions = new String[1];
		stream.writeByte(clickOptions.length);
		for(int index = 0; index < clickOptions.length; index++)
			if(clickOptions[index] != null && !clickOptions[index].equals(null) && !clickOptions[index].equals("null"))
				stream.writeString(clickOptions[index]);
			else
				stream.writeString("");
	
		indexHolder = clickOptions.length >> 4;
		if((indexHolder ^ 0xffffffff) < -1) {
			stream.writeByte(unknownByte18);
			stream.writeShort(anIntArray4863[unknownByte18]);
		}
		if((indexHolder ^ 0xffffffff) < -2) {
			stream.writeByte(unknownByte19);
			stream.writeShort(anIntArray4863[unknownByte19]);
		}
		
		if(specialClick == null)
			specialClick = "";
		stream.writeString(specialClick);
		
		stream.writeByte(anInt4708);
		stream.writeByte(anInt4795);
		stream.writeByte(anInt4860);
		stream.writeString(aString4786);
		
		if((method925(componentClickType) ^ 0xffffffff) != -1) {
			stream.writeShort(widgetSettingArg);
			stream.writeShort(anInt4698);
			stream.writeShort(anInt4839);
		}
		if(newInt >= 0)
			stream.writeShort(anInt4761);
		if (newInt >= 0) {
			stream.writeByte(unknownByte20);
			for(int i = 0; i < unknownByte20; i++) {
				stream.write24BitInt(unknown24BitInteger2);
				stream.writeInt(unknownInt1);
			}
			stream.writeByte(unknownByte21);
			for(int i = 0; i < unknownByte21; i++) {
				stream.write24BitInt(unknown24BitInteger3);
				stream.writeJagString(unknownJagString1);
			}
		}
		
		// Client Scripts
		encodeScript(cs2HoverArray, stream);
		encodeScript(anObjectArray4706, stream);
		encodeScript(cs2IdleComponentHover, stream);
		encodeScript(anObjectArray4771, stream);
		encodeScript(anObjectArray4768, stream);
		encodeScript(anObjectArray4807, stream);
		encodeScript(anObjectArray4742, stream);
		encodeScript(anObjectArray4788, stream);
		encodeScript(anObjectArray4701, stream);
		encodeScript(cs2CloseWidget, stream);
		if((newInt ^ 0xffffffff) <= -1)
			encodeScript(anObjectArray4751, stream);
		encodeScript(cs2ComponentHover, stream);
		encodeScript(anObjectArray4774, stream);
		encodeScript(anObjectArray4803, stream);
		encodeScript(anObjectArray4680, stream);
		encodeScript(anObjectArray4856, stream);
		encodeScript(anObjectArray4852, stream);
		encodeScript(anObjectArray4711, stream);
		encodeScript(anObjectArray4753, stream);
		encodeScript(anObjectArray4688, stream);
		encodeScript(anObjectArray4775, stream);
		
		encodeIntegerScript(anIntArray4838, stream);
		encodeIntegerScript(anIntArray4833, stream);
		encodeIntegerScript(anIntArray4789, stream);
		encodeIntegerScript(anIntArray4829, stream);
		encodeIntegerScript(anIntArray4805, stream);
		
		stream.writeByte(0);
		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.setBytes(data, 0, data.length);
		return data;
	}

	public static void writeWidget(Cache store, int id, WCDefinition[] defs) {
		if(store.getIndices()[3].getArchive(id) == null) {
			store.getIndices()[3].putArchive(id, store);
		}
		for(int i = 0; i < defs.length; i++)
			store.getIndices()[3].putFile(id, i, defs[i].getData());
	}

	final void decode(InputStream stream) {
		newInt = stream.readUnsignedByte();
		if(newInt == 255)
			newInt = -1;
		type = stream.readUnsignedByte();
		if ((type & 0x80 ^ 0xffffffff) != -1) {
			type &= 0x7f;
			aString4765 = stream.readString();
		}
		anInt4814 = stream.readUnsignedShort();
		startX = stream.readShort();
		startY = stream.readShort();
		width = stream.readUnsignedShort();
		height = stream.readUnsignedShort();
		horizontalResizeMode = (byte) stream.readByte();
		verticalResizeMode = (byte) stream.readByte();
		horizontalPositionMode = (byte) stream.readByte();
		verticalPositionMode = (byte) stream.readByte();
		parentId = stream.readUnsignedShort();
		if (parentId != 65535) {
			parentId = parentId + (hash & ~0xffff);
		} else {
			parentId = -1;
		}
		hiddenByte = stream.readUnsignedByte();
		hidden = (0x1 & hiddenByte ^ 0xffffffff) != -1;
		if (newInt >= 0)
			aBoolean4858 = (hiddenByte & 0x2 ^ 0xffffffff) != -1;
		if (type == 0) {
			containerMaxScrollW = stream.readUnsignedShort();
			containerMaxScrollH = stream.readUnsignedShort();
			if ((newInt ^ 0xffffffff) > -1)
				aBoolean4858 = (boolean4858Byte = stream.readUnsignedByte()) == 1;
		}
		if (type == 5) {
			spriteId = stream.readInt();
			spriteI1 = stream.readUnsignedShort();
			spriteB1B2Byte = stream.readUnsignedByte();
			spriteB1 = (spriteB1B2Byte & 0x1 ^ 0xffffffff) != -1;
			spriteB2 = (spriteB1B2Byte & 0x2) != 0;
			componentAlpha = stream.readUnsignedByte();
			spriteBorderThickness = stream.readUnsignedByte();
			spriteShadowColor = stream.readInt();
			spriteFlippedW = (spriteFlippedWByte = stream.readUnsignedByte()) == 1;
			spriteFlippedH = (spriteFlippedHByte = stream.readUnsignedByte()) == 1;
			componentColor = stream.readInt();
			if (newInt >= 3)
				spriteB3 = (spriteB3Byte = stream.readUnsignedByte()) == 1;
		}
		if (type == 6) {
			modelType = 1;
			if(isNewWidget())
				modelId = stream.readBigSmart();
			else {
				modelId = stream.readUnsignedShort();
				if ((modelId ^ 0xffffffff) == -65536) {
					modelId = -1;
				}
			}
			unknownByte7 = stream.readUnsignedByte();
			aBoolean4707 = (0x4 & unknownByte7) == 4;
			unknownBoolean1 = (0x1 & unknownByte7) == 1;
			aBoolean4865 = (unknownByte7 & 0x2 ^ 0xffffffff) == -3;
			aBoolean4727 = (0x8 & unknownByte7 ^ 0xffffffff) == -9;
			if (unknownBoolean1) {
				anInt4709 = stream.readShort();
				anInt4797 = stream.readShort();
				anInt4815 = stream.readUnsignedShort();
				anInt4821 = stream.readUnsignedShort();
				anInt4682 = stream.readUnsignedShort();
				anInt4787 = stream.readUnsignedShort();
			} else if (aBoolean4865) {
				anInt4709 = stream.readShort();
				anInt4797 = stream.readShort();
				anInt4842 = stream.readShort();
				anInt4815 = stream.readUnsignedShort();
				anInt4821 = stream.readUnsignedShort();
				anInt4682 = stream.readUnsignedShort();
				anInt4787 = stream.readShort();
			}
			if(isNewWidget())
				anInt4773 = stream.readBigSmart();
			else {
				anInt4773 = stream.readUnsignedShort();
				if ((anInt4773 ^ 0xffffffff) == -65536) {
					anInt4773 = -1;
				}
			}
			if (horizontalResizeMode != 0) {
				anInt4800 = stream.readUnsignedShort();
			}
			if ((verticalResizeMode ^ 0xffffffff) != -1) {
				anInt4849 = stream.readUnsignedShort();
			}
		}
		if (type == 4) {
			if(isNewWidget())
				textFontId = stream.readBigSmart();
			else {
				textFontId = stream.readUnsignedShort();
				if ((textFontId ^ 0xffffffff) == -65536) {
					textFontId = -1;
				}
			}
			if ((newInt ^ 0xffffffff) <= -3) {
				textDropFontColors = ((textDropFontColorsByte = stream.readUnsignedByte()) ^ 0xffffffff) == -2;
			}
			text = stream.readString();
			textLineHeight = stream.readUnsignedByte();
			textHorizontalPositionMode = stream.readUnsignedByte();
			textVerticalPositionMode = stream.readUnsignedByte();
			textHasShadow = ((textHasShadowByte = stream.readUnsignedByte()) ^ 0xffffffff) == -2;
			componentColor = stream.readInt();
			componentAlpha = stream.readUnsignedByte();
			if ((newInt ^ 0xffffffff) <= -1) {
				textMaxLines = stream.readUnsignedByte();
			}
		}
		if ((type ^ 0xffffffff) == -4) { // box
			componentColor = stream.readInt();
			boxIsFilled = ((boxIsFilledByte = stream.readUnsignedByte()) ^ 0xffffffff) == -2;
			componentAlpha = stream.readUnsignedByte();
		}
		if ((type ^ 0xffffffff) == -10) { // line
			lineThickWidth = stream.readUnsignedByte();
			componentColor = stream.readInt();
			lineFlip = (lineFlipByte = stream.readUnsignedByte()) == 1;
		}
		componentClickType = stream.read24BitInt();
		unknownByte12 = stream.readUnsignedByte();
		// No idea what this whole code segment is
		if ((unknownByte12 ^ 0xffffffff) != -1) {
			aByteArray4806 = new byte[11];
			aByteArray4733 = new byte[11];
			anIntArray4705 = new int[11];
			for (; unknownByte12 != 0; unknownByte12 = stream.readUnsignedByte()) {
				int index = (unknownByte12 >> 4) - 1;
				unknownByte12 = stream.readUnsignedByte() | unknownByte12 << 8;
				unknownByte12 &= 0xfff;
				if (unknownByte12 == 4095) {
					unknownByte12 = -1;
				}
				unknownByte15 = (byte) stream.readByte();
				if ((unknownByte15 ^ 0xffffffff) != -1) {
					aBoolean4802 = true;
				}
				unknownByte16 = (byte) stream.readByte();
				if(index == -1)
					continue;
				anIntArray4705[index] = unknownByte12;
				aByteArray4806[index] = unknownByte15;
				aByteArray4733[index] = unknownByte16;
			}
		}
		// No idea what this whole code segment is ^
		
		componentName = stream.readString();
		unknownByte17 = stream.readUnsignedByte();
		int leftClickOptionsSize = 0xf & unknownByte17;
		if (leftClickOptionsSize > 0) {
			clickOptions = new String[leftClickOptionsSize];
			for (int index1 = 0; (index1 ^ 0xffffffff) > (leftClickOptionsSize ^ 0xffffffff); index1++)
				clickOptions[index1] = stream.readString();
		}
		indexHolder = unknownByte17 >> 4;
		if ((indexHolder ^ 0xffffffff) < -1) {
			unknownByte18 = stream.readUnsignedByte();
			anIntArray4863 = new int[unknownByte18 - -1];
			for (int index2 = 0; (index2 ^ 0xffffffff) > (anIntArray4863.length ^ 0xffffffff); index2++)
				anIntArray4863[index2] = -1;
			anIntArray4863[unknownByte18] = stream.readUnsignedShort();
		}
		if ((indexHolder ^ 0xffffffff) < -2) {
			unknownByte19 = stream.readUnsignedByte();
			anIntArray4863[unknownByte19] = stream.readUnsignedShort();
		}
		specialClick = stream.readString();
		if (specialClick.equals("")) {
			specialClick = null;
		}
		anInt4708 = stream.readUnsignedByte();
		anInt4795 = stream.readUnsignedByte();
		anInt4860 = stream.readUnsignedByte();
		aString4786 = stream.readString();
		widgetSettingArg = -1;
		if((method925(componentClickType) ^ 0xffffffff) != -1) {
			widgetSettingArg = stream.readUnsignedShort();
			if (widgetSettingArg == 65535) {
				widgetSettingArg = -1;
			}
			anInt4698 = stream.readUnsignedShort();
			if ((anInt4698 ^ 0xffffffff) == -65536) {
				anInt4698 = -1;
			}
			anInt4839 = stream.readUnsignedShort();
			if (anInt4839 == 65535) {
				anInt4839 = -1;
			}
		}
		if (newInt >= 0) {
			anInt4761 = stream.readUnsignedShort();
			if (anInt4761 == 65535) {
				anInt4761 = -1;
			}
		}
		widgetSettings = new WidgetSettings(componentClickType, widgetSettingArg);
		if (newInt >= 0) {
			unknownByte20 = stream.readUnsignedByte();
			for (int i = 0; unknownByte20 > i; i++) {
				unknown24BitInteger2 = stream.read24BitInt();
				unknownInt1 = stream.readInt();
				aHashTable4823.put((long) unknown24BitInteger2, unknownInt1);
			}
			unknownByte21 = stream.readUnsignedByte();
			for (int i = 0; i < unknownByte21; i++) {
				unknown24BitInteger3 = stream.read24BitInt();
				unknownJagString1 = stream.readJagString();
				aHashTable4823.put((long) unknown24BitInteger3, unknownJagString1);
			}
		}
		cs2HoverArray = decodeScript(stream);
		anObjectArray4706 = decodeScript(stream);
		cs2IdleComponentHover = decodeScript(stream);
		anObjectArray4771 = decodeScript(stream);
		anObjectArray4768 = decodeScript(stream);
		anObjectArray4807 = decodeScript(stream);
		anObjectArray4742 = decodeScript(stream);
		anObjectArray4788 = decodeScript(stream);
		anObjectArray4701 = decodeScript(stream);
		cs2CloseWidget = decodeScript(stream);
		if ((newInt ^ 0xffffffff) <= -1) {
			anObjectArray4751 = decodeScript(stream);
		}
		cs2ComponentHover = decodeScript(stream);
		anObjectArray4774 = decodeScript(stream);
		anObjectArray4803 = decodeScript(stream);
		anObjectArray4680 = decodeScript(stream);
		anObjectArray4856 = decodeScript(stream);
		anObjectArray4852 = decodeScript(stream);
		anObjectArray4711 = decodeScript(stream);
		anObjectArray4753 = decodeScript(stream);
		anObjectArray4688 = decodeScript(stream);
		anObjectArray4775 = decodeScript(stream);
		anIntArray4838 = method4150(stream);
		anIntArray4833 = method4150(stream);
		anIntArray4789 = method4150(stream);
		anIntArray4829 = method4150(stream);
		anIntArray4805 = method4150(stream);
	}

	private final Object[] decodeScript(InputStream buffer) {
		int i = buffer.readUnsignedByte();
		if ((i ^ 0xffffffff) == -1) {
			return null;
		}
		Object[] scriptParams = new Object[i];
		for (int index = 0; i > index; index++) {
			int i_4_ = buffer.readUnsignedByte();
			if (i_4_ == 0) {
				scriptParams[index] = new Integer(buffer.readInt());
			} else if ((i_4_ ^ 0xffffffff) == -2) {
				scriptParams[index] = buffer.readString();
			}
		}
		hasScripts = true;
		return scriptParams;
	}

	private final int[] method4150(InputStream buffer) {
		int i = buffer.readUnsignedByte();
		if (i == 0) {
			return null;
		}
		int[] is = new int[i];
		for (int i_60_ = 0; i_60_ < i; i_60_++)
			is[i_60_] = buffer.readInt();
		return is;
	}
	
	private void encodeScript(Object[] script, OutputStream stream) {
		if(script == null) {
			stream.writeByte(0);
			return;
		}
		if(script == cs2HoverArray) {
			System.out.println("Hover array being encoded...");
		}
		stream.writeByte(script.length);
		for(int i = 0; i < script.length; i++) {
			if(script[i] instanceof Integer) {
				stream.writeByte(0); // Integer parameter
				stream.writeInt((int) script[i]);
			} else if(script[i] instanceof String) {
				stream.writeByte(1); // String parameter
				stream.writeString((String) script[i]);
			}
		}
	}
	
	private void encodeIntegerScript(int[] script, OutputStream stream) {
		if(script == null) {
			stream.writeByte(0);
			return;
		}
		for(int i = 0; i < script.length; i++)
			stream.writeInt(script[i]);
	}
	
	public boolean isHidden() {
		return hiddenByte == 1;
	}
	
	public void setHidden(boolean b) {
		hiddenByte = b ? 1 : 0;
		this.hidden = b;
	}
	
	public void setBoxFilled(boolean b) {
		boxIsFilledByte = b ? 1 : 0;
		this.boxIsFilled = b;
	}
	
	public void setSpriteB1B2(boolean b) {
		spriteB1B2Byte = b ? 1 : 0;
		this.spriteB1 = b;
		this.spriteB2 = b;
	}
	
	public void setSpriteB3(boolean b) {
		spriteB3Byte = b ? 1 : 0;
		this.spriteB3 = b;
	}
	
	public void setSpriteFlippedW(boolean b) {
		spriteFlippedWByte = b ? 1 : 0;
		this.spriteFlippedW = b;
	}
	
	public void setSpriteFlippedH(boolean b) {
		spriteFlippedHByte = b ? 1 : 0;
		this.spriteFlippedH = b;
	}
	
	public void setTextDropFontColors(boolean b) {
		textDropFontColorsByte = b ? 1 : 0;
		this.textDropFontColors = b;
	}
	
	public void setTextHasShadow(boolean b) {
		textHasShadowByte = b ? 1 : 0;
		this.textHasShadow = b;
	}
	
	public void setLineFlipped(boolean b) {
		lineFlipByte = b ? 1 : 0;
		this.lineFlip = b;
	}
	
	public String getName() {
		return componentName;
	}
	
	public void setName(String s) {
		this.componentName = s;
	}
	
	public int getAlpha() {
		return componentAlpha;
	}
	
	public void setAlpha(int alpha) {
		this.componentAlpha = alpha;
	}
	
	public int getClickType() {
		return componentClickType;
	}
	
	public void setClickType(int clickType) {
		this.componentClickType = clickType;
	}
	
	public int getStartX() {
		return startX;
	}
	
	public void setStartX(int x) {
		this.startX = x;
	}
	
	public int getStartY() {
		return startY;
	}
	
	public void setStartY(int y) {
		this.startY = y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setWidth(int i) {
		this.width = i;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setHeight(int i) {
		this.height = i;
	}
	
	public int getSpriteId() {
		return spriteId;
	}
	
	public void setSpriteId(int i) {
		this.spriteId = i;
	}
	
	public int getContainerMaxScrollH() {
		return containerMaxScrollH;
	}
	
	public void setContainerMaxScrollH(int i) {
		this.containerMaxScrollH = i;
	}
	
	public int getContainerMaxScrollW() {
		return containerMaxScrollW;
	}
	
	public void setContainerMaxScrollW(int i) {
		this.containerMaxScrollW = i;
	}
	
	public boolean isContainer() {
		return type == 0;
	}
	
	public boolean isText() {
		return type == 4;
	}
	
	public boolean isBox() {
		return type == 3;
	}
	
	public boolean isSprite() {
		return type == 5;
	}
	
	public boolean isLine() {
		return type == 9;
	}
	
	public boolean isModel() {
		return type == 6;
	}
	
	public int getType() {
		return (int) getType(false);
	}
	
	public Object getType(boolean name) {
		if(!name)
			return type;
		else
			switch(type) {
				case 0:
					return "Container";
				case 3:
					return "Box";
				case 4:
					return "Text";
				case 5:
					return "Sprite";
				case 6:
					return "Model";
				case 9:
					return "Line";
			}
		return 0;
	}
	
	public void setType(String name) {
		switch(name) {
			case "container":
				type = 0;
				break;
			case "text":
				type = 4;
				break;
			case "box":
				type = 3;
				break;
			case "sprite":
				type = 5;
				break;
			case "line":
				type = 9;
				break;
			case "model":
				type = 6;
				break;
			default:
				type = 0; // Container
				break;
		}
	}
	
	/**
	 * Adds a model or sprite over a component when
	 * a person hovers their mouse over it.
	 * @param idleId
	 * @param hoverId
	 * @param model
	 */
	public void addComponentHover(int idleId, int hoverId, boolean model) {
		cs2IdleComponentHover = new Object[3];
		cs2IdleComponentHover[0] = model ? 67 : 44;
		cs2IdleComponentHover[1] = -2147483645;
		cs2IdleComponentHover[2] = idleId;
		cs2ComponentHover = new Object[3];
		cs2ComponentHover[0] = model ? 67 : 44;
		cs2ComponentHover[1] = -2147483645;
		cs2ComponentHover[2] = hoverId;
	}
	
	/**
	 * Adds options to the component
	 * @param options
	 */
	public void addClickOptions(String... options) {
		clickOptions = new String[options.length];
		for(int i = 0; i < options.length; i++)
			clickOptions[i] = options[i];
		componentClickType = 2; // Only shows the user's set click options.
	} 
	
	/**
	 * The 'x' button which closes the interface.
	 */
	public void addCloseWidgetScript() {
		cs2CloseWidget = new Object[1];
		cs2CloseWidget[0] = 29;
	}
	
	/**
	 * Adds a scroll bar to the widget with the specified
	 * componentHash being the hashId for the container.
	 * @param componentHash
	 */
	public void addScrollBar(int componentHash) {
		cs2HoverArray = new Object[9];
		cs2HoverArray[0] = 63; // Script ID
		cs2HoverArray[1] = -2147483645; // Client constant
		cs2HoverArray[2] = componentHash;
		cs2HoverArray[3] = 792;
		cs2HoverArray[4] = 789;
		cs2HoverArray[5] = 790;
		cs2HoverArray[6] = 791;
		cs2HoverArray[7] = 773;
		cs2HoverArray[8] = 788;
	}
	
	public boolean isNewWidget() {
		return getWidgetId() > 1144;
	}
	
	public int getComponentId() {
		return hash - (getWidgetId() << 16);
	}

	public int getWidgetId() {
		return hash >> 16;
	}
	
	/**
	 * Compares all of the variables in this class.
	 * @param iiii
	 * @throws Exception
	 */
	public static void compareDefs(int iiii) throws Exception {
		Cache.load();
		int[] ids = { iiii };
		Class<WCDefinition> c = WCDefinition.class;
		for(Field f : c.getDeclaredFields()) {
			f.setAccessible(true);
			System.out.print("["+f.getName()+"](");
			for(int id : ids) {
				WCDefinition[] defs = WCDefinition.getComponents(id);
				try {
					for(int i = 0; i < defs.length; i++)
						System.out.print(i+""+(i == defs.length - 1 ? "" : ", ")+"");
					System.out.print("): ");
					for(WCDefinition def : defs) { // Each individual component
//					WCDefinition def = defs[0];
					Object o = f.get(def);
					if(o instanceof WCDefinition[][] || o instanceof WidgetSettings)
						continue;
						if(o instanceof int[]) {
							int[] intArray = (int[]) o;
							System.out.print(Arrays.toString(intArray)+""+(id == ids[ids.length - 1] ? "" : ", "));
						} else if(o instanceof String[]) {
							String[] array = (String[]) o;
							System.out.print(Arrays.toString(array)+""+(id == ids[ids.length - 1] ? "" : ", "));
						} else if(o instanceof Object[]) {
							Object[] array = (Object[]) o;
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
							System.out.print(f.get(def)+""+(id == ids[ids.length - 1] ? "" : ", ")+", ");
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		try {
			//addWidgetFromFolder(371);
			//createItemSearchWidget();
			//compareDefs(1151);//116);
//			Cache.load();
//			WCDefinition[] defs = WCDefinition.getComponents(1152); // Test
//			defs[0].anObjectArray4807 = null;
//			defs[0].anIntArray4838 = null;
//			writeWidget(Cache.INSTANCE, defs[0].getWidgetId(), defs);
//			System.out.println("done");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addWidgetFromFolder(int folderID) {
		try {
			Cache.load();
			Cache store = Cache.INSTANCE;
			WCDefinition def = WCDefinition.createWidget(-1);
			int id = def.getWidgetId();
			File f = new File("./data/ce/widgets/backups/Mon Dec 18, 2017/"+folderID+"/");
			for(int i = 0; i < f.listFiles().length; i++)
				WCDefinition.addComponent(id);
			if(store.getIndices()[3].getArchive(id) == null)
				store.getIndices()[3].putArchive(id, store);
			for(File comp : f.listFiles()) {
				if(comp.getPath().endsWith(".dat")) {
					byte[] data = CacheUtils.getBytesFromFile(comp);
					int componentId = Integer.parseInt(comp.getName().replaceAll(".dat", ""));
					store.getIndices()[3].putFile(id, componentId, data);
				}
			}
			System.out.println("New widget: "+def.getWidgetId());
			System.out.println(Cache.INSTANCE.getIndices()[3].getArchive(id) == null);
			System.out.println("Done!!!!!!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a custom widget that lets
	 * a person search for items to spawn easily.
	 */
	public static void createItemSearchWidget() {
		try {
			Cache.load();
			WCDefinition def = WCDefinition.createWidget(1150);
			def.setType("container");
			def.containerMaxScrollH = 1000;
			def.startX = 3;
			def.startY = 25;
			def.width = 170;
			def.height = 250;
			int hash = (0 + (def.getWidgetId() << 16));
			WCDefinition scroll = WCDefinition.addComponent(def.getWidgetId());
			scroll.setType("container");
			scroll.startX = 172;
			scroll.startY = 25;
			scroll.width = 16;
			scroll.height = 230;
			scroll.addScrollBar(hash);
			WCDefinition xB = WCDefinition.addComponent(def.getWidgetId());
			xB.setType("sprite");
			xB.componentName = "";
			xB.spriteId = 2201;
			xB.addComponentHover(2201, 2202, false);
			xB.addClickOptions("Close");
			xB.startX = 2;
			xB.startY = 2;
			xB.width = 16;
			xB.height = 16;
			WCDefinition search = WCDefinition.addComponent(def.getWidgetId());
			search.setType("sprite");
			search.componentName = "";
			search.spriteId = 8497;
			search.addClickOptions("Search");
			search.startX = 160;
			search.startY = 2;
			search.width = 24;
			search.height = 17;
			WCDefinition text = WCDefinition.addComponent(def.getWidgetId());
			text.setType("text");
			text.textFontId = 494;
			text.componentName = "Results";
			text.text = "\t<col=ffffff>Item Search";
			text.startX = 22;
			text.startY = 4;
			text.width = 5;
			text.height = 5;
			System.out.println("New widget: "+def.getWidgetId());
			writeWidget(Cache.INSTANCE, def.getWidgetId(), WCDefinition.getComponents(def.getWidgetId()));
			System.out.println("Done!!!!!!");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return ""+getWidgetId();
	}

}
