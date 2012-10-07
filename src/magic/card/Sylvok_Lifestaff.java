package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Sylvok_Lifestaff {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEquippedCreature()==died) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN gains 3 life."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),3));
        }
    };
}
