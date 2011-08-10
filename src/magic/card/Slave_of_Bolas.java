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

public class Slave_of_Bolas {

	public static final MagicSpellCardEvent V5958 =new MagicSpellCardEvent("Slave of Bolas") {

		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(cardOnStack.getCard(),player,MagicTargetChoice.NEG_TARGET_CREATURE,MagicExileTargetPicker.getInstance(),
				new Object[]{cardOnStack,player},this,
				"Gain control of target creature$. Untap that creature. It gains haste until end of turn. Sacrifice it at end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {

			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
			final MagicPermanent creature=event.getTarget(game,choiceResults,0);
			if (creature!=null) {
				game.doAction(new MagicGainControlAction((MagicPlayer)data[1],creature));
				game.doAction(new MagicUntapAction(creature));
				game.doAction(new MagicSetAbilityAction(creature,MagicAbility.Haste));
				game.doAction(new MagicChangeStateAction(creature,MagicPermanentState.SacrificeAtEndOfTurn,true));
			}
		}
	};

}
