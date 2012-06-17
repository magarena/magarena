package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Tel_Jilad_Wolf {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            if (creature == permanent) {
                final MagicPermanentList plist = new MagicPermanentList();
                for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                    if (blocker.isArtifact() && blocker.isCreature()) {
                        plist.add(blocker);
                    }
                }
                if (!plist.isEmpty()) {
                    final int amount = permanent.getBlockingCreatures().size() * 3;
                    return new MagicEvent(
                            permanent,
                            permanent.getController(),
                            new Object[]{permanent,amount},
                            this,
                            permanent + " gets +" + amount + "/+" +  amount + " until end of turn.");
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
                    (Integer)data[1],
                    (Integer)data[1]));
        }
    };
}
