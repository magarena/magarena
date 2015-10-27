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
import magic.model.MagicMessage;

public class RevealAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<MagicCard>();

    public RevealAction(final MagicCard aCard) {
        cards.add(aCard);
    }

    public RevealAction(final Collection<MagicCard> aCards) {
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
            MagicSource.NONE,
            you.getOpponent(),
            new MagicFromCardListChoice(cards, message),
            MagicEventAction.NONE,
            ""
        ));
    }

    public void undoAction(final MagicGame game) {
        //do nothing for now
    }

    private static String cardNames(final Collection<MagicCard> cards) {
        final StringBuffer sb = new StringBuffer();
        for (final MagicCard card : cards) {
            sb.append(MagicMessage.getCardToken(card));
            sb.append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append('.');
        return sb.toString();
    }
}
