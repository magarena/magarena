package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Arctic_Nishoba {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
            return (permanent == data) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    player + " gains 2 life for each age counter on " + permanent + "."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Charge) * 2;
            game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    amount));
        }
    };
}
