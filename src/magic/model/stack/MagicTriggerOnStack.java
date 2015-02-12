package magic.model.stack;

import magic.model.MagicCard;
import magic.model.MagicCopyMap;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;

public class MagicTriggerOnStack extends MagicItemOnStack {
    public MagicTriggerOnStack(final MagicEvent event) {
        super(event.getSource(), event.getSource().getController(), event);
        assert event.getSource() != MagicCard.NONE : event.toString();
        assert event.getSource().getController() != MagicPlayer.NONE : event.toString();
    }

    private MagicTriggerOnStack(final MagicCopyMap copyMap ,final MagicTriggerOnStack source) {
        super(copyMap, source);
    }

    @Override
    public MagicTriggerOnStack copy(final MagicCopyMap copyMap) {
        return new MagicTriggerOnStack(copyMap, this);
    }

    @Override
    public boolean isSpell() {
        return false;
    }

    @Override
    public boolean canBeCountered() {
        return true;
    }

}
