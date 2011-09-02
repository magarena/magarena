package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class MagicBattleCryTrigger extends MagicWhenAttacksTrigger {

	private static final MagicTrigger INSTANCE = new MagicBattleCryTrigger();

    private MagicBattleCryTrigger() {}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
        final MagicPlayer player = permanent.getController();
		return (permanent==data) ?
            new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,player},
                    this,
                    "Each other attacking creature gets +1/+0 until end of turn."):
            null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
		final MagicPermanent permanent=(MagicPermanent)data[0];
		final Collection<MagicTarget> targets=
            game.filterTargets((MagicPlayer)data[1],MagicTargetFilter.TARGET_ATTACKING_CREATURE);
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
