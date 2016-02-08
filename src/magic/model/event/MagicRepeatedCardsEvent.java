package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicSource;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicRepeatedCardsEvent extends MagicEvent {

    private final MagicCondition cond;

    public MagicRepeatedCardsEvent(final MagicSource source, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        this(source, source.getController(), targetChoice, amt, factory);
    }

    public MagicRepeatedCardsEvent(final MagicSource source, final MagicPlayer player, final MagicTargetChoice targetChoice, final int amt, final MagicChainEventFactory factory) {
        super(
            source,
            player,
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
        cond = MagicConditionFactory.YouHaveAtLeast(targetChoice.getCardFilter(), amt);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }
}
