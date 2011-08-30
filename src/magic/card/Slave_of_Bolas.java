package magic.card;

import magic.model.*;
import magic.model.action.*;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicExileTargetPicker;
import magic.model.action.MagicPermanentAction;

public class Slave_of_Bolas {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    MagicExileTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Gain control of target creature$. Untap that creature. " + 
                    "It gains haste until end of turn. Sacrifice it at end of turn.");
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
                    game.doAction(new MagicGainControlAction((MagicPlayer)data[1],creature));
                    game.doAction(new MagicUntapAction(creature));
                    game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
                    game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.SacrificeAtEndOfTurn,true));
                }
			});
		}
	};
}
