package org.nova.game.player.content;

import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.masks.Animation;
import org.nova.game.masks.Graphics;
import org.nova.game.player.Player;
public final class FairyRing {

	/*
	 * Author InflictionRB
	 */
	
    public static final int[] UNUSED_COMPONENTS = { 14, 16, 17, 18, 21, 22, 24, 28 };


    public static void openFairyRing(Player player) {
        player.interfaces().sendInterface(734);
        player.interfaces().sendInventoryInterface(735);
        player.packets().sendIComponentText(735, 15, "                                             Asgarnia: Mudskipper Point");
        player.packets().sendIComponentText(735, 19, "                                             Dungeons: Dark cave south of Dorgesh-Kaan");
        player.packets().sendIComponentText(735, 20, "                                             Kandarin: Slayer cave south-east of Rellekka");
        player.packets().sendIComponentText(735, 23, "                                             Kandarin: Piscatoris Hunter area");
        player.packets().sendIComponentText(735, 25, "                                             Feldip Hills: Feldip Hunter area");
        player.packets().sendIComponentText(735, 26, "                                             Kandarin: Feldip Hills");
        player.packets().sendIComponentText(735, 27, "                                             Morytania: Haunted Woods east of Canifis");
        player.packets().sendIComponentText(735, 29, "                                             Kandarin: Mcgrubor's Woods");
        player.packets().sendIComponentText(735, 30, "                                             Islands: Polypore Dungeon");
        player.packets().sendIComponentText(735, 31, "                                             Kharidian Desert: Near Kalphite hive");

    }


    public static void ringTele(Player player, int firstColumn, int secondColumn, int thirdColumn) {
        final int a = 1;
        final int i = 1;
        final int p = 1;
        final int d = 2;
        final int l = 2;
        final int s = 2;
        final int c = 3;
        final int k = 3;
        final int r = 3;
        final int b = 4;
        final int j = 4;
        final int q = 4;
        if (player.getTemporaryAttributtes().get("TeleBlocked") != null) {
			player.packets().sendMessage("You cannot use the fairyring when teleblocked!");
                    return;
            }


        /**
         * START OF SEQUENCES
         */

        if (player.firstColumn == a && player.secondColumn == j && player.thirdColumn == q) { //dorgesh-kaan
        	player.closeInterfaces();
        	player.interfaces().sendInterface(578);
       
        } else if (player.firstColumn == d && player.secondColumn == k && player.thirdColumn == r) { //edgeville
            player.closeInterfaces();
            fairyTeleport(player, 3129, 3496, 0);
       
        } else if (player.firstColumn == a && player.secondColumn == i && player.thirdColumn == q) { //mudskipper point
            player.closeInterfaces();
            fairyTeleport(player, 2996, 3114, 0);
     
        } else if (player.firstColumn == a && player.secondColumn == j && player.thirdColumn == r) { //slayer cave south-east of rellekka
            player.closeInterfaces();
            fairyTeleport(player, 2780, 3613, 0);
           
        } else if (player.firstColumn == a && player.secondColumn == k && player.thirdColumn == q) { //Piscatoris Hunter Area
            player.closeInterfaces();
            fairyTeleport(player, 2319, 3619, 0);
            
        } else if (player.firstColumn == a && player.secondColumn == k && player.thirdColumn == s) { //Feldip Hills Jungle Area
            player.closeInterfaces();
            fairyTeleport(player, 2571, 2956, 0);
            
        } else if (player.firstColumn == a && player.secondColumn == l && player.thirdColumn == p) { //Feldip Hills near Gu'Tanoth
            player.closeInterfaces();
            fairyTeleport(player, 2385, 3035, 0);
            
        } else if (player.firstColumn == a && player.secondColumn == l && player.thirdColumn == q) { //Haunted Woods
            player.closeInterfaces();
            fairyTeleport(player, 3597, 3495, 0);
            
        } else if (player.firstColumn == a && player.secondColumn == l && player.thirdColumn == s) { //McGrubor's Wood
            player.closeInterfaces();
            fairyTeleport(player, 2644, 3495, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == i && player.thirdColumn == p) { //Polypore Dungeon
            player.closeInterfaces();
            fairyTeleport(player, 3410, 3324, 0);
          
        } else if (player.firstColumn == b && player.secondColumn == i && player.thirdColumn == q) { //Kalphite hive
            player.closeInterfaces();
            fairyTeleport(player, 3251, 3095, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == j && player.thirdColumn == q) { //Ancient Caverns
            player.closeInterfaces();
            fairyTeleport(player, 1737, 5342, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == k && player.thirdColumn == p) { //Feldip hills south of castle wars
            player.closeInterfaces();
            fairyTeleport(player, 2385, 3035, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == k && player.thirdColumn == r) { //Mort Myre, south of Canifis
            player.closeInterfaces();
            fairyTeleport(player, 3469, 3431, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == l && player.thirdColumn == p) { //Tzhaar Area
            player.closeInterfaces();
            fairyTeleport(player, 4622, 5147, 0);
            
        } else if (player.firstColumn == b && player.secondColumn == l && player.thirdColumn == r) { //Legends' Guild
            player.closeInterfaces();
            fairyTeleport(player, 2740, 3351, 0);
            
        } else if (player.firstColumn == c && player.secondColumn == i && player.thirdColumn == p) { //Miscellania
            player.closeInterfaces();
            fairyTeleport(player, 2513, 3884, 0);
            
        } else if (player.firstColumn == c && player.secondColumn == i && player.thirdColumn == q) { //North-west of Yanille
            player.closeInterfaces();
            fairyTeleport(player, 2528, 3127, 0);
            
			} else if (player.firstColumn == c && player.secondColumn == j && player.thirdColumn == r) { //Kandarin: Sinclair Mansion (east) 
            player.closeInterfaces();
            fairyTeleport(player, 2705, 3576, 0);
                        
			} else if (player.firstColumn == c && player.secondColumn == k && player.thirdColumn == r) { //karamja near tai bui wani or w/e its claled 
            player.closeInterfaces();
            fairyTeleport(player, 2801, 3003, 0);
			                        
			} else if (player.firstColumn == c && player.secondColumn == k && player.thirdColumn == s) { //Canifis
            player.closeInterfaces();
            fairyTeleport(player, 3447, 3470, 0);
			                        
			} else if (player.firstColumn == c && player.secondColumn == l && player.thirdColumn == r) { //ape atoll
            player.closeInterfaces();
            fairyTeleport(player, 2735, 2742, 0);
						                        
			} else if (player.firstColumn == c && player.secondColumn == l && player.thirdColumn == s) { //east yanille island
            player.closeInterfaces();
            fairyTeleport(player, 2682, 3081, 0);
									                        
			} else if (player.firstColumn == d && player.secondColumn == i && player.thirdColumn == p) { //South Mos Le'Harmless island
            player.closeInterfaces();
            fairyTeleport(player, 3763, 2930, 0);
									                        
			} else if (player.firstColumn == d && player.secondColumn == i && player.thirdColumn == s) { //Wizard Tower
            player.closeInterfaces();
            fairyTeleport(player, 3108, 3149, 0);
									                        
			} else if (player.firstColumn == d && player.secondColumn == j && player.thirdColumn == p) { //Necromancer
            player.closeInterfaces();
            fairyTeleport(player, 2658, 3230, 0);
									                        
			} else if (player.firstColumn == d && player.secondColumn == j && player.thirdColumn == r) { //sinclair mansion
            player.closeInterfaces();
            fairyTeleport(player, 2676, 3587, 0);
												                        
			} else if (player.firstColumn == d && player.secondColumn == k && player.thirdColumn == p) { //south musa point
            player.closeInterfaces();
            fairyTeleport(player, 2900, 3111, 0);
												                        
			} else if (player.firstColumn == d && player.secondColumn == k && player.thirdColumn == q) { //Glacors
            player.closeInterfaces();
            fairyTeleport(player, 4183, 5726, 0);
															                        
			} else if (player.firstColumn == d && player.secondColumn == k && player.thirdColumn == r) { //East of edgeville
            player.closeInterfaces();
            fairyTeleport(player, 3129, 3496, 0);
																		                        
			} else if (player.firstColumn == d && player.secondColumn == k && player.thirdColumn == s) { //Keldagrim
            player.closeInterfaces();
            fairyTeleport(player, 2744, 3719, 0);
																		                        
			} else if (player.firstColumn == d && player.secondColumn == l && player.thirdColumn == q) { //pollinavich
            player.closeInterfaces();
            fairyTeleport(player, 3423, 3016, 0);
			
        } else {
            player.closeInterfaces();
            fairyTeleport(player, 2412, 4434, 0);
			player.sm("You've entered an invalid code!");
            //player.sm("The code you've entered doesn't exist, you have been transported to the main fairy ring!");
            return;
        }
    }


    

    public static void MainFairyRing(Player player) {
        fairyTeleport(player, 2412, 4434, 0);
    }


    public static void warningInterface(Player player) {
        player.closeInterfaces();
        fairyTeleport(player, 2735, 5221, 0);
    }

   
    private static void fairyTeleport(final Player player, final int x, final int y, final int z) {
    		player.lock(3);
    	//player.setNextGraphics(new Graphics(569));
    		player.setNextAnimation(new Animation(3254));
    		player.setNextGraphics(new Graphics(2670));
            WorldTasksManager.schedule(new WorldTask() {
            	@Override
    			public void run() {
            		player.lock(3);
            		player.setNextGraphics(new Graphics(2671));
            		player.setNextAnimation(new Animation(3255));
            		player.setLocation(new Location(x, y, z));
    				refresh(player);
    				player.closeInterfaces();
                    player.stopAll();
    			}	

    		}, 3);
    	}
				
    
public static void refresh(Player player) {                
    player.firstColumn = 1;
    player.secondColumn = 1;
    player.thirdColumn = 1;         
	}
}