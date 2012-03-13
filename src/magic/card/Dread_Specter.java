package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Dread_Specter {
    public static final MagicWhenBecomesBlockedTrigger T1 = Deathgazer.T1;
    public static final MagicWhenBlocksTrigger T2 = Deathgazer.T2;
}
