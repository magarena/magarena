package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicCombatDamageGrowTrigger extends MagicWhenDamageIsDealtTrigger {

	private final boolean combat;
	private final boolean player;
	
    public MagicCombatDamageGrowTrigger(final boolean combat,final boolean player) {
		this.combat = combat;
		this.player = player;
	}

	@Override
	public MagicEvent executeTrigger(
			final MagicGame game,
			final MagicPermanent permanent,
			final MagicDamage damage) {
		return (damage.getSource() == permanent &&
				(!player || damage.getTarget().isPlayer()) &&
				(player || (damage.getTarget().isPermanent() &&
				((MagicPermanent)damage.getTarget()).isCreature(game))) &&
				(!combat || damage.isCombat())) ?
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
