package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;

public class ShiftCardAction extends MagicAction {
    public final MagicCard card;
    public final MagicLocationType from;
    public final MagicLocationType to;

    public ShiftCardAction(final MagicCard aCard, final MagicLocationType fromLocation, final MagicLocationType toLocation) {
        card = aCard;
        from = fromLocation;
        to   = toLocation;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (card.isIn(from)) {
            game.doAction(new RemoveCardAction(card, from));
            game.doAction(new MoveCardAction(card, from, to));
        }
    }

    @Override
    public void undoAction(final MagicGame game) {}
}
