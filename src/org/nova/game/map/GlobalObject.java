package org.nova.game.map;

import org.nova.cache.definition.ObjectDefinition;
import org.nova.game.Game;
import org.nova.game.masks.Animation;
import org.nova.utility.loading.map.ObjectExamines;

@SuppressWarnings("serial")
public class GlobalObject extends Location {

	private int id;
	private int type;
	private int rotation;
	private int life;
	private boolean needingSpawn;
	
	public GlobalObject(int id, int type, int rotation, Location tile) {
		super(tile.getX(), tile.getY(), tile.getZ());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public GlobalObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
	}

	public GlobalObject(int id, int type, int rotation, Location tile,
			boolean needingSpawn) {
		super(tile.getX(), tile.getY(), tile.getZ());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
		this.needingSpawn = needingSpawn;
	}

	public GlobalObject(int id, int type, int rotation, int x, int y,
			int plane, int life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
	}

	public GlobalObject(GlobalObject object) {
		super(object.getX(), object.getY(), object.getZ());
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	public GlobalObject(int objectId, GlobalObject object) {
		super(object.getX(), object.getY(), object.getZ());
		this.id = objectId;
		this.type = object.getType();
		this.rotation = object.getRotation();
	}

	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void decrementObjectLife() {
		this.life--;
	}

	public ObjectDefinition defs() {
		return ObjectDefinition.get(id);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getExamine() {
		return ObjectExamines.getExamine(this);
	}

	public boolean needingSpawn() {
		return needingSpawn;
	}

	/**
	 * Returns if this object completely matches the other, except in terms of
	 * ID.
	 * 
	 * @param other
	 * @return
	 */
	public boolean matchesObject(GlobalObject other) {
		return other.getX() == getX() && other.getY() == getY() && other.getZ() == getZ() 
			&& other.getType() == getType() && other.getRotation() == getRotation();
	}

	public String getName() {
		return defs().name;
	}

	public void animate(int i) {
		Game.sendObjectAnimation(this, new Animation(i));
	}

	public String getInformation() {
		GlobalObject o = this;
		return (o.getId()+" - "+o.getName()+" - "+o.toString()+" - ["+o.getRotation()+", "+o.getType()+"]");
	}

}
