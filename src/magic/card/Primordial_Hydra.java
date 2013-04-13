package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicAtUpkeepTrigger;

import java.util.Set;

public class Primordial_Hydra {
    public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
        @Override
        public void modAbilityFlags(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final Set<MagicAbility> flags) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            if (amount >= 10) {
                flags.add(MagicAbility.Trample);
            }
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne);
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "Put " + amount + " +1/+1 counters on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt(),
                true
            ));
        }
    };
}
