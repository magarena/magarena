package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicDrawEvent;
import magic.model.event.MagicEvent;

public class MagicThiefTrigger extends MagicTrigger {

	private final boolean combat;
	private final int amount;
	
    public MagicThiefTrigger(final boolean combat,final int amount) {
		super(MagicTriggerType.WhenDamageIsDealt);
		this.combat=combat;
		this.amount=amount;
	}
	
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		final MagicDamage damage=(MagicDamage)data;
		return (damage.getSource()==permanent&&damage.getTarget().isPlayer()&&(!combat||damage.isCombat())) ?
            new MagicDrawEvent(permanent,permanent.getController(),amount):
            null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {

	}
}
