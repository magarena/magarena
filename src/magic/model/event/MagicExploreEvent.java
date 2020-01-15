package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.action.ChangeStateAction;
import magic.model.action.RevealAction;
import magic.model.action.ShiftCardAction;
import magic.model.choice.MagicMayChoice;

public class MagicExploreEvent extends MagicEvent {

    public MagicExploreEvent(final MagicPermanent source) {
        super(
            source,
            EventAction,
            ""
        );
    }

    private static final MagicEventAction MoveCard = (final MagicGame game, final MagicEvent event) -> {
        if (event.isYes()) {
            game.doAction(new ShiftCardAction(event.getRefCard(), MagicLocationType.OwnersLibrary, MagicLocationType.Graveyard));
        }
    };

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicPermanent perm = event.getPermanent();
        final MagicPlayer player = event.getPlayer();
        for (final MagicCard top : player.getLibrary().getCardsFromTop(1)) {
            game.doAction(new RevealAction(top));
            if (top.isLand()) {
                game.doAction(new ShiftCardAction(top, MagicLocationType.OwnersLibrary, MagicLocationType.OwnersHand));
            } else {
                game.doAction(new ChangeCountersAction(player, perm, MagicCounterType.PlusOne, 1));
                game.addFirstEvent(new MagicEvent(
                    perm,
                    player,
                    new MagicMayChoice("Put " + top + " into your graveyard?"),
                    top,
                    MoveCard,
                    "PN may$ put RN into his or her graveyard."
                ));
            }
        }
        ChangeStateAction.trigger(game, perm, MagicPermanentState.Explores);
    };
}
