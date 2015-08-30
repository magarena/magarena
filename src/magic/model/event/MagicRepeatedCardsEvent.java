package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedCardsEvent extends MagicEvent {
    
    private final MagicCondition cond;

    public MagicRepeatedCardsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        super(
            source,
            targetChoice,
            amt - 1,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicEvent ev = factory.getEvent(event);
                    ev.executeEvent(game, event.getChosen());
                    for (int i = 0; i < event.getRefInt(); i++) {
                        game.addFirstEvent(ev);
                    }
                }
            },
            ""
        );
        cond = MagicConditionFactory.YouHaveAtLeast(targetChoice.getCardFilter(), amt);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
