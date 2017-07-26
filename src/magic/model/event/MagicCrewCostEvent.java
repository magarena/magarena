package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.TapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicTapTargetPicker;

public class MagicCrewCostEvent extends MagicEvent {

    private final int crew;

    public MagicCrewCostEvent(final MagicSource source, final int n) {
        super(
            source,
            MagicTargetChoice.AN_UNTAPPED_CREATURE_YOU_CONTROL,
            MagicTapTargetPicker.Tap,
            n,
            EVENT_ACTION,
            "Tap an untapped creature you control$."
        );
        crew = n;
    }

    @Override
    public boolean isSatisfied() {
        return getTargetChoice().getPermanentFilter()
            .filter(getSource(), getPlayer())
            .stream()
            .mapToInt(it -> it.getPower())
            .sum() >= crew;
    }

    public static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        event.processTargetPermanent(game, (final MagicPermanent it) -> {
            game.doAction(new TapAction(it));
            final int n = event.getRefInt() - it.getPower();
            if (n > 0) {
                game.addNextCostEvent(new MagicCrewCostEvent(event.getSource(), n));
            }
        });
    };
}
