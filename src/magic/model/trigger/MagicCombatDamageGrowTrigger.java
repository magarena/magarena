package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicCombatDamageGrowTrigger extends MagicWhenDamageIsDealtTrigger {

	private static final MagicCombatDamageGrowTrigger INSTANCE = new MagicCombatDamageGrowTrigger();

    private MagicCombatDamageGrowTrigger() {}

    public static MagicCombatDamageGrowTrigger create() {
        return INSTANCE;
    }

	@Override
	public MagicEvent executeTrigger(
			final MagicGame game,
			final MagicPermanent permanent,
			final MagicDamage damage) {
		return (damage.getSource() == permanent &&
				damage.getTarget().isPlayer() &&
				damage.isCombat()) ?
            new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent},
                    this,
                    "Put a +1/+1 counter on " + permanent + "."):
            MagicEvent.NONE;
	}
	@Override
	public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object data[],
            final Object[] choiceResults) {
		game.doAction(new MagicChangeCountersAction(
				(MagicPermanent)data[0],
				MagicCounterType.PlusOne,
				1,
				true));
	}
}
