package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.MagicPermanentState;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Phyrexian_Slayer {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPermanentList plist = new MagicPermanentList();
                for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                    final int colorFlags = blocker.getColorFlags();
                    if (MagicColor.White.hasColor(colorFlags)) {
                        plist.add(blocker);
                    }
                }
                if (!plist.isEmpty()) {
                    return new MagicEvent(
                            permanent,
                            permanent.getController(),
                            new Object[]{plist},
                            this,
                            plist.size() > 1 ?
                                    "Destroy blocking white creatures. They can't be regenerated." :
                                    "Destroy " + plist.get(0) + ". It can't be regenerated.");
                }
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
                game.doAction(new MagicChangeStateAction(blocker,MagicPermanentState.CannotBeRegenerated,true));
                game.doAction(new MagicDestroyAction(blocker));
            }
        }
    };
}
