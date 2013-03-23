package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLifeChangeTriggerData;
import magic.model.trigger.MagicWhenLifeIsGainedTrigger;

public class Kavu_Predator {
    public static final MagicWhenLifeIsGainedTrigger T = new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            final MagicPlayer player = permanent.getController();
            final int amount = lifeChange.amount;
            return (player.getOpponent() == lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    player,
                    amount,
                    this,
                    amount > 1 ?
                        "Put " + amount + " +1/+1 counters on SN." :
                        "Put a +1/+1 counter on SN.") :
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
                    true));
        }
    };
}
