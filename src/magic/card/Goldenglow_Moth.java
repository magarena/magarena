package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Goldenglow_Moth {
    public static final MagicWhenBlocksTrigger T = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (creature == permanent) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new MagicSimpleMayChoice(
                                player + " may gain 4 life.",
                                MagicSimpleMayChoice.GAIN_LIFE,
                                3,
                                MagicSimpleMayChoice.DEFAULT_YES),
                            this,
                            player + " may$ gain 4 life.") :
                    MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),4));
            }
        }
    };
}
