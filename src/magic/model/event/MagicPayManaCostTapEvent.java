package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicPayManaCostChoice;

public class MagicPayManaCostTapEvent extends MagicEvent {
	public MagicPayManaCostTapEvent(final MagicSource source,final MagicPlayer player,final MagicManaCost cost) {
		super(
            source,
            player,
            new MagicPayManaCostChoice(cost),
            MagicEvent.NO_DATA,
            new MagicEventAction() {
            @Override
            public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
                event.payManaCost(game,player.map(game),choiceResults,0);
                game.doAction(new MagicTapAction((MagicPermanent)source.map(game),true));
            }},
            "Pay "+cost.getText()+"$. Tap "+source.getName()+".");
	}			
}
