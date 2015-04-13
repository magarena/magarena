package magic.model.action;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.event.MagicEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MagicRevealAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<MagicCard>();

    public MagicRevealAction(final MagicCard aCard) {
        cards.add(aCard);
    }

    public MagicRevealAction(final Collection<MagicCard> aCards) {
        cards.addAll(aCards);
    }

    public void doAction(final MagicGame game) {
        if (cards.isEmpty()) {
            return;
        }
        game.doAction(new AIRevealAction(cards));
        final MagicPlayer you = cards.get(0).getController();
        final String message = you + " reveals " + cardNames(cards);
        game.logAppendMessage(you, message);
        game.addEvent(new MagicEvent(
            MagicEvent.NO_SOURCE,
            you.getOpponent(),
            new MagicFromCardListChoice(cards, message),
            MagicEvent.NO_ACTION,
            ""
        ));
    }

    public void undoAction(final MagicGame game) {
        //do nothing for now
    }

    private static String cardNames(final Collection<MagicCard> cards) {
        final StringBuffer sb = new StringBuffer();
        for (final MagicCard card : cards) {
            sb.append(card.toString());
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append('.');
        return sb.toString();
    }
}
