package magic.model.trigger;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentList;
import magic.model.action.ChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class FlankingTrigger extends ThisBecomesBlockedByTrigger {

    private static final FlankingTrigger INSTANCE = new FlankingTrigger();

    private FlankingTrigger() {}

    public static FlankingTrigger create() {
        return INSTANCE;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
        return super.accept(permanent, blocker) && blocker.hasAbility(MagicAbility.Flanking) == false;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent blocker) {
        return new MagicEvent(
            permanent,
            blocker,
            this,
            "RN gets -1/-1 until end of turn."
        );
    }
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeTurnPTAction(event.getRefPermanent(),-1,-1));
    }
}
