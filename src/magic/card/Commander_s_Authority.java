package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Commander_s_Authority {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer data) {
            final MagicPermanent enchanted = permanent.getEnchantedCreature();
            final MagicPlayer controller = enchanted.getController();
            return (controller == data) ?
                new MagicEvent(
                        enchanted,
                        controller,
                        new Object[]{controller},
                        this,
                        controller + " puts a 1/1 white Human " +
                        "creature token onto the battlefield.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicPlayTokenAction(
                    (MagicPlayer)data[0],
                    TokenCardDefinitions.get("Human1")));
        }
    };
}
