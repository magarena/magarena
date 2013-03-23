package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicTapEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

import java.util.Set;

public class Urabrask_the_Hidden {
    public static final MagicStatic S = new MagicStatic(
        MagicLayer.Ability, 
        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            flags.add(MagicAbility.Haste);
        }
    };
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() && 
                    otherPermanent.getController()!=permanent.getController()) ?
                new MagicTapEvent(otherPermanent):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {}

        @Override
        public boolean usesStack() {
            return false;
        }        
    };
}
