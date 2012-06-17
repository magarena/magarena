package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
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
            final MagicPlayer player = permanent.getController();
            if (permanent == creature &&
                player.getNrOfPermanentsWithType(MagicType.Creature) > 1) {
                final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
                        MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL,permanent);
                final MagicTargetChoice targetChoice = new MagicTargetChoice(
                        targetFilter,true,MagicTargetHint.None,"another creature to return");
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        targetChoice,
                        MagicBounceTargetPicker.getInstance(),
                        MagicEvent.NO_DATA,
                        this,
                        "Return another creature you control$ to its owner's hand.");
            }
            return MagicEvent.NONE;           
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicRemoveFromPlayAction(
                            creature,
                            MagicLocationType.OwnersHand));
                }
            });
        }
    };
}
