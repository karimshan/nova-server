package org.nova.game.player.actions.summoning;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;

public class SummonTrain {
	@SuppressWarnings("unused")
	private Player player;
	public SummonTrain(Player player) {
		this.player = player;
	}

	public static void CreatePouch(Player p, int lvl, int ShardAmt, int CharmId, int MasterItem, int MasterItem2, int Create, int xp) {

				if(p.getSkills().getLevelFromXP(23) < lvl) { //check lvl
					p.sm("You do not have a high enough Summoning level to create this Summoning pouch.");
					return;
				}

				if(MasterItem2 != -1 && p.getInventory().containsOneItem(MasterItem2) == false)  {//check second master item
					p.sm("You do not have all the materials to make these Summoning pouches.");
					ItemDefinition charm = ItemDefinition.get(CharmId);
					ItemDefinition masteritem = ItemDefinition.get(MasterItem);
					ItemDefinition masteritem2 = ItemDefinition.get(MasterItem2);
					p.sm("This pouch requires 1 "+masteritem2.getName()+", 1 "+masteritem.getName()+", 1 "+charm.getName()+" and "+ShardAmt+" spirit shards.");
					return;
				}

				if(p.getInventory().containsOneItem(12155) == false || p.getInventory().containsOneItem(12183, ShardAmt) == false || p.getInventory().containsOneItem(CharmId) == false || p.getInventory().containsOneItem(MasterItem) == false)  {//check all otheritems
					p.sm("You do not have all the materials to make these Summoning pouches.");
					ItemDefinition charm = ItemDefinition.get(CharmId);
					ItemDefinition masteritem = ItemDefinition.get(MasterItem);
					p.sm("This pouch requires 1 "+masteritem.getName()+", 1 "+charm.getName()+" and "+ShardAmt+" spirit shards.");
					return;
				}

			p.getInventory().deleteItem(12155, 1);
			p.getInventory().deleteItem(12183, ShardAmt);
			p.getInventory().deleteItem(CharmId, 1);
			p.getInventory().deleteItem(MasterItem, 1);
				if(MasterItem2 != -1) {
					p.getInventory().deleteItem(MasterItem2, 1);
				}
				p.getInventory().addItem(Create, 1);
				p.getSkills().addXp(23, xp);
				p.setNextAnimation(new Animation(725));
				p.setNextGraphics(new Graphics(1207));
	}

}
