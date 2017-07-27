package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedCostEvent extends MagicEvent {

    private final MagicCondition cond;
    private final MagicChainEventFactory factory;

    private static final MagicEventAction action = (final MagicGame game, final MagicEvent event) -> {
        final MagicChainEventFactory fact = ((MagicRepeatedCostEvent)event).factory;
        final MagicEvent ev = fact.getEvent(event);
        ev.executeEvent(game, event.getChosen());
        for (int i = 0; i < event.getRefInt(); i++) {
            game.addNextCostEvent(ev);
        }
    };

    public MagicRepeatedCostEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        this(source, source.getController(), targetChoice, amt, factory);
    }

    public MagicRepeatedCostEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory aFactory) {
        super(
            source,
            player,
            targetChoice,
            amt - 1,
            action,
            aFactory.getEvent(source, targetChoice).getDescription()
        );
        cond = MagicConditionFactory.YouHaveAtLeast(targetChoice.getFilter(), amt);
        factory = aFactory;
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
