package magic.card;

import magic.model.trigger.MagicRefugeLandTrigger;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicTrigger;

public class Sejiri_Refuge {
    public static final MagicTrigger T = new MagicTappedIntoPlayTrigger();
	public static final MagicTrigger T2 = new MagicRefugeLandTrigger();
}
