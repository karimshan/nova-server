package org.nova.game.player;

import java.io.Serializable;


public final class Skills implements Serializable {

	private static final long serialVersionUID = -7086829989489745985L;

	public static final double MAXIMUM_EXP = 200000000;
	public static final int SKILL_COUNT = 25;
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGE = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, CONSTRUCTION = 22, HUNTER = 21, SUMMONING = 23,
			DUNGEONEERING = 24;

	public static final String[] SKILL_NAME = { "Attack", "Defence",
			"Strength", "Hitpoints", "Range", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Hunter", "Construction", "Summoning",
			"Dungeoneering" };

	public short level[];
	public double xp[];
	private double xpCounter;

	private transient Player player;

	public void passLevels(Player p) {
		this.level = p.getSkills().level;
		this.xp = p.getSkills().xp;
	}

	public Skills() {
		level = new short[25];
		xp = new double[25];
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		xp[3] = 1184;
		level[HERBLORE] = 3;
		xp[HERBLORE] = 250;
	}

	public void restoreSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			level[skill] = (short) getLevelFromXP(skill);
			refresh(skill);
		}
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getLevel(int skill) {
		return level[skill];
	}

	public double getXp(int skill) {
		return xp[skill];
	}
		public static int getTotalLevel(Player player) {
			int totallevel = 0;
			for(int i = 0; i <= 24; i++) {
				totallevel += player.getSkills().getLevelFromXP(i);
			}
			return totallevel;
		}
	public int getCombatLevel() {
		int attack = getLevelFromXP(0);
		int defence = getLevelFromXP(1);
		int strength = getLevelFromXP(2);
		int hp = getLevelFromXP(3);
		int prayer = getLevelFromXP(5);
		int ranged = getLevelFromXP(4);
		int magic = getLevelFromXP(6);
		int combatLevel = 3;
		combatLevel = (int) ((defence + hp + Math.floor(prayer / 2)) * 0.25) + 1;
		double melee = (attack + strength) * 0.325;
		double ranger = Math.floor(ranged * 1.5) * 0.325;
		double mage = Math.floor(magic * 1.5) * 0.325;
		if (melee >= ranger && melee >= mage) {
			combatLevel += melee;
		} else if (ranger >= melee && ranger >= mage) {
			combatLevel += ranger;
		} else if (mage >= melee && mage >= ranger) {
			combatLevel += mage;
		}
		return combatLevel;
	}

	public void set(int skill, int newLevel) {
		level[skill] = (short) newLevel;
		refresh(skill);
	}

	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		refresh(skill);
		return drainLeft;
	}

	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}

	public int getSummoningCombatLevel() {
		return getLevelFromXP(Skills.SUMMONING) / 8;
	}

	public void drainSummoning(int amt) {
		int level = getLevel(Skills.SUMMONING);
		if (level == 0)
			return;
		set(Skills.SUMMONING, amt > level ? 0 : level - amt);
	}

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public void init() {
		for (int skill = 0; skill < level.length; skill++)
			refresh(skill);
		refreshXpCounter();
	}

	private void refreshXpCounter() {
		player.packets().sendConfig(1801, (int) (xpCounter * 10));
	}

	public void resetXpCounter() {
		xpCounter = 0;
		refreshXpCounter();
	}

	public void refresh(int skill) {
		player.packets().sendSkillLevel(skill);
	}
	
	public void addXp(int skill, double exp) {
		addXp(skill, exp, true);
	}
	
	public void addRSXP(int skill, double exp) {
		addXp(skill, exp, false);
	}

	public void addXp(int skill, double exp, boolean useXPBoost) {
		if(useXPBoost) {
			if(skill == ATTACK || skill == STRENGTH || skill == DEFENCE 
					|| skill == RANGE|| skill == HITPOINTS || skill == MAGIC) {
				exp *= player.combatXPBoost();
			} else {
				exp *= player.skillXPBoost();
			}
		}
		player.getControllerManager().trackXP(skill, (int) exp);
		int oldLevel = getLevelFromXP(skill);
		xp[skill] += exp;
		xpCounter += exp;
		refreshXpCounter();
		if (xp[skill] > MAXIMUM_EXP) {
			xp[skill] = MAXIMUM_EXP;
		}
		int newLevel = getLevelFromXP(skill);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skill] += levelDiff;
			
			player.getMatrixDialogues().startDialogue("LevelUp", skill);
			if (skill == HITPOINTS)
				player.heal(levelDiff * 10);
			if (skill == PRAYER)
				player.getPrayer().restorePrayer(levelDiff * 10);
			if (skill == SUMMONING || (skill >= ATTACK && skill <= MAGIC))
				player.getAppearance().generateAppearanceData();
		}
		refresh(skill);
	}
	public int getLevelFromXP(int skill) {
		double exp = xp[skill];
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0
					* Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}

public boolean isMaxed(){
	int maxlevels = 0;
	for (int ji = 0; ji < level.length; ji++){
		if (this.getLevel(ji) != 99){
			continue;
		}
		maxlevels++;
	}
	if (maxlevels >= 23){
		return true;
	}
	return false;
}
	public void addSkillXpRefresh(int skill, double xp) {
		this.xp[skill] += xp;
		level[skill] = (short) getLevelFromXP(skill);
	}

	public void resetSkillNoRefresh(int skill) {
		xp[skill] = 0;
		level[skill] = 1;
	}

	public void setXp(int skill, double exp) {
		xp[skill] = exp;
		refresh(skill);
	}

	public boolean hasRequiriments(int magic2, int i) {
		// TODO Auto-generated method stub
		return false;
	}

	   public boolean hasRequiriments(int... skills) {
			for (int i = 0; i < skills.length; i += 2) {
			    int skillId = skills[i];
			    if (skillId == CONSTRUCTION || skillId == DUNGEONEERING || skillId == SLAYER)
				continue;
			    int skillLevel = skills[i + 1];
			    if (getLevelFromXP(skillId) < skillLevel)
				return false;

			}
			return true;
		    }

	public void reset() {
		for (int i = 0; i < SKILL_COUNT; i++) {
			level[i] = 1;
			xp[i] = 0;
		}
		level[3] = 10;
		player.setHitpoints(100);
		xp[3] = 1184;
		level[5] = 1;
		player.getPrayer().setPrayerpoints(10);
		xp[5] = 0;
		player.packets().sendConfigByFile(7198, player.getHitpoints());
		player.packets().sendConfig(2382, player.getPrayer().getPrayerpoints());
		player.packets().sendSkillLevels();
		player.getSkills().refresh(Skills.PRAYER);
		player.getSkills().refresh(Skills.HITPOINTS);
		player.getAppearance().generateAppearanceData();
	}

}
