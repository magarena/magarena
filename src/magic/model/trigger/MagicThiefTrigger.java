package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicDrawEvent;
import magic.model.event.MagicEvent;

public class MagicThiefTrigger extends MagicWhenDamageIsDealtTrigger {

	private final boolean combat;
	private final int amount;
	
    public MagicThiefTrigger(final boolean combat,final int amount) {
		this.combat=combat;
		this.amount=amount;
	}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
		return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&(!combat||damage.isCombat())) ?
            new MagicDrawEvent(permanent,permanent.getController(),amount):
            MagicEvent.NONE;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

	}
}
