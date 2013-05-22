package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;

public class Graveyard_Shovel {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{2}")},
            new MagicActivationHints(MagicTiming.Main),
            "Exile") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(source,"{2}")};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_PLAYER,
                this,
                "Target player$ exiles a card from his or her graveyard. " +
                "If it's a creature card, PN gains 2 life."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                    if (targetPlayer.getGraveyard().size() > 0) {
                        final MagicPlayer player = event.getPlayer();
                        game.addEvent(new MagicEvent(
                            event.getSource(),
                            targetPlayer,
                            MagicTargetChoice.TARGET_CARD_FROM_GRAVEYARD,
                            new MagicGraveyardTargetPicker(true),
                            player,
                            EVENT_ACTION,
                            "PN exiles a card$ from his or her graveyard."
                        ));
                    }
                }
            });
        }
        private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
            @Override
            public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                        game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
                        if (card.getCardDefinition().isCreature()) {
                            game.doAction(new MagicChangeLifeAction(event.getRefPlayer(),2));
                        }
                    }
                });
            }
        };
    };
}
