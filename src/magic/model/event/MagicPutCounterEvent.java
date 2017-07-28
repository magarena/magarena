package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.MagicTuple;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPumpTargetPicker;

public class MagicPutCounterEvent extends MagicEvent {

    public MagicPutCounterEvent(final MagicSource source, final MagicCounterType type, final int amount) {
        super(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            new MagicTuple(amount, type),
            EventActionTarget,
            "PN puts " + amount + " " + type.getName() + " counters on target creature$."
        );
    }

    public MagicPutCounterEvent(final MagicSource source, final int amount) {
        this(source, MagicCounterType.PlusOne, amount);
    }

    private static final MagicEventAction EventActionTarget= (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent creature) -> {
            final MagicTuple tup = event.getRefTuple();
            game.doAction(new ChangeCountersAction(
                creature,
                tup.getCounterType(1),
                tup.getInt(0)
            ));
        });
    };

    public static final MagicEvent Self(final MagicSource source, final MagicCounterType type, final int amount) {
        return new MagicEvent(
            source,
            amount,
            EventAction,
            "PN puts " + amount + " " + type.getName() + " counters on SN."
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        game.doAction(new ChangeCountersAction(
            event.getPermanent(),
            tup.getCounterType(1),
            tup.getInt(0)
        ));
    };
}
