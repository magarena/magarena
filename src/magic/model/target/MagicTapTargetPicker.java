package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicTapTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private final boolean tap;
	private final boolean untap;
	
	public MagicTapTargetPicker(final boolean tap,final boolean untap) {
		this.tap=tap;
		this.untap=untap;
	}	
	
	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		final boolean tapped=permanent.isTapped();
		if ((tapped&&!untap)||(!tapped&&!tap)) {
			return 0;
		}		
		final boolean owner=permanent.getController()==player;
		if (owner==tapped) {
			return 1+permanent.getPower(game)+permanent.getActivations().size();			
		}
		return 0;
	}
}
