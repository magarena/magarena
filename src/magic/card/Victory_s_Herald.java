package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Victory_s_Herald {
	
    private static final long VICTORYS_HERALD_FLAGS=
		MagicAbility.Flying.getMask() |
        MagicAbility.LifeLink.getMask();

    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent data) {
			if (permanent==data) {
				final MagicPlayer player = permanent.getController();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Attacking creatures gain flying and lifelink until end of turn.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {

			final Collection<MagicTarget> targets = game.filterTargets(
                    (MagicPlayer)data[0],
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicSetAbilityAction(
                            (MagicPermanent)target,
                            VICTORYS_HERALD_FLAGS));
			}
		}		
    };
}
