package magic.card;

import java.util.Arrays;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicManaType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.trigger.MagicTappedIntoPlayTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Khalni_Garden {
	public static final MagicTappedIntoPlayTrigger T1 = new MagicTappedIntoPlayTrigger();
	
    public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 0/1 green Plant creature token onto the battlefield.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicPlayTokenAction(
					(MagicPlayer)data[0],
					TokenCardDefinitions.getInstance().getTokenDefinition("Plant")));
		}		
    };
    
    public static final MagicManaActivation M = new MagicTapManaActivation(
            Arrays.asList(MagicManaType.Green),1);
}
