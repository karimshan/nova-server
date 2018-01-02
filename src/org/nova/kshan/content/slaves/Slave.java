package org.nova.kshan.content.slaves;

import java.io.Serializable;

import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.game.player.actions.PlayerFollow;
import org.nova.kshan.bot.Bot;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

/**
 * Represents a player's slave.
 * 
 * @author K-Shan
 *
 */
public class Slave implements Serializable {
	
	private static final long serialVersionUID = 8396297974780130082L;
	
	public static final String GENERATED_PASSWORD = "Ai29u20JIDnJ20i2hNDBUD920DHdbjdQJssj";
	
	private transient Player player;
	private transient boolean connected;
	
	private String username;
	private Bot bot;
	
	/**
	 * Sets the player
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	/**
	 * Creates the bot instance
	 */
	public void create(String username) {
		this.username = username;
		if(SFiles.containsPlayer(username)) {
			player.sm("Your slave cannot have that name.");
			return;
		}
		if(bot != null)
			return;
		setBot(new Bot(username, GENERATED_PASSWORD, Location.findEmptyLocation(player, 1)));
		bot.create();
		bot.getActionManager().setAction(new PlayerFollow(player));
		bot.forceTalk("I'm at your service.");
		connected = true;
	}
	
	public void terminate(String... message) {
		if(message.length == 1)
			player.sm(message[0]);
		bot.finish();
		connected = false;
	}
	
	/**
	 * Processes the game pulse
	 */
	public void processPulse() {
		if(!connected)
			return;
		if(!bot.withinDistance(player, 8)) {
			bot.setLocation(Location.findEmptyLocation(player, 1));
			bot.graphics(2009);//188); // Either one is okay
			bot.getActionManager().setAction(new PlayerFollow(player));
			if(Misc.random(2) == 0)
				bot.forceTalk("Wait up "+(player.getAppearance().isMale() ? "sir" : "ma'am")+"!");
		}
	}
	
	public void call(String... message) {
		if(connected) {
			if(bot.withinDistance(player, 8)) {
				player.sm(bot.getDisplayName()+" is already here!");
				return;
			} else {
				bot.setLocation(Location.findEmptyLocation(player, 1));
				bot.getActionManager().setAction(new PlayerFollow(player));
			}
			return;
		}
		if(message.length == 1)
			player.forceTalk(message[0]);
		bot = (Bot) SFiles.loadPlayer(getUsername());
		bot.setUsername(getUsername());
		bot.create();
		bot.getActionManager().setAction(new PlayerFollow(player));
		bot.forceTalk("I am at your service, "+player.getDisplayName()+".");
		connected = true;
	}
	
	public void viewInventory() {
		player.sm("You are viewing your slave's inventory.");
	}
	
	public String getUsername() {
		return username;
	}
	
	public Bot getBot() {
		return bot;
	}
	
	public void setBot(Bot bot) {
		this.bot = bot;
	}

}
