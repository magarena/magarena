package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRemoveFromCombatAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Gustcloak_Savior {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            final MagicPlayer player = permanent.getController();
            return (creature.getController() == player) ?
                    new MagicEvent(
                            permanent,
                            player,
                            new MagicMayChoice(player + " may untap " + creature +
                                    " and remove it from combat."),
                            new Object[]{creature},
                            this, 
                            player + " may$ untap " + creature +
                            " and remove it from combat.") :
            MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                final MagicPermanent permanent = (MagicPermanent)data[0];
                game.doAction(new MagicUntapAction(permanent));
                game.doAction(new MagicRemoveFromCombatAction(permanent));
            }
        }
    };
}
