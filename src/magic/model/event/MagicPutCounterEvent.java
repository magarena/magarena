package magic.model.event;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeCountersAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicPumpTargetPicker;

public class MagicPutCounterEvent extends MagicEvent {

    final MagicCounterType ctype;

    public MagicPutCounterEvent(final MagicSource source, final MagicCounterType type, final int amount) {
        super(
            source,
            MagicTargetChoice.POS_TARGET_CREATURE,
            MagicPumpTargetPicker.create(),
            amount,
            EventActionTarget,
            "PN puts " + amount + " " + type.getName() + " counters on target creature$."
        );
        ctype = type;
    }

    public MagicPutCounterEvent(final MagicSource source, final int amount) {
        this(source, MagicCounterType.PlusOne, amount);
    }

    private static final MagicEventAction EventActionTarget= (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent creature) -> {
            final MagicCounterType type = ((MagicPutCounterEvent)event).ctype;
            game.doAction(new ChangeCountersAction(
                creature,
                type,
                event.getRefInt()
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
        final MagicCounterType type = ((MagicPutCounterEvent)event).ctype;
        game.doAction(new ChangeCountersAction(
            event.getPermanent(),
            type,
            event.getRefInt()
        ));
    };

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicPutCounterEvent(
            copyMap.copy(getSource()),
            ctype,
            getRefInt()
        );
    }
}
