package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLandfallTrigger;

public class Grazing_Gladehart {
    public static final MagicLandfallTrigger T = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicPlayer player = permanent.getController();
            return new MagicEvent(
                    permanent,
                    player,
                    new MagicSimpleMayChoice(
                            player + " may gain 2 life.",
                            MagicSimpleMayChoice.GAIN_LIFE,
                            2,
                            MagicSimpleMayChoice.DEFAULT_YES),
                    MagicEvent.NO_DATA,
                    this,
                    player + " may gain 2 life.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
            }
        }        
    };
}
