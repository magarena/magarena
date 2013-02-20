package magic.model.event;

import magic.model.MagicPayedCost;
import magic.model.stack.MagicCardOnStack;

public interface MagicCardEvent {
    MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost);
}
