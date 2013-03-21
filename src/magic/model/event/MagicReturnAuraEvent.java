package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;

/** 
 * An Aura that is returned to play does NOT target a permanent
 */
public class MagicReturnAuraEvent extends MagicEvent {
    public MagicReturnAuraEvent(final MagicCardOnStack cardOnStack) {
        super(
            cardOnStack,
            new MagicTargetChoice(
                cardOnStack.getEvent().getTargetChoice().getTargetFilter(),
                false,
                cardOnStack.getEvent().getTargetChoice().getTargetHint(true),
                cardOnStack.getEvent().getTargetChoice().getDescription()
            ),
            cardOnStack.getEvent().getTargetPicker(),
            EVENT_ACTION,
            cardOnStack.getEvent().getChoiceDescription()
        );        
    }    
    
    private static final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicCardOnStack cardOnStack = event.getCardOnStack();
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicPlayCardFromStackAction(cardOnStack,creature));
                }
            });
        }
    };
}
