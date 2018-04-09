package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicPumpTargetPicker;

public class ModularTrigger extends ThisDiesTrigger {

    private static final ModularTrigger INSTANCE = new ModularTrigger();

    private ModularTrigger() {}

    public static ModularTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
        final int amount = permanent.getCounters(MagicCounterType.PlusOne);
        return new MagicEvent(
            permanent,
            new MagicMayChoice(
                MagicTargetChoice.POS_TARGET_ARTIFACT_CREATURE
            ),
            MagicPumpTargetPicker.create(),
            amount,
            this,
            "PN may$ put RN +1/+1 counters on target artifact creature$."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            event.processTargetPermanent(game, (final MagicPermanent creature) ->
                game.doAction(new ChangeCountersAction(
                    event.getSource(),
                    creature,
                    MagicCounterType.PlusOne,
                    event.getRefInt()
                ))
            );
        }
    }
}
