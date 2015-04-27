package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MoveCardAction;
import magic.model.action.RemoveCardAction;
import magic.model.action.ShuffleLibraryAction;
import magic.model.action.AIRevealAction;
import magic.model.choice.MagicChoice;
import magic.model.choice.MagicCardChoiceResult;
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
    
    @Override
    public void onAddEvent(final MagicGame game) {
        // reveal library so that search can choose the actual cards
        game.doAction(new AIRevealAction(getPlayer().getLibrary()));
    }

    private static final MagicEventAction EventAction = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            // choice could be MagicMayChoice or MagicTargetChoice or MagicFromCardListChoice
            if (event.isNo()) {
                // do nothing
            } else if (event.getChosen()[0] instanceof MagicCardChoiceResult) {
                game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                event.processChosenCards(game, new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.logAppendMessage(event.getPlayer(), "Found (" + card + ").");
                        game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        final MagicLocationType toLocation = MagicLocationType.values()[event.getRefInt()];
                        game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary, toLocation));
                    }
                });
            } else {
                game.doAction(new ShuffleLibraryAction(event.getPlayer()));
                event.processTargetCard(game, new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.logAppendMessage(event.getPlayer(), "Found (" + card + ").");
                        game.doAction(new RemoveCardAction(card,MagicLocationType.OwnersLibrary));
                        final MagicLocationType toLocation = MagicLocationType.values()[event.getRefInt()];
                        game.doAction(new MoveCardAction(card,MagicLocationType.OwnersLibrary, toLocation));
                    }
                });
            }
            // hide back all the cards in the library
            game.doAction(AIRevealAction.Hide(event.getPlayer().getLibrary()));
        }
    };
}
