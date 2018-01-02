package org.nova.kshan.bot;

import java.io.Serializable;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.map.Location;
import org.nova.game.player.Player;
import org.nova.kshan.bot.action.BotActionHandler;
import org.nova.utility.loading.playerutils.SFiles;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class Bot extends Player implements Serializable {
	
	private static final long serialVersionUID = -4864898276631259474L;

	private transient BotPackets packets;
	private transient BotActionHandler botActionHandler;
	
	public Bot() {
		super(null);
	}
	
	public Bot(String name, String password, Location location) {
		super(password, location); // New player instance
		this.username = name;
	}

	public Bot create() {
		packets = new BotPackets(this); // Sets the "packets" to be sent to the client
		botActionHandler = new BotActionHandler();
		botActionHandler.setBot(this);
		super.init(null, getUsername(), 0, 0, 0, null); // Initializes player fields
		super.start(); // Sends initial startup stuff
		super.setBot(true); // Declares this player a bot.
		BotList.getBots().add(this);
		SFiles.savePlayer(this); // Initial save.
		return this;
	}
	
	@Override
	public void processEntity() {
		super.processEntity();
	}
	
	public BotActionHandler getBotActionHandler() {
		return botActionHandler;
	}
	
	public BotPackets getFakePacketSender() {
		return packets;
	}
	
	public boolean isOnline() {
		return Game.containsPlayer(getUsername());
	}
	
	@Override
	public void publicChat(String message) {
		String formatted = Misc.fixChatMessage(message);
		final Bot bot = this;
		forceTalk(formatted);
		Game.submit(new GameTick(0.3) {

			@Override
			public void run() {
				stop();
				for(Player p : Game.getLocalPlayers(bot))
					if(p != null) {
						p.sm(getDisplayName()+": <col=0000ff>"+(formatted));
					}
			}
			
		});
	}
	
	public String getInfo() {
		return "["+getUsername()+" - "+getPassword()+" - "+getLocation().toString()+"]";
	}

}
