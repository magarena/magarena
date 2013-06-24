package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;

public class MagicSetKickerAction extends MagicAction {

    private final int newKicker;
    private int oldKicker;

    public MagicSetKickerAction(final int aKicker) {
        newKicker = aKicker;
    }

    @Override
    public void doAction(final MagicGame game) {
        final MagicPayedCost payedCost = game.getPayedCost();
        oldKicker = payedCost.getKicker();
        payedCost.setKicker(newKicker);
        game.setPayedCost(payedCost);
    }

    @Override
    public void undoAction(final MagicGame game) {
        final MagicPayedCost payedCost = game.getPayedCost();
        payedCost.setKicker(oldKicker);
        game.setPayedCost(payedCost);
    }
}
