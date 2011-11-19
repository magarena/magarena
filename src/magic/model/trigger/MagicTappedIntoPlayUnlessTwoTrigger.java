package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;

public class MagicTappedIntoPlayUnlessTwoTrigger extends MagicWhenComesIntoPlayTrigger {

    private static final MagicTappedIntoPlayUnlessTwoTrigger INSTANCE = new MagicTappedIntoPlayUnlessTwoTrigger();

    private MagicTappedIntoPlayUnlessTwoTrigger() {}

    public static MagicTappedIntoPlayUnlessTwoTrigger create() {
        return INSTANCE;
    }

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
		return (player.getNrOfPermanentsWithType(MagicType.Land,game) > 3) ?
			new MagicEvent(
                permanent,
                player,
                new Object[]{permanent},
                this,
                permanent+" enters the battlefield tapped."):
            MagicEvent.NONE;
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
