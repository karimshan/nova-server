package org.nova.kshan.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nova.game.map.Location;

/**
 * 
 * @author karim
 *
 */
public class LocationCrystal implements Serializable {
	
	private static final long serialVersionUID = 3017560566908959931L;
	
	private int charges;
	private int maxLocationCount;
	private boolean active;
	private List<String> names;
	private List<Location> locations;
	
	public LocationCrystal() {
		charges = 0;
		maxLocationCount = 3;
		names = new ArrayList<String>();
		locations = new ArrayList<Location>();
	}
	
	public int getCharges() {
		return charges;
	}
	
	public void setCharges(int i) {
		charges = i;
	}
	
	public int getMaxLocationCount() {
		return maxLocationCount;
	}
	
	public void setMaxLocationCount(int lc) {
		maxLocationCount = lc;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean b) {
		this.active = b;
	}

	public List<String> getNames() {
		return names;
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	
	public void addLocation(String name, Location loc) {
		names.add(name);
		locations.add(loc);
	}
	
	public void replace(int index, String newName, Location newLocation) {
		names.set(index, newName);
		locations.set(index, newLocation);
	}
	
	public void deleteLocation(int index) {
		names.remove(index);
		locations.remove(index);
	}
	
	public void clearCrystal() {
		names.clear();
		locations.clear();
	}

	public int getSize() {
		return names.size();
	}

}
