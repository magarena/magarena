package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicCounterType;
import magic.model.MagicCopyMap;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionFactory;

public class MagicPayEnergyEvent extends MagicEvent {

    private final MagicCondition cond;

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPlayer(), MagicCounterType.Energy, -event.getRefInt()));

    public MagicPayEnergyEvent(final MagicSource source,final int amount) {
        this(source, source.getController(), amount);
    }

    public MagicPayEnergyEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            amount,
            EVENT_ACTION,
            "Pay " + repeat("{E}", amount) + "."
        );
        cond = MagicConditionFactory.YouEnergyAtLeast(amount);
    }

    private static final String repeat(final String s, final int n) {
        return new String(new char[n]).replace("\0", s);
    }

    @Override
    public boolean isSatisfied() {
        return cond.accept(getSource()) && super.isSatisfied();
    }

    @Override
    public MagicEvent copy(final MagicCopyMap copyMap) {
        return new MagicPayEnergyEvent(
            copyMap.copy(getSource()),
            copyMap.copy(getPlayer()),
            getRefInt()
        );
    }
}
