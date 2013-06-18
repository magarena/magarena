package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;

public class MagicSetKickerAction extends MagicAction {

    private final MagicCard card;
    private final int newKicker;
    private final int oldKicker;
    
    public MagicSetKickerAction(final MagicCard aCard,final int aKicker) {
        card = aCard;
        oldKicker = card.getKicker();
        newKicker = aKicker;
    }
    
    @Override
    public void doAction(final MagicGame game) {
        card.setKicker(newKicker);
    }

    @Override
    public void undoAction(final MagicGame game) {
        card.setKicker(oldKicker);
    }
}
