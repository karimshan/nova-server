package org.nova.cache.loaders;

public class WidgetSettings {
	
	public int anInt4602;
	public int settings;

	public WidgetSettings(int arg0, int arg1) {
		settings = arg0;
		anInt4602 = arg1;
	}

	public boolean method1879() {
		if (((0x2eaa42 & settings) >> -1445214219 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1881() {
		if (((0x55f65fb5 & settings) >> 1601172318 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1882() {
		if (((settings & 0x1fa3c81f) >> -233371236 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1883() {
		if ((0x1 & settings >> 111606079 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1884() {
		if ((0x1 & settings ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public boolean method1885() {
		if ((0x1 & settings >> 1361505174 ^ 0xffffffff) == -1)
			return false;
		return true;
	}

	public int method1887() {
		return (settings & 0x1f873d) >> 1483551026;
	}

	public int method1888() {
		return 0x7f & settings >> -809958741;
	}

	public boolean unlockedSlot(int slot) {
		if ((settings >> 1 + slot & 0x1 ^ 0xffffffff) == -1)
			return false;
		return true;
	}
}
