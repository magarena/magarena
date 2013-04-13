package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

import java.util.Collection;

public class Highland_Berserker {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isFriend(permanent) &&
                    otherPermanent.hasSubType(MagicSubType.Ally)) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.PUMP,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may$ have Ally creatures he or " +
                    "she controls gain first strike until end of turn."
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
                for (final MagicPermanent creature : targets) {
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.FirstStrike));
                }
            }            
        }        
    };
}
