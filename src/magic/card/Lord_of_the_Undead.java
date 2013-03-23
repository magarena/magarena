package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.target.MagicTargetFilter;

public class Lord_of_the_Undead {
    public static final MagicStatic S = new MagicStatic(
            MagicLayer.ModPT, 
            MagicTargetFilter.TARGET_ZOMBIE) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            pt.add(1,1);
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source != target;
        }
    };
        
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
        new MagicCondition[]{
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ManaCost("{1}{B}")
        },
        new MagicActivationHints(MagicTiming.FirstMain),
        "Return"
    ) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{1}{B}"))};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD,
                new MagicGraveyardTargetPicker(false),
                this,
                "Return target Zombie card$ from your graveyard to your hand."
            );
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetCard(game,new MagicCardAction() {
                public void doAction(final MagicCard card) {
                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                }
            });
        }
    };
}
