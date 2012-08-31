package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Kavu_Predator {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object[] data) {
            final MagicPlayer player = permanent.getController();
            final int amount = (Integer)data[1];
            return (player.getOpponent() == (MagicPlayer)data[0]) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{amount},
                    this,
                    amount > 1 ?
                        "Put " + amount + " +1/+1 counters on " + permanent + "." :
                        "Put a +1/+1 counter on " + permanent + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.PlusOne,
                    (Integer)data[0],
                    true));
        }
    };
}
