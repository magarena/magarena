package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;

public class MagicExileCardEvent extends MagicEvent {

    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
        	event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
        		public void doAction(final MagicCard card) {
        			game.doAction(new MagicRemoveCardAction(
                    		card,
                    		MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(
                    		card,
                    		MagicLocationType.Graveyard,
                    		MagicLocationType.Exile));
                }
            });
        }
    };

    public MagicExileCardEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicTargetChoice targetChoice) {
        super(
            source,
            player,
            targetChoice,
            MagicEvent.NO_DATA,
            EVENT_ACTION,
            "Choose " + targetChoice.getTargetDescription() + "$."
        );
    }
}
