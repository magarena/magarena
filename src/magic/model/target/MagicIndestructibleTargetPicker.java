package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;

public class MagicIndestructibleTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTargetPicker INSTANCE=new MagicIndestructibleTargetPicker();
	
	private MagicIndestructibleTargetPicker() {}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		if (permanent.hasAbility(game,MagicAbility.Indestructible)) {
			return 0;
		}
		return 100+permanent.getPower(game)*2+permanent.getDamage()-permanent.getToughness(game);
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
