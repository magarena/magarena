package magic.model;

import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTarget;

public class MagicAmountFactory {

    public static MagicAmount FromFilter(final MagicTargetFilter<MagicTarget> filter) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                return filter.filter(source).size();
            }
        };
    }

    public static MagicAmount CounterOnSource(final MagicCounterType type) {
        return new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getCounters(type);
            }
        };
    }

    public static MagicAmount One = 
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                return 1;
            }
        };
}
