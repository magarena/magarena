package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;

import java.util.Collection;

public class Maelstrom_Pulse {

	public static final MagicSpellCardEvent V4129 =new MagicSpellCardEvent("Maelstrom Pulse") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			return new MagicEvent(cardOnStack.getCard(),cardOnStack.getController(),MagicTargetChoice.NEG_TARGET_NONLAND_PERMANENT,new MagicDestroyTargetPicker(false),
				new Object[]{cardOnStack},this,"Destroy target nonland permanent$ and all other permanents with the same name as that permanent.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
			
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			game.doAction(new MagicMoveCardAction(cardOnStack));
			
			final MagicPermanent targetPermanent=event.getTarget(game,choiceResults,0);
			if (targetPermanent!=null) {
				final MagicTargetFilter targetFilter=new MagicTargetFilter.NameTargetFilter(targetPermanent.getName());
				final Collection<MagicTarget> targets=game.filterTargets(cardOnStack.getController(),targetFilter);
				for (final MagicTarget target : targets) {

					game.doAction(new MagicDestroyAction((MagicPermanent)target));
				}
			}
		}
	};
	
}
