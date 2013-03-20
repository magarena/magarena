package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.event.MagicUntapEvent;
import magic.model.target.MagicDamageTargetPicker;
import magic.model.target.MagicTarget;

public class Hateflayer {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.CAN_UNTAP_CONDITION,MagicConditionFactory.ManaCost("{2}{R}")},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{2}{R}")),
                new MagicUntapEvent(source)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(5),
                    this,
                    "SN deals damage equal to its power to target creature or player$.");
        }

        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choiceResults) {
            event.processTarget(game,choiceResults,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicPermanent permanent=event.getPermanent();
                    final MagicDamage damage=new MagicDamage(permanent,target,permanent.getPower());
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    };
}
