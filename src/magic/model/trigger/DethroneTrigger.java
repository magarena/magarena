package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.ChangeCountersAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicEvent;

public class DethroneTrigger extends ThisAttacksTrigger {

    private static final DethroneTrigger INSTANCE = new DethroneTrigger();

    private DethroneTrigger() {}

    public static DethroneTrigger create() {
        return INSTANCE;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent attacker) {
        return MagicCondition.OPPONENT_HAS_GREATER_OR_EQUAL_LIFE.accept(permanent) ?
            new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(),MagicCounterType.PlusOne,1));
    }
}
