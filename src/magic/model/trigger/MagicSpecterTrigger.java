package magic.model.trigger;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
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
        final String targetName = target.getName();
        final String opponentName = permanent.getController().getName();
        final String prefix = (target == permanent.getController() ? opponentName : targetName) + " discards a card";
		return (damage.getSource()==permanent && 
                target.isPlayer() && 
                (!combat||damage.isCombat())) ?
            new MagicEvent(
                permanent, 
                permanent.getController(), 
                MagicEvent.NO_DATA, 
                (random?prefix+" at random.":prefix)+'.', 
                new MagicEventAction() {
                @Override
                public void executeEvent(
                    final MagicGame game,
                    final MagicEvent event,
                    final Object data[],
                    final Object[] choices) {
                    final MagicPlayer player = (MagicPlayer)(target.map(game));
                    game.addEvent(new MagicDiscardEvent(permanent.map(game),player,1,random));
                }}):
            MagicEvent.NONE;
	}
}
