package org.nova.game.player.content.exchange;

import java.util.LinkedList;
import java.util.List;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.EconomyPrices;
import org.nova.utility.ShopsHandler;


public final class GrandExchange extends GrandExchangeUtils {

	private final List<Offer> buy = new LinkedList<Offer>();
	private final List<Offer> sell = new LinkedList<Offer>();
	private int tempGESell;
	private Item item;
	public void handleButtons(Player player, int button, int packetId) {
		System.out.println("Button: "+button+" packetId: "+packetId);
		try{
		switch(button) {
		case 28:	
			player.closeInterfaces();
			break;
		case 31:
		case 47:
		case 63:
		case 82:
		case 101:
		case 120:
			int buyBox = getExchangeBox(button);
			if(buyBox > -1) {
				displayBuyInterface(player, buyBox);
				player.box = buyBox;
				player.packets().sendConfig(1112, buyBox);
			}
			break;
		case 32:
		case 48:
		case 64:
		case 83:
		case 102:
		case 121:
			player.closeInterfaces();
			ShopsHandler.openShop(player, 99);
			break;
		case 18:
		case 34:
		case 50:
		case 69:
		case 88:
		case 107: 
			int myBox = getExchangeBox(button);
			if(myBox > -1) {
				Offer myOffer = findOffer(player, myBox);
				if(myOffer != null) {
					boolean aborted = myOffer.isAborted();
					if(!aborted) {
						int[] items = {-1, -1};
						int[] amount = {0, 0};
						if(myOffer.getType() == Type.BUY){
							items[1] = myOffer.getId();
							items[0] = myOffer.getId();
							amount[1] = myOffer.coins;
							amount[0] = myOffer.getCurrentAmount();
						}
					}
					if(aborted) {
						int[] items = {-1, -1};
						int[] amount = {0, 0};
						if(myOffer.getType() == Type.BUY) {
							if(myOffer.coins > 0) {
								items[1] = 14484;
								items[0] = myOffer.getId();
								amount[1] = myOffer.coins;
								amount[0] = myOffer.getCurrentAmount();
							} else {
								items[0] = myOffer.getId();
								amount[0] = myOffer.getCurrentAmount();
							}
						} else if(myOffer.getType() == Type.SELL) {
							
						}
						player.packets().setItemSlot(myBox, items, amount);
					}
					player.packets().sendConfig(1112, myBox);
					player.box = myBox;
				}
			}
			break;
		case 203:
			int abortBox = (Integer) player.box;
			if(abortBox > -1) {
				Offer myOffer = findOffer(player, abortBox);
				if(myOffer == null) {
					return;
				}
				if(myOffer.isAborted()) {
					player.sendMessage("Your offer is currently aborting!");
					return;
				}
				myOffer.setAborted(true);
				int[] items = {-1, -1};
				int[] amount = {0, 0};
				if(myOffer.getType() == Type.SELL) {
					player.packets().setGe(abortBox, -3, myOffer.getId(), myOffer.getPrice(), myOffer.getAmount(), myOffer.getAmount() - myOffer.getCurrentAmount());
				} else if(myOffer.getType() == Type.BUY) {		
					player.packets().setGe(abortBox, -3, myOffer.getId(), myOffer.getPrice(), myOffer.getAmount(), myOffer.getAmount() - myOffer.getCurrentAmount());
				}
				player.packets().setItemSlot(abortBox, items, amount);
			}
			break;	
		case 157:
		case 159:
		case 162:
		case 164:
		case 166:
		case 168:
		case 171:
		case 173:
			setAmount(player, button);
			break;
		case 190://Sell Offer
			player.packets().sendInterface(true, 752, 12, 389);
			break;
		case 186:
			if(player.buying == true) {
				acceptOffer(player, Type.BUY);
			} else {
				acceptOffer(player, Type.SELL);
			}
			break;
		case 209:
			//216 = first   , 211 = second
			int box = (Integer) player.box;
			if(box > -1) {
				Offer myOffer = findOffer(player, box);
				
				if(myOffer != null) {
					int id = myOffer.getId();
					if(!myOffer.isAborted()) {
						if(myOffer.getType() == Type.BUY) {
							int freeSlots = player.getInventory().getFreeSlots();
							if(freeSlots < myOffer.getAmount() && !ItemDefinition.get(myOffer.getId()).isStackable()) {
								player.getInventory().addItem(myOffer.getId()+1, myOffer.getAmount());
								player.offers[box].setId(-1);
								player.offers[box] = null;
								player.packets().resetItemSlot(player, box);
								player.packets().setGe(box, 0, -1, -1, -1, 0);
							} else if(freeSlots > myOffer.getAmount() || ItemDefinition.get(myOffer.getId()).isStackable()) {
							player.getInventory().addItem(myOffer.getId(), myOffer.getAmount());
							player.offers[box].setId(-1);
							player.offers[box] = null;
							player.packets().resetItemSlot(player, box);
							player.packets().setGe(box, 0, -1, -1, -1, 0);
						//	player.getPackets().resetGe(box);
							}
						}
						if(myOffer.getType() == Type.SELL) {
							player.getInventory().addItem(995, this.tempGESell);
							player.offers[box].setId(-1);
							player.offers[box] = null;
							player.packets().resetItemSlot(player, box);
							player.packets().setGe(box, 0, -1, -1, -1, 0);
							//player.getPackets().resetGe(box);
						}
					}
				}
			}
			break;
		case 211:

			break;
		case 127:
			openGE(player);
			break;
		}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void openGE(final Player player) {
		try{
		player.geAmount = 0;
		player.geItem = 0;
		player.price = 0;
		player.box = 0;
		player.packets().sendConfig2(563, 4194304);
		player.packets().sendConfig(1112, -1);
		player.packets().sendConfig(1113, -1);
		player.packets().sendConfig(1109, -1);
		player.packets().sendConfig(1110, 0);
		player.interfaces().closeInventoryInterface();
		player.interfaces().sendInterface(105);
		player.packets().sendAccessMask(-1, -1, 105, 209, 0, 6);
		player.packets().sendAccessMask(-1, -1, 105, 211, 0, 6);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int[] nonex = {20135,11256,11257,11724,11726,11720,11718,11716,11714,11712, 13740, 13742,13744,13746,13738, 20139, 11704, 10362, 406,10354,405, 11706, 11708, 11702, 20143, 20159, 20163, 20167, 20147, 20151, 20155,1038,1040,1042,1044,11694,11695,
		1046,1048,1053,1055,1057,1050,13387,13893,13899,13905,13911,13917,13929, 13923, 13884, 13887, 13890, 13896,13902,13908,13914,13920,13926,13858,13861,13864,13867,13932,13935,13938,
		13941,13870,13873,13876,13879,13880,13881,13882,13883,13944,13947,13950,13953,13954,13955,13956,13957,
		11814, 11816,11818,11820,11822,11824,11826,11828,11830,11832,11834,11836,11838,118340,11842,11844,11846,11848,11850,11852,11854,
		19481,21768,21769, 14592, 14531,7400,7403,7398,7399,13746,13747,13748,13749,13750,13751,13753, 11286, 11287,989,985,986,987,988};

	public void acceptOffer(Player player, Type type) {
		int amount = (int) player.geAmount;
		int id = (int) player.geItem;
		int price = (int) player.price;
		long cost = (long) amount * price;
if (cost > Integer.MAX_VALUE) {
    return;
}
		int box = 1;
		if(type == Type.BUY) {
			if(price > 500000000) {
				if(amount > 5) {
					player.sendMessage("Your offer is too big!");
					return;
				}
			}
			for (int uij =11813; uij < 11947; uij++){
				if (uij == id){
				return;
				}
			}
			for (int uij = 24197; uij < 25000; uij++){
				if (uij == id){
				return;
				}
			}
			for (int uij =14524; uij < 11532; uij++){
				if (uij == id){
				return;
				}
			}
			for (int uij =19519; uij < 19599; uij++){
				if (uij == id){
				return;
				}
			}
			for (int po: nonex){
				if (po == id){
					player.sm(""+item.getName()+" cannot be exchanged.");
					return;
				}
				
			}
			long totalPrice = price * amount;
			if (totalPrice >= Integer.MAX_VALUE || totalPrice < 0) {
			player.sm("The price is too high!");
			return;
			}
			if (price < -1){
				player.sm("This item is not exchangable.");
				return;
			}
			
			if (totalPrice <= player.getInventory().getItems().getNumberOf(995)) {
				player.getInventory().deleteItem(995, (int) totalPrice);
				openGE(player);
				if (amount > player.getInventory().getFreeSlots() && !ItemDefinition.get(id).isStackable()){
					player.getInventory().addItem(id+1, amount);
				} else {
				player.getInventory().addItem(id, amount);
				}
				player.sm("One or more of your offers have been completed.");
			} else {
				player.sendMessage("You don't have enough coins!");
			}
		} else if(type == Type.SELL) {
			long totalPrice = price * amount;
			if (totalPrice >= Integer.MAX_VALUE || totalPrice < 0) {
				player.sm("The price is too high!");
				return;
				}
			if (amount <= player.getInventory().getItems().getNumberOf(id)) {
				player.getInventory().deleteItem(id, amount);
				openGE(player);
				player.getInventory().addItem(995, price*amount);
				player.sm("One or more of your offers have been completed.");
			} else {
				player.sendMessage("You don't have enough items to do that offer!");
			}
		} else {
			player.sendMessage("An error has occured.");
		}
	}

	public void sellItem(Player player, int id, int amount) {
		if(!canExchange(id)) {
			player.sendMessage(name(id) + " are not exchangeable!");
			return;
		}
		ItemDefinition def = ItemDefinition.get(id);
		if (def == null) {
		player.sm("item "+id+" is not added yet");
			return;
		}
		sendPriceConfigs(player, id);
		set(player, id, EconomyPrices.getPrice(def.getId()), amount);
	}

	/*
	 * Clicking the item on the Search Interface
	 */
	/*
	 * Clicking the item on the Search Interface
	 */
	public void buyItem(Player player, int idz) {
		try {
			ItemDefinition def = ItemDefinition.get(idz);
			if (def == null) {
				return;
			}
			switch (idz) {
				default:

					set(player, idz, ItemDefinition.get(def.getId()).getValue(def.getId()), 1);
			}
			sendPriceConfigs(player, idz);
			set(player, idz, ItemDefinition.get(def.getId()).getValue(def.getId()), 1);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public String name(int id) {
		ItemDefinition def = ItemDefinition.get(id);
		return def.getName();
	}

	public Offer findOffer(Player player, int box) {
		return player.offers[box];
	}

	public enum Type {
		BUY, SELL
	}
}
