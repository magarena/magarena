package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.action.SacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicFadeVanishCounterTrigger extends MagicAtUpkeepTrigger {

    private final MagicCounterType counterType;

    public static final MagicFadeVanishCounterTrigger Fade = new MagicFadeVanishCounterTrigger(MagicCounterType.Fade);
    public static final MagicFadeVanishCounterTrigger Time = new MagicFadeVanishCounterTrigger(MagicCounterType.Time);

    private MagicFadeVanishCounterTrigger(final MagicCounterType counterType) {
        this.counterType = counterType;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return permanent.isController(upkeepPlayer);
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        boolean sacrifice = false;
        final int amount = permanent.getCounters(counterType);
        if (counterType == MagicCounterType.Fade) {
            sacrifice = amount == 0;
            return sacrifice ?
                new MagicEvent(
                        permanent,
                        SAC_PERM,
                        "PN sacrifices SN."
                    ):
                new MagicEvent(
                        permanent,
                        REMOVE_FADE_COUNTER,
                        "PN removes a fade counter from SN."
                    );
        } else if (counterType == MagicCounterType.Time){
            sacrifice = amount == 1;
            return sacrifice ?
                new MagicEvent(
                        permanent,
                        REMOVE_AND_SAC,
                        "PN removes a time counter from SN. PN sacrifices SN."
                    ):
                new MagicEvent(
                        permanent,
                        REMOVE_TIME_COUNTER,
                        "PN removes a time counter from SN."
                    );
        }
        return MagicEvent.NONE;
    }

    private static final MagicEventAction SAC_PERM = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };

    private static final MagicEventAction REMOVE_TIME_COUNTER = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Time,
                -1
            ));
        }
    };
    
    private static final MagicEventAction REMOVE_AND_SAC = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Time,
                -1
            ));
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };
    
    private static final MagicEventAction REMOVE_FADE_COUNTER = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Fade,
                -1
            ));
        }
    };
}
