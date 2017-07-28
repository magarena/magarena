package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.MagicCopyMap;
import magic.model.MagicTuple;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedCostEvent extends MagicEvent {

    private final MagicCondition cond;

    private static final MagicEventAction action = (final MagicGame game, final MagicEvent event) -> {
        final MagicTuple tup = event.getRefTuple();
        final MagicChainEventFactory factory = tup.getChainEventFactory(1);
        final MagicEvent ev = factory.getEvent(event);
        ev.executeEvent(game, event.getChosen());
        final int amt = tup.getInt(0);
        for (int i = 0; i < amt; i++) {
            game.addNextCostEvent(ev);
        }
    };

    public MagicRepeatedCostEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        this(source, source.getController(), targetChoice, amt, factory);
    }

    public MagicRepeatedCostEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        super(
            source,
            player,
            targetChoice,
            new MagicTuple(amt - 1, factory),
            action,
            factory.getEvent(source, targetChoice).getDescription()
        );
        cond = MagicConditionFactory.YouHaveAtLeast(targetChoice.getFilter(), amt);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        final MagicTuple tup = getRefTuple();
        return new MagicRepeatedCostEvent(
            copyMap.copy(getSource()),
            copyMap.copy(getPlayer()),
            getTargetChoice(),
            tup.getInt(0),
            tup.getChainEventFactory(1)
        );
    }
}
