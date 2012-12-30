package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Genesis_Chamber {
	public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
            		permanent.isUntapped() &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.isCreature()) ?
                new MagicEvent(
                        permanent,
                        this,
                        "PN puts a 1/1 colorless Myr artifact creature token onto the battlefield."
                        ):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {            
        	game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Myr1")));
        }        
    };	
}
