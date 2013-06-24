package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicCounterType;
import magic.model.event.MagicEvent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicSimpleMayChoice;

public abstract class MagicAtUpkeepTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtUpkeepTrigger(final int priority) {
        super(priority);
    }

    public MagicAtUpkeepTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtUpkeep;
    }

    public static final MagicAtUpkeepTrigger MayCharge = new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ put a charge counter on SN."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Charge,1,true));
            }
        }
    };
}
