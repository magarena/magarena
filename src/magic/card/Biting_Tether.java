package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Biting_Tether {
    public static final Object S = Control_Magic.S;
    
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent.getEnchantedCreature()},
                    this,
                    player + " puts a -1/-1 counter on " +
                    permanent.getEnchantedCreature() + ".") :
            MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    (MagicPermanent)data[0],
                    MagicCounterType.MinusOne,1,true));            
        }
    };
}
