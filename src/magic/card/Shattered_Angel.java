package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicLandfallTrigger;

public class Shattered_Angel {
    public static final MagicLandfallTrigger T = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.GAIN_LIFE,
                    3,
                    MagicSimpleMayChoice.DEFAULT_YES),
                this,
                "PN may$ gain 3 life."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
            }
        }        
    };
}
