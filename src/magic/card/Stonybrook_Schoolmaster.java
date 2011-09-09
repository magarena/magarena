package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Stonybrook_Schoolmaster {
    public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
    		final MagicPlayer player = permanent.getController();
    		return (permanent == data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts a 1/1 blue Merfolk Wizard creature token onto the battlefield.") :
                MagicEvent.NONE;
    	}
    	@Override
    	public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.MERFOLK_WIZARD_TOKEN_CARD));
    	}
    };
}
