package magic.model.action;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.choice.MagicBuilderManaCost;

public class PayDelayedCostsAction extends MagicAction {

    private final MagicPlayer player;
    private MagicBuilderManaCost oldBuilderCost;

    public PayDelayedCostsAction(final MagicPlayer aPlayer) {
        player = aPlayer;
    }

    /**
     * The actual mana activations are never done for speed reasons.
     * This can have side effects in decision making (e.g. pain mana).
     */
    @Override
    public void doAction(final MagicGame game) {
        oldBuilderCost = player.getBuilderCost();
        player.setBuilderCost(MagicBuilderManaCost.ZERO_COST);
    }

    @Override
    public void undoAction(final MagicGame game) {
        player.setBuilderCost(oldBuilderCost);
    }
}
