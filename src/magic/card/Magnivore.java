package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.mstatic.MagicCDA;
import magic.model.target.MagicTargetFilter;

public class Magnivore {
    public static final MagicCDA CDA = new MagicCDA() {
        @Override
        public void modPowerToughness(
                final MagicGame game,
                final MagicPlayer player,
                final MagicPowerToughness pt) {
            final int size = game.filterCards(
                    player,
                    MagicTargetFilter.TARGET_SORCERY_CARD_FROM_ALL_GRAVEYARDS).size();
            pt.set(size, size);
        }
    };
}
