package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;


public class Wurm_s_Tooth {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return cardOnStack.hasColor(MagicColor.Green) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.GAIN_LIFE,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may$ gain 1 life.") :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
            }
        }
    };
}
