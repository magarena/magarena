package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicDiscardCardAction;
import magic.model.choice.MagicCardChoice;
import magic.model.choice.MagicCardChoiceResult;
import magic.model.choice.MagicTargetChoice;

public class MagicReturnCardEvent extends MagicEvent {

    public MagicReturnCardEvent(final MagicSource source,final MagicPlayer player) {
        super(
            source,
            player,
            MagicTargetChoice.TARGET_CARD_FROM_HAND,
            EVENT_ACTION,
            "PN put a card$ from your hand on top of your library."
        );
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicDiscardCardAction(
                        event.getPlayer(),
                        card,
                        MagicLocationType.TopOfOwnersLibrary
                    ));
                }
            });
        }
    };
}
