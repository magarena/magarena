package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;

public class Wildwood_Geist {
    public static final MagicStatic S = new MagicStatic(MagicLayer.ModPT) {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            if (game.hasTurn(permanent.getController())) {
                pt.add(2,2);
            }
        }
    };
}
