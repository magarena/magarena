package magic.model.action;

import magic.model.MagicLocationType;
import magic.model.MagicPermanent;

public class MagicSacrificeAction extends MagicRemoveFromPlayAction {

	public MagicSacrificeAction(final MagicPermanent permanent) {
		
		super(permanent,MagicLocationType.Graveyard);
	}
}