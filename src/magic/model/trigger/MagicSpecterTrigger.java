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
	private final boolean random;
	
	public MagicSpecterTrigger(final boolean combat,final boolean random) {
		this.combat=combat;
		this.random=random;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
		final MagicTarget target = damage.getTarget();
        final MagicTarget player = target;
        final String playerName = player.getName();
        final String opponentName = permanent.getController().getName();
        final String prefix = (player == permanent.getController() ? opponentName : playerName) + " discards a card";
		return (damage.getSource()==permanent && 
                target.isPlayer() && 
                (!combat||damage.isCombat())) ?
            new MagicEvent(
                permanent,
                permanent.getController(),
                new Object[]{permanent,player},
                this,
                (random?prefix+" at random.":prefix)+'.'):
            null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choices) {
		final MagicPermanent permanent=(MagicPermanent)data[0];
		final MagicPlayer player=(MagicPlayer)data[1];
		game.addEvent(new MagicDiscardEvent(permanent,player,1,random));
	}
}
