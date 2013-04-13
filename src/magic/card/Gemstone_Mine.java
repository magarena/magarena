package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicRemoveCounterEvent;
import magic.model.event.MagicSacrificeEvent;
import magic.model.event.MagicTapEvent;

public class Gemstone_Mine {
    
    public static final MagicManaActivation MANA = new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        new MagicCondition[] {
            MagicCondition.CAN_TAP_CONDITION,
            MagicConditionFactory.ChargeCountersAtLeast(1)
        },
        2) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
            if (permanent.getCounters(MagicCounterType.Charge) == 1) {
                return new MagicEvent[]{
                    new MagicTapEvent(permanent),
                    new MagicRemoveCounterEvent(
                        permanent,
                        MagicCounterType.Charge,
                        1
                    ),
                    new MagicSacrificeEvent(permanent)
                };
            }
            return new MagicEvent[]{
                new MagicTapEvent(permanent),
                new MagicRemoveCounterEvent(
                    permanent,
                    MagicCounterType.Charge,
                    1
                )
            };
        }
    };
}
