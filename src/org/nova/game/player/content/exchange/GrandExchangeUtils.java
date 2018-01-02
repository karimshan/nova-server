package org.nova.game.player.content.exchange;

import static org.nova.game.player.content.exchange.GrandExchangeConstants.UNEXCHANGEABLE_ITEMS;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;
import org.nova.utility.EconomyPrices;

public class GrandExchangeUtils {

	public boolean canExchange(int id) {
		for(int i : UNEXCHANGEABLE_ITEMS) {
			if(id == i) {
				return false;
			}
		}
		return true;
	}


	public void sendPriceConfigs(Player player, int id) {
		try{
		ItemDefinition def = ItemDefinition.get(id);
		player.packets().sendConfig(1115, EconomyPrices.getPrice(def.getId()));
		player.packets().sendConfig(1114, EconomyPrices.getPrice(def.getId()));
		player.packets().sendConfig(1116, EconomyPrices.getPrice(def.getId()));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void displaySellInterface(Player player, int offerBox) {
		reset(player);
		player.buying = false;
		player.packets().sendConfig(1113, 1);
		player.interfaces().sendInterface(107);
		player.packets().sendRunScript(149, GrandExchangeConstants.OFFER);
		player.packets().sendAccessMask(0, 27, 107, 18, 0, 1026);
		sendInventory(player);
		player.packets().setInterfaceConfig(107, 0, false);
	}

	public void displayBuyInterface(Player player, int offerBox) {
	try{
		player.buying = true;
		reset(player);
		player.packets().sendConfig(1113, 0);
		player.packets().setGeSearch(GrandExchangeConstants.SEARCH);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void sendInventory(Player player) {
		player.interfaces().sendInventoryInterface(621);
		player.packets().sendItems(93, player.getInventory().getItems());
		player.packets().sendUnlockIComponentOptionSlots(621, 0, 0, 27, 0,
				1, 2, 3, 4, 5);
	}
	public void reset(Player player) {
		try{
		player.packets().sendConfig(1109, -1);
		player.packets().sendConfig(1110, 0);	
		player.packets().sendConfig(1111, 0);
		player.packets().sendConfig(1112, -1);	
		player.packets().sendConfig(1113, 0);
		player.box = 0;
		player.geItem = 0;
		player.price = 0;
		player.geAmount = 0;
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void set(Player player, int id, int price, int amount) {
		try{
		player.packets().sendConfig(1109, id);	
		player.packets().sendConfig(1110, amount);
		player.packets().sendConfig(1111, price);
		player.geItem = id;
		player.price = price;
		player.geAmount = amount;
	}catch (Exception e){
		e.printStackTrace();
	}
	}

	public void setPrice(Player player, int type, ItemDefinition def) {
		try{
		int price = 0;
		switch(type) {
		case 0:
			price = EconomyPrices.getPrice(def.getId());
			break;
		case 1:
			price = EconomyPrices.getPrice(def.getId());
			break;
		case 2:
			price = EconomyPrices.getPrice(def.getId());
			break;
		}
		player.price = price;
		player.packets().sendConfig(1111, price);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void setAmount(Player player, int button) {
		try{
		int amount = (Integer) player.geAmount;
		int id = (Integer) player.geItem;
		switch(button) {
		case 155:
			if(amount > 1) {
				amount--;
			} else {
				amount = 1;
			}
			break;
		case 157:
			if(amount < Integer.MAX_VALUE) {
				amount++;
			}
			break;
		case 160:
			if(player.buying == true) {
				amount += 1;
			} else {
				amount = 1;
			}
			break;
		case 162:
			if(player.buying == true) {
				amount += 10;
			} else {
				amount = 10;
			}
			break;
		case 164:
			if(player.buying == true) {
				amount += 100;
			} else {
				amount = 100;
			}
			break;
		case 166:
			if(player.buying == true) {
				amount += 1000;
			} else {
				amount = player.getInventory().getItems().getNumberOf(id);
			}
			break;
		case 168:
		case 170:
			player.packets().sendConfig(1111, (Integer) player.price);
			break;
		}
		if(amount >= Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		}
		player.geAmount = amount;
		player.packets().sendConfig(1109, id);	
		player.packets().sendConfig(1110, amount);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	public int getExchangeBox(int button) {
		try{
		switch(button) {
		case 31: return 0;
		case 47: return 1;
		case 63: return 2;
		case 82: return 3;
		case 101: return 4;
		case 120: return 5;
		case 32: return 0;
		case 48: return 1;
		case 64: return 2;
		case 83: return 3;
		case 102: return 4;
		case 121: return 5;
		case 19: return 0;
		case 35: return 1;
		case 51: return 2;
		case 70: return 3;
		case 89: return 4;
		case 108: return 5;
		}
		}catch (Exception e){
			e.printStackTrace();
		}
		return -1;
	}


}
