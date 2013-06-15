package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicColor;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicCardDefinition;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.MagicManaCost;
import magic.model.choice.MagicTargetChoice;

public class MagicConditionFactory {
    public static MagicCondition ChargeCountersAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(MagicCounterType.Charge) >= n;
            }
        };
    }
    
    public static MagicCondition PlusOneCounterAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(MagicCounterType.PlusOne) >= n;
            }
        };
    }
    
    public static MagicCondition CounterAtLeast(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(counterType) >= n;
            }
        };
    }

    public static MagicCondition ManaCost(String manaCost) {
        return MagicManaCost.create(manaCost).getCondition();
    }
    
    
    public static MagicCondition HasOptions(final MagicTargetChoice targetChoice) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return targetChoice.hasOptions(source.getGame(), source.getController(), source, false);
            }
        };
    }
}
