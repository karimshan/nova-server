package org.nova.utility.loading.npcs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.nova.game.Game;
import org.nova.game.map.GlobalObject;
import org.nova.game.map.Location;
import org.nova.game.npc.NPC;
import org.nova.utility.FileUtilities;
import org.nova.utility.misc.Misc.EntityDirection;

public class NPCSpawning {
	
	private static boolean spawned;

	/**
	 * Contains the custom npc spawning
	 */

	public static void spawnNPCS() {
		if(spawned)
			return;
		Game.spawnFacingNPC(6537, new Location(3090,3494,0), 0, true, EntityDirection.WEST);
		
		Game.spawnFacingNPC(2253, new Location(3086,3504,0), 0, true, EntityDirection.SOUTH);
		
		Game.spawnFacingNPC(2208, new Location(2892,2728,0), 0, true, EntityDirection.NORTH);
		
		//Edgeville.
		Game.spawnFacingNPC(456, new Location(3094,3505,0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(741, new Location(3094,3479,0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(11294, new Location(3090,3504,0), 0, true, EntityDirection.SOUTH);
		Game.spawnNPC(14012, new Location(3085,3486,0), -1, false, false);	//	frosts
	
		/**
		 * GE NPCS
		 */
		Game.spawnFacingNPC(1419, new Location(3179, 3479, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(3418, new Location(3179, 3478, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(1419, new Location(3180, 3477, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2718, new Location(3181, 3477, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2241, new Location(3182, 3478, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(3418, new Location(3182, 3479, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(2593, new Location(3181, 3480, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(3418, new Location(3180, 3480, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(1419, new Location(3148, 3477, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2718, new Location(3149, 3477, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2241, new Location(3150, 3478, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(3418, new Location(3150, 3479, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(2593, new Location(3149, 3480, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(3418, new Location(3148, 3480, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(1419, new Location(3147, 3479, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(3418, new Location(3147, 3478, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(1419, new Location(3148, 3503, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2718, new Location(3149, 3503, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2241, new Location(3150, 3404, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(3418, new Location(3150, 3405, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(2593, new Location(3149, 3506, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(3418, new Location(3148, 3506, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(1419, new Location(3147, 3505, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(3418, new Location(3147, 3504, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(1419, new Location(3180, 3503, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2718, new Location(3181, 3503, 0), 0, true, EntityDirection.SOUTH);
		Game.spawnFacingNPC(2241, new Location(3182, 3504, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(3418, new Location(3182, 3505, 0), 0, true, EntityDirection.EAST);
		Game.spawnFacingNPC(2593, new Location(3181, 3506, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(3418, new Location(3180, 3506, 0), 0, true, EntityDirection.NORTH);
		Game.spawnFacingNPC(1419, new Location(3179, 3505, 0), 0, true, EntityDirection.WEST);
		Game.spawnFacingNPC(3418, new Location(3179, 3504, 0), 0, true, EntityDirection.WEST);
		
		
		
		Game.spawnNPC(8850, new Location(3680, 4944, 0), -1, true, true);	//	frosts
		Game.spawnNPC(9703, new Location(3183, 3499, 0), -1, true, true);	//	sea slut
		Game.spawnNPC(1821, new Location(2484, 3488, 1), -1, false, false);
		
		/**
		 * NPCS
		 *//* --------------------- Frost Dragons dungeon --------------------- */
		Game.spawnNPC(51, new Location(1311, 4520, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1312, 4529, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1302, 4530, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1320, 4533, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1329, 4529, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1333, 4521, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1325, 4519, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1318, 4518, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1312, 4499, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1320, 4496, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1323, 4495, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1318, 4490, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1312, 4490, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1305, 4489, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1299, 4488, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1291, 4488, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1293, 4494, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1301, 4496, 0), -1, true, true);	//	frosts
		Game.spawnNPC(51, new Location(1309, 4497, 0), -1, true, true);	//	frosts
		//Avalani
		Game.spawnNPC(13823, new Location(2718, 5210, 3), -1, true, true);
		Game.spawnNPC(13824, new Location(2723, 5194, 3), -1, true, true);
		Game.spawnNPC(13827, new Location(2701, 5201, 3), -1, true, true);
			Game.spawnNPC(7939, new Location(2445, 4956, 0), -1, true, true);
		/*Slayer Tower*/
			/*Crawling Hands*/
			Game.spawnNPC(1648, new Location(3423, 3542, 0), -1, true, true);
			Game.spawnNPC(1650, new Location(3420, 3546, 0), -1, true, true);
			Game.spawnNPC(1649, new Location(3421, 3541, 0), -1, true, true);
			Game.spawnNPC(1653, new Location(3411, 3549, 0), -1, true, true);
			Game.spawnNPC(1655, new Location(3427, 3549, 0), -1, true, true);
			Game.spawnNPC(1651, new Location(3426, 3555, 0), -1, true, true);
			Game.spawnNPC(1652, new Location(3423, 3559, 0), -1, true, true);
			Game.spawnNPC(1657, new Location(3418, 3556, 0), -1, true, true);
			Game.spawnNPC(1655, new Location(3413, 3558, 0), -1, true, true);
			Game.spawnNPC(1648, new Location(3413, 3564, 0), -1, true, true);
			Game.spawnNPC(1650, new Location(3414, 3574, 0), -1, true, true);
			Game.spawnNPC(1650, new Location(3415, 3572, 0), -1, true, true);
			Game.spawnNPC(1657, new Location(3422, 3574, 0), -1, true, true);
			Game.spawnNPC(1657, new Location(3418, 3571, 0), -1, true, true);
			Game.spawnNPC(1657, new Location(3415, 3559, 0), -1, true, true);
			/*Banshees*/
			Game.spawnNPC(1612, new Location(3446, 3537, 0), -1, true, true);
			Game.spawnNPC(1612, new Location(3440, 3538, 0), -1, true, true);
			Game.spawnNPC(1612, new Location(3433, 3551, 0), -1, true, true);
			Game.spawnNPC(1612, new Location(3438, 3561, 0), -1, true, true);
			Game.spawnNPC(1612, new Location(3446, 3562, 0), -1, true, true);
			Game.spawnNPC(1612, new Location(3439, 3572, 0), -1, true, true);
			/*Infernall Mages*/
			Game.spawnNPC(1643, new Location(3447, 3571, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3443, 3574, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3440, 3568, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3434, 3571, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3440, 3557, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3435, 3559, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3438, 3563, 1), -1, true, true);
			Game.spawnNPC(1643, new Location(3433, 3562, 1), -1, true, true);
			/*BloodVelds*/
			Game.spawnNPC(1618, new Location(3422, 3557, 1), -1, true, true);
			Game.spawnNPC(1618, new Location(3425, 3566, 1), -1, true, true);
			Game.spawnNPC(1618, new Location(3418, 3565, 1), -1, true, true);
			Game.spawnNPC(1618, new Location(3412, 3560, 1), -1, true, true);
			Game.spawnNPC(1618, new Location(3409, 3572, 1), -1, true, true);
			Game.spawnNPC(1618, new Location(3413, 3573, 1), -1, true, true);
			/*Aberrant Spectre*/
			Game.spawnNPC(1604, new Location(3410, 3535, 1), -1, true, true);
			Game.spawnNPC(1607, new Location(3429, 3539, 1), -1, true, true);
			Game.spawnNPC(1604, new Location(3423, 3541, 1), -1, true, true);
			Game.spawnNPC(1605, new Location(3417, 3545, 1), -1, true, true);
			Game.spawnNPC(1605, new Location(3412, 3549, 1), -1, true, true);
			Game.spawnNPC(1605, new Location(3442, 3545, 1), -1, true, true);
			Game.spawnNPC(1607, new Location(3434, 3550, 1), -1, true, true);
			Game.spawnNPC(1604, new Location(3427, 3551, 1), -1, true, true);
			/*Gargoyles*/
			Game.spawnNPC(1610, new Location(3434, 3550, 2), -1, true, true);
			Game.spawnNPC(1610, new Location(3442, 3547, 2), -1, true, true);
			Game.spawnNPC(1610, new Location(3448, 3537, 2), -1, true, true);
			Game.spawnNPC(1610, new Location(3441, 3540, 2), -1, true, true);
			Game.spawnNPC(1610, new Location(3435, 3539, 2), -1, true, true);
			/*Nechreyal*/
			Game.spawnNPC(1613, new Location(3447, 3573, 2), -1, true, true);
			Game.spawnNPC(1613, new Location(3441, 3571, 2), -1, true, true);
			Game.spawnNPC(1613, new Location(3438, 3566, 2), -1, true, true);
			Game.spawnNPC(1613, new Location(3434, 3572, 2), -1, true, true);
			Game.spawnNPC(1613, new Location(3436, 3559, 2), -1, true, true);
			Game.spawnNPC(1613, new Location(3445, 3561, 2), -1, true, true);
			/*Abby Demons*/
			Game.spawnNPC(1615, new Location(3411, 3570, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3410, 3576, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3427, 3565, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3427, 3572, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3421, 3571, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3417, 3566, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3413, 3562, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3417, 3558, 2), -1, true, true);
			Game.spawnNPC(1615, new Location(3415, 3551, 2), -1, true, true);
			/*Dark Beast*/
			Game.spawnNPC(2783, new Location(3411, 3571, 2), -1, true, true);
			Game.spawnNPC(2783, new Location(3420, 3566, 2), -1, true, true);
			Game.spawnNPC(2783, new Location(3415, 3558, 2), -1, true, true);
			
			
			/*IceFiends*/
			Game.spawnNPC(3406, new Location(2721, 10214, 0), -1, true, true);
			Game.spawnNPC(3406, new Location(2715, 10219, 0), -1, true, true);
			Game.spawnNPC(3406, new Location(2730, 10205, 0), -1, true, true);
			Game.spawnNPC(3406, new Location(2712, 10205, 0), -1, true, true);
			
			
			
			
			
			
		
		Game.spawnNPC(14923, new Location(5887, 4703, 0), -2, true, true);
		Game.spawnNPC(2593, new Location(5889, 4702, 0), -3, true, true);
		Game.spawnNPC(2593, new Location(5886, 4701, 0), -4, true, true);
		/*Hunter*/
			/*Chinchompa Carnivorus*/
		Game.spawnNPC(5080, new Location(2574, 2906, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2576, 2903, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2574, 2908, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2572, 2908, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2571, 2901, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2572, 2909, 0), -1, true, true);
			
			/*Wagtails*/
		Game.spawnNPC(5072, new Location(2595, 2911, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2597, 2912, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2593, 2914, 0), -1, true, true);
			
			/*Swifts*/
		Game.spawnNPC(5073, new Location(2607, 2925, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2602, 2918, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2609, 2920, 0), -1, true, true);
			
			/*Warbler*/
		Game.spawnNPC(5075, new Location(2592, 2887, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2594, 2883, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2597, 2887, 0), -1, true, true);
			
			/*Longtail*/
		Game.spawnNPC(5076, new Location(2604, 2896, 0), -1, true, true);
		Game.spawnNPC(5076, new Location(2607, 2893, 0), -1, true, true);
		
		
		
		
		Game.spawnNPC(7133, new Location(3100, 5537, 0), -1, true, true);
		Game.spawnNPC(659, new Location(5882, 4712, 0), -1, true, true);
		Game.spawnNPC(2745, new Location(2405, 5085, 0), -1, true, true);
		Game.spawnNPC(14078, new Location(5892, 4710, 0), -1, true, true);
		Game.spawnNPC(1158, new Location(3484, 9493, 0), -1, true, true);
		//World.spawnNPC(14057, new Location(3164, 3479, 0), -1, true, true);
		
		
		
		/*Jadinko's*/
		Game.spawnNPC(13816, new Location(3059, 9246, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3044, 9234, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3028, 9235, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3033, 9237, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3039, 9235, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3045, 9234, 0), -1, true, true);
		Game.spawnNPC(13816, new Location(3026, 9232, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3039, 9244, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3022, 9257, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3015, 9260, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3039, 9244, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3046, 9270, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3043, 9260, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3043, 9260, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3063, 9236, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3056, 9250, 0), -1, true, true);
		
		Game.spawnNPC(13818, new Location(3019, 9255, 0), -1, true, true);
		Game.spawnNPC(13818, new Location(3046, 9265, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3044, 9229, 0), -1, true, true);
		Game.spawnNPC(13819, new Location(3059, 9242, 0), -1, true, true);
		/*Mutated Jadinko's*/
			/*Babies*/
		Game.spawnNPC(13820, new Location(3060, 9238, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3060, 9247, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3036, 9231, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3030, 9236, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3020, 9234, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3040, 9262, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3045, 9270, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3022, 9253, 0), -1, true, true);
		Game.spawnNPC(13820, new Location(3015, 9259, 0), -1, true, true);
		
			/*Guards*/
		Game.spawnNPC(13821, new Location(3065, 9243, 0), -1, true, true);
		Game.spawnNPC(13821, new Location(3034, 9235, 0), -1, true, true);
			
			/*Males*/
		Game.spawnNPC(13822, new Location(3043, 9239, 0), -1, true, true);
		Game.spawnNPC(13822, new Location(3043, 9265, 0), -1, true, true);
	
		Game.spawnNPC(6222, new Location(2828, 5302, 2), -1, true, true);
		Game.spawnNPC(6203, new Location(2934, 5324, 2), -1, true, true);
		Game.spawnNPC(1265, new Location(2700, 3715, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2696, 3719, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2706, 3724, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2711, 3719, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2717, 3726, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2721, 3717, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2721, 3706, 0), -1, true, true);
		Game.spawnNPC(1265, new Location(2716, 3700, 0), -1, true, true);
		//End Crabs start abyss
		Game.spawnNPC(1615, new Location(3029, 4842, 0), -1, true, true);
		Game.spawnNPC(3200, new Location(3026, 4829, 0), -1, true, true);
		Game.spawnNPC(1615, new Location(3033, 4821, 0), -1, true, true);
		Game.spawnNPC(3200, new Location(3047, 4822, 0), -1, true, true);
		Game.spawnNPC(1615, new Location(3051, 4837, 0), -1, true, true);
		//Start thieving npcs and home wolfs
		Game.spawnNPC(1, new Location(3490, 3491, 0), -1, true, true);
		Game.spawnNPC(2, new Location(3503, 3488, 0), -1, true, true);
		Game.spawnNPC(4, new Location(3490, 3483, 0), -1, true, true);
		Game.spawnNPC(5, new Location(3507, 3503, 0), -1, true, true);
		Game.spawnNPC(7, new Location(3472, 3499, 0), -1, true, true);
		Game.spawnNPC(15, new Location(3482, 3476, 0), -1, true, true);
		Game.spawnNPC(1715, new Location(3479, 3496, 1), -1, true, true);
		Game.spawnNPC(1714, new Location(3497, 3474, 1), -1, true, true);
		//Start of fishing
		Game.spawnNPC(327, new Location(2478, 5128, 0), -1, true, true);
		Game.spawnNPC(328, new Location(2484, 5134, 0), -1, true, true);
		Game.spawnNPC(312, new Location(2488, 5137, 0), -1, true, true);
		Game.spawnNPC(6267, new Location(2492, 5128, 0), -1, true, true);
		//Start dragons
		Game.spawnNPC(51, new Location(2914, 9807, 0), -1, true, true);
		Game.spawnNPC(52, new Location(2914, 9798, 0), -1, true, true);
		Game.spawnNPC(53, new Location(2910, 9801, 0), -1, true, true);
		Game.spawnNPC(54, new Location(2907, 9806, 0), -1, true, true);
		Game.spawnNPC(55, new Location(2902, 9803, 0), -1, true, true);
		//Start Dags
		Game.spawnNPC(2881, new Location(2423, 4697, 0), -1, true, true);
		Game.spawnNPC(2882, new Location(2425, 4709, 0), -1, true, true);
		Game.spawnNPC(2883, new Location(2422, 4721, 0), -1, true, true);
		//Start STQ
		Game.spawnNPC(3847, new Location(2323, 4593, 0), -1, true, true);
		//Start Snails
		Game.spawnNPC(1227, new Location(2861, 9732, 0), -1, true, true);
		Game.spawnNPC(1228, new Location(2857, 9733, 0), -1, true, true);
		Game.spawnNPC(1229, new Location(2858, 9738, 0), -1, true, true);
		Game.spawnNPC(1230, new Location(2861, 9738, 0), -1, true, true);
		Game.spawnNPC(1231, new Location(2866, 9736, 0), -1, true, true);
		Game.spawnNPC(1232, new Location(2862, 9734, 0), -1, true, true);
		Game.spawnNPC(1233, new Location(2860, 9733, 0), -1, true, true);
		Game.spawnNPC(1234, new Location(2859, 9736, 0), -1, true, true);
		Game.spawnNPC(1235, new Location(2860, 9740, 0), -1, true, true);
		//Hunter
		Game.spawnNPC(5080, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5081, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(6916, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(7272, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(6942, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(1192, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5074, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(7031, new Location(2715, 9203, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5081, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(6916, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(7272, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(6942, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(1192, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5074, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(7031, new Location(2732, 9186, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5081, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(6916, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(7272, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(6942, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(1192, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5074, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(7031, new Location(2714, 9170, 0), -1, true, true);
		Game.spawnNPC(5080, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(5081, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(6916, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(7272, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(6942, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(1192, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(5073, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(5075, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(5074, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(5072, new Location(2699, 9186, 0), -1, true, true);
		Game.spawnNPC(7031, new Location(2699, 9186, 0), -1, true, true);
		//Ardy thieving stall area and guards and scorps
		Game.spawnNPC(21, new Location(2664, 3310, 0), -1, true, true);
		Game.spawnNPC(20, new Location(2664, 3310, 0), -1, true, true);
		Game.spawnNPC(23, new Location(2664, 3310, 0), -1, true, true);
		Game.spawnNPC(21, new Location(2664, 3301, 0), -1, true, true);
		Game.spawnNPC(20, new Location(2664, 3301, 0), -1, true, true);
		Game.spawnNPC(23, new Location(2664, 3301, 0), -1, true, true);
		Game.spawnNPC(21, new Location(2659, 3306, 0), -1, true, true);
		Game.spawnNPC(20, new Location(2659, 3306, 0), -1, true, true);
		Game.spawnNPC(23, new Location(2659, 3306, 0), -1, true, true);
		Game.spawnNPC(2, new Location(2655, 3314, 0), -1, true, true);
		Game.spawnNPC(2, new Location(2656, 3298, 0), -1, true, true);
		Game.spawnNPC(2, new Location(2667, 3299, 0), -1, true, true);
		Game.spawnNPC(2, new Location(2667, 3314, 0), -1, true, true);
		Game.spawnNPC(9, new Location(2965, 3396, 0), -1, true, true);
		Game.spawnNPC(9, new Location(2965, 3396, 0), -1, true, true);
		Game.spawnNPC(9, new Location(2965, 3396, 0), -1, true, true);
		Game.spawnNPC(9, new Location(2965, 3396, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(19, new Location(2972, 3343, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3007, 3323, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3007, 3323, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3007, 3323, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3007, 3323, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3270, 3429, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3270, 3429, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3275, 3428, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3275, 3428, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3175, 3428, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3175, 3428, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3175, 3428, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3211, 3381, 0), -1, true, true);
		Game.spawnNPC(9, new Location(3211, 3381, 0), -1, true, true);
		Game.spawnNPC(172, new Location(3227, 3372, 0), -1, true, true);
		Game.spawnNPC(174, new Location(3227, 3372, 0), -1, true, true);
		Game.spawnNPC(172, new Location(3227, 3372, 0), -1, true, true);
		Game.spawnNPC(174, new Location(3228, 3367, 0), -1, true, true);
		Game.spawnNPC(172, new Location(3228, 3367, 0), -1, true, true);
		Game.spawnNPC(174, new Location(3228, 3367, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3299, 3387, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3300, 3294, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3295, 3299, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3299, 3306, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3299, 3313, 0), -1, true, true);
		Game.spawnNPC(107, new Location(3299, 3313, 0), -1, true, true);
		//Living Rock Caves
		Game.spawnNPC(7044, new Location(3626, 5113, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3624, 5128, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3634, 5143, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3654, 5141, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3696, 5141, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3695, 5137, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3682, 5110, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3677, 5111, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3649, 5083, 0), -1, true, true);
		Game.spawnNPC(7044, new Location(3629, 5083, 0), -1, true, true);
		Game.spawnNPC(8832, new Location(3644, 5096, 0), -1, true, true);
		Game.spawnNPC(8833, new Location(3632, 5090, 0), -1, true, true);
		Game.spawnNPC(8834, new Location(3635, 5107, 0), -1, true, true);
		Game.spawnNPC(82, new Location(3668, 5088, 0), -1, true, true);
		Game.spawnNPC(82, new Location(3679, 5097, 0), -1, true, true);
		Game.spawnNPC(83, new Location(3669, 5114, 0), -1, true, true);
		Game.spawnNPC(83, new Location(3664, 5125, 0), -1, true, true);
		Game.spawnNPC(84, new Location(3672, 5135, 0), -1, true, true);
		Game.spawnNPC(84, new Location(3685, 5134, 0), -1, true, true);
		Game.spawnNPC(84, new Location(3677, 5154, 0), -1, true, true);
		Game.spawnNPC(83, new Location(3653, 5137, 0), -1, true, true);
		Game.spawnNPC(84, new Location(3642, 5145, 0), -1, true, true);
		Game.spawnNPC(8834, new Location(3628, 5120, 0), -1, true, true);
		Game.spawnNPC(327, new Location(2595, 3423, 0), -1, true, true);
		Game.spawnNPC(328, new Location(2598, 3419, 0), -1, true, true);
		Game.spawnNPC(312, new Location(2488, 5137, 0), -1, true, true);
		Game.spawnNPC(6267, new Location(2594, 3424, 0), -1, true, true);
		Game.spawnNPC(327, new Location(4079, 4904, 0), -1, true, true);
		Game.spawnNPC(328, new Location(4076, 4904, 0), -1, true, true);
		Game.spawnNPC(312, new Location(4073, 4904, 0), -1, true, true);
		//NEW HOME WITH SKILLS SHOPS
		Game.spawnNPC(521, new Location(3682, 3477, 0), -1, true, true);
		Game.spawnNPC(522, new Location(3679, 3469, 0), -1, true, true);
		Game.spawnNPC(521, new Location(3682, 3477, 0), -1, true, true);
	
		Game.spawnNPC(8009, new Location(5856, 4703, 0), -1, false, true);
		
		
		Game.spawnNPC(3374, new Location(5893, 4699, 0), -1, false, true);
		Game.spawnNPC(3709, new Location(5884, 4699, 0), -1, true, true);
		//old home shops
		//World.spawnNPC(6537, new Location(3480, 3498, 0), -1, true, true);
		Game.spawnNPC(519, new Location(5868, 4714, 0), -1, true, true);
		Game.spawnNPC(12379, new Location(5865, 4705, 0), -1, true, true);
	//	World.spawnNPC(8443, new Location(3156, 3480, 0), -1, false, true);
//		World.spawnNPC(520, new Location(3158, 3483, 0), -1, true, true);
//		World.spawnNPC(521, new Location(3153, 3482, 0), -1, true, true);
//		World.spawnNPC(522, new Location(3150, 3493, 0), -1, true, true);
//		World.spawnNPC(523, new Location(3146, 3492, 0), -1, true, true);
//		World.spawnNPC(524, new Location(3149, 3490, 0), -1, true, true);
//		World.spawnNPC(525, new Location(3150, 3485, 0), -1, true, true);
//		World.spawnNPC(526, new Location(3164, 3478, 0), -1, true, true);
//		World.spawnNPC(527, new Location(3168, 3476, 0), -1, true, true);
//		World.spawnNPC(528, new Location(3167, 3472, 1), -1, true, true);
//		World.spawnNPC(529, new Location(3156, 3473, 0), -1, true, true);
//		World.spawnNPC(530, new Location(3157, 3492, 0), -1, true, true);
//		World.spawnNPC(531, new Location(3163, 3498, 0), -1, true, true);
//		World.spawnNPC(534, new Location(3168, 3505, 0), -1, true, true);
//		World.spawnNPC(535, new Location(3177, 3495, 0), -1, true, true);
//		World.spawnNPC(551, new Location(3173, 3492, 0), -1, true, true);
//		World.spawnNPC(552, new Location(3176, 3488, 0), -1, true, true);
//		World.spawnNPC(554, new Location(3167, 3487, 0), -1, true, true);
//		World.spawnNPC(555, new Location(3162, 3487, 0), -1, true, true);
		//World.spawnNPC(561, new Location(3500, 3469, 0), -1, true, true);
		Game.spawnNPC(1699, new Location(5862, 4694, 0), -1, true, true);
		Game.spawnNPC(1917, new Location(5857, 4693, 0), -1, true, true);
//		World.spawnNPC(11678, new Location(2870, 10206, 0), -1, true, true);
		Game.spawnNPC(11679, new Location(5857, 4686, 0), -1, true, true);
		Game.spawnNPC(536, new Location(5863, 4684, 0), -1, true, true);
		Game.spawnNPC(537, new Location(5873, 4694, 0), -1, true, true);
	//	World.spawnNPC(538, new Location(2863, 10206, 0), -1, true, true);
	//	World.spawnNPC(556, new Location(2863, 10204, 0), -1, true, true);
		Game.spawnNPC(540, new Location(5884, 4691, 0), -1, true, true);
		Game.spawnNPC(541, new Location(5891, 4692, 0), -1, true, true);
		
		Game.spawnNPC(6893, new Location(5901, 4696, 0), -1, true, true);
/* ------end of new home ---------*/
		Game.spawnNPC(2676, new Location(5911, 4718, 0), -1, true, true);//makover mage
		Game.spawnNPC(8443, new Location(5904, 4714, 0), -1, true, true);//

		Game.spawnNPC(8528, new Location(3360, 5860, 0), -1, true, true);// nomad

		Game.spawnNPC(8461, new Location(5874, 4713, 0), -1, true, true);//slayer
		Game.spawnNPC(550, new Location(5854, 4704, 0), -1, true, true);//range

	//	World.spawnNPC(445, new Location(3173, 3488, 0), -1, true, true);//frog runes
		Game.spawnNPC(519, new Location(5859, 4709, 0), -1, true, true);//bob

	//	World.spawnNPC(576, new Location(3173, 3486, 0), -1, true, true);//harry

		Game.spawnNPC(2253, new Location(5868, 4717, 0), -1, true, true);//skillcape

		// World.spawnNPC(6537, new Location(3087, 3500, 0), -1, true, true);  // mandrith

		/* ------------ Glacors ------------*/

		Game.spawnNPC(14301, new Location(4194, 5716, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4190, 5708, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4182, 5708, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4714, 5722, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4192, 5720, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4203, 5716, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4176, 5721, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4171, 5715, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4184, 5717, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4212, 5710, 0), -1, true, true);
		Game.spawnNPC(14301, new Location(4212, 5718, 0), -1, true, true);
		Game.spawnObject(new GlobalObject(2213, 10, 0, 3171, 9766, 0), true);
		Game.spawnObject(new GlobalObject(2213, 10, 0, 3681, 3468, 0), true);
		Game.spawnObject(new GlobalObject(2213, 10, 0, 3293, 3297, 0), true);
		Game.spawnObject(new GlobalObject(2478, 10, 0, 3189, 5718, 0), true);
		Game.spawnObject(new GlobalObject(2479, 10, 0, 3189, 5715, 0), true);
		Game.spawnObject(new GlobalObject(2480, 10, 0, 3189, 5710, 0), true);
		Game.spawnObject(new GlobalObject(2481, 10, 0, 3175, 5710, 0), true);
		Game.spawnObject(new GlobalObject(2482, 10, 0, 3176, 5716, 0), true);
		Game.spawnObject(new GlobalObject(2483, 10, 0, 3181, 5716, 0), true);
		Game.spawnObject(new GlobalObject(2484, 10, 0, 3193, 5713, 0), true);
		Game.spawnObject(new GlobalObject(2485, 10, 0, 3185, 5711, 0), true);
		Game.spawnObject(new GlobalObject(2486, 10, 0, 3179, 5710, 0), true);
		Game.spawnObject(new GlobalObject(2487, 10, 0, 3180, 5718, 0), true);
		Game.spawnObject(new GlobalObject(2488, 10, 0, 3184, 5708, 0), true);
		Game.spawnObject(new GlobalObject(17010, 10, 0, 3187, 5724, 0), true);
		Game.spawnObject(new GlobalObject(30624, 10, 0, 3188, 5721, 0), true);
		Game.spawnObject(new GlobalObject(34312, 10, 0, 3098, 3495, 0), true);
		Game.spawnObject(new GlobalObject(34313, 10, 0, 3097, 3495, 0), true);
		Game.spawnObject(new GlobalObject(34313, 10, 0, 3096, 3495, 0), true);
		spawned = true;
	}

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();

	public static void npcSpawn() {
		int size = 0;
		boolean ignore = false;
		try {
			for (String string : FileUtilities
					.readFile("data/npcs/npcspawns.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] spawn = string.split(" ");
				@SuppressWarnings("unused")
				int id = Integer.parseInt(spawn[0]), x = Integer
						.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer
						.parseInt(spawn[3]), faceDir = Integer
						.parseInt(spawn[4]);
				NPC npc = null;
				Class<?> npcHandler = CUSTOM_NPCS.get(id);
				if (npcHandler == null) {
					npc = new NPC(id, new Location(x, y, z), -1, true, false);
				} else {
					npc = (NPC) npcHandler.getConstructor(int.class)
							.newInstance(id);
				}
				if (npc != null) {
					Location spawnLoc = new Location(x, y, z);
					npc.setCoords(spawnLoc);
					Game.spawnNPC(npc.getId(), spawnLoc, -1, true, false);
					size++;
				}
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		System.err.println("Loaded " + size + " custom npc spawns!");
	}

}