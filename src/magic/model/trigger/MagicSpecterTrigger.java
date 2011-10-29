package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;

public class MagicSpecterTrigger extends MagicWhenDamageIsDealtTrigger {

	private final boolean combat;
	private final boolean opponent;
	private final boolean random;
	
	public MagicSpecterTrigger(final boolean combat,final boolean opponent,final boolean random) {
		this.combat = combat;
		this.opponent = opponent;
		this.random = random;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
        final MagicTarget target = damage.getTarget();
        final String prefix = target + " discards a card";
		return (damage.getSource() == permanent && 
				target.isPlayer() && 
				((MagicPlayer)target).getHandSize() > 0 &&
                (!opponent || target != permanent.getController()) &&
                (!combat || damage.isCombat())) ?
            new MagicEvent(
                permanent,
                permanent.getController(),
                new Object[]{permanent,target},
                this,
                random ? prefix + " at random." : prefix + "."):
            MagicEvent.NONE;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {
		game.addEvent(new MagicDiscardEvent(
				(MagicPermanent)data[0],
				(MagicPlayer)data[1],
				1,
				random));
	}
}
