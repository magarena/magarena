package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Judge_of_Currents {
    public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
    		final MagicPlayer player = permanent.getController();
    		return (data.getController() == player &&
    				data.isCreature() &&
    				data.hasSubType(MagicSubType.Merfolk)) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " gains 1 life.") :
                MagicEvent.NONE;
    	}
    	@Override
    	public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],1));
    	}
    };
}
