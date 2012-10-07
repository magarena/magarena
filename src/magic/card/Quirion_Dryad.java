package magic.card;

import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenOtherSpellIsCastTrigger;

public class Quirion_Dryad {
    public static final MagicWhenOtherSpellIsCastTrigger T = new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            final int color = cardOnStack.getColorFlags();
            return (permanent.isFriend(cardOnStack) &&
                    (MagicColor.White.hasColor(color) ||
                     MagicColor.Blue.hasColor(color)  ||
                     MagicColor.Black.hasColor(color) ||
                     MagicColor.Red.hasColor(color))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
        }
    };
}
