package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Nirkana_Cutthroat {
    public static final MagicStatic S1 = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=3) {
                pt.set(5,4);
            } else if (charges>=1) {
                pt.set(4,3);
            }
        }
    };

    public static final MagicStatic S2 = new MagicStatic(MagicLayer.Ability) {
        @Override
        public long getAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final long flags) {
            final int charges=permanent.getCounters(MagicCounterType.Charge);
            if (charges>=3) {
                return flags|MagicAbility.FirstStrike.getMask()|MagicAbility.Deathtouch.getMask();
            } else if (charges>=1) {
                return flags|MagicAbility.Deathtouch.getMask();
            } else {
                return flags;
            }
        }
    };
}
