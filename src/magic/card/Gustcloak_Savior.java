package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicRemoveFromCombatAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Gustcloak_Savior {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return permanent.isFriend(creature) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    creature,
                    this, 
                    "PN may$ untap " + creature + " and remove it from combat.") :
            MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                final MagicPermanent permanent = event.getRefPermanent();
                game.doAction(new MagicUntapAction(permanent));
                game.doAction(new MagicRemoveFromCombatAction(permanent));
            }
        }
    };
}
