package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Tidewalker {
    public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
            final int amount = permanent.getCounters(MagicCounterType.Charge);
            pt.set(amount,amount);
        }
    };
    
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            final int amount = game.filterPermanents(player,MagicTargetFilter.TARGET_ISLAND_YOU_CONTROL).size();
            return (amount > 0) ?
                new MagicEvent(
                    permanent,
                    player,
                    amount,
                    this,
                    amount > 1 ?
                        "SN enters the battlefield with " + amount + " time counters on it." :
                        "SN enters the battlefield with a time counter on it.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    event.getRefInt(),
                    true));
        }
        @Override
        public boolean usesStack() {
            return false;
        }
    };
}
