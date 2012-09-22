package magic.card;

import java.util.Collection;
import java.util.EnumSet;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;


public class Aurification {
    public static final MagicWhenDamageIsDealtTrigger T1 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return (permanent.isController(damage.getTarget()) && 
                    damage.getSource().isCreature()) ?
                new MagicEvent(
                    permanent,
                    new Object[]{damage.getSource()},
                    this,
                    "PN puts a gold counter on " + damage.getSource() + "."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent creature = (MagicPermanent)data[0];
            game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.Gold,1,true));
            game.doAction(new MagicAddStaticAction(creature, new MagicStatic(MagicLayer.Ability) {
                @Override
                public long getAbilityFlags(
                        final MagicPermanent source,
                        final MagicPermanent permanent,
                        final long flags) {
                    return flags | MagicAbility.Defender.getMask();
                }
                @Override
                public boolean condition(
                        final MagicGame game,
                        final MagicPermanent source,
                        final MagicPermanent target) {
                    return target.getCounters(MagicCounterType.Gold) > 0;
                }
            }));
            game.doAction(new MagicAddStaticAction(creature, new MagicStatic(MagicLayer.Type) {
                @Override
                public void modSubTypeFlags(
                        final MagicPermanent permanent,
                        final EnumSet<MagicSubType> flags) {
                    flags.add(MagicSubType.Wall);
                }
                @Override
                public boolean condition(
                        final MagicGame game,
                        final MagicPermanent source,
                        final MagicPermanent target) {
                    return target.getCounters(MagicCounterType.Gold) > 0;
                }
            }));
        }
    };
    
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            return (permanent == left) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Remove all gold counters from all creatures."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final Collection<MagicTarget> targets =
                    game.filterTargets(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicTarget target : targets) {
                final MagicPermanent permanent = (MagicPermanent)target;
                final int amount = permanent.getCounters(MagicCounterType.Gold);
                if (amount > 0) {
                    game.doAction(new MagicChangeCountersAction(
                            permanent,
                            MagicCounterType.Gold,
                            -amount,
                            true));
                }
            }
        }
    };
}
