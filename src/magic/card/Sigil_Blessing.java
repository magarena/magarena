package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicPumpTargetPicker;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.action.MagicPermanentAction;

import java.util.Collection;

public class Sigil_Blessing {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                    MagicPumpTargetPicker.getInstance(),
                    new Object[]{cardOnStack,player},
                    this,
                    "Until end of turn, target creature$ you control gets +3/+3 and other creatures you control get +1/+1.");
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
                    final Collection<MagicTarget> targets=game.filterTargets(
                            (MagicPlayer)data[1],
                            MagicTargetFilter.TARGET_CREATURE_YOU_CONTROL);
                    for (final MagicTarget target : targets) {
                        final MagicPermanent permanent=(MagicPermanent)target;
                        if (permanent==creature) {
                            game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
                        } else {
                            game.doAction(new MagicChangeTurnPTAction(permanent,1,1));
                        }
                    }
                }
            });
		}
	};
}
