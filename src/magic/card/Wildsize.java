package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Wildsize {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.POS_TARGET_CREATURE,
                    MagicPumpTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Target creature$ gets +2/+2 and gains trample until end of turn. Draw a card.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicChangeTurnPTAction(creature,2,2));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Trample));
                }
			});
			game.doAction(new MagicDrawAction((MagicPlayer)data[1],1));
		}
	};
}
