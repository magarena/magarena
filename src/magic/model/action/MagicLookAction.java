package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagicLookAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<MagicCard>();
    private final String desc;

    public MagicLookAction(final MagicCard aCard, final String aDesc) {
        cards.add(aCard);
        desc = aDesc;
    }

    public MagicLookAction(final Collection<MagicCard> aCards, final String aDesc) {
        cards.addAll(aCards);
        desc = aDesc;
    }

    public void doAction(final MagicGame game) {
        if (cards.isEmpty()) {
            return;
        }
        game.addEvent(new MagicEvent(
            MagicEvent.NO_SOURCE,
            cards.get(0).getController(),
            new MagicFromCardListChoice(cards, "Look at the " + desc + "."),
            MagicEvent.NO_ACTION,
            ""
        ));
    }

    public void undoAction(final MagicGame game) {
        //do nothing for now
    }
}
