package magic.model.target;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;

public class MagicMustAttackTargetPicker extends MagicTargetPicker<MagicPermanent> {

	private static final MagicTargetPicker INSTANCE=new MagicMustAttackTargetPicker();

	private MagicMustAttackTargetPicker() {}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final MagicPermanent permanent) {
		if (!permanent.canAttack(game)) {
			return -100;
		}		
		if (permanent.hasAbility(game,MagicAbility.AttacksEachTurnIfAble)) {
			return -50;
		}
		final MagicPowerToughness pt=permanent.getPowerToughness(game);
		return 50-pt.power*2-pt.toughness+permanent.getDamage();
	}
	
	public static MagicTargetPicker getInstance() {
		return INSTANCE;
	}
}
