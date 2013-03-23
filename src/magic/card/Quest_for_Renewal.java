package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

import java.util.Collection;

public class Quest_for_Renewal {
    public static final MagicWhenBecomesTappedTrigger T1 = new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            return (tapped.isFriend(permanent) && 
                    tapped.isCreature()) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ put a quest counter on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    1,
                    true
                ));
            }
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return (permanent.isOpponent(upkeepPlayer) &&
                    permanent.getCounters(MagicCounterType.Charge) >= 4) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Untap all creatures you control."):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final Collection<MagicPermanent> targets =
                game.filterPermanents(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicUntapAction(target));
            }
        }
    };
}
