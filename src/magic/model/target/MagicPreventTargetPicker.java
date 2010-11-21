package magic.model.target;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

/** Creature permanent or player. */
public class MagicPreventTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicPreventTargetPicker();
	
	private MagicPreventTargetPicker() {
		
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		if (target==player) {
			return 15-player.getLife();
		} 
		final MagicPermanent permanent=(MagicPermanent)target;		
		return 10-permanent.getToughness(game)+permanent.getDamage()-permanent.getPreventDamage();
	}
	
	public static MagicTargetPicker getInstance() {
		
		return INSTANCE;
	}
}