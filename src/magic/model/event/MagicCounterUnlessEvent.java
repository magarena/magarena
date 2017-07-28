package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicMessage;
import magic.model.MagicSource;
import magic.model.MagicTuple;
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
            new MagicTuple(itemOnStack, toLocation),
            EventAction,
            MagicMessage.format(
                "You may$ pay %s$. If you don't, counter %s.",
                cost.getText(),
                itemOnStack
            )
        );
    }

    private static final MagicEventAction EventAction = (final MagicGame game, final MagicEvent event) -> {
        if (event.isNo()) {
            final MagicTuple tup = event.getRefTuple();
            game.doAction(new CounterItemOnStackAction(
                tup.getItemOnStack(0),
                tup.getLocationType(1)
            ));
        }
    };
}
