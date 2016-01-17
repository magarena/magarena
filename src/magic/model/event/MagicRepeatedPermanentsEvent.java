package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedPermanentsEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicRepeatedPermanentsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        super(
            source,
            targetChoice,
            amt - 1,
            (final MagicGame game, final MagicEvent event) -> {
                final MagicEvent ev = factory.getEvent(event);
                ev.executeEvent(game, event.getChosen());
                for (int i = 0; i < event.getRefInt(); i++) {
                    game.addFirstEvent(ev);
                }
            },
            factory.getEvent(source, targetChoice).getDescription()
        );
        cond = MagicConditionFactory.YouControlAtLeast(targetChoice.getPermanentFilter(), amt);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
