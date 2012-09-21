package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;

public class Kozilek__Butcher_of_Truth {
    public static final MagicWhenSpellIsCastTrigger T1 = new MagicWhenSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack spell) {
            return new MagicEvent(
                spell,
                this,
                "PN draws four cards."
            );
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(),4));
        }
    };
    
    public static final Object T2 = Ulamog__the_Infinite_Gyre.T2;
}
