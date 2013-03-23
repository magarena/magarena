package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

import java.util.Collection;

public class Kazuul_Warlord {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_PLUSONE_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES),
                    this,
                    "PN may$ put a +1/+1 counter on " +
                    "each Ally creature he or she controls."
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            if (event.isYes()) {
                final Collection<MagicPermanent> targets =
                        game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_ALLY_YOU_CONTROL);
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicChangeCountersAction(
                        target,
                        MagicCounterType.PlusOne,
                        1,
                        true
                    ));
                }
            }            
        }        
    };
}
