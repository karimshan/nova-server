package org.nova.kshan.content.areas;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.nova.game.map.Location;

/**
 * This class represents an "Area" in the game world.
 * 
 * @author K-Shan
 *
 */
public class Area {
	
	private static final ArrayList<Area> AREAS = new ArrayList<Area>();
	
	public static void initialize() {
		AREAS.clear();
		addAreas();
	}

	private String name;

	private Point southWest;
	private Point northEast;
	
	private byte z;

	private AreaType type;
	
	private boolean polygon;
	private int sides;
	private int[][] points;
	private Location[] locations;
	
	public Area(String name, AreaType type, Location[] loc) {
		this.name = name;
		this.type = type;
		this.z = 0;
		locations = loc;
		sides = loc.length;
		points = new int[sides][2];
		for (int i = 0; i < sides; i++) {
			getPoints()[i][0] = loc[i].getX();
			getPoints()[i][1] = loc[i].getY();
		}
		polygon = true;
	}
	
	public Area(Point southWest, Point northEast, byte z, String name, AreaType type) {
		this.name = name;
		this.southWest = southWest;
		this.northEast = northEast;
		this.type = type;
		this.z = z;
	}
	
	public Area() {
		name = "";
		southWest = null;
		northEast = null;
		z = 0;
    }
	
	public static Area get(String name) {
		for (Area areas : getAreas())
			if(areas.getName().equalsIgnoreCase(name))
				return areas;
		return null;
	}
	
	public static boolean inArea(Location location, Area area) {
		if(area.isPolygon()) {
			boolean inside = false;
			int y = location.getY(), x = location.getX();
			int sides = area.getSides() - 1;
			for (int i = 0; i < area.getSides(); sides = i++) {
				if ((area.getPoints()[i][1] < y && area.getPoints()[sides][1] >= y) || (area.getPoints()[sides][1] < y && area.getPoints()[i][1] >= y)) {
					if (area.getPoints()[i][0] + (y - area.getPoints()[i][1]) / (area.getPoints()[sides][1] - area.getPoints()[i][1]) * 
							(area.getPoints()[sides][0] - area.getPoints()[i][0]) < x) {
						inside = !inside;
					}
				}
			}
			return inside;
		} else
			return location.getX() >= area.getSouthWest().getX() && location.getX() <= area.getNorthEast().getX()
				&& location.getY() >= area.getSouthWest().getY() && location.getY() <= area.getNorthEast().getY();
	}
	
	public static void addAreas() {
		Area area = null;
		try {
			BufferedReader r = new BufferedReader(new FileReader(new File("data/map/areas.txt")));
			while (true) {
				String line = r.readLine();
				boolean ignore = false;
				if (line == null)
					break;
				if (line.startsWith("//") || line.equals(""))
					continue;
				if (line.contains("/*"))
					continue;
				ignore = true;
				if (line.contains("*/"))
					if (ignore == true)
						continue;
				ignore = false;
				boolean poly = false;
				String[] splitLine = line.split(" - ");
				String areaName = splitLine[0];
				String[] sP = splitLine[1].split(", ");
				for(AreaType t : AreaType.values())
					if(sP[0].equalsIgnoreCase(t.name()))
						poly = true;
				if(poly) {
					sP = splitLine[1].split(", ");
					String areaType = sP[0];
					byte z = Byte.parseByte(sP[1]);
					String[] coords = splitLine[2].split(", ");
					Location[] loc = new Location[coords.length / 2];
					int count = 0;
					for (int i = 0; i < loc.length; i++)
						loc[i] = new Location(Integer.valueOf(coords[count++]), Integer.valueOf(coords[count++]), z);
					if(areaName != null)
						area = new Area(areaName, getType(areaType), loc);
				} else {
					sP = splitLine[1].split(", ");
					Point sW = new Point(Integer.parseInt(sP[0]), Integer.parseInt(sP[1]));
					Point nE = new Point(Integer.parseInt(sP[2]), Integer.parseInt(sP[3]));
					byte z = Byte.parseByte(sP[4]);
					String areaType = sP[5];
					if(areaName != null)
						area = new Area(sW, nE, z, areaName, getType(areaType));
				}
				AREAS.add(area);
			}	
			r.close();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	public short getLength() {
		return (short) (northEast.getX() - southWest.getX());
	}
	
	public short getWidth() {
		return (short) (northEast.getY() - southWest.getY());
	}
	
	public Location getCenter() {
		return new Location(southWest.x + (Math.round(getLength() / 2)), southWest.y + (Math.round(getWidth() / 2)), z);
	}
	
	public Point getNorthEast() {
		return northEast;
	}

	public Point getSouthWest() {
		return southWest;
	}
	
	public String getName() {
		return name;
	}
	
	public AreaType getAreaType() {
		return type;
	}
	
	public static AreaType getType(String type) {
		switch(type) {
			case "PVP":
				return AreaType.PVP;
			case "DESERT":
				return AreaType.DESERT;
			case "MULTI":
				return AreaType.MULTI;
			case "MEMBERS":
				return AreaType.MEMBERS_ONLY;
			case "MULTI_PVP":
				return AreaType.MULTI_PVP;
			case "NORMAL":
				return AreaType.NORMAL;
		}
		return null;
	}
	
	public static ArrayList<Area> getAreas() {
		return AREAS;
	}
	
	public enum AreaType {
		NORMAL, MEMBERS_ONLY, PVP, MULTI, MULTI_PVP, DESERT
	}
	
	public int[][] getPoints() {
		return points;
	}
	
	public int getSides() {
		return sides;
	}
	
	public boolean isPolygon() {
		return polygon;
	}
	
	public Location[] getLocations() {
		return locations;
	}
}
