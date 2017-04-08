package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPumpTargetPicker;

public class MagicPutCounterEvent extends MagicEvent {

    public MagicPutCounterEvent(final MagicSource source, final MagicCounterType type, final int amount) {
        super(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            amount,
            EventActionTarget(type),
            "PN puts " + amount + " " + type.getName() + " counters on target creature$."
        );
    }

    public MagicPutCounterEvent(final MagicSource source, final int amount) {
        this(source, MagicCounterType.PlusOne, amount);
    }

    private static final MagicEventAction EventActionTarget(final MagicCounterType type) {
        return (final MagicGame game, final MagicEvent event) -> {
            event.processTargetPermanent(game, (final MagicPermanent creature) -> {
                game.doAction(new ChangeCountersAction(
                    creature,
                    type,
                    event.getRefInt()
                ));
            });
        };
    }

    public static final MagicEvent Self(final MagicSource source, final MagicCounterType type, final int amount) {
        return new MagicEvent(
            source,
            amount,
            EventAction(type),
            "PN puts " + amount + " " + type.getName() + " counters on SN."
        );
    }

    private static final MagicEventAction EventAction(final MagicCounterType type) {
        return (final MagicGame game, final MagicEvent event) -> {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                type,
                event.getRefInt()
            ));
        };
    }
}
