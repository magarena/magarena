package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicCDA;

public class Rusting_Golem {
    public static final MagicCDA cda = new MagicCDA() {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPlayer player,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            pt.set(amount,amount);
        }
    };
}
