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
import magic.model.event.MagicPermanentActivation;

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

    public static MagicCondition HandAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getHandSize() >= n;
            }
        };
    }

    public static MagicCondition LifeAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLife() >= n;
            }
        };
    }

    public static MagicCondition SingleActivation(final MagicPermanentActivation act) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                final MagicGame game = source.getGame();
                return !game.getStack().hasActivationOnTop(source,act);
            }
        };
    }
}
