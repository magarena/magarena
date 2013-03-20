package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.action.MagicCardOnStackAction;
import magic.model.action.MagicCopyCardOnStackAction;
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
import magic.model.stack.MagicCardOnStack;

public class Echo_Mage {
    public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=4) {
                pt.set(2,5);
            } else if (charges>=2) {
                pt.set(2,4);
            }
        }        
    };
    
    public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
            new MagicCondition[]{
                MagicConditionFactory.ChargeCountersAtLeast(2),
                MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{U}{U}")},
            new MagicActivationHints(MagicTiming.Spell),
            "Copy") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{U}{U}"))};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final int amount=source.getCounters(MagicCounterType.Charge)>=4?2:1;
            final String description = amount == 2 ?
                    "Copy target instant or sorcery spell$ twice. You may choose new targets for the copies.":
                    "Copy target instant or sorcery spell$. You may choose new targets for the copy.";
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_INSTANT_OR_SORCERY_SPELL,
                amount,
                this,
                description
            );
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetCardOnStack(game,choiceResults,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    final MagicPlayer player = event.getPlayer();
                    final int amount=event.getRefInt();
                    for (int count=amount;count>0;count--) {
                        game.doAction(new MagicCopyCardOnStackAction(player,targetSpell));
                    }
                }
            });
        }
    };
}
