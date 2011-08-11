package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayUnlessTwoTrigger extends MagicTrigger {
	
    public MagicTappedIntoPlayUnlessTwoTrigger() {
		super(MagicTriggerType.WhenComesIntoPlay);
	}

	public MagicTappedIntoPlayUnlessTwoTrigger(final String name) {
		super(MagicTriggerType.WhenComesIntoPlay,name);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
		final MagicPlayer player=permanent.getController();
		if (player.getNrOfPermanentsWithType(MagicType.Land) > 3) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent},
                    this,
                    permanent.getName()+" enters the battlefield tapped.");
		}
		return null;
	}
	
	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choices) {
		game.doAction(new MagicTapAction((MagicPermanent)data[0],false));
	}
	
	@Override
	public boolean usesStack() {
		return false;
	}
}
