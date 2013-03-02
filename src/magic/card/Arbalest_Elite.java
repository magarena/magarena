package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicSource;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicDamageTargetPicker;

public class Arbalest_Elite {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicConditionFactory.ManaCost("{2}{W}")},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.create("{2}{W}"))};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicTargetChoice.NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE,
                    new MagicDamageTargetPicker(3),
                    this,
                    "SN deals 3 damage to target attacking or blocking creature$. " +
                    "SN doesn't untap during your next untap step.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage=new MagicDamage(event.getPermanent(),creature,3);
                    game.doAction(new MagicDealDamageAction(damage));
                    game.doAction(new MagicChangeStateAction(event.getPermanent(),MagicPermanentState.DoesNotUntapDuringNext,true));
                }
            });
        }
    };
}
