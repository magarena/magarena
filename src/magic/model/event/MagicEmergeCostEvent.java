package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.SacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.target.MagicSacrificeTargetPicker;
import magic.model.target.MagicTargetFilterFactory;

public class MagicEmergeCostEvent extends MagicEvent {

    private final MagicManaCost manaCost;

    public MagicEmergeCostEvent(final MagicSource source, final MagicManaCost aManaCost) {
        super(
            source,
            new MagicTargetChoice(
                MagicTargetFilterFactory.CREATURE_YOU_CONTROL_FOR_EMERGE(aManaCost),
                "a creature you control"
            ),
            MagicSacrificeTargetPicker.create(),
            EVENT_ACTION,
            "Sacrifice a creature you control$."
        );
        manaCost = aManaCost;
    }

    public static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        event.processTargetPermanent(game, (final MagicPermanent permanent) -> {
            final MagicManaCost manaCost = ((MagicEmergeCostEvent)event).manaCost;
            game.doAction(new SacrificeAction(permanent));
            game.addNextCostEvent(new MagicPayManaCostEvent(event.getSource(), manaCost.reduce(permanent.getConvertedCost())));
        });

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicEmergeCostEvent(
            copyMap.copy(getSource()),
            manaCost
        );
    }
}
