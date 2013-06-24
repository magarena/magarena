package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.MagicCounterType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicSimpleMayChoice;

public abstract class MagicLandfallTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicLandfallTrigger(final int priority) {
        super(priority);
    }

    public MagicLandfallTrigger() {}

    protected abstract MagicEvent getEvent(final MagicPermanent permanent);

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
        return (played.isFriend(permanent) && played.isLand()) ?
            getEvent(permanent) :
            MagicEvent.NONE;
    }

    public static final MagicLandfallTrigger Quest = new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                    1,
                    MagicSimpleMayChoice.DEFAULT_YES
                ),
                this,
                "PN may$ put a quest counter on SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Charge,
                    1,
                    true
                ));
            }
        }
    };
}
