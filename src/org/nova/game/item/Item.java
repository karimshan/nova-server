package org.nova.game.item;

import java.io.Serializable;

import org.nova.cache.definition.ItemDefinition;
import org.nova.cache.loaders.ItemEquipIds;
import org.nova.kshan.utilities.FormatUtils;
import org.nova.utility.loading.items.ItemExamines;

public class Item implements Serializable {

	private static final long serialVersionUID = -6485003878697568087L;

	public short id;
	public int amount;
	
	public int getId() {
		return id;
	}

	public Item clone() {
		return new Item(id, amount);
	}

	public Item(int id) {
		this(id, 1);
	}

	public Item(int id, int amount) {
		this(id, amount, false);
	}

	public Item(int id, int amount, boolean amt0) {
		this.id = (short) id;
		this.amount = amount;
		if (this.amount <= 0 && !amt0) {
			this.amount = 1;
		}
	}

	public ItemDefinition defs() {
		return ItemDefinition.get(id);
	}

	public int getEquipId() {
		return ItemEquipIds.getEquipId(id);
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void setId(int id) {
		this.id = (short) id;
	}

	public int getAmount() {
		return amount;
	}

	public String getName() {
		return defs().getName();
	}
	
	public String getExamine() {
		if (getAmount() >= 100000)
			return getAmount() + " x " + getName()
					+ ".";
		else if (defs().isNoted())
			return "Swap this note at any bank for the equivalent item.";
		else {
			String e = getExamineFromFile();;
			if (e != null)
				return e;
		}
		return FormatUtils.formatForExamine(getName());
	}
	
	private String getExamineFromFile() {
		return ItemExamines.getExamine(this.getId());
	}
	
	@Override
	public String toString() {
		return getId()+" - "+getName();
	}

}
