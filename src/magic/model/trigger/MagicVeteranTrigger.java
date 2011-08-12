package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;

public class MagicVeteranTrigger extends MagicTrigger {

	private boolean combat;
	
    public MagicVeteranTrigger(final boolean combat) {
		super(MagicTriggerType.WhenDamageIsDealt);
		this.combat=combat;
	}
	
	public MagicVeteranTrigger(final String name,final boolean combat) {
		super(MagicTriggerType.WhenDamageIsDealt,name);
		this.combat=combat;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		
		final MagicDamage damage=(MagicDamage)data;
		if (damage.getSource()==permanent&&damage.getTarget().isPermanent()&&(!combat||damage.isCombat())) {
			final MagicPermanent target=(MagicPermanent)damage.getTarget();
			if (target.isCreature()) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"Put a +1/+1 counter on "+permanent.getName()+".");
			}
		}
		return null;
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {
		
		game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
	}
}
