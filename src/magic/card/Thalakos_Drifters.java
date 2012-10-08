package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicSetAbilityAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPlayAbilityEvent;
import magic.model.event.MagicTiming;

public class Thalakos_Drifters {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicCondition.HAS_CARD_CONDITION},
            new MagicActivationHints(MagicTiming.Pump,false,1),
            "Shadow") {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicDiscardEvent(source,source.getController(),1,false),
                new MagicPlayAbilityEvent(source)
            };
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "SN gains shadow until end of turn.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSetAbilityAction(event.getPermanent(),MagicAbility.Shadow));
        }
    };
}
