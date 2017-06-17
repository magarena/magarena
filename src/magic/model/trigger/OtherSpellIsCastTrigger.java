package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTargetFilter;

public abstract class OtherSpellIsCastTrigger extends MagicTrigger<MagicCardOnStack> {
    public OtherSpellIsCastTrigger(final int priority) {
        super(priority);
    }

    public OtherSpellIsCastTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherSpellIsCast;
    }

    public static final OtherSpellIsCastTrigger create(final MagicTargetFilter<MagicItemOnStack> filter, final MagicSourceEvent sourceEvent) {
        return new OtherSpellIsCastTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
                return filter.accept(permanent, permanent.getController(), spell);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
                return sourceEvent.getTriggerEvent(permanent, spell.getController());
            }
        };
    }

    public static final OtherSpellIsCastTrigger createYou(final MagicTargetFilter<MagicItemOnStack> filter, final MagicSourceEvent sourceEvent) {
        return new OtherSpellIsCastTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
                return permanent.isFriend(spell) && filter.accept(permanent, permanent.getController(), spell);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
                return sourceEvent.getTriggerEvent(permanent, spell.getController());
            }
        };
    }

    public static final OtherSpellIsCastTrigger createOpponent(final MagicTargetFilter<MagicItemOnStack> filter, final MagicSourceEvent sourceEvent) {
        return new OtherSpellIsCastTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
                return permanent.isEnemy(spell) && filter.accept(permanent, permanent.getController(), spell);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCardOnStack spell) {
                return sourceEvent.getTriggerEvent(permanent, spell.getController());
            }
        };
    }
}
