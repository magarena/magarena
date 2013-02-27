package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;

public class Rootwalla {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{
                    MagicCondition.ABILITY_ONCE_CONDITION,
                    MagicConditionFactory.ManaCost("{1}{G}")},
            new MagicActivationHints(MagicTiming.Pump),
            "Pump") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.create("{1}{G}")),
                new MagicPlayAbilityEvent(source)};
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "SN gets +2/+2 until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicChangeTurnPTAction(permanent,2,2));
        }
    };
}
