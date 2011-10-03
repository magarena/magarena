package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicExileUntilEndOfTurnAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;


public class Argent_Sphinx {
	public static final MagicPermanentActivation A =new MagicPermanentActivation(
			new MagicCondition[]{
				MagicManaCost.BLUE.getCondition(),
                MagicCondition.METALCRAFT_CONDITION
            },
            new MagicActivationHints(MagicTiming.Removal,false,1),
            "Exile") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.BLUE)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Exile " + source + ". Return it to the battlefield " +
                    "under your control at the end of turn.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicExileUntilEndOfTurnAction((MagicPermanent)data[0]));
		}
	};
}
