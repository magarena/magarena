package magic.card;

import java.util.Collection;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Quest_for_Renewal {
    public static final MagicWhenBecomesTappedTrigger T1 = new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (data.getController() == player && data.isCreature()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a quest counter on " + permanent + ".",
                                MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ put a quest counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeCountersAction(
                        (MagicPermanent)data[0],
                        MagicCounterType.Charge,
                        1,
                        true));
            }
        }
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player != data &&
                    permanent.getCounters(MagicCounterType.Charge) >= 4) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Untap all creatures you control."):
                MagicEvent.NONE;
        }    
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            final MagicPlayer player = (MagicPlayer)data[0];
            final Collection<MagicTarget> targets =
                game.filterTargets(player,MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
            for (final MagicTarget target : targets) {
                game.doAction(new MagicUntapAction((MagicPermanent)target));
            }
        }
    };
}
