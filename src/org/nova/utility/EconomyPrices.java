package org.nova.utility;

import java.io.BufferedReader;
import java.io.FileReader;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.content.ItemConstants;

public final class EconomyPrices {
	public static int loadExchangePrices(int itemId) {
		try (BufferedReader buf = new BufferedReader(new FileReader("./data/items/prices.txt"))) {
			String line;
			while ((line = buf.readLine()) != null) {

				String[] regex = line.split(",");
				if(line.startsWith(String.valueOf("ItemID: "+itemId))) {
					int itemValue = Integer.valueOf(regex[1].replace(" Value: ", ""));
					System.out.println("ITEMID: " + itemId + " PRICE: " + itemValue);
					return itemValue;
				}
			}
			buf.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;
	}
	public static int getPrice(int itemId) {
		ItemDefinition defs = ItemDefinition.get(itemId);
		
		if (defs.isNoted())
			itemId = defs.getCertId();
		else if (defs.isLended())
			itemId = defs.getCertId();
		if (!ItemConstants.isTradeable(new Item(itemId, 1)))
			return 1;
		if (itemId == 995) // TODO after here
			return 1;
			switch(itemId){
                case 11694:
                      //  item.getDefinitions().setValue(184500000);
                        break;
             
			
			
			}
		return loadExchangePrices(itemId);
									
	}

	private EconomyPrices() {

	}
}
