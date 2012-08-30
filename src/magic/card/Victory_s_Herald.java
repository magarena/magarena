package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSetAbilityAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.Collection;

public class Victory_s_Herald {
    
    private static final long VICTORYS_HERALD_FLAGS=
        MagicAbility.Flying.getMask() |
        MagicAbility.LifeLink.getMask();

    public static final MagicWhenAttacksTrigger T1 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent==data) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Attacking creatures gain flying and lifelink until end of turn."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {

            final Collection<MagicTarget> targets = game.filterTargets(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicSetAbilityAction(
                            (MagicPermanent)target,
                            VICTORYS_HERALD_FLAGS));
            }
        }        
    };
}
