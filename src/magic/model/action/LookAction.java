package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LookAction extends MagicAction {

    private final List<MagicCard> cards = new ArrayList<MagicCard>();
    private final MagicPlayer player;
    private final String desc;

    public LookAction(final MagicCard aCard, final MagicPlayer aPlayer, final String aDesc) {
        cards.add(aCard);
        player = aPlayer;
        desc = aDesc;
    }

    public LookAction(final Collection<MagicCard> aCards, final MagicPlayer aPlayer, final String aDesc) {
        cards.addAll(aCards);
        player = aPlayer;
        desc = aDesc;
    }

    @Override
    public void doAction(final MagicGame game) {
        if (cards.isEmpty()) {
            return;
        }
        game.addEvent(new MagicEvent(
            MagicSource.NONE,
            player,
            new MagicFromCardListChoice(cards, "Look at the " + desc + "."),
            MagicEventAction.NONE,
            ""
        ));
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing for now
    }
}
