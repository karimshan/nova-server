package org.nova.game.player.content.economy;

import java.util.ArrayList;
import java.util.List;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.Game;
import org.nova.game.item.Item;
import org.nova.game.player.Player;
import org.nova.utility.misc.Misc;


public class GrandExchange {

	public static List<Offer> offerList = new ArrayList<Offer>(2000);
		
	public Player p;
	 
	public int itemId = -1;
	
	public int minPrice = 0;
	public int basicPrice = 0;
	public int maxPrice = 0;
	
	public int itemPrice = 0;
	public int itemAmount = 0;

	public int offerType = -1;
	
	public static int i = -1;
	
	private boolean wasNoted;
	
	public GrandExchange(Player p) {
        this.p = p;
    }
	
	public void openMainInterface() {
		p.packets().sendConfig2(563, 4194304);
		p.packets().sendConfig(1112, -1);
		p.packets().sendConfig(1113, -1);
		p.packets().sendConfig(1109, -1);
		p.packets().sendConfig(1110, 0);
		p.packets().sendConfig(1111, 1);
		p.interfaces().sendInterface(105);
		p.packets().sendItems(-1, -1327, 93, new int[] {}, new int[] {});
		//p.getInterfaceManager().restoreTabs();
		p.getInventory().refresh();
		p.packets().setInterfaceConfig(107, 0, true);
		p.packets().setInterfaceConfig(105, 0, false);
		p.packets().sendAccessMask(6, 0, 105, -1, -1, basicPrice);
		//p.frames.setItems(p, -1, -1757, 523, new int[] {995}, new int[] {135});
		//p.frames.setItems(p, -1, -1758, 524, new int[] {995}, new int[] {1359});
		//p.frames.setItems(p, -1, -1759, 525, new int[] {995}, new int[] {13500});
		//p.frames.setItems(p, -1, -1760, 526, new int[] {995}, new int[] {135000});
		//p.frames.setItems(p, -1, -1761, 527, new int[] {995}, new int[] {1350000});
		//p.frames.setItems(p, -1, -1762, 528, new int[] {995}, new int[] {13500000});
	}

	public void handleButtons(int buttonId) {
		switch(buttonId) {
			case 30:
				setBuySetup(0);
				break;
			case 31:
				setSellSetup(0);
				break;
			case 46:
				setBuySetup(1);
				break;
			case 47:
				setSellSetup(1);
				break;
			case 62:
			    setBuySetup(2);
			    break;
			case 63:
			    setSellSetup(2);
			    break;
			case 81:
			    setBuySetup(3);
			    break;
			case 82:
			    setSellSetup(3);
			    break;
			case 100:
			    setBuySetup(4);
			    break;
			case 101:
			    setSellSetup(4);
			    break;
			case 119:
			    setBuySetup(5);
			    break;
			case 120:
			    setSellSetup(5);
			    break;
			case 127:
				openMainInterface();
				break;
			case 157:
			    if(itemAmount != 1) {
				itemAmount--;
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 159:
			    if(itemAmount < 2147483647) {
				itemAmount++;
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 162:
			    if(itemAmount < 2147483647) {
				itemAmount++;
			    }
			    if(offerType == 0) {
				itemAmount = 1;
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 164:
			    if(itemAmount < 2147483637) {
				itemAmount += 10;
			    }
			    if(offerType == 0) {
				itemAmount = 10;
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 166:
			    if(itemAmount < 2147483547) {
				itemAmount += 100;
			    }
			    if(offerType == 0) {
				itemAmount = 100;
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 168:
			    if(itemAmount < 2147482647) {
				itemAmount += 1000;
			    }
			    if(offerType == 0) {
				itemAmount = p.getInventory().numberOf(itemId);
			    }
			    p.packets().sendConfig(1109, itemId);	
			    p.packets().sendConfig(1110, itemAmount);
			    break;
			case 183:
				setItemPrice(getitemPrice(maxPrice));
			break;
			case 180:
				setItemPrice(getitemPrice(basicPrice));
			break;
			case 173:
				setItemPrice(getitemPrice(itemPrice + 1));
			break;
			case 171:
				setItemPrice(getitemPrice(itemPrice - 1));
			break;
			case 190:
				if (offerType == 0) {
					confirmSellOffer();
				} else {
					confirmBuyOffer();
				}
			break;
			case 18:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 0) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 0;
			    p.packets().sendConfig(1112, 0);
			    break;
			case 34:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 1) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 1;
			    p.packets().sendConfig(1112, 1);
			    break;
			case 50:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 2) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 2;
			    p.packets().sendConfig(1112, 2);
			    break;
			case 69:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 3) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 3;
			    p.packets().sendConfig(1112, 3);
			    break;
			case 88:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 4) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 4;
			    p.packets().sendConfig(1112, 4);
			    break;
			case 107:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != 5) {
				    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentPrice);
				} else if(!Offer.aborted) {
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentPrice);
				}
			    }
			    p.currentSlot = 5;
			    p.packets().sendConfig(1112, 5);
			    break;
			case 209:
			    boolean bool = false;
			    int i = 0;
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || Offer.slot != p.currentSlot) {
		    		    continue;
				}
				if(Offer.type == 0 && !Offer.aborted) {
				    p.packets().resetItemSlot(p, Offer.slot);
				    p.getInventory().addItem(995, Offer.currentPrice);
				    Offer.updatePrice(Offer.currentPrice, false);
				    i = Offer.slot;
				} else if(!Offer.aborted) {
				    p.packets().resetItemSlot(p, Offer.slot);
				    p.getInventory().addItem(Offer.item, Offer.currentPrice);
				    Offer.updatePrice(Offer.currentPrice, false);
				    i = Offer.slot;
				}
				if(Offer.aborted) {
				    i = Offer.slot;
				    bool = true;
				    if(Offer.type == 0) {
				    p.getInventory().addItem(Offer.item, Offer.currentAmount);
					p.packets().resetItemSlot(p, Offer.slot);
					bool = true;

				    } else {
					p.getInventory().addItem(995, Offer.currentAmount * Offer.price);
					p.packets().resetItemSlot(p, Offer.slot);
					bool = true;
				    }
				    Offer.clear();
				}
				if(Offer.completed) {
				    bool = true;
				    Offer.clear();
				}
			    }
			    if(bool) {
			        p.packets().resetGe(i);
			    }
			    break;
			case 203:
			    for(Offer Offer : offerList) {
				if(Offer == null || Offer.id != p.getIndex() || p.currentSlot != Offer.slot) {
		    		    continue;
				}
				Offer.aborted = true;
				if(Offer.type == 0) {
				    p.packets().setGe(Offer.slot, -3, Offer.item, Offer.price, Offer.amount, Offer.amount-Offer.currentAmount);
				    p.packets().setItemSlot(Offer.slot, Offer.item, Offer.currentAmount);
				} else {
				    p.packets().setGe(Offer.slot, 5, Offer.item, Offer.price, Offer.amount, Offer.amount-Offer.currentAmount);
				    p.packets().setItemSlot(Offer.slot, 995, Offer.currentAmount * Offer.price);//Offer.currentAmount
				}
			    }
			    break;
		}
	}
	
	public void setSellSetup(int offerId) {
		itemId = -1;
		minPrice = 0;
		basicPrice = 0;
		maxPrice = 0;
		itemPrice = 0;
		itemAmount = 0;
		p.currentSlot = offerId;
		p.packets().sendConfig(1109, -1);
		p.packets().sendConfig(1110, 0);	
		p.packets().sendConfig(1111, 0);	
		p.packets().sendConfig(1112, -1);	
		p.packets().sendConfig(1113, 1);
		p.packets().sendConfig(1112, offerId);
		p.interfaces().sendInventoryInterface(107);
		Object[] o2 = new Object[]{"", "", "", "", "Offer", -1, 0, 7, 4, 93, 7012370};
		p.packets().sendRunScript(149, o2, "IviiiIsssss");
		p.packets().sendAccessMask(1026, 18, 107, 0, 27);
	//	p.getPackets().sendItems(p, -1, -1327, 93, p.items, p.itemsN);
		p.packets().setInterfaceConfig(107, 0, false);
		offerType = 0;
	}
	
	public void setBuySetup(int offerId) {
		itemId = -1;
		minPrice = 0;
		basicPrice = 0;
		maxPrice = 0;
		itemPrice = 0;
		itemAmount = 0;
		p.currentSlot = offerId;
		p.packets().sendConfig(1109, -1);
		p.packets().sendConfig(1110, 0);	
		p.packets().sendConfig(1111, 0);
		p.packets().sendConfig(1113, 1);
		p.packets().sendConfig(1113, 0);
		p.packets().sendConfig(1112, -1);
		p.packets().sendConfig(1112, offerId);
		offerType = 1;
	//	Object[] o = new Object[]{"Grand Exchange Item Search"};
		//p.getPackets().setGeSearch(o);
	}
	
	public void offerItem(int itemIndex) {
		Item item = p.getInventory().getItems().get(itemIndex);
		wasNoted = item.defs().isNoted() ? true : false;
		item = new Item(
		itemId = item.defs().isNoted() ? item.getId() - 1 : item.getId(), item.getAmount());
		itemId = item.getId();
		itemAmount = item.getAmount();
		p.packets().sendConfig(1109, itemId);	
		p.packets().sendConfig(1110, itemAmount);
		setPrices(item.defs());
	}
	
	public void setBuyItem(int itemId) {
		this.itemId = itemId;
		itemAmount = 1;
		p.packets().sendConfig(1109, itemId);	
		p.packets().sendConfig(1110, itemAmount);
		setPrices(ItemDefinition.get(itemId));
	}
	
	public void setItemPrice(int price) {
		p.packets().sendConfig(1111, price);
		itemPrice = price;
	}
	
	public void setPrices(ItemDefinition j) {
		minPrice = 0;
		basicPrice = 0;
		maxPrice = 0;
		System.out.println("low: " + minPrice + "; mid: " + basicPrice + "; max: " + maxPrice);
		p.packets().sendConfig(1115, minPrice);
		p.packets().sendConfig(1114, itemPrice);
		p.packets().sendConfig(1116, maxPrice);
		setItemPrice(itemPrice);
	}
	
	public int getitemPrice(int price) {
		if (price >= maxPrice) {
			return maxPrice;
		}
		if (price <= minPrice) {
			return minPrice;
		}
		return price;
	}
	
	public void confirmSellOffer() {
		if (itemAmount <= p.getInventory().numberOf(itemId)) {
			offerList.add(new Offer(p.getIndex(), itemId, itemAmount, itemAmount, itemPrice, 0, p.getUsername(), false, p.currentSlot));
			openMainInterface();
			p.getInventory().deleteItem(itemId, itemAmount);
			p.packets().setGe(p.currentSlot, -1, itemId, itemPrice, itemAmount, 0);
			updateOffers();
		} else {
			p.sendMessage("You don't have enough items in your inventory to complete this offer.");
		}
	}
	
	public void confirmBuyOffer() {
		if ((itemAmount * itemPrice) <= p.getInventory().numberOf(995)) {
			offerList.add(new Offer(p.getIndex(), itemId, itemAmount, itemAmount, itemPrice, 1, p.getUsername(), false, p.currentSlot));
			openMainInterface();
			p.getInventory().deleteItem(995, (itemAmount * itemPrice));
			p.packets().setGe(p.currentSlot, 3, itemId, itemPrice, itemAmount, 0);
			updateOffers();
		} else {
			p.sendMessage("You don't have enough coins to complete this offer.");
		}
	}
	
	@SuppressWarnings("null")
	public void updateOffers() {
	    for(Offer Offer : offerList) {
		if(Offer == null || Offer.type != 0 || Offer.completed || Offer.aborted) {
		    continue;
		}
		for(Offer Offer2 : offerList) {
		    if(Offer2 == null || Offer2.type != 1 || Offer2.completed || Offer2.aborted) {
			continue;
		    }
		    if(Offer2.item == Offer.item && Offer2.price >= Offer.price) {
		    Player p2 = Game.getPlayers().get(Misc.getIdFromName(Offer2.owner));
			Player p3 = Game.getPlayers().get(Misc.getIdFromName(Offer.owner));
			int amount = Offer2.currentAmount;
			if(Offer.currentAmount < Offer2.currentAmount) {
				amount = Offer.currentAmount;
			}
			if(p2 != null || p2.isOnline) {
				p2.sendMessage("Theres been a update on one of your GE offers!");
			}
			if(p3 != null || p3.isOnline) {
			    p3.sendMessage("Theres been a update on one of your GE offers!");
			}
			Offer2.currentAmount -= amount;
			Offer.currentAmount -= amount;
			Offer.updatePrice(amount, true);
			Offer2.updatePrice(amount, true);
			p3.packets().setGe(Offer.slot, -1, Offer.item, Offer.price, Offer.amount, Offer.amount-Offer.currentAmount);
			p2.packets().setGe(Offer2.slot, 3, Offer2.item, Offer2.price, Offer2.amount, Offer2.amount-Offer2.currentAmount);
			if(Offer.currentAmount == 0) {
			    p3.packets().setGe(Offer.slot, -3, Offer.item, Offer.price, Offer.amount, Offer.amount-Offer.currentAmount);
			    Offer.completed = true;
			}
			if(Offer2.currentAmount == 0) {
			    p2.packets().setGe(Offer2.slot, 5, Offer2.item, Offer2.price, Offer2.amount, Offer2.amount-Offer2.currentAmount);
			    Offer2.completed = true;
			}
		    }
		}
	    }
	}
}