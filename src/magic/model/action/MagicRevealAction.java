package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;

import java.util.Collection;
import java.util.ArrayList;

public class MagicRevealAction extends MagicAction {
    
    private final Collection<MagicCard> cards = new ArrayList<MagicCard>();

    public MagicRevealAction(final MagicCard aCard) {
        cards.add(aCard);
    }

    public MagicRevealAction(final Collection<MagicCard> aCards) {
        cards.addAll(aCards);
    }

    public void doAction(final MagicGame game) {
        //do nothing for now
    }

    public void undoAction(final MagicGame game) {
        //do nothing for now
    }
}
