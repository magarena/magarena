package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.SacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTargetFilter;

public abstract class ThisBecomesTargetTrigger extends BecomesTargetTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
        return item.isTarget(permanent);
    }

    public static ThisBecomesTargetTrigger create(final MagicTargetFilter<MagicItemOnStack> filter, final MagicSourceEvent sourceEvent) {
        return new ThisBecomesTargetTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return super.accept(permanent, itemOnStack) && filter.accept(permanent, permanent.getController(), itemOnStack);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static ThisBecomesTargetTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisBecomesTargetTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static ThisBecomesTargetTrigger createSpell(final MagicSourceEvent sourceEvent) {
        return new ThisBecomesTargetTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
                return super.accept(permanent, item) && item.isSpell();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }

    public static final ThisBecomesTargetTrigger SacWhenTargeted = new ThisBecomesTargetTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getPermanent()));
        }
    };
}
