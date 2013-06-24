package magic.model.trigger;

import magic.model.stack.MagicCardOnStack;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;

public abstract class MagicWhenYouCastSpiritOrArcaneTrigger extends MagicWhenOtherSpellIsCastTrigger {
    public MagicWhenYouCastSpiritOrArcaneTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenYouCastSpiritOrArcaneTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
        return (permanent.isFriend(spell) &&
                (spell.getCardDefinition().hasSubType(MagicSubType.Spirit) ||
                 spell.getCardDefinition().hasSubType(MagicSubType.Arcane)));
    }
}
