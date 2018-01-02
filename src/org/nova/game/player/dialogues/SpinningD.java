package org.nova.game.player.dialogues;

import org.nova.cache.definition.ItemDefinition;
import org.nova.game.player.Player;
import org.nova.game.player.actions.Action;
import org.nova.game.player.content.SkillsDialogue;
import org.nova.game.player.content.Spinning;

/**
 * The dialogue of the spinning wheel.
 * @author Arham Siddiqui
 */
public class SpinningD extends MatrixDialogue {

    private int itemId[];

    @Override
    public void start() {
        SkillsDialogue.sendSkillsDialogue(player, SkillsDialogue.MAKE, "Choose how many you wish to make, then click on the chosen item to begin.", 28, new int[] {1759, 1777, 9438, 9438, 6038, 954}, new SkillsDialogue.ItemNameFilter() {
            @Override
            public String rename(String name) {
                if (name.equalsIgnoreCase(ItemDefinition.get(1759).getName())) {
                    return "Ball of wool (Wool)";
                } else if (name.equalsIgnoreCase(ItemDefinition.get(1777).getName())) {
                    return "Bow string (Flax)";
                } else if (name.equalsIgnoreCase(ItemDefinition.get(9438).getName()) && SkillsDialogue.getItemSlot(16) == 2) {
                    return "C'bow string (Sinew)";
                } else if (name.equalsIgnoreCase(ItemDefinition.get(9438).getName()) && SkillsDialogue.getItemSlot(17) == 3) {
                    return "C'bow string (Tree roots)";
                } else if (name.equalsIgnoreCase(ItemDefinition.get(6038).getName())) {
                    return "Magic string (Magic roots)";
                } else if (name.equalsIgnoreCase(ItemDefinition.get(954).getName())) {
                    return "Rope (Yak hide)";
                }
                return name;
            }
        });
    }

    @Override
    public void run(int interfaceId, int componentId) {
        int option = SkillsDialogue.getItemSlot(componentId);
        itemId = Spinning.getBeforeFromAfter(SkillsDialogue.getItem(option));
        final int[] quantity = {SkillsDialogue.getQuantity(player)};
        int invQuantity = player.getInventory().getItems().getNumberOf(itemId[0]);
        if (quantity[0] > invQuantity)
            quantity[0] = invQuantity;
        Action action = new Action() {
            @Override
            public boolean start(Player player) {
                if (quantity[0] <= 0) {
                    return false;
                }
                return true;
            }

            @Override
            public boolean process(Player player) {
                if (quantity[0] <= 0) {
                    return false;
                }
                return true;
            }

            @Override
            public int processWithDelay(Player player) {
                quantity[0]--;
                Spinning.canSpin(player, itemId[0]);
                return 5;
            }

            @Override
            public void stop(Player player) {
            }
        };
        player.getActionManager().setAction(action);
        end();
    }

    @Override
    public void finish() {

    }
}