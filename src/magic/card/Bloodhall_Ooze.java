package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Bloodhall_Ooze {
    public static final MagicAtUpkeepTrigger T1 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data &&
                    game.filterTargets(
                        player,
                        MagicTargetFilter.TARGET_BLACK_PERMANENT_YOU_CONTROL).size() > 0) ?
                    new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a +1/+1 counter on " + permanent,
                                MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ put a +1/+1 counter on " + permanent) :
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
                        MagicCounterType.PlusOne,
                        1,
                        true));
            }
        }        
    };
    
    public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data &&
                    game.filterTargets(
                        player,
                        MagicTargetFilter.TARGET_GREEN_PERMANENT_YOU_CONTROL).size() > 0) ?
                    new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                                player + " may put a +1/+1 counter on " + permanent,
                                MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{permanent},
                        this,
                        player + " may$ put a +1/+1 counter on " + permanent) :
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
                        MagicCounterType.PlusOne,
                        1,
                        true));
            }
        }        
    };
}
