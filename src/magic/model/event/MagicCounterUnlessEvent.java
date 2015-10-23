package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicMessage;
import magic.model.MagicSource;
import magic.model.action.CounterItemOnStackAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;
import magic.model.stack.MagicItemOnStack;

public class MagicCounterUnlessEvent extends MagicEvent {
    
    public MagicCounterUnlessEvent(final MagicSource source,final MagicItemOnStack itemOnStack,final MagicManaCost cost) {
        this(source, itemOnStack, cost, MagicLocationType.Graveyard);
    }
    
    public MagicCounterUnlessEvent(final MagicSource source,final MagicItemOnStack itemOnStack,final MagicManaCost cost, final MagicLocationType toLocation) {
        super(
            source,
            itemOnStack.getController(),
            new MagicMayChoice(
                "Pay "+cost.getText()+"?",
                new MagicPayManaCostChoice(cost)
            ),
            itemOnStack,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    if (event.isNo()) {
                        game.doAction(new CounterItemOnStackAction(
                            event.getRefItemOnStack(),
                            toLocation
                        ));
                    }
                }
            },
            String.format("You may$ pay %s$. If you don't, counter %s.",
                cost.getText(),
                MagicMessage.getCardToken(itemOnStack)
            )
        );
    }
}
