package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Drifter_il_Dal {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicMayChoice(
                                "You may pay {U}.",
                                new MagicPayManaCostChoice(MagicManaCost.BLUE)),
                            new Object[]{permanent},
                            this,
                            "You may$ pay {U}$. If you don't, sacrifice " + permanent + ".") :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isNoChoice(choiceResults[0])) {
                game.doAction(new MagicSacrificeAction((MagicPermanent)data[0]));
            }            
        }
    };
}
