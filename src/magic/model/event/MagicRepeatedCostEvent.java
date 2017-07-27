package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedCostEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicRepeatedCostEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        this(source, source.getController(), targetChoice, amt, factory);
    }

    public MagicRepeatedCostEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        super(
            source,
            player,
            targetChoice,
            amt - 1,
            (final MagicGame game, final MagicEvent event) -> {
                final MagicEvent ev = factory.getEvent(event);
                ev.executeEvent(game, event.getChosen());
                for (int i = 0; i < event.getRefInt(); i++) {
                    game.addNextCostEvent(ev);
                }
            },
            factory.getEvent(source, targetChoice).getDescription()
        );
        cond = MagicConditionFactory.YouHaveAtLeast(targetChoice.getFilter(), amt);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
