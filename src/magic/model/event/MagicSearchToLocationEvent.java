package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicShuffleLibraryAction;
import magic.model.choice.MagicChoice;
import magic.model.target.MagicGraveyardTargetPicker;

public class MagicSearchToLocationEvent extends MagicEvent {
    
    public MagicSearchToLocationEvent(final MagicEvent event, final MagicChoice choice, final MagicLocationType toLocation) {
        this(event.getSource(), event.getPlayer(), choice, toLocation);
    }

    public MagicSearchToLocationEvent(final MagicSource source, final MagicPlayer player, final MagicChoice choice, final MagicLocationType toLocation) {
        super(
            source,
            player,
            choice,
            MagicGraveyardTargetPicker.ReturnToHand,
            toLocation.ordinal(),
            EventAction,
            ""
        );
    }

    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            // choice could be MagicMayChoice or MagicTargetChoice, the condition below takes care of both cases
            if (event.isNo() == false) {
                event.processTargetCard(game, new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.logAppendMessage(event.getPlayer(), "Found (" + card + ").");
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        game.doAction(new MagicShuffleLibraryAction(event.getPlayer()));
                        final MagicLocationType toLocation = MagicLocationType.values()[event.getRefInt()];
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.OwnersLibrary, toLocation));
                    }
                });
            }
        }
    };
}
