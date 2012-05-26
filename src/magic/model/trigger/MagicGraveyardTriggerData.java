package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicLocationType;

public class MagicGraveyardTriggerData {

	public final MagicCard card;
	public final MagicLocationType fromLocation;
	
	public MagicGraveyardTriggerData(final MagicCard card,final MagicLocationType fromLocation) {
		this.card=card;
		this.fromLocation=fromLocation;
	}
}
