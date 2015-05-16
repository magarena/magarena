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
    
    public static MagicAmount Equipment = 
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getEquipmentPermanents().size();
            }
        };
    
    public static MagicAmount Aura = 
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getAuraPermanents().size();
            }
        };
    
    public static MagicAmount Domain = 
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                return source.getController().getDomain();
            }
        };
    
    public static MagicAmount SN_Power = 
        new MagicAmount() {
            @Override
            public int getAmount(final MagicSource source) {
                final MagicPermanent perm = (MagicPermanent)source;
                return perm.getPower();
            }
        };
}
