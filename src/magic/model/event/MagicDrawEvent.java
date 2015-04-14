package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DrawAction;

public class MagicDrawEvent extends MagicEvent {
    public MagicDrawEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        super(
            source,
            player,
            amount,
            EVENT_ACTION,
            "PN " + genDescription(amount)
        );
    }

    private static final MagicEventAction EVENT_ACTION=new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), event.getRefInt()));
        }
    };

    private static final String genDescription(final int amount) {
        if (amount != 1) {
            return "draws "+amount+" cards.";
        } else {
            return "draws a card.";
        }
    }
}
