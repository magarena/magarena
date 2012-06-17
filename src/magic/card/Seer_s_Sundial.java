package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Seer_s_Sundial {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
            final MagicPlayer player = permanent.getController();
            return (player == played.getController() && played.isLand()) ?
                new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
                            "You may pay {2}.",
                            new MagicPayManaCostChoice(MagicManaCost.TWO)),
                    new Object[]{player},
                    this,
                    "You may$ pay {2}$. If you do, draw a card.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicDrawAction((MagicPlayer)data[0],1));
            }
        }        
    };
}
