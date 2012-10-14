package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.choice.MagicTargetChoice;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicWhenSpellIsCastTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicStackGetChoicesEvent extends MagicEvent {
    public MagicStackGetChoicesEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getEvent().getSource(),
            itemOnStack.getController(),
            itemOnStack.getEvent().getChoice(),
            itemOnStack.getEvent().getTargetPicker(),
            itemOnStack,
            EVENT_ACTION,
            ""
        );
    }
    
    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicItemOnStack itemOnStack = event.getRefItemOnStack();
            itemOnStack.setChoiceResults(choiceResults);

            // pay mana cost when there is one.
            event.payManaCost(game,itemOnStack.getController(),choiceResults);

            // trigger WhenTargeted
            final MagicTargetChoice tchoice = event.getChoice().getTargetChoice();
            if (tchoice != null && tchoice.isTargeted()) {
                game.executeTrigger(MagicTriggerType.WhenTargeted,itemOnStack);
            }
                
            if (itemOnStack.isSpell()) {
                // execute spell is cast triggers
                for (final MagicWhenSpellIsCastTrigger trigger : itemOnStack.getSource().getCardDefinition().getSpellIsCastTriggers()) {
                    game.executeTrigger(
                        trigger,
                        MagicPermanent.NONE,
                        itemOnStack.getSource(),
                        (MagicCardOnStack)itemOnStack
                    );
                }
                
                // execute other spell is cast triggers
                game.executeTrigger(MagicTriggerType.WhenOtherSpellIsCast,itemOnStack);
            }
        }
    };
}
