package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicCounterUnlessEvent;
import magic.model.event.MagicEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.trigger.MagicWhenTargetedTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Frost_Titan {
    //counter opponent spell or ability unless its controller pay {2}
    public static final MagicWhenTargetedTrigger T1 = new MagicWhenTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return (target.containsInChoiceResults(permanent) &&
                    target.getController() != permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{permanent,target},
                    this,
                    "Counter spell or ability$ unless its controller pays {2}."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            final MagicSource source = (MagicSource)data[0];
            final MagicItemOnStack target = (MagicItemOnStack)data[1];
            game.addEvent(new MagicCounterUnlessEvent(source,target,MagicManaCost.TWO));
        }
    };
   
    //tap target permanent. It doesn't untap during its controller's next untap step.
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target permanent$. It doesn't untap during its controller's next untap step.");
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicTapAction(perm,true));
                    game.doAction(new MagicChangeStateAction(perm,MagicPermanentState.DoesNotUntapDuringNext,true));
                }
            });
        }
    };
    
    public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (permanent == creature) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    MagicTargetChoice.TARGET_PERMANENT,
                    MagicEvent.NO_DATA,
                    this,
                    "Tap target permanent$. It doesn't untap during its controller's next untap step."):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicTapAction(perm,true));
                    game.doAction(new MagicChangeStateAction(perm,MagicPermanentState.DoesNotUntapDuringNext,true));
                }
            });
        }
    };
}
