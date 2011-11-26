package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;

public class MagicExaltedTrigger extends MagicWhenAttacksTrigger {

	private static final MagicExaltedTrigger INSTANCE = new MagicExaltedTrigger();

    private MagicExaltedTrigger() {}
	
	public static MagicExaltedTrigger create() {
		return INSTANCE;
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
		final MagicPlayer player=permanent.getController();
		return (creature.getController()==player&&player.getNrOfAttackers()==1) ?
            new MagicEvent(
                    permanent,
                    player,
                    new Object[]{creature},
                    this,
                    creature.getName() + " gets +1/+1 until end of turn."):
            MagicEvent.NONE;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
		game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],1,1));
	}
}
