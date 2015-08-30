package magic.model.trigger;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicFlankingTrigger extends MagicWhenBecomesBlockedTrigger {

    private static final MagicFlankingTrigger INSTANCE = new MagicFlankingTrigger();

    private MagicFlankingTrigger() {}

    public static MagicFlankingTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocked) {
        if (permanent == blocked) {
            final MagicPermanentList plist = new MagicPermanentList();
            for (final MagicPermanent blocker : permanent.getBlockingCreatures()) {
                if (!blocker.hasAbility(MagicAbility.Flanking)) {
                    plist.add(blocker);
                }
            }
            if (!plist.isEmpty()) {
                return new MagicEvent(
                    permanent,
                    plist,
                    this,
                    plist.size() > 1 ?
                        "Blocking creatures get -1/-1 until end of turn." :
                        plist.get(0) + " gets -1/-1 until end of turn."
                );
            }
        }
        return MagicEvent.NONE;
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        for (final MagicPermanent blocker : event.getRefPermanentList()) {
            game.doAction(new ChangeTurnPTAction(blocker,-1,-1));
        }
    }
}
