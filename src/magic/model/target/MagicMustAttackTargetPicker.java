package magic.model.target;

import magic.model.*;

public class MagicMustAttackTargetPicker extends MagicTargetPicker {

	private static final MagicTargetPicker INSTANCE=new MagicMustAttackTargetPicker();

	private MagicMustAttackTargetPicker() {
		
	}

	@Override
	protected int getTargetScore(final MagicGame game,final MagicPlayer player,final Object target) {

		final MagicPermanent permanent=(MagicPermanent)target;
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