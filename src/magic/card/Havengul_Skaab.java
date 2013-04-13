package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicType;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicBounceTargetPicker;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetHint;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Havengul_Skaab {
    public static final MagicWhenAttacksTrigger T = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            if (permanent == creature && 
                permanent.getController().getNrOfPermanentsWithType(MagicType.Creature) > 1) {
                final MagicTargetFilter<MagicPermanent> targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                    MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent
                );
                final MagicTargetChoice targetChoice = new MagicTargetChoice(
                    targetFilter,
                    true,
                    MagicTargetHint.None,"another creature to return"
                );
                return new MagicEvent(
                    permanent,
                    targetChoice,
                    MagicBounceTargetPicker.getInstance(),
                    this,
                    "Return another creature PN controls$ to its owner's hand."
                );
            }
            return MagicEvent.NONE;           
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(
                        creature,
                        MagicLocationType.OwnersHand
                    ));
                }
            });
        }
    };
}
