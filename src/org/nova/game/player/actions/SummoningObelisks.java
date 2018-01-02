package org.nova.game.player.actions;

import org.nova.game.player.Player;
import org.nova.game.player.Skills;
/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
 *
 */
public class SummoningObelisks {
	
	public static int getAffectedSkill(Player player, int skillId,
			int actualLevel, int realLevel) {
		int restore = (int) (Math.floor(player.getSkills().getLevelFromXP(Skills.SUMMONING) * 0.25) + 7);
		if(actualLevel + restore > realLevel)
			return realLevel;
		return actualLevel + restore;
	}
}
