package magic.model.trigger;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

public class MagicBattleCryTrigger extends MagicTrigger {

	private static final MagicTrigger INSTANCE=new MagicBattleCryTrigger();
	
	private MagicBattleCryTrigger() {
	
		super(MagicTriggerType.WhenAttacks);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

		if (permanent==data) {
			final MagicPlayer player=permanent.getController();
			return new MagicEvent(permanent,player,new Object[]{permanent,player},this,"Each other attacking creature gets +1/+0 until end of turn.");
		}
		return null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

		final MagicPermanent permanent=(MagicPermanent)data[0];
		final Collection<MagicTarget> targets=game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_ATTACKING_CREATURE);
		for (final MagicTarget target : targets) {

			final MagicPermanent creature=(MagicPermanent)target;
			if (creature!=permanent&&creature.isAttacking()) {
				game.doAction(new MagicChangeTurnPTAction(creature,1,0));
			}
		}
	}
	
	public static MagicTrigger getInstance() {
		
		return INSTANCE;
	}
}