package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicCDA;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Sturmgeist {
    public static final MagicCDA CDA = new MagicCDA() {
        @Override
        public void modPowerToughness(final MagicGame game,final MagicPlayer player,final MagicPowerToughness pt) {
            final int amount = player.getHandSize();
            pt.set(amount,amount);
        }
    };
}
