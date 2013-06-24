package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicFadeVanishCounterTrigger extends MagicAtUpkeepTrigger {

    private final String counterType;

    public MagicFadeVanishCounterTrigger(final String counterType) {
        this.counterType = counterType;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return permanent.isController(upkeepPlayer);
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        boolean sacrifice = false;
        final int amount = permanent.getCounters(MagicCounterType.Charge);
        if (counterType == "fade") {
            sacrifice = amount == 0;
        } else if (amount == 1){
            sacrifice = true;
            game.doAction(new MagicChangeCountersAction(permanent,MagicCounterType.Charge,-1,true));
        }
        return sacrifice ?
            new MagicEvent(
                permanent,
                SAC_PERM,
                "PN sacrifices SN."
            ):
            new MagicEvent(
                permanent,
                REMOVE_COUNTER,
                "PN removes a " + counterType + " counter from SN."
            );
    }

    private static final MagicEventAction SAC_PERM = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };

    private static final MagicEventAction REMOVE_COUNTER = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Charge,
                -1,
                true
            ));
        }
    };
}
