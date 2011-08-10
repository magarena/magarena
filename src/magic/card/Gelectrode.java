package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

public class Gelectrode {

	public static final MagicPermanentActivation V922 =new MagicPermanentActivation(			"Gelectrode",
            new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION},
            new MagicActivationHints(MagicTiming.Removal),
            "Damage") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicTapEvent((MagicPermanent)source)};
		}

		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                    new MagicDamageTargetPicker(1),
                    new Object[]{source},
                    this,
                    "Gelectrode deals 1 damage to target creature or player$.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			final MagicTarget target=event.getTarget(game,choiceResults,0);
			if (target!=null) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],target,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
	};
	
    public static final MagicTrigger V7407 =new MagicTrigger(MagicTriggerType.WhenSpellIsPlayed,"Gelectrode") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			final MagicPlayer player=permanent.getController();
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data;
			if (cardOnStack.getController()==player&&cardOnStack.getCardDefinition().isSpell()) {
				return new MagicEvent(permanent,player,new Object[]{permanent},this,"Untap Gelectrode.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			game.doAction(new MagicUntapAction((MagicPermanent)data[0]));
		}
    };
    
}
