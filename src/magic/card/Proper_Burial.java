package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Proper_Burial {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final int toughness = otherPermanent.getToughness();
            return (otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent) &&
                    toughness > 0) ?
                new MagicEvent(
                    permanent,
                    toughness,
                    this,
                    "PN gains " + toughness + " life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(
                    event.getPlayer(),
                    event.getRefInt()));
        }
    };
}
