package magic.model.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class RevealAction extends MagicAction {
    
    private final List<MagicCard> cards = new ArrayList<>();

    public RevealAction(final MagicCard aCard) {
        cards.add(aCard);
    }

    public RevealAction(final Collection<MagicCard> aCards) {
        cards.addAll(aCards);
    }

    @Override
    public void doAction(final MagicGame game) {
        if (cards.isEmpty()) {
            return;
        }
        game.doAction(new AIRevealAction(cards));
        final MagicPlayer you = cards.get(0).getController();
        final String message = you + " reveals " + cardNames(cards);
        System.out.println(message);
        game.logAppendMessage(you, message);
        game.addEvent(new MagicEvent(
            MagicSource.NONE,
            you.getOpponent(),
            new MagicFromCardListChoice(cards, message),
            MagicEventAction.NONE,
            ""
        ));
    }

    @Override
    public void undoAction(final MagicGame game) {
        //do nothing for now
    }

    public static String cardNames(final Collection<MagicCard> cards) {
        return cards.stream()
            .map(card -> MagicMessage.getCardToken(card))
            .collect(Collectors.joining(", ", "", cards.isEmpty() ? "" : "."));
    }
}
