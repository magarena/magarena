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
import java.util.Set;

public class Victory_s_Herald {
    public static final MagicWhenAttacksTrigger T1 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent attacker) {
            return (permanent==attacker) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Attacking creatures gain flying and lifelink until end of turn."
                ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {

            final Collection<MagicPermanent> targets = game.filterPermanents(
                    event.getPlayer(),
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicSetAbilityAction(
                    target,
                    MagicAbility.Flying,
                    MagicAbility.LifeLink
                ));
            }
        }        
    };
}
