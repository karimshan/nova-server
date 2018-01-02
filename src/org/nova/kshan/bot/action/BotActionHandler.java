package org.nova.kshan.bot.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

import org.nova.game.Game;
import org.nova.game.engine.GameTick;
import org.nova.game.player.Player;
import org.nova.kshan.bot.Bot;
import org.nova.kshan.bot.BotList;
import org.nova.utility.loading.Logger;
import org.nova.utility.misc.Misc;

/**
 * 
 * @author K-Shan
 *
 */
public class BotActionHandler {
	
	/**
	 * Nothing is going to be saved
	 */
	private transient BotAction botAction;
	private transient Bot bot;
	private transient GameTick currentTick;

	public void handleAction(String actionType, Object... extras) {
		if(botAction != null)
			stopAction();
		botAction = getAction(actionType);
		if(botAction == null) 
			return;
		botAction.bot = bot;
		botAction.data = extras;
		botAction.startAction();
	}
	
	public void stopAction() {
		if(botAction == null)
			return;
		botAction.setResponding(false);
		getCurrentTick().stop();
		botAction = null;
	}
	
	public void setBot(Bot bot) {
		this.bot = bot;
	}
	
	public Bot getBot() {
		return bot;
	}

	/**
	 * Sends an action response to the player.
	 * @param player
	 * @param messageReceived
	 */
	public void sendResponse(final Player player, final String messageReceived) {
		try {
			@SuppressWarnings("resource")
			BufferedReader r = new BufferedReader(new FileReader(new File("data/misc/botResponse.txt")));
			while (true) {
				String line = r.readLine();
				if (line == null)
					break;
				if (line.startsWith("//") || line.equals(""))
					continue;
				String[] splitArray;
				splitArray = line.split(" - ");
				final String actionType = splitArray[1];
				String playerInput = splitArray[2];
				String[] playerInputArray = playerInput.split("- ");
				String botResponse = splitArray[splitArray.length > 3 ? 3 : 2]; // basically makes the player input the bot output
				final String[] botResponseArray = botResponse.split("- "); // splits up every response of the bot into individual strings
				for(int i = 0; i < playerInputArray.length; i++) {
					if(replace(player, messageReceived).toLowerCase().
							equals(actionType.equals("ATTACK_NPC") ? replaceWithMessageReceived(playerInputArray[i], messageReceived) : 
								replace(player, playerInputArray[i]).toLowerCase())) {
						final String replaceFrom = playerInputArray[i];
						final String replaceWith = actionType.equals("ATTACK_NPC") ? replaceWithMessageReceived(playerInputArray[i], messageReceived) : "";
						for(Player p : Game.getLocalPlayers(player)) {
							if(p != null) {
								if(p.isBot()) {
									for(final Bot bot : BotList.getBots()) {
										if(bot != null) {
											if(bot.getUsername().equalsIgnoreCase(p.getUsername())) {
												String response = "";
												for(int j = 0; j < botResponseArray.length; j++)
													response = botResponseArray[(Misc.random(botResponseArray.length))];
												response = bot.getBotActionHandler().replace(bot, response);
												if(bot.getBotActionHandler().getCurrentBotAction() == null) {
													if(actionType.equals("ATTACK_NPC"))
														bot.getBotActionHandler().handleAction(actionType, messageReceived, 
															replaceFrom, replaceWith);
													else
														bot.getBotActionHandler().handleAction(actionType, messageReceived, response);	
													return;
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}	
			r.close();
		} catch(Throwable e) {
			e.printStackTrace();
		}
	}
	
	private BotAction getCurrentBotAction() {
		return botAction;
	}

	/**
	 * Replaces any annotations in the .txt document which loads the responses.
	 * @param p
	 * @param message
	 * @return
	 */
	public String replace(Player p, String message) {
		boolean isSkill = false;
		if(message.contains("@lvl"))
			isSkill = true;
		if(isSkill)
			for(int i = 0; i <= 24; i++)
				message = message.replace("@lvl"+i+"@", ""+p.getSkills().getLevel(i));
		else {
			if(message.contains("@cblvl@"))
				message = message.replace("@cblvl@", ""+p.getSkills().getCombatLevelWithSummoning());

		}
		return message;
	}
	
	/**
	 * Replaces the @npcName@ annotation which the npcName, if the action type is ATTACK_NPC
	 * @param replaceFrom
	 * @param replaceWith
	 * @return
	 */
	public String replaceWithMessageReceived(String replaceFrom, String replaceWith) {
		String npcName = "";
		String charsUpToNPCName = "";
		charsUpToNPCName = replaceFrom.replace(" @npcName@", "");
		npcName = replaceWith.replace(charsUpToNPCName+" ", "");
		replaceFrom = replaceFrom.replace("@npcName@", npcName);
		return replaceFrom;
	}

	public GameTick getCurrentTick() {
		return currentTick;
	}

	public void setCurrentTick(GameTick currentTick) {
		this.currentTick = currentTick;
	}
	
	private static final HashMap<Object, Class<BotAction>> actions = new HashMap<Object, Class<BotAction>>();
	
	@SuppressWarnings({ "unchecked" })
	public static final void addAll() {
		actions.clear();
		try {
			Class<BotAction>[] classes = Misc.getClasses("org.nova.kshan.bot.action.impl");
			for (Class<BotAction> c : classes) {
				if (c.isAnonymousClass())
					continue;
				actions.put(c.getSimpleName(), c);
			}
			addOthers();
		} catch (Throwable e) {
			Logger.handle(e);
		}
	}
	
	private static void addOthers() throws ClassNotFoundException {
		
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	private static void addClass(Class<?> c) throws ClassNotFoundException {
		actions.put(c.getSimpleName(), (Class<BotAction>) Class.forName(c.getCanonicalName()));
	}

	public static final void reload() {
		actions.clear();
		addAll();
	}
	
	public static final BotAction getAction(String key) {
		key = key.toLowerCase().replaceAll("_", "");
		Class<BotAction> bA = null;
		for(Object s : actions.keySet()) {
			String name = (String) s;
			name = name.toLowerCase().replace("bot", "");
			if(name.equals(key))
				bA = actions.get(s);
		}
		if(bA == null)
			return null;
		try {
			return bA.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
