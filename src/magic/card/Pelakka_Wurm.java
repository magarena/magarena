package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicDieDrawCardTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Pelakka_Wurm {
    public static final MagicDieDrawCardTrigger T2 = new MagicDieDrawCardTrigger(true);
}
