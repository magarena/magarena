package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.action.MagicRemoveItemFromStackAction;
import magic.model.action.MagicTargetAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Venser__Shaper_Savant {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_SPELL_OR_PERMANENT,
                    MagicBounceTargetPicker.getInstance(),
                    MagicEvent.NO_DATA,
                    this,
                    "Return target spell or permanent$ to its owner's hand.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTarget(game,choiceResults,0,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    if (target.isPermanent()) {
                        game.doAction(new MagicRemoveFromPlayAction((MagicPermanent)target,MagicLocationType.OwnersHand));
                    } else {
                        final MagicCardOnStack cardOnStack=(MagicCardOnStack)target;
                        game.doAction(new MagicRemoveItemFromStackAction(cardOnStack));
                        game.doAction(new MagicMoveCardAction(
                                    cardOnStack.getCard(),
                                    MagicLocationType.Stack,
                                    MagicLocationType.OwnersHand));
                    }
                }
            });
        }
    };
}
