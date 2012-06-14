package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeStateAction;
import magic.model.action.MagicGainControlAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicSetAbilityAction;
import magic.model.action.MagicUntapAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicExileTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.mstatic.MagicStatic;

public class Zealous_Conscripts {
	public static final Object T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicEvent(
                    permanent,
                    player,
                    MagicTargetChoice.NEG_TARGET_PERMANENT,
                    MagicExileTargetPicker.create(),
                    new Object[]{permanent,player},
                    this,
				    "Gain control of target permanent$ until end of turn. Untap that permanent. " +
				    "It gains haste until end of turn.");
		}

		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                public void doAction(final MagicPermanent perm) {
                    game.doAction(new MagicGainControlAction((MagicPlayer)data[1],perm,MagicStatic.UntilEOT));
                    game.doAction(new MagicUntapAction(perm));
                    game.doAction(new MagicSetAbilityAction(perm,MagicAbility.Haste));
                }
			});
		}
	};

}
