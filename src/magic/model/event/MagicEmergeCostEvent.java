package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilterFactory;

public class MagicEmergeCostEvent extends MagicEvent {

    public MagicEmergeCostEvent(final MagicSource source, final MagicManaCost manaCost) {
        super(
            source,
            new MagicTargetChoice(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL_FOR_EMERGE(manaCost),
                "a creature you control"
            ),
            MagicSacrificeTargetPicker.create(),
            manaCost,
            EVENT_ACTION,
            "Sacrifice a creature you control$."
        );
    }

    public static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent permanent) -> {
            final MagicManaCost manaCost = event.getRefManaCost();
            game.doAction(new SacrificeAction(permanent));
            game.addNextCostEvent(new MagicPayManaCostEvent(event.getSource(), manaCost.reduce(permanent.getConvertedCost())));
        });
}
