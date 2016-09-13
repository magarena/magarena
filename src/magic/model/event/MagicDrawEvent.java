package magic.model.event;

import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.DrawAction;

public class MagicDrawEvent extends MagicEvent {
    public MagicDrawEvent(final MagicSource source, final MagicPlayer player, final int amount, final String desc) {
        super(
            source,
            player,
            amount,
            EVENT_ACTION,
            desc
        );
    }

    public MagicDrawEvent(final MagicSource source,final MagicPlayer player,final int amount) {
        this(source, player, amount, "PN " + genDescription(amount));
    }

    private static final MagicEventAction EVENT_ACTION = (final MagicGame game, final MagicEvent event) ->
        game.doAction(new DrawAction(event.getPlayer(), event.getRefInt()));

    private static final String genDescription(final int amount) {
        if (amount != 1) {
            return "draws "+amount+" cards.";
        } else {
            return "draws a card.";
        }
    }
}
