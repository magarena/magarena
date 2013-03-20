package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Goblin_Ruinblaster {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            return permanent.isKicked() ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_NONBASIC_LAND,
                    new MagicDestroyTargetPicker(false),
                    this,
                    "Destroy target nonbasic land$."
                ):
                MagicEvent.NONE;
        }
    
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,new MagicPermanentAction() {
                public void doAction(final MagicPermanent land) {
                    game.doAction(new MagicDestroyAction(land));
                }
            });
        }
    };
}
