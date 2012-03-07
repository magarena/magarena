package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicPlayerState;
import magic.model.MagicSource;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTriggerType;

public class MagicDealDamageAction extends MagicAction {

	private final MagicDamage damage;
	private MagicTarget target;
	private int oldDamage;
	private int oldPrevent;
    private final int UNINIT = Integer.MIN_VALUE;
	
	public MagicDealDamageAction(final MagicDamage damage) {
		this.damage=damage;
        oldDamage=UNINIT;
		oldPrevent=UNINIT;
	}

	private int preventDamage(final MagicGame game,int amount) {
		// Damage is not preventable.
		if (damage.isUnpreventable()) {
			return amount;
		}
		// Prevent all damage.
		if (target.getController().hasState(MagicPlayerState.PreventAllDamage)) {
			return 0;
		}
		// Prevent all damage by abilities when target is permanent.
		if (target.isPermanent()) {
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			if (targetPermanent.hasProtectionFrom(game,damage.getSource())) {
				return 0;
			}
		}
		// Prevent all combat damage.
		if (target.getController().hasState(MagicPlayerState.PreventAllCombatDamage) &&
				damage.isCombat()) {
			return 0;
		}
		// Prevent damage.
		final int prevent=target.getPreventDamage();
		if (prevent>0) {
			final int min=Math.min(amount,prevent);
			amount-=min;
			oldPrevent=prevent;
			target.setPreventDamage(prevent-min);
		}		
		return amount;
	}
	
	@Override
	public void doAction(final MagicGame game) {
		game.executeTrigger(MagicTriggerType.IfDamageWouldBeDealt,damage);
		damage.setDealtAmount(0);
		int dealtAmount=damage.getAmount();
		if (dealtAmount<=0) {
			return;
		}

		target=damage.getTarget();
		dealtAmount=preventDamage(game,dealtAmount);
		if (dealtAmount<=0) {
			return;
		}

		final MagicSource source=damage.getSource();
		if (target.isPermanent()) {
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			if (damage.hasNoRegeneration()) {
				game.doAction(new MagicChangeStateAction(targetPermanent,MagicPermanentState.CannotBeRegenerated,true));
			}
			if (source.hasAbility(game,MagicAbility.Wither)||source.hasAbility(game,MagicAbility.Infect)) {
				game.doAction(new MagicChangeCountersAction(targetPermanent,MagicCounterType.MinusOne,dealtAmount,true));
			} else {
				oldDamage=targetPermanent.getDamage();
				targetPermanent.setDamage(oldDamage+dealtAmount);
			}
			if (source.hasAbility(game,MagicAbility.Deathtouch)) {
				game.doAction(new MagicChangeStateAction(targetPermanent,MagicPermanentState.Destroyed,true));
			}
		} else if (target.isPlayer()) {
            final MagicPlayer targetPlayer = (MagicPlayer)target;
			if (source.hasAbility(game,MagicAbility.Infect)) {
				game.doAction(new MagicChangePoisonAction(targetPlayer,dealtAmount));
			} else {
				game.doAction(new MagicChangeLifeAction(targetPlayer,-dealtAmount));
			}
			game.doAction(new MagicChangePlayerStateAction(targetPlayer,MagicPlayerState.WasDealtDamage,true));
		}

		damage.setDealtAmount(dealtAmount);
		if (source.hasAbility(game,MagicAbility.LifeLink)) {
			game.doAction(new MagicChangeLifeAction(source.getController(),dealtAmount));
		}
		game.executeTrigger(MagicTriggerType.WhenDamageIsDealt,damage);
		
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {
		if (oldDamage!=UNINIT) {
			final MagicPermanent targetPermanent=(MagicPermanent)target;
			targetPermanent.setDamage(oldDamage);
		}
		if (oldPrevent!=UNINIT) {
			target.setPreventDamage(oldPrevent);
		}
	}
}
