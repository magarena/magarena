package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicMessage;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.AIRevealAction;
import magic.model.action.ShiftCardAction;
import magic.model.action.ShuffleLibraryAction;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicGraveyardTargetPicker;

public class MagicSearchToLocationEvent extends MagicEvent {

    public MagicSearchToLocationEvent(final MagicEvent event, final MagicChoice choice, final MagicLocationType toLocation) {
        this(event.getSource(), event.getPlayer(), choice, toLocation, true);
    }

    public MagicSearchToLocationEvent(final MagicEvent event, final MagicChoice choice, final MagicLocationType toLocation, final boolean reveal) {
        this(event.getSource(), event.getPlayer(), choice, toLocation, reveal);
    }

    public MagicSearchToLocationEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final MagicLocationType toLocation) {
        this(source, player, choice, toLocation, true);
    }

    public MagicSearchToLocationEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final MagicLocationType toLocation, final boolean reveal) {
        super(
            source,
            player,
            choice,
            MagicGraveyardTargetPicker.ReturnToHand,
            toLocation.ordinal(),
            reveal ? ACTION_WITH_REVEAL : ACTION_WITHOUT_REVEAL,
            ""
        );
    }

    private static final MagicEventAction ACTION_WITH_REVEAL = genEventAction(true);
    private static final MagicEventAction ACTION_WITHOUT_REVEAL = genEventAction(false);

    private static final MagicEventAction genEventAction(final boolean revealed) {
        return (final MagicGame game, final MagicEvent event) -> {
            final MagicLocationType toLocation = MagicLocationType.values()[event.getRefInt()];

            // choice could be MagicMayChoice or MagicTargetChoice or MagicFromCardListChoice
            if (event.isNo()) {
                // do nothing
            } else if (event.getChosen()[0] instanceof MagicCardChoiceResult) {
                game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                event.processChosenCards(game, (final MagicCard card) -> {
                    if (revealed) {
                        game.logAppendMessage(
                            event.getPlayer(),
                            MagicMessage.format("Found (%s).", card)
                        );
                        game.doAction(new AIRevealAction(card));
                    }
                    game.doAction(new ShiftCardAction(card,MagicLocationType.OwnersLibrary, toLocation));
                });
            } else {
                game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                event.processTargetCard(game, (final MagicCard card) -> {
                    if (revealed) {
                        game.logAppendMessage(
                            event.getPlayer(),
                            MagicMessage.format("Found (%s).", card)
                        );
                        game.doAction(new AIRevealAction(card));
                    }
                    game.doAction(new ShiftCardAction(card, MagicLocationType.OwnersLibrary, toLocation));
                });
            }
        };
    }
}
