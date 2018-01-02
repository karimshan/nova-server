/* Lucien.java - Created by n1ght br0
ask to repost.
 */
package org.nova.game.player.dialogues;

import org.nova.cache.definition.NPCDefinition;
import org.nova.game.map.Location;
import org.nova.game.player.content.Magic;

public class Lucien extends MatrixDialogue {

private int npcId;

@Override
        public void start() {
                npcId = (Integer) parameters[0];
                sendEntityDialogue(SEND_4_TEXT_CHAT,
                                new String[] { NPCDefinition.get(npcId).name,
                                                "Hello, I can teleport you to PvP Areas.",
                                                "What you do there is fight other player,",
                                                " and get tokens and you can spend them,",
                                                "on great prizes." }, IS_NPC, npcId, 9827);
        }

        @Override
        public void run(int interfaceId, int componentId) {
                if (stage == -1) {
                        sendEntityDialogue(SEND_1_TEXT_CHAT,
                                        new String[] { player.getDisplayName(), "Sure, why not." },
                                        IS_PLAYER, player.getIndex(), 9827);
                        stage = 1;
                } else if (stage == 1) {
                        stage = 2;
                        sendDialogue(SEND_5_OPTIONS, "Where would you like to go?",
                                                "Magic Bank.", "Multi Area. (PvP)", "Fight Pits.",
                                                "Wests(PvP)", "More Options");
                } else if (stage == 2) {
                        if (componentId == 1)
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2539,
                                                4712, 0));
                        else if (componentId == 2) {
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3240,
                                                3611, 0));
                                player.getControllerManager().startController("Wilderness");
                        } else if (componentId == 3)
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2399,
                                                5177, 0));
                        else if (componentId == 4) {
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(2984,
                                                3596, 0));
                                player.getControllerManager().startController("Wilderness");
                        } else if (componentId == 5) {
                                sendDialogue(SEND_2_OPTIONS, "Where would you like to go?",
                                                "Easts (PvP)", "Edgeville.");
                                stage = 3;
                        }
                } else if (stage == 3) {
                        if (componentId == 1) {
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3360,
                                                3658, 0));
                                player.getControllerManager().startController("Wilderness");
                        } else if (componentId == 2) {
                                Magic.sendNormalTeleportSpell(player, 0, 0, new Location(3092,
                                                3498, 0));
                        }
                }
        }
        
        @Override
        public void finish() {

        }
}