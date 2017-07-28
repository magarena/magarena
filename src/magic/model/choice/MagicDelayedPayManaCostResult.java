package magic.model.choice;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicCopyMap;
import magic.model.action.PayDelayedCostAction;

/**
 * Delayed mana costs can give wrong scores when a mana source:
 * - taps to produces mana and then attacks or blocks : not possible
 * - produced mana and then leaves play : reduces the available mana incorrectly
 */
public class MagicDelayedPayManaCostResult implements MagicPayManaCostResult {

    private final MagicManaCost cost;
    private final int x;

    MagicDelayedPayManaCostResult(final MagicManaCost cost,final int x) {
        this.cost=cost;
        this.x=x;
    }

    public MagicManaCost getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return cost.getText() + x;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getConverted() {
        return cost.getConvertedCost(x);
    }

    @Override
    public void doAction(final MagicGame game,final MagicPlayer player) {
        game.doAction(new PayDelayedCostAction(player,this));
    }

    @Override
    public MagicDelayedPayManaCostResult copy(final MagicCopyMap copyMap) {
        return this;
    }

    @Override
    public MagicDelayedPayManaCostResult map(final MagicGame game) {
        return this;
    }

    @Override
    public long getId() {
        return hashCode();
    }
}
