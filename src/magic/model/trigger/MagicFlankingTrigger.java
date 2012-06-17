package magic.model.trigger;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicFlankingTrigger extends MagicWhenBecomesBlockedTrigger {

    private static final MagicFlankingTrigger INSTANCE = new MagicFlankingTrigger();

    private MagicFlankingTrigger() {}

    public static MagicFlankingTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
        if (data == permanent) {
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (!blocker.hasAbility(MagicAbility.Flanking)) {
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
                            "Blocking creatures get -1/-1 until end of turn." :
                            plist.get(0) + " gets -1/-1 until end of turn.");
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
            game.doAction(new MagicChangeTurnPTAction(blocker,-1,-1));
        }
    }
}

