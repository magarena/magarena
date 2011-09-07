package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicTapAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;

public class MagicVividLandTrigger extends MagicWhenComesIntoPlayTrigger {
	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
		return new MagicEvent(
            permanent, 
            permanent.getController(), 
            MagicEvent.NO_DATA, 
            permanent+" enters the battlefield tapped with two charge counters on it.", 
            new MagicEventAction() {
            @Override
            public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choices) {
                game.doAction(new MagicTapAction(permanent.map(game),false));
                game.doAction(new MagicChangeCountersAction(permanent.map(game),MagicCounterType.Charge,2,false));
            }});
	}
	@Override
	public boolean usesStack() {
		return false;
	}
}
