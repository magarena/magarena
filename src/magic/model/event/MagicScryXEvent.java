
package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.ScryComplAction;
import magic.model.choice.MagicFromCardListChoice;

/**
 * General "Scry X" effect.
 * Not used (commented out in MagicRuleEventAction), as there are too many choices for AI potentially to make
 */
public class MagicScryXEvent extends MagicEvent {

    public MagicScryXEvent(final MagicSource source, final MagicPlayer player, final int X) {
        super(
            source,
            player,
            X,
            StartScrying,
            ""
        );
    }

    private static final MagicEventAction TopAction = (final MagicGame game, final MagicEvent event) ->
        event.processChosenCards(game, (final MagicCard card) -> {
            game.doAction(new ScryComplAction(event.getPlayer(), card, false));
            game.logAppendMessage(event.getPlayer(), event.getPlayer() + " puts back a card to the top of his or her library.");
        });

    private static final MagicEventAction BottomAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicCardList processedCards = new MagicCardList();
        event.processChosenCards(game, (final MagicCard card) -> {
            processedCards.add(card);
            game.doAction(new ScryComplAction(event.getPlayer(), card, true));
            game.logAppendMessage(event.getPlayer(), event.getPlayer() + " moves a card from top of his or her library to the bottom.");
        });
        final int X = event.getRefInt() - processedCards.size();
        final MagicCardList choiceList = event.getPlayer().getLibrary().getCardsFromTop(X);
        if (choiceList.size() > 1) {
            game.addFirstEvent(new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                new MagicFromCardListChoice(choiceList, choiceList.size(), "to be put on the top of your library"),
                TopAction,
                ""
            ));
        }
    };

    private static final MagicEventAction StartScrying = (final MagicGame game, final MagicEvent event) -> {
        final int X = event.getRefInt();
        final MagicCardList choiceList = event.getPlayer().getLibrary().getCardsFromTop(X);
        game.addFirstEvent(new MagicEvent(
            event.getSource(),
            event.getPlayer(),
            new MagicFromCardListChoice(choiceList, choiceList.size(), true, "to be put on the bottom of your library"),
            choiceList.size(),
            BottomAction,
            ""
        ));
    };
}
