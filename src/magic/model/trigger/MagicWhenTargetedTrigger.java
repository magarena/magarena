package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicWhenTargetedTrigger extends MagicTrigger<MagicItemOnStack> {
    public MagicWhenTargetedTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenTargetedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTargeted;
    }

    public static final MagicWhenTargetedTrigger SacWhenTargeted = new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return target.containsInChoiceResults(permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
    
    public static final MagicWhenTargetedTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenTargetedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return itemOnStack.containsInChoiceResults(permanent) ?
                    sourceEvent.getEvent(permanent):
                    MagicEvent.NONE;
            }
        };
    }
    
}
