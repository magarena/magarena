package magic.model.event;

import java.util.List;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.action.RevealAction;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicFromCardListChoice;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;

public class MagicTutorTopEvent {

    private static final MagicEventAction TakeCard(final int reveal, final MagicLocationType rest) {
        return (final MagicGame game, final MagicEvent event) -> {
            if (reveal > 1) {
                game.doAction(new RevealAction(event.getRefCardList()));
            }
            event.processChosenCards(game, (final MagicCard chosen) -> {
                if (reveal == 1) {
                    game.doAction(new RevealAction(chosen));
                }
                game.doAction(new ShiftCardAction(
                    chosen,
                    MagicLocationType.OwnersLibrary,
                    MagicLocationType.OwnersHand
                ));
            });
            for (final MagicCard card : event.getRefCardList()) {
                game.doAction(new ShiftCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    rest
                ));
            }
        };
    }

    private static final MagicEventAction HandGraveyard = TakeCard(0, MagicLocationType.Graveyard);
    private static final MagicEventAction HandBottom = TakeCard(0, MagicLocationType.BottomOfOwnersLibrary);
    private static final MagicEventAction RevealHandBottom = TakeCard(1, MagicLocationType.BottomOfOwnersLibrary);
    private static final MagicEventAction RevealHandGraveyard = TakeCard(2, MagicLocationType.Graveyard);

    public static MagicEvent reveal(final MagicEvent event, final int n, final MagicTargetFilter<MagicCard> filter) {
        return create(event, n, filter, RevealHandGraveyard);
    }

    public static MagicEvent look(final MagicEvent event, final int n, final MagicTargetFilter<MagicCard> filter) {
        return create(event, n, filter, RevealHandBottom);
    }

    private static MagicEvent create(final MagicEvent event, final int n, final MagicTargetFilter<MagicCard> filter, final MagicEventAction action) {
        final MagicPlayer player = event.getPlayer();
        final MagicCardList topCards = player.getLibrary().getCardsFromTop(n);
        final List<MagicCard> choiceList = player.filterCards(topCards, filter);
        return new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(choiceList, topCards, 1, true),
            MagicGraveyardTargetPicker.ReturnToHand,
            topCards,
            action,
            ""
        );
    }

    public static MagicEvent toGraveyard(final MagicEvent event, final int n, final int h) {
        return create(event, n, h, HandGraveyard);
    }

    public static MagicEvent toBottom(final MagicEvent event, final int n, final int h) {
        return create(event, n, h, HandBottom);
    }

    private static MagicEvent create(final MagicEvent event, final int n, final int h, final MagicEventAction action) {
        final MagicPlayer player = event.getPlayer();
        final MagicCardList topCards = player.getLibrary().getCardsFromTop(n);
        return new MagicEvent(
            event.getSource(),
            player,
            new MagicFromCardListChoice(topCards, h),
            MagicGraveyardTargetPicker.ReturnToHand,
            topCards,
            action,
            ""
        );
    }
}
