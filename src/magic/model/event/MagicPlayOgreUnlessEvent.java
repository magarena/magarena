package magic.model.event;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicPlayTokenAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPlayOgreUnlessEvent extends MagicEvent {

	public MagicPlayOgreUnlessEvent(
            final MagicSource source,
            final MagicPlayer player,
            final MagicPlayer controller,
            final MagicManaCost cost) {
		super(
            source,
            player,
            new MagicMayChoice("You may pay "+cost.getText()+'.',new MagicPayManaCostChoice(cost)),
			MagicEvent.NO_DATA,
            new MagicEventAction() {
            @Override
            public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object choiceResults[]) {
                if (MagicMayChoice.isYesChoice(choiceResults[0])) {
                    event.payManaCost(game,player.map(game),choiceResults,1);
                } else {
                    game.doAction(new MagicPlayTokenAction(controller.map(game),TokenCardDefinitions.OGRE_TOKEN_CARD));
                }
            }},
            "You may$ pay "+cost.getText()+"$.");
	}
}
