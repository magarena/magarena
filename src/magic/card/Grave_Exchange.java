package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Grave_Exchange {
    public static final MagicSpellCardEvent S =new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                new MagicGraveyardTargetPicker(false),
                this,
                "Return target creature card$ from your graveyard to your hand."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(
                        targetCard,
                        MagicLocationType.Graveyard
                    ));
                    game.doAction(new MagicMoveCardAction(
                        targetCard,
                        MagicLocationType.Graveyard,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
            game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(new MagicEvent(
                event.getSource(),
                MagicTargetChoice.NEG_TARGET_PLAYER,
                EVENT_ACTION,
                "Target player$ sacrifices a creature."
            ))));
        }
    };
    
    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    game.addEvent(new MagicSacrificePermanentEvent(
                        event.getSource(),
                        opponent,
                        MagicTargetChoice.SACRIFICE_CREATURE
                    ));
                }
            });
        }
    };
}
