package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Arcbound_Fiend {
    public static final MagicAtUpkeepTrigger T3 = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player=permanent.getController();
            return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                            player + " may move a +1/+1 counter from " +
                            "target creature onto " + permanent + ".",
                            MagicTargetChoice.NEG_TARGET_CREATURE_PLUSONE_COUNTER),
                        new Object[]{permanent},
                        this,
                        player + " may$ move a +1/+1 counter from " +
                        "target creature$ onto " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                event.processTargetPermanent(game,choiceResults,1,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        if (creature.getCounters(MagicCounterType.PlusOne) > 0) {
                            game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
                            game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,-1,true));
                        }
                    }
                });
            }
        }
    };
}
