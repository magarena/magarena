package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicVeteranTrigger extends MagicWhenDamageIsDealtTrigger {

	private boolean combat;
	
    public MagicVeteranTrigger(final boolean combat) {
		this.combat=combat;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        if (!damage.getTarget().isPermanent()) {
            return MagicEvent.NONE;
        }
        final MagicPermanent target=(MagicPermanent)damage.getTarget();
		return (damage.getSource() == permanent && 
                (!combat||damage.isCombat()) &&
                target.isCreature()) ?
            new MagicEvent(
                permanent, 
                permanent.getController(), 
                new Object[]{permanent}, 
                "Put a +1/+1 counter on "+permanent+".", 
                new MagicEventAction() {
                @Override
                public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object data[],
                    final Object[] choices) {
                    game.doAction(new MagicChangeCountersAction(permanent.map(game),MagicCounterType.PlusOne,1,true));
                }}):
            MagicEvent.NONE;
	}
}
