package magic.card;

import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;

public class Frost_Titan {
    //counter opponent spell or ability unless its controller pay {2}
    public static final MagicTrigger T1 = new MagicTrigger(MagicTriggerType.WhenTargeted) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            final MagicItemOnStack target = (MagicItemOnStack)data;
            if (target.containsInChoiceResults(permanent) &&
                target.getController() != permanent.getController()) {
			    return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,target},
                        this,"Counter spell or ability$ unless its controller pays {2}.");
            }
            return null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicSource source=(MagicSource)data[0];
			final MagicItemOnStack target=(MagicItemOnStack)data[1];
			if (target != null) {
				game.addEvent(new MagicCounterUnlessEvent(source,target,MagicManaCost.TWO));
			}
        }
    };
   
    //tap target permanent. It doesn't untap during its controller's next untap step.
    public static final MagicTrigger T2 = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            return new MagicEvent(
                    permanent,
                    permanent.getController(),
                    MagicTargetChoice.NEG_TARGET_PERMANENT,
                    null,
                    this,
                    "Tap target permanent$. It doesn't untap during its controller's next untap step.");
        }
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent perm = event.getTarget(game,choiceResults,0);
			if (perm!=null) {
                game.doAction(new MagicTapAction(perm,true));
			    game.doAction(new MagicChangeStateAction(perm,MagicPermanentState.DoesNotUntap,true));
			}
		}
    };
    
    public static final MagicTrigger T3 = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			if (permanent==data) {
                return new MagicEvent(
                        permanent,
                        permanent.getController(),
                        MagicTargetChoice.NEG_TARGET_PERMANENT,
                        null,
                        this,
						"Tap target permanent$. It doesn't untap during its controller's next untap step.");
			}
            return null;
        }
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent perm = event.getTarget(game,choiceResults,0);
			if (perm!=null) {
                game.doAction(new MagicTapAction(perm,true));
			    game.doAction(new MagicChangeStateAction(perm,MagicPermanentState.DoesNotUntap,true));
			}
		}
    };
}
