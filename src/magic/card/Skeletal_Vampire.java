package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicRegenerateAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicSingleActivationCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicTiming;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Skeletal_Vampire {

	public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicManaCost.THREE_BLACK_BLACK.getCondition(),
                MagicCondition.CONTROL_BAT_CONDITION},
			new MagicActivationHints(MagicTiming.Token,true),
            "Token") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			final MagicPlayer player=source.getController();
			return new MagicEvent[]{					
				new MagicPayManaCostEvent(source,player,MagicManaCost.THREE_BLACK_BLACK),
				new MagicSacrificePermanentEvent(source,player,MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			final MagicPlayer player=source.getController();
			return new MagicEvent(
                    source,
                    player,
                    new Object[]{player},
                    this,
                    "Put two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.getInstance().getTokenDefinition("Bat")));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.getInstance().getTokenDefinition("Bat")));			}
	};

	public static final MagicPermanentActivation A2 = new MagicPermanentActivation(
			new MagicCondition[]{
                MagicCondition.CAN_REGENERATE_CONDITION,
                MagicCondition.CONTROL_BAT_CONDITION,
                new MagicSingleActivationCondition()},
			new MagicActivationHints(MagicTiming.Pump),
            "Regen") {

		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{
                new MagicSacrificePermanentEvent(
                        source,
                        source.getController(),
                        MagicTargetChoice.SACRIFICE_BAT)};
		}

		@Override
		public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
			return new MagicEvent(
                    source,
                    source.getController(),
                    new Object[]{source},
                    this,
                    "Regenerate " + source + ".");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
		}
	};
	
    public static final MagicWhenComesIntoPlayTrigger T1 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts two 1/1 black Bat creature tokens with flying onto the battlefield.");
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.getInstance().getTokenDefinition("Bat")));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.getInstance().getTokenDefinition("Bat")));
		}		
    };
}
