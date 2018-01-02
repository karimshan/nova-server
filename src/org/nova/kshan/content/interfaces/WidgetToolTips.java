package org.nova.kshan.content.interfaces;

import java.util.List;

import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.network.stream.InputStream;

public class WidgetToolTips {
	
	@SuppressWarnings("unchecked")
	public static void decode(Player player, InputStream stream) {
		int inter = stream.readInt();
		int button = stream.readInt();
		int slot = stream.readInt();
		if(inter == 620 && button == 25) {
			player.packets().sendInterfaceHoverMessage("Item in slot: "+slot, inter, button, slot);
			return;
		}
		if(inter == 1150) {
			if(button == 0) {
				List<Integer> items = (List<Integer>) player.getData().getRuntimeData().get("item_search");
				if(slot > items.size())
					return;
				player.packets().sendInterfaceHoverMessage((new Item(items.get(slot)).getExamine()), inter, button, slot);
			} else if(button == 3)
				player.packets().sendInterfaceHoverMessage("Click here to search\nfor an item.", inter, button, slot);
			else if(button == 2)
				player.packets().sendInterfaceHoverMessage("Reset search", inter, button, slot);
			return;
		}
	}

}
