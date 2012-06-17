package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Ajani_s_Mantra {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
            final MagicPlayer player = permanent.getController();
            return (player == data) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new MagicSimpleMayChoice(
                                    player + " may gain 1 life.",
                                    MagicSimpleMayChoice.GAIN_LIFE,
                                    1,
                                    MagicSimpleMayChoice.DEFAULT_YES),
                            new Object[]{player},
                            this,
                            player + " may$ gain 1 life.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
            }
        }        
    };
}
