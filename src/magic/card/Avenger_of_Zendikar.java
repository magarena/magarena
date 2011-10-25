package magic.card;

import java.util.Collection;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicSimpleMayChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Avenger_of_Zendikar {
    public static final MagicWhenComesIntoPlayTrigger T1 =new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 0/1 green Plant creature token onto " +
                    "the battlefield for each land he or she controls.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			int amount = player.getNrOfPermanentsWithType(MagicType.Land,game);
			for (;amount>0;amount--) {
				game.doAction(new MagicPlayTokenAction(
						player,
						TokenCardDefinitions.getInstance().getTokenDefinition("Plant")));
			}
		}		
    };
    
    public static final MagicWhenOtherComesIntoPlayTrigger T2 = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPermanent played) {
			final MagicPlayer player = permanent.getController();
			return (player == played.getController() &&
					played.isLand()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new MagicSimpleMayChoice(
                        		player + " may put a +1/+1 counter on each " +
                        		"Plant creature he or she controls.",
                                MagicSimpleMayChoice.ADD_CHARGE_COUNTER,
                                1,
                                MagicSimpleMayChoice.DEFAULT_YES),
                        new Object[]{player},
                        this,
                        player + " may$ put a +1/+1 counter on each " +
                        "Plant creature he or she controls."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				final MagicPlayer player = (MagicPlayer)data[0];
				final Collection<MagicTarget> targets = game.filterTargets(
						player,
						MagicTargetFilter.TARGET_PLANT_YOU_CONTROL);
					for (final MagicTarget target : targets) {
						game.doAction(new MagicChangeCountersAction(
								(MagicPermanent)target,
								MagicCounterType.PlusOne,
								1,
								true));
					}
			}
		}		
    };
}
