package magic.model.action;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;

// Set an ability until end of turn.
public class MagicSetAbilityAction extends MagicAction {

	private final MagicPermanent permanent;
	private final long flags;
	private long oldAbilityFlags;
	
	public MagicSetAbilityAction(final MagicPermanent permanent,final long flags) {
		this.permanent=permanent;
		this.flags=flags;
	}
	
	public MagicSetAbilityAction(final MagicPermanent permanent,final MagicAbility ability) {
		this(permanent,ability.getMask());
	}
		
	@Override
	public void doAction(final MagicGame game) {
		oldAbilityFlags=permanent.getTurnAbilityFlags();
		permanent.setTurnAbilityFlags(oldAbilityFlags|flags);
	}

	@Override
	public void undoAction(final MagicGame game) {
		permanent.setTurnAbilityFlags(oldAbilityFlags);
	}
}
