package magic.card;

import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;
import magic.model.trigger.MagicWhenBlocksTrigger;

public class Amphibious_Kavu {
    public static final MagicWhenBecomesBlockedTrigger T1 = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPermanentList plist = permanent.getBlockingCreatures();
                boolean pump = false;
                for (final MagicPermanent blocker : plist) {
                    final int colorFlags = blocker.getColorFlags();
                    if (MagicColor.Blue.hasColor(colorFlags) ||
                        MagicColor.Black.hasColor(colorFlags)) {
                        pump = true;
                    }
                }
                if (pump) {
                    return new MagicEvent(
                            permanent,
                            permanent.getController(),
                            new Object[]{permanent},
                            this,
                            permanent + " gets +3/+3 until end of turn.");
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
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    3,
                    3));
        }
    };
    
    public static final MagicWhenBlocksTrigger T2 = new MagicWhenBlocksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked = permanent.getBlockedCreature();
            return (permanent == data &&
                    blocked.isValid() &&
                    (MagicColor.Blue.hasColor(blocked.getColorFlags()) ||
                    MagicColor.Black.hasColor(blocked.getColorFlags()))) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent},
                    this,
                    permanent + " gets +3/+3 until end of turn."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeTurnPTAction(
                    (MagicPermanent)data[0],
                    3,
                    3));
        }
    };
}
