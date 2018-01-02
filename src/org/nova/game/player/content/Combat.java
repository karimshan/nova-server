package org.nova.game.player.content;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.entity.Entity;
import org.nova.game.npc.NPC;
import org.nova.game.player.Player;
import org.nova.game.player.content.cities.ApeToll;
import org.nova.utility.misc.Misc;

public final class Combat {

    public static boolean hasAntiDragProtection(Entity target) {
	if (target instanceof NPC)
	    return false;
	Player p2 = (Player) target;
	int shieldId = p2.getEquipment().getShieldId();
	return shieldId == 1540 || shieldId == 11283 || shieldId == 11284;
    }
    public static boolean rollHit(double att, double def) {
    	if (att < 0) // wont happen unless low att lv plus negative bonus
    	    return false;
    	if (def < 0) // wont happen unless low def lv plus negative bonus
    	    return true;
    	return Misc.random((int) (att + def)) >= def;
        }
    

        public static int getDefenceEmote(Entity target) {
    	if (target instanceof NPC) {
    	    NPC n = (NPC) target;
    	    return n.getCombatDefinitions().getDefenceEmote();
    	} else {
    	    Player p = (Player) target;
    	    if (p.isTransformed())
    	    	ApeToll.untransformPlayer(p);
    	    int shieldId = p.getEquipment().getShieldId();
    	    String shieldName = shieldId == -1 ? null : ItemDefinition.get(shieldId).getName().toLowerCase();
    	    if (shieldId == -1 || (shieldName.contains("book") && shieldId != 18346)) {
    		int weaponId = p.getEquipment().getWeaponId();
    		if (weaponId == -1)
    		    return 424;
    		String weaponName = ItemDefinition.get(weaponId).getName().toLowerCase();
    		if (weaponName != null && !weaponName.equals("null")) {
    		    if (weaponName.contains("scimitar")
    		    		|| weaponName.contains("korasi sword")
    		    			|| weaponName.contains("assassin's blades"))
    			return 15074;
    		    if (weaponName.contains("whip"))
    			return 11974;
    		    if (weaponName.contains("staff of light"))
    			return 12806;
    		    if (weaponName.contains("longsword") || weaponName.contains("darklight") || weaponName.contains("silverlight") || weaponName.contains("excalibur"))
    			return 388;
    		    if (weaponName.contains("dagger") || weaponName.contains("the first blade"))
    			return 378;
    		    if (weaponName.contains("rapier"))
    			return 13038;
    		    if (weaponName.contains("pickaxe"))
    			return 397;
    		    if (weaponName.contains("mace") || weaponName.contains("annihilation"))
    			return 403;
    		    if (weaponName.contains("claws"))
    			return 404;
    		    if (weaponName.contains("hatchet"))
    			return 397;
    		    if (weaponName.contains("greataxe"))
    			return 12004;
    		    if (weaponName.contains("wand"))
    			return 415;
    		    if (weaponName.contains("chaotic staff"))
    			return 13046;
    		    if (weaponName.contains("staff") || weaponName.contentEquals("obliteration"))
    			return 420;
    		    if (weaponName.contains("warhammer") || weaponName.contains("tzhaar-ket-em"))
    			return 403;
    		    if (weaponName.contains("maul") || weaponName.contains("tzhaar-ket-om"))
    			return 1666;
    		    if (weaponName.contains("zamorakian spear"))
    			return 12008;
    		    if (weaponName.contains("spear") || weaponName.contains("halberd") || weaponName.contains("hasta"))
    			return 430;
    		    if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.equals("saradomin sword"))
    			return 7050;
    		}
    		return 424;
    	    }
    	    if (shieldName != null) {
    		if (shieldName.contains("shield"))
    		    return 1156;
    		if (shieldName.contains("defender"))
    		    return 4177;
    	    }
    	    switch (shieldId) {
    		case -1:
    		default:
    		    return 424;
    	    }
    	}
        }


        public static boolean isUndead(Entity target) {
    	if (target instanceof Player)
    	    return false;
    	NPC npc = (NPC) target;
    	String name = npc.defs().getName().toLowerCase();
    	return name.contains("aberrant spectre")
    		|| name.contains("zombie")
    		|| name.contains("ankou")
    		|| name.contains("crawling hand")
    		|| name.contains("ghost")
    		|| name.contains("ghast")
    		|| name.contains("mummy")
    		|| name.contains("revenant")
    		|| name.contains("shade")
    		|| npc.getId() == 8125
    		|| (npc.getId() >= 2044 && npc.getId() <= 2057)
    		|| name.contains("undead")
    		;
    	
        }
        
        
        private Combat() {
        }
        public static int getSlayerLevelForNPC(int id) {
    		switch (id) {
    		// crawling hands, 5 slayer required.
    		case 1648:
    			return 5;
    		case 1649:
    			return 5;
    		case 1650:
    			return 5;
    		case 1651:
    			return 5;
    		case 1652:
    			return 5;
    		case 1653:
    			return 5;
    		case 1654:
    			return 5;
    		case 1655:
    			return 5;
    		case 1656:
    			return 5;
    		case 1657:
    			return 5;
    		case 4226:
    			return 5;
    		case 106694:
    			return 5;
    		case 14202:
    			return 5;
    			
    		// cave bugs, 7 slayer required.
    		case 1832:
    			return 7;
    		case 5750: //this one is cb 96 and in dorgesh kaan, gotta get mapdata for it
    			return 7;
    		
    		//cave crawlers, 10 slayer required.
    		case 1600:
    			return 10;
    		case 1601:
    			return 10;
    		case 1602:
    			return 10;
    		case 1603:
    			return 10;
    		case 7787:
    			return 10;
    		case 7798: //monstrous cave crawler, prob from qbd no idea.
    			return 10;
    		case 10695: //high rev cave crawler, so from new content, qbd prob.
    			return 10;
    		
    		//banshee, 15 slayer required.
    		case 1612:
    			return 15;
    			
    		//cave slime, 17 slayer required.
    		case 1831:
    			return 17;
    		case 10696: //high rev npc, qbd possibly?
    			return 17;
    		
    		//rockslug, 20 slayer required.
    		case 1631:
    			return 20;
    		case 1632:
    			return 20;
    			
    		//desert lizards, 22 slayer required.
    		case 2804:
    			return 22;
    		case 2805:
    			return 22;
    		case 2806:
    			return 22;
    			
    		//cockatrice, 25 slayer required.
    		case 1620:
    			return 25;
    		case 4227:
    			return 25;
    			
    		//pyrefiend, 30 slayer required.µ
    		case 1633:
    			return 30;
    		case 1634:
    			return 30;
    		case 1635:
    			return 30;
    		case 1636:
    			return 30;
    		case 6216:
    			return 30;
    		case 8598:
    			return 30;
    		case 8616:
    			return 30;
    		case 10697: //soul wars pyrefiend.
    			return 30;
    			
    		//mogre, 32 slayer required.
    		case 114:
    			return 32;
    			
    		//harpie bug swarm, 33 slayer required.
    		case 3153:
    			return 33;
    			
    		//wall beast, 35 slayer required.
    		case 7823:
    			return 35;
    			
    		//killerwatt, 37 slayer required.
    		case 3201:
    			return 37;
    		case 3202:
    			return 37;
    			
    		//molanisk, 39 slayer required.
    		case 5751:
    			return 39;
    		
    		//basilisk, 40 slayer required.
    		case 1616:
    			return 40;
    		case 1617:
    			return 40;
    		case 4228:
    			return 40;
    		
    		//terror dog, 40 slayer required.
    		case 5417:
    			return 40;
    		case 5418:
    			return 40;
    		case 11365: //not sure where this is from.
    			return 40;
    		
    		//fever spider, 42 slayer required.
    		case 2850:
    			return 42;
    			
    		//infernal mage, 45 slayer required.
    		case 1643:
    			return 45;
    		case 1644:
    			return 45;
    		case 1645:
    			return 45;
    		case 1646:
    			return 45;
    		case 1647:
    			return 45;
    		
    		//brine rat, 47 slayer required.
    		case 3707:
    			return 47;
    			
    		//bloodveld, 50 slayer required.
    		case 1618:
    			return 50;
    		case 1619:
    			return 50;
    		case 6215:
    			return 50;
    			
    		//mutated bloodveld, 50 slayer required.
    		case 7642:
    			return 50;
    		case 7643:
    			return 50;
    			
    		//phoenix, 51 slayer required.
    		case 8548:
    			return 51;
    		case 8549:
    			return 51;
    		case 8575:
    			return 51;
    		case 8576:
    			return 51;
    		
    		//jelly, 52 slayer required.
    		case 1637:
    			return 52;
    		case 1638:
    			return 52;
    		case 1639:
    			return 52;
    		case 1640:
    			return 52;
    		case 1641:
    			return 52;
    		case 1642:
    			return 52;
    		case 7459:
    			return 52;
    		case 7460:
    			return 52;
    		case 8599:
    			return 52;
    		case 8617:
    			return 52;
    		case 10699: //from soul wars.
    			return 52;
    			
    		//turoth, 55 slayer required.
    		case 1623:
    			return 55;
    		case 1626:
    			return 55;
    		case 1627:
    			return 55;
    		case 1628:
    			return 55;
    		case 1629:
    			return 55;
    		case 1630:
    			return 55;
    			
    		//warped terrorbird, 56 slayer required.
    		case 6285:
    			return 56;
    		case 6286:
    			return 56;
    		case 6287:
    			return 56;
    		case 6288:
    			return 56;
    		case 6289:
    			return 56;
    		case 6290:
    			return 56;
    		case 6291:
    			return 56;
    		case 6292:
    			return 56;
    		case 6293:
    			return 56;
    		case 6294:
    			return 56;
    		case 6295:
    			return 56;
    		case 6322:
    			return 56;
    		case 6323:
    			return 56;
    		case 6324:
    			return 56;
    		case 6325:
    			return 56;
    		case 6326:
    			return 56;
    		case 6327:
    			return 56;
    		case 6328:
    			return 56;
    		case 6329:
    			return 56;
    		case 6330:
    			return 56;
    		case 6331:
    			return 56;
    		case 6332:
    			return 56;
    		case 6608:
    			return 56;
    			
    		//warped tortoise, 56 slayer required.
    		case 6296:
    			return 56;
    		case 6297:
    			return 56;
    			
    		//mutated zygnomite, 57 slayer required.
    		case 3346:
    			return 57;
    		case 3347:
    			return 57;
    			
    		//cave horror, 58 slayer required.
    		case 4353:
    			return 58;
    		case 4354:
    			return 58;
    		case 4355:
    			return 58;
    		case 4356:
    			return 58;
    		case 4357:
    			return 58;
    			
    		//aberrant spectre, 60 slayer required.
    		case 1604:
    			return 60;
    		case 1605:
    			return 60;
    		case 1606:
    			return 60;
    		case 1607:
    			return 60;
    		case 7801:
    			return 60;
    		case 7802:
    			return 60;
    		case 7803:
    			return 60;
    		case 7804:
    			return 60;
    			
    		//'Rum'-pumped crab, 61 slayer required.
    		case 13603:
    			return 61;
    		case 13604:
    			return 61;
    		case 13605:
    			return 61;
    		case 13606:
    			return 61;
    		case 13607:
    			return 61;
    			
    		//spiritual ranger, 63 slayer required.
    		case 6220:
    			return 63;
    		case 6230:
    			return 63;
    		case 6256:
    			return 63;
    		case 6276:
    			return 63;
    			
    		//dust devil, 65 slayer required.
    		case 1624:
    			return 65;
    			
    		//spiritual warrior, 68 slayer required.
    		case 6219:
    			return 68;
    		case 6229:
    			return 68;
    		case 6255:
    			return 68;
    		case 6277:
    			return 68;
    			
    		//kurask, 70 slayer required.
    		case 1608:
    			return 70;
    		case 1609:
    			return 70;
    		case 4229:
    			return 70;
    			
    		//skeletal wyvern, 72 slayer required.
    		case 3068:
    			return 72;
    		case 3069:
    			return 72;
    		case 3070:
    			return 72;
    		case 3071:
    			return 72;
    			
    		//Jungle strykewyrm, 73 slayer required.
    		case 9467:
    			return 73;
    			
    		//gargoyle, 75 slayer required.
    		case 1610:
    			return 75;
    		case 1827:
    			return 75;
    		case 6389:
    			return 75;
    		case 9087:
    			return 75;
    			
    		//Desert strykewyrm, 77 slayer required.
    		case 9465:
    			return 77;
    			
    		//nechryael, 80 slayer required.
    		case 1613:
    			return 80;
    		case 10702:
    			return 80;
    			
    		//aquanite, 80 slayer required.
    		case 9172:
    			return 80;
    		
    		//spiritual mage, 83 slayer required.
    		case 6221:
    			return 83;
    		case 6231:
    			return 83;
    		case 6257:
    			return 83;
    		case 6258:
    			return 83;
    			
    		//abyssal demon, 85 slayer required.
    		case 1615:
    			return 85;
    		case 4230:
    			return 85;
    		case 9086:
    			return 85;
    			
    		//dark beast, 90 slayer required.
    		case 2783:
    			return 90;
    			
    		//ice strykewyrm, 93 slayer required.		
    		case 9463:
    			return 93;
    			
    		//Ganodermic runt, 95 slayer required.
    		case 14698:
    			return 95;
    		case 14699:
    			return 95;
    			
    		//ganodermic beast, 95 slayer required.
    		case 14696:
    			return 95;
    		case 14697:
    			return 95;
    			
    		default:
    			return 0;
    		}
    	}
    	

    }
