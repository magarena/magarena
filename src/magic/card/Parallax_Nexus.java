package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicExileUntilThisLeavesPlayAction;
import magic.model.action.MagicReturnExiledUntilThisLeavesPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Parallax_Nexus {
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicExileUntilThisLeavesPlayAction(
                        event.getPermanent(),
                        card,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    };

    public static final MagicPermanentActivation A = new MagicPermanentActivation(
        new MagicCondition[]{
            MagicCondition.SORCERY_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(1)
        },
        new MagicActivationHints(MagicTiming.Main),
        "Exile"
    ) {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicRemoveCounterEvent(source,MagicCounterType.Charge,1)
            };
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_OPPONENT,
                this,
                "Target opponent$ exiles a card from his or her hand."
            );
        }
    
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game,new MagicPlayerAction() {
                public void doAction(final MagicPlayer player) {
                    game.addEvent(new MagicEvent(
                        event.getSource(),
                        player,
                        MagicTargetChoice.TARGET_CARD_FROM_HAND,
                        EVENT_ACTION,
                        "PN exiles a card from his or her hand."
                    ));
                }
            });
        }
    };
    
    public static final MagicWhenLeavesPlayTrigger T3 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent left) {
            if (permanent == left && !permanent.getExiledCards().isEmpty()) {
                final MagicCardList clist = new MagicCardList(permanent.getExiledCards());
                return new MagicEvent(
                    permanent,
                    this,
                    clist.size() == 1 ?
                        "Return " + clist.get(0) + " to its owner's hand" :
                        "Return exiled cards to their owner's hand"
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new MagicReturnExiledUntilThisLeavesPlayAction(permanent,MagicLocationType.OwnersHand));
        }
    };
}
