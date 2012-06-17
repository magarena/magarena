package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Elven_Warhounds {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPermanentList plist = new MagicPermanentList(permanent.getBlockingCreatures());
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{plist},
                        this,
                        plist.size() > 1 ?
                        "Put blocking creatures on top of their owner's library." :
                        "Put " + plist.get(0) + " on top of its owner's library.");
            }
            return MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicPermanentList plist = (MagicPermanentList)data[0];
            for (final MagicPermanent blocker : plist) {
                game.doAction(new MagicRemoveFromPlayAction(blocker,MagicLocationType.TopOfOwnersLibrary));
            }
        }
    };
}
