package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Look means reveal to self, i.e. look at your library
public class MagicLookAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<MagicCard>();
    private final List<Boolean> known = new ArrayList<Boolean>();
    private boolean newValue;

    public MagicLookAction(final Collection<MagicCard> aCards, final boolean aNewValue) {
        cards.addAll(aCards);
        newValue = aNewValue;
    }

    public void doAction(final MagicGame game) {
        for (final MagicCard card : cards) {
            known.add(card.isGameKnown());
            card.setGameKnown(newValue);
        }
    }

    public void undoAction(final MagicGame game) {
        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setGameKnown(known.get(i));
        }
    }
}
