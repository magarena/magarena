package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.event.MagicEvent;

public class Lightkeeper_of_Emeria {                 
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeLifeAction(player, 2 * permanent.getKicker()));
            }
            return MagicEvent.NONE;
        }
    };
}
