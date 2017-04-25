package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicTriggerType;
import magic.model.trigger.ThisSpellIsCastTrigger;

public class MagicSpellCastEvent extends MagicEvent {
    public MagicSpellCastEvent(final MagicItemOnStack itemOnStack) {
        super(
            itemOnStack.getEvent().getSource(),
            itemOnStack,
            EVENT_ACTION,
            ""
        );
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) -> {
        final MagicItemOnStack itemOnStack = event.getRefItemOnStack();

        // execute spell is cast triggers
        for (final ThisSpellIsCastTrigger trigger : itemOnStack.getCardDefinition().getSpellIsCastTriggers()) {
            game.executeTrigger(
                trigger,
                MagicPermanent.NONE,
                itemOnStack.getSource(),
                (MagicCardOnStack)itemOnStack
            );
        }

        // execute other spell is cast triggers
        game.executeTrigger(MagicTriggerType.WhenOtherSpellIsCast,itemOnStack);
        itemOnStack.getController().incSpellsCast();
        if (!itemOnStack.hasType(MagicType.Creature)){
            itemOnStack.getController().incNonCreatureSpellsCast();
        }
    };
}
