package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPlayer;
import magic.model.action.MagicAddStaticAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;

public class Witherscale_Wurm {
    private static final MagicStatic AB = new MagicStatic(MagicLayer.Ability, MagicStatic.UntilEOT) {
        @Override
        public long getAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final long flags) {
            return flags | MagicAbility.Wither.getMask();
        }
    };
    
    public static final MagicWhenBecomesBlockedTrigger T1 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            if (permanent != attacker) {
                return MagicEvent.NONE;
            }
            final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
            return new MagicEvent(
                permanent,
                new Object[]{plist},
                this,
                plist.size() > 1 ?
                    "Blocking creatures gain wither until end of turn." :
                    plist.get(0) + " gains wither until end of turn."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanentList plist = (MagicPermanentList)data[0]; 
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicAddStaticAction(blocker,AB));
            }
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
            final MagicPermanent attacker = permanent.getBlockedCreature();
            return (permanent == blocker && attacker.isValid()) ?
                new MagicEvent(
                    permanent,
                    new Object[]{attacker},
                    this,
                    attacker + " gains wither until end of turn."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicAddStaticAction((MagicPermanent)data[0],AB));
        }
    };
    
    public static final MagicWhenDamageIsDealtTrigger T3 = new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            return (damage.getSource() == permanent &&
                    damage.getTarget() == player.getOpponent() &&
                    permanent.getCounters(MagicCounterType.MinusOne) > 0) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Remove all -1/-1 counters from SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanent permanent = event.getPermanent();
            final int amount = permanent.getCounters(MagicCounterType.MinusOne);
            game.doAction(new MagicChangeCountersAction(
                    permanent,
                    MagicCounterType.MinusOne,
                    -amount,
                    true));
        }
    };
}
