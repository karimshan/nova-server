package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.engine.cores.tasks.WorldTask;
import org.nova.game.engine.cores.tasks.WorldTasksManager;
import org.nova.game.map.Location;
import org.nova.game.player.content.TeleportManager;
/**
 * 
 * @author Fuzen Seth
 * @information Game introduction.
 * @since 24.6.2014
 */
public class ThessaliaD extends MatrixDialogue {
	/**
	 * Holds the NPC id.
	 */
	private int npcId = 548;

	
	
	@Override
	public void start() {
		npcId = (Integer) parameters[0];
		sendEntityDialogue(SEND_1_TEXT_CHAT,
				new String[] { NPCDefinition.get(npcId).name,
						"You look handsome, "+player.getDisplayName()+"!"}, IS_NPC, npcId, 9785);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
			switch (loop) {
			case 0:	player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_1_TEXT_CHAT,
						new String[] { player.getDisplayName(), "Oh hey Thessalia and thank you!" },
						IS_PLAYER, player.getIndex(), 9827);
				break;
			case 3:	player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_3_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
								"No problem "+player.getDisplayName()+". I'm going to",
								"Introduce you quickly to Nova. It won't", "take too long!"}, IS_NPC, npcId, 9827);
				break;
			case 7:	player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_2_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
								"This is the Grand Exchange, where you can buy",
								"Any items that you need, also why not just hang out?"}, IS_NPC, npcId, 9827);
				break;
			case 10:
				TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3086, 3501, 0));
			
				break;
			case 11:	player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_4_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
							"This is Nova's home area, Edgeville!",
							"To teleport around the world speak with the Wise Old Man.",
							"Father Aereck will change your prayers and spellbooks.",
							"Locks and traps trainer displays all quest rewards."}, IS_NPC, npcId, 9827);
				break;
			case 17:
				TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3086,3486, 0));
				break;
			case 18:
				player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_4_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
							"Dean Vellios is friendly for new adventurers,",
							"speak with him and he will take you to the best",
							"training areas on Nova! Vellio also",
							"sells you combat supplies that you can use in training."}, IS_NPC, npcId, 9827);
				break;
			case 25:
				TeleportManager.preformScrollTeleportation(player, 0,0, new Location(2995,9679, 0));
				break;
			case 26:
				player.addStopDelay(555);
				sendEntityDialogue(SEND_NO_CONTINUE_3_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
							"We have loads of minigames and other fun",
							"activities on Nova. Clan wars is a very fun",
							"area to go for an example."}, IS_NPC, npcId, 9827);
				break;
			case 31:

				TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3101,3493,0));
				break;
			case 32:
				TeleportManager.preformScrollTeleportation(player, 0,0, new Location(3101,3493,0));
				player.addStopDelay(555);
				sendEntityDialogue(SEND_1_TEXT_CHAT,
						new String[] { NPCDefinition.get(npcId).name,
							"You are now free to go, "+player.getDisplayName()+"."}, IS_NPC, npcId, 9827);
			
				break;
			case 34:
				player.addStopDelay(1);
				stop();	 //force stop introducing
				end(); //we end dialogue.
				break;
			}
				loop++;
				}
			}, 0, 1);
		
	}

	@Override
	public void finish() {

	}
}