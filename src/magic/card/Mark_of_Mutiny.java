package magic.card;

import magic.model.*;
import magic.model.action.*;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicExileTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Mark_of_Mutiny {
	public static final MagicSpellCardEvent SOR=new MagicSpellCardEvent() {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicExileTargetPicker.getInstance(),
				    new Object[]{cardOnStack,player},this,
				    "Gain control of target creature$ until end of turn. Put a +1/+1 counter on it and untap it. " +
                    "That creature gains haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    game.doAction(new MagicGainControlAction((MagicPlayer)data[1],creature));
                    game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.ReturnToOwnerAtEndOfTurn,true));
                    game.doAction(new MagicChangeCountersAction(creature,MagicCounterType.PlusOne,1,true));
                    game.doAction(new MagicUntapAction(creature));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
                }
			});
		}
	};

}
