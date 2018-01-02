package org.nova.game.player.content.cities.content;

import org.nova.game.player.Player;

public class Canoes {

	public Canoes() {
		
	}

	public static final void travel(Player player) {
		
	}
	/**
	 * Grabs the configs
	 * @author Fuzen Seth
	 *
	 */
	public static enum ConfigsBase {
		EDGEVILLE,
		LUMBRIDGE,
		WILDERNESS,
		CHAMPION_GUILD,
		BARBARIAN_VILLAGE;
		public static ConfigsBase getDestinationConfigs(int index) {
			switch (index) {
			case 0: //Config id
				return ConfigsBase.EDGEVILLE;
			case 1: 
				return ConfigsBase.LUMBRIDGE;
			case 2:
				return ConfigsBase.BARBARIAN_VILLAGE;
			case 3:
				return ConfigsBase.CHAMPION_GUILD;
			case 4:
				return ConfigsBase.WILDERNESS;
				
			}
			return null;
		}
	}
	
	
	
}
