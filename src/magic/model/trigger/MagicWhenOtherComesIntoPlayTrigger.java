package magic.model.trigger;

import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.MagicCounterType;
import magic.model.event.MagicEvent;
import magic.model.action.MagicChangeCountersAction;

public abstract class MagicWhenOtherComesIntoPlayTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenOtherComesIntoPlayTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenOtherComesIntoPlayTrigger() {}
    
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherComesIntoPlay;
    }
    
    public static final MagicWhenOtherComesIntoPlayTrigger Evolve = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (permanent != otherPermanent &&
                    otherPermanent.isCreature() &&
                    permanent.isCreature() &&
                    (otherPermanent.getPower() > permanent.getPower() ||
                     otherPermanent.getToughness() > permanent.getToughness())) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN put a +1/+1 counter on SN.") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    };
}
