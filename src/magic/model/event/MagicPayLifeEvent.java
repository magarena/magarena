package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeLifeAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicPayLifeEvent extends MagicEvent {

    private final MagicCondition cond;

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new ChangeLifeAction(event.getPlayer(), -event.getRefInt()));

    public MagicPayLifeEvent(final MagicSource source,final int amount) {
        this(source, source.getController(), amount);
    }

    public MagicPayLifeEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            amount,
            EVENT_ACTION,
            "Pay RN life."
        );
        cond = MagicConditionFactory.YouLifeAtLeast(amount);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicPayLifeEvent(
            copyMap.copy(getSource()),
            copyMap.copy(getPlayer()),
            getRefInt()
        );
    }
}
