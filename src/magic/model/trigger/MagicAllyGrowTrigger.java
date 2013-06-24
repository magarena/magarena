package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;

public class MagicAllyGrowTrigger extends MagicWhenOtherComesIntoPlayTrigger {

    private static final MagicAllyGrowTrigger INSTANCE = new MagicAllyGrowTrigger();

    private MagicAllyGrowTrigger() {}

    public static final MagicAllyGrowTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPermanent otherPermanent) {
        return (otherPermanent.isFriend(permanent) &&
                otherPermanent.hasSubType(MagicSubType.Ally)) ?
            new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                    1,
                    MagicSimpleMayChoice.DEFAULT_YES
                ),
                this,
                "PN may$ put a +1/+1 counter on SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    }
}
